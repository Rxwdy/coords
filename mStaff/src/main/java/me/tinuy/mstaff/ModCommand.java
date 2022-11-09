package me.tinuy.mstaff;

import me.tinuy.mstaff.mod.ModMode;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ModCommand implements CommandExecutor
{

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            HCFProfile profile = HCFProfile.getByPlayer(p);
            if(p.hasPermission("mstaff.staffmode")){
                if(profile.getModMode() == null) {
                    profile.setModMode(new ModMode(p));
                    profile.getModMode().enable();
                    p.sendMessage(C.color(Main.getInstance().getConfig().getString("mod-messages.enabled")));
                    p.setGameMode(GameMode.CREATIVE);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 5, false));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 5, false));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 5, false));
                } else {
                    profile.getModMode().disable();
                    profile.setModMode(null);
                    p.sendMessage(C.color(Main.getInstance().getConfig().getString("mod-messages.disabled")));
                    p.setGameMode(GameMode.SURVIVAL);
                    p.removePotionEffect(PotionEffectType.SATURATION);
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                }
            }
        }
        return false;
    }
}
