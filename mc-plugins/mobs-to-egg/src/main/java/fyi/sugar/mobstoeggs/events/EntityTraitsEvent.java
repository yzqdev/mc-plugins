package fyi.sugar.mobstoeggs.events;

import fyi.sugar.mobstoeggs.data.EggDetermineData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public class EntityTraitsEvent
        implements Listener {
    public static void onEntityTraits(EggDetermineData data) {
        Player player = data.getCatcher();
        Entity damager = data.getEntity();
        Entity entity = data.getCapsule();


        EggCraftEvent.onEggCraft(new EggDetermineData(player, entity, damager));
    }
}


