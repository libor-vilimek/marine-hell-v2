package strategy;

import java.util.ArrayList;
import java.util.List;

import bwapi.Game;
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

		if (unit.getType().isWorker() && unit.isIdle() && !commandCenter.getWorkers().contains(unit)) {
			double distance = BWTA.getGroundDistance(unit.getTilePosition(),
					commandCenter.getUnit().getTilePosition());
			return 200;
		}

		return 0;
	}

	@Override
	public boolean addUnit(Unit unit) {
		if (commandCenter.getWorkers().contains(unit)){
			return false;
		}
		commandCenter.addNewWorker(unit);
		return true;
	}

	@Override
	public void execute(Game game) {
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

	@Override
	public void specialStrategies(Game game) {
		this.commandCenter.writeStrategies(game);
	}

	@Override
	public int graspStrength(Unit unit) {
		return 10;
	}
}
