package survivor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
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
	public static String playerFileName = "C:\\Users\\19523\\Documents\\SaveData.txt";
	public static String teamFileName = "C:\\Users\\19523\\Documents\\teamSaveData.txt";

	public static void main(String[] args) throws LoginException {
		if(Commands.importFile(playerFileName).size() != 0) {
			loadTeamData();
			loadPlayerData();
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
	
	public static void loadTeamData() {
		ArrayList<String> data = new ArrayList<>();
		data = Commands.importFile(teamFileName);
		for(int i = 0; i < data.size(); i+=3) {
			int a = i;
			String role = data.get(a);
			int idolNumber = Integer.parseInt(data.get(a+=1));
			boolean isIdolFound = Boolean.parseBoolean(data.get(a+=1));
			Teams t = new Teams(null, idolNumber, isIdolFound);
			survivorTeams.add(t);
		}
	}
	
	public static void loadPlayerData() {
		ArrayList<String> data = new ArrayList<>();
		data = Commands.importFile(playerFileName);
		for(int i = 0; i < data.size(); i+=9) {
			int a = i;
			String name = data.get(a);
			User u = User.fromId(Long.parseLong(data.get(a+=1)));
			int idolCount = Integer.parseInt(data.get(a+=1));
			int votesAgainst = Integer.parseInt(data.get(a+=1));
			boolean isIn = Boolean.parseBoolean(data.get(a+=1));
			boolean hasVoted = Boolean.parseBoolean(data.get(a+=1));
			boolean hasPlayedIdol = Boolean.parseBoolean(data.get(a+=1));
			long teamId = Long.parseLong(data.get(a+=1));
			Players p = new Players(name, u, idolCount, votesAgainst, isIn, hasVoted, hasPlayedIdol, null);
			Main.peoplePlaying.add(p);
		}
		System.out.println("done loading data: " + Main.peoplePlaying.size() + " People are playing");
	}
}
