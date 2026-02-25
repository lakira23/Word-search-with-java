package week_21_OOP;

public class Shape {

	public void area(int r) {
		System.out.println("Circle area = "+3.14*r*r);
	}
	public void area(double b, double h) {
		
		System.out.println("Triangle area="+0.5*b*h);
	}
	public void area(int l, int b) {
		System.out.println("Rectangle area="+l*b);
	}
}

class TestShape {
	public static void main(String[] args) {
		Shape myShape = new Shape();
		myShape.area(11);
		myShape.area(3.0,2.1);
		myShape.area(7,4);
	}

}


