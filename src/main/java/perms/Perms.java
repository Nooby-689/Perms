package perms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import perms.managers.PermManager;
import perms.managers.PluginManager;
import perms.Files.files;

import perms.commands.executeCommand;
import perms.commands.permsCommand;
import perms.listeners.ChatListeners;
import perms.listeners.PlayerListener;

public class Perms extends JavaPlugin {

    private static Perms ts;
    private PermManager permManager;

    public files config;
    public files players;
    public files perms;

    @Override
    public void onEnable() {
        ts = this;

        PluginManager.getInstance().initialize();

        config = new files("config.yml", this);
        players = new files("players.yml", this);
        perms = new files("perms.yml", this);

        players.reload();
        permManager = new PermManager(players.get());
        getCommand("ex").setExecutor(new executeCommand(permManager));
        getCommand("perms").setExecutor(new permsCommand(permManager));
        getCommand("perms").setTabCompleter(new permsCommand(permManager));
        getServer().getPluginManager().registerEvents(new PlayerListener(permManager), this);
        getServer().getPluginManager().registerEvents(new ChatListeners(permManager), this);

        // def structure for the files
        if (!players.get().isSet("Owner.commands")) players.get().set("Owner.commands", new ArrayList<>());
        if (!players.get().isSet("Mod.commands")) players.get().set("Mod.commands", new ArrayList<>());
        if (!players.get().isSet("Helper.commands")) players.get().set("Helper.commands", new ArrayList<>());
        if (!players.get().isSet("CoOwner.commands")) players.get().set("CoOwner.commands", new ArrayList<>());
        if (!players.get().isSet("member.commands")) players.get().set("member.commands", new ArrayList<>());
        if (!players.get().isSet("Owner.users")) players.get().set("Owner.users", new ArrayList<>());
        if (!players.get().isSet("Mod.users")) players.get().set("Mod.users", new ArrayList<>());
        if (!players.get().isSet("Helper.users")) players.get().set("Helper.users", new ArrayList<>());
        if (!players.get().isSet("CoOwner.users")) players.get().set("CoOwner.users", new ArrayList<>());
        if (!players.get().isSet("member.users")) players.get().set("member.users", new ArrayList<>());
        if (!players.get().isSet("Owner.prefix")) players.get().set("Owner.prefix", "s");
        if (!players.get().isSet("Mod.prefix")) players.get().set("Mod.prefix", "s");
        if (!players.get().isSet("Helper.prefix")) players.get().set("Helper.prefix", "s");
        if (!players.get().isSet("CoOwner.prefix")) players.get().set("CoOwner.prefix", "s");
        if (!players.get().isSet("member.prefix")) players.get().set("member.prefix", "s");
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
            perms.get().set("CommandsList", commandList);
            perms.save();
        });
        Bukkit.getScheduler().runTaskTimer(this, () -> {players.save();}, 20L * 30, 20L * 30);
        getLogger().info("Perms has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Perms has been disabled!");
    }

    public static Perms getInstance() {
        return ts;
    }
}
