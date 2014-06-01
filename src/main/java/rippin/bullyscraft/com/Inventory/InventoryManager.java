package rippin.bullyscraft.com.Inventory;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import rippin.bullyscraft.com.Configs.PlayerDataConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InventoryManager {

    public static void savePlayerData(Player player){
        FileConfiguration config = PlayerDataConfig.getConfig();
        String uuid = player.getUniqueId().toString();
        PlayerInventory inv = player.getInventory();
        String inventory = ItemSerialization.toBase64(inv);
        Location location = player.getLocation();
        Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
        List<String> potionStringList = new ArrayList<String>();
       for (PotionEffect effect : potionEffects){
           String string = PotionSerialization.potionEffectToString(effect);
            potionStringList.add(string);
       }
       config.set(uuid + ".Inventory", inventory); //set inv
       config.set(uuid + ".Potions", potionStringList); //set potions

       config.set(uuid + ".Location.World", location.getWorld().toString());
       config.set(uuid + ".Location.X", location.getX());
       config.set(uuid + ".Location.Y", location.getY());
       config.set(uuid + ".Location.Z", location.getZ());
       config.set(uuid + ".Location.Yaw", location.getYaw());
       config.set(uuid + ".Location.Pitch", location.getPitch());
    }

    public static void returnItemsAndLocation(Player player){
        //clear player stuff
        FileConfiguration config = PlayerDataConfig.getConfig();
        String uuid = player.getUniqueId().toString();
        Location loc = parseLoc(uuid);
        List<String> potions = config.getStringList(uuid + ".Potions");
        String stringInv = config.getString(uuid + ".Inventory");
        PlayerInventory oldInv = (PlayerInventory) ItemSerialization.fromBase64(stringInv);
        PlayerInventory currentInv = player.getInventory();
        for (String string: potions){
            PotionEffect effect = PotionSerialization.fromStringToPotionEffect(string);
            player.addPotionEffect(effect);
        }
        currentInv = oldInv;

    }

    private static Location parseLoc(String uuid){
     FileConfiguration config = PlayerDataConfig.getConfig();
     String world = config.getString(uuid + ".Location.World");
     double x = config.getInt(uuid + ".Location.X");
     double y = config.getInt(uuid + ".Location.Y");
     double z = config.getInt(uuid + ".Location.Z");
     float yaw = (float) config.getDouble(uuid + ".Location.Yaw");
     float pitch = (float) config.getDouble(uuid + ".Location.Pitch");

     return new Location(Bukkit.getWorld(world),x,y,z,yaw, pitch);
    }

}
