package building;

import bwapi.Game;
import models.ReservedResources;

public interface BuildDesire {
	public ReservedResources desire(Game game);
	public void execute();
}
