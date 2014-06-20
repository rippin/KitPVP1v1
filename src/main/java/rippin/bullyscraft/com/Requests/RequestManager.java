package rippin.bullyscraft.com.Requests;

import me.bullyscraft.com.Classes.Kit;
import me.bullyscraft.com.Classes.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rippin.bullyscraft.com.Arena;
import rippin.bullyscraft.com.ArenaManager;
import rippin.bullyscraft.com.ArenaState;

import java.util.ArrayList;
import java.util.List;

public class RequestManager {
private static List<Request> request = new ArrayList<Request>();

    public static Request getRequest(String UUID){
       for (Request r : getAllRequest()){
           if (r.getReceiverUUID().equalsIgnoreCase(UUID) || r.getSenderUUID().equalsIgnoreCase(UUID)){
               return r;
           }
       }
          return null;
    }

    public static List<Request> getAllRequest(){
        return  request;
    }

    public static boolean hasSentActiveRequest(Player player){
        String uuid = player.getUniqueId().toString();
        for (Request r : getAllRequest()){
            if (r.getSenderUUID().equalsIgnoreCase(uuid)){
                return true;
            }
        }
        return false;
    }
    public static boolean hasActiveRequest(Player player){
        String uuid = player.getUniqueId().toString();
        for (Request r : getAllRequest()){
            if (r.getReceiverUUID().equalsIgnoreCase(uuid)){
                return true;
            }
        }
        return false;
    }

    public static boolean hasActiveRequestFromSpecificPlayer(Player sender, Player receiver){
        String senderUUID = sender.getUniqueId().toString();
        String receiverUUID = receiver.getUniqueId().toString();

        for (Request r : getAllRequest()){
         if (r.getSenderUUID().equalsIgnoreCase(senderUUID) && r.getReceiverUUID().equalsIgnoreCase(receiverUUID)){
             return true;
         }
        }
        return false;
    }
    public static void addRequest(Request r){
        getAllRequest().add(r);
    }

    public static void removeRequest(Request r){
        if (r.isRunning()){
            r.cancelTask();
        }
        getAllRequest().remove(r);


    }
    public static void acceptRequest(Request r, Player sender, Player receiver){
        Arena a = r.getArena();
        List<Player> players = new ArrayList<Player>();
        players.add(sender);
        players.add(receiver);
        if (a.getState() == ArenaState.VACANT){
            a.start(r.getK(), players);
            removeRequest(r);
        }
        else {
            sender.sendMessage(ChatColor.RED + receiver.getName() + " tried to accept your 1v1 request but arena "
                    + a.getName() + " is currently in use.");
            receiver.sendMessage(ChatColor.RED + a.getName() + " arena is currently occupied try later.");
            removeRequest(r);
        }
    }

    public static void cancelSentRequest(Player player){
        if (hasSentActiveRequest(player)){
           Request r = getRequest(player.getUniqueId().toString());
            player.sendMessage(ChatColor.GREEN + "1v1 Request has been canceled");
           removeRequest(r);

        }
        else {
            player.sendMessage(ChatColor.RED + " You do not have an active 1v1 request.");
        }
    }

    public static boolean sendRequest(Player sender, Player receiver, String ArenaName, String kitName, String bid){
        if (sender != null && receiver != null) {
            if (!RequestManager.hasSentActiveRequest(sender)){
                if (ArenaManager.isArena(ArenaName)){
                    Arena a = ArenaManager.getArena(ArenaName);

                    if (KitManager.getKit(kitName) != null){
                        Kit k = KitManager.getKit(kitName);
                        if (!k.getKitType().equalsIgnoreCase("PREMIUM") || (k.getKitType().equalsIgnoreCase("PREMIUM") && sender.hasPermission("Kit." + k.getName()))){
                            if (isNumeric(bid)){
                                Request r = new Request(a, k, sender, receiver, Integer.parseInt(bid));
                                RequestManager.addRequest(r);
                                r.sendRequest();
                                return true;
                            }
                            else{
                                sender.sendMessage(ChatColor.RED + "Bid is not numeric? Or argument error.");
                            }
                        }
                    }
                    else{
                        sender.sendMessage(ChatColor.RED + "That Kit does not seem to exist.");
                    }
                }
                else{
                    sender.sendMessage(ChatColor.RED + "That arena is currently in use or disabled. Do /1v1 arenas to find a vacant arena.");
                }
            }
            else{
                sender.sendMessage(ChatColor.RED + "You may only have 1 active 1v1 request. Wait until it expires or do /1v1 cancel");
            }
        }

        return false;
    }

    public static boolean sendRequest(Player sender, Player receiver, String ArenaName, String kitName){
        if (sender != null && receiver != null) {
            if (!RequestManager.hasSentActiveRequest(sender)){
                if (ArenaManager.isArena(ArenaName)){
                    Arena a = ArenaManager.getArena(ArenaName);

                    if (KitManager.getKit(kitName) != null){
                        Kit k = KitManager.getKit(kitName);
                        if (!k.getKitType().equalsIgnoreCase("PREMIUM") || (k.getKitType().equalsIgnoreCase("PREMIUM") && sender.hasPermission("Kit." + k.getName()))){
                            Request r = new Request(a, k, sender, receiver);
                            RequestManager.addRequest(r);
                            r.sendRequest();
                            return true;

                        }
                    }
                    else{
                        sender.sendMessage(ChatColor.RED + "That Kit does not seem to exist.");
                    }
                }
                else{
                    sender.sendMessage(ChatColor.RED + "That arena does not seem to exist.");
                }
            }
            else{
                sender.sendMessage(ChatColor.RED + "You may only have 1 active 1v1 request. Wait until it expires or do /1v1 cancel");
            }
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        if (str == null){
            return false;
        }
        return str.matches("-?\\d+(\\.\\d+)?");
    }


}
