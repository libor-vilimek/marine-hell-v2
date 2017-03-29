package strategy;

import java.util.ArrayList;
import java.util.List;

import bwapi.Game;
import bwapi.Unit;
import bwta.BWTA;
import bwta.BaseLocation;
import game.GlobalInformation;

public class Information implements Desire {
	private List<Unit> units = new ArrayList<Unit>();
	private Game game;
	private List<BaseLocation> startLocations;
	private int currentLocation = 0;

	public Information(Game game) {
		this.game = game;
		this.startLocations = BWTA.getStartLocations();
	}

	@Override
	public int desire(Unit unit) {
		if (!GlobalInformation.enemyBuildings.isEmpty()){
			return 0;
		}
		
		if (unit.getType().isWorker() && game.self().supplyUsed() > 14 && units.isEmpty()) {
			return 5000;
		}

		return 0;
	}

	@Override
	public boolean addUnit(Unit unit) {
		if (!units.contains(unit)) {
			this.units.add(unit);
			return true;
		}

		return false;
	}

	@Override
	public void execute(Game game) {
		if (units.isEmpty()) {
			return;
		}

		Unit unit = units.get(0);
		if (unit.isGatheringMinerals() || unit.isIdle()) {
			unit.move(startLocations.get(currentLocation % startLocations.size()).getPosition());
			currentLocation++;
		}
	}

	@Override
	public void removeUnit(Unit unit) {
		this.units.remove(unit);
	}

	@Override
	public String infoText() {
		return "Information";
	}

	@Override
	public void specialStrategies(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public int graspStrength(Unit unit) {
		if (GlobalInformation.enemyBuildings.isEmpty()){
			return 10000;
		}
		return 0;
	}

}
