package vectors;

import java.util.ArrayList;
import java.util.Set;

import entity.Term;

public class QueryVector {
  public static ArrayList<String> termArr=new ArrayList<String>();
  public static ArrayList<Term> queryVector=new ArrayList<Term>();
  
  
  public static void getTerms(){
	  termArr=AllTerms.getAllTermsArray();
  }
  
  public static void queryVectorinitialize(Set<String> queryTerms){
	  getTerms(); // Get all the terms
	  
	  // Check if the query contains the term and initialize accordingly
	  for(int i=0; i<termArr.size(); i++){
		   String term=termArr.get(i);
		   if(queryTerms.contains(term)){
			  Term t=new Term(term,1.0);
			  queryVector.add(t);
		   } else{
			   Term t=new Term(term,0.0);
			   queryVector.add(t);
		   }
	  }
  }
}
