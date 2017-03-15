package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import building.BuildDesire;
import building.BuildState;
import building.BuildingOrUnit;
import bwapi.Game;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import spawners.Spawner;

public class WorkerBuilding implements Desire {
	Map<BuildDesire, Spawner> buildDesires;
	List<Unit> units = new ArrayList<>();
	Map<Unit, BuildDesire> unitsHasBuildDesires = new HashMap<>();
	Map<Unit, Unit> unitsHasBuildingsConstructed = new HashMap<>();

	public WorkerBuilding(Map<BuildDesire, Spawner> buildDesires) {
		this.buildDesires = buildDesires;
	}

	@Override
	public int desire(Unit unit) {
		BuildDesire buildDesire = this.notStartedDesire();
		if (unit.getType().isWorker() && buildDesire != null && !units.contains(unit)) {
			return 5000;
		}

		return 0;
	}

	@Override
	public boolean addUnit(Unit unit) {
		BuildDesire buildDesire = notStartedDesire();
		if (buildDesire != null && !units.contains(unit)) {
			this.units.add(unit);
			unitsHasBuildDesires.put(unit, buildDesire);
			buildDesire.setBuildState(BuildState.InProgress);

			return true;
		}

		return false;
	}

	@Override
	public void execute(Game game) {
		List<Unit> toRemove = new ArrayList<>();
		for (Map.Entry<Unit, BuildDesire> entry : unitsHasBuildDesires.entrySet()) {
			Unit unit = entry.getKey();
			BuildDesire buildDesire = entry.getValue();

			if ( (unit.isGatheringMinerals() || unit.isIdle()) && buildDesire.getBuildState() == BuildState.InProgress) {
				TilePosition tilePosition = getBuildTile(unit, buildDesire.getUnitType(), unit.getTilePosition(), game);
				unit.build(buildDesire.getUnitType(), tilePosition);
			}
			
			if (unit.isConstructing() && unit.canHaltConstruction()){
				buildDesire.setBuildState(BuildState.InConstruction);
				Unit buildUnit = unit.getBuildUnit();
				if (buildUnit != null){
					unitsHasBuildingsConstructed.put(unit, buildUnit);
				}
			}
			
			Unit buildUnit = unitsHasBuildingsConstructed.get(unit);
			if (buildUnit != null && buildUnit.isCompleted() && buildDesire.getBuildState() == BuildState.InConstruction){
				buildDesire.setBuildState(BuildState.Finished);
				toRemove.add(unit);
			}			
		}
		
		for (Unit unit : toRemove){
			this.removeUnit(unit);
		}
	}

	@Override
	public void removeUnit(Unit unit) {
		this.units.remove(unit);
		BuildDesire buildState = this.unitsHasBuildDesires.get(unit);
		this.unitsHasBuildDesires.remove(unit);
		if (buildState != null) {
			buildState.setBuildState(BuildState.Finished);
		}
		this.unitsHasBuildingsConstructed.remove(unit);
	}

	@Override
	public String infoText() {
		return "Worker makes buildings";
	}

	@Override
	public void specialStrategies(Game game) {
		int i = 0;
		game.drawTextScreen(10, 30, "Build Desires:");
		for (BuildDesire buildDesire : buildDesires.keySet()) {
			game.drawTextScreen(10, 40 + i * 10, buildDesire.infoText() + " - " + buildDesire.getBuildState());
			i++;
		}
	}

	public BuildDesire notStartedDesire() {
		for (BuildDesire buildDesire : buildDesires.keySet()) {
			if (buildDesire.buildingOrUnit() == BuildingOrUnit.Building
					&& buildDesire.getBuildState() == BuildState.NotStarted) {
				return buildDesire;
			}
		}

		return null;
	}

	@Override
	public int graspStrength(Unit unit) {
		if (!units.contains(unit)){
			return 0;
		}
		return 10000;
	}

	// Returns a suitable TilePosition to build a given building type near
	// specified TilePosition aroundTile, or null if not found. (builder
	// parameter is our worker)
	public TilePosition getBuildTile(Unit builder, UnitType buildingType, TilePosition aroundTile, Game game) {
		TilePosition ret = null;
		int maxDist = 3;
		int stopDist = 40;

		// Refinery, Assimilator, Extractor
		if (buildingType.isRefinery()) {
			for (Unit n : game.neutral().getUnits()) {
				if ((n.getType() == UnitType.Resource_Vespene_Geyser)
						&& (Math.abs(n.getTilePosition().getX() - aroundTile.getX()) < stopDist)
						&& (Math.abs(n.getTilePosition().getY() - aroundTile.getY()) < stopDist))
					return n.getTilePosition();
			}
		}

		while ((maxDist < stopDist) && (ret == null)) {
			for (int i = aroundTile.getX() - maxDist; i <= aroundTile.getX() + maxDist; i++) {
				for (int j = aroundTile.getY() - maxDist; j <= aroundTile.getY() + maxDist; j++) {
					if (game.canBuildHere(new TilePosition(i, j), buildingType, builder, false)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : game.getAllUnits()) {
							if (u.getID() == builder.getID())
								continue;
							if ((Math.abs(u.getTilePosition().getX() - i) < 4)
									&& (Math.abs(u.getTilePosition().getY() - j) < 4))
								unitsInWay = true;
						}
						if (!unitsInWay) {
							return new TilePosition(i, j);
						}
						// creep for Zerg
						if (buildingType.requiresCreep()) {
							boolean creepMissing = false;
							for (int k = i; k <= i + buildingType.tileWidth(); k++) {
								for (int l = j; l <= j + buildingType.tileHeight(); l++) {
									if (!game.hasCreep(k, l))
										creepMissing = true;
									break;
								}
							}
							if (creepMissing)
								continue;
						}
					}
				}
			}
			maxDist += 2;
		}

		if (ret == null)
			game.printf("Unable to find suitable build position for " + buildingType.toString());
		return ret;
	}
}
