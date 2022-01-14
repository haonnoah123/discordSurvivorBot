package survivor;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {

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
			// checks to see if message sent is todays idol and dm's the user and the game
			// master a chat
			if (event.getChannel().getName().equals("idol-hunting")) {
				if (Integer.parseInt(messageSent) == Main.idolNumber && Main.isIdolFound == false) {
					System.out.println(event.getAuthor());
					event.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendMessage("You got the idol"))
							.queue();
					TextChannel textChannel = event.getJDA().getGuildById("860700864489586698")
							.getTextChannelsByName("henry-bot-chat", true).get(0);
					textChannel.sendMessage(user + " got an idol").queue();
					Main.isIdolFound = true;
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

			// save data
			// for(int i = 0; i < Main.peoplePlaying.size(); i++) {
			// Players p = null;
			// p = Main.peoplePlaying.get(i);
			// arrayListToFile.toTextFile(p);
			// }

			/*
			 * if (messageSent.length() > 5) { if (messageSent.substring(0,
			 * 5).equalsIgnoreCase(Main.prefix + "vote")) { TextChannel textChannel =
			 * event.getJDA().getGuildById("919693815198138399")
			 * .getTextChannelsByName("tribal-council", true).get(0);
			 * textChannel.sendMessage("Vote #" + Main.i + " goes to " +
			 * messageSent.substring(5)).queue(); Main.i++; } } if
			 * (messageSent.equalsIgnoreCase(Main.prefix + "info") &&
			 * event.getChannel().getName().equals("general")) {
			 * System.out.println(event.getChannel());
			 * event.getChannel().sendTyping().queue();
			 * event.getChannel().sendMessage("Hey there, I'm alive.").queue(); }
			 * 
			 */}
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

}