package perms.listeners;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.entity.Player;
import perms.managers.PermManager;

public class chatlistener implements Listener {

    private final PermManager permManager;

    public chatlistener(PermManager permManager) {
        this.permManager = permManager;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage(); 
        String command = message.substring(1).split(" ")[0]; 

        if (!permManager.canUseCommand(player, command)) {
            event.setCancelled(true); 
            player.sendMessage("§cYou do not have permission to use this command!");
        }
    }
}
