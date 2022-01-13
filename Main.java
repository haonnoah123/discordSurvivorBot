package survivor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Main {

	public static String prefix = "!";
	public static int idolNumber = (int) (Math.random() * 100 + 1);
	public static boolean isGameOver = false;
	public static int i = 1;
	public static boolean isIdolFound = false;
	public static LocalDate whenIdolChanged = LocalDate.now();
	public static ArrayList<Players> peoplePlaying = new ArrayList<>();
	public static String gameMaster = "Someone";

	public static void main(String[] args) throws LoginException {
		System.out.println(idolNumber);
		System.out.println(LocalTime.now());
		System.out.println(LocalTime.MIDNIGHT);
		JDABuilder jda = JDABuilder.createDefault("OTE5NjkzMDQ2Njg0MTk2ODY1.YbZg5Q.LuoDnbFyksmMhzo9R6meHHnO7IQ");
		jda.setActivity(Activity.playing("The bot version of Henry"));
		//jda.setActivity(Activity.listening("Weezer"));
		jda.setStatus(OnlineStatus.ONLINE);
		jda.addEventListeners(new Commands());
		jda.build();
	}
}
