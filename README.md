# CustomTeleportPlugin (1.1)
**Minecraft Java (Paper) Plugin**

## Overview
Minecraft Paper plugin that allows server admins to create customized "seating" locations for players. Useful for Minecraft server events where seating should be enforced on players, like concerts, churches, etc. 

## Features
### Commands
1. /ctp tool: Retrieves the block selection tool. Right-click a block to add, left-click to remove.
2. /ctp viewing: Enable viewing the set blocks
3. /ctp warn: Send a title message to all players, warning them of imminent teleportatio
4. /ctp tpall: Teleport each player to their own block and stop them from walking/jumping/flying
5. /ctp return: Return all teleported players to their original location and allow player movement
6. /ctp cancel: Cancel the execution of a command that needs confirmation

### Permission Nodes
1. ctp.admin: Grants permission to CTP commands and the use of the block selection tool
2. ctp.shouldbeteleported: Causes a player to be teleported by the plugin. If you don't want a specific group/player to be teleported, you must deny this permission
