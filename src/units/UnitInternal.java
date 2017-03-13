package units;

import bwapi.Unit;

public class UnitInternal {
	private Unit unit;
	
	public UnitInternal(Unit unit){
		this.setUnit(unit);
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
}
