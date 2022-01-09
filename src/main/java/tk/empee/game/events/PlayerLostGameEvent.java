package tk.empee.game.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import tk.empee.game.PlayerStatus;
import tk.empee.game.arena.Arena;
import tk.empee.game.game.Game;

public class PlayerLostGameEvent<T extends PlayerStatus<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final T playerStatus;

    public PlayerLostGameEvent(T playerStatus) {
        this.playerStatus = playerStatus;
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

}
