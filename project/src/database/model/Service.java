package database.model;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Model of an offered service, such as a haircut or colour. Each service may
 * have an arbitrary duration, given as a {@link java.time.Duration Duration}
 *
 * @author krismania
 */
public class Service {
    public final int ID;

    private String name;
    private Duration duration;

    public Service(int id, String name, Duration duration) {
        this.ID = id;
        this.name = name;
        this.duration = duration;
    }

    public Service(int id) {
        this(id, null, null);
    }

    public LocalTime getEnd(LocalTime start) {
        return start.plus(duration);
    }

    @Override
    public String toString() {
        return String.format("%d: %s (%sm)", ID, name, duration.toMinutes());
    }

    /* Simple getters/setters */
    public String getName() {
        return name;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
