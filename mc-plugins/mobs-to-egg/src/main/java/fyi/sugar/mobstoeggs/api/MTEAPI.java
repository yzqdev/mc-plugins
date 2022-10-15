package fyi.sugar.mobstoeggs.api;

import fyi.sugar.mobstoeggs.events.CapsuleHitEvent;
import fyi.sugar.mobstoeggs.events.CapsuleThrowEvent;


public class MTEAPI {
    public static CapsuleThrowEvent capsuleTypeOnThrow(CapsuleThrowEvent capsule) {
        return capsule;
    }


    public static boolean hasMobHitByCapsule(CapsuleHitEvent onCapsuleHit) {
        boolean hasBeenHit = true;
        return hasBeenHit;
    }


    public static String mobTypeCapsuleHit() {
        String mobName = "Mob";
        return mobName;
    }


    public static boolean hasMobCustomName() {
        boolean hasName = true;
        return hasName;
    }


    public static boolean hasMobTraits() {
        boolean hasTraits = true;
        return hasTraits;
    }


    public static String mobTypeTraits() {
        String mobTrait = "Mob";
        return mobTrait;
    }
}


