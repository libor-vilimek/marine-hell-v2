package command;

import java.util.List;

import bwapi.Unit;
import bwta.BWTA;
import units.CommandCenter;

public class RegisterCommandCenter implements BaseCommand {
	private CommandCenter commandCenter;
	
	public RegisterCommandCenter(Unit commandCenter, List<Unit> allUnits) {
		Unit bestMineral = null;
		int bestDistance = 0;
		for (Unit unit : allUnits) {
			if (unit.getType().isMineralField()) {
				int distance = commandCenter.getDistance(unit);
				if (bestMineral == null || distance < bestDistance) {
					bestMineral = unit;
					bestDistance = distance;
				}
			}
		}
		
		this.commandCenter = new CommandCenter(commandCenter, bestMineral);		
	}

	@Override
	public void execute() {
		
	}

	public CommandCenter getCommandCenter() {
		return commandCenter;
	}
}
