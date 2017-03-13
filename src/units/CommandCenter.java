package units;

import java.util.ArrayList;
import java.util.List;

import bwapi.Unit;

public class CommandCenter extends UnitInternal {
	private List<Unit> workers = new ArrayList<>();
	private Unit closestMineralField;

	public CommandCenter(Unit unit, Unit closestMineralField) {
		super(unit);
		this.closestMineralField = closestMineralField;
	}

	public List<Unit> getWorkers() {
		return workers;
	}

	public Unit getClosestMineralField() {
		return closestMineralField;
	}
	
	public void addNewWorker(Unit unit){
		workers.add(unit);
		// unit.gather(closestMineralField);
	}
	
	public void removeWorker(Unit unit){
		workers.remove(unit);
	}
}
