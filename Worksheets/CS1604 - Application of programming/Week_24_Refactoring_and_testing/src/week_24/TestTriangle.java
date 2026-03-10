package week_24;
import java.util.Scanner;
public class TestTriangle {
	
	static Scanner scn = new Scanner(System.in);
	
	public static void main(String args[])
	{
		System.out.println("Enter first integer: ");
		int a = scn.nextInt();
		System.out.println("Enter second integer: ");
		int b= scn.nextInt();
		System.out.println("Enter third integer: ");
		int c= scn.nextInt();
		TriangleYN tri = new TriangleYN(a, b, c);
		tri.test();
		scn.close();
	}

	private static void angle_checker() {
		System.out.println("Enter the three angles:");
		int angle1 = scn.nextInt();
		int angle2 = scn.nextInt();
		int angle3 = scn.nextInt();
		int sum = angle1 + angle2 + angle3;
		if(sum == 180 && angle1 > 0 && angle2 > 0 && angle3 > 0)
		{
			System.out.println("Triangle is a valid one - sum is 180");
		}
		else
		{
			System.out.println("Triangle is not valid");
		}
		if (angle1 == 90 || angle2 == 90 || angle3 == 90)
		{
			System.out.println("Right angled as well...");
		}
	}

}
