package tk.empee.gameManager.listners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import tk.empee.gameManager.GamesManager;
import tk.empee.gameManager.PlayerData;
import tk.empee.gameManager.events.PlayerLeaveGameEvent;

public class BlockDisconnectListener implements Listener {

    private final GamesManager<?, ?, ?> gamesManager;
    public BlockDisconnectListener(GamesManager<?, ?, ?> gamesManager) {
        this.gamesManager = gamesManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData<?, ?, ?> playerData = gamesManager.getPlayerStatus(player);

        if(playerData != null) {
            playerData.getGame().removePlayer(player, PlayerLeaveGameEvent.Reason.DISCONNECT);
        }
    }


}
