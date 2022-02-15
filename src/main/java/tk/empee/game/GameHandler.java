package tk.empee.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import tk.empee.game.arena.Arena;
import tk.empee.game.game.Game;
import tk.empee.game.listners.BlockDisconnectListener;
import tk.empee.game.listners.CommandBlockerListener;
import tk.empee.game.listners.PluginStopListener;
import tk.empee.game.listners.TeleportBlockerListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public abstract class GameHandler<T extends PlayerStatus<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> {

    private final JavaPlugin plugin;
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    private final ArrayList<K> arenas = new ArrayList<>();

    public GameHandler(JavaPlugin plugin) {
        this.plugin = plugin;

        pluginManager.registerEvents(new PluginStopListener(this), plugin);
    }
    public final JavaPlugin getPlugin() { return plugin; }

    @Nullable
    public T getPlayerStatus(Player player) {

        for(K arena : arenas) {
            J game = arena.getRunningGame();
            if(game != null) {
                T playerStatus = game.getPlayerStatus(player);
                if(playerStatus != null) {
                    return playerStatus;
                }
            }
        }

        return null;

    }
    public boolean isPlaying(Player player) {
        return getPlayerStatus(player) != null;
    }

    protected void setCommandBlocker() {
        setCommandBlocker(Collections.EMPTY_LIST);
    }
    protected void setCommandBlocker(Collection<String> whitelist) {
        pluginManager.registerEvents(new CommandBlockerListener(this, whitelist), plugin);
    }
    protected void setTeleportBlocker() {
        pluginManager.registerEvents(new TeleportBlockerListener(this), plugin);
    }
    protected void setDisconnectBlocker() {
        pluginManager.registerEvents(new BlockDisconnectListener(this), plugin);
    }


    public K getFreeArena() {
        for(K arena : arenas ) {
            if(arena.getRunningGame() == null) {
                return arena;
            }
        }
        return null;
    }
    public final void addArena(K arena) {
        arenas.add(arena);
    }

    public List<J> getRunningGames() {

        ArrayList<J> games = new ArrayList<>();

        for(Arena<T, K, J> arena : arenas) {
            J game = arena.getRunningGame();
            if(game != null) {
                games.add(game);
            }
        }

        return Collections.unmodifiableList(games);

    }

    public abstract void onStart(J game);
    public abstract void onEnd(J game);

    public abstract boolean checksWinCondition(J game);

    public abstract void onPlayerLost(T playerStatus);
    public abstract void onPlayerWin(T playerStatus);

    public abstract void onPlayerLeave(T playerStatus);

    public abstract void resetArena(K arena);

}
