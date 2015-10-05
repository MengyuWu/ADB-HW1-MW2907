package constant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class constant {
	public static String ACCOUNT_KEY="pb71DGWbKoLI5Vki6bTSeAIM4otYkmdXMqSV+s/WvP0"; // BING search account key
	public static int TOP_NUMBER_OF_RESULT=10; // Return the top 10 results
	public static double PRECISION=0.9; // desired precision@10 value
	
	// Reasonable parameter values, taken from Chapter 9 p183 of Introduction to Information Retrieval (Manning, Raghavan, Schutze)
	public static double alpha=1.0; 
	public static double beta=0.75;
	public static double gamma=0.15;
	
	// Set of stopwords
	public static Set<String> stopWords=new HashSet<String>(Arrays.asList("a","an","the","at","of", "and", "or", "but", "then", "off", "on", "like"));
}
