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

    public PlayerStatus(J game, Player player) {
        this.player = player;
        this.game = game;
    }

    public final Player getPlayer() { return player; }
    public final J getGame() {
        return game;
    }

    public void teleport(Location location) {
        allowTeleport();
        player.teleport(location);
        disallowTeleport();
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
}
