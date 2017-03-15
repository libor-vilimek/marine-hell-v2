package spawners;

import java.util.ArrayList;
import java.util.List;

import building.BarrackBuilt;
import building.BuildDesire;
import building.SupplyDepotBuild;
import bwapi.Game;

public class BarracksSpawner implements Spawner {
	List<BuildDesire> buildDesires = new ArrayList<>();

	@Override
	public BuildDesire buildDesire(Game game) {
		if (game.self().supplyTotal() > 10 && game.self().minerals() >= 150 && buildDesires.isEmpty()){
			BuildDesire desire = new BarrackBuilt();
			buildDesires.add(desire);
			return desire;
		}
		
		return null;
	}

	@Override
	public void removeDesire(BuildDesire buildDesire) {
		buildDesires.remove(buildDesire);
	}

	@Override
	public String infoText() {
		return "Spawn barracks desire";
	}

	@Override
	public void specialStrategies(Game game) {
		// TODO Auto-generated method stub
		
	}
}
