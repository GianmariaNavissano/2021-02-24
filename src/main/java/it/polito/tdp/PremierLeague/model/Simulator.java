package it.polito.tdp.PremierLeague.model;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {
	
	//modello
	private Graph<Player, DefaultWeightedEdge> grafo;
	private int nPlayersTHome = 11;
	private int nPlayersTAway = 11;
	private Team tHome;
	private Team tAway;
	private Team bestTeam;
	
	//input
	private int N;
	
	//output
	private int goalTHome;
	private int goalTAway;
	private int espulsiTHome;
	private int espulsiTAway;
	
	public void init(Graph<Player, DefaultWeightedEdge> grafo, int N, Team bestTeam, Team tHome, Team tAway) {
		this.grafo = grafo;
		this.N = N;
		this.bestTeam = bestTeam;
		this.tAway = tAway;
		this.tHome = tHome;
		
		
		this.goalTHome = 0;
		this.goalTAway = 0;
		this.espulsiTHome = 0;
		this.espulsiTAway = 0;
		
	}
	
	public void run() {
		for(int i = 0; i<N; i++) {
			int p = (int)(Math.random()*100);
			if(p<50) {
				//goal
				if(this.nPlayersTHome>this.nPlayersTAway) {
					this.goalTHome++;
				}else if(this.nPlayersTHome<this.nPlayersTAway) {
					this.goalTAway++;
				}else {
					if(bestTeam.equals(tHome)) {
						this.goalTHome++;
					}else if(bestTeam.equals(tAway)) {
						this.goalTAway++;
					}
				}
			} else if(p>=50 && p<80) {
				//espulsione
				int p2 = (int)(Math.random()*100);
				if(p2<60) {
					//espulso giocatore della squadra migliore
					if(bestTeam.equals(tHome)) {
						this.espulsiTHome++;
						this.nPlayersTHome--;
					}else if(bestTeam.equals(tAway)) {
						this.espulsiTAway++;
						this.nPlayersTAway--;
					}
				} else {//espulso il giocatore della squadra peggiore
					if(bestTeam.equals(tHome)) {
						this.espulsiTAway++;
						this.nPlayersTAway--;
					}else if(bestTeam.equals(tAway)) {
						this.espulsiTHome++;
						this.nPlayersTHome--;
					}
				}
			}else {
				//infortunio
				int p3 = (int)(Math.random()*100);
				if(p3<50) {
					//espulso giocatore T1
					this.espulsiTHome++;
					this.nPlayersTHome--;
				}else {
					//espuslo giocatore T2
					this.espulsiTAway++;
					this.nPlayersTAway--;
				}
				int p4 = (int)(Math.random()*100);
				if(p4<50)
					this.N +=2;
				else this.N += 3;
			}
		}
	}

	public int getGoalT1() {
		return goalTHome;
	}

	public int getGoalT2() {
		return goalTAway;
	}

	public int getEspulsiT1() {
		return espulsiTHome;
	}

	public int getEspulsiT2() {
		return espulsiTAway;
	}

	public Team getTHome() {
		return tHome;
	}

	public Team getTAway() {
		return tAway;
	}
	
	
	
	
	
}
