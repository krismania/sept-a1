package database.model;

/**
 * Enumeration that describes the possible shift times
 *
 * @author krismania
 * @deprecated
 */
@Deprecated
public enum ShiftTime {
    MORNING(0), AFTERNOON(1), EVENING(2);

    private int value;

    private ShiftTime(int time) {
        this.value = time;
    }

    public int getValue() {
        return value;
    }

    /**
     * Helper to format data types to Strings for processing
     *
     * @author krismania
     */
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
