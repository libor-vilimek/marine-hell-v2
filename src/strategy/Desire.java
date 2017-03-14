package strategy;

import java.util.List;

import bwapi.Game;
import bwapi.Unit;
import units.UnitInternal;

public interface Desire {
	public int desire(Unit unit);
	public boolean addUnit(Unit unit);
	public void execute(Game game);
	public void removeUnit(Unit unit);
	public String infoText();
	public void specialStrategies(Game game);
	public int graspStrength(Unit unit);
}
