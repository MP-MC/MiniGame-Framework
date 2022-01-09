package tk.empee.game;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tk.empee.game.arena.Arena;
import tk.empee.game.game.Game;
import tk.empee.game.listners.BlockDisconnectListener;
import tk.empee.game.listners.CommandBlockerListener;
import tk.empee.game.listners.PluginStopListener;
import tk.empee.game.listners.TeleportBlockerListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public abstract class GameHandler<T extends PlayerStatus<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> {

    private final JavaPlugin plugin;
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    private final ArrayList<K> arenas = new ArrayList<>();


    public GameHandler(JavaPlugin plugin) {
        this.plugin = plugin;

        pluginManager.registerEvents(new PluginStopListener(plugin, this), plugin);
    }
    public final JavaPlugin getPlugin() { return plugin; }


    protected void setCommandBlocker(Collection<String> whitelist) {
        pluginManager.registerEvents(new CommandBlockerListener(whitelist), plugin);
    }
    protected void setTeleportBlocker() {
        pluginManager.registerEvents(new TeleportBlockerListener(), plugin);
    }
    protected void setDisconnectBlocker() {
        pluginManager.registerEvents(new BlockDisconnectListener(), plugin);
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

    public List<J> getGames() {

        ArrayList<J> games = new ArrayList<>();

        for(Arena<T, K, J> arena : arenas) {
            J game = arena.getRunningGame();
            if(game != null) {
                games.add(game);
            }
        }

        return games;

    }

    public abstract void onStart(J game);
    public abstract void onEnd(J game);

    public abstract boolean checksWinCondition(J game);

    public abstract void onPlayerLost(T playerStatus);
    public abstract void onPlayerWin(T playerStatus);

    public abstract void onPlayerLeave(T playerStatus);

    public abstract void resetArena(K arena);

}
