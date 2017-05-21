package database.model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

/**
 * Keeps track of a recurring shift. A shift is associated with an employee &
 * a day of the week, and also has start/end times.
 * @author krismania
 */
public class Shift // implements Comparable<Shift>
{
	public final int ID;
	public final int employeeID;
	
	private DayOfWeek day;
	private LocalTime start;
	private LocalTime end;
	
	public Shift(int ID, int employeeID, DayOfWeek day, LocalTime start, LocalTime end)
	{
		this.ID = ID;
		this.employeeID = employeeID;
		this.day = day;
		this.start = start;
		this.end = end;
	}
	
	// public Shift() {employeeID = 0; ID = 0;}
	
	public DayOfWeek getDay()
	{
		return day;
	}
	
	public LocalTime getStart()
	{
		return start;
	}
	
	public LocalTime getEnd()
	{
		return end;
	}
	
	/**
	 * Return the duration of the shift
	 * @author krismania
	 */
	public Duration getDuration()
	{
		return Duration.between(start, end);
	}
	
	public void setDay(DayOfWeek day)
	{
		this.day = day;
	}
	
	public void setStart(LocalTime time)
	{
		this.start = time;
	}
	
	public void setEnd(LocalTime time)
	{
		this.end = time;
	}
	
	@Override
	public String toString()
	{
		return String.format("ID: %s, EmployeeID: %s, Day: %s, Time: %s to %s (%s)",
						ID, employeeID, day.toString(), start.toString(), end.toString(), 
						getDuration().toString());
	}

	/**
	 * Commenting out comparable implementation -kg
	 */
//	@Override
//	public int compareTo(Shift s)
//	{
//		int byDay = day.compareTo(s.day);
//		
//		if (byDay == 0)
//		{
//			return time.compareTo(s.time);
//		}
//		
//		return byDay;
//	}
}
