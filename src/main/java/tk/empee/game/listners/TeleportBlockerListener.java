package tk.empee.game.listners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import tk.empee.game.GamesManager;
import tk.empee.game.PlayerData;

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
