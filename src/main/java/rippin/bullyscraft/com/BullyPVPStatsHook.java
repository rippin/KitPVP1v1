package rippin.bullyscraft.com;


import me.bullyscraft.com.Scoreboards.BullyScoreBoard;
import me.bullyscraft.com.Stats.PlayerStatsObject;
import org.bukkit.entity.Player;

public class BullyPVPStatsHook {
public static void setStatsFrom1v1(Player winner, Player loser, PlayerStatsObject psoWinner, PlayerStatsObject psoLoser){

    int highStreak1v1 = psoWinner.getHighStreak1v1();
    int currentSteak1v1 = psoWinner.getCurrentStreak1v1();
    int wins1v1 = psoWinner.getWins1v1();
    psoWinner.setWins1v1(++wins1v1);
    psoWinner.setCurrentStreak1v1(++currentSteak1v1);

    if (currentSteak1v1 > highStreak1v1){
        psoWinner.setHighStreak1v1(currentSteak1v1);
    }
    int losses = psoLoser.getLosses1v1();
    psoLoser.setCurrentStreak1v1(0);
    psoLoser.setLosses1v1(++losses);

    BullyScoreBoard b = new BullyScoreBoard(winner, psoWinner);
    b.updateWithoutPrefixes();
    BullyScoreBoard b1 = new BullyScoreBoard(loser, psoLoser);
    b1.update();

}

}
