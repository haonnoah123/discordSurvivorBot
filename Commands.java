package survivor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.concurrent.Task;

public class Commands extends ListenerAdapter {
	
	public static MessageReceivedEvent testEvent = null;
	public static LocalDate timerDate = null;
	public static String timerId = null;

	public void onMessageReceived(MessageReceivedEvent event) {
		String user = event.getAuthor().getName();
		if (!user.equals("JeffBot")) {
			User tempUser = event.getAuthor();
			System.out.println(tempUser);
			Players tempPlayer = new Players(user, tempUser);
			if (findPlayer(tempUser) == -1) {
				Main.peoplePlaying.add(tempPlayer);
			}
			int indexOfUser = findPlayer(tempUser);
			Players player = Main.peoplePlaying.get(indexOfUser);
			System.out.println(indexOfUser);
			String messageSent = event.getMessage().getContentRaw();
			System.out.println(messageSent);
			// Time.valueOf(LocalTime.now()).compareTo(Time.valueOf("00:00:00")) >= 1 &&
			if (!Main.whenIdolChanged.equals(LocalDate.now())) {
				Main.isIdolFound = false;
				Main.idolNumber = (int) (Math.random() * 100 + 1);
				Main.whenIdolChanged = LocalDate.now();
				TextChannel textChannel = event.getJDA().getGuildById("860700864489586698")
						.getTextChannelsByName("idol-hunting", true).get(0);
				textChannel.sendMessage("IDOL CHANGED").queue();
				System.out.println(Main.idolNumber);

				// clear player votes
				makeNewDay();
			}
			/*
			 * //setup if (messageSent.equalsIgnoreCase(Main.prefix + "setup") &&
			 * !isAlreadyEnroled(tempUser) && !Main.hasGameStarted ) { setup(messageSent,
			 * tempUser,tempPlayer, event, user); }
			 */
			
			// checks to see if message sent is todays idol and dm's the user and sends the game
			// master a chat
			if (event.getChannel().getName().equals("idol-hunting") && Main.largeGroupIdol) {
				if (Integer.parseInt(messageSent) == Main.idolNumber && Main.isIdolFound == false) {
					System.out.println(event.getAuthor());
					event.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendMessage("You got the idol"))
							.queue();
					TextChannel textChannel = event.getJDA().getGuildById("860700864489586698")
							.getTextChannelsByName("henry-bot-chat", true).get(0);
					textChannel.sendMessage(user + " got an idol").queue();
					player.addIdol();
					Main.isIdolFound = true;
				}
			}
			
			//team idol mode
			if (event.getChannel().getName().contains("idol-hunting") && !Main.largeGroupIdol) {
				if (Integer.parseInt(messageSent) == player.getTeam().getIdolNumber() && !player.getTeam().getIsIdolFound()) {
					System.out.println(event.getAuthor());
					event.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendMessage("You got the idol"))
							.queue();
					TextChannel textChannel = event.getJDA().getGuildById("860700864489586698")
							.getTextChannelsByName("henry-bot-chat", true).get(0);
					textChannel.sendMessage(user + " got an idol").queue();
					player.addIdol();
					player.getTeam().idolFound();
				}
			}

			// !vote to vote
			if (messageSent.contains(Main.prefix + "vote") && !player.getHasVoted() && player.getIsIn()) {
				boolean isFound = false;
				for (int i = 0; i < Main.peoplePlaying.size(); i++) {
					if (messageSent.contains(Main.peoplePlaying.get(i).getName())) {
						Main.peoplePlaying.get(i).addVote();
						isFound = true;
						System.out.println(Main.peoplePlaying.get(i).getVotesAgainst());
						event.getChannel().sendMessage("vote counted").queue();
						break;
					}
				}
				if (!isFound)
					event.getChannel().sendMessage("player not found").queue();
			}

