package tk.empee.gameManager.exceptions;

public final class PlayerNotInGame extends IllegalArgumentException {

    public PlayerNotInGame() {
        this("The player isn't in this game!");
    }

    public PlayerNotInGame(String message) {
        super(message);
    }

}
