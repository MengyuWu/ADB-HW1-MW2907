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
	public static Set<String> stopWords=new HashSet<String>(Arrays.asList("a","an", "able","about","across","after","all","almost", 
				"also", "am", "among", "and", "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot", 
				"could", "dear", "did", "do", "does", "either", "else", "ever", "every", "for", "from", "get", "got", "had", "has",
				"have", "he", "her", "hers", "him", "his", "how", "however", "i", "if", "in", "into", "is", "it", "its", "just", "least",
				"let", "like", "likely", "may", "me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of", "off", "often", 
				"on", "only", "or", "other", "our", "rather", "said", "say", "says", "she", "should", "since", "so", "some", "than", "that", 
				"the", "their", "them", "then", "there", "these", "they", "this", "tis", "to", "too", "twas", "us", "wants", "was", "we", 
				"were", "what", "when", "where", "which", "while", "who", "whom", "why", "will", "with", "would", "yet", "you", "your"));
}
