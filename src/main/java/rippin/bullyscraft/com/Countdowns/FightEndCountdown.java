package rippin.bullyscraft.com.Countdowns;
import org.bukkit.scheduler.BukkitRunnable;
import rippin.bullyscraft.com.Arena;
import rippin.bullyscraft.com.ArenaManager;
import rippin.bullyscraft.com.ArenaState;
import rippin.bullyscraft.com.KitPVP1v1;

public class FightEndCountdown extends CountdownInterface {
private Arena arena;
private int delay;
private KitPVP1v1 plugin;
private int taskid;

    public FightEndCountdown(Arena arena, int delay, KitPVP1v1 plugin){
        this.arena = arena;
        this.delay = delay;
        this.plugin = plugin;
   }

    public void startCountdown(){
        ArenaManager.broadcastToArena(arena, "&aTeleporting back to previous location in " + delay + ".");
        arena.setState(ArenaState.ENDING);
        taskid =  plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
            @Override
            public void run() {
            --delay;
            if (delay == 0){
                ArenaManager.broadcastToArena(arena, "&aTeleporting...");
                arena.end();
                cancelTask(taskid);
            }
           }
        }, 20L, 20L);
    }

    public void cancelTask(int taskid){
        plugin.getServer().getScheduler().cancelTask(taskid);
    }

}
