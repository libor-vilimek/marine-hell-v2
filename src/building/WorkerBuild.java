package building;

import bwapi.Game;
import bwapi.UnitType;
import models.ReservedResources;
import units.CommandCenter;

public class WorkerBuild implements BuildDesire {
	private CommandCenter commandCenter;
	private BuildState buildState = BuildState.NotStarted;

	public WorkerBuild(CommandCenter commandCenter) {
		this.commandCenter = commandCenter;
	}

	@Override
	public ReservedResources desire(Game game) {
		if (this.commandCenter.getWorkers().size() < 20 && this.commandCenter.getUnit().getTrainingQueue().isEmpty()) {
			return new ReservedResources(50, 0, 0, 500);
		}

		return null;
	}

	@Override
	public void execute() {
		if (buildState == BuildState.NotStarted) {
			this.commandCenter.getUnit().build(UnitType.Terran_SCV);
		}
		buildState = BuildState.Finished;
	}

	@Override
	public BuildState getBuildState() {
		return buildState;
	}

	@Override
	public String infoText() {
		return "Build worker";
	}

	@Override
	public void specialStrategies(Game game) {
		game.drawTextMap(this.commandCenter.getUnit().getPosition().getX(), this.commandCenter.getUnit().getPosition().getY()+10, "Build worker: " + this.buildState);
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
