package week_7_Objects_and_Arrays;

import java.util.ArrayList;

public class ArrayLists_and_Objects {

	public static void main(String[] args) {
	
		Data Fred = new Data("Fred",41);
		Data Jo = new Data("Jo",43);
		Data Zoe = new Data("Zoe",37);
		
		ArrayList<Data> people = new ArrayList<Data>();
		people.add(Fred);
		people.add(Jo);
		people.add(Zoe);
		
		people.forEach(x -> x.Print());
	


	}
	private static void PrintArray(int[][] array) 
	{
		for(int i=0;i<array.length;++i)
		{
			for(int j=0;j<array[i].length;++j)
			{
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	private static void PrintDataArray(ArrayList<Data> array) 
	{
		for(Data e: array)
		{
			e.Print();
		}
	}

	


}
