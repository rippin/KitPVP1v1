package rippin.bullyscraft.com.Inventory;

import me.bullyscraft.com.Classes.Wipe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import rippin.bullyscraft.com.Configs.CachedData;
import rippin.bullyscraft.com.Configs.PlayerDataConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerDataHandler {

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

       config.set(uuid + ".Location.World", location.getWorld().getName());
       config.set(uuid + ".Location.X", location.getX());
       config.set(uuid + ".Location.Y", location.getY());
       config.set(uuid + ".Location.Z", location.getZ());
       config.set(uuid + ".Location.Yaw", location.getYaw());
       config.set(uuid + ".Location.Pitch", location.getPitch());
       Wipe.wipe(player); //remove everything from player
       CachedData.getSavedUUIDList().add(uuid); //add to the list
       PlayerDataConfig.saveFile();
    }
    // add method to clear and give kit and crap
    public static void returnItemsAndLocation(Player player){
        Wipe.wipe(player); // Wipe player
        FileConfiguration config = PlayerDataConfig.getConfig();
        String uuid = player.getUniqueId().toString();
        Location loc = parseLoc(uuid);
        List<String> potions = config.getStringList(uuid + ".Potions");
        String stringInv = config.getString(uuid + ".Inventory");
        PlayerInventory oldInv = (PlayerInventory) ItemSerialization.fromBase64(stringInv);
        for (String string: potions){
            PotionEffect effect = PotionSerialization.fromStringToPotionEffect(string);
            player.addPotionEffect(effect);
        }
        player.getInventory().setContents(oldInv.getContents());
        player.getInventory().setArmorContents(oldInv.getArmorContents());
        player.teleport(loc);
        config.set(uuid, null); // To delete saved data
        PlayerDataConfig.saveFile();
        CachedData.getSavedUUIDList().remove(player.getUniqueId().toString()); // Remove from the cached list.
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
