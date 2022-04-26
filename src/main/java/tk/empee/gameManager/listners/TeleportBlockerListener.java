package tk.empee.gameManager.listners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import tk.empee.gameManager.GamesManager;
import tk.empee.gameManager.PlayerData;

public final class TeleportBlockerListener implements Listener {

    private final GamesManager<?, ?, ?> gamesManager;
    public TeleportBlockerListener(GamesManager<?, ?, ?> gamesManager) {
        this.gamesManager = gamesManager;
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if(!event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            PlayerData<?, ?, ?> playerData = gamesManager.getPlayerStatus(player);
            if (!player.isOp() && playerData != null) {
                if(!playerData.canTeleport()) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
