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
	
	public Players(String names, User u) {
		name = names;
		user = u;
		idolCount = 0;
		isIn = true;
		hasVoted = false;
		hasPlayedIdol = false;
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
}
