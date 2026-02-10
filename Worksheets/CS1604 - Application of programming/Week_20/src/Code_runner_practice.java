
public class Code_runner_practice {

	public static void main(String[] args) {
		System.out.println("hello world");
		System.out.println();
		
		//test 1
		System.out.println(JoulesToWattHours(-2.0));
		System.out.println(JoulesToWattHours(-0.1));
		
		//test2
		System.out.println(JoulesToWattHours(1000000));
		System.out.println(JoulesToWattHours(1000001.0));
		
		//test3
		System.out.println(JoulesToWattHours(3600.0));
		System.out.println(JoulesToWattHours(200.0));
	}
	
	public static double JoulesToWattHours (double j) {
	    if (j <= 0.0) {
	    	return -1.0;
	    }
	    
	    else if (j>1000000.0) {
	    	return -2.0;
	    }
	    
	    else {
	    	return (j / 3600.0);
	    }
	}

}
