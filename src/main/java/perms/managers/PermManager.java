package perms.managers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import perms.Perms;
import perms.utils.PrefixUtil;

public final class PermManager {

    public final FileConfiguration config;

    public PermManager(FileConfiguration config) {
        this.config = config;
    }

    public boolean canUseCommand(Player player, String command) {
        command = command.toLowerCase();
        if (player.isOp()) return true;

        for (String role : getAllRoles()) {
            if (isUserInRole(role, player)) {
                List<String> cmds = config.getStringList(role + ".commands");
                if (cmds != null && cmds.contains(command)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isUserInRole(String role, Player player) {
        List<String> users = config.getStringList(role + ".users");
        return users != null && users.contains(player.getName());
    }

    public List<String> getAllRoles() {
        if (config.getKeys(false) == null) return new ArrayList<>();
        return new ArrayList<>(config.getKeys(false));
    }

    public void addCommand(String role, String command, Player player) {
        command = command.toLowerCase();
        List<String> commands = config.getStringList(role + ".commands");
        if (commands.contains(command)) {
            player.sendMessage("§cThis command is already in the role!");
        } else {
            commands.add(command);
            config.set(role + ".commands", commands);
            player.sendMessage("§aCommand added to role " + role + "!");
        }
    }

    public void removeCommand(String role, String command, Player player) {
        command = command.toLowerCase();
        List<String> commands = config.getStringList(role + ".commands");
        if (!commands.contains(command)) {
            player.sendMessage("§cThis command is not in the role!");
        } else {
            commands.remove(command);
            config.set(role + ".commands", commands);
            player.sendMessage("§aCommand removed from role " + role + "!");
        }
    }


    public void addPlayer(String role, String playerName, Player executor) {
        List<String> users = config.getStringList(role + ".users");
        if (users.contains(playerName)) {
            executor.sendMessage("§cThis player is already in the role!");
            return;
        }
        users.add(playerName);
        config.set(role + ".users", users);
        executor.sendMessage("§aPlayer added to role " + role + "!");
        PrefixUtil.apply(executor, this);
    }

    public void removePlayer(String role, String playerName, Player executor) {
        List<String> users = config.getStringList(role + ".users");
        if (!users.contains(playerName)) {
            executor.sendMessage("§cThis player is not in the role!");
            return;
        }
        users.remove(playerName);
        config.set(role + ".users", users);
        executor.sendMessage("§aPlayer removed from role " + role + "!");
    }

    public void setPrefix(String role, String newPrefix, Player executor) {
        if (!config.contains(role)) {
            executor.sendMessage("§cRole does not exist: " + role);
            return;
        }

        config.set(role + ".prefix", newPrefix);
        Perms.getInstance().players.save();

        PrefixUtil.apply(executor, this);
        executor.sendMessage("§aPrefix set for role " + role + ": " + newPrefix);
    }

    public String getPrefix(String role) {
        if (!config.contains(role + ".prefix")) return "";
        return config.getString(role + ".prefix", "");
    }

    public void checkCommandAndNotify(Player player, String command) {
        boolean inRole = getAllRoles().stream().anyMatch(role -> isUserInRole(role, player));

        if (!inRole) {
            List<String> members = config.getStringList("Member.users");
            members.add(player.getName());
            config.set("Member.users", members);
        }

        if (!canUseCommand(player, command)) {
            player.sendMessage("§cYou do not have permission to use §e/" + command);
        }
    }
}
