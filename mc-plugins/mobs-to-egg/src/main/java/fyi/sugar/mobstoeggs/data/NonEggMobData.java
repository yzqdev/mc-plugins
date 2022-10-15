package fyi.sugar.mobstoeggs.data;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum NonEggMobData {
    /**
     * 僵尸猪灵
     */
    PIG_ZOMBIE(EntityType.PIG_ZOMBIE, Material.ZOMBIE_PIGMAN_SPAWN_EGG),
    MOOSHROOM(EntityType.MUSHROOM_COW, Material.MOOSHROOM_SPAWN_EGG),
    IRON_GOLEM(EntityType.IRON_GOLEM, Material.IRON_INGOT),
    ENDER_DRAGON(EntityType.ENDER_DRAGON, Material.END_STONE),
    WITHER(EntityType.WITHER, Material.SOUL_SAND);

    private EntityType entityType;

    private Material entityMaterial;


    NonEggMobData(EntityType entityType, Material entityMaterial) {
        this.entityType = entityType;
        this.entityMaterial = entityMaterial;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Material getEntityMaterial() {
        return this.entityMaterial;
    }
}


