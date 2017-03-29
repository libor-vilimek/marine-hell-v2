package building;

import bwapi.Game;
import bwapi.UnitType;
import models.ReservedResources;
import units.Barracks;
import units.CommandCenter;

public class BarracksBuild implements BuildDesire {
	private Barracks barracks;
	private BuildState buildState = BuildState.NotStarted;

	public BarracksBuild(Barracks barracks) {
		this.barracks = barracks;
	}

	@Override
	public ReservedResources desire(Game game) {
		if (game.self().minerals() > 50) {
			return new ReservedResources(50, 0, 0, 500);
		}

		return null;
	}

	@Override
	public void execute() {
		if (buildState == BuildState.NotStarted) {
			this.barracks.getUnit().build(UnitType.Terran_Marine);
		}

		if (this.barracks.getUnit().getTrainingQueue().isEmpty()) {
			if (buildState != BuildState.NotStarted) {
				buildState = BuildState.Finished;
			}
		} else {
			if (buildState == BuildState.NotStarted) {
				this.buildState = BuildState.InConstruction;
			}
		}
	}

	@Override
	public BuildState getBuildState() {
		return buildState;
	}

	@Override
	public String infoText() {
		return "Build marine";
	}

	@Override
	public void specialStrategies(Game game) {
		game.drawTextMap(this.barracks.getUnit().getPosition().getX(),
				this.barracks.getUnit().getPosition().getY() + 10, "Build marine: " + this.buildState);
	}

	@Override
	public BuildingOrUnit buildingOrUnit() {
		return BuildingOrUnit.Unit;
	}

	@Override
	public UnitType getUnitType() {
		return UnitType.Terran_SCV;
	}

	@Override
	public void setBuildState(BuildState buildState) {
		this.buildState = buildState;
	}
}
