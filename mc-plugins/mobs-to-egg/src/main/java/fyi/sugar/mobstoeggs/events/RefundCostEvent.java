package fyi.sugar.mobstoeggs.events;

import fyi.sugar.mobstoeggs.Main;
import fyi.sugar.mobstoeggs.data.CreateCapsuleData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class RefundCostEvent {
    private static Main plugin = (Main) Main.getPlugin(Main.class);


    public static void getRefundEvent(Player refundPlayer, int refundAmount) {
        if (!plugin.cm.getSettings().getBoolean("refund-egg-on-fail")) {
            return;
        }

        if (!refundPlayer.getGameMode().equals(GameMode.CREATIVE)) {
            CreateCapsuleData.getCapsule(refundPlayer, refundAmount);
        }
    }
}


