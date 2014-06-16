package rippin.bullyscraft.com.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import rippin.bullyscraft.com.Arena;
import rippin.bullyscraft.com.ArenaManager;
import rippin.bullyscraft.com.ArenaState;
import rippin.bullyscraft.com.Configs.CachedData;
import rippin.bullyscraft.com.Countdowns.FightEndCountdown;
import rippin.bullyscraft.com.Inventory.PlayerDataHandler;
import rippin.bullyscraft.com.KitPVP1v1;

public class KitPVP1v1Listeners implements Listener {
    private KitPVP1v1 plugin;

    public KitPVP1v1Listeners(KitPVP1v1 plugin){
    this.plugin = plugin;

    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player){
            Player p = (Player) event.getEntity();
            if (ArenaManager.isInArena(p)){
                Arena a = ArenaManager.getPlayersArenaUUID(p.getUniqueId().toString());
                if (a.getState() != ArenaState.OCCUPIED){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (ArenaManager.isInArena(player)){
            Arena a = ArenaManager.getPlayersArenaUUID(player.getUniqueId().toString());
            a.removePlayer(player);
            PlayerDataHandler.returnItemsAndLocation(player);
            ArenaManager.broadcastToArena(a, "&4Your opponent has left the game and you have become the winner.");
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();

        if (CachedData.getSavedUUIDList().contains(p.getUniqueId().toString())){
            PlayerDataHandler.returnItemsAndLocation(p);
        }
    }

}
