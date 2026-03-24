public class processing_speed {

	public static void main(String[] args) {
		System.out.println(ComputerSpeedTest(1000));

	}
	
	public static long ComputerSpeedTest(long ms) {
		long before = System.currentTimeMillis();
		long count = 0;
		
		while (System.currentTimeMillis() - before < ms) {
			count++;
			
		}
		return count;
	}

}
