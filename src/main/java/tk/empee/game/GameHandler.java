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

public abstract class GameHandler {

    private final JavaPlugin plugin;
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    private final ArrayList<Arena> arenas = new ArrayList<>();


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


    public Arena getFreeArena() {
        for(Arena arena : arenas ) {
            if(arena.getRunningGame() == null) {
                return arena;
            }
        }
        return null;
    }
    public final void addArena(Arena arena) {
        arenas.add(arena);
    }

    public List<Game> getGames() {

        ArrayList<Game> games = new ArrayList<>();

        for(Arena arena : arenas) {
            Game game = arena.getRunningGame();
            if(game != null) {
                games.add(game);
            }
        }

        return games;

    }

    public abstract void onStart(Game game);
    public abstract void onEnd(Game game);

    public abstract boolean checksWinCondition(Game game);

    public abstract void onPlayerLost(PlayerStatus playerStatus);
    public abstract void onPlayerWin(PlayerStatus playerStatus);

    public abstract void onPlayerLeave(PlayerStatus playerStatus);

    public abstract void resetArena(Arena arena);

}
