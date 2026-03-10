package week_24;
import java.util.Scanner;
public class TestTriangle {
	public static void main(String args[])
	{
		Scanner scn = new Scanner(System.in);
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
	
	
}
