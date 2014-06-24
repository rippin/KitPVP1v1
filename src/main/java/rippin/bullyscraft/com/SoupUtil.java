package rippin.bullyscraft.com;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SoupUtil {
public static int checkSoup(Player player){
    PlayerInventory inventory = player.getInventory();
    int i = 0;
    for (ItemStack item : inventory){
        if (item == null){
            continue;
        }
        if (item.getType().equals(Material.MUSHROOM_SOUP)){
            i = i + item.getAmount();
        }
    }
    return i;
  }

}
