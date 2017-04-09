package main;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;

public class DummyDatabase implements DBInterface
{
	
	@Override
	public boolean addAccount(Account account, String password)
	{
		return true;
	}
	
	
	@Override
	public Employee buildEmployee()
	{
		return new Employee(5, "", "", "", "");
	}
	
	
	@Override
	public boolean addEmployee(Employee employee)
	{
		return true;
	}
	
	
	@Override
	public Shift buildShift(int employeeID)
	{
		return new Shift(5, employeeID, null, null);
	}
	
	
	@Override
	public boolean addShift(Shift shift)
	{
		return true;
	}
	
	
	@Override
	public boolean accountExists(String username)
	{
		return false;
	}
	
	
	@Override
	public Account getAccount(String username)
	{
		if (username.equals("JohnRulez"))
		{
			return new BusinessOwner("JohnRulez", "SomeBusiness", "John S.", "10 Some St, Some Town", "(03) 5555 5555");
		}
		else if (username.equals("badUsername"))
		{
			return null;
		}
		else
		{
			return new Customer("candy", "Candice", "Carazalez", "candy@sourcefed.com", "1234 5681");
		}
	}
	
	@Override
	public ArrayList<Customer> getAllCustomers()
	{
		ArrayList<Customer> customers = new ArrayList<Customer>();
		
		customers.add(new Customer("JamesRulez", "James", "McLennan", "testing@testing.com", "0400000000"));
		customers.add(new Customer("krismania", "Kristian", "Giglia", "s3543819@student.rmit.edu.au", "0400 123 456"));
		
		return customers;
	}
	
	@Override
	public ArrayList<BusinessOwner> getAllBusinessOwners()
	{
		ArrayList<BusinessOwner> businessOwners = new ArrayList<BusinessOwner>();
		
		businessOwners.add(new BusinessOwner("JohnRulez", "SomeBusiness", "John S.", "10 Some St, Some Town", "(03) 5555 5555"));
		
		return businessOwners;
	}
	
	
	@Override
	public Employee getEmployee(int id)
	{
		for (Employee employee: getAllEmployees())
		{
			if (employee.ID == id) return employee;
		}
		
		return null;
	}
	
	
	@Override
	public ArrayList<Employee> getAllEmployees()
	{
		ArrayList<Employee> employees = new ArrayList<Employee>();
		
		employees.add(new Employee(1, "Jack", "Patillo", "jack@smith.com", "(03) 4521 8852"));
		employees.add(new Employee(2, "Barbara", "Dunkleman", "barb@smith.com", "13255521"));
		employees.add(new Employee(3, "John", "Risinger", "rising-star@smith.com", "4852-5236"));
		employees.add(new Employee(4, "Gus", "Sorola", "gus@rt.com", "(03) 1243-5468"));
		employees.add(new Employee(5, "John", "Smith", "john@smith.com", "+61 400 685 354"));

		// shuffle the list (the DB doesn't guarantee the order
		Collections.shuffle(employees);
		
		return employees;
	}
	
	@Override
	public boolean shiftExists(DayOfWeek day, ShiftTime time, int EmployeeID)
	{
		return false;
	}
	
	@Override
	public Shift getShift(int shiftID)
	{
		return new Shift(shiftID, 3, DayOfWeek.WEDNESDAY, ShiftTime.AFTERNOON);
	}
	
	
	@Override
	public ArrayList<Shift> getShifts(int EmpID)
	{
		ArrayList<Shift> shifts = new ArrayList<Shift>();
		
		shifts.add(new Shift(1, 3, DayOfWeek.MONDAY, ShiftTime.AFTERNOON));
		shifts.add(new Shift(2, 4, DayOfWeek.MONDAY, ShiftTime.AFTERNOON));
		shifts.add(new Shift(3, 1, DayOfWeek.MONDAY, ShiftTime.EVENING));
		
		shifts.add(new Shift(4, 4, DayOfWeek.TUESDAY, ShiftTime.MORNING));
		shifts.add(new Shift(5, 4, DayOfWeek.TUESDAY, ShiftTime.AFTERNOON));
		
		shifts.add(new Shift(6, 2, DayOfWeek.THURSDAY, ShiftTime.EVENING));
		
		shifts.add(new Shift(7, 2, DayOfWeek.FRIDAY, ShiftTime.MORNING));
		shifts.add(new Shift(8, 4, DayOfWeek.FRIDAY, ShiftTime.EVENING));
		
		shifts.add(new Shift(9, 5, DayOfWeek.SATURDAY, ShiftTime.AFTERNOON));
		
		// shuffle the list (the DB doesn't guarantee the order
		Collections.shuffle(shifts);
		
		return shifts;
	}
	
	
	@Override
	public ArrayList<Shift> getShiftsNotBooked()
	{
		ArrayList<Shift> shifts = new ArrayList<Shift>();
		
		shifts.add(new Shift(1, 3, DayOfWeek.MONDAY, ShiftTime.AFTERNOON));
		
		shifts.add(new Shift(4, 4, DayOfWeek.TUESDAY, ShiftTime.MORNING));
		shifts.add(new Shift(5, 4, DayOfWeek.TUESDAY, ShiftTime.AFTERNOON));
		
		shifts.add(new Shift(6, 2, DayOfWeek.THURSDAY, ShiftTime.EVENING));
		
		shifts.add(new Shift(7, 2, DayOfWeek.FRIDAY, ShiftTime.MORNING));
		
		shifts.add(new Shift(9, 5, DayOfWeek.SATURDAY, ShiftTime.AFTERNOON));
		
		// shuffle the list (the DB doesn't guarantee the order
		Collections.shuffle(shifts);
		
		return shifts;
	}
	
	
	@Override
	public Account login(String username, String password)
	{
		return getAccount(username);
	}
	
}
