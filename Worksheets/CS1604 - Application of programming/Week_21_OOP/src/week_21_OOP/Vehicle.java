package week_21_OOP;

public class Vehicle
{
	private int nwheels;
	private int nseats;
	private int max_speed;
	
	public Vehicle(int nw, int num_seats, int ms)
	{
		nwheels = nw;
		nseats = num_seats;
		max_speed = ms;
	}
	public void display()
	{
		System.out.println("Number of wheels = "+nwheels);
		System.out.println("Number of seats = "+nseats);
		System.out.println("Maximum speed is = " + max_speed);
	}
}