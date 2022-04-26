package tk.empee.game.listners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import tk.empee.game.GamesManager;

import java.util.Collection;
import java.util.Locale;

public class CommandBlockerListener implements Listener {

    private final Collection<String> whitelist;
    private final GamesManager<?, ?, ?> gamesManager;
    public CommandBlockerListener(GamesManager<?, ?, ?> gamesManager, Collection<String> whitelist) {
        this.whitelist = whitelist;
        this.gamesManager = gamesManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerIssueCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(!player.isOp() && gamesManager.isPlaying(player)) {
            String command = event.getMessage().split(" ")[0].toLowerCase(Locale.ROOT);
            if(!whitelist.contains(command)) {
                event.setCancelled(true);
            }
        }
    }

}
