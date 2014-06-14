package rippin.bullyscraft.com;

import me.bullyscraft.com.Classes.Kit;
import me.bullyscraft.com.Classes.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import rippin.bullyscraft.com.Configs.ArenaConfig;
import rippin.bullyscraft.com.Requests.Request;
import rippin.bullyscraft.com.Requests.RequestManager;

import java.util.*;

public class ArenaManager {
 private static List<Arena> allArenas = new ArrayList<Arena>();

    public static void createArena(Player p, String name) {
        ArenaConfig.getConfig().createSection("Arena." + name);
        Arena a = new Arena(name);
        getAllArenas().add(a);
        ArenaConfig.saveFile(ArenaConfig.getFile(), ArenaConfig.getConfig());
        p.sendMessage(ChatColor.GREEN + "Arena " + name + " created");

    }


    public static boolean loadArenas() {
        getAllArenas().clear();
        try {
            for (String key : ArenaConfig.getConfig()
                    .getConfigurationSection("Arena").getKeys(false)) {
                System.out.println("Arena " + key
                        + " has been loaded from file!");
                Arena a = new Arena(key);
                allArenas.add(a);
                parseSpawns(a);
                return true;
            }
        } catch (NullPointerException e) {
            System.out.println("No arenas found!");

        }
        return false;
    }

    public static void parseSpawns(Arena arena){
        String name = arena.getName();
        for (String key : ArenaConfig.getConfig()
                .getConfigurationSection("Arena." + name + ".Spawn").getKeys(false)) {
        int index = 0;
        FileConfiguration config = ArenaConfig.getConfig();

        World w = Bukkit.getWorld(config.getString("Arena." + name + ".Spawn." + index + ".World"));
        double x = config.getDouble("Arena." + name + ".Spawn." + index + ".X");
        double y = config.getDouble("Arena." + name + ".Spawn." + index + ".Y");
        double z = config.getDouble("Arena." + name + ".Spawn." + index + ".Z");
        float yaw = (float) config.getDouble("Arena." + name + ".Spawn." + index + ".Yaw");
        float pitch = (float)config.getDouble("Arena." + name + ".Spawn." + index + ".Pitch");

        Location loc = new Location(w, x, y, z, yaw, pitch);
            arena.setSpawn(loc, index);
            System.out.println("Set spawn " + index + " for arena " + name);
            index++;
        }
    }

    public static void setSpawn(Arena arena, Location loc, int index){
        String name = arena.getName();
        World w = loc.getWorld();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();

        FileConfiguration config = ArenaConfig.getConfig();

        config.set("Arena." + name + ".Spawn." + index + ".World", w);
        config.set("Arena." + name + ".Spawn." + index + ".X", x);
        config.set("Arena." + name + ".Spawn." + index + ".Y", y);
        config.set("Arena." + name + ".Spawn." + index + ".Z", z);
        config.set("Arena." + name + ".Spawn." + index + ".Yaw", yaw);
        config.set("Arena." + name + ".Spawn." + index + ".Pitch", pitch);

        arena.setSpawn(loc, index);
    }

    public static List<Arena> getAllArenas() {
        return allArenas;
    }

    public static Arena getArena(String name) {
        Arena arena = null;

        for (Arena allArenas : getAllArenas()) {
            if (allArenas.getName().equalsIgnoreCase(name)) {
                arena = allArenas;
            }
        }

        return arena;
    }
    public static boolean isInArena(Player player) {
        for (Arena arena : getAllArenas()) {
            if (arena.getPlayersUUID().contains(player.getUniqueId())){
                return true;
            }
        }
        return false;
    }

    public static boolean isArena(String name) {


        for (Arena arena : getAllArenas()) {
            if (arena != null && arena.getName() != null) {
                if (arena.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void broadcastToArena(Arena arena, String message){
        List<String> players = arena.getPlayersUUID();
        for (String uuid : players){
            Player player = Bukkit.getServer().getPlayer(UUID.fromString(uuid));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        }
    }

    public static boolean sendRequest(Player sender, Player receiver, String ArenaName, String kitName, String bid){
    if (sender != null && receiver != null) {
       if (!RequestManager.hasSentActiveRequest(sender)){
        if (isArena(ArenaName)){
         Arena a = getArena(ArenaName);
        if (a.getState() == ArenaState.VACANT){
            if (KitManager.getKit(kitName) != null){
                Kit k = KitManager.getKit(kitName);
                if (!k.getKitType().equalsIgnoreCase("PREMIUM") || (k.getKitType().equalsIgnoreCase("PREMIUM") && sender.hasPermission("Kit." + k.getName()))){
                    if (isNumeric(bid)){
                        Request r = new Request(a, k, sender, receiver, bid);
                        RequestManager.addRequest(r);
                        r.sendRequest();
                        return true;
                    }
                    else{
                        sender.sendMessage(ChatColor.RED + "Bid is not numeric? Or argument error.");
                    }
                 }
            }
            else{
                sender.sendMessage(ChatColor.RED + "That Kit does not seem to exist.");
            }
        }
            else{
            sender.sendMessage(ChatColor.RED + "That arena is currently in use or disabled. Do /1v1 arenas to find a vacant arena.");
        }
       }
        else{
            sender.sendMessage(ChatColor.RED + "That arena does not seem to exist.");
        }
      }
        else{
           sender.sendMessage(ChatColor.RED + "You may only have 1 active 1v1 request. Wait until it expires or do /1v1 cancel");
       }
    }

        return false;
    }

    public static boolean sendRequest(Player sender, Player receiver, String ArenaName, String kitName){
        if (sender != null && receiver != null) {
            if (!RequestManager.hasSentActiveRequest(sender)){
                if (isArena(ArenaName)){
                    Arena a = getArena(ArenaName);
                    if (a.getState() == ArenaState.VACANT){
                        if (KitManager.getKit(kitName) != null){
                            Kit k = KitManager.getKit(kitName);
                            if (!k.getKitType().equalsIgnoreCase("PREMIUM") || (k.getKitType().equalsIgnoreCase("PREMIUM") && sender.hasPermission("Kit." + k.getName()))){
                               Request r = new Request(a, k, sender, receiver);
                                    RequestManager.addRequest(r);
                                    r.sendRequest();
                                    return true;

                            }
                        }
                        else{
                            sender.sendMessage(ChatColor.RED + "That Kit does not seem to exist.");
                        }
                    }
                    else{
                        sender.sendMessage(ChatColor.RED + "That arena is currently in use or disabled. Do /1v1 arenas to find a vacant arena.");
                    }
                }
                else{
                    sender.sendMessage(ChatColor.RED + "That arena does not seem to exist.");
                }
            }
            else{
                sender.sendMessage(ChatColor.RED + "You may only have 1 active 1v1 request. Wait until it expires or do /1v1 cancel");
            }
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
