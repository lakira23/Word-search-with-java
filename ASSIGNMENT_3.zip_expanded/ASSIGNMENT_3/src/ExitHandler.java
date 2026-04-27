public class ExitHandler {
    private static volatile boolean exitRequested = false;

    public static void requestExit() {
        exitRequested = true;
    }

    public static boolean isExitRequested() {
        return exitRequested;
    }
}
