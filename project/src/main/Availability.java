package main;

import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Holds information about an employee's availability on a given day. This availability
 * takes into account the employee's shift times, as well as their current bookings.
 * @author krismania
 */
class Availability
{
	private ArrayList<TimeSpan> slots;
	
	public Availability()
	{
		slots = new ArrayList<TimeSpan>();
	}
	
	public void addTimeSpan(LocalTime start, LocalTime end)
	{
		TimeSpan t = new TimeSpan(start, end);
		slots.add(t);
	}
	
	public ArrayList<TimeSpan> getSlots()
	{
		return slots;
	}
}

class TimeSpan
{
	final LocalTime start;
	final LocalTime end;
	
	TimeSpan(LocalTime start, LocalTime end)
	{
		if (end.isAfter(start))
		{
			this.start = start;
			this.end = end;
		}
		else
		{
			throw new IllegalArgumentException("TimeSpan end must be after start");
		}
	}
}