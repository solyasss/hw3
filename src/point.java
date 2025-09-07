import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class point {
    private final int x;
    private final int y;

    public point() {
        this.x = 0;
        this.y = 0;
    }

    public point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int get_x() { return x; }
    public int get_y() { return y; }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    private long start_time;
    private final Random rand_gen = new Random();

    public void demo_method() {
        ExecutorService thread_pool = Executors.newFixedThreadPool(3);
        start_time = System.nanoTime();
        Future<?> task = CompletableFuture
                .supplyAsync(coords_supplier, thread_pool)
                .thenApply(point_builder)
                .thenApply(sign_analyzer)
                .thenAccept(printer);
        try {
            task.get();
            thread_pool.shutdown();
            thread_pool.awaitTermination(3, TimeUnit.SECONDS);
            thread_pool.shutdownNow();
        } catch (Exception e) {
            log("error: " + e.getMessage());
        }
    }

    private final Supplier<int[]> coords_supplier = () -> {
        log("supplier started");
        pause();
        int coord_x = rand_gen.nextInt(-10, 11);
        int coord_y = rand_gen.nextInt(-10, 11);
        log("supplier finished  coordinates=[" + coord_x + ", " + coord_y + "]");
        return new int[]{coord_x, coord_y};
    };

    private final Function<int[], point> point_builder = coords -> {
        log("point builder started from coordinates=" + coords[0] + ", " + coords[1]);
        pause();
        point p = new point(coords[0], coords[1]);
        log("point builder finished" + p);
        return p;
    };

    private final Function<point, String> sign_analyzer = p -> {
        log("analyzer started for " + p);
        pause();
        int px = p.get_x();
        int py = p.get_y();
        String result;
        if (px == 0 && py == 0) {
            result = p + " origin";
        } else if (px == 0) {
            result = p + "on Y axis";
        } else if (py == 0) {
            result = p + "on X axis";
        } else if (px > 0 && py > 0) {
            result = p + "q- 1";
        } else if (px < 0 && py > 0) {
            result = p + "q- 2";
        } else if (px < 0 && py < 0) {
            result = p + "q- 3";
        } else {
            result = p + "q- 4";
        }
        log("analyzer finished" + result);
        return result;
    };

    private final Consumer<String> printer = output -> {
        log("printer started");
        pause();
        log("printer finished" + output);
    };

    private void log(String message) {
        System.out.printf("%.1f ms  %s%n", elapsed_ms(), message);
    }

    private double elapsed_ms() {
        return (System.nanoTime() - start_time) / 1e6;
    }

    private void pause() {
        try {
            Thread.sleep(rand_gen.nextInt(123, 555));
        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();
        }
    }
}
