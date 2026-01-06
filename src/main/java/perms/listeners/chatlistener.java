package perms.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.entity.Player;
import perms.managers.PermManager;
import perms.managers.PermsUtils;
import java.util.List;

public class chatlistener implements Listener {

    private final PermManager permManager;
    private final PermsUtils permsUtils;

    public chatlistener(PermManager permManager, PermsUtils permUtils) {
        this.permManager = permManager;
        this.permsUtils = permUtils;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String command = message.substring(1).split(" ")[0].toLowerCase();
        List<String> allCommands = permsUtils.getAllCommands();
        boolean commandExists = allCommands.contains(command);
        if (commandExists && !permManager.canUseCommand(player, command)) {
            event.setCancelled(true);
            player.sendMessage("§c You dont have perms to use this command!");
        }

    }
}
