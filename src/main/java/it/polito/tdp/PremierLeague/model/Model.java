package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	private Map<Integer, Team> teams;
	private Simulator sim;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<Integer, Player>();
		this.dao.listAllPlayers(idMap);
		this.teams = new HashMap<Integer, Team>();
		this.dao.listAllTeams(teams);
		this.sim = new Simulator();
	}
	
	public void creaGrafo(Match m) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		Graphs.addAllVertices(grafo, this.dao.getVertici(m, idMap, teams));
		
		//aggiungo gli archi pesati
		for(Adiacenza a : this.dao.getEdges(m, idMap)) {
			if(a.getPeso()>=0)
				Graphs.addEdgeWithVertices(grafo, a.getP1(), a.getP2(), a.getPeso());
			else 
				Graphs.addEdgeWithVertices(grafo, a.getP2(), a.getP1(), (-1)*a.getPeso());
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Match> getAllMatches(){
		List<Match> matches = this.dao.listAllMatches();
		Collections.sort(matches, new Comparator<Match>() {

			@Override
			public int compare(Match m1, Match m2) {
				return m1.getMatchID().compareTo(m2.getMatchID());
			}
			
		});
		return matches;
	}
	
	public GiocatoreMigliore getMigliore() {
		if(grafo == null) {
			return null;
		}
		
		Player best = null;
		double maxDelta = Double.MIN_VALUE;
		
		for(Player p : this.grafo.vertexSet()) {
			//calcolo la somma dei pesi degli archi uscenti
			double delta= 0;
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
				delta += this.grafo.getEdgeWeight(e);
			}
			
			//calcolo la somma dei pesi degli archi uscenti
			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) {
				delta -= this.grafo.getEdgeWeight(e);
			}
			
			if(delta > maxDelta) {
				best = p;
				maxDelta = delta;
			}
		}
		return new GiocatoreMigliore(best, maxDelta);
	}

	public Graph<Player, DefaultWeightedEdge> getGrafo() {
		
		return this.grafo;
	}
	
	public String doSimulation(int N, Match m) {
		if(this.grafo == null) {
			return null;
		}
		Team tHome = this.teams.get(m.getTeamHomeID());
		Team tAway = this.teams.get(m.getTeamAwayID());
		if(tHome==null || tAway==null) {
			return null;
		}
		this.sim.init(grafo, N, this.getMigliore().getP().getTeam(), tHome, tAway);
		this.sim.run();
		String s = "Risultato: "+this.sim.getTHome()+" "+this.sim.getGoalT1()+" - "+this.sim.getGoalT2()+" "+this.sim.getTAway()+"\n";
		s += "Espulsi "+this.sim.getTHome()+": "+this.sim.getEspulsiT1()+"\n";
		s += "Espulsi "+this.sim.getTAway()+": "+this.sim.getEspulsiT2()+"\n";
		return s;
	}
	
	
	
	
	
}
