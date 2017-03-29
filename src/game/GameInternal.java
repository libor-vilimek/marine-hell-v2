package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import building.BarrackBuilt;
import building.BuildDesire;
import building.BuildState;
import building.WorkerBuild;
import bwapi.Color;
import bwapi.Game;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import command.RegisterCommandCenter;
import models.ReservedResources;
import models.ResourcesWithDesire;
import spawners.BarracksSpawner;
import spawners.BarracksUnitSpawner;
import spawners.BunkerSpawner;
import spawners.CommandCenterSpawner;
import spawners.Spawner;
import spawners.SupplyLimitSpawner;
import strategy.BuildAndAttack;
import strategy.Desire;
import strategy.Information;
import strategy.WorkerBuilding;
import strategy.WorkerMining;
import units.Barracks;

public class GameInternal {
	Game game;
	List<Desire> desires = new ArrayList<>();
	Map<BuildDesire, Spawner> buildDesires = new HashMap<>();
	Map<Unit, Desire> unitsInDesires = new HashMap<>();
	Map<BuildDesire, ReservedResources> toBuild = new HashMap<>();
	List<Spawner> spawners = new ArrayList<>();

	private ReservedResources actualReservedResources() {
		ReservedResources actualRes = new ReservedResources();
		for (Map.Entry<BuildDesire, ReservedResources> resource : toBuild.entrySet()) {
			if (resource.getKey().getBuildState() == BuildState.NotStarted
					|| resource.getKey().getBuildState() == BuildState.InProgress) {
				actualRes.add(resource.getValue());
			}
		}

		return actualRes;
	}

	public GameInternal(Game game) {
		this.game = game;
		GlobalInformation.initialize(game);
		
		RegisterCommandCenter regCommandCenter = null;
		for (Unit unit : game.self().getUnits()) {
			if (unit.getType() == UnitType.Terran_Command_Center) {
				regCommandCenter = new RegisterCommandCenter(unit, game.neutral().getUnits());
			}
		}

		if (regCommandCenter != null) {
			desires.add(new WorkerMining(regCommandCenter.getCommandCenter()));
			desires.add(new WorkerBuilding(this.buildDesires));
			desires.add(new Information(game));
			desires.add(new BuildAndAttack(game));
			spawners.add(new CommandCenterSpawner(regCommandCenter.getCommandCenter()));
			spawners.add(new SupplyLimitSpawner());
			spawners.add(new BarracksSpawner());
			spawners.add(new BunkerSpawner());
		}
	}

	public void updateDesires(List<Unit> units) {
		for (Unit unit : units) {
			if (!unit.isCompleted()) {
				continue;
			}
			
			Desire maxDesire = null;
			int maxDesireValue = 0;

			for (Desire desire : desires) {
				Desire currentDesire = unitsInDesires.get(unit);
				int desireValue = desire.desire(unit);
				if (currentDesire != null) {
					desireValue -= currentDesire.graspStrength(unit);
				}
				if (maxDesire == null || maxDesireValue < desireValue) {
					maxDesire = desire;
					maxDesireValue = desireValue;
				}
			}

			if (maxDesireValue > 0) {
				boolean accepted = maxDesire.addUnit(unit);

				if (accepted) {
					Desire oldDesire = unitsInDesires.get(unit);
					if (oldDesire != null && !oldDesire.equals(maxDesire)) {
						oldDesire.removeUnit(unit);
					}
					unitsInDesires.put(unit, maxDesire);
				}
			}
		}
	}

