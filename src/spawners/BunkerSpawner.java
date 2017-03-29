package spawners;

import java.util.ArrayList;
import java.util.List;

import building.BarrackBuilt;
import building.BuildDesire;
import building.BunkerBuild;
import bwapi.Game;
import bwapi.UnitType;
import game.GlobalInformation;

public class BunkerSpawner implements Spawner {
	List<BuildDesire> buildDesires = new ArrayList<>();

	@Override
	public BuildDesire buildDesire(Game game) {
		if (game.self().supplyTotal() > 20 && GlobalInformation.numOfBuildings(UnitType.Terran_Bunker) == 0 && buildDesires.isEmpty() && GlobalInformation.closestChokePoint != null){
			BuildDesire desire = new BunkerBuild();
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
		return "Spawn bunker desire";
	}

	@Override
	public void specialStrategies(Game game) {
		// TODO Auto-generated method stub
		
	}	
}
