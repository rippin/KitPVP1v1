package rippin.bullyscraft.com;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
        int i = 0;
        World w = Bukkit.getWorld(ArenaConfig.getConfig().getString("Arena." + name + ".Spawn." + key + ".World"));
        double x = ArenaConfig.getConfig().getDouble("Arena." + name + ".Spawn." + key + ".X");
        double y = ArenaConfig.getConfig().getDouble("Arena." + name + ".Spawn." + key + ".Y");
        double z = ArenaConfig.getConfig().getDouble("Arena." + name + ".Spawn." + key + ".Z");
        float yaw = (float) ArenaConfig.getConfig().getDouble("Arena." + name + ".Spawn." + key + ".Yaw");
        float pitch = (float) ArenaConfig.getConfig().getDouble("Arena." + name + ".Spawn." + key + ".Pitch");

        Location loc = new Location(w, x, y, z, yaw, pitch);
            arena.setSpawn(loc, i);
            i++;
        }
    }

    public static void setSpawn(String name, Location loc, int index){
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

        Arena a = ArenaManager.getArena(name);
        a.setSpawn(loc, index);
    }

    public static void setPlayer(String name, String player){

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
            if (arena.getPlayers().contains(player.getName())){
                return true;
            }
        }
        return false;
    }

    public static boolean isArena(String name) {
        boolean exists = false;

        for (Arena arena : getAllArenas()) {
            if (arena != null && arena.getName() != null) {
                if (arena.getName().equalsIgnoreCase(name)) {
                    exists = true;
                }
            }
        }
        return exists;
    }
}
