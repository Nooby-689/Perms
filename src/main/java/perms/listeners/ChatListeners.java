package perms.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.entity.Player;
import perms.managers.PermManager;

public class ChatListeners implements Listener {

    private final PermManager permManager;

    public ChatListeners(PermManager permManager) {
        this.permManager = permManager;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage(); 

        if (message.length() < 2) return; 
        String command = message.substring(1).split(" ")[0].toLowerCase();

        permManager.checkCommandAndNotify(player, command);
        if (!permManager.canUseCommand(player, command)) {
            event.setCancelled(true);
        }
    }
}