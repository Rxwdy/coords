package me.tinuy.mstaff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AnnouncementCommand implements CommandExecutor
{
    public HashMap<String, Long> cooldowns;

    public AnnouncementCommand() {
        this.cooldowns = new HashMap<String, Long>();
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if (sender.hasPermission("hcf.announcement")) {
                if (args.length >= 1){
                    StringBuilder message = new StringBuilder();
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("broadcast.prefix")+" "+message.toString()));
                } else {
                    sender.sendMessage(C.color(Main.getInstance().getConfig().getString("announcement.usage")));
                }
            }
            else {
                sender.sendMessage(Main.getInstance().getConfig().getString(C.color("announcement.no-permission")).replace("&", "§"));
            }
        }
        return false;
    }
}
