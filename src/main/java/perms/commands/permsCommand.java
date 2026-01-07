package perms.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import perms.managers.PermManager;
import perms.Perms;

import java.util.ArrayList;
import java.util.Collections;

public class permsCommand implements CommandExecutor, TabCompleter {

    private final PermManager pm;

    public permsCommand(PermManager pm) {
        this.pm = pm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players");
            return true;
        }

        if (!player.isOp()) {
            player.sendMessage("§cOnly ops can use this command!");
            return true;
        }

        if (args.length < 3) {
            player.sendMessage("§cUsage: /perms <add|remove|addrole|removerole> <role> <command/player>");
            return true;
        }

        String action = args[0].toLowerCase();
        String role = args[1];
        String target = args[2];

        switch (action) {
            case "add" -> pm.addCommand(role, target, player);
            case "remove" -> pm.removeCommand(role, target, player);
            case "addrole" -> pm.addPlayer(role, target, player);
            case "removerole" -> pm.removePlayer(role, target, player);
            case "setprefix" -> pm.setPrefix(role, target, player);
            default -> player.sendMessage("§cUnknown action. use add remove addrole removerole");
        }

        Perms.getInstance().players.save();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return Collections.emptyList();

        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("add");
            suggestions.add("remove");
            suggestions.add("addrole");
            suggestions.add("removerole");
        } else if (args.length == 2) {
            suggestions.add("member");
            suggestions.add("Owner");
            suggestions.add("CoOwner");
            suggestions.add("Mod");
        } else if(args.length == 3 && (args[0] == "addrole" || args[0] == "removerole")) {
            suggestions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        } else if (args.length == 3) {
            List<String> allCommands = Perms.getInstance().perms.get().getStringList("CommandsList");
            suggestions.addAll(allCommands);
        }
        
    return suggestions;
    }
}