package main;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;
import game.GameInternal;
import game.GlobalInformation;

public class MarineHell extends DefaultBWListener {

	private Mirror mirror = new Mirror();

	private Game game;
	private GameInternal gameInternal;

	private Player self;

	public void run() {
		mirror.getModule().setEventListener(this);
		mirror.startGame();
	}

	@Override
	public void onUnitCreate(Unit unit) {
		gameInternal.unitCreated(unit);
	}
	
	@Override
	public void onUnitDestroy(Unit unit) {
		gameInternal.unitDied(unit);
	}

	@Override
	public void onUnitDiscover(Unit unit) {
		gameInternal.unitDiscovered(unit);
	}
	
	@Override
	public void onStart() {
		game = mirror.getGame();
		self = game.self();
		game.setLocalSpeed(0);

		// Use BWTA to analyze map
		// This may take a few minutes if the map is processed first time!
		BWTA.readMap();
		BWTA.analyze();

		gameInternal = new GameInternal(game);
	}

	@Override
	public void onFrame() {
		try {
			gameInternal.updateDesires(self.getUnits());
			gameInternal.executeDesires(game);
			gameInternal.writeStrategies(game);

			gameInternal.updateSpawners(game);
			gameInternal.removeBuildDesires();
			gameInternal.updateBuildDesires(game);
			gameInternal.executeBuildDesires();


			for (BaseLocation b : BWTA.getBaseLocations()) {
				Color color;
				if (b.isStartLocation()) {
					color = Color.Green;
				} else {
					color = Color.Yellow;
				}

				game.drawCircleMap(b.getX(), b.getY(), 100, color);
			}
			
			for (Chokepoint chokePoint: BWTA.getChokepoints()){
				game.drawTextMap(chokePoint.getCenter(), "Chokepoint");
			}

			// iterate through my units
			for (Unit myUnit : self.getUnits()) {
				if (myUnit.getOrderTargetPosition().getX() != 0 && myUnit.getOrderTargetPosition().getY() != 0) {
					game.drawLineMap(myUnit.getPosition(), myUnit.getOrderTargetPosition(), Color.Red);
				}
			}
		} catch (Exception err) {
			System.out.println(err.getMessage());
			err.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new MarineHell().run();
	}
}