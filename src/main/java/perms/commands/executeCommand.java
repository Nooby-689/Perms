package perms.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import perms.managers.PermManager;

public class executeCommand implements CommandExecutor {

    private final PermManager permManager;

    public executeCommand(PermManager permManager) {
        this.permManager = permManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            player.sendMessage("§cUsage: /ex <command> [args...]");
            return true;
        }

        String targetCommand = String.join(" ", args);


        if (!permManager.canUseCommand(player, args[0].toLowerCase())) {
            player.sendMessage("§cYou do not have permission to use this command!");
            return true;
        }

        // run as console 
        boolean success = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), targetCommand);

        if (success) {
            player.sendMessage("§aCommand executed: §e/" + targetCommand);
        } else {
            player.sendMessage("§cCommand failed or does not exist.");
        }

        return true;
    }
}
