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
import space.guus.freezereloaded.AlertType;
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
            plugin.sendMsg(p, "Player.Inventory");
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
                    for(String s : plugin.getMessages().getStringList("Frozen")){
                        p.sendMessage(plugin.translate(s));
                    }
                    lastMessage.put(p.getUniqueId(), System.currentTimeMillis() + 10000);
                }
            }else{
                plugin.sendIcon(p);
                for(String s : plugin.getMessages().getStringList("Frozen")){
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
            plugin.sendMsg(p, "Player.Interact");
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

            this.alertStaff(p, null, AlertType.LOGOUT);
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
            plugin.sendMsg(p, "Player.Drop");
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e){
        if (!plugin.blockedactions.contains("CHAT")) return;
        Player p = e.getPlayer();
        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "Player.Chat");
            this.alertStaff(p, e.getMessage(), AlertType.CHAT);
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
                    plugin.sendMsg((Player)e.getDamager(), "Player.No-Attack");
                }
            }
        }
        if(e.getDamager() instanceof Player){
            Player p = (Player)e.getDamager();
            if(plugin.frozen.contains(p)){
                e.setCancelled(true);
                plugin.sendMsg(p, "Player.No-Attack-Frozen");
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
            plugin.sendMsg(p, "Player.Place");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if (!plugin.blockedactions.contains("BREAK")) return;
        Player p = e.getPlayer();

        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "Player.Break");
        }
    }

    private void alertStaff(Player player, String message, AlertType type){
        for(Player pp : Bukkit.getOnlinePlayers()){
            if(pp.hasPermission("freeze.alert")){
                if(type.equals(AlertType.LOGOUT)){
                    pp.sendMessage(plugin.translate(plugin.getMessages().getString("Staff.Alert").replaceAll("%player%", player.getDisplayName())));
                }else if(type.equals(AlertType.CHAT)){
                    pp.sendMessage(plugin.translate(plugin.getMessages().getString("Staff.Chat-Notify")
                            .replaceAll("%player%", player.getDisplayName())
                            .replaceAll("%message%", message)
                    ));
                }
            }
        }
    }

}
