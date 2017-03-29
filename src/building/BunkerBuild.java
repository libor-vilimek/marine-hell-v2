package building;

import bwapi.Game;
import bwapi.UnitType;
import game.GlobalInformation;
import models.ReservedResources;

public class BunkerBuild implements BuildDesire {
private BuildState buildState = BuildState.NotStarted;
	
	@Override
	public ReservedResources desire(Game game) {
		if (game.self().minerals() >= 100 && GlobalInformation.route != null && GlobalInformation.numOfBuildings(UnitType.Terran_Bunker) == 0 && GlobalInformation.numOfBuildings(UnitType.Terran_Barracks) >= 1){
			return new ReservedResources(100, 0, 0, 4000);
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
		return "Bunker";
	}

	@Override
	public void specialStrategies(Game game) {

	}

	@Override
	public BuildingOrUnit buildingOrUnit() {
		return BuildingOrUnit.Building;
	}

	@Override
	public UnitType getUnitType() {
		return UnitType.Terran_Bunker;
	}

	@Override
	public void setBuildState(BuildState buildState) {
		this.buildState = buildState;
	}
}
