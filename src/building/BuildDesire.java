package building;

import bwapi.Game;
import bwapi.UnitType;
import models.ReservedResources;

public interface BuildDesire {
	public ReservedResources desire(Game game);
	public void execute();
	public BuildState getBuildState();
	public void setBuildState(BuildState buildState);
	public String infoText();
	public void specialStrategies(Game game);
	public BuildingOrUnit buildingOrUnit();
	public UnitType getUnitType();
}
