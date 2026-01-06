package perms.managers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public final class PermManager {

    private final FileConfiguration config;


    public PermManager(FileConfiguration config) {
        this.config = config;
    }


    public boolean canUseCommand(Player player, String command) {
        command = command.toLowerCase();
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
}
