package fyi.sugar.mobstoeggs.data;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EggDetermineData {
    Player player;
    Entity capsule;
    Entity entity;

    public EggDetermineData(Player player, Entity capsule, Entity entity) {
        this.player = player;
        this.capsule = capsule;
        this.entity = entity;
    }

    public Player getCatcher() {
        return this.player;
    }

    public Entity getCapsule() {
        return this.capsule;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}


