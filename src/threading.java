import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class threading {

    public void demo_method() {

    }

    static class month_percent implements Callable<Double> {
        private final int month;
        month_percent(int month) {
            this.month = month;
        }
        @Override
        public Double call() throws Exception {
            Thread.sleep(500);
            return this.month / 10.0;
        }
    }

    private void demo_percent() {
        ExecutorService thread_pool = Executors.newFixedThreadPool(5);
        List<Future<Double>> tasks = new ArrayList<>();
        long t = System.nanoTime();
        for (int i = 0; i <= 12; i++) {
            tasks.add(thread_pool.submit(new month_percent(i)));
        }
        try {
            Double sum = 100.0;
            for (Future<Double> task : tasks) {
                Double res = task.get();
                System.out.println("calculated percent" + res);
                sum *= (1.0 + res / 100.0);
            }

            System.out.printf("%.1f ms: final sum %.3f%n", (System.nanoTime() - t) / 1e6, sum);
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println("execution error: " + ex.getMessage());
        }
        thread_pool.shutdown();
    }

    public void demo_threads() {
        ExecutorService thread_pool = Executors.newFixedThreadPool(3);
        thread_pool.submit(() -> System.out.println("task executed"));
        Future<String> task2 = thread_pool.submit(() -> {
            System.out.println("callable task running");
            return "callable finished";
        });
        thread_pool.shutdown();
        try {
            String res = task2.get();
            System.out.println("result" + res);
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println("execution error: " + ex.getMessage());
        }
    }

    static class random_char_task implements Callable<Character> {
        private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        @Override
        public Character call() throws Exception {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2, 15));
            return alphabet[ThreadLocalRandom.current().nextInt(alphabet.length)];
        }
    }

    public String generate_random_string(int length) {
        if (length < 0) throw new IllegalArgumentException("length must be >= 0");
        if (length == 0) return "";
        int threads = Math.min(length, Runtime.getRuntime().availableProcessors());
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        try {
            List<Future<Character>> futures = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                futures.add(pool.submit(new random_char_task()));
            }
            char[] out = new char[length];
            for (int i = 0; i < length; i++) {
                out[i] = futures.get(i).get();
            }
            return new String(out);
        } catch (Exception e) {
            throw new RuntimeException("string failed", e);
        } finally {
            pool.shutdown();
        }
    }

    private void demo_random_string() {
        long t = System.nanoTime();
        String s = generate_random_string(24);
        System.out.printf("string generated in %.1f ms: %s%n", (System.nanoTime() - t) / 1e6, s);
    }
}
