package main;

import java.sql.Time;
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
	private Time time;
	
	public Shift(int ID, int employeeID, DayOfWeek day, Time time)
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
	
	public Time getTime()
	{
		return time;
	}
}
