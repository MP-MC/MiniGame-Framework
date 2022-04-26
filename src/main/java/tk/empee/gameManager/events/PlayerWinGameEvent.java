package tk.empee.gameManager.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import tk.empee.gameManager.Arena;
import tk.empee.gameManager.Game;
import tk.empee.gameManager.PlayerData;

public class PlayerWinGameEvent<T extends PlayerData<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final T playerStatus;

    public PlayerWinGameEvent(T playerStatus) {
        this.playerStatus = playerStatus;
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return playerStatus.getPlayer();
    }
    public T getPlayerStatus() {
        return this.playerStatus;
    }

}
