/*
 * FreezeReloaded - Copyright (c) 2020.
 * Author: Gusuu/Mugai
 * GitHub Repository: https://github.com/gusuu1/FreezeReloaded
 * SpigotMC: https://www.spigotmc.org/resources/freezereloaded-1-8-1-15-simple-freeze-plugin-highly-configurable.77762/
 */

package space.guus.freezereloaded.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.guus.freezereloaded.FreezeReloaded;

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
                    p.sendMessage(plugin.translate(plugin.getConfig().getString("wrong-usage")));
                    if(p.hasPermission("freeze.reload")){
                        p.sendMessage(plugin.translate(plugin.getConfig().getString("reload-usage")));
                    }
                    return true;
                }else{
                    if(args[0].equalsIgnoreCase("reload")){
                        if(p.hasPermission("freeze.reload")){
                            plugin.reloadConfig();
                            plugin.sendMsg(p, "reload");
                            return true;
                        }else{
                            plugin.sendMsg((Player)sender, "no-permission");
                            return true;
                        }
                    }
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
                    plugin.sendIcon(target);
                    for(String s : plugin.getConfig().getStringList("frozen")){
                        target.sendMessage(plugin.translate(s));
                    }
                    return true;
                }
            }else{
                plugin.sendMsg((Player)sender, "no-permission");
            }
        }else{
            plugin.getLogger().warning(plugin.getConfig().getString("only-players"));
        }
        return true;
    }
}
