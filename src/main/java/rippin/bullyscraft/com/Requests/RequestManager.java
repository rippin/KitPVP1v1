package rippin.bullyscraft.com.Requests;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RequestManager {
private static List<Request> request = new ArrayList<Request>();

    public static Request getRequest(String recieverUUID){
       for (Request r : getAllRequest()){
           if (r.getReceiverUUID().equalsIgnoreCase(recieverUUID)){
               return r;
           }
       }
          return null;
    }

    public static List<Request> getAllRequest(){
        return  request;
    }

    public static boolean hasSentActiveRequest(Player player){
        for (Request r : getAllRequest()){
            String uuid = player.getUniqueId().toString();
            if (r.getSenderUUID().equalsIgnoreCase(uuid)){
                return true;
            }
        }
        return false;
    }
    public static boolean hasActiveRequest(Player player){
        for (Request r : getAllRequest()){
            String uuid = player.getUniqueId().toString();
            if (r.getReceiverUUID().equalsIgnoreCase(uuid)){
                return true;
            }
        }
        return false;
    }
    public static void addRequest(Request r){
        getAllRequest().add(r);
    }

    public static void removeRequest(Request r){
        getAllRequest().remove(r);
    }


}
