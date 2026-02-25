package week_21_OOP;

public class Bird extends Animal{
	
	public Bird(int legs, String color) {
		super(legs, color,true,"Cherp");
		
	}
	
	public void display() {
		 System.out.println("\n***** Bird *****");
	     super.display();
	}

}
