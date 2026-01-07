package perms.managers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import perms.utils.PrefixUtil;

public final class PermManager {

    private final FileConfiguration config;

    public PermManager(FileConfiguration config) {
        this.config = config;
    }

    public boolean canUseCommand(Player player, String command) {
        command = command.toLowerCase();
        if (player.isOp()) {
            return true;
        }
        ConfigurationSection staff = config.getConfigurationSection("staff");
        if (staff != null) {
            for (String role : staff.getKeys(false)) {
                String basePath = "staff." + role;
                if (isUserInRole(basePath, player)) {
                    List<String> cmds = config.getStringList(basePath + ".commands");
                    if (cmds.contains(command)) {
                        return true;
                    }
                }
            }
        }
        if (isUserInRole("members.member", player)) {
            return config.getStringList("members.member.commands")
                    .contains(command);
        }
        return false;
    }

    public boolean isUserInRole(String path, Player player) {
        return config.getStringList(path + ".users")
                .contains(player.getName());
    }

    public List<String> getStaffRoles() {
        ConfigurationSection staff = config.getConfigurationSection("staff");
        if (staff != null) {
            return new ArrayList<>(staff.getKeys(false));
        }
        return new ArrayList<>();
    }

    public List<String> getCommandsForRole(String path) {
        return config.getStringList(path + ".commands");
    }

    public void checkCommandAndNotify(Player player, String command) {
        boolean inStaff = false;
        ConfigurationSection staff = config.getConfigurationSection("staff");
        if (staff != null) {
            for (String role : staff.getKeys(false)) {
                if (isUserInRole("staff." + role, player)) {
                    inStaff = true;
                    break;
                }
            }
        }

        boolean inMembers = isUserInRole("members.member", player);

        if (!inStaff && !inMembers) {
            List<String> members = config.getStringList("members.member.users");
            members.add(player.getName());
            config.set("members.member.users", members);
            inMembers = true;
        }

        if (canUseCommand(player, command)) {
        } else {
            player.sendMessage("§cYou do not have perms to use the command §e/" + command);
        }
    }



    public void AddCommand(String rolePath, String command, Player player) {
        command = command.toLowerCase();
        List<String> commands = config.getStringList(rolePath + ".commands");
        if (commands.contains(command)) {
            player.sendMessage("§cThis command is already in the role!");
        } else {
            commands.add(command);
            config.set(rolePath + ".commands", commands);
            player.sendMessage("Done my guy :D");
        }
    }

    public void RemoveCommand(String rolePath, String command, Player player) {
        command = command.toLowerCase();
        List<String> commands = config.getStringList(rolePath + ".commands");
        if (!commands.contains(command)) {
            player.sendMessage("§cThis command is not in the role!");
        } else {
            commands.remove(command);
            config.set(rolePath + ".commands", commands);
            player.sendMessage("Done my guy :D");
        }
    }

    public void AddPlayer(String rolePath, String playerName, Player player) {
        List<String> users = config.getStringList(rolePath + ".users");
        if (users.contains(playerName)) {
            player.sendMessage("§cThis player is already in the role!");
        } else {
            users.add(playerName);
            config.set(rolePath + ".users", users);
            player.sendMessage("Done my guy :D");
            PrefixUtil.apply(player, this);
        }
    }

    public void RemovePlayer(String rolePath, String playerName, Player player) {
        List<String> users = config.getStringList(rolePath + ".users");
        if (!users.contains(playerName)) {
            player.sendMessage("§cThis player does not have this role!");
        } else {
            users.remove(playerName);
            config.set(rolePath + ".users", users);
            player.sendMessage("Done my guy :D");
        }
    }



}
