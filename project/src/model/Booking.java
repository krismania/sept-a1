package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

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
	public void setCustomer(String customer) 
	{
		this.customer = customer;
	}
	
	public void setEmployee(int id)
	{
		this.employeeID = id;
	}
	
	public void setDate(LocalDate localDate)
	{
		this.date = localDate;
	}
	
	public void setTime(LocalTime time) 
	{
		this.time = time;
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
		
		//TODO added .toSecondOfDay() in order to fix error - not sure if correct though.
		if (byDate == 0)
		{
			return this.time.toSecondOfDay() - b.time.toSecondOfDay();
		}
		
		return byDate;
	}
}
