// =======================================================
// JAVA EXAM CHEAT SHEET – ORGANISED
// =======================================================

//ALWAYS REMMBER DIVISION IS DONE WITH DOUBLES: 1 /360 IS GONNA GIVE 1, I WANT 1.0 / 360.0
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class different_fundementals {

	// ===================================================
	// MAIN METHOD (RUN / TEST METHODS HERE)
	// ===================================================
	public static void main(String[] args) {

		studentID_formatter();
		Price_Calculator();

		String[] usernames = {"Alex", "Sam", "Jordan", "Taylor"};
		System.out.println(Username_checker(usernames));

		Temperature_analysis();
		ExamScores();
		grade_lookup();
	}

	// ===================================================
	// INPUT + TYPE CASTING (int → String → float)
	// ===================================================
	public static void studentID_formatter() {
		Scanner text = new Scanner(System.in);

		System.out.println("give studentID >");
		int studentID = text.nextInt();

		// int → String
		String s1 = String.valueOf(studentID);
		String s2 = Integer.toString(studentID);

		System.out.println(s1 + " " + s2);
		System.out.println("length: " + s1.length());

		// int → float
		float f = (float) studentID;
		System.out.println(f / 2);
	}

	// ===================================================
	// SIMPLE PRICE CALCULATOR
	// ===================================================
	public static void Price_Calculator() {
		Scanner text = new Scanner(System.in);

		System.out.println("number of items:");
		int numItems = text.nextInt();

		System.out.println("price per item:");
		int pricePerItem = text.nextInt();

		int total = numItems * pricePerItem;

		float floatTotal = (float) total;
		String strTotal = String.valueOf(floatTotal);

		System.out.println("Total price is £" + strTotal);
	}

	// ===================================================
	// ARRAY SEARCH (USERNAME CHECK)
	// ===================================================
	public static boolean Username_checker(String[] usernames) {
		Scanner text = new Scanner(System.in);
		System.out.println("give username:");
		String input = text.nextLine();

		// Method 1: Arrays.asList
		if (Arrays.asList(usernames).contains(input)) {
			return true;
		}

		// Method 2: for-each loop
		for (String name : usernames) {
			if (name.equals(input)) {
				return true;
			}
		}
		return false;
	}

	// ===================================================
	// ARRAY + SORT + AVERAGE
	// ===================================================
	public static void Temperature_analysis() {
		int[] temps = new int[5];
		Scanner text = new Scanner(System.in);

		System.out.println("enter 5 temperatures:");
		for (int i = 0; i < temps.length; i++) {
			temps[i] = text.nextInt();
		}

		System.out.println(Arrays.toString(temps));

		Arrays.sort(temps);
		System.out.println(Arrays.toString(temps));

		System.out.println("highest: " + temps[temps.length - 1]);
		System.out.println("average (min & max): " +
				(float) (temps[0] + temps[temps.length - 1]) / 2);
	}

	// ===================================================
	// ARRAYLIST + SORTING
	// ===================================================
	public static void ExamScores() {
		ArrayList<Integer> scores = new ArrayList<>();
		Scanner text = new Scanner(System.in);

		System.out.println("enter 6 scores:");
		for (int i = 0; i < 6; i++) {
			scores.add(text.nextInt());
		}

		Collections.sort(scores);

		System.out.println("lowest: " + scores.get(0));
		System.out.println("highest: " + scores.get(scores.size() - 1));
	}

	// ===================================================
	// HASHMAP LOOKUP
	// ===================================================
	public static void grade_lookup() {
		HashMap<String, Integer> grades = new HashMap<>(Map.of(
				"Alex", 94,
				"Bob", 54,
				"Lakira", 32,
				"Hob", 54
				));

		Scanner text = new Scanner(System.in);
		System.out.println("enter name:");
		String name = text.nextLine();

		if (grades.containsKey(name)) {
			System.out.println("grade: " + grades.get(name));
		} else {
			System.out.println("student not found");
		}
	}

	// ===================================================
	// STRING → DOUBLE + ERROR HANDLING
	// ===================================================
	public static ArrayList<Double> CToF(ArrayList<String> C) {
		ArrayList<Double> F = new ArrayList<>();

		for (int i = 0; i < C.size(); i++) {

			if (C.get(i) == null || C.get(i).isEmpty()) {
				System.out.println(i + " is null or empty");
				return null;
			}

			try {
				double value = Double.parseDouble(C.get(i));
				double converted = value * 9 / 5 + 32;
				F.add(converted);
			}
			catch (Exception e) {
				System.out.println(i + " conversion error");
				return new ArrayList<>();
			}
		}
		return F;
	}

	//=======================================================
	// SWITCH / CASE — EXAM SYNTAX + EXAMPLES
	//=======================================================

	private static void grade_system() {
		int mark = 2;

		switch (mark) {
		case 1:
			System.out.println("Fail");
			break;

		case 2:
			System.out.println("Pass");
			break;

		case 3:
			System.out.println("Merit");
			break;

		default:
			System.out.println("Invalid mark");
		}
	}

	private static void day_tracker(String day) {
		switch (day) {
		case "MON":
			System.out.println("Monday");
			break;

		case "TUE":
			System.out.println("Tuesday");
			break;

		default:
			System.out.println("Unknown day");
		}
	}
}

/*
=======================================================
QUICK FIND – EXAM SYNTAX (SCAN THIS UNDER PRESSURE)
=======================================================

ARRAY
----------------
int[] nums = {1, 2, 3, 4};

for (int n : nums) {
	System.out.println(n);
}


STRING ↔ NUMBER
----------------
int x = Integer.parseInt("123");
double d = Double.parseDouble("12.5");

String s1 = String.valueOf(123);
String s2 = Integer.toString(123);

-------------------
NULL / EMPTY CHECKS
-------------------
if (str == null) {}
if (str.isEmpty()) {}
if (str.isBlank()) {}

------
ARRAYS
------
Arrays.sort(arr);
Arrays.toString(arr);
arr.length;

---------
ARRAYLIST
---------
ArrayList<Integer> list = new ArrayList<>();
list.add(5);
list.get(0);
list.size();
Collections.sort(list);

-------
HASHMAP
-------
HashMap<String, Integer> map = new HashMap<>();
map.put("A", 1);
map.get("A");
map.containsKey("A");

------
SCANNER
-------
Scanner sc = new Scanner(System.in);
int x = sc.nextInt();
String s = sc.nextLine();

----------
TRY / CATCH
-----------
try {
	int x = Integer.parseInt(str);
}
catch (Exception e) {
	System.out.println("error");
}

catch (NumberFormatException e) {
	system.out.println("Invalid Number");
}

catch (NullPOinterExceptions e){
	system.out.println("Null");
}
 
=======================================================
== vs .equals() — EXAM QUICK RULE
=======================================================

PRIMITIVE TYPES  (use ==)
------------------------
int
double
float
char
boolean
long

Example:
int a = 5;
int b = 5;
if (a == b) {}   // ✅ correct


OBJECT TYPES (use .equals())
----------------------------
String
Integer
Double
Boolean
Arrays
ArrayList
HashMap
Any custom object

Example:
String s1 = new String("hi");
String s2 = new String("hi");
 */