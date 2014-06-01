package rippin.bullyscraft.com;

import java.util.HashMap;
import java.util.Map;

public class PlayerLists {
// Key player who sent request, value player who was requested
    private static HashMap<String, String> requestSent = new HashMap<String, String>();

    public HashMap<String, String> getRequestSentMap(){
        return requestSent;
    }

    public static boolean hasRequest(String name){
        for (Map.Entry<String, String> entry : requestSent.entrySet()){

            if (entry.getKey().equalsIgnoreCase(name) || entry.getValue().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }
}
