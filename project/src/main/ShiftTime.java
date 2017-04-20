package main;

public enum ShiftTime
{
	MORNING(0), AFTERNOON(1), EVENING(2);
	
	private int value;
	
	private ShiftTime(int time)
	{
		this.value = time;
	}
	
	public int getValue()
	{
		return value;
	}
	
	@Override
	public String toString()
	{
		return name().charAt(0) + name().substring(1).toLowerCase();
	}
}