			// !readvotes to read the votes from the tribal council.
			if (messageSent.equalsIgnoreCase(Main.prefix + "readVotes") && user.equals(Main.gameMaster)) {
				int mostVotes = 0;
				int indexOfMostVotes = 0;
				boolean isTie = false;

				TextChannel textChannel = event.getJDA().getGuildById("919693815198138399")
						.getTextChannelsByName("tribal-council", true).get(0);

				// sends a message with who played idols
				if (!findPlayedIdols().equals("")) {
					textChannel.sendMessage(findPlayedIdols() + " played an idol").queue();
				}

				// determines who got the most votes
				for (int i = 0; i < Main.peoplePlaying.size(); i++) {
					if (Main.peoplePlaying.get(i).getVotesAgainst() != 0) {
						textChannel.sendMessage(Main.peoplePlaying.get(i).getName() + " received "
								+ Main.peoplePlaying.get(i).getVotesAgainst() + " votes").queue();
						if (Main.peoplePlaying.get(i).getVotesAgainst() == mostVotes
								&& !Main.peoplePlaying.get(i).getHasPlayedIdol()) {
							isTie = true;
						}
						if (Main.peoplePlaying.get(i).getVotesAgainst() > mostVotes
								&& !Main.peoplePlaying.get(i).getHasPlayedIdol()) {
							indexOfMostVotes = i;
							isTie = false;
						}
					}
				}

				// sends who got voted off
				if (!isTie) {
					textChannel.sendMessage("And the person voted off this tribal council is "
							+ Main.peoplePlaying.get(indexOfMostVotes).getName()).queue();
					textChannel.sendMessage("https://tenor.com/view/survivor-jeff-probst-tribe-gif-5473072").queue();
				} else {
					textChannel.sendMessage("It's a tie").queue();
				}
			}

			// !idols to get dm'd the amount of idols you have
			if (messageSent.equalsIgnoreCase(Main.prefix + "idols")) {
				for (int i = 0; i < Main.peoplePlaying.size(); i++) {
					if (user.equalsIgnoreCase(Main.peoplePlaying.get(i).getName())) {
						int a = Main.peoplePlaying.get(i).amountOfIdols();
						event.getAuthor().openPrivateChannel()
								.flatMap(channel -> channel.sendMessage("You have " + a + " idols")).queue();
						break;
					}
				}
			}

			if (messageSent.equalsIgnoreCase(Main.prefix + "startGame") && user.equals(Main.gameMaster)) {
				Main.hasGameStarted = true;
			}

			if (messageSent.equalsIgnoreCase(Main.prefix + "useIdol")) {
				player.playIdol();
			}
			
			if (messageSent.equalsIgnoreCase(Main.prefix + "resetTeams") && user.equals(Main.gameMaster)) {
				clearTeams();
				findTeams(event);
				System.out.println(Main.survivorTeams.get(0).getIdolNumber());
			}
			
			if(messageSent.equalsIgnoreCase(Main.prefix + "switchIdolMode") && user.equals(Main.gameMaster)) {
				Main.largeGroupIdol = false;
			}
			
			if(messageSent.equalsIgnoreCase(Main.prefix + "changeIdol") && user.equals(Main.gameMaster)) {
				String str = "";
				changeIdol();
				TextChannel textChannel = event.getJDA().getGuildById("860700864489586698")
						.getTextChannelsByName("henry-bot-chat", true).get(0);
				for(Teams t: Main.survivorTeams) {
					str += event.getJDA().getRoleById(t.getRoleId()).getAsMention() + ": " + t.getIdolNumber() + "\n";
				}
				textChannel.sendMessage(str).queue();
			}
			
			if(messageSent.equalsIgnoreCase(Main.prefix + "help") && user.equals(Main.gameMaster)) {
				help(event);
			}
			
			if(messageSent.equalsIgnoreCase(Main.prefix + "setup") && user.equals(Main.gameMaster)) {
				setupSurvivor(event);
				clearTeams();
				findTeams(event);
				findNonPlayers();
				Main.hasGameStarted = true;
				TextChannel textChannel = event.getJDA().getGuildById("860700864489586698")
						.getTextChannelsByName("henry-bot-chat", true).get(0);
				textChannel.sendMessage("done").queue();
				System.out.println(Main.survivorTeams.get(0).getIdolNumber());
				System.out.println(Main.peoplePlaying.size());
				System.out.println(Main.survivorTeams.size());
			}
			
