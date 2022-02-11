package survivor;

import net.dv8tion.jda.api.entities.User;

public class Players{
	
	private String name;
	private User user;
	private int idolCount;
	private int votesAgainst;
	private boolean isIn;
	private boolean hasVoted;
	private boolean hasPlayedIdol;
	private Teams team;
	
	public Players(String names, User u) {
		name = names;
		user = u;
		idolCount = 0;
		isIn = true;
		hasVoted = false;
		hasPlayedIdol = false;
	}
	
	public Players(String name, User user, int idolCount, int votesAgainst, boolean isIn, boolean hasVoted, boolean hasPlayedIdol, Teams team) {
		this.name = name;
		this.user = user;
		this.idolCount = idolCount;
		this.votesAgainst = votesAgainst;
		this.isIn = isIn;
		this.hasVoted = hasVoted;
		this.hasPlayedIdol = hasPlayedIdol;
		this.team = team;
	}
	
	public void setPlayerTeam(Teams t) {
		team = t;
	}
	
	public void newDay() {
		votesAgainst = 0;
		hasVoted = false;
		hasPlayedIdol = false;
	}
	
	public void playIdol() {
		hasPlayedIdol = true;
		idolCount--;
	}
	
	public void unPlayIdol() {
		hasPlayedIdol = false;
	}
	
	public void didVote() {
		hasVoted = true;
	}
	
	public void newTribal() {
		hasVoted = false;
	}
	
	public void voteOut() {
		isIn = false;
	}
	
	public void addIdol() {
		idolCount++;
	}
	
	public void addVote() {
		votesAgainst++;
	}
	
	public void clearVotes() {
		votesAgainst = 0;
	}

	public String getName() {
		return name;
	}
	
	public String getNameOfPlayer() {
		return name;
	}
	
	public int amountOfIdols() {
		return idolCount;
	}
	
	public int getVotesAgainst() {
		return votesAgainst;
	}
	
	public User getUser() {
		return user;
	}
	
	public boolean getHasVoted() {
		return hasVoted;
	}
	
	public boolean getIsIn() {
		return isIn;
	}
	
	public boolean getHasPlayedIdol() {
		return hasPlayedIdol;
	}
	
	public Teams getTeam() {
		return team;
	}
	
	public String toString() {
		String info = (name + "\n" + user.getIdLong() + "\n" + idolCount + "\n" + votesAgainst + "\n" + isIn + "\n" + hasVoted + "\n" + hasPlayedIdol + "\n");
		if(team != null) {
			info += team.getRole().getIdLong() + "\n";
		} else {
			info += 0 + "\n";
		}
		return info;
	}
}
