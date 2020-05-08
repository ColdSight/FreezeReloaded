/*
 * FreezeReloaded - Copyright (c) 2020.
 * Author: Gusuu/Mugai
 * GitHub Repository: https://github.com/gusuu1/FreezeReloaded
 */

package space.guus.plugins.freezereloaded.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import space.guus.plugins.freezereloaded.FreezeReloaded;

public class PlayerInventory implements Listener {

    private FreezeReloaded plugin;

    public PlayerInventory(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(InventoryClickEvent e){
        if (!plugin.blockedactions.contains("INVENTORY")) return;

        Player p = (Player)e.getWhoClicked();
        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "inventory");
        }
    }
}
