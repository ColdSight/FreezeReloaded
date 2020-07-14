/*
 * FreezeReloaded - Copyright (c) 2020.
 * Author: Gusuu/Mugai
 * GitHub Repository: https://github.com/gusuu1/FreezeReloaded
 * SpigotMC: https://www.spigotmc.org/resources/freezereloaded-1-8-1-15-simple-freeze-plugin-highly-configurable.77762/
 */

package space.guus.freezereloaded.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import space.guus.freezereloaded.FreezeReloaded;

import java.util.HashMap;
import java.util.UUID;

public class FreezeListener implements Listener {

    private FreezeReloaded plugin;
    private HashMap<UUID, Long> lastMessage = new HashMap<>();

    public FreezeListener(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if (!plugin.blockedactions.contains("INVENTORY")) return;

        Player p = (Player)e.getWhoClicked();
        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "inventory");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if (!plugin.blockedactions.contains("MOVE")) return;
        Player p = e.getPlayer();
        if(plugin.frozen.contains(p)){
            int x = (int) e.getFrom().getX();
            int y = (int) e.getFrom().getY();
            int z = (int) e.getFrom().getZ();

            int x1 = (int) e.getTo().getX();
            int y1 = (int) e.getTo().getY();
            int z1 = (int) e.getTo().getZ();

            if(!(x != x1 || y != y1 || z != z1)) return;
            // do nothing, because the player only moved their camera
            p.teleport(e.getFrom(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
            if(plugin.getConfig().getBoolean("notify-only-freeze")) return;
            if(lastMessage.containsKey(p.getUniqueId())){
                if(lastMessage.get(p.getUniqueId()) < System.currentTimeMillis()){
                    plugin.sendIcon(p);
                    for(String s : plugin.getConfig().getStringList("frozen")){
                        p.sendMessage(plugin.translate(s));
                    }
                    lastMessage.put(p.getUniqueId(), System.currentTimeMillis() + 10000);
                }
            }else{
                plugin.sendIcon(p);
                for(String s : plugin.getConfig().getStringList("frozen")){
                    p.sendMessage(plugin.translate(s));
                }
                lastMessage.put(p.getUniqueId(), System.currentTimeMillis() + 10000);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        if (!plugin.blockedactions.contains("INTERACT")) return;
        Player p = e.getPlayer();
        if(plugin.frozen.contains(p) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            e.setCancelled(true);
            plugin.sendMsg(p, "interact");
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e){
        if (!plugin.blockedactions.contains("FOOD")) return;
        Player p = (Player)e.getEntity();
        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
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

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e){
        if (!plugin.blockedactions.contains("TELEPORT")) return;
        Player p = e.getPlayer();
        if(plugin.frozen.contains(p)){
            if(e.getCause().equals(PlayerTeleportEvent.TeleportCause.UNKNOWN)) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
        if (!plugin.blockedactions.contains("DROP")) return;
        Player p = e.getPlayer();

        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "drop");
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e){
        if (!plugin.blockedactions.contains("CHAT")) return;
        Player p = e.getPlayer();
        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "chat");
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        if (!plugin.blockedactions.contains("DAMAGE")) return;
        if(e.getEntity() instanceof Player){
            Player p = (Player)e.getEntity();
            if(plugin.frozen.contains(p)){
                e.setCancelled(true);
                if(e.getDamager() instanceof Player){
                    plugin.sendMsg((Player)e.getDamager(), "no-attack");
                }
            }
        }
        if(e.getDamager() instanceof Player){
            Player p = (Player)e.getDamager();
            if(plugin.frozen.contains(p)){
                e.setCancelled(true);
                plugin.sendMsg(p, "no-attack-frozen");
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if (!plugin.blockedactions.contains("GODMODE")) return;
        if(e.getEntityType().equals(EntityType.PLAYER)){
            Player p = (Player)e.getEntity();
            if(plugin.frozen.contains(p)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (!plugin.blockedactions.contains("PLACE")) return;
        Player p = e.getPlayer();

        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "place");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if (!plugin.blockedactions.contains("BREAK")) return;
        Player p = e.getPlayer();

        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "break");
        }
    }




}
