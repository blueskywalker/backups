/**
 * Created by kkim on 8/25/15.
 */
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class HelloForkJoin extends RecursiveTask<Integer> {

    private static final int SEQUENTIAL_THRESHOLD = 5;

    private final int[] data;
    private final int start;
    private final int end;

    public HelloForkJoin(int[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    public HelloForkJoin(int[] data) {
        this(data, 0, data.length);
    }

    @Override
    protected Integer compute() {
        final int length = end - start;
        if (length < SEQUENTIAL_THRESHOLD) {
            return computeDirectly();
        }
        final int split = length / 2;
        final HelloForkJoin left = new HelloForkJoin(data, start, start + split);
        left.fork();
        final HelloForkJoin right = new HelloForkJoin(data, start + split, end);
        return Math.max(right.compute(), left.join());
    }

    private Integer computeDirectly() {
        System.out.println(Thread.currentThread() + " computing: " + start
                + " to " + end);
        int max = Integer.MIN_VALUE;
        for (int i = start; i < end; i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }
        return max;
    }

    public static void main(String[] args) {
        // create a random data set
        final int[] data = new int[1000];
        final Random random = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(100);
        }

        // submit the task to the pool
        final ForkJoinPool pool = new ForkJoinPool(4);
        final HelloForkJoin finder = new HelloForkJoin(data);
        System.out.println(pool.invoke(finder));
    }
}