package tk.empee.gameManager.exceptions;

public final class PlayerAlreadyInGame extends IllegalArgumentException {

    public PlayerAlreadyInGame() {
        this("The player must first exit the game that is playing");
    }

    public PlayerAlreadyInGame(String message) {
        super(message);
    }

}