			if(messageSent.contains(Main.prefix + "removeIdol") && user.equals(Main.gameMaster)) {
				TextChannel textChannel = event.getJDA().getGuildById("860700864489586698")
						.getTextChannelsByName("henry-bot-chat", true).get(0);
				if(removeIdol(messageSent.substring(10))) {
						textChannel.sendMessage("done").queue();
				} else {
						textChannel.sendMessage("player not found try again").queue();
				}
			}
			
			if(messageSent.contains(Main.prefix + "addIdol") && user.equals(Main.gameMaster)) {
				TextChannel textChannel = event.getJDA().getGuildById("860700864489586698")
						.getTextChannelsByName("henry-bot-chat", true).get(0);
				if(addIdol(messageSent.substring(7))) {
						textChannel.sendMessage("done").queue();
				} else {
						textChannel.sendMessage("player not found try again").queue();
				}
			}
			
			if(messageSent.equalsIgnoreCase(Main.prefix + "getAllIdols") && user.equals(Main.gameMaster)) {
				String str = "";
				for(Players p: Main.peoplePlaying) {
					if(p.getIsIn()) {
						str += p.getName() + ": " + p.amountOfIdols() + "\n";
					}
				}
				TextChannel textChannel = event.getJDA().getGuildById("860700864489586698")
						.getTextChannelsByName("henry-bot-chat", true).get(0);
					textChannel.sendMessage(str).queue();
			}
			
			if(messageSent.contains(Main.prefix + "removePlayer") && user.equals(Main.gameMaster)) {
				TextChannel textChannel = event.getJDA().getGuildById("860700864489586698")
						.getTextChannelsByName("henry-bot-chat", true).get(0);
				if(removePlayer(messageSent.substring(12))) {
					textChannel.sendMessage("done").queue();
				} else {
					textChannel.sendMessage("player not found");
				}
			}
			
