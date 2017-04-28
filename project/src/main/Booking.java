package main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;

/**
 * Keeps track of an individual booking made by a customer & tied to an employee,
 * a shift and a date.
 * @author krismania
 */
public class Booking implements Comparable<Booking>
{
	public final int ID;
	private String customer;
	private int employeeID;
	private LocalDate date;
	private LocalTime time;
	
	public Booking(int ID, String customer, int employeeID, LocalDate date, LocalTime time)
	{
		// TODO: does this class care about the name of the business? -kg
		this.ID = ID;
		this.customer = customer;
		this.employeeID = employeeID;
		this.date = date;
		this.time = time;
	}
	
	public String getCustomer()
	{
		return customer;
	}
	
	public int getEmployeeID()
	{
		return employeeID;
	}
	
	public LocalDate getDate()
	{
		return date;
	}
	
	public DayOfWeek getDay()
	{
		DateFormat weekdayFormat = new SimpleDateFormat("EEEE");
		return DayOfWeek.valueOf(weekdayFormat.format(date).toUpperCase());
	}
	
	public LocalTime getTime()
	{
		return time;
	}
	
	@Override
	public String toString()
	{
		return String.format("Booking: %d, Day: %s, Time: %s, Cust: %s",
						ID, getDay().toString(), time.toString(), customer);
	}

	/**
	 * Sort by date and then by time.
	 */
	@Override
	public int compareTo(Booking b)
	{
		int byDate = this.date.compareTo(b.date);
		
		if (byDate == 0)
		{
			return this.time.getValue() - b.time.getValue();
		}
		
		return byDate;
	}
}
