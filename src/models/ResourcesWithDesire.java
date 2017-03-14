package models;

import building.BuildDesire;

public class ResourcesWithDesire {
	private ReservedResources resources;
	private BuildDesire desire;
	
	public ResourcesWithDesire(ReservedResources resources, BuildDesire desire){
		this.resources = resources;
		this.desire = desire;
	}

	public ReservedResources getResources() {
		return resources;
	}

	public void setResources(ReservedResources resources) {
		this.resources = resources;
	}

	public BuildDesire getDesire() {
		return desire;
	}

	public void setDesire(BuildDesire desire) {
		this.desire = desire;
	}
}
