package rippin.bullyscraft.com.Configs;

import java.util.ArrayList;
import java.util.List;

public class CachedData {

public static int fightLength = Config.getConfig().getInt("1v1-Length");
public static int startDelay = Config.getConfig().getInt("1v1-Start-Delay");
public static int endDelay = Config.getConfig().getInt("1v1-End-Delay");
public static int requestExpire = (Config.getConfig().getInt("Request-Expire-Time")* 20);
private static List<String> savedUUIDSList = new ArrayList<String>();
private static List<String> disabledRequest = new ArrayList<String>();


    public static void loadSavedUUIDs(){
        savedUUIDSList.clear();
        try {
            for (String key : PlayerDataConfig.getConfig().getKeys(false)) {
                   savedUUIDSList.add(key);
            }
        } catch (NullPointerException e) {

        }
    }

    public static List<String> getSavedUUIDList(){
        return savedUUIDSList;
    }

    public static void loadDisabledRequest(){
        disabledRequest.clear();
        try {
            for (String key : PlayerDataConfig.getConfig().getConfigurationSection("Disabled-Request").getKeys(false)) {
                disabledRequest.add(key);
            }
        } catch (NullPointerException e) {

        }
    }

    public static List<String> getDisabledRequestList(){
        return disabledRequest;
    }


}
