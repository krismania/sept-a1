package main;

import java.util.ArrayList;

public class DummyDatabase implements DBInterface
{
	
	@Override
	public boolean addAccount(Account account, String password)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public Employee buildEmployee()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public boolean addEmployee(Employee employee)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public Shift buildShift(int employeeID)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public boolean addShift(Shift shift)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public boolean accountExists(String username)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public Account getAccount(String username)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Employee getEmployee(int id)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public ArrayList<Employee> getAllEmployees()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Shift getShift(int shiftID)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public ArrayList<Shift> getShifts(int EmpID)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public ArrayList<Shift> getShiftsNotBooked()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Account login(String username, String password)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void CreateDatabase()
	{
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void CreateDatabaseTable(String... strings)
	{
		// TODO Auto-generated method stub
		
	}
	
}
