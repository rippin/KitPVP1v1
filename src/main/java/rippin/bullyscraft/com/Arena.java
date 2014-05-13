package rippin.bullyscraft.com;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private String name;
    private String state;
    private String type;

    private List<Location> spawns = new ArrayList<Location>();
    private List<String> players = new ArrayList<String>();

    public Arena(String name){
    this.name = name;
    state = ArenaState.vacant;
    }

    public void setSpawn(Location loc, int index){
        spawns.set(index, loc);

    }

    public List<Location> getSpawns(){
        return spawns;
    }

    public String getState(){
    return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public List<String> getPlayers(){
        return players;
    }

    public void setPlayers(List<String> players){
        this.players = players;
    }
    public String getName(){
        return  name;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
}
