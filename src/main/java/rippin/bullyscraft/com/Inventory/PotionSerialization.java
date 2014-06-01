package rippin.bullyscraft.com.Inventory;


import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionSerialization {
    /*  returns string Example Speed:1@50 Effect:Amplifier@Duration  */
    public static String potionEffectToString(PotionEffect effect){
        PotionEffectType type = effect.getType();
        int amp = effect.getAmplifier();
        int duration = effect.getDuration();
        String potionString = type.toString() + ":" + amp + "@" +duration;

        return potionString;
    }

    public static PotionEffect fromStringToPotionEffect(String string){
        String split[] = string.split(":|@");

        return new PotionEffect(PotionEffectType.getByName(split[0]), Integer.parseInt(split[2]), Integer.parseInt(split[1]));

    }
}
