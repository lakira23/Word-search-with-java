package week_21_OOP;

public class Animal {
	private  int num_legs;
	private  String colour;
	private boolean animal_is_wild;
	private String sound;
	
	public Animal(int legs, String color, boolean iswild, String animal_sound) {
		if (legs < 1 || legs > 4) {
			throw new IllegalArgumentException("Number of legs must be between 1 and 4");
		}
		num_legs = legs;
		colour = color;
		animal_is_wild = iswild;
		sound = animal_sound;
		
	}
	public void display() {
		System.out.println("number of legs : " + num_legs);
		System.out.println("colour : "+ colour);
		System.out.println("is the animal wild? : "+ animal_is_wild);
		
		make_sound();
	}
	
	public void make_sound() {
		System.out.println(sound);
	}
}
