package perms.managers;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public final class PermsUtils {

    private final FileConfiguration permsfile;

    public PermsUtils(FileConfiguration permsConfig) {
        this.permsfile = permsConfig;
    }

    public List<String> getAllCommands() {
        return permsfile.getStringList("CommandsList");
    }
}
