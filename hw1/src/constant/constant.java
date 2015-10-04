package constant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class constant {
	public static String ACCOUNT_KEY="pb71DGWbKoLI5Vki6bTSeAIM4otYkmdXMqSV+s/WvP0";
	public static int TOP_NUMBER_OF_RESULT=10;
	public static double PRECISION=0.9;
	
	//chapter9 p183 
	public static double alpha=1.0;
	public static double beta=0.75;
	public static double gamma=0.15;
	
	
	public static Set<String> stopWords=new HashSet<String>(Arrays.asList("a","an","the","at","from","of"));
	
}
