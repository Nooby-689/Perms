package perms;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.command.Command;
import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import perms.managers.PluginManager;
import perms.listeners.PlayerListener;
import perms.Files.files;
import java.nio.file.Files;

public class Perms extends JavaPlugin {
    private static Perms ts;
    public files config;
    public files players;
    public files perms;

    @Override
    public void onEnable() {
        ts = this;
        PluginManager.getInstance().initialize();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getLogger().info("Perms has been enabled!");

        Plugin plugin = Perms.getLib();
        if (plugin == null) {
            throw new IllegalStateException("Perms plugin is dependent on the lib 'dml' but the lib is not found!");
        }
        config = new files("config.yml", this);
        players = new files("players.yml", this);
        perms = new files("perms.yml", this);
        
        if (!players.get().contains("staff.Owner")) players.get().set("staff.Owner", null);
        if (!players.get().contains("staff.Mod")) players.get().set("staff.Mod", null);
        if (!players.get().contains("staff.Helper")) players.get().set("staff.Helper", null);
        if (!players.get().contains("staff.CoOwner")) players.get().set("staff.CoOwner", null);
        if (!players.get().contains("members.member")) players.get().set("members.member", null);
        players.save();
        

        Bukkit.getScheduler().runTask(this, () -> {
            List<String> commandList = new ArrayList<>();
            try {
                Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                CommandMap commandMap = (CommandMap) f.get(Bukkit.getServer());

                Field knownField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownField.setAccessible(true);
                Map<String, Command> knownCommands = (Map<String, Command>) knownField.get(commandMap);

                commandList.addAll(knownCommands.keySet());
            } catch (Exception e) {
                getLogger().severe("Failed to fetch server commands!");
                e.printStackTrace();
            }

            String commandsStr = String.join(",", commandList);
            getLogger().info("Commands line: " + commandsStr);
        });
    }   




    @Override
    public void onDisable() {
        getLogger().info("Perms has been disabled!");
    }

    public static Perms getInstance() {
        return ts;

    }
    public static Plugin getLib() {
        Plugin plugin = getInstance().getServer().getPluginManager().getPlugin("dml");
        if (plugin == null || !plugin.isEnabled()) {
            getInstance().getLogger().warning("dml lib is not loaded!");
            return null;
        }
        return plugin;
    }
}