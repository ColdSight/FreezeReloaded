package space.guus.plugins.freezereloaded.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import space.guus.plugins.freezereloaded.FreezeReloaded;

public class PlayerDrop implements Listener {

    private FreezeReloaded plugin;

    public PlayerDrop(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerDropItemEvent e){
        if (!plugin.blockedactions.contains("DROP")) return;
        Player p = e.getPlayer();

        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "drop");
        }
    }

}
