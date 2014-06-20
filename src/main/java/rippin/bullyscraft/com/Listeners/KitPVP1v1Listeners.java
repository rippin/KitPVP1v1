package rippin.bullyscraft.com.Listeners;

import me.bullyscraft.com.BullyPVP;
import me.bullyscraft.com.Stats.PlayerStatsObject;
import me.bullyscraft.com.Stats.PlayerStatsObjectManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import rippin.bullyscraft.com.Arena;
import rippin.bullyscraft.com.ArenaManager;
import rippin.bullyscraft.com.ArenaState;
import rippin.bullyscraft.com.Configs.CachedData;
import rippin.bullyscraft.com.Inventory.PlayerDataHandler;
import rippin.bullyscraft.com.KitPVP1v1;

import java.util.UUID;

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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        if (ArenaManager.isInArena(player)){
            Arena a = ArenaManager.getPlayersArenaUUID(player.getUniqueId().toString());
            a.removePlayer(player);
            Player winner = null;
            for (String uuid: a.getPlayersUUID()){
                winner = Bukkit.getPlayer(UUID.fromString(uuid));
                break;
            }

            PlayerStatsObject psoWinner = PlayerStatsObjectManager.getPSO(winner, BullyPVP.instance);
            PlayerStatsObject psoLeft = PlayerStatsObjectManager.getPSO(player, BullyPVP.instance);

            if (a.isThereBid()){
                int bid = a.getBid();
                if (a.getPlayersUUID().size() == 1){

                    psoWinner.addCoins(bid);
                    psoLeft.removeCoins(bid);
                    winner.sendMessage(ChatColor.GREEN + "You have received " + bid + " coins, your opponent left.");
                 }
            }
            BullyPVPStatsHook.setStatsFrom1v1(winner, player, psoWinner, psoLeft);
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event){
        final Player p = event.getPlayer();

        if (CachedData.getSavedUUIDList().contains(p.getUniqueId().toString())){
            plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerDataHandler.returnItemsAndLocation(p);
                }
            }, 1L);

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event){
       Player dead = event.getEntity();
       Player killer = event.getEntity().getKiller();
       String uuid = dead.getUniqueId().toString();
        if (ArenaManager.isInArena(dead)){
             ArenaManager.getWasInArena().add(dead.getUniqueId().toString()); // add to list to prevent kit
            event.setDeathMessage(null);
            Arena arena = ArenaManager.getPlayersArenaUUID(uuid);
            arena.removePlayer(dead); //remove



            PlayerStatsObject psoKiller = PlayerStatsObjectManager.getPSO(killer, BullyPVP.instance);
            PlayerStatsObject psoDead = PlayerStatsObjectManager.getPSO(dead, BullyPVP.instance);

            ArenaManager.broadcastToArena(arena, "&6" + dead.getName() + " has died.");


            //if killer is not winner
            if ((!(killer instanceof  Player)) || killer == null || killer.getName().equalsIgnoreCase(dead.getName())){
                //Only two people in the list removed dead player, remaining is winner.
                for (String winnerUUID: arena.getPlayersUUID()){
                    killer = Bukkit.getPlayer(UUID.fromString(winnerUUID));

                    break;
                }
                psoKiller = PlayerStatsObjectManager.getPSO(killer, BullyPVP.instance);
            }
            // end not killer winner.

            if (arena.isThereBid()){
                int bid = arena.getBid();
                if (arena.getPlayersUUID().size() == 1){

                    psoKiller.addCoins(bid);
                    psoDead.removeCoins(bid);

                    ArenaManager.broadcastToArena(arena, "&6" + killer.getName() + " &4has received " + String.valueOf(bid) + " coins.");
                    dead.sendMessage(ChatColor.RED + String.valueOf(bid) + " coins have been taken from you for losing.");
                }
            }
            ArenaManager.broadcastToArena(arena, "&6" + killer.getName() + " &4has won.");
            //regular stats
            BullyPVPStatsHook.setStatsFrom1v1(killer, dead, psoKiller, psoDead);

        }
    }
}
