/*
 * FreezeReloaded - Copyright (c) 2020.
 * Author: Gusuu/Mugai
 * GitHub Repository: https://github.com/gusuu1/FreezeReloaded
 */

package space.guus.plugins.freezereloaded.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import space.guus.plugins.freezereloaded.FreezeReloaded;

public class PlayerFoodLevelChange implements Listener {

    private FreezeReloaded plugin;

    public PlayerFoodLevelChange(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(FoodLevelChangeEvent e){
        if (!plugin.blockedactions.contains("FOOD")) return;
        Player p = (Player)e.getEntity();
        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
        }
    }
}
