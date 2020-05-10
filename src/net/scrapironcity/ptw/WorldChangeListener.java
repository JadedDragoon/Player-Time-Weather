package net.scrapironcity.ptw;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {
	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
		// on world change, always sync time and weather to server 
		CommandMain.resetTime(event.getPlayer());
		CommandMain.resetWeather(event.getPlayer());
	}
}
