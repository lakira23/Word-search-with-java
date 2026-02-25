package week_21_OOP;

public class Dog extends Animal{

	public Dog(int legs, String color) {
		super(legs, color,false,"Woof");
	}
	
	public void display() {
		 System.out.println("\n***** DOG *****");
	     super.display();
	}

}
