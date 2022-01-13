package survivor;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {

	public void onMessageReceived(MessageReceivedEvent event) {
		String user = event.getAuthor().getName();
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
			for (int i = 0; i < Main.peoplePlaying.size(); i++) {
				Main.peoplePlaying.get(i).clearVotes();
			}
		}

		if (messageSent.equalsIgnoreCase(Main.prefix + "setup")) {
			Players p = new Players(user);
			Main.peoplePlaying.add(p);
			TextChannel textChannel = event.getJDA().getGuildById("919693815198138399")
					.getTextChannelsByName("important", true).get(0);
			textChannel.sendMessage(user + " joined survivor!!").queue();
		}

	/*	boolean isPlaying = false;
		for (int i = 0; i < Main.peoplePlaying.size(); i++) {
			if (user.equalsIgnoreCase(Main.peoplePlaying.get(i).getName())) {
				isPlaying = true;
			}
		}
		if (!isPlaying && !user.equals("JeffBot")) {
			Players p = new Players(user);
			Main.peoplePlaying.add(p);
			event.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendMessage("You have been added"))
					.queue();
		}
*/
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

		if (messageSent.contains(Main.prefix + "vote")) {
			boolean isFound = false;
			for (int i = 0; i < Main.peoplePlaying.size(); i++) {
				if (messageSent.contains(Main.peoplePlaying.get(i).getName())) {
					Main.peoplePlaying.get(i).addVote();
					isFound = true;
					System.out.println(Main.peoplePlaying.get(i).getVotesAgainst());
					break;
				}
			}
			if (!isFound)
				event.getChannel().sendMessage("player not found").queue();
		}

		if (messageSent.equalsIgnoreCase(Main.prefix + "readVotes") && user.equals(Main.gameMaster)) {
			int mostVotes = 0;
			int indexOfMostVotes = 0;
			for (int i = 0; i < Main.peoplePlaying.size(); i++) {
				if (Main.peoplePlaying.get(i).getVotesAgainst() != 0) {
					TextChannel textChannel = event.getJDA().getGuildById("919693815198138399")
							.getTextChannelsByName("tribal-council", true).get(0);
					textChannel.sendMessage(Main.peoplePlaying.get(i).getName() + " received "
							+ Main.peoplePlaying.get(i).getVotesAgainst() + " votes").queue();
					if (Main.peoplePlaying.get(i).getVotesAgainst() > mostVotes) {
						indexOfMostVotes = i;
					}
				}
			}
			TextChannel textChannel = event.getJDA().getGuildById("919693815198138399")
					.getTextChannelsByName("tribal-council", true).get(0);
			textChannel.sendMessage("And the person voted off this tribal council is " + Main.peoplePlaying.get(indexOfMostVotes).getName()).queue();
			textChannel.sendMessage("https://tenor.com/view/survivor-jeff-probst-tribe-gif-5473072").queue();
		}

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
		
		//save data
		//for(int i = 0; i < Main.peoplePlaying.size(); i++) {
			//Players p = null;
			//p = Main.peoplePlaying.get(i);
		//arrayListToFile.toTextFile(p);
		//}

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
		 */}

}
