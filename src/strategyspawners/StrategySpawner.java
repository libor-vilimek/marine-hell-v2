package strategyspawners;

import bwapi.Game;
import strategy.Desire;

public interface StrategySpawner {
	public Desire buildDesire(Game game);
	public void removeDesire(Desire desire);
	public String infoText();
	public void specialStrategies(Game game);
}
