package main;

import java.time.DayOfWeek;

/**
 * Keeps track of a recurring shift. A shift is associated with an employee,
 * a day of the week and a time.
 * @author krismania
 */
public class Shift
{
	public final int ID;
	private int employeeID;
	private DayOfWeek day;
	private ShiftTime time;
	
	public Shift(int ID, int employeeID, DayOfWeek day, ShiftTime time)
	{
		this.ID = ID;
		this.employeeID = employeeID;
		this.day = day;
		this.time = time;
	}
	
	public int getEmployeeID()
	{
		return employeeID;
	}
	
	public DayOfWeek getDay()
	{
		return day;
	}
	
	public ShiftTime getTime()
	{
		return time;
	}
	
	@Override
	public String toString()
	{
		return String.format("ID: %s, EmployeeID: %s, Day: %s, Time: %s",
						ID, employeeID, day.toString(), time.toString());
	}
}
