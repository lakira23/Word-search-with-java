package week_21_OOP;

public class Test_animals {
	public static void main(String[] args) {
		Bird bird = new Bird(2, "yellow");
		Dog doggie = new Dog(4, "golden");
		Cat pussy = new Cat(4, "toroise shell");

		bird.display();
		doggie.display();
		pussy.display();
		
	}

}
