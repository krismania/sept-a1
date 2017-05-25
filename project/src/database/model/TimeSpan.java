package database.model;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Comparable TimeSpan objects which hold 2 LocalTimes.
 *
 * @author krismania
 */
public class TimeSpan implements Comparable<TimeSpan> {
    public final LocalTime start;
    public final LocalTime end;

    /**
     * Create a new TimeSpan with the given {@code start} and {@code end} times.
     * End time must come after start time.
     *
     * @throws IllegalArgumentException
     *             if end time is not after start time.
     */
    public TimeSpan(LocalTime start, LocalTime end) {
        if (end.isAfter(start)) {
            this.start = start;
            this.end = end;
        } else {
            throw new IllegalArgumentException("TimeSpan end must be after start");
        }
    }

    public Duration getDuration() {
        return Duration.between(start, end);
    }

    /**
     * Helper to duration start points to end points
     *
     * @author krismania
     */
    @Override
    public int compareTo(TimeSpan t) {
        if (start.isBefore(t.start)) {
            return -1;
        } else if (start.equals(t.start)) {
            if (end.isBefore(t.end)) {
                return -1;
            } else if (end.equals(t.end)) {
                return 0;
            } else if (end.isAfter(t.end)) {
                return 1;
            }
        }

        return 1;
    }

    /**
     * Helper to format data types to Strings for processing
     *
     * @author krismania
     */
    @Override
    public String toString() {
        return start.toString() + " to " + end.toString();
    }
}
