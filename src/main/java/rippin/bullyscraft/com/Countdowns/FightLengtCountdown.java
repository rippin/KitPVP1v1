package rippin.bullyscraft.com.Countdowns;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import rippin.bullyscraft.com.Arena;
import rippin.bullyscraft.com.ArenaManager;
import rippin.bullyscraft.com.KitPVP1v1;

public class FightLengtCountdown {

    private Arena arena;
    private int delay;
    private KitPVP1v1 plugin;

    public FightLengtCountdown(Arena arena, int delay, KitPVP1v1 plugin){
        this.arena = arena;
        this.delay = delay;
        this.plugin = plugin;
    }

    public void startCountdown(){
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
            @Override
            public void run() {
            if (delay == 300){
                ArenaManager.broadcastToArena(arena, ChatColor.GREEN + "5 minutes remain in the 1v1.");
            }
            else if (delay == 240){
                    ArenaManager.broadcastToArena(arena, ChatColor.GREEN + "4 minutes remain in the 1v1.");
                }

            else if (delay == 180){
                ArenaManager.broadcastToArena(arena, ChatColor.GREEN + "3 minutes remain in the 1v1.");
            }
            else if (delay == 120){
                ArenaManager.broadcastToArena(arena, ChatColor.GREEN + "2 minutes remain in the 1v1.");
            }
            else if (delay == 60){
                ArenaManager.broadcastToArena(arena, ChatColor.GREEN + "1 minute remains in the 1v1.");
            }
            else if (delay == 30){
                ArenaManager.broadcastToArena(arena, ChatColor.GREEN + "30 seconds remains in the 1v1.");
            }
            else if (delay == 15){
                ArenaManager.broadcastToArena(arena, ChatColor.GREEN + "15 seconds remains in the 1v1.");
            }
            else if (delay <=5){
                ArenaManager.broadcastToArena(arena, ChatColor.GREEN + String.valueOf(delay) + " seconds remains in the 1v1.");
            }
            else if(delay == 0){
                //time ran out
                //run method
            }
            --delay;
            }
        }, 20L, 20L);
    }
}
