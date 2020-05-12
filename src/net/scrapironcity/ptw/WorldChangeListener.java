package net.scrapironcity.ptw;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {
	PTWCommand ptwCommand = new PTWCommand();

	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
		// on world change, always sync time and weather to server 
		ptwCommand.resetTime(event.getPlayer());
		ptwCommand.resetWeather(event.getPlayer());
	}
}
