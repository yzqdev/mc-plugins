package fyi.sugar.mobstoeggs.events;

import fyi.sugar.mobstoeggs.Main;
import fyi.sugar.mobstoeggs.data.EggDetermineData;
import fyi.sugar.mobstoeggs.data.MaterialData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EggCraftEvent  implements Listener {
    private static Main plugin = Main.getPlugin(Main.class);

    public static void onEggCraft(EggDetermineData data) {
        Player player = data.getCatcher();
        Entity entity = data.getEntity();
        Material material = MaterialData.getEggMaterial(entity.getType());
        String catchparticle = plugin.cm.getSettings().getString("catch-effects.particle-effect").toUpperCase();
        int particlenum = plugin.cm.getSettings().getInt("catch-effects.particle-count");
        double particlex = plugin.cm.getSettings().getDouble("catch-effects.particle-x");
        double particley = plugin.cm.getSettings().getDouble("catch-effects.particle-y");
        double particlez = plugin.cm.getSettings().getDouble("catch-effects.particle-z");
        String catchsound = plugin.cm.getSettings().getString("catch-effects.sound").toUpperCase();
        float catchsoundvolume = (float) plugin.cm.getSettings().getDouble("catch-effects.sound-volume");
        float catchsoundpitch = (float) plugin.cm.getSettings().getDouble("catch-effects.sound-pitch");

        ItemStack dropegg = new ItemStack(material);
        ItemMeta dropmeta = dropegg.getItemMeta();

        String customName = entity.getCustomName();
        dropmeta.setDisplayName(customName);


        if (entity instanceof Pig && (
                (Pig) entity).hasSaddle()) {
            entity.getWorld().dropItem(entity.getLocation(), new ItemStack(Material.SADDLE, 1));
        }


        dropegg.setItemMeta(dropmeta);
        entity.getWorld().dropItem(entity.getLocation(), dropegg);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.cm.getMessages().getString("catch-message").replaceAll("%mobname%", entity.getName())));
        entity.getWorld().spawnParticle(Particle.valueOf(catchparticle), entity.getLocation(), particlenum, particlex, particley, particlez, 0.0D);
        entity.getWorld().playSound(entity.getLocation(), Sound.valueOf(catchsound), catchsoundvolume, catchsoundpitch);
        entity.remove();
    }
}


