package fyi.sugar.mobstoeggs.data;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;


public class MaterialData {
    public static Material getEggMaterial(EntityType entitytype) {
        String getMaterial = entitytype.toString() + "_SPAWN_EGG";
        Material getEntityEgg = null;

        try {
            getEntityEgg = Material.valueOf(getMaterial);
        } catch (IllegalArgumentException e) {
            NonEggMobData[] arrayOfNonEggMobData;
            int i;
            byte b;
            for (arrayOfNonEggMobData = NonEggMobData.values(), i = arrayOfNonEggMobData.length, b = 0; b < i; ) {
                NonEggMobData theName = arrayOfNonEggMobData[b];
                if (!theName.getEntityType().equals(entitytype)) {
                    b++;
                    continue;
                }
                return theName.getEntityMaterial();
            }

        }
        return getEntityEgg;
    }
}


