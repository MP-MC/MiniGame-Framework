package tk.empee.game;

public class Arena<T extends PlayerStatus<T, K, J>, K extends Arena<T, K, J>, J extends Game<T, K, J>> {

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
                game = null;
            }
        }

    }

    public J getRunningGame() {
        return game;
    }

}
