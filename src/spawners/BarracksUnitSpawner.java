package spawners;

import java.util.ArrayList;
import java.util.List;

import building.BarrackBuilt;
import building.BarracksBuild;
import building.BuildDesire;
import building.SupplyDepotBuild;
import building.WorkerBuild;
import bwapi.Game;
import units.Barracks;
import units.CommandCenter;

public class BarracksUnitSpawner implements Spawner {
	private BuildDesire buildMarine = null;
	private Barracks barracks;
	
	public BarracksUnitSpawner(Barracks barracks){
		this.barracks = barracks;
	}

	@Override
	public BuildDesire buildDesire(Game game) {
		if (buildMarine != null){
			return null;
		}
		
		if (barracks.getUnit().getTrainingQueue().isEmpty() ){
			buildMarine = new BarracksBuild(this.barracks);
			return buildMarine;
		}
		
		return null;
	}

	@Override
	public void removeDesire(BuildDesire buildDesire) {
		if (buildDesire.equals(this.buildMarine)){
			this.buildMarine = null;
		}
	}

	@Override
	public String infoText() {
		return "Barracks spawner";
	}

	@Override
	public void specialStrategies(Game game) {
		
	}	
}
