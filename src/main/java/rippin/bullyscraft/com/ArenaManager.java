package rippin.bullyscraft.com;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import rippin.bullyscraft.com.Configs.ArenaConfig;


import java.util.*;

public class ArenaManager {
 private static List<Arena> allArenas = new ArrayList<Arena>();
 private static List<String> wasInArena = new ArrayList<String>();
    public static void createArena(Player p, String name) {
        ArenaConfig.getConfig().createSection("Arena." + name);
        Arena a = new Arena(name);
        getAllArenas().add(a);
        ArenaConfig.saveFile();
        p.sendMessage(ChatColor.GREEN + "Arena " + name + " created");

    }


    public static void loadArenas() {
        getAllArenas().clear();
        try {
            for (String key : ArenaConfig.getConfig()
                    .getConfigurationSection("Arena").getKeys(false)) {
                System.out.println("Arena " + key
                        + " has been loaded from file!");
                Arena a = new Arena(key);
                allArenas.add(a);
                parseSpawns(a);

            }
        } catch (NullPointerException e) {
            System.out.println("No arenas found!");

        }
    }

    public static void parseSpawns(Arena arena){
        String name = arena.getName();
        int index = 0;
        for (String key : ArenaConfig.getConfig()
                .getConfigurationSection("Arena." + name + ".Spawn").getKeys(false)) {
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
        String w = loc.getWorld().getName();
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
        ArenaConfig.saveFile();
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
            if (arena.getPlayersUUID().contains(player.getUniqueId().toString())){
                return true;
            }
        }
        return false;
    }

    public static Arena getPlayersArenaUUID(String uuid){
        for (Arena arena : getAllArenas()){
            if (arena.getPlayersUUID().contains(uuid)){
                return arena;
            }
        }
        return null;
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
            if (player != null)
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        }
    }


    public static void enableAllArenas(Player sender){
        for (Arena a : getAllArenas()){
            a.enable();
            sender.sendMessage(ChatColor.GREEN + a.getName() + " has been enabled.");
        }

    }

    public static void disableAllArenas(Player sender){
        for (Arena a : getAllArenas()){
            a.disable();
            sender.sendMessage(ChatColor.GREEN + a.getName() + " has been disabled.");
        }

    }

    public static void enableArena(Arena a, Player sender){
        if (a != null){
            a.enable();
            sender.sendMessage(ChatColor.GREEN + a.getName() + " has been enabled.");
        }

        else {
            sender.sendMessage(ChatColor.RED + " that arena does not exist.");
        }
    }

    public static void disableArena(Arena a, Player sender){
        if (a != null){
            a.disable();
            sender.sendMessage(ChatColor.GREEN + a.getName() + " has been disbled.");
        }

        else {
            sender.sendMessage(ChatColor.RED + " that arena does not exist.");
        }
    }
    public static void listArenas(Player sender){
        for (Arena a : getAllArenas()){
            if (a.getSpawns().length != 0) {
            sender.sendMessage(ChatColor.GOLD + "Arena " + ChatColor.RED
                    + a.getName() + ChatColor.GOLD + " currently: " + ChatColor.RED + a.getState().toString());
/*                if (a.getState() != ArenaState.VACANT || a.getState() != ArenaState.DISABLED){

          }  */
            }
        }
    }

    public static boolean wasInArena(Player player){

        return wasInArena.contains(player.getUniqueId().toString());
    }

    public static List<String> getWasInArena(){
    return wasInArena;
    }
}
