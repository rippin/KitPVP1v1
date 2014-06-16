package rippin.bullyscraft.com.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import rippin.bullyscraft.com.KitPVP1v1;

public class KitPVP1v1CommandExecutor implements CommandExecutor {

    KitPVP1v1 plugin;
    public KitPVP1v1CommandExecutor(KitPVP1v1 plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
       CommandHelper helper = new CommandHelper(commandSender, command);
        if (command.getName().equalsIgnoreCase("1v1")){
            new KitPVP1v1Commands(plugin).executeCommand(commandSender,command, args);
            return true;
        }
        else {
            helper.unknownCommand();
        }

        return false;
    }
}
