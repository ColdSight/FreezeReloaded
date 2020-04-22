package space.guus.plugins.freezereloaded.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.guus.plugins.freezereloaded.FreezeReloaded;

public class FreezeCommand implements CommandExecutor {

    private FreezeReloaded plugin;

    public FreezeCommand(FreezeReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("freeze.use")){
                if(args.length == 0){
                    p.sendMessage(plugin.translate("&cWrong usage! Usage: /freeze <player>"));
                    return true;
                }else{
                    Player target = Bukkit.getPlayerExact(args[0]);

                    if(target == null){
                        plugin.sendMsg(p, "no-player");
                        return true;
                    }

                    if(plugin.frozen.contains(target)){
                        plugin.frozen.remove(target);
                        p.sendMessage(plugin.translate(plugin.getConfig().getString("unfreeze").replaceAll("%player%", target.getDisplayName())));
                        return true;
                    }

                    plugin.frozen.add(target);
                    target.closeInventory();
                    p.sendMessage(plugin.translate(plugin.getConfig().getString("freeze").replaceAll("%player%", target.getDisplayName())));
                    return true;
                }
            }else{
                plugin.sendMsg((Player)sender, "no-permission");
            }
        }else{
            plugin.getLogger().warning("Sorry, only players may execute this command.");
        }
        return true;
    }
}
