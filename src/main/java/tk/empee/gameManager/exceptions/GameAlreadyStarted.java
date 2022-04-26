package tk.empee.gameManager.exceptions;

public final class GameAlreadyStarted extends IllegalArgumentException {

    public GameAlreadyStarted() {
        this("You can't add a player while the game is already started");
    }

    public GameAlreadyStarted(String message) {
        super(message);
    }

}
