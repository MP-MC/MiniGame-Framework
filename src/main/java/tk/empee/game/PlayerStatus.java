package tk.empee.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tk.empee.game.arena.Arena;
import tk.empee.game.game.Game;

public class PlayerStatus<T extends PlayerStatus<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> implements Comparable<T> {

    private final Player player;
    private final J game;
    private boolean teleportFlag;
    private Boolean isLoser = null;

    public PlayerStatus(J game, Player player) {
        this.player = player;
        this.game = game;
    }

    public final Player getPlayer() { return player; }
    public final J getGame() {
        return game;
    }

    public void teleport(Location location) {
        teleportFlag = true;
        player.teleport(location);
        teleportFlag = false;
    }

    public final boolean equals(Player player) {
        return this.player.equals(player);
    }

    public boolean canTeleport() {
        return teleportFlag;
    }
    public void allowTeleport() { teleportFlag = true; }
    public void disallowTeleport() { teleportFlag = false; }

    @Override
    public int compareTo(@NotNull T o) {
        return player.getUniqueId().compareTo(o.getPlayer().getUniqueId());
    }

    /**
     * @return <b>true</b>, if the player has lost <br>
     * <b>false</b>, if the player has won <br>
     * <b>null</b>, if the player is still in-game
     */
    public Boolean isLoser() {
        return isLoser;
    }
    public void setLoser() {
        isLoser = true;
    }
    public void setWinner() { isLoser = false; }

}
