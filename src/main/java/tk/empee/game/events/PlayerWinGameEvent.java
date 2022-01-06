package tk.empee.game.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import tk.empee.game.PlayerStatus;

public class PlayerWinGameEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final PlayerStatus playerStatus;

    public PlayerWinGameEvent(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return playerStatus.getPlayer();
    }
    public PlayerStatus getPlayerStatus() {
        return this.playerStatus;
    }

}
