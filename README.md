# Player Time & Weather
A spigot plugin for controlling the apparent time and weather of a player without affecting other players or the server (actual) time and weather.

## Setup

No configuration. No permissions. No Dependencies. The commands only ever affect the player executing them. Simply place the .jar file in your `spigot/plugins` folder and restart your Minecraft server.

## Commands
Commands can only be used by a player in-game.

### /ptw
**description**: Alias to /pts status. \
**usage**: /ptw
   
### /ptw status
**description**: View your current Player Time & Weather status. \
**usage**: /ptw status
   
### /ptw tset
**description**: Set your personal (apparent) time. \
**usage**: /ptw tset \<time\>
  
### /ptw rtset
**description**: Set your relative personal (apparent) time. \
**usage**: /ptw rtset \<time-offset\>
  
### /ptw tsync
**description**: Synchronize your personal (apparent) time with the server (actual) time. \
**usage**: /ptw tsync

### /ptw wset
**description**: Set your personal (apparent) weather \
**usage**: /ptw tset \<CLEAR|DOWNFALL\>

### /ptw wsync
**description**: Synchronize your personal (apparent) weather with the server (actual) weather. \
**usage**: /ptw tsync

### /ptw sync
**description**: Synchronize both your personal time and weather with the server.
**usage**: /ptw sync