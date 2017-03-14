package building;

import bwapi.Game;
import bwapi.UnitType;
import models.ReservedResources;
import strategy.WorkerBuilding;

public class SupplyDepotBuild implements BuildDesire {	
	private BuildState buildState = BuildState.NotStarted;
	
	@Override
	public ReservedResources desire(Game game) {
		if (game.self().minerals() >= 100){
			return new ReservedResources(100, 0, 0, 5000);
		}
		
		return null;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BuildState getBuildState() {
		return buildState;
	}

	@Override
	public String infoText() {
		return "Supply depot";
	}

	@Override
	public void specialStrategies(Game game) {
		game.drawTextScreen(10, 20, "Supply depot desire");
	}

	@Override
	public BuildingOrUnit buildingOrUnit() {
		return BuildingOrUnit.Building;
	}

	@Override
	public UnitType getUnitType() {
		return UnitType.Terran_Supply_Depot;
	}

	@Override
	public void setBuildState(BuildState buildState) {
		this.buildState = buildState;
	}

}
