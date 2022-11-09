package me.tinuy.mstaff.mod;

import lombok.Getter;
import lombok.Setter;
import me.tinuy.mstaff.PlayerInv;
import me.tinuy.mstaff.mod.item.ModItems;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
public class ModMode {

    public static final String teamName = "hidden";

    @Getter private final Player player;
    @Getter private boolean vanished;

    @Getter @Setter private PlayerInv inv;

    public ModMode(Player player) {
        this.player = player;
        this.vanished = false;
    }

    public void enable() {
        // Setting the player to vanished.
        this.setVanished(true);

        // Setting items into arrays for later use when mod-mode is disabled.
        this.inv = PlayerInv.fromPlayer(this.player.getInventory());

        // Updating player's inventory items.
        this.refreshItems();

        // Setting the player to Creative Mode.
        this.player.setGameMode(GameMode.CREATIVE);
        this.player.setAllowFlight(true);
    }

    public void disable() {
        this.setVanished(false);

        if(this.inv != null)
            this.inv.load(this.player);

        this.player.setGameMode(GameMode.SURVIVAL);
        this.player.setAllowFlight(false);
    }

    public void refreshItems() {
        // Clearing the player's entire inventory so that their items don't interfere with the mod-mode ones.
        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);

        // Setting the player's inventory to the mod-mode inventory.
        ModItems.getItemsFor(player).forEach(this.player.getInventory()::setItem);

        // Updating the player's inventory.
        this.player.updateInventory();
    }

    public void setVanished(boolean vanished) {
        this.vanished = vanished;

        if(vanished) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(!player.hasPermission("hcf.staff")) {
                    if(player.getName().equals(this.player.getName()))
                        continue;

                    player.hidePlayer(this.player);
                } else {
                    if(player.hasMetadata("showstaffmetadata")){
                        player.hidePlayer(this.player);
                    }else{
                        player.showPlayer(this.player);
                    }
                }
            }
        } else {
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.showPlayer(this.player);
            }
        }
    }
}
