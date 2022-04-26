package tk.empee.gameManager.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import tk.empee.gameManager.Arena;
import tk.empee.gameManager.Game;
import tk.empee.gameManager.PlayerData;

public class PlayerLeaveGameEvent<T extends PlayerData<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final T playerStatus;
    private final Reason reason;

    public PlayerLeaveGameEvent(T playerStatus, Reason reason) {
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
    public T getPlayerStatus() {
        return this.playerStatus;
    }

    public Reason getReason() {
        return reason;
    }

    public enum Reason {
        DISCONNECT,
        LEFT,
        GAME_ENDED,
        OTHER
    }

}
