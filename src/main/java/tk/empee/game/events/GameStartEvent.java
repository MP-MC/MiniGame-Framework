package tk.empee.game.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import tk.empee.game.PlayerStatus;
import tk.empee.game.arena.Arena;
import tk.empee.game.game.Game;

public class GameStartEvent<T extends PlayerStatus<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final J game;

    public GameStartEvent(J game) {
        this.game = game;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public J getGame() {
        return game;
    }

}
