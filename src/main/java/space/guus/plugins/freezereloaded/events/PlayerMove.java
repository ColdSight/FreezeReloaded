package space.guus.plugins.freezereloaded.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import space.guus.plugins.freezereloaded.FreezeReloaded;

public class PlayerMove implements Listener {

    private FreezeReloaded plugin;

    public PlayerMove(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerMoveEvent e){
        if (!plugin.blockedactions.contains("MOVE")) return;
        Player p = e.getPlayer();
        if(plugin.frozen.contains(p)){
            e.setTo(e.getFrom());
            plugin.sendIcon(p);
            for(String s : plugin.getConfig().getStringList("frozen")){
                p.sendMessage(plugin.translate(s));
            }
        }
    }

}
