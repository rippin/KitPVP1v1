package rippin.bullyscraft.com;

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

    private List<Location> spawns = new ArrayList<Location>();
    private List<String> playersUUID = new ArrayList<String>();

    public Arena(String name){
    this.name = name;
    state = ArenaState.VACANT;
    }

    public void start(String kit){
    state = ArenaState.STARTING;
      for (Player p : getPlayers()){
          PlayerDataHandler.savePlayerData(p);
      }
    new FightStartCountdown(this, CachedData.startDelay, KitPVP1v1.plugin).startCountdown();

    }

    public void setSpawn(Location loc, int index){
        spawns.set(index, loc);

    }

    public void end(){
        for (Player p : getPlayers()){
            PlayerDataHandler.returnItemsAndLocation(p);
        }
        playersUUID.clear();
        getPlayers().clear();
        state = ArenaState.VACANT;
    }

    public List<Location> getSpawns(){
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
}
