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
                    p.sendMessage(plugin.translate(plugin.getMessages().getString("Wrong-Usage")));
                    if(p.hasPermission("freeze.reload")){
                        p.sendMessage(plugin.translate(plugin.getMessages().getString("Reload.Usage")));
                    }
                    return true;
                }else{
                    if(args[0].equalsIgnoreCase("reload")){
                        if(p.hasPermission("freeze.reload")){
                            plugin.reloadConfig();
                            plugin.sendMsg(p, "Reload.Success");
                            return true;
                        }else{
                            plugin.sendMsg((Player)sender, "No-Permission");
                            return true;
                        }
                    }
                    Player target = Bukkit.getPlayerExact(args[0]);

                    if(target == null){
                        plugin.sendMsg(p, "Player.Not-Found");
                        return true;
                    }

                    if(plugin.frozen.contains(target)){
                        plugin.frozen.remove(target);
                        p.sendMessage(plugin.translate(plugin.getMessages().getString("Player.Unfreeze").replaceAll("%player%", target.getDisplayName())));
                        target.sendMessage(plugin.translate(plugin.getMessages().getString("Player.Unfreeze-Notify")));
                        return true;
                    }

                    plugin.frozen.add(target);
                    target.closeInventory();
                    p.sendMessage(plugin.translate(plugin.getMessages().getString("Player.Freeze").replaceAll("%player%", target.getDisplayName())));
                    plugin.sendIcon(target);
                    for(String s : plugin.getMessages().getStringList("Frozen")){
                        target.sendMessage(plugin.translate(s));
                    }
                    return true;
                }
            }else{
                plugin.sendMsg((Player)sender, "No-Permission");
            }
        }else{
            plugin.getLogger().warning(plugin.getMessages().getString("Only-Players"));
        }
        return true;
    }
}
