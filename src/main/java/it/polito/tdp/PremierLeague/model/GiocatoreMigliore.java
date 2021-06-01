package it.polito.tdp.PremierLeague.model;

public class GiocatoreMigliore {
	
	private Player P;
	double delta;
	public GiocatoreMigliore(Player p, double delta) {
		super();
		P = p;
		this.delta = delta;
	}
	public Player getP() {
		return P;
	}
	public void setP(Player p) {
		P = p;
	}
	public double getDelta() {
		return delta;
	}
	public void setDelta(double delta) {
		this.delta = delta;
	}
	@Override
	public String toString() {
		return "GiocatoreMigliore: " + P + " delta= " + delta;
	}
	
	

}
