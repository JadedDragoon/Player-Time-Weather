package net.scrapironcity.ptw;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMain implements CommandExecutor {
    Logger log=Bukkit.getLogger();
    String prefix=ChatColor.GOLD + "" + ChatColor.BOLD + "PTW" + ChatColor.DARK_GRAY + " - " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender src, Command cmd, String label, String[] args) {
		String flabel=label + (args.length > 0 ? " " + args[0] : "");
        if (src instanceof Player) {
    		Player player = (Player) src;
        	try {
        		String subc=args.length > 0 ? args[0] : "";
            
	            switch(subc) {
	            	case "tset" :
	            		if (args.length < 2) {
	            			player.sendMessage(prefix + ChatColor.RED + "You must specify a time value.");
	                		player.chat("/help " + flabel);
	            			break;
	            		}
	            		return this.setTime(player, args[1], false, flabel);
	
	            	case "rtset" :
	            		if (args.length < 2) {
	            			player.sendMessage(prefix + ChatColor.RED + "You must specify a time offset value.");
	                		player.chat("/help " + flabel);
	            			break;
	            		}
	            		return this.setTime(player, args[1], true, flabel);
	            		
	            	case "wset" :
	            		if (args.length < 2 || (!args[1].equalsIgnoreCase("CLEAR") && !args[1].equalsIgnoreCase("DOWNFALL"))) {
	            			player.sendMessage(prefix + ChatColor.RED + "You must specify DOWNFALL for rain/snow or CLEAR for no rain/snow.");
	                		player.chat("/help " + flabel);
	            			break;
	            		}
	            		return this.setWeather(player, args[1], flabel);
	
	            	case "tsync" :
	            		return this.resetTime(player, flabel);
	            		
	            	case "wsync" :
	            		return this.resetWeather(player, flabel);
	            		
	            	case "sync":
	            		return this.resetWeather(player, flabel) && this.resetTime(player, flabel);
	            		
	            	case "status" :
	            	case "" :
	            		return this.getStatus(player, flabel);
	
	            	default :
	                    player.sendMessage(prefix + ChatColor.RED + "Unkown command: " + subc);            		
	            }
        	} catch (Exception e) {
        		log.warning("Failed parsing command in Player Time & Weather:");
        		log.warning("	" + e.toString());
        		player.chat("/help " + flabel);
        	}
            
            return true;
        }
        
        src.sendMessage("Only players may use /ptw!");        
        return true;
    }
    
    private boolean getStatus(Player player, String label) {
    	try {
	    	String[] msg={
	    		ChatColor.GOLD + "" + ChatColor.BOLD + "Player Weather" + ChatColor.DARK_GRAY + ":  " + ChatColor.RESET + (player.getPlayerWeather() != null ? ChatColor.GREEN + player.getPlayerWeather().toString() : ChatColor.AQUA + "Synced with server."),
	    		ChatColor.GOLD + "" + ChatColor.BOLD + "Player Time" + ChatColor.DARK_GRAY + ":        " + ChatColor.RESET + (player.getPlayerTime() != player.getWorld().getFullTime() ? (player.isPlayerTimeRelative() ? ChatColor.DARK_GREEN + "+" : "") + ChatColor.GREEN + player.getPlayerTimeOffset() : ChatColor.AQUA + "Synced with server."),
	    	};
	    	
	    	player.sendMessage(msg);
	    	
    	} catch (Exception e) {
    		log.warning("Unable to display status in Player Time & Weather:");
    		log.warning("	" + e.toString());
    		player.chat("/help " + label);
    	}
    	
    	return true;
    }
    
    private boolean resetTime(Player player, String label) {
    	try {
    		player.resetPlayerTime();
    		player.sendMessage(prefix + ChatColor.AQUA + "Time Syncronized");
    	} catch (Exception e) {
    		log.warning("Unable to reset time in Player Time & Weather:");
    		log.warning("	" + e.toString());
    		player.chat("/help " + label);
    	}
    	
    	return true;
    }
    
    private boolean resetWeather(Player player, String label) {
    	try {
    		player.resetPlayerWeather();
    		player.sendMessage(prefix + ChatColor.AQUA + "Weather Syncronized");
    	} catch (Exception e) {
    		log.warning("Unable to reset weather in Player Time & Weather:");
    		log.warning("	" + e.toString());
    		player.chat("/help " + label);
    	}
    	
    	return true;
    }
    
    private boolean setTime(Player player, String timeString, Boolean rel, String label) {
    	try {
    		player.setPlayerTime(Long.parseLong(timeString), rel);
    		player.sendMessage(prefix + ChatColor.GREEN + "Personal Time Set" + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + (player.isPlayerTimeRelative() ? "+" : "") + player.getPlayerTimeOffset());
    	} catch (NumberFormatException e) {
    		player.sendMessage(prefix + ChatColor.RED + timeString + " is not a valid time string.");
    		player.chat("/help " + label);
    	} catch (Exception e) {
    		log.warning("Unable to set time in Player Time & Weather:");
    		log.warning("	" + e.toString());
    		player.resetPlayerTime();
    		player.chat("/help " + label);
    	}
    	
    	return true;
    }
    
    private boolean setWeather(Player player, String weatherString, String label) {
    	try {
    		player.setPlayerWeather(WeatherType.valueOf(weatherString.toUpperCase()));
    		player.sendMessage(prefix + "" + ChatColor.GREEN + "Personal Weather Set" + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + player.getPlayerWeather());
    	} catch (Exception e) {
    		log.warning("Unable to set weather in Player Time & Weather:");
    		log.warning("	" + e.toString());
    		player.resetPlayerWeather();
    		player.chat("/help " + label);
    	}
    	
    	return true;
    }
}