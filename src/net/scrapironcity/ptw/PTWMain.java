package net.scrapironcity.ptw;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PTWMain extends JavaPlugin {
	private Listener worldChangeListener = new WorldChangeListener();

	@Override
	public void onLoad() {
	}
	
    @Override
    public void onEnable() {
    	this.getCommand("ptw").setExecutor(new PTWCommand());
    	getServer().getPluginManager().registerEvents(worldChangeListener, this);
    }
    
    @Override
    public void onDisable() {
    	HandlerList.unregisterAll(worldChangeListener);
    }
}
