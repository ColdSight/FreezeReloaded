package space.guus.plugins.freezereloaded.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
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

}
