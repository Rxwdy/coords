package me.tinuy.mstaff;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.Listener;

import java.util.List;

public class FrozenEvent implements Listener
{
    public List<String> getFrozenDisconnectedMessage() {
        return Main.getInstance().getConfig().getStringList("freeze.disconnected-broadcast");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuitFreeze(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playername = event.getPlayer().getDisplayName();

        if (player.hasMetadata("frozen")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.getInstance().getConfig().getString("freeze.disconnect-command").replaceAll("%player%", playername));

            for (String string : getFrozenDisconnectedMessage()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "announcement " + string.replaceAll("%player%", playername));
            }
        }
        event.setQuitMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageFrozen(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.hasMetadata("frozen")) {
                event.setCancelled(true);
                if (event.getDamager() instanceof Player) {
                    Player damager = (Player) event.getDamager();
                    damager.sendMessage(C.color(Main.getInstance().getConfig().getString("freeze.cannot-do-this")));
                }
            }
        }

        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (player.hasMetadata("frozen")) {
                event.setCancelled(true);
                player.sendMessage(C.color(Main.getInstance().getConfig().getString("freeze.cannot-do-this")));
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.hasMetadata("frozen"))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMoveFrozen(final PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }
        final Player player = event.getPlayer();
        if (player.hasMetadata("frozen")) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractFrozen(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (player.hasMetadata("frozen")) {
            e.setCancelled(true);
            player.sendMessage(C.color(Main.getInstance().getConfig().getString("freeze.cannot-do-this")));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDropFrozen(PlayerDropItemEvent e) {
        Player player = e.getPlayer();

        if (player.hasMetadata("frozen")) {
            e.setCancelled(true);
            player.sendMessage(C.color(Main.getInstance().getConfig().getString("freeze.cannot-do-this")));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickupFrozen(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();

        if (player.hasMetadata("frozen")) {
            e.setCancelled(true);
            player.sendMessage(C.color(Main.getInstance().getConfig().getString("freeze.cannot-do-this")));
        }
    }
}