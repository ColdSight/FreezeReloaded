/*
 * FreezeReloaded - Copyright (c) 2020.
 * Author: Gusuu/Mugai
 * GitHub Repository: https://github.com/gusuu1/FreezeReloaded
 */

package space.guus.plugins.freezereloaded.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import space.guus.plugins.freezereloaded.FreezeReloaded;

public class PlayerTeleport implements Listener {

    private FreezeReloaded plugin;

    public PlayerTeleport(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerTeleportEvent e){
        if (!plugin.blockedactions.contains("TELEPORT")) return;
        Player p = e.getPlayer();
        if(plugin.frozen.contains(p)){
            if(e.getCause().equals(PlayerTeleportEvent.TeleportCause.UNKNOWN)) return;
            e.setCancelled(true);
        }
    }
}
