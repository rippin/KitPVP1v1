package rippin.bullyscraft.com.Configs;

public class CachedData {

public static int fightLength = Config.getConfig().getInt("1v1-Length");
public static int startDelay = Config.getConfig().getInt("1v1-Start-Delay");
public static int endDelay = Config.getConfig().getInt("1v1-End-Delay");
public static int requestExpire = Config.getConfig().getInt("Requests-Expire-Time");

}
