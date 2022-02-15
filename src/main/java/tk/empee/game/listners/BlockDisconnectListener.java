package tk.empee.game.listners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import tk.empee.game.GameHandler;
import tk.empee.game.PlayerStatus;
import tk.empee.game.events.PlayerLeaveGameEvent;

public class BlockDisconnectListener implements Listener {

    private final GameHandler<?, ?, ?> gameHandler;
    public BlockDisconnectListener(GameHandler<?, ?, ?> gameHandler) {
        this.gameHandler = gameHandler;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerStatus<?, ?, ?> playerStatus = gameHandler.getPlayerStatus(player);

        if(playerStatus != null) {
            playerStatus.getGame().removePlayer(player, PlayerLeaveGameEvent.Reason.DISCONNECT);
        }
    }


}
