package tk.empee.game.listners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import tk.empee.game.PlayerStatus;

import java.util.Collection;
import java.util.Locale;

public class CommandBlockerListener implements Listener {

    private final Collection<String> whitelist;
    public CommandBlockerListener(Collection<String> whitelist) {
        this.whitelist = whitelist;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerIssueCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(!player.isOp() && PlayerStatus.get(player) != null) {
            String command = event.getMessage().split(" ")[0].toLowerCase(Locale.ROOT);
            if(!whitelist.contains(command)) {
                event.setCancelled(true);
            }
        }
    }

}
