package rippin.bullyscraft.com;

import org.bukkit.plugin.java.JavaPlugin;
import rippin.bullyscraft.com.Configs.Config;

public class KitPVP1v1 extends JavaPlugin {

    public static KitPVP1v1 plugin = null;
    public void onEnable(){
    plugin = this;
     Config.setUp(this);

    }


    public void onDisable(){
    plugin = null;
    }

}
