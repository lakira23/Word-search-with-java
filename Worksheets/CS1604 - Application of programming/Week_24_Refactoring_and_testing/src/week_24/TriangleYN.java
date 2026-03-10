package week_24;

public class TriangleYN
{
	int first_int, second_int, third_int, tri_base, tri_height;
	boolean is_tri = true;
	
	public TriangleYN(int sd1, int sd2, int sd3, int base, int height)
	{
		first_int = sd1; second_int = sd2; third_int = sd3;
		tri_base = base; tri_height = height;
	}
	public void test()
	{
		if ((first_int + second_int > third_int) && (second_int + third_int > first_int) && (third_int + first_int > second_int))
			System.out.println("Valid triangle");
		else
		{
			System.out.println("Invalid triangle");
			is_tri = false;
		}
	}
	public void whichtriangle()
	{
		if (is_tri)
		{
			if(first_int == second_int && second_int == third_int)
				System.out.println("Equilateral Triangle.");
			else if(first_int == second_int || first_int == third_int || second_int == third_int)
				System.out.println("Isosceles Triangle.");
			else
				System.out.println("Scalene Triangle.");
		}
		
		double area = 0.5 * tri_base * tri_height;
		System.out.println("Area of triangle is..." +area);
	}
	
	
}