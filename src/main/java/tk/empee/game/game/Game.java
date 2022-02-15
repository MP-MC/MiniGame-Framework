package tk.empee.game.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import tk.empee.game.GameHandler;
import tk.empee.game.PlayerStatus;
import tk.empee.game.arena.Arena;
import tk.empee.game.events.*;
import tk.empee.game.exceptions.GameAlreadyStarted;
import tk.empee.game.exceptions.PlayerAlreadyInGame;
import tk.empee.game.exceptions.PlayerNotInGame;
import tk.empee.game.utils.ArrayJoiner;
import tk.empee.game.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public abstract class Game<T extends PlayerStatus<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> {

    private final GameHandler<T, K, J> gameHandler;

    private final K arena;

    private final int minPlayers;
    private final int maxPlayers;

    private final int countdownTime;
    private BukkitTask countdownTask;

    private final long delayEndTime;

    protected final ArrayList<T> players = new ArrayList<>();
    protected final ArrayList<T> losers = new ArrayList<>();


    protected GameStatus status;

    /**
     * @param countdownTime Time in seconds to wait before the game can start after it <br>
     *                    has reached the minimum numbers of players
     * @param delayEndTime Time in seconds to wait before doing cleaning operations (For instance: <br>
     *                     kicking out players, resetting the arena etc... )
     */
    public Game(GameHandler<T, K, J> gameHandler, K arena, int minPlayers, int maxPlayers, int countdownTime, int delayEndTime) {
        this.gameHandler = gameHandler;

        this.arena = arena;
        this.arena.setBusy((J) this);

        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;

        this.countdownTime = countdownTime;
        this.delayEndTime = delayEndTime * 20L;

        status = GameStatus.STARTING;
    }

    public final K getArena() {
        return arena;
    }

    public final List<T> getPlayers() {
        return Collections.unmodifiableList(players);
    }
    public final List<T> getLosers() {
        return Collections.unmodifiableList(losers);
    }

    public final GameStatus getStatus() {
        return status;
    }

    public void addPlayer(Player player) throws GameAlreadyStarted, PlayerAlreadyInGame {
        if(!status.equals(GameStatus.STARTING)) {
            throw new GameAlreadyStarted();
        }

        players.add(createPlayerStatus(player));

        if (players.size() >= minPlayers) {
            if(players.size() == maxPlayers) {
                start();
            } else {
                startCountdown();
            }
        }

    }
    public void removePlayer(T playerStatus, PlayerLeaveGameEvent.Reason reason) throws PlayerNotInGame {

        if(playerStatus.getGame() != this) {
            throw new PlayerNotInGame();
        } else if(status.equals(GameStatus.STARTING)) {
            removeFromStartingGame(playerStatus);
        } else if(status.equals(GameStatus.STARTED)) {
            addLoser(playerStatus);
        }

        leaveActions(playerStatus, reason);
        //Try to remove otherwise fail silently
        losers.remove(playerStatus);

    }
    protected void removeFromStartingGame(T playerStatus) {
        if(players.size() == minPlayers) {
            cancelCountdown();
        }
        players.remove(playerStatus);
    }
    protected void leaveActions(T playerStatus, PlayerLeaveGameEvent.Reason reason) {
        Bukkit.getPluginManager().callEvent(new PlayerLeaveGameEvent<>(playerStatus, reason));
        gameHandler.onPlayerLeave(playerStatus);
        playerStatus.delete();
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

        countdownTask = Bukkit.getScheduler().runTaskTimer(gameHandler.getPlugin(), new Timer(countdownTime) {
            public void onLastCycle() {
                start();
            }

            public void onCycle(int cycles) {
                onCountdown(cycles);
            }
        }, 0, 20);
    }

    /**
     * This method is going to be executed every second during the countdown
     */
    protected void onCountdown(int secondsPassed) {}

    public void addLoser(T playerStatus) throws PlayerNotInGame {

        if(playerStatus.getGame() != this) {
            throw new PlayerNotInGame();
        } else if(status != GameStatus.STARTED) {
            return;
        }

        if(players.remove(playerStatus)) {
            Bukkit.getPluginManager().callEvent(new PlayerLostGameEvent<>(playerStatus));
            losers.add(playerStatus);
            gameHandler.onPlayerLost(playerStatus);

            if(gameHandler.checksWinCondition((J) this)) {
                prepareStop();
            }
        }
    }

    protected void start() {
        Bukkit.getPluginManager().callEvent(new GameStartEvent<>((J) this));
        cancelCountdown();

        status = GameStatus.STARTED;
        gameHandler.onStart((J) this);
    }
    protected void prepareStop() {
        Bukkit.getPluginManager().callEvent(new GameEndEvent<>((J) this));
        status = GameStatus.ENDING;

        for(T playerStatus : players) {
            Bukkit.getPluginManager().callEvent(new PlayerWinGameEvent<>(playerStatus));
            gameHandler.onPlayerWin(playerStatus);
        }

        gameHandler.onEnd((J) this);
        startDelayedStop();
    }
    protected void stop() {
        status = GameStatus.ENDED;

        ArrayJoiner<T> players = new ArrayJoiner<>(this.players, new ArrayJoiner<>(losers));
        players.foreach( playerStatus -> leaveActions(playerStatus, PlayerLeaveGameEvent.Reason.GAME_ENDED));

        Bukkit.getPluginManager().callEvent(new GameResetArenaEvent<>( (J) this));
        gameHandler.resetArena(arena);
        arena.setFree();
    }
    protected void startDelayedStop() {
        Bukkit.getScheduler().runTaskLater(gameHandler.getPlugin(), r -> stop(), delayEndTime);
    }

    public void forceStart() {

        if(status != GameStatus.STARTING) {
            return;
        }

        start();
    }
    public void forceStop() {

        if(status == GameStatus.ENDED) {
            return;
        }

        stop();
    }

    protected abstract T createPlayerStatus(Player player) throws PlayerAlreadyInGame;

}
