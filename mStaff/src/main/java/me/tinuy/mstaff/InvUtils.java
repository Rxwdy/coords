package me.tinuy.mstaff;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InvUtils {

    public static String invToString(final PlayerInv inv) {
        if (inv == null) {
            return "null";
        }
        final StringBuilder builder = new StringBuilder();
        final ItemStack[] armor = inv.getArmor();
        for (int i = 0; i < armor.length; ++i) {
            if (i == 3) {
                if (armor[i] == null) {
                    builder.append(itemStackToString(new ItemStack(Material.AIR)));
                }
                else {
                    builder.append(itemStackToString(armor[3]));
                }
            }
            else if (armor[i] == null) {
                builder.append(itemStackToString(new ItemStack(Material.AIR))).append(";");
            }
            else {
                builder.append(itemStackToString(armor[i])).append(";");
            }
        }
        builder.append("|");
        for (int i = 0; i < inv.getItems().length; ++i) {
            builder.append(i).append("#").append(itemStackToString(inv.getItems()[i])).append((i == inv.getItems().length - 1) ? "" : ";");
        }
        return builder.toString();
    }

    public static PlayerInv invFromString(final String in) {
        if (in == null || in.equals("unset") || in.equals("null") || in.equals("'null'")) {
            return null;
        }
        final PlayerInv inv = new PlayerInv();
        final String[] data = in.split("\\|");
        final ItemStack[] armor = new ItemStack[data[0].split(";").length];
        for (int i = 0; i < data[0].split(";").length; ++i) {
            armor[i] = itemStackFromString(data[0].split(";")[i]);
        }
        inv.setArmor(armor);
        final ItemStack[] contents = new ItemStack[data[1].split(";").length];
        for (final String s : data[1].split(";")) {
            final int slot = Integer.parseInt(s.split("#")[0]);
            if (s.split("#").length == 1) {
                contents[slot] = null;
            }
            else {
                contents[slot] = itemStackFromString(s.split("#")[1]);
            }
        }
        inv.setItems(contents);
        return inv;
    }

    public static String itemStackToString(final ItemStack item) {
        final StringBuilder builder = new StringBuilder();
        if (item != null) {
            final String isType = String.valueOf(item.getType().getId());
            builder.append("t@").append(isType);
            if (item.getDurability() != 0) {
                final String isDurability = String.valueOf(item.getDurability());
                builder.append(":d@").append(isDurability);
            }
            if (item.getAmount() != 1) {
                final String isAmount = String.valueOf(item.getAmount());
                builder.append(":a@").append(isAmount);
            }
            final Map<Enchantment, Integer> isEnch = (Map<Enchantment, Integer>)item.getEnchantments();
            if (isEnch.size() > 0) {
                for (final Map.Entry<Enchantment, Integer> ench : isEnch.entrySet()) {
                    builder.append(":e@").append(ench.getKey().getId()).append("@").append(ench.getValue());
                }
            }
            if (item.hasItemMeta()) {
                final ItemMeta imeta = item.getItemMeta();
                if (imeta.hasDisplayName()) {
                    builder.append(":dn@").append(imeta.getDisplayName());
                }
                if (imeta.hasLore()) {
                    builder.append(":l@").append(imeta.getLore());
                }
            }
        }
        return builder.toString();
    }

    public static ItemStack itemStackFromString(final String in) {
        ItemStack item = null;
        ItemMeta meta = null;
        if (in.equals("null")) {
            return new ItemStack(Material.AIR);
        }
        final String[] split2;
        final String[] split = split2 = in.split(":");
        for (final String itemInfo : split2) {
            final String[] itemAttribute = itemInfo.split("@");
            final String s4;
            final String s2 = s4 = itemAttribute[0];
            switch (s4) {
                case "t": {
                    item = new ItemStack(Material.getMaterial((int)Integer.valueOf(itemAttribute[1])));
                    meta = item.getItemMeta();
                    break;
                }
                case "d": {
                    if (item != null) {
                        item.setDurability((short)Short.valueOf(itemAttribute[1]));
                        break;
                    }
                    break;
                }
                case "a": {
                    if (item != null) {
                        item.setAmount((int)Integer.valueOf(itemAttribute[1]));
                        break;
                    }
                    break;
                }
                case "e": {
                    if (meta == null && item != null) {
                        meta = item.getItemMeta();
                    }
                    if (meta != null) {
                        meta.addEnchant(Enchantment.getById((int)Integer.valueOf(itemAttribute[1])), (int)Integer.valueOf(itemAttribute[2]), true);
                        break;
                    }
                    break;
                }
                case "dn": {
                    if (meta != null) {
                        meta.setDisplayName(itemAttribute[1]);
                        break;
                    }
                    break;
                }
                case "l": {
                    itemAttribute[1] = itemAttribute[1].replace("[", "");
                    itemAttribute[1] = itemAttribute[1].replace("]", "");
                    final List<String> lore = Arrays.asList(itemAttribute[1].split(","));
                    for (int x = 0; x < lore.size(); ++x) {
                        String s3 = lore.get(x);
                        if (s3 != null && s3.toCharArray().length != 0) {
                            if (s3.charAt(0) == ' ') {
                                s3 = s3.replaceFirst(" ", "");
                            }
                            lore.set(x, s3);
                        }
                    }
                    if (meta != null) {
                        meta.setLore((List)lore);
                        break;
                    }
                    break;
                }
            }
        }
        if (meta != null) {
            item.setItemMeta(meta);
        }
        return item;
    }

    public static void fillSidesWithItem(final Inventory inv, final ItemStack item) {
        final int size = inv.getSize();
        final int rows = size / 9;
        if (rows >= 3) {
            for (int i = 0; i <= 8; ++i) {
                inv.setItem(i, item);
            }
            for (int s = 8; s < inv.getSize() - 9; s += 9) {
                final int lastSlot = s + 1;
                inv.setItem(s, item);
                inv.setItem(lastSlot, item);
            }
            for (int lr = inv.getSize() - 9; lr < inv.getSize(); ++lr) {
                inv.setItem(lr, item);
            }
        }
    }

    public static void addItems(final Player player, final ItemStack... itemStacks) {
        final Map<Integer, ItemStack> itemMap = (Map<Integer, ItemStack>)player.getInventory().addItem(itemStacks);
        if (!itemMap.isEmpty()) {
            itemMap.values().forEach(itemStack -> player.getWorld().dropItem(player.getLocation(), itemStack));
        }
    }

    public static Integer getAmount(final Player player, final Material material) {
        int amount = 0;
        final ItemStack[] contents;
        final ItemStack[] itemStacks = contents = player.getInventory().getContents();
        for (final ItemStack itemStack : contents) {
            if (itemStack != null) {
                if (itemStack.getType() != Material.AIR) {
                    if (itemStack.getType() == material) {
                        amount += itemStack.getAmount();
                    }
                }
            }
        }
        return amount;
    }

    public static List<ItemStack> getAllFromMaterial(final Player player, final Material material) {
        final List<ItemStack> items = new ArrayList<ItemStack>();
        final ItemStack[] contents;
        final ItemStack[] itemStacks = contents = player.getInventory().getContents();
        for (final ItemStack itemStack : contents) {
            if (itemStack != null) {
                if (itemStack.getType() != Material.AIR) {
                    if (itemStack.getType() == material) {
                        items.add(itemStack);
                    }
                }
            }
        }
        return items;
    }

    public static Integer getAmount(final Player player, final ItemStack item) {
        int amount = 0;
        final ItemStack[] contents;
        final ItemStack[] itemStacks = contents = player.getInventory().getContents();
        for (final ItemStack itemStack : contents) {
            if (itemStack != null) {
                if (itemStack.getType() != Material.AIR) {
                    if (itemStack.isSimilar(item)) {
                        amount += itemStack.getAmount();
                    }
                }
            }
        }
        return amount;
    }

    public static List<ItemStack> getAllFromStack(final Player player, final ItemStack item) {
        final List<ItemStack> items = new ArrayList<ItemStack>();
        final ItemStack[] contents;
        final ItemStack[] itemStacks = contents = player.getInventory().getContents();
        for (final ItemStack itemStack : contents) {
            if (itemStack != null) {
                if (itemStack.getType() != Material.AIR) {
                    if (itemStack.isSimilar(item)) {
                        items.add(itemStack);
                    }
                }
            }
        }
        return items;
    }
}
