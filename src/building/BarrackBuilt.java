package building;

import bwapi.Game;
import bwapi.UnitType;
import models.ReservedResources;

public class BarrackBuilt implements BuildDesire {
private BuildState buildState = BuildState.NotStarted;
	
	@Override
	public ReservedResources desire(Game game) {
		if (game.self().minerals() >= 150){
			return new ReservedResources(150, 0, 0, 1000);
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
		return "Barracks";
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
		return UnitType.Terran_Barracks;
	}

	@Override
	public void setBuildState(BuildState buildState) {
		this.buildState = buildState;
	}
}
