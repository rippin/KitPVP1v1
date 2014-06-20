package rippin.bullyscraft.com;

import me.bullyscraft.com.Classes.Kit;
import me.bullyscraft.com.Classes.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import rippin.bullyscraft.com.Configs.CachedData;
import rippin.bullyscraft.com.Countdowns.FightStartCountdown;
import rippin.bullyscraft.com.Inventory.PlayerDataHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {
    private String name;
    private ArenaState state;
    private boolean premium = false;
    private boolean bidBoolean = false;
    private int bid;

    private Location spawns[] = new Location[2] ;
    private List<String> playersUUID = new ArrayList<String>();

    public Arena(String name){
    this.name = name;
    state = ArenaState.VACANT;
    }

    public void start(Kit kit, List<Player> players){
     getPlayersUUID().clear();
     state = ArenaState.STARTING;
        int i = 0;
      for (Player p : players){
          PlayerDataHandler.savePlayerData(p);
          kit.giveKit1v1(p);
          getPlayersUUID().add(p.getUniqueId().toString());
          p.teleport(getSpawns()[i]);
          i++;
      }
    new FightStartCountdown(this, CachedData.startDelay, KitPVP1v1.plugin).startCountdown();

    }

    public void setSpawn(Location loc, int index){
        spawns[index] = loc;

    }

    public void end(){
        for (Player p : getPlayers()){
            PlayerDataHandler.returnItemsAndLocation(p);
        }
        playersUUID.clear();
        getPlayers().clear();
        state = ArenaState.VACANT;
        bidBoolean = false;
        bid = 0;
    }

    public void disable(){
        for (Player p : getPlayers()){
            PlayerDataHandler.returnItemsAndLocation(p);
        }
        playersUUID.clear();
        getPlayers().clear();
        state = ArenaState.DISABLED;
    }

    public void enable(){
        for (Player p : getPlayers()){
            PlayerDataHandler.returnItemsAndLocation(p);
        }
        playersUUID.clear();
        getPlayers().clear();
        state = ArenaState.VACANT;
    }

    public Location[] getSpawns(){
        return spawns;
    }

    public ArenaState getState(){
    return state;
    }

    public void setState(ArenaState state){
        this.state = state;
    }

    public List<String> getPlayersUUID(){
        return playersUUID;
    }

    public List<Player> getPlayers(){
        List<Player> players = new ArrayList<Player>();
        for (String uuid : getPlayersUUID()){
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            players.add(p);
        }
        return players;
    }
    public void setPlayersUUID(String uuid1, String uuid2){
        getPlayersUUID().add(uuid1);
        getPlayersUUID().add(uuid2);
    }
    public void removePlayer(Player player){
        getPlayers().remove(player);
        getPlayersUUID().remove(player.getUniqueId().toString());
    }

    public void setPlayersUUID(List<String> players){
        this.playersUUID = players;
    }
    public String getName(){
        return  name;
    }
    public void setPremium(boolean premium){
        this.premium = premium;
    }
    public boolean getPremium(){
        return premium;
    }
    public boolean isThereBid(){
        return bidBoolean;
    }
    public int getBid(){
        return bid;
    }
    public void setBid(int bid){
        this.bid = bid;
    }

    public void setBidBoolean(boolean bid){
        this.bidBoolean = bid;
    }
}
