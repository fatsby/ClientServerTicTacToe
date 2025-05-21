import java.io.IOException;
import java.util.concurrent.*;

public class CustomTimer {
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> future;
    private GameSession game;
    private int seconds;

    public CustomTimer(GameSession game, int seconds) {
        this.game = game;
        this.seconds = seconds;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        future = scheduler.schedule(() -> {
            try {
                game.playerTimedOut();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }, seconds, TimeUnit.SECONDS);
    }

    public void stop() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
    }
}
