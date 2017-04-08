package main;

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
	private int shiftID;
	private Date date;
	
	public Booking(int ID, String customer, int employeeID, int shiftID, Date date)
	{
		// TODO: does this class care about the name of the business? -kg
		this.ID = ID;
		this.customer = customer;
		this.employeeID = employeeID;
		this.shiftID = shiftID;
		this.date = date;
	}
	
	public String getCustomer()
	{
		return customer;
	}
	
	public int getEmployeeID()
	{
		return employeeID;
	}
	
	public int getShiftID()
	{
		return shiftID;
	}
	
	public Date getDate()
	{
		return date;
	}
	
	@Override
	public String toString()
	{
		return String.format("Booking: %d, Cust: %s", ID, customer);
	}

	@Override
	public int compareTo(Booking b)
	{
		return this.date.compareTo(b.date);
	}
}
