package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Game;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;
import units.Barracks;

public class GlobalInformation {
	private static BaseLocation savedExp = null;
	public static List<Unit> myBuildings = new ArrayList<>();	
	public static Map<Unit, Position> enemyBuildings = new HashMap<>();
	public static Game game;
	public static List<TilePosition> route = null;
	public static Chokepoint closestChokePoint = null;
	
	public static int numOfBuildings(UnitType type){
		int count = 0;
		for (Unit unit : myBuildings){
			if (unit.getType() == type){
				count++;
			}
		}
		
		return count;
	}
	
	public static BaseLocation getMyStartLocation() {
		return BWTA.getStartLocation(game.self());
	}
	
	public static void unitDiscovered(Unit unit){
		if (unit.getPlayer().equals(game.self())){
			myBuildings.add(unit);
		}
	}
	
	public static void unitDied(Unit unit){
		if (unit.getPlayer().equals(game.self())){
			myBuildings.remove(unit);
		}
	}
	
	public static BaseLocation getExp() {
		if (savedExp != null){
			return savedExp;
		}
		
		BaseLocation exp = null;
		double minValue = Double.MAX_VALUE;
		for (BaseLocation baseLocation : BWTA.getBaseLocations()){
			if (getMyStartLocation().getPosition().equals(baseLocation.getPosition())){
				continue;
			}
			double value = getMyStartLocation().getGroundDistance(baseLocation);
			if ( (exp == null) || minValue > value){
				exp = baseLocation;
				minValue = value;
			}
		}
		savedExp = exp;
		return exp;
	}
	public static void initialize(Game game){
		enemyBuildings.clear();	
		enemyBuildings = new HashMap<>();
		GlobalInformation.game = game;
		savedExp = null;
	}
}
