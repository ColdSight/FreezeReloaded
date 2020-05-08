/*
 * FreezeReloaded - Copyright (c) 2020.
 * Author: Gusuu/Mugai
 * GitHub Repository: https://github.com/gusuu1/FreezeReloaded
 */

package space.guus.plugins.freezereloaded.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import space.guus.plugins.freezereloaded.FreezeReloaded;

public class PlayerQuit implements Listener {

    private FreezeReloaded plugin;

    public PlayerQuit(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(plugin.frozen.contains(p)){
            plugin.frozen.remove(p);
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("command").replaceAll("%player%", p.getDisplayName()));

            for(Player pp : Bukkit.getOnlinePlayers()){
                if(pp.hasPermission("freeze.alert")){
                    pp.sendMessage(plugin.translate(plugin.getConfig().getString("alert").replaceAll("%player%", p.getDisplayName())));
                }
            }
        }
    }
}
