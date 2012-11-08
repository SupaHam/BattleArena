package mc.alk.arena.objects.teams;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import mc.alk.arena.objects.ArenaPlayer;

/**
 * Class that is a collection of other teams
 * @author alkarin
 *
 */
public class CompositeTeam extends Team{
	final Set<Team> oldTeams = new HashSet<Team>();
	boolean nameSet = false;

	public CompositeTeam() {
		super();
		isPickupTeam = true;
	}

	protected CompositeTeam(Team team) {
		this();
		addTeam(team);
	}
	protected CompositeTeam(Set<ArenaPlayer> tplayers) {
		super(tplayers);
		isPickupTeam=true;
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		nameSet = true;
	}

	public void addTeam(Team t) {
		if (t instanceof CompositeTeam){
			CompositeTeam ct = (CompositeTeam) t;
			oldTeams.addAll(ct.oldTeams);
			players.addAll(ct.getPlayers());
		} else if (oldTeams.add(t)){
			players.addAll(t.getPlayers());
		}
	}

	public boolean removeTeam(Team t) {
		if (t instanceof CompositeTeam){
			for (Team tt : ((CompositeTeam)t).getOldTeams()){
				oldTeams.remove(tt);
			}
		}
		boolean has = oldTeams.remove(t);
		if (has){
			players.removeAll(t.getPlayers());}
		return has;
	}

	@Override
	public boolean hasTeam(Team team){
		for (Team t: oldTeams){
			if (t.hasTeam(team))
				return true;
		}
		return false;
	}

	public void finish() {
		if (!nameSet)
			createName();
	}

	public void removePlayer(ArenaPlayer p) {
		for (Team t: oldTeams){
			if (t.hasMember(p)){
				oldTeams.remove(t);
				break;
			}
		}
	}
	public Collection<Team> getOldTeams(){
		return oldTeams;
	}
}
