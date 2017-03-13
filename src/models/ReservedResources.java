package models;

public class ReservedResources {
	private int minerals;
	private int gas;
	private int supply;
	private int points;
	
	public ReservedResources(){
		this.minerals = 0;
		this.gas = 0;
		this.supply = 0;
		this.points = 0;
	}
	
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
	
	public boolean canReserve(ReservedResources newResources, ReservedResources maxResources){
		if (this.minerals + newResources.minerals > maxResources.minerals){
			return false;
		}
		if (this.gas + newResources.gas > maxResources.gas){
			return false;
		}
		if (this.supply + newResources.supply > maxResources.supply){
			return false;
		}
		return true;
	}
	
	public void add(ReservedResources newResources) {
		this.minerals = this.minerals + newResources.getMinerals();
		this.gas = this.gas + newResources.getGas();
		this.supply = this.supply + newResources.getSupply();
	}
}
