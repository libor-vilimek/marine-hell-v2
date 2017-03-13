package models;

public class ReservedResources {
	private int minerals;
	private int gas;
	private int supply;
	private int points;
	
	public ReservedResources(int minerals, int gas, int supply,int points){
		this.minerals = minerals;
		this.gas = gas;
		this.supply = supply;
		this.points = points;
	}
	
	public int getMinerals() {
		return minerals;
	}
	public void setMinerals(int minerals) {
		this.minerals = minerals;
	}
	public int getGas() {
		return gas;
	}
	public void setGas(int gas) {
		this.gas = gas;
	}

	public int getSupply() {
		return supply;
	}

	public void setSupply(int supply) {
		this.supply = supply;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
