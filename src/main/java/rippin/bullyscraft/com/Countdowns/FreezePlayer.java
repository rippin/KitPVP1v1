package rippin.bullyscraft.com.Countdowns;


import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rippin.bullyscraft.com.Arena;

public class FreezePlayer {

    public static void freezeArenaPlayers(Arena arena){
        for (Player p : arena.getPlayers()){
            freeze(p);
        }
    }

    public static void unFreezeArenaPlayers(Arena arena){
        for (Player p : arena.getPlayers()){
            unFreeze(p);
        }
    }


    public static void freeze(Player player){
        player.setWalkSpeed(0.0F);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10000, 128));
    }

    public static void unFreeze(Player player){
        player.setWalkSpeed(0.2F);
        player.removePotionEffect(PotionEffectType.JUMP);
    }

}
