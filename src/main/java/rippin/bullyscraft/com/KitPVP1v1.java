package rippin.bullyscraft.com;

import org.bukkit.plugin.java.JavaPlugin;
import rippin.bullyscraft.com.Commands.KitPVP1v1CommandExecutor;
import rippin.bullyscraft.com.Configs.CachedData;
import rippin.bullyscraft.com.Configs.ConfigManager;
import rippin.bullyscraft.com.Listeners.KitPVP1v1Listeners;

public class KitPVP1v1 extends JavaPlugin {

    public static KitPVP1v1 plugin = null;
    public void onEnable(){
    plugin = this;
        ConfigManager.generateConfigs(this);
     getCommand("1v1").setExecutor(new KitPVP1v1CommandExecutor(this));
     getServer().getPluginManager().registerEvents(new KitPVP1v1Listeners(this), this);
     ArenaManager.loadArenas();
     CachedData.loadSavedUUIDs();
    }


    public void onDisable(){
    plugin = null;
    }

}
