package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import building.BuildDesire;
import building.WorkerBuild;
import bwapi.Game;
import bwapi.Unit;
import bwapi.UnitType;
import command.RegisterCommandCenter;
import strategy.Desire;
import strategy.WorkerMining;

public class GameInternal {
	List<Desire> desires = new ArrayList<>();
	List<BuildDesire> buildDesires = new ArrayList<>();
	Map<Unit, Desire> unitsInDesires = new HashMap<>();

	public GameInternal(Game game) {
		RegisterCommandCenter regCommandCenter = null;
		for (Unit unit : game.self().getUnits()) {
			if (unit.getType() == UnitType.Terran_Command_Center) {
				regCommandCenter = new RegisterCommandCenter(unit, game.neutral().getUnits());
			}
		}

		if (regCommandCenter != null) {
			desires.add(new WorkerMining(regCommandCenter.getCommandCenter()));
			buildDesires.add(new WorkerBuild(regCommandCenter.getCommandCenter()));
		}
	}

	public void updateDesires(List<Unit> units) {
		for (Unit unit : units) {
			Desire maxDesire = null;
			int maxDesireValue = 0;

			for (Desire desire : desires) {
				int desireValue = desire.desire(unit);
				if (maxDesire == null || maxDesireValue > desireValue) {
					maxDesire = desire;
					maxDesireValue = desireValue;
				}
			}

			if (maxDesireValue > 0) {
				maxDesire.addUnit(unit);
				unitsInDesires.put(unit, maxDesire);

				Desire oldDesire = unitsInDesires.get(unit);

				if (oldDesire != null && !oldDesire.equals(maxDesire)) {
					oldDesire.removeUnit(unit);
				}
				
				
			}
		}
	}
	
	public void updateBuildDesires(Game game){
		for (BuildDesire desire : buildDesires){
			desire.desire(game);
		}
	}

	public void executeDesires() {
		for (Desire desire : desires) {
			desire.execute();
		}
	}

	public void unitDied(Unit unit) {
		Desire desire = unitsInDesires.get(unit);

		if (desire != null) {
			desire.removeUnit(unit);
		}
	}
	
	public void writeStrategies(Game game){
		for (Map.Entry<Unit, Desire> entry : unitsInDesires.entrySet()){
			game.drawTextMap(entry.getKey().getPosition(), entry.getValue().infoText());
		}
		
		for (Desire desire : desires){
			desire.specialStrategies(game);
		}
	}
}
