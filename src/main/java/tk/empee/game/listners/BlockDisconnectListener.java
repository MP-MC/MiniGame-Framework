package tk.empee.game.listners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import tk.empee.game.PlayerStatus;
import tk.empee.game.events.PlayerLeaveGameEvent;

public class BlockDisconnectListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        PlayerStatus playerStatus = PlayerStatus.get(event.getPlayer());
        if(playerStatus != null) {
            playerStatus.getGame().removePlayer(playerStatus, PlayerLeaveGameEvent.Reason.DISCONNECT);
        }
    }


}
