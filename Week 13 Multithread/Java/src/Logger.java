import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static synchronized void log(String message) {
        System.out.println("[" + LocalDateTime.now().format(formatter) + "] " + message);
    }

    public static synchronized void error(String message, Throwable throwable) {
        System.err.println("[" + LocalDateTime.now().format(formatter) + "] ERROR: " + message);
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }
}