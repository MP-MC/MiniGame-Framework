package tk.empee.gameManager.listners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tk.empee.gameManager.Game;
import tk.empee.gameManager.GamesManager;

public class PluginStopListener implements Listener {

    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(PluginStopListener.class);
    private final GamesManager<?, ?, ?> gamesManager;

    public PluginStopListener(GamesManager<?, ?, ?> gamesManager) {
        this.gamesManager = gamesManager;
    }

    @EventHandler
    public void onPluginStop(PluginDisableEvent event) {

        if(event.getPlugin().equals(plugin)) {

            plugin.getLogger().info("Stopping all games...");
            for(Game<?, ?, ?> game : gamesManager.getRunningGames()) {
                game.forceStop();
            }

            plugin.getLogger().info("All games has been force stopped!");

        }

    }

}
