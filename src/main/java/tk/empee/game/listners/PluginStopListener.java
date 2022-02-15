package tk.empee.game.listners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import tk.empee.game.GameHandler;
import tk.empee.game.game.Game;

public class PluginStopListener implements Listener {

    private final Plugin plugin;
    private final GameHandler<?, ?, ?> gameHandler;

    public PluginStopListener(Plugin plugin, GameHandler<?, ?, ?> gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
    }

    @EventHandler
    public void onPluginStop(PluginDisableEvent event) {

        if(event.getPlugin().equals(plugin)) {

            plugin.getLogger().info("Stopping all games...");
            for(Game<?, ?, ?> game : gameHandler.getRunningGames()) {
                game.forceStop();
            }

            plugin.getLogger().info("All games has been force stopped!");

        }

    }

}
