package building;

import bwapi.Game;
import bwapi.UnitType;
import models.ReservedResources;
import units.CommandCenter;

public class WorkerBuild implements BuildDesire {
	private CommandCenter commandCenter;
	
	public WorkerBuild(CommandCenter commandCenter){
		this.commandCenter = commandCenter;
	}
	
	@Override
	public ReservedResources desire(Game game) {		
		if (this.commandCenter.getWorkers().size() < 20 && this.commandCenter.getUnit().getTrainingQueue().isEmpty()){
			return new ReservedResources(50, 0, 0, 500);
		}
		
		
		return null;
	}

	@Override
	public void execute() {
		this.commandCenter.getUnit().build(UnitType.Terran_SCV);
	}
}
