package net.scrapironcity.ptw;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PTWWorldChangeListener implements Listener {
	PTWCommand ptwCommand = new PTWCommand();

	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
		// on world change, always silently sync time and weather to server 
		ptwCommand.resetTime(event.getPlayer(), false);
		ptwCommand.resetWeather(event.getPlayer(), false);
	}
}
