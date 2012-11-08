package mc.alk.arena.competition;

import mc.alk.arena.objects.CompetitionState;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.teams.Team;

public class BlankCompetition extends Competition{
	MatchParams params;
	static int count = 0;
	int id = count++;

	public BlankCompetition(MatchParams mp) {this.params = mp;}

	@Override
	public Long getTime(CompetitionState state) {return null;}

	@Override
	public int getID() {return id;}

	@Override
	public CompetitionState getState() {return null;}

	@Override
	protected void transitionTo(CompetitionState state) {}

	@Override
	public MatchParams getParams() {return this.params;}

	@Override
	public void addTeam(Team team) {this.teams.add(team);}
}
