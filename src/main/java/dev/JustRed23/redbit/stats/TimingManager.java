package dev.JustRed23.redbit.stats;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TimingManager {

    private static final Map<String, Long> timings = new ConcurrentHashMap<>();

    public static void startTiming(String name) {
        timings.put(name, System.currentTimeMillis());
    }

    public static void stopTiming(String name) {
        long time = System.currentTimeMillis() - timings.get(name);
        timings.remove(name);
        timings.put(name, time);
    }

    public static void clearTimings() {
        timings.clear();
    }

    public static Map<String, Long> getTimings() {
        return timings;
    }

    public static long getTiming(String name) {
        return timings.get(name);
    }

    public static void printTimings() {
        timings.forEach((name, time) -> System.out.println(name + ": " + time + "ms"));
    }
}
