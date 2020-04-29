package net.scrapironcity.ptw;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMain implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender src, Command cmd, String label, String[] args) {
        if (src instanceof Player) {
        	try {
        		Player player = (Player) src;
            
        		String subc=args.length > 0 ? args[0] : "";
            
	            switch(subc) {
	            	case "tset" :
	            		if (args.length < 2) {
	            			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You must specify a time value.");
	            			break;
	            		}
	            		return this.setTime(player, args[1], false);
	
	            	case "rtset" :
	            		if (args.length < 2) {
	            			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You must specify a time offset value.");
	            			break;
	            		}
	            		return this.setTime(player, args[1], true);
	            		
	            	case "wset" :
	            		if (args.length < 2 || (!args[1].equalsIgnoreCase("CLEAR") && !args[1].equalsIgnoreCase("DOWNFALL"))) {
	            			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You must specify DOWNFALL for rain/snow or CLEAR for no rain.");
	            			break;
	            		}
	            		return this.setWeather(player, args[1]);
	
	            	case "tsync" :
	            		return this.resetTime(player);
	            		
	            	case "wsync" :
	            		return this.resetWeather(player);
	            		
	            	case "sync":
	            		return this.resetWeather(player) && this.resetTime(player);
	            		
	            	case "status" :
	            	case "" :
	            		return this.getStatus(player);
	
	            	default :
	                    player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Unkown command: " + subc);            		
	            }
        	} catch (Exception e) {
        		Bukkit.getLogger().warning("Failed parsing command in Player Time & Weather:");
        		Bukkit.getLogger().warning("	" + e.getLocalizedMessage());
        		
        		return false;    		
        	}
            
            return true;
        }
        
        src.sendMessage("Only players may use /ptw!");        
        return false;
    }
    
    private boolean getStatus(Player player) {
    	try {
	    	String[] msg={
	    		ChatColor.GOLD + "" + ChatColor.BOLD + "Player Weather" + ChatColor.DARK_GRAY + ":  " + ChatColor.RESET + (player.getPlayerWeather() != null ? ChatColor.GREEN + player.getPlayerWeather().toString() : ChatColor.AQUA + "Synced with server."),
	    		ChatColor.GOLD + "" + ChatColor.BOLD + "Player Time" + ChatColor.DARK_GRAY + ":        " + ChatColor.RESET + (player.getPlayerTime() != player.getWorld().getFullTime() ? (player.isPlayerTimeRelative() ? ChatColor.DARK_GREEN + "+" : "") + ChatColor.GREEN + player.getPlayerTimeOffset() : ChatColor.AQUA + "Synced with server."),
	    	};
	    	
	    	player.sendMessage(msg);
	    	
    	} catch (Exception e) {
    		Bukkit.getLogger().warning("Unable to display status in Player Time & Weather:");
    		Bukkit.getLogger().warning("	" + e.getLocalizedMessage());
    		
    		return false;
    	}
    	
    	return true;
    }
    
    private boolean resetTime(Player player) {
    	try {
    		player.resetPlayerTime();
    		player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Time Syncronized");
    	} catch (Exception e) {
    		Bukkit.getLogger().warning("Unable to reset time in Player Time & Weather:");
    		Bukkit.getLogger().warning("	" + e.getLocalizedMessage());
    		
    		return false;    		
    	}
    	
    	return true;
    }
    
    private boolean resetWeather(Player player) {
    	try {
    		player.resetPlayerWeather();
    		player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Weather Syncronized");
    	} catch (Exception e) {
    		Bukkit.getLogger().warning("Unable to reset weather in Player Time & Weather:");
    		Bukkit.getLogger().warning("	" + e.getLocalizedMessage());
    		
    		return false;    		
    	}
    	
    	return true;
    }
    
    private boolean setTime(Player player, String timeString, Boolean rel) {
    	try {
    		player.setPlayerTime(Long.parseLong(timeString), rel);
    		player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Personal Time Set" + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + (player.isPlayerTimeRelative() ? "+" : "") + player.getPlayerTimeOffset());
    	} catch (Exception e) {
    		Bukkit.getLogger().warning("Unable to set time in Player Time & Weather:");
    		Bukkit.getLogger().warning("	" + e.getLocalizedMessage());
    		player.resetPlayerTime();
    		
    		return false;    		
    	}
    	
    	return true;
    }
    
    private boolean setWeather(Player player, String weatherString) {
    	try {
    		player.setPlayerWeather(WeatherType.valueOf(weatherString.toUpperCase()));
    		player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Personal Weather Set" + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + player.getPlayerWeather());
    	} catch (Exception e) {
    		Bukkit.getLogger().warning("Unable to set weather in Player Time & Weather:");
    		Bukkit.getLogger().warning("	" + e.getLocalizedMessage());
    		player.resetPlayerWeather();
    		
    		return false;    		
    	}
    	
    	return true;
    }
}