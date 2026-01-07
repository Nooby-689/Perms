package perms.utils;
import org.bukkit.entity.Player;
import perms.managers.PermManager;

public class PrefixUtil {

    public static void apply(Player player, PermManager pm) {
        String prefix = "";

        if (pm.isUserInRole("staff.Owner", player))
            prefix = "§c[Owner] §r";
        else if (pm.isUserInRole("staff.CoOwner", player))
            prefix = "§4[CoOwner] §r";
        else if (pm.isUserInRole("staff.Mod", player))
            prefix = "§2[Mod] §r";
        else if (pm.isUserInRole("staff.Helper", player))
            prefix = "§b[Helper] §r";
        else if (pm.isUserInRole("members.member", player))
            prefix = "§7[Member] §r";

        player.setDisplayName(prefix + player.getName());
        player.setPlayerListName(prefix + player.getName());
    }
}
