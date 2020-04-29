package net.scrapironcity.ptw;

import org.bukkit.plugin.java.JavaPlugin;

public class ptw extends JavaPlugin {
    @Override
    public void onEnable() {
    	this.getCommand("ptw").setExecutor(new CommandMain());
    }
    
    @Override
    public void onDisable() {

    }
}
