package rippin.bullyscraft.com.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rippin.bullyscraft.com.Arena;
import rippin.bullyscraft.com.ArenaManager;
import rippin.bullyscraft.com.KitPVP1v1;

public class KitPVP1v1Commands extends CommandAbstract{
   public KitPVP1v1Commands(KitPVP1v1 plugin){
       super(plugin);
   }
    @Override
    public void executeCommand(CommandSender sender, Command cmd, String[] args) {
        CommandHelper helper = new CommandHelper(sender, cmd);
        if(sender instanceof Player){
        if (args.length == 0){
          helper.helpMessage();
        }
        else if (args.length == 1){
        if (args[0].equalsIgnoreCase("cancel")){
            //player to cancel request
        }
        else if (args[0].equalsIgnoreCase("arenas")){
            //list all arenas
        }
        else if (args[0].equalsIgnoreCase("resetAll")){
            //reset all areans
        }
        else if (args[0].equalsIgnoreCase("enableAll")) {
          //enable all arenas
           }
        }
        else if (args.length == 2){
            if (args[0].equalsIgnoreCase("reset")){
                //args[1] check for arena name
            }
            else if (args[0].equalsIgnoreCase("enable")){
                //args[1] check for arena name
            }
            else if (args[0].equalsIgnoreCase("accept")){

            }
            else if (args[0].equalsIgnoreCase("deny")){

            }
            else if (args[0].equalsIgnoreCase("createArena")){
                    ArenaManager.createArena((Player)sender, args[1]);
            }
        }
        else if (args.length == 3){
            if (args[0].equalsIgnoreCase("setspawn")){
                if (ArenaManager.isArena(args[1])){
                   Arena a = ArenaManager.getArena(args[1]);
                    if (ArenaManager.isNumeric(args[2])){
                       int i = Integer.parseInt(args[2]);
                       i = (i <= 0)? (1) : (i);
                        ArenaManager.setSpawn(a, ((Player) sender).getLocation(), i);
                    }
                }
            }
            else {
            Player receiver = Bukkit.getPlayerExact(args[0]);
            if (receiver != null){
                ArenaManager.sendRequest((Player)sender, receiver, args[1], args[2]);
            }
            else{
                sender.sendMessage(ChatColor.RED + "The player " + args[0] + " is not online or does not exist.");
            }
           }
        }
        else if (args.length == 4){
            Player receiver = Bukkit.getPlayerExact(args[0]);
            if (receiver != null){
                ArenaManager.sendRequest((Player)sender, receiver, args[1], args[2], args[3]);
            }
            else {
                sender.sendMessage(ChatColor.RED + "The player " + args[0] + " is not online or does not exist.");
            }
        }
        else{
            helper.wrongArguments();
          }
         }
        else {
            helper.noConsole();
        }
    }
}