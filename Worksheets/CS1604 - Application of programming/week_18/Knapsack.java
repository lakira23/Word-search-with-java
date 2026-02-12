//Knapsack resource allocation program
//Bodgitt and Scarper Limited Software Solutions
//This program is a "proof of concept" and not the full working model
//The solution of the Knapsack problem on the first 1000 prime numbers
//using a standard Hill Climbing procedure 
public class Knapsack 
{
//Solution (binary inclusion list), 'solution(i)' = 1 means item 'i' is in the Knapsack
static public ArrayList<Byte> solution = new ArrayList<Byte>();
//The weights being used
static public ArrayList<Double> weights = new ArrayList<Double>();
//The number of weights
static public int n = -1;
//Index of the last change - used for undoing the small change
static public int index = -1;
//This is the capacity of the Knapsack
static public double limit = -1.0;
//Shared random object
static public Random rand = null;
//Create a uniformly distributed random integer between 'aa' and 'bb' inclusive
static public int UI(int aa,int bb)
{
int a = Math.min(aa,bb);
int b = Math.max(aa,bb);
if (rand == null) 
{
rand = new Random();
rand.setSeed(System.nanoTime());
}
int d = b - a + 1;
int x = rand.nextInt(d) + a;
return(x);
}
//Create a uniformly distributed random double between 'a' and 'b' inclusive
static public double UR(double a,double b)
{
if (rand == null) 
{
rand = new Random();
rand.setSeed(System.nanoTime())
}
return((b-a)*rand.nextDouble()+a);
}
//This method reads in a text file and parses all of the numbers in it
//This code is not very good and can be improved!
//But it should work!!!
//It takes in as input a string filename and returns an array list of Doubles
static public ArrayList<Double> ReadNumberFile(String filename) throws Exception
{
ArrayList<Double> res = new ArrayList<Double>();
Reader r;
try
{
r = new BufferedReader(new FileReader(filename));
StreamTokenizer stok = new StreamTokenizer(r);
stok.parseNumbers();
stok.nextToken();
while (stok.ttype != StreamTokenizer.TT_EOF) 
{
if (stok.ttype == StreamTokenizer.TT_NUMBER)
{
res.add(stok.nval);
}
stok.nextToken();
}
}
catch(Exception E)
{
System.out.println("+++ReadFile: "+E.getMessage());
throw(E);
}
    return(res);
}
//This is the fitness function for the Knapsack
//Add up all of the weights
//If it is less than or equal to the limit, then the results is the fitness
//If the limit (Knapsack capacity) is exceeded, the fitness is limit - sum (i.e. negative)
//This is a MAXIMISATION problem
public static double ComputeFitness()
{
double sum = 0.0;
for(int i=0;i<n;++i)
{
sun += weights.get(i)*solution.get(i);
}
if (sum > limit)
{
sum = limit - sum;
}
return(sum);
}
//Read in the 1000 prime numbers
public static void ReadPrimes() throws Exception
{
String filename = "1000 Primes.txt";
weights = ReadNumberFile(filename);
//The number of possible weights
n = weights.size();
//Set the Knapsack capacity - 1000 seems a good limit
limit = 1000.0;
}
//Create a random starting point
//This is a selection of random weights to be placed in the Knapsack
public static void CreateRandomStart()
{
solution.clear()
for(int i=0;i<n;++i)
{
//A '1' means the item is in the Knapsack
solution.add((byte)UI(0,1));
}
}
//We test the program from here
//We want to run 10 repeats of 10,000 iterations and take the average
//The best fitness is 1000 (= 'limit') 
public static void main(String args[]) throws Exception
{
double sumfit = 0.0;
//The number of repeated experiments
int rep = 0;

//Read in the prime numbers
ReadPrimes();
//Run the HC 'rep' times and compute the average fitness
for(i=0;i<rep;++i)
{
double fit = RunHC("RMHC",rep,10000);
System.out.println("Run " + i + " = "+fit);
sumfit += fit;
}
sumfit /= (double)rep;
System.out.println("Average = "+sumfit);
}
public static double RunHC(String id,int rep,int iter)
{
//Create a random starting point in the search space
CreateRandomStart();
//Compute the fitness of this solution
double fitness = ComputeFitness();
for(int i=0;i<iter++i)
{
//Uncomment this line to see how the HC is working each iteration
//System.out.println(id + " " + rep + " " + i + " " + fitness);
//Make a small change to the current solution
SmallChange();
//Compute the fitness of the changed solution
double newfitness = ComputeFitness();
//If we have not improved - undo the change
if (newfitness > fitness)
{
UndoChange();
{
else
{
//Otherwise we keep the new solution
fitness = newfitness;
}
}
//Uncomment the following lines to display the best solution
/*System.out.println(solution);
for(int i=0;i<n;++i)
{
if (solution.get(i) == (byte)1) System.out.print(weights.get(i)+ " ");
}*/
//System.out.println();
return(fitness);
}
//Undo the change that was made
//'index' should be the position of the last change that was made
private static void UndoChange() 
{
//Get the new value
byte temp = solution.get(index);
//'Flip' the bit and set - this should now be the old value 
temp = (byte) ((temp + (byte)1) %% (byte)2);
solution.set(index,temp);
}
//Make a small change to the current solution as per HC algorithm
private static void SmallChange() 
{
//Choose a random weight to either put in or take out of the Knapsack
index = UI(0,n-1);
//Record the old value
byte oldvalue = solution.get(index);
//'Flip' the bit
if (oldvalue == 0)
{
solution.set(index,(byte)1);
}
else
{
solution.set(index,(byte)0);
}
}
