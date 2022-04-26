package tk.empee.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import tk.empee.game.events.*;
import tk.empee.game.exceptions.GameAlreadyStarted;
import tk.empee.game.exceptions.PlayerAlreadyInGame;
import tk.empee.game.exceptions.PlayerNotInGame;
import tk.empee.game.helpers.ArrayJoiner;
import tk.empee.game.helpers.Timer;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
import java.util.UUID;

@SuppressWarnings("unused")
public abstract class Game<T extends PlayerStatus<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> {

    private final GamesManager<T, K, J> gamesManager;

    private final K arena;

    private final int minPlayers;
    private final int maxPlayers;

    private final int countdownTime;
    private BukkitTask countdownTask;

    private final long delayEndTime;

    protected final TreeMap<UUID, T> players = new TreeMap<>();
    protected final TreeMap<UUID, T> losers = new TreeMap<>();


    protected Status status;

    /**
     * @param countdownTime Time in seconds to wait before the game can start after it <br>
     *                    has reached the minimum numbers of players
     * @param delayEndTime Time in seconds to wait before doing cleaning operations (For instance: <br>
     *                     kicking out players, resetting the arena etc... )
     */
    public Game(GamesManager<T, K, J> gamesManager, K arena, int minPlayers, int maxPlayers, int countdownTime, int delayEndTime) {
        this.gamesManager = gamesManager;

        this.arena = arena;
        this.arena.setBusy((J) this);

        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;

        this.countdownTime = countdownTime;
        this.delayEndTime = delayEndTime * 20L;

        status = Status.STARTING;
    }

    public final K getArena() {
        return arena;
    }

    public final Collection<T> getPlayers() {
        return Collections.unmodifiableCollection(players.values());
    }
    public final Collection<T> getLosers() {
        return Collections.unmodifiableCollection(losers.values());
    }
    public final T getPlayerStatus(Player player) {
        T playerStatus = players.get(player.getUniqueId());
        return playerStatus != null ? playerStatus : losers.get(player.getUniqueId());
    }

    public final Status getStatus() {
        return status;
    }

    public boolean addPlayer(Player player) throws GameAlreadyStarted, PlayerAlreadyInGame {
        if(!status.equals(Status.STARTING)) {
            throw new GameAlreadyStarted();
        }

        T playerStatus = createPlayerStatus(player);
        players.put(player.getUniqueId(), playerStatus);

        if (players.size() >= minPlayers) {
            if(players.size() == maxPlayers) {
                start();
            } else {
                startCountdown();
            }
            return true;
        }

        return false;
    }

    public boolean removePlayer(Player player, PlayerLeaveGameEvent.Reason reason) {
        T playerStatus = getPlayerStatus(player);
        if(playerStatus != null) {
            removePlayer(playerStatus, reason);
            return true;
        }

        return false;
    }
    protected void removePlayer(T playerStatus, PlayerLeaveGameEvent.Reason reason) throws PlayerNotInGame {

        if(playerStatus.getGame() != this) {
            throw new PlayerNotInGame();
        } else if(status.equals(Status.STARTING)) {
            if(players.size() == minPlayers) {
                cancelCountdown();
            }
            players.remove(playerStatus);
        } else if(status.equals(Status.STARTED)) {
            addLoser(playerStatus);
        } else {
            throw new IllegalStateException("Cannot remove player from game in status " + status);
        }

        leaveActions(playerStatus, reason);
    }
    protected void leaveActions(T playerStatus, PlayerLeaveGameEvent.Reason reason) {
        Bukkit.getPluginManager().callEvent(new PlayerLeaveGameEvent<>(playerStatus, reason));
        onPlayerLeave(playerStatus);
    }

    protected void cancelCountdown() {
        if(countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
    }
    protected void startCountdown() {
        if(countdownTask != null || countdownTime == 0) {
            return;
        }

        countdownTask = Bukkit.getScheduler().runTaskTimer(gamesManager.getPlugin(), new Timer(countdownTime) {
            public void onLastCycle() {
                start();
            }

            public void onCycle(int cycles) {
                onStartCountdown(cycles);
            }
        }, 0, 20);
    }

    /**
     * This method is going to be executed every second during the countdown
     */
    protected void onStartCountdown(int secondsPassed) {}

    public void addLoser(T playerStatus) throws PlayerNotInGame {

        if(playerStatus.getGame() != this) {
            throw new PlayerNotInGame();
        } else if(status != Status.STARTED) {
            return;
        }

        UUID playerUUID = playerStatus.getPlayer().getUniqueId();
        if(players.remove(playerUUID) != null) {
            Bukkit.getPluginManager().callEvent(new PlayerLostGameEvent<>(playerStatus));

            playerStatus.setLoser();
            losers.put(playerUUID, playerStatus);
            onPlayerLose(playerStatus);

            if(hasEndConditions()) {
                prepareStop();
            }
        }
    }

    protected void start() {
        Bukkit.getPluginManager().callEvent(new GameStartEvent<>((J) this));
        cancelCountdown();

        status = Status.STARTED;
        onStart();
    }
    protected void prepareStop() {
        Bukkit.getPluginManager().callEvent(new GameEndEvent<>((J) this));
        status = Status.ENDING;

        for(T playerStatus : players.values()) {
            playerStatus.setWinner();
            Bukkit.getPluginManager().callEvent(new PlayerWinGameEvent<>(playerStatus));
            onPlayerWin(playerStatus);
        }

        onEnd();
        startDelayedStop();
    }
    protected void startDelayedStop() {
        Bukkit.getScheduler().runTaskLater(gamesManager.getPlugin(), r -> stop(), delayEndTime);
    }
    protected void stop() {
        status = Status.ENDED;

        ArrayJoiner<T> players = new ArrayJoiner<>(this.players.values(), new ArrayJoiner<>(losers.values()));
        players.foreach(playerStatus -> leaveActions(playerStatus, PlayerLeaveGameEvent.Reason.GAME_ENDED));

        Bukkit.getPluginManager().callEvent(new GameResetArenaEvent<>( (J) this));
        arena.setFree();
    }

    public void forceStart() {
        if(status != Status.STARTING) {
            return;
        }

        start();
    }
    public void forceStop() {
        if(status == Status.ENDED) {
            return;
        }

        stop();
    }

    protected abstract T createPlayerStatus(Player player);

    public enum Status {
        STARTING,
        STARTED,
        ENDING,
        ENDED
    }

    public abstract void onStart();
    public abstract void onEnd();

    /**
     * This method is used to check if the conditions needed to end the game are met
     */
    public abstract boolean hasEndConditions();

    public abstract void onPlayerLose(T playerStatus);
    public abstract void onPlayerWin(T playerStatus);

    public abstract void onPlayerLeave(T playerStatus);

}