			if(messageSent.contains(Main.prefix + "startTimer") && user.equals(Main.gameMaster)) {
				int tempYear = Integer.parseInt(messageSent.substring(12, 16));
				int tempMonth = Integer.parseInt(messageSent.substring(17, 19));
				int tempDay = Integer.parseInt(messageSent.substring(20, 22));
				LocalDate tempDate = LocalDate.of(tempYear, tempMonth, tempDay);
				timerDate = tempDate;
				Timer timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTaskStuff(), 0, 1000000000);
			}

			saveData(Main.playerFileName);
			saveTeamData(Main.teamFileName);
			}
	}
	
	public static boolean removePlayer(String messageSent) {
		boolean b = false;
		for(Players p: Main.peoplePlaying) {
			if(messageSent.contains(p.getName())) {
				p.voteOut();
				b = true;
			}
		}
		return b;
	}
	
	public static void findNonPlayers() {
		for(Players p: Main.peoplePlaying) {
			if(p.getTeam() == null || p.getTeam().getRoleId() == 0) {
				p.voteOut();
			}
		}
	}
	
	public static boolean removeIdol(String messageSent) {
		boolean b = false;
		for(Players p: Main.peoplePlaying) {
			if(messageSent.contains(p.getName())) {
				p.useIdol();
				b = true;
			}
		}
		return b;
	}
	
	public static boolean addIdol(String messageSent) {
		boolean b = false;
		for(Players p: Main.peoplePlaying) {
			if(messageSent.contains(p.getName())) {
				p.addIdol();
				b = true;
			}
		}
		return b;
	}
	
	public static void setupSurvivor(MessageReceivedEvent event) {
		//gets a list of all members
		List<Member> members = event.getGuild().getMembers();
		//sorts through them
		for(Member m : members) {
			User tempUser = m.getUser();
			String user = m.getUser().getName();
			Players tempPlayer = new Players(user, tempUser);
			//finds out if they are already a player
			if (findPlayer(tempUser) == -1 && !user.equals("JeffBot")) {
				Main.peoplePlaying.add(tempPlayer);
			}
		}
	}
	
	public static void help(MessageReceivedEvent event) {
		event.getChannel().sendMessage("!startGame to start game").queue();
		event.getChannel().sendMessage("!resetTeams use after you switch teams").queue();
		event.getChannel().sendMessage("!switchIdolMode switch from large group idol to small group idol, default is small group idol").queue();
		event.getChannel().sendMessage("!changeIdol in smallGroupIdol mode it will reset all the groups idols").queue();
	}
	
	public static void changeIdol() {
		for(Teams t : Main.survivorTeams) {
			t.resetIdolNumber();
		}
	}
	
	public static void findTeams(MessageReceivedEvent event) {	
		//gets all the roles in the server
		List<Role> roles = new ArrayList<>();
		roles = event.getGuild().getRoles();
		//goes through all the roles
		for(Role r : roles) {
			//if the roles has the word "Team" in it it'll make a Team and add it to the ArrayList
			if(r.getName().contains("Team")) {
				Teams tempTeam = new Teams(r.getIdLong());
				Main.survivorTeams.add(tempTeam);
				//find everyone with the role
				List<Member> members = new ArrayList<>();
				members = event.getGuild().getMembersWithRoles(r);
				for(Member m : members) {
					//adds everyone with the role to the team
					Players tempPlayer = Main.peoplePlaying.get(findPlayer(m.getUser()));
					tempPlayer.setPlayerTeam(tempTeam);
				}
			}
		}
	}
	
	public static void clearTeams() {
		for(int i = 0; i < Main.survivorTeams.size(); i++) {
			Main.survivorTeams.remove(i);
			i--;
		}
	}

	public static void setup(String messageSent, User tempUser, Players tempPlayer, MessageReceivedEvent event,
			String user) {
		if (messageSent.equalsIgnoreCase(Main.prefix + "setup") && !isAlreadyEnroled(tempUser)
				&& !Main.hasGameStarted) {
			Main.peoplePlaying.add(tempPlayer);
			TextChannel textChannel = event.getJDA().getGuildById("919693815198138399")
					.getTextChannelsByName("important", true).get(0);
			textChannel.sendMessage(user + " joined survivor!!").queue();
		}
	}

	public static void makeNewDay() {
		for (Players p : Main.peoplePlaying) {
			p.newDay();
		}
	}

	public static boolean isAlreadyEnroled(User u) {
		for (Players p : Main.peoplePlaying) {
			if (p.getUser().equals(u))
				return true;
		}
		return false;
	}

	public static int findPlayer(User u) {
		for (int i = 0; i < Main.peoplePlaying.size(); i++) {
			if (Main.peoplePlaying.get(i).getUser().equals(u))
				return i;
		}
		return -1;
	}

	public static String findPlayedIdols() {
		String s = "";
		for (Players p : Main.peoplePlaying) {
			if (p.getHasPlayedIdol())
				s += p.getNameOfPlayer() + " ";
		}
		return s;
	}
	
	public static void saveData(String fileName) {
		try {
			WriteFile data = new WriteFile(fileName, true);
			data.clearFile();
			for(Players p : Main.peoplePlaying) {
				data.writeToFile(p.toString());
			}
			System.out.println("Text File Written");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveTeamData(String fileName) {
		try {
			WriteFile data = new WriteFile(fileName, true);
			data.clearFile();
			for(Teams t : Main.survivorTeams) {
				data.writeToFile(t.toString());
			}
			System.out.println("Text File Written");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	  public static ArrayList<String> importFile(String fileName) {
		    ArrayList<String> words = new ArrayList<>();
		    try {
		      File myObj = new File(fileName);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        words.add(data);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		    return words;
		  }

}
