package net.scrapironcity.ptw;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {
	CommandMain commandMain = new CommandMain();

	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
		// on world change, always sync time and weather to server 
		commandMain.resetTime(event.getPlayer());
		commandMain.resetWeather(event.getPlayer());
	}
}
