import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class chaining {

    private long t;

    public void demo_method() {
        ExecutorService thread_pool = Executors.newFixedThreadPool(3);
        t = System.nanoTime();
        Future<?> task1 = CompletableFuture
                .supplyAsync(string_supplier, thread_pool)
                .thenApply(processor1)
                .thenApply(processor2)
                .thenAccept(printer);
        try {
            task1.get();
            System.out.printf("%.1f ms: chain finished%n", (System.nanoTime() - t) / 1e6);
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println("Execution error: " + ex.getMessage());
        }
        try {
            thread_pool.shutdown();
            thread_pool.awaitTermination(3, TimeUnit.SECONDS);
            thread_pool.shutdownNow();
        } catch (InterruptedException e) {
            System.out.println("Shutdown interrupted: " + e.getMessage());
        }
    }

    private final Supplier<String> string_supplier = new Supplier<String>() {
        @Override
        public String get() {
            System.out.printf("%.1f ms: supplier is starting%n", (System.nanoTime() - t) / 1e6);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.out.println("Supplier interrupted: " + ex.getMessage());
            }
            System.out.printf("%.1f ms: supplier finished%n", (System.nanoTime() - t) / 1e6);
            return "Original data 1";
        }
    };

    private final Function<String, String> processor1 = new Function<String, String>() {
        @Override
        public String apply(String input) {
            System.out.printf("%.1f ms: processor1 working%n", (System.nanoTime() - t) / 1e6);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.out.println("Processor1 interrupted: " + ex.getMessage());
            }
            System.out.printf("%.1f ms: processor1 done%n", (System.nanoTime() - t) / 1e6);
            return input + "next";
        }
    };

    private final Function<String, String> processor2 = input -> {
        System.out.printf("%.1f ms: processor2 working%n", (System.nanoTime() - t) / 1e6);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            System.out.println("Processor2 interrupted: " + ex.getMessage());
        }
        System.out.printf("%.1f ms: processor2 done%n", (System.nanoTime() - t) / 1e6);
        return input + " next";
    };

    private final Consumer<String> printer = new Consumer<String>() {
        @Override
        public void accept(String input) {
            System.out.printf("%.1f ms: printer received result '%s'%n", (System.nanoTime() - t) / 1e6, input);
        }
    };
}
