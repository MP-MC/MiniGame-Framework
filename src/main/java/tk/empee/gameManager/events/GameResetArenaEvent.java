package tk.empee.gameManager.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import tk.empee.gameManager.Arena;
import tk.empee.gameManager.Game;
import tk.empee.gameManager.PlayerData;

public class GameResetArenaEvent<T extends PlayerData<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final J game;

    public GameResetArenaEvent(J game) {
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
