package spawners;

import building.BuildDesire;
import bwapi.Game;

public interface Spawner {
	public BuildDesire buildDesire(Game game);
	public void removeDesire(BuildDesire buildDesire);
	public String infoText();
	public void specialStrategies(Game game);
}
