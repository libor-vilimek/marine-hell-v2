package spawners;

import building.BuildDesire;
import building.WorkerBuild;
import bwapi.Game;
import units.CommandCenter;

public class CommandCenterSpawner implements Spawner {
	private BuildDesire buildWorker = null;
	private CommandCenter commandCenter;
	
	public CommandCenterSpawner(CommandCenter commandCenter){
		this.commandCenter = commandCenter;
	}

	@Override
	public BuildDesire buildDesire(Game game) {
		if (buildWorker != null){
			return null;
		}
		
		if (commandCenter.getUnit().getTrainingQueue().isEmpty() && commandCenter.getWorkers().size() < 20){
			return new WorkerBuild(this.commandCenter);
		}
		
		return null;
	}

	@Override
	public void removeDesire(BuildDesire buildDesire) {
		if (buildDesire.equals(this.buildWorker)){
			this.buildWorker = null;
		}
	}

	@Override
	public String infoText() {
		return "Command Center Spawner";
	}

	@Override
	public void specialStrategies(Game game) {
		game.drawTextMap(commandCenter.getUnit().getPosition().getX(), commandCenter.getUnit().getPosition().getY()-10, "Workers: " + this.commandCenter.getWorkers().size());
		
	}	
}
