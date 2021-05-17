package space.guus.freezereloaded;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import space.guus.freezereloaded.command.FreezeCommand;
import space.guus.freezereloaded.listener.FreezeListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class FreezeReloaded extends JavaPlugin {

    public ArrayList<Player> frozen;
    public ArrayList<String> blockedactions;

    private File messagesFile;
    private FileConfiguration messagesConfiguration;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        createMessages();

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

        // register listener
        getServer().getPluginManager().registerEvents(new FreezeListener(this), this);

        // register blocked actions
        this.blockedactions.addAll(getConfig().getStringList("block-actions"));
    }

    public String translate(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public void sendMsg(Player p, String path){
        p.sendMessage(this.translate(getConfig().getString("prefix") + getMessages().getString(path)));
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

    private void createMessages() {
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        messagesConfiguration = new YamlConfiguration();
        try {
            messagesConfiguration.load(messagesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getMessages(){
        return this.messagesConfiguration;
    }
}
