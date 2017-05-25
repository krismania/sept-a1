package database.model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

/**
 * Keeps track of a recurring shift. A shift is associated with an employee & a
 * day of the week, and also has start/end times.
 *
 * @author krismania
 */
public class Shift implements Comparable<Shift> {
    public final int ID;
    public final int employeeID;

    private DayOfWeek day;
    private LocalTime start;
    private LocalTime end;

    public Shift(int ID, int employeeID, DayOfWeek day, LocalTime start, LocalTime end) {
        this.ID = ID;
        this.employeeID = employeeID;
        this.day = day;
        this.start = start;
        this.end = end;
    }

    // public Shift() {employeeID = 0; ID = 0;}

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    /**
     * Return the duration of the shift
     *
     * @author krismania
     */
    public Duration getDuration() {
        return Duration.between(start, end);
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public void setStart(LocalTime time) {
        this.start = time;
    }

    public void setEnd(LocalTime time) {
        this.end = time;
    }

    /**
     * Helper to format data types to Strings for processing
     *
     * @author krismania
     */
    @Override
    public String toString() {
        return String.format("ID: %s, EmployeeID: %s, Day: %s, Time: %s to %s (%s)", ID, employeeID, day.toString(),
                start.toString(), end.toString(), getDuration().toString());
    }

    @Override
    public int compareTo(Shift s) {
        int byDay = day.compareTo(s.day);

        if (byDay == 0) {
            if (start.isBefore(s.start)) {
                return -1;
            } else if (start.equals(s.start)) {
                if (end.isBefore(s.end)) {
                    return -1;
                } else if (end.equals(s.end)) {
                    return 0;
                } else if (end.isAfter(s.end)) {
                    return 1;
                }
            }
        }

        return byDay;
    }
}
