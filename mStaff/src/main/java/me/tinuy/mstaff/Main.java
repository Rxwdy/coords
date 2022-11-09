package me.tinuy.mstaff;

import lombok.Getter;
import me.tinuy.mstaff.mod.item.ModItems;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{

    @Getter
    private FreezeCommand islicense;

    public static Main getInstance() {
        return Main.instance;
    }
    public static Main instance;

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener)new FrozenEvent(), (Plugin)this);
        pm.registerEvents((Listener)new ModeListener(), (Plugin)this);
        pm.registerEvents((Listener)new ModeItemListener(), (Plugin)this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Checking license..");
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Loading..");
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Thank you for purchasing mStaff!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "mStaff ENABLED!");
        super.getCommand("freeze").setExecutor((CommandExecutor)new FreezeCommand());
        super.getCommand("mod").setExecutor((CommandExecutor)new ModCommand());
        super.getCommand("staffchat").setExecutor((CommandExecutor)new SCCommand());
        ModItems.instate();
        String key = this.getConfig().getString("license-key");
        this.getConfig().set("license-key", key);
        if(key.contains("|")||
                key.contains("-")||
                key.contains("HWID")||
                key.contains("Username")||
                key.equalsIgnoreCase("|")||
                key.equalsIgnoreCase("-")
                ||key.equalsIgnoreCase("Username")||
                key.equalsIgnoreCase("HWID")||
                key.contains(" ")||
                key.equalsIgnoreCase(" ")||
                key.equalsIgnoreCase("")||
                key == null){
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "This plugin was not successfully licenced message Tinuy#4068 on discord.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        islicense = new FreezeCommand();
        islicense.isLicence(this);
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Join our support discord! https://discord.gg/ATskkyE");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "mStaff DISABLED!");
        this.saveDefaultConfig();
    }
}