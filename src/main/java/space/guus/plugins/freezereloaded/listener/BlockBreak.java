/*
 * FreezeReloaded - Copyright (c) 2020.
 * Author: Gusuu/Mugai
 * GitHub Repository: https://github.com/gusuu1/FreezeReloaded
 */

package space.guus.plugins.freezereloaded.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import space.guus.plugins.freezereloaded.FreezeReloaded;

public class BlockBreak implements Listener {

    private FreezeReloaded plugin;

    public BlockBreak(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(BlockBreakEvent e){
        if (!plugin.blockedactions.contains("BREAK")) return;
        Player p = e.getPlayer();

        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "break");
        }
    }
}
