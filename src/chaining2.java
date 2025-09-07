import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class chaining2 {

    private long t;
    private final Random random = new Random();
    private final Locale ua = Locale.forLanguageTag("en-GB");

    public void demo_method() {
        ExecutorService thread_pool = Executors.newFixedThreadPool(3);
        t = System.nanoTime();
        Future<?> task = CompletableFuture
                .supplyAsync(random_int, thread_pool)
                .thenApply(sign_analyzer)
                .thenAccept(printer);
        try {
            task.get();
            thread_pool.shutdown();
            thread_pool.awaitTermination(3, TimeUnit.SECONDS);
            thread_pool.shutdownNow();
        } catch (Exception e) {
            System.out.println("Execution error: " + e.getMessage());
        }
    }

    private final Supplier<Integer> random_int = () -> {
        print_with_time("supplier started");
        sleep();
        int n = random.nextInt();
        print_with_time("supplier finished with number " + n);
        return n;
    };

    private final Function<Integer, String> sign_analyzer = num -> {
        print_with_time("analyzer started with number " + num);
        sleep();
        String status = (num > 0) ? "positive" : (num < 0) ? "negative" : "zero";
        String msg = "Number " + num + " is " + status;
        print_with_time("analyzer finished" + msg);
        return msg;
    };

    private final Consumer<String> printer = str -> {
        print_with_time("printer started");
        sleep();
        print_with_time("printer finished, result: " + str);
    };

    private void print_with_time(String message) {
        System.out.printf(ua, "%.1f ms: %s%n", time(), message);
    }

    private double time() {
        return (System.nanoTime() - t) / 1e6;
    }

    private void sleep() {
        try {
            Thread.sleep(random.nextInt(100, 500));
        } catch (InterruptedException ex) {
            System.out.println("Interrupted: " + ex.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
