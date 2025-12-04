public class SettingsManager {

    private static String gameMode = "Singleplayer"; // default
    private static int boardSize = 3; // default 3x3

    public static String getGameMode() {
        return gameMode;
    }

    public static void setGameMode(String mode) {
        gameMode = mode;
    }

    public static int getBoardSize() {
        return boardSize;
    }

    public static void setBoardSize(int size) {
        boardSize = size;
    }
}
