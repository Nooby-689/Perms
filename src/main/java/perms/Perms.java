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
        
        if (!players.get().isSet("staff.Owner.commands")) players.get().set("staff.Owner.commands", new ArrayList<>());
        if (!players.get().isSet("staff.Mod.commands")) players.get().set("staff.Mod.commands", new ArrayList<>());
        if (!players.get().isSet("staff.Helper.commands")) players.get().set("staff.Helper.commands", new ArrayList<>());
        if (!players.get().isSet("staff.CoOwner.commands")) players.get().set("staff.CoOwner.commands", new ArrayList<>());
        if (!players.get().isSet("members.member.commands")) players.get().set("members.member.commands", new ArrayList<>());
        if (!players.get().isSet("staff.Owner.users")) players.get().set("staff.Owner.users", new ArrayList<>());
        if (!players.get().isSet("staff.Mod.users")) players.get().set("staff.Mod.users", new ArrayList<>());
        if (!players.get().isSet("staff.Helper.users")) players.get().set("staff.Helper.users", new ArrayList<>());
        if (!players.get().isSet("staff.CoOwner.users")) players.get().set("staff.CoOwner.users", new ArrayList<>());
        if (!players.get().isSet("members.member.users")) players.get().set("members.member.users", new ArrayList<>());
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

