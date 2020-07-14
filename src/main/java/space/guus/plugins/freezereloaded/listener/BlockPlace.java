/*
 * FreezeReloaded - Copyright (c) 2020.
 * Author: Gusuu/Mugai
 * GitHub Repository: https://github.com/gusuu1/FreezeReloaded
 */

package space.guus.plugins.freezereloaded.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import space.guus.plugins.freezereloaded.FreezeReloaded;

public class BlockPlace implements Listener {

    private FreezeReloaded plugin;

    public BlockPlace(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(BlockPlaceEvent e){
        if (!plugin.blockedactions.contains("PLACE")) return;
        Player p = e.getPlayer();

        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "place");
        }
    }
}
