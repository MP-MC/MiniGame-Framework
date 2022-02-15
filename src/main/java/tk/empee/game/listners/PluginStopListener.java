package tk.empee.game.listners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tk.empee.game.GameHandler;
import tk.empee.game.game.Game;

public class PluginStopListener implements Listener {

    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(PluginStopListener.class);
    private final GameHandler<?, ?, ?> gameHandler;

    public PluginStopListener(GameHandler<?, ?, ?> gameHandler) {
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
