package tk.empee.game;

import org.bukkit.entity.Player;
import tk.empee.game.arena.Arena;
import tk.empee.game.exceptions.PlayerAlreadyInGame;
import tk.empee.game.game.Game;

import java.util.TreeSet;

public class PlayerStatus<T extends PlayerStatus<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> implements Comparable<PlayerStatus<?, ?, ?>> {

    private static final TreeSet<PlayerStatus<?, ?, ?>> playerStatuses = new TreeSet<>();
    public static PlayerStatus<?, ?, ?> get(Player player) {
        for(PlayerStatus<?, ?, ?> status : playerStatuses) {
            if(status.equals(player)) {
                return status;
            }
        }
        return null;
    }

    private final Player player;
    private final J game;

    public PlayerStatus(J game, Player player) throws PlayerAlreadyInGame {
        this.player = player;
        this.game = game;

        if(PlayerStatus.get(player) != null) {
            throw new PlayerAlreadyInGame();
        }
        playerStatuses.add(this);

    }

    public final Player getPlayer() { return player; }
    public final J getGame() {
        return game;
    }

    public final void delete() {
        playerStatuses.remove(this);
    }

    public final boolean equals(Player player) {
        return this.player.equals(player);
    }

    @Override
    public final int compareTo(PlayerStatus o) {
        return player.getName().compareTo(o.player.getName());
    }
}
