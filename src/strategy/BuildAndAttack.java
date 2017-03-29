package strategy;

import java.util.ArrayList;
import java.util.List;

import bwapi.Color;
import bwapi.Game;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.Chokepoint;
import game.GlobalInformation;

public class BuildAndAttack implements Desire {
	private Position groupPosition = null;
	List<Unit> units = new ArrayList<>();
	Game game;

	public BuildAndAttack(Game game) {
		this.game = game;
	}

	@Override
	public int desire(Unit unit) {
		if (units.contains(unit)) {
			return 0;
		}
		if (unit.getType() == UnitType.Terran_Marine) {
			return 1000;
		}

		return 0;
	}

	@Override
	public boolean addUnit(Unit unit) {
		if (units.contains(unit)) {
			return false;
		}
		if (unit.getType() == UnitType.Terran_Marine) {
			units.add(unit);
			return true;
		}

		return false;
	}

	@Override
	public void execute(Game game) {
		// TilePosition exp =
		// BWTA.getNearestBaseLocation(BWTA.getStartLocation(game.self()).getTilePosition());

		Position pos;
		if (groupPosition == null) {
			pos = BWTA.getNearestChokepoint(BWTA.getStartLocation(game.self()).getTilePosition()).getCenter();
		} else {
			pos = groupPosition;
		}
		if (units.size() > 50) {

			for (Position position : GlobalInformation.enemyBuildings.values()) {
				pos = position;
			}

		}

		for (Unit unit : units) {
			if (unit.isIdle()) {
				unit.attack(pos);
			}
		}

	}

	@Override
	public void removeUnit(Unit unit) {
		units.remove(unit);
	}

	@Override
	public String infoText() {
		return "Build and Attack";
	}

	@Override
	public void specialStrategies(Game game) {
		if (GlobalInformation.enemyBuildings.isEmpty()) {
			return;
		}

		if (GlobalInformation.route == null) {
			TilePosition pos1 = GlobalInformation.enemyBuildings.values().iterator().next().toTilePosition();
			TilePosition pos2 = GlobalInformation.getExp().getTilePosition();
			GlobalInformation.route = BWTA.getShortestPath(pos2, pos1);
			int away = 15;
			if (GlobalInformation.route.size() <= 15) {
				away = GlobalInformation.route.size() - 1;
			}
			
			GlobalInformation.closestChokePoint = BWTA.getNearestChokepoint(GlobalInformation.route.get(away));
			groupPosition = GlobalInformation.closestChokePoint.getCenter();
			
			pos2 = GlobalInformation.closestChokePoint.getCenter().toTilePosition();
			GlobalInformation.route = BWTA.getShortestPath(pos2, pos1);
		}

		TilePosition before = null;
		int i = 0;
		for (TilePosition tile : GlobalInformation.route) {
			game.drawTextMap(tile.toPosition(), "" + i);
			i++;
			if (before != null) {
				game.drawLineMap(before.toPosition(), tile.toPosition(), Color.Red);
			}
			before = tile;
		}
	}

	@Override
	public int graspStrength(Unit unit) {
		// TODO Auto-generated method stub
		return 100;
	}

}
