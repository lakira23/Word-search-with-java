package week_21_OOP;

public class Cat extends Animal{
	public Cat(int legs, String color) {
		super(legs, color,true,"Meow");
		
	}
	
	public void display() {
		 System.out.println("\n***** Cat *****");
	     super.display();
	}

}
