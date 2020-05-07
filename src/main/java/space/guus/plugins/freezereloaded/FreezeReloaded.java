package space.guus.plugins.freezereloaded;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import space.guus.plugins.freezereloaded.command.FreezeCommand;
import space.guus.plugins.freezereloaded.events.*;

import java.util.ArrayList;

public final class FreezeReloaded extends JavaPlugin {

    public ArrayList<Player> frozen;
    public ArrayList<String> blockedactions;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        this.register();

        getLogger().info(String.format("FreezeReloaded (v%s) by Gusuu is now enabled.", getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        getLogger().info(String.format("FreezeReloaded (v%s) by Gusuu is now disabled.", getDescription().getVersion()));
    }

    private void register() {
        this.frozen = new ArrayList<>();
        this.blockedactions = new ArrayList<>();
        // register commands
        getCommand("freeze").setExecutor(new FreezeCommand(this));

        // register events
        getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDamage(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInventory(this), this);
        getServer().getPluginManager().registerEvents(new PlayerTeleport(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDrop(this), this);
        getServer().getPluginManager().registerEvents(new PlayerFoodLevelChange(this), this);

        getServer().getPluginManager().registerEvents(new BlockBreak(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(this), this);

        // register blocked actions
        this.blockedactions.addAll(getConfig().getStringList("block-actions"));
    }

    public String translate(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public void sendMsg(Player p, String path){
        p.sendMessage(this.translate(getConfig().getString("prefix") + getConfig().getString(path)));
    }

    public void sendIcon(Player p){
        if(!getConfig().getBoolean("icon")) return;
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f████&c█&f████"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f███&c█&6█&c█&f███"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f██&c█&6█&0█&6█&c█&f██"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f██&c█&6█&0█&6█&c█&f██"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f█&c█&6██&0█&6██&c█&f█"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f█&c█&6█████&c█&f█"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c█&6███&0█&6███&c█"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c█████████"));
    }
}
