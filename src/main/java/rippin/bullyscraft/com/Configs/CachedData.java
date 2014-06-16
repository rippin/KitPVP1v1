package rippin.bullyscraft.com.Configs;

import java.util.ArrayList;
import java.util.List;

public class CachedData {

public static int fightLength = Config.getConfig().getInt("1v1-Length");
public static int startDelay = Config.getConfig().getInt("1v1-Start-Delay");
public static int endDelay = Config.getConfig().getInt("1v1-End-Delay");
public static int requestExpire = (Config.getConfig().getInt("Request-Expire-Time")* 20);
private static List<String> savedUUIDSList = new ArrayList<String>();

    public static void loadSavedUUIDs(){
        savedUUIDSList.clear();
        try {
            for (String key : PlayerDataConfig.getConfig().getKeys(false)) {
                System.out.println("UUID  " + key
                        + " has been loaded from file!");
                   savedUUIDSList.add(key);
            }
        } catch (NullPointerException e) {
            System.out.println("No saved UUID'S found!");

        }
    }

    public static List<String> getSavedUUIDList(){
        return savedUUIDSList;
    }


}
