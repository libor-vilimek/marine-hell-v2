package spawners;

import java.util.ArrayList;
import java.util.List;

import building.BuildDesire;
import building.SupplyDepotBuild;
import bwapi.Game;

public class SupplyLimitSpawner implements Spawner {
	List<BuildDesire> buildDesires = new ArrayList<>();

	@Override
	public BuildDesire buildDesire(Game game) {
		int supplyLeft = game.self().supplyTotal() - game.self().supplyUsed();
		boolean add = false;
		
		if (buildDesires.size() > 3){
			return null;
		}
		
		if (buildDesires.isEmpty() && supplyLeft <= 4){
			add = true;
		}
		
		if (buildDesires.size() == 1 && supplyLeft <= 4 && game.self().supplyTotal() > 25){
			add = true;
		}
		
		if (buildDesires.size() == 2 && supplyLeft <= 4 && game.self().supplyTotal() > 50){
			add = true;
		}
		
		if (add){
			BuildDesire desire = new SupplyDepotBuild();
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
		return "Spawn supply depot desire";
	}

	@Override
	public void specialStrategies(Game game) {
		// TODO Auto-generated method stub
		
	}
}
