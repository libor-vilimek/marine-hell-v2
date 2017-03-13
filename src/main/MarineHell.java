package main;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import game.GameInternal;

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
		// System.out.println("New unit discovered " + unit.getType());
	}

	@Override
	public void onStart() {
		game = mirror.getGame();
		self = game.self();
		game.setLocalSpeed(1);

		// Use BWTA to analyze map
		// This may take a few minutes if the map is processed first time!
		BWTA.readMap();
		BWTA.analyze();
		
		gameInternal = new GameInternal(game);
	}

	@Override
	public void onFrame() {
		gameInternal.updateDesires(self.getUnits());
		gameInternal.executeDesires();
		gameInternal.writeStrategies(game);
		
		// game.setTextSize(10);
		game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());

		for (BaseLocation b : BWTA.getBaseLocations()) {
			Color color;
			if (b.isStartLocation()) {
				color = Color.Green;
			} else {
				color = Color.Yellow;
			}

			game.drawCircleMap(b.getX(), b.getY(), 100, color);
		}

		// iterate through my units
		for (Unit myUnit : self.getUnits()) {
			if (myUnit.getOrderTargetPosition().getX() != 0 && myUnit.getOrderTargetPosition().getY() != 0) {
				game.drawLineMap(myUnit.getPosition(), myUnit.getOrderTargetPosition(), Color.Red);
			}
		}
	}

	public static void main(String[] args) {
		new MarineHell().run();
	}
}