package perms.utils;

import org.bukkit.entity.Player;
import perms.managers.PermManager;

public class PrefixUtil {

    public static void apply(Player player, PermManager pm) {
        String prefix = "";

        // Manually check each role
        if (pm.isUserInRole("Owner", player)) {
            prefix = pm.getPrefix("Owner");
            player.sendMessage("Prefix " + prefix);
        } else if (pm.isUserInRole("CoOwner", player)) {
            prefix = pm.getPrefix("CoOwner");
            player.sendMessage("Prefix " + prefix);
        } else if (pm.isUserInRole("Mod", player)) {
            prefix = pm.getPrefix("Mod");
            player.sendMessage("Prefix " + prefix);
        } else if (pm.isUserInRole("Helper", player)) {
            prefix = pm.getPrefix("Helper");
            player.sendMessage("Prefix " + prefix);
        } else if (pm.isUserInRole("Member", player)) {
            prefix = pm.getPrefix("Member");
            player.sendMessage("Prefix " + prefix);
        }

        if (prefix == null) prefix = "403";
        player.setDisplayName(prefix + player.getName());
        player.setPlayerListName(prefix + player.getName());
        player.sendMessage("Prefix " + prefix);
    }
}
