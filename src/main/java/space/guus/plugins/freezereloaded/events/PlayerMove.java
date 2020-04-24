package space.guus.plugins.freezereloaded.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import space.guus.plugins.freezereloaded.FreezeReloaded;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMove implements Listener {

    private FreezeReloaded plugin;
    private HashMap<UUID, Long> lastMessage = new HashMap<>();

    public PlayerMove(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerMoveEvent e){
        if (!plugin.blockedactions.contains("MOVE")) return;
        Player p = e.getPlayer();
        if(plugin.frozen.contains(p)){
            e.setCancelled(true);
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

}
