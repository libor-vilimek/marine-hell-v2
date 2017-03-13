package strategy;

import java.util.List;

import bwapi.Unit;
import units.UnitInternal;

public interface Desire {
	public int desire(Unit unit);
	public void addUnit(Unit unit);
	public void execute();
	public void removeUnit(Unit unit);
	public String infoText();
}
