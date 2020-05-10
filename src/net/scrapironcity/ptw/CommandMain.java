package net.scrapironcity.ptw;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMain implements CommandExecutor {
    private static Logger log=Bukkit.getLogger();
    private static String prefix=ChatColor.GOLD + "" + ChatColor.BOLD + "PTW" + ChatColor.DARK_GRAY + " - " + ChatColor.RESET;
    private static double multiplier = 1000/(double) 60;
    
    @Override
    public boolean onCommand(CommandSender src, Command cmd, String label, String[] args) {
		String flabel=label + (args.length > 0 ? " " + args[0] : "");
        if (src instanceof Player) {
    		Player player = (Player) src;
    		boolean success = true; 
        	try {
        		String subc=args.length > 0 ? args[0] : "";
            
	            switch(subc) {
	            	case "tset" :
	            		if (args.length < 2) {
	            			player.sendMessage(prefix + ChatColor.RED + "You must specify a time value.");
	                		success = false;
	            			break;
	            		}
	            		success = setTime(player, args[1], false);
	            		break;
	
	            	case "rtset" :
	            		if (args.length < 2) {
	            			player.sendMessage(prefix + ChatColor.RED + "You must specify a time offset value.");
	                		success = false;
	            			break;
	            		}	            		
            			success = setTime(player, args[1], true);
            			break;
	            		
	            	case "wset" :
	            		if (args.length < 2 || (!args[1].equalsIgnoreCase("CLEAR") && !args[1].equalsIgnoreCase("DOWNFALL"))) {
	            			player.sendMessage(prefix + ChatColor.RED + "You must specify DOWNFALL for rain/snow or CLEAR for no rain/snow.");
	                		success = false;
	            			break;
	            		}
	            		success = setWeather(player, args[1]);
	            		break;
	
	            	case "tsync" :
	            		success = resetTime(player);
	            		break;
	            		
	            	case "wsync" :
	            		success = resetWeather(player);
	            		break;
	            		
	            	case "sync":
	            		success = resetWeather(player) && resetTime(player);
	            		break;
	            		
	            	case "status" :
	            	case "" :
	            		success = getStatus(player);
	            		break;
	
	            	default :
	                    player.sendMessage(prefix + ChatColor.RED + "Unkown command: " + subc);
	            		player.chat("/help PlayerTimeWeather");
	            }
        	} catch (Exception e) {
        		log.warning("Failed parsing command in Player Time & Weather:");
        		log.warning("	" + e.toString());
        		success = false;
        	}
            
        	if (!success) player.chat("/help " + flabel);
        	
            return true;
        }
        
        src.sendMessage("Only players may use /ptw!");        
        return true;
    }
    
    public static boolean getStatus(Player player) {
    	try {
	    	String[] msg={
	    		ChatColor.GOLD + "" + ChatColor.BOLD + "Player Weather" + ChatColor.DARK_GRAY + ":  " + ChatColor.RESET + (player.getPlayerWeather() != null ? ChatColor.GREEN + player.getPlayerWeather().toString() : ChatColor.AQUA + "Synced with server."),
	    		ChatColor.GOLD + "" + ChatColor.BOLD + "Player Time" + ChatColor.DARK_GRAY + ":        " + ChatColor.RESET + (player.getPlayerTime() != player.getWorld().getFullTime() ? (player.isPlayerTimeRelative() ? ChatColor.DARK_GREEN + "+" : "") + ChatColor.GREEN + player.getPlayerTimeOffset() : ChatColor.AQUA + "Synced with server."),
	    	};
	    	
	    	player.sendMessage(msg);
	    	
    	} catch (Exception e) {
    		log.warning("Unable to display status in Player Time & Weather:");
    		log.warning("	" + e.toString());
    		return false;
    	}
    	
    	return true;
    }
    
    public static boolean resetTime(Player player) {
    	try {
    		player.resetPlayerTime();
    		player.sendMessage(prefix + ChatColor.AQUA + "Time Syncronized");
    	} catch (Exception e) {
    		log.warning("Unable to reset time in Player Time & Weather:");
    		log.warning("	" + e.toString());
    		return false;
    	}
    	
    	return true;
    }
    
    public static boolean resetWeather(Player player) {
    	try {
    		player.resetPlayerWeather();
    		player.sendMessage(prefix + ChatColor.AQUA + "Weather Syncronized");
    	} catch (Exception e) {
    		log.warning("Unable to reset weather in Player Time & Weather:");
    		log.warning("	" + e.toString());
    		return false;
    	}
    	
    	return true;
    }
    
    public static boolean setTime(Player player, String timeString, Boolean rel) {
    	try {
    		// do any available conversions
    		long ticks;
    		if (timeString.matches("^\\d{1,2}:\\d\\d$")) {
    			ticks = timeToTicks(timeString, rel, player.getWorld().getTime());

    		} else if (timeString.matches("^[a-zA-Z]+$")) {
    			ticks = eventToTicks(timeString, rel, player.getWorld().getTime());
    		
    		} else if (timeString.matches("^\\d+$")) {
    			ticks = Long.parseLong(timeString);
    		
    		} else { throw new IllegalArgumentException("invalid time string"); }
    		
    		player.setPlayerTime(ticks, rel);
    		player.sendMessage(prefix + ChatColor.GREEN + "Personal " + (player.isPlayerTimeRelative() ? "Relative " : "") + "Time Set" + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + player.getPlayerTimeOffset());
    	} catch (NumberFormatException e) {
    		player.sendMessage(prefix + ChatColor.RED + timeString + " is not a valid time string.");
    		return false;
    	} catch (Exception e) {
    		log.warning("Unable to set time in Player Time & Weather:");
    		log.warning("	" + e.toString());
    		player.resetPlayerTime();
    		return false;
    	}
    	
    	return true;
    }
    
    public static boolean setWeather(Player player, String weatherString) {
    	try {
    		player.setPlayerWeather(WeatherType.valueOf(weatherString.toUpperCase()));
    		player.sendMessage(prefix + "" + ChatColor.GREEN + "Personal Weather Set" + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + player.getPlayerWeather());
    	} catch (Exception e) {
    		log.warning("Unable to set weather in Player Time & Weather:");
    		log.warning("	" + e.toString());
    		player.resetPlayerWeather();
    		return false;
    	}
    	
    	return true;
    }
    
    private static long timeToTicks(String timeString, boolean relative, long wTicks) throws IllegalArgumentException, NumberFormatException {
    	long outp;
    	int length = timeString.split(":").length;
    	
    	//declare and initialize t (array of bytes holding different each unit of time).
    	long[] time = new long[length];
    	for (int i=length; i>0; i--) {
    		long current = Long.valueOf(timeString.split(":")[i-1]);
    		if (current > 60 || current < 0)
    			throw new IllegalArgumentException("value out of bounds");
    		
    		time[i-1] = current;
    	}

    	/*
    	 * 1 element = hour
    	 * 2 element = hour:minute
    	 */
    	long ticks;
    	switch (length) {
    		case 1:
    			ticks = Math.round((time[0]*60)*multiplier);
    			break;
    		case 2:
    			ticks = Math.round(((time[0]*60)+time[1])*multiplier);
    			break;
    		default:
    			throw new IllegalArgumentException("invalid time format");
    	}
    	
    	if (ticks >= 6000) {
    		ticks = ticks - 6000;
    	} else {
    		ticks = ticks + 18000;
    	}
    	
		if (relative) {
	    	outp = ticks - wTicks;
	    } else {
	    	outp = ticks;
	    }
    	
    	return outp;
    }
    
    private static long eventToTicks(String event, boolean relative, long wTicks) throws IllegalArgumentException, NumberFormatException {
    	long outp;
    	
    	@SuppressWarnings("serial")
		Map<String, Long> eventMap = new HashMap<String, Long>() {{;
	    	put("sunrise", 	(long) 23000);
	    	put("morning", 	(long)     0);
	    	put("day", 		(long)  1000);
	    	put("noon", 	(long)  6000);
	    	put("sunset", 	(long) 12000);
	    	put("night", 	(long) 13000);
	    	put("midnight", (long) 18000);
	    }};
	    
		if (!eventMap.containsKey(event)) throw new IllegalArgumentException("unknown event");
	    
		if (relative) {
	    	outp = eventMap.get(event) - wTicks;
	    } else {
	    	outp = eventMap.get(event);
	    }
    	
    	return outp;
    }
}