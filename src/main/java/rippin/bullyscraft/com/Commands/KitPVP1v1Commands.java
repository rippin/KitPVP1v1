package rippin.bullyscraft.com.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rippin.bullyscraft.com.Arena;
import rippin.bullyscraft.com.ArenaManager;
import rippin.bullyscraft.com.KitPVP1v1;
import rippin.bullyscraft.com.Requests.Request;
import rippin.bullyscraft.com.Requests.RequestManager;

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
        RequestManager.cancelSentRequest((Player)sender);
        }
        else if (args[0].equalsIgnoreCase("arenas")){
            ArenaManager.listArenas((Player) sender);
        }
        else if (args[0].equalsIgnoreCase("disableAll")){
            if (sender.hasPermission("1v1.admin"))
            ArenaManager.disableAllArenas((Player) sender);
        }
        else if (args[0].equalsIgnoreCase("enableAll")) {
            if (sender.hasPermission("1v1.admin"))
                ArenaManager.enableAllArenas((Player) sender);
           }
            else {
            helper.wrongArguments();
        }
        }
        else if (args.length == 2){
            if (args[0].equalsIgnoreCase("disable")){
                if (sender.hasPermission("1v1.admin")) {
                    Arena a = ArenaManager.getArena(args[1]);
                    ArenaManager.disableArena(a, (Player) sender);
                }
            }
            else if (args[0].equalsIgnoreCase("enable")){
                if (sender.hasPermission("1v1.admin")) {
                    Arena a = ArenaManager.getArena(args[1]);
                    ArenaManager.enableArena(a, (Player) sender);
                }
            }
            else if (args[0].equalsIgnoreCase("accept")){
                if (RequestManager.hasActiveRequest((Player)sender)){
                    Request r = RequestManager.getRequest(((Player) sender).getUniqueId().toString());
                    Player p = Bukkit.getPlayerExact(args[1]);
                    if (p != null && p.isOnline()){
                        RequestManager.acceptRequest(r, p, (Player)sender);
                    }
                    else{
                        sender.sendMessage(ChatColor.RED + "1v1 request does not exist from that person or they logged off.");
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("deny")){

                    Player p = Bukkit.getPlayerExact(args[1]);
                    if (p != null && p.isOnline()){
                        if (RequestManager.hasActiveRequestFromSpecificPlayer(p, (Player)sender)){
                        Request r = RequestManager.getRequest(((Player) sender).getUniqueId().toString());
                        RequestManager.removeRequest(r);
                        p.sendMessage(ChatColor.RED + sender.getName() + " has denied your 1v1 request.");
                        sender.sendMessage(ChatColor.GREEN + "1v1 request has been denied.");
                    }
                }
                    else {
                        sender.sendMessage(ChatColor.RED + "You do not have a active request from that person maybe they left?");
                    }

            }
            else if (args[0].equalsIgnoreCase("createArena")){
                    ArenaManager.createArena((Player)sender, args[1]);
            }
            else {
                helper.wrongArguments();
            }
        }
        else if (args.length == 3){
            if (args[0].equalsIgnoreCase("setspawn")){
                if (ArenaManager.isArena(args[1])){
                   Arena a = ArenaManager.getArena(args[1]);
                    if (ArenaManager.isNumeric(args[2])){
                       int i = Integer.parseInt(args[2]);
                       i = (i - 1);
                        i = (i <= 0)? (0) : (i);
                        System.out.print(" Index " + i);
                        ArenaManager.setSpawn(a, ((Player) sender).getLocation(), i);
                        sender.sendMessage(ChatColor.GREEN + "Spawn set at index " + i);
                    }
                }
            }
            else {
            Player receiver = Bukkit.getPlayerExact(args[0]);
            if (receiver != null && receiver.isOnline()){
                if (!((Player) sender).getUniqueId().equals(receiver.getUniqueId())) {
                RequestManager.sendRequest((Player)sender, receiver, args[1], args[2]);
                }
                    else {
                    sender.sendMessage(ChatColor.RED + "Dum dum you can't 1v1 yourself.");
                }
            }
            else{
                sender.sendMessage(ChatColor.RED + "The player " + args[0] + " is not online or does not exist.");
            }
           }

        }
        else if (args.length == 4){
            Player receiver = Bukkit.getPlayerExact(args[0]);
            if (receiver != null && receiver.isOnline()){
                RequestManager.sendRequest((Player)sender, receiver, args[1], args[2], args[3]);
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