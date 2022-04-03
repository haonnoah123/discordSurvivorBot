package survivor;

import java.util.TimerTask;
import java.time.*;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.internal.entities.DataMessage;


public class TimerTaskStuff extends TimerTask{

	public void run() {
		MessageReceivedEvent event = Commands.testEvent;
		long i = Math.abs(LocalDate.now().toEpochDay() - Commands.timerDate.toEpochDay());
		if(Commands.timerId != null) {
			event.getChannel().editMessageById(Commands.timerId,"Time left: " + Long.toString(i) + " days").queue();
		} else {
			Commands.timerId = Long.toString(event.getChannel().sendMessage("Time left: " + Long.toString(i) + " days").complete().getIdLong());
		}
	}

}
