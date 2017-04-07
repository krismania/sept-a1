package main;

public enum ShiftTime
{
	MORNING, AFTERNOON, EVENING;
	
	@Override
	public String toString()
	{
		return name().charAt(0) + name().substring(1).toLowerCase();
	}

}
