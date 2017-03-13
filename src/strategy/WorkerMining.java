package strategy;

import java.util.ArrayList;
import java.util.List;

import bwapi.Unit;
import bwta.BWTA;
import units.CommandCenter;
import units.UnitInternal;
import units.Worker;

public class WorkerMining implements Desire {
	CommandCenter commandCenter;

	public WorkerMining(CommandCenter commandCenter) {
		this.commandCenter = commandCenter;
	}

	@Override
	public int desire(Unit unit) {
		if (commandCenter.getWorkers().size() >= 20) {
			return 0;
		}

		if (unit.getType().isWorker() && unit.isIdle()) {
			double distance = BWTA.getGroundDistance(unit.getTilePosition(),
					commandCenter.getUnit().getTilePosition());
			return 200;
		}

		return 0;
	}

	@Override
	public void addUnit(Unit unit) {
		commandCenter.addNewWorker(unit);
	}

	@Override
	public void execute() {
		List<Unit> workers = commandCenter.getWorkers();
		for (Unit worker : workers){
			if (!worker.isGatheringMinerals()){
				worker.gather(commandCenter.getClosestMineralField());
			}
		}
	}

	@Override
	public void removeUnit(Unit unit) {
		commandCenter.removeWorker(unit);
	}

	@Override
	public String infoText() {
		return "Gathering resources";
	}
}
