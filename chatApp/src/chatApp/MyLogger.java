package chatApp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger {
    private static Logger logger = Logger.getLogger("chatApp.MyLogger");

    public static void log(Level level, String message) {
        logger.log(level, "myLogger:\n" + message);
    }
}
