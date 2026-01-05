package perms;

import org.bukkit.plugin.java.JavaPlugin;
import perms.managers.PluginManager;
import perms.listeners.PlayerListener;

public class Perms extends JavaPlugin {
    
    @Override
    public void onEnable() {
        
        // Initialize managers
        PluginManager.getInstance().initialize();
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        
        getLogger().info("Perms has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Perms has been disabled!");
    }
    
}