package tk.empee.game.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import tk.empee.game.PlayerStatus;

public class PlayerLeaveGameEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final PlayerStatus playerStatus;
    private final Reason reason;

    public PlayerLeaveGameEvent(PlayerStatus playerStatus, Reason reason) {
        this.playerStatus = playerStatus;
        this.reason = reason;
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

    public Reason getReason() {
        return reason;
    }

    public enum Reason {
        DISCONNECT,
        LEFT,
        GAME_ENDED
    }

}
