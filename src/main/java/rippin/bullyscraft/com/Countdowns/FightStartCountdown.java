package rippin.bullyscraft.com.Countdowns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import rippin.bullyscraft.com.Arena;
import rippin.bullyscraft.com.ArenaManager;
import rippin.bullyscraft.com.Configs.CachedData;
import rippin.bullyscraft.com.KitPVP1v1;

public class FightStartCountdown {

    private Arena arena;
    private int delay;
    private KitPVP1v1 plugin;
    private int taskid;
    public FightStartCountdown(Arena arena, int delay, KitPVP1v1 plugin){
        this.arena = arena;
        this.delay = delay;
        this.plugin = plugin;

    }

    public void startCountdown(){

       taskid =  plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
            if (delay == 10){
                ArenaManager.broadcastToArena(arena, ChatColor.GREEN + "1v1 will start in 10 seconds");
            }
            else if (delay <= 5){
                ArenaManager.broadcastToArena(arena, ChatColor.GREEN + "1v1 will start in " + delay + " seconds");
            }
            else if (delay == 0){
                ArenaManager.broadcastToArena(arena, ChatColor.AQUA + "Fight!");

                new FightLengtCountdown(arena, CachedData.fightLength, plugin).startCountdown();

                cancelTask(taskid);
            }

              --delay;
            }
        }, 20L, 20L);
    }
    public void cancelTask(int taskid){
        plugin.getServer().getScheduler().cancelTask(taskid);
    }
}
