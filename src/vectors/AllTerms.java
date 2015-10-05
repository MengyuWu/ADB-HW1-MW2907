package vectors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.Document;
import entity.Term;

public class AllTerms {
  public static Set<String> allTerms=new HashSet<String>();
  
  public static ArrayList<String> getAllTermsArray(){
	  ArrayList<String> alltermsarr=new ArrayList<String>(allTerms);
	  return alltermsarr;
  }
  
  public static ArrayList<Term> getDocumentFrequency(List<Document> documents){
	 
	  ArrayList<String> terms=getAllTermsArray();
	  ArrayList<Term> documentFrequency=new ArrayList<Term>();
	  
	  for(String t:terms){
		  double count=0;
		  // Count how many docs contain the term
		  for(Document d:documents){
			  if(d.termFrequency.containsKey(t)){
				  count++;
			  }
		  }
		  // Add the term and its count to documentFrequency
		  Term term=new Term(t,count);
		  documentFrequency.add(term);  
	  }
	  return documentFrequency;
  }
  
}
