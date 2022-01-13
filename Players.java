package survivor;

public class Players implements java.io.Serializable{
	
	private String name;
	private int idolCount;
	private int votesAgainst;
	
	public Players(String names) {
		name = names;
		idolCount = 0;
	}
	
	public void addIdol() {
		idolCount++;
	}
	
	public void useIdol() {
		idolCount--;
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
}
