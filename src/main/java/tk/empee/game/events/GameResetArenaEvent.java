package tk.empee.game.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import tk.empee.game.game.Game;

public class GameResetArenaEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final Game game;

    public GameResetArenaEvent(Game game) {
        this.game = game;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Game getGame() {
        return game;
    }

}
