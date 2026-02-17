package week_21_OOP;

public class InheritanceTest {
	public static void main(String args[])
	{	
		System.out.println("Car: ");
		Car c = new Car();
		c.display();
		
		System.out.println("Bicycle: ");
		Bicycle bike = new Bicycle();
		bike.display();
		
		System.out.println("Scooter: ");
		Scooter sc = new Scooter();
		sc.display();
		
		
	}
}