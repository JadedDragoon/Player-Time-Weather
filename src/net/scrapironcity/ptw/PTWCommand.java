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

public class PTWCommand implements CommandExecutor {
	private static final String PREFIX = ChatColor.GOLD + "" + ChatColor.BOLD + "PTW" + ChatColor.DARK_GRAY + " - " + ChatColor.RESET;
	private static final String DENIED = PREFIX + ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is a mistake.";
	private static final double MULTIPLIER = 1000 / (double) 60;

	private Logger log = Bukkit.getLogger();

	@Override
	public boolean onCommand(CommandSender src, Command cmd, String label, String[] args) {
		String flabel = label + (args.length > 0 ? " " + args[0] : "");
		if (src instanceof Player) {
			Player player = (Player) src;
			boolean showHelp = false;
			String subc = args.length > 0 ? args[0] : "status";

			try {
				switch (subc) {
				case "tset":
					if (!player.hasPermission("ptw.tset")) {
						player.sendMessage(DENIED);
						break;
					}
					if (args.length < 2) {
						player.sendMessage(PREFIX + ChatColor.RED + "You must specify a time value.");
						showHelp = true;
						break;
					}

					showHelp = !setTime(player, args[1], false);
					break;

				case "rtset":
					if (!player.hasPermission("ptw.rtset")) {
						player.sendMessage(DENIED);
						break;
					}
					if (args.length < 2) {
						player.sendMessage(PREFIX + ChatColor.RED + "You must specify a time offset value.");
						showHelp = true;
						break;
					}

					showHelp = !setTime(player, args[1], true);
					break;

				case "wset":
					if (!player.hasPermission("ptw.wset")) {
						player.sendMessage(DENIED);
						break;
					}
					if (args.length < 2	|| (!args[1].equalsIgnoreCase("CLEAR") && !args[1].equalsIgnoreCase("DOWNFALL"))) {
						player.sendMessage(PREFIX + ChatColor.RED + "You must specify DOWNFALL for rain/snow or CLEAR for no rain/snow.");
						showHelp = true;
						break;
					}

					showHelp = !setWeather(player, args[1]);
					break;

				case "tsync":
					if (!player.hasPermission("ptw.tsync")) {
						player.sendMessage(DENIED);
						break;
					}

					showHelp = !resetTime(player);
					break;

				case "wsync":
					if (!player.hasPermission("ptw.wsync")) {
						player.sendMessage(DENIED);
						break;
					}

					showHelp = !resetWeather(player);
					break;

				case "sync":
					if (!player.hasPermission("ptw.sync")) {
						player.sendMessage(DENIED);
						break;
					}

					showHelp = !(resetWeather(player) && resetTime(player));
					break;

				case "status":
					if (!player.hasPermission("ptw.status")) {
						player.sendMessage(DENIED);
						break;
					}

					showHelp = !getStatus(player);
					break;

				default:
					player.sendMessage(PREFIX + ChatColor.RED + "Unkown command: " + subc);
					player.chat("/help PlayerTimeWeather");
				}
			} catch (Exception e) {
				log.warning("Failed parsing command in Player Time & Weather:");
				log.warning("	" + e.toString());
			}

			if (showHelp)
				player.chat("/help " + flabel);

			return true;
		}

		src.sendMessage("Only players may use /ptw!");
		return true;
	}

	boolean getStatus(Player player) {
		return getStatus(player, true);
	}
	
	boolean getStatus(Player player, boolean doMsg) {
		try {
			String[] msg = {
				ChatColor.GOLD + "" + ChatColor.BOLD + "Player Weather" + ChatColor.DARK_GRAY + ":  " + ChatColor.RESET + (player.getPlayerWeather() != null ? ChatColor.GREEN + player.getPlayerWeather().toString() : ChatColor.AQUA + "Synced with server."),
				ChatColor.GOLD + "" + ChatColor.BOLD + "Player Time" + ChatColor.DARK_GRAY + ":        " + ChatColor.RESET + (player.getPlayerTime() != player.getWorld().getFullTime() ? (player.isPlayerTimeRelative() ? ChatColor.DARK_GREEN + "+" : "") + ChatColor.GREEN + player.getPlayerTimeOffset() : ChatColor.AQUA + "Synced with server."),
			};

			if (doMsg) player.sendMessage(msg);

		} catch (Exception e) {
			log.warning("Unable to display status in Player Time & Weather:");
			log.warning("	" + e.toString());
			return false;
		}

		return true;
	}

	boolean resetTime(Player player) {
		return resetTime(player, true);
	}
	
	boolean resetTime(Player player, boolean doMsg) {
		try {
			player.resetPlayerTime();
			if (doMsg) player.sendMessage(PREFIX + ChatColor.AQUA + "Time Syncronized");
		} catch (Exception e) {
			log.warning("Unable to reset time in Player Time & Weather:");
			log.warning("	" + e.toString());
			return false;
		}

		return true;
	}
	
