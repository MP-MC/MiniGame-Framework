package tk.empee.game;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Arena<T extends PlayerData<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> {

    private Plugin plugin = JavaPlugin.getProvidingPlugin(Arena.class);
    private J game;

    public void setBusy(J game) {

        if(this.game != null) {
            throw new IllegalStateException("You can't change an arena of a game while there is a game in progress inside it");
        } else {
            Game.Status status = game.getStatus();
            if(status != null && !status.equals(Game.Status.ENDED)) {
                throw new IllegalStateException("You can't change an arena of a game while it is running");
            }
        }

        this.game = game;
    }
    public void setFree() {

        if(game != null) {
            if(game.getStatus().equals(Game.Status.ENDED)) {
                Bukkit.getScheduler().runTaskAsynchronously(
                        plugin,
                        () -> {
                            resetArena();
                            game = null;
                        }
                );
            }
        }

    }

    public J getRunningGame() {
        return game;
    }

    /**
     * <b>!</b> This method is run asynchronously when the game ends
     */
    public abstract void resetArena();

}
