package perms;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.Command;
import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import perms.managers.PluginManager;
import perms.listeners.PlayerListener;
import perms.Files.files;

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
        config = new files("config.yml", this);
        players = new files("players.yml", this);
        perms = new files("perms.yml", this);
        
        if (!players.get().contains("staff.Owner")) players.get().set("staff.Owner", "");
        if (!players.get().contains("staff.Mod")) players.get().set("staff.Mod", "");
        if (!players.get().contains("staff.Helper")) players.get().set("staff.Helper", "");
        if (!players.get().contains("staff.CoOwner")) players.get().set("staff.CoOwner", "");
        if (!players.get().contains("members.member")) players.get().set("members.member", "");
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
            perms.get().set("CommandsList", commandsStr);
            perms.save();
        });
        perms.save();
    }   




    @Override
    public void onDisable() {
        getLogger().info("Perms has been disabled!");
    }

    public static Perms getInstance() {
        return ts;

    }
}