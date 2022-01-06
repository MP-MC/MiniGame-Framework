package tk.empee.game.arena;

import tk.empee.game.game.Game;
import tk.empee.game.game.GameStatus;

public class Arena {

    private Game game;

    public void setBusy(Game game) {

        if(this.game != null) {
            throw new IllegalStateException("You can't change an arena of a game while there is a game in progress inside it");
        } else {
            GameStatus status = game.getStatus();
            if(status != null && !status.equals(GameStatus.ENDED)) {
                throw new IllegalStateException("You can't change an arena of a game while it is running");
            }
        }

        this.game = game;
    }
    public void setFree() {

        if(game != null) {
            if(game.getStatus().equals(GameStatus.ENDED)) {
                game = null;
            }
        }

    }

    public Game getRunningGame() {
        return game;
    }

}
