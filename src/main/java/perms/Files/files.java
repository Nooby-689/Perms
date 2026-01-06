package perms.Files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class files {
    private final File file;
    private FileConfiguration config;
    public files(String fileName, Plugin plugin) { //fileName must end with .yml
        file = new File(Bukkit.getServer().getPluginManager().getPlugin(plugin.getName()).getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs(); 
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Couldn't make the file: " + fileName);
                e.printStackTrace(); 
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save the file: " + file.getName());
            e.printStackTrace();
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}

