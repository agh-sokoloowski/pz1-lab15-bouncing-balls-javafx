package lab15;

import java.util.ArrayDeque;

public class MovingAverage {
    int windowSize;
    ArrayDeque<Long> values;

    public MovingAverage(int windowSize) {
        this.windowSize = windowSize;
        values = new ArrayDeque<>(windowSize);
    }

    public void add(long value) {
        while (values.size() >= windowSize) {
            values.poll();
        }
        values.offer(value);
    }

    public double getMean() {
        if (values.isEmpty()) {
            return 0;
        }
        return values.stream().mapToDouble(a -> a).average().getAsDouble();
    }
}
