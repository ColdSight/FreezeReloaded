package space.guus.plugins.freezereloaded.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import space.guus.plugins.freezereloaded.FreezeReloaded;

public class PlayerChat implements Listener {

    private FreezeReloaded plugin;

    public PlayerChat(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(AsyncPlayerChatEvent e){
        if (!plugin.blockedactions.contains("CHAT")) return;
        Player p = e.getPlayer();
        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
            plugin.sendMsg(p, "chat");
        }
    }
}
