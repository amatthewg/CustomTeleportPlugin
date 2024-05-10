package com.pepperish.customteleportplugin.managers;


/*
Let "playerAmount" equal the size of the set of all connected Players p,
such that p.hasPermission(Permission.SHOULD_BE_TELEPORTED.getString())

Let "blocksAvailable" equal the size of the set availableBlockLocations (LocationManager.availableBlockLocations)

POSSIBILITY 1: Admin confirms command "/ctp tpall" when playerAmount > blocksAvailable
    This will result in 'x1' players being teleported, such that x1 = blocksAvailable, and will further result in
    'y1' players failing to be teleported, such that y1 = playerAmount - blocksAvailable.

    POSSIBILITY 1A: Admin subsequently confirms command "/ctp return"
            This will result in x1 players being teleported to their pre-teleport location, and will further result in
            y1 players not being teleported
    POSSIBILITY 1B: Admin subsequently executes command "/ctp

 */
public class CommandStateManager {
}
