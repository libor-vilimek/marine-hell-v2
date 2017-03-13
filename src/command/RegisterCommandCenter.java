package command;

import java.util.List;

import bwapi.Unit;
import bwta.BWTA;
import units.CommandCenter;

public class RegisterCommandCenter implements BaseCommand {
	private CommandCenter commandCenter;
	
	public RegisterCommandCenter(Unit commandCenter, List<Unit> allUnits) {
		Unit bestMineral = null;
		double bestDistance = Double.MAX_VALUE;
		for (Unit unit : allUnits) {
			if (unit.getType().isMineralField()) {
				double distance = BWTA.getGroundDistance(commandCenter.getTilePosition(), unit.getTilePosition());
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
