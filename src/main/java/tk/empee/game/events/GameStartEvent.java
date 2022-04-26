package tk.empee.game.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import tk.empee.game.Arena;
import tk.empee.game.Game;
import tk.empee.game.PlayerStatus;

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
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public J getGame() {
        return game;
    }

}