	boolean resetWeather(Player player) {
		return resetWeather(player, true);
	}

	boolean resetWeather(Player player, boolean doMsg) {
		try {
			player.resetPlayerWeather();
			if (doMsg) player.sendMessage(PREFIX + ChatColor.AQUA + "Weather Syncronized");
		} catch (Exception e) {
			log.warning("Unable to reset weather in Player Time & Weather:");
			log.warning("	" + e.toString());
			return false;
		}

		return true;
	}

	boolean setTime(Player player, String timeString, Boolean rel) {
		return setTime(player, timeString, rel, true);
	}
	
	boolean setTime(Player player, String timeString, Boolean rel, boolean doMsg) {
		try {
			// do any available conversions
			long ticks;
			if (timeString.matches("^\\d{1,2}:\\d\\d$")) {
				ticks = timeToTicks(timeString, rel, player.getWorld().getTime());

			} else if (timeString.matches("^[a-zA-Z]+$")) {
				ticks = eventToTicks(timeString, rel, player.getWorld().getTime());

			} else if (timeString.matches("^\\d+$")) {
				ticks = Long.parseLong(timeString);

			} else {
				throw new IllegalArgumentException("invalid time string");
			}

			player.setPlayerTime(ticks, rel);
			if (doMsg) player.sendMessage(PREFIX + ChatColor.GREEN + "Personal " + (player.isPlayerTimeRelative() ? "Relative " : "") + "Time Set" + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + player.getPlayerTimeOffset());
			
		} catch (NumberFormatException e) {
			if (doMsg) player.sendMessage(PREFIX + ChatColor.RED + timeString + " is not a valid time string.");
			return false;
			
		} catch (IllegalArgumentException e) {
			if (doMsg) player.sendMessage(PREFIX + ChatColor.RED + timeString + " is not a valid time string.");
			return false;
			
		} catch (Exception e) {
			log.warning("Unable to set time in Player Time & Weather:");
			log.warning("	" + e.toString());
			player.resetPlayerTime();
			return false;
		}

		return true;
	}
	
	boolean setWeather(Player player, String weatherString) {
		return setWeather(player, weatherString, true);
	}

	boolean setWeather(Player player, String weatherString, boolean doMsg) {
		try {
			player.setPlayerWeather(WeatherType.valueOf(weatherString.toUpperCase()));
			if (doMsg) player.sendMessage(PREFIX + "" + ChatColor.GREEN + "Personal Weather Set" + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + player.getPlayerWeather());
		} catch (Exception e) {
			log.warning("Unable to set weather in Player Time & Weather:");
			log.warning("	" + e.toString());
			player.resetPlayerWeather();
			return false;
		}

		return true;
	}

	private long timeToTicks(String timeString, boolean relative, long wTicks) throws IllegalArgumentException, NumberFormatException {
		long outp;
		int length = timeString.split(":").length;

		// declare and initialize t (array of bytes holding different each unit of
		// time).
		long[] time = new long[length];
		for (int i = length; i > 0; i--) {
			long current = Long.valueOf(timeString.split(":")[i - 1]);
			if (current > 60 || current < 0)
				throw new IllegalArgumentException("value out of bounds");

			time[i - 1] = current;
		}

		/*
		 * 1 element = hour 2 element = hour:minute
		 */
		long ticks;
		switch (length) {
		case 1:
			ticks = Math.round((time[0] * 60) * MULTIPLIER);
			break;
		case 2:
			ticks = Math.round(((time[0] * 60) + time[1]) * MULTIPLIER);
			break;
		default:
			throw new IllegalArgumentException("invalid time format");
		}

		if (ticks >= 6000) {
			ticks -= 6000;
		} else {
			ticks += 18000;
		}

		if (relative) {
			outp = ticks - wTicks;
		} else {
			outp = ticks;
		}

		return outp;
	}

	private long eventToTicks(String event, boolean relative, long wTicks) throws IllegalArgumentException, NumberFormatException {
		long outp;

		@SuppressWarnings("serial")
		Map<String, Long> eventMap = new HashMap<String, Long>() {{
			put("sunrise",  (long) 23000);
			put("morning",  (long)     0);
			put("day", 	    (long)  1000);
			put("noon",     (long)  6000);
			put("sunset",   (long) 12000);
			put("night",    (long) 13000);
			put("midnight", (long) 18000);
		}};

		if (!eventMap.containsKey(event))
			throw new IllegalArgumentException("unknown event");

		if (relative) {
			outp = eventMap.get(event) - wTicks;
		} else {
			outp = eventMap.get(event);
		}

		return outp;
	}

}