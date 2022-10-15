package fyi.sugar.mobstoeggs.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

public class CapsuleThrowEvent {
    public static EntityType onCapsuleProjectile(EntityType capsuletype) {
        String getEntity = capsuletype.toString().toUpperCase();
        EntityType capsule = null;

        try {
            capsule = EntityType.valueOf(getEntity);
        } catch (NullPointerException e) {
            System.out.println("" + ChatColor.RED + "MTE | No entity type could be found! " + ChatColor.RED);
            return null;
        }
        return capsule;
    }
}


