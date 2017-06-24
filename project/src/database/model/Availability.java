package database.model;

import java.time.LocalTime;
import java.util.List;
import java.util.TreeSet;

/**
 * Holds information about an employee's availability on a given day. This
 * availability takes into account the employee's shift times, as well as their
 * current bookings.
 *
 * @author krismania
 */
public class Availability {
    private TreeSet<TimeSpan> shifts;
    private TreeSet<TimeSpan> bookedTimes;

    public Availability() {
        shifts = new TreeSet<TimeSpan>();
        bookedTimes = new TreeSet<TimeSpan>();
    }

    public void addShift(Shift shift) {
        shifts.add(new TimeSpan(shift.getStart(), shift.getEnd()));
    }

    public void addAllShifts(List<Shift> shifts) {
        for (Shift shift : shifts) {
            addShift(shift);
        }
    }

    public void addBooking(Booking booking) {
        bookedTimes.add(new TimeSpan(booking.getStart(), booking.getEnd()));
    }

    public void addAllBookings(List<Booking> bookings) {
        for (Booking booking : bookings) {
            addBooking(booking);
        }
    }

    public TreeSet<TimeSpan> getAvailability() {
        TreeSet<TimeSpan> availability = new TreeSet<TimeSpan>();

        // iterate over shifts
        for (TimeSpan shift : shifts) {
            // availability.add(new TimeSpan(shift.getStart(), shift.getEnd()));

            LocalTime currentTime = shift.start;

            // iterate over booked times
            for (TimeSpan booking : bookedTimes) {
                // if the booking takes place during this shift, block out this
                // time
                if (overlap(shift, booking)) {
                    availability.add(new TimeSpan(currentTime, booking.start));
                    currentTime = booking.end;
                }
            }

            // include any time between last booking & shift end
            if (currentTime.isBefore(shift.end)) {
                availability.add(new TimeSpan(currentTime, shift.end));
            }
        }

        return availability;
    }

    /**
     * Checks if a given LocalTime is within this availability
     *
     * @author krismania
     */
    public boolean contains(Booking b) {
        for (TimeSpan t : getAvailability()) {
            // iterates over the timespans in this availability
            if (t.start.isBefore(b.getStart()) && t.end.isAfter(b.getEnd())) {
                // if the booking fits in at least one of them, return true
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for overlapping availability
     *
     * @author krismania
     */
    private boolean overlap(TimeSpan t1, TimeSpan t2) {
        if (t2.start.isAfter(t1.start) || t2.start.equals(t1.start)) {
            if (t2.end.isBefore(t1.end) || t2.end.equals(t1.end)) {
                return true;
            }
        }
        return false;
    }
}