	public void updateBuildDesires(Game game) {
		ReservedResources myReservedSources = actualReservedResources();

		List<ResourcesWithDesire> resources = new ArrayList<>();
		for (BuildDesire desire : buildDesires.keySet()) {

			ReservedResources newRes = desire.desire(game);
			if (newRes != null && toBuild.get(desire) == null) {
				resources.add(new ResourcesWithDesire(newRes, desire));
			}
		}

		Collections.sort(resources, new Comparator<ResourcesWithDesire>() {
			@Override
			public int compare(ResourcesWithDesire o1, ResourcesWithDesire o2) {
				return o2.getResources().getPoints() - o1.getResources().getPoints();
			}
		});

		for (ResourcesWithDesire resource : resources) {
			if (myReservedSources.canReserve(resource.getResources(), new ReservedResources(game.self().minerals(),
					game.self().gas(), game.self().supplyTotal() - game.self().supplyUsed(), 0))) {

				myReservedSources.add(resource.getResources());
				toBuild.put(resource.getDesire(), resource.getResources());
			}
		}
	}

	public void updateSpawners(Game game) {
		for (Spawner spawner : spawners) {
			BuildDesire desire = spawner.buildDesire(game);
			if (desire != null) {
				buildDesires.put(desire, spawner);
			}
		}
	}

	public void removeBuildDesires() {
		List<BuildDesire> desireToDelete = new ArrayList<>();
		for (Map.Entry<BuildDesire, Spawner> desire : buildDesires.entrySet()) {
			if (desire.getKey().getBuildState() == BuildState.Finished) {
				desire.getValue().removeDesire(desire.getKey());
				desireToDelete.add(desire.getKey());
			}
		}

		for (BuildDesire desire : desireToDelete) {
			buildDesires.remove(desire);
			toBuild.remove(desire);
		}
	}

	public void executeDesires(Game game) {
		for (Desire desire : desires) {
			desire.execute(game);
		}
	}

	public void executeBuildDesires() {
		for (BuildDesire desire : this.toBuild.keySet()) {
			desire.execute();
		}
	}

	public void unitDiscovered(Unit unit) {
		if (!unit.getPlayer().equals(game.self()) && unit.getType().isBuilding() && !unit.getType().isNeutral()) {
			GlobalInformation.enemyBuildings.put(unit, unit.getPosition());
		}
		
		GlobalInformation.unitDiscovered(unit);
	}

	public void unitDied(Unit unit) {
		Desire desire = unitsInDesires.get(unit);

		if (desire != null) {
			desire.removeUnit(unit);
		}
		
		GlobalInformation.enemyBuildings.remove(unit);
		GlobalInformation.unitDied(unit);
	}

	public void unitCreated(Unit unit) {
		if (unit.getType() == UnitType.Terran_Barracks) {
			spawners.add(new BarracksUnitSpawner(new Barracks(unit)));
		}
	}

	public void writeStrategies(Game game) {
		ReservedResources resources = actualReservedResources();
		if (!GlobalInformation.enemyBuildings.isEmpty()) {
			game.setLocalSpeed(20);
		} else {
			game.setLocalSpeed(0);
		}

		game.drawTextScreen(570, 20, "Time: " + game.elapsedTime());

		game.drawTextScreen(10, 10, "Reserved Resources - Minerals: " + resources.getMinerals() + " Gas: "
				+ resources.getGas() + " Supply: " + resources.getSupply());

		for (Map.Entry<Unit, Desire> entry : unitsInDesires.entrySet()) {
			game.drawTextMap(entry.getKey().getPosition(), entry.getValue().infoText());
		}

		for (Desire desire : desires) {
			desire.specialStrategies(game);
		}

		for (BuildDesire buildDesire : toBuild.keySet()) {
			buildDesire.specialStrategies(game);
		}

		for (Spawner spawner : spawners) {
			spawner.specialStrategies(game);
		}

		for (Position position : GlobalInformation.enemyBuildings.values()) {
			game.drawCircleMap(position, 50, Color.Red);
			if (game.isVisible(position.toTilePosition())){
				game.drawTextMap(position, "Is visible");
			} else {
				game.drawTextMap(position, "Not visible");
			}
		}
		
		
		
		game.drawTextMap(GlobalInformation.getExp().getPosition(), "First expansion");
	}

}
