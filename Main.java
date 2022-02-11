package survivor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Main {

	public static String prefix = "!";
	public static int idolNumber = (int) (Math.random() * 100 + 1);
	public static boolean isGameOver = false;
	public static int i = 1;
	public static boolean isIdolFound = false;
	public static LocalDate whenIdolChanged = LocalDate.now();
	public static ArrayList<Players> peoplePlaying = new ArrayList<>();
	public static String gameMaster = "Someone";
	public static boolean hasGameStarted = false;
	public static ArrayList<Teams> survivorTeams = new ArrayList<>();
	public static boolean largeGroupIdol = false;
	public static String fileName = "/Users/90309333/Desktop/SaveData";

	public static void main(String[] args) throws LoginException {
		if(Commands.importFile().size() != 0) {
			loadData();
		}
		System.out.println(idolNumber);
		JDABuilder jda = JDABuilder.createDefault("OTE5NjkzMDQ2Njg0MTk2ODY1.YbZg5Q.LuoDnbFyksmMhzo9R6meHHnO7IQ")
				.setChunkingFilter(ChunkingFilter.ALL).setMemberCachePolicy(MemberCachePolicy.ALL)
				.enableIntents(GatewayIntent.GUILD_MEMBERS);
		jda.setActivity(Activity.playing("The bot version of Henry"));
		jda.setStatus(OnlineStatus.ONLINE);
		jda.addEventListeners(new Commands());
		jda.build();
	}
	
	public static void loadData() {
		ArrayList<String> data = new ArrayList<>();
		data = Commands.importFile();
		for(int i = 0; i < data.size(); i+=9) {
			String name = data.get(i);
			System.out.println("test");
			long userId = Long.parseLong(data.get(i++));
			int idolCount = Integer.parseInt(data.get(i+=2));
			System.out.println(data.get(i));
			int votesAgainst = Integer.parseInt(data.get(i+=3));
			boolean isIn = Boolean.parseBoolean(data.get(i+=4));
			boolean hasVoted = Boolean.parseBoolean(data.get(i+=5));
			boolean hasPlayedIdol = Boolean.parseBoolean(data.get(i+=6));
			long teamId = Long.parseLong(data.get(i+=7));
			Players p = new Players(name, null, idolCount, votesAgainst, isIn, hasVoted, hasPlayedIdol, null);
			Main.peoplePlaying.add(p);
		}
		System.out.println("done loading data: " + Main.peoplePlaying.size() + " People are playing");
	}
}
