package space.guus.plugins.freezereloaded.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import space.guus.plugins.freezereloaded.FreezeReloaded;

public class PlayerInteract implements Listener {

    private FreezeReloaded plugin;

    public PlayerInteract(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerInteractEvent e){
        if (!plugin.blockedactions.contains("INTERACT")) return;
        Player p = e.getPlayer();
        if(plugin.frozen.contains(p) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            e.setCancelled(true);
            plugin.sendMsg(p, "interact");
        }
    }
}
