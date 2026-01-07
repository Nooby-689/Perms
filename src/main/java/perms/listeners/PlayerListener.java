package perms.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import perms.managers.PermManager;
import perms.utils.PrefixUtil;

public class PlayerListener implements Listener {

    private final PermManager permManager;

    public PlayerListener(PermManager permManager) {
        this.permManager = permManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PrefixUtil.apply(event.getPlayer(), permManager);
    }
}