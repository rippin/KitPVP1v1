package rippin.bullyscraft.com.Configs;

import rippin.bullyscraft.com.KitPVP1v1;

public class ConfigManager {

    public static void generateConfigs(KitPVP1v1 plugin){
    Config.setUp(plugin);
    PlayerDataConfig.setUp(plugin);
    ArenaConfig.setUp(plugin);

    }

}
