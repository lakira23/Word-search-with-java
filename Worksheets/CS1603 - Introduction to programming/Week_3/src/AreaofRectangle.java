import java.util.Scanner; 
public class AreaofRectangle {
	
    public static void main(String[] args) {
        // Declare and assign variables for length and width
    	Scanner myObj = new Scanner(System.in);
    	System.out.println("length : ");
    	int l = myObj.nextInt();
    	
    	System.out.println("width : ");
    	int w = myObj.nextInt();
        
        // Expression to calculate the area
        int A = l * w;
        
        // Print the result
        System.out.println("Area of the rectangle: " + A);
        
        myObj.close();
        //testing out onedrive
        
    }
}
