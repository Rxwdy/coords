package me.tinuy.mstaff;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class FreezeCommand implements CommandExecutor
{
    public static List<UUID> paniced;
    public FreezeCommand() {
        paniced = new ArrayList<>();
    }

    public List<String> getFrozenMessage() {
        return Main.getInstance().getConfig().getStringList("freeze.frozen-message");
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if(sender instanceof Player){
            if(!sender.hasPermission("mstaff.freeze")){
                return false;
            }
            if(args.length < 1) {

                sender.sendMessage(C.color(Main.getInstance().getConfig().getString("freeze.freeze-usage")));
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage(C.color(Main.getInstance().getConfig().getString("freeze.player-not-found").replaceAll("%name%", args[0])));
                return false;
            }
            if(target.hasMetadata("frozen")) {
                paniced.remove(target.getUniqueId());
                target.removeMetadata("frozen", Main.getInstance());

                target.sendMessage(C.color(Main.getInstance().getConfig().getString("freeze.player-unfrozen")));
                sender.sendMessage(C.color(Main.getInstance().getConfig().getString("freeze.successfully-unfrozen").replaceAll("%player%", target.getName())));
                return false;
            }
            target.setMetadata("frozen", new FixedMetadataValue(Main.getInstance(), true));

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!target.isOnline() || !target.hasMetadata("frozen")) {
                        cancel();
                        return;
                    }
                    for(String string : getFrozenMessage()) {
                        target.sendMessage(C.color(string));
                    }
                }
            }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L * Main.getInstance().getConfig().getInt("freeze.message-delay"));

            sender.sendMessage(C.color(Main.getInstance().getConfig().getString("freeze.frozen-player").replaceAll("%player%", target.getName())));
        }
        return false;
    }
    public void isLicence(Main plugin) {
        String key = plugin.getConfig().getString("license-key");
        if (key.contains("|") || key.contains("-") || key.contains("HWID") || key.contains("Username") ||
                key.equalsIgnoreCase("|") || key.equalsIgnoreCase("-")
                || key.equalsIgnoreCase("Username") || key.equalsIgnoreCase("HWID") ||
                key.contains(" ") || key.equalsIgnoreCase(" ") || key.equalsIgnoreCase("") || key == null) {
            plugin.getLogger().info("This plugin was not successfully licenced.");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        try {
            String url = "https://pastebin.com/raw/fHtN6sfH";
            URLConnection openConnection = new URL(url).openConnection();
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            @SuppressWarnings("resource")
            Scanner scan = new Scanner((new InputStreamReader(openConnection.getInputStream())));
            while (scan.hasNextLine()) {
                String firstline = scan.nextLine();
                if (firstline.contains("mStaff")) {
                    if (key.contains("|") || key.contains("-") || key.contains("HWID") || key.contains("Username") ||
                            key.equalsIgnoreCase("|") || key.equalsIgnoreCase("-")
                            || key.equalsIgnoreCase("Username") || key.equalsIgnoreCase("HWID") ||
                            key.contains(" ") || key.equalsIgnoreCase(" ") || key.equalsIgnoreCase("") || key == null) {
                        plugin.getLogger().info("This plugin was not successfully licenced.");
                        Bukkit.getPluginManager().disablePlugin(plugin);
                        return;
                    }
                    String customer = scan.nextLine();
                    if (customer.contains(" " + key + " ")) {
                        return;
                    }
                    if (scan.hasNextLine()) {
                        String customera = scan.nextLine();
                        if (customera.contains(" " + key + " ")) {
                            return;
                        }
                    }
                    if (scan.hasNextLine()) {
                        String customera = scan.nextLine();
                        if (customera.contains(" " + key + " ")) {
                            return;
                        }
                    }
                    if (scan.hasNextLine()) {
                        String customera = scan.nextLine();
                        if (customera.contains(" " + key + " ")) {
                            return;
                        }
                    }
                    if (scan.hasNextLine()) {
                        String customera = scan.nextLine();
                        if (customera.contains(" " + key + " ")) {
                            return;
                        }
                    }
                    if (scan.hasNextLine()) {
                        String customera = scan.nextLine();
                        if (customera.contains(" " + key + " ")) {
                            return;
                        }
                    }
                    if (scan.hasNextLine()) {
                        String customera = scan.nextLine();
                        if (customera.contains(" " + key + " ")) {
                            return;
                        }
                    }
                    if (scan.hasNextLine()) {
                        String customera = scan.nextLine();
                        if (customera.contains(" " + key + " ")) {
                            return;
                        }
                    }
                    if (scan.hasNextLine()) {
                        String customera = scan.nextLine();
                        if (customera.contains(" " + key + " ")) {
                            return;
                        }
                    }
                    if (scan.hasNextLine()) {
                        String customera = scan.nextLine();
                        if (customera.contains(" " + key + " ")) {
                            return;
                        }
                    }
                    plugin.getLogger().info("This plugin was not successfully licenced.");
                    Bukkit.getPluginManager().disablePlugin(plugin);
                    return;
                }
            }
        } catch (Exception e) {

        }
        plugin.getLogger().info("This plugin was not successfully licenced.");
        Bukkit.getPluginManager().disablePlugin(plugin);
        return;
    }
}
