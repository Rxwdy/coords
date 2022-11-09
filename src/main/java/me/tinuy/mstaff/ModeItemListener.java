package me.tinuy.mstaff;

import me.tinuy.mstaff.mod.item.ModItemAction;
import me.tinuy.mstaff.mod.item.ModItems;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModeItemListener implements Listener {

//    public List<String> getModGUIList() {
//        return Main.getInstance().getConfig().getStringList("online-staff.gui-list");
//    }

    private List<Material> armor = Arrays.asList(
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS
    );

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(!e.getPlayer().hasPermission("hcf.staff"))
            return;

        HCFProfile profile = HCFProfile.getByUuid(e.getPlayer().getUniqueId());
        if(e.getItem() == null)
            return;
        if(profile.getModMode() == null)
            return;
        if(e.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        if(e.getAction().name().contains("RIGHT")) {
            if(armor.contains(e.getItem().getType())) {
                e.setCancelled(true);
                e.getPlayer().updateInventory();
            }
        }

        ModItemAction action = ModItems.getActionByItem(e.getPlayer().getItemInHand());
        if(action == null)
            return;

        switch(action) {
            case VANISH_ON:
                profile.getModMode().setVanished(true);
                profile.getModMode().refreshItems(); // TODO: Make it only refresh the vanished item.
                return;
            case VANISH_OFF:
                profile.getModMode().setVanished(false);
                profile.getModMode().refreshItems(); // TODO: Make it only refresh the vanished item.
                return;
            case VANISH_TOGGLE:
                profile.getModMode().setVanished(!profile.getModMode().isVanished());
                profile.getModMode().refreshItems(); // TODO: Make it only refresh the vanished item.
                return;
            case RANDOMTP:
                List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                int randomNumber = TimeUtils.random.nextInt(players.size());
                e.getPlayer().teleport(players.get(randomNumber));
                e.getPlayer().sendMessage(C.color(Main.getInstance().getConfig().getString("mod-messages.randomly-teleported").replaceAll("%name%", players.get(randomNumber).getName())));
                return;
            case ONLINE_STAFF:
                Inventory staffInv = Bukkit.createInventory(null, 27, C.color("&bOnline Staff"));
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(!player.hasPermission("hcf.staff"))
                        continue;

                    HCFProfile staffProfile = HCFProfile.getByUuid(player.getUniqueId());
                    String modMode = ((staffProfile.getModMode() != null) ? "&aTrue" : "&cFalse");
                    String vanished = ((staffProfile.getModMode() != null) ? ((staffProfile.getModMode().isVanished()) ? "&aTrue" : "&cFalse") : "&cFalse");
                    World.Environment world = player.getWorld().getEnvironment();
                    String location = player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ();

                    ItemStack item = new ItemBuilder(
                            new ItemStack(
                                    Material.SKULL_ITEM,
                                    1,
                                    (byte) SkullType.PLAYER.ordinal()
                            )
                    ).owner(
                            player.getName()
                    ).name(
                            Main.getInstance().getConfig().getString(C.color("online-staff.player-name").replaceAll("%player%",player.getName()))
                    ).lore(
                            Main.getInstance().getConfig().getString(C.color("online-staff.gui-list.mod-mode".replaceAll("%variable%",modMode))),
                            Main.getInstance().getConfig().getString(C.color("online-staff.gui-list.vanished".replaceAll("%vanished%",vanished))),
                            Main.getInstance().getConfig().getString(C.color("online-staff.gui-list.world".replaceAll("%world%", String.valueOf(world)))),
                            Main.getInstance().getConfig().getString(C.color("online-staff.gui-list.location".replaceAll("%location%", location)))
//                            "",
//                            "&7Right-click to teleport to them."
                    ).get();

                    staffInv.addItem(item);
                }

                e.getPlayer().openInventory(staffInv);
                return;
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        if(!e.getPlayer().hasPermission("hcf.staff"))
            return;

        HCFProfile profile = HCFProfile.getByUuid(e.getPlayer().getUniqueId());
        if(profile.getModMode() == null)
            return;
        if(!(e.getRightClicked() instanceof Player))
            return;

        Player interacted = (Player) e.getRightClicked();
        if(e.getPlayer().getItemInHand() != null) {
            ModItemAction action = ModItems.getActionByItem(e.getPlayer().getItemInHand());
            if(action == null)
                return;

            switch(action) {
                case INSPECTOR:
                    e.getPlayer().openInventory(interacted.getInventory());
                    TaskUtils.runSync(() -> {
                        EntityPlayer entityPlayer = ((CraftPlayer)e.getPlayer()).getHandle();

                        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, 0, interacted.getName() + "'s Inventory", e.getPlayer().getOpenInventory().getTopInventory().getSize(), false);
                        entityPlayer.playerConnection.sendPacket(packet);
                        entityPlayer.updateInventory(entityPlayer.activeContainer);
                    });
                    return;
                case FREEZE:
                    if(interacted.hasPermission("hcf.staff")){
                        e.getPlayer().sendMessage(ChatColor.RED + "You may not freeze this player");
                        return;
                    }
                    e.getPlayer().chat("/freeze " + interacted.getName());
                    return;
                case RANDOMTP:
                    List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                    int randomNumber = TimeUtils.random.nextInt(players.size());
                    e.getPlayer().teleport(players.get(randomNumber));
                    e.getPlayer().sendMessage(C.color(Main.getInstance().getConfig().getString("mod-messages.randomly-teleported").replaceAll("%name%", players.get(randomNumber).getName())));
                    return;

            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(!e.getWhoClicked().hasPermission("hcf.staff"))
            return;
        if(e.getCurrentItem() == null)
            return;
        if(e.getCurrentItem().getItemMeta() == null)
            return;
        if(e.getClick() == null)
            return;
        if(!C.strip(e.getInventory().getName()).equals("Online Staff"))
            return;

        HCFProfile profile = HCFProfile.getByUuid(e.getWhoClicked().getUniqueId());
        if(profile.getModMode() == null)
            return;

        e.setCancelled(true);
        if(e.getClick() == ClickType.RIGHT) {
            String staffName = C.strip(e.getCurrentItem().getItemMeta().getDisplayName());
            Player staff = Bukkit.getPlayer(staffName);
            if(staff != null && staff.isOnline())
                e.getWhoClicked().teleport(staff);
        }
    }

    @EventHandler
    public void staffJoinMessage(PlayerJoinEvent e){
        if(e.getPlayer().hasPermission("hcf.staff")){
            final String format = Main.getInstance().getConfig().getString("staff.join-message").replace("&", "§").replace("%player%", e.getPlayer().getName());
            for (final Player staffs : Bukkit.getServer().getOnlinePlayers()) {
                if (staffs.hasPermission("hcf.staff")) {
                    staffs.sendMessage(format);
                }
            }
        }
    }

    @EventHandler
    public void staffLeaveMessage(PlayerQuitEvent e){
        if(e.getPlayer().hasPermission("hcf.staff")){
            final String format = Main.getInstance().getConfig().getString("staff.leave-message").replace("&", "§").replace("%player%", e.getPlayer().getName());
            for (final Player staffs : Bukkit.getServer().getOnlinePlayers()) {
                if (staffs.hasPermission("hcf.staff")) {
                    staffs.sendMessage(format);
                }
            }
        }
    }
}
