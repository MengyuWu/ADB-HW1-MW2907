package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.codec.EncoderException;

import vectors.AllTerms;
import vectors.QueryVector;
import entity.Document;
import entity.Term;
import helper.bingHelper;
import constant.constant;
public class bingRun {
   public static List<Document> results;
   public static Set<String> queryTerms;
   public static String[] originalQueryTerms;
   public static Comparator<Term> termcompartor=new Comparator<Term>(){
		@Override
		public int compare(Term a, Term b){ // Compare terms by weight
			if(a.weight>b.weight){
				return -1;
			}else if(a.weight<b.weight){
				return 1;
			}else{
				return 0;
			}
		}
	};
	
	
	public static void main(String[] args) {
		//Deal with command line argument
		if(args.length<3){
			System.out.println("Input length:"+args.length);
			System.out.println("Please follow the format: <Bing Account Key> <Precision> <Query>");
			System.exit(1);
		}
		
		constant.ACCOUNT_KEY=args[0];
		try { // validate correctness data format for precision@10 value 
			constant.PRECISION=Double.parseDouble(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("Improper data type for precision@10. Please enter decimal value between 0 and 1.");
			System.exit(1);
		}
		// validate precision@10 value within allowed range 
		if (constant.PRECISION <= 0 || constant.PRECISION >= 1) {
			System.out.println("Improper range for precision@10. Please enter a value between 0 and 1.");
			System.exit(1);
		}
		
		// Add all the query terms
		for(int i=2; i<args.length; i++){
			bingHelper.queryTermsStr+=args[i].replaceAll("\\p{P}", " ") + " ";
		}
		originalQueryTerms = bingHelper.queryTermsStr.split(" ");
		
		System.out.println("Your initial query terms: "+bingHelper.queryTermsStr+" Precision: "+constant.PRECISION);
		int iteration = 1;

		while(true) {
			try {
				results = bingHelper.bingSearch(); // retrieve documents
				System.out.println("Return document number: " + results.size());
				
				if(iteration == 1 && results.size() < 10){
					System.out.println("There are fewer than 10 results. Terminating program.");
					System.exit(0);
				}
				
				for(int i = 0; i < results.size(); i++){
			        System.out.printf("---------- Document %d-----------",i+1);
			        System.out.println();
			           
			        Document d = results.get(i); 
			        d.showDocumentContent(); // Display info about document to console
			        /* Calculate term frequency for title, description. Place newfound terms in set,
			           or increment count of existing terms */
			        d.calculateTermFrequencyAndPutTermInSet();
			       
			       Scanner in = new Scanner(System.in); // Have the user mark the doc as relevant or not
	               System.out.println("Is this document relevant? Please type (Y/N)");
			       String s = in.next().toLowerCase();
			       
			       while(!s.equals("y") && ! s.equals("n")){
			    	   System.out.println("Please type (Y/N)");
				       s = in.next().toLowerCase();
			       }
			       
			       if(s.equals("y")){
			    	   d.relevant = true;
			       } else{
			    	   d.relevant = false;
			       }
				 }
				 
				 // Summary, after getting all the user responses for 10 documents
				 System.out.println("----------------Summary----------------");
				 double currentPrecision = getCurrentPrecision(results);
				 System.out.println("current iteration using terms: " + bingHelper.queryTermsStr);
				 System.out.println("current precision: " + currentPrecision);
				 
				 // Desired precision reached, or no relevant results
				 if(currentPrecision >= constant.PRECISION || (currentPrecision == 0)) {
					 System.out.println("---------------- Stop ---------------");
					 break;
				 }
				 
				 // test: query vector:
				 queryTerms=new HashSet<String>();
				 String[] queryArr=bingHelper.queryTermsStr.split(" ");
				 for(int m=0; m<queryArr.length;m++){
					 queryTerms.add(queryArr[m]); 
					 System.out.println("Query term: " + queryArr[m]);
				 }

				 QueryVector.queryVectorinitialize(queryTerms);
				 System.out.println("number of terms: " + AllTerms.allTerms.size() );
				
				 ArrayList<String> allTerms = AllTerms.getAllTermsArray();

				 // Calculate the document frequency for each term
				 getDocumentFrequencies();	 
				
				ArrayList<Term> nextQueryVector=getNextQueryVector(); // Obtained via Rocchio Algorithm

				List<Term> newWords=createNewQueryTerms(nextQueryVector,queryTerms);
				List<Term> curretWords=getCurretQueryTerms(nextQueryVector,queryTerms);
				
				// Combine words in current query + up to 2 new words, forming the next query
				List<Term> nextQueryTerms=new ArrayList<Term>();
				nextQueryTerms.addAll(curretWords);
				nextQueryTerms.addAll(newWords);
				
				sortQueryTerms(nextQueryTerms);

				// Ensure the original query terms are in the front and in the correct order
				for (int i = originalQueryTerms.length-1; i >= 0; i--) {
					int idx = 0;
					Term term = nextQueryTerms.get(0); // init to non-null value
					for (int j = 0; j < nextQueryTerms.size(); j++) {
						Term t = nextQueryTerms.get(j);
						if (t.term.equals(originalQueryTerms[i])) {
							idx = j;
							term = t;
							break;
						}
					}
					nextQueryTerms.remove(idx);
					nextQueryTerms.add(0, term); // re-add the term back to the front
				}

				bingHelper.queryTermsStr=createQueryString(nextQueryTerms);
				System.out.println("next query terms: " + bingHelper.queryTermsStr);
				
				//At the end of each iteration, should clear previous dataset
				cleanHistory();
				iteration++;
			} catch (EncoderException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void cleanHistory(){
		// the data set is changing, need to recalculate vectors
		AllTerms.allTerms.clear();
		Document.documentFrequency.clear();
		results.clear();
		QueryVector.termArr.clear();
		QueryVector.queryVector.clear();
	}
	
	public static List<Term> createNewQueryTerms(ArrayList<Term> nextQueryVector, Set<String> queryTerms){
		ArrayList<Term> newWords=new ArrayList<Term>();
		
		Collections.sort(nextQueryVector,termcompartor);
		
		int max=2; // Can only add 2 new query terms per iteration
		System.out.println("--------------next query vector----------------");

		for(Term t:nextQueryVector){
			if(max==0){
				break;
			}
			// add the term with the lowest term weight (that isn't in queryTerms yet)
			if(!queryTerms.contains(t.term)){
				newWords.add(t);
				max--;
			}
		}
		return newWords;
	}
	
	public static List<Term> getCurretQueryTerms( ArrayList<Term> nextQueryVector, Set<String> queryTerms ){
		List<Term> currentQueryTerms=new ArrayList<Term>();
		for(String str:queryTerms){
			for(Term t:nextQueryVector){
				if(t.term.equals(str)){
					currentQueryTerms.add(t);
					continue;
				}
			}
		}
		return currentQueryTerms;
	}
	
	// Sorts query terms in descending order of term weight
	public static void sortQueryTerms(List<Term> queryTerms){
		Collections.sort(queryTerms, termcompartor);
	}
	
	public static String createQueryString(List<Term> sortedQueryTerms){
		String querystring="";
		for(Term t:sortedQueryTerms){
			querystring+=t.term+" ";
		}
		return querystring;
	}
	
	// Use Rocchio's Algorithm to obtain the modified query vector (that will hopefully improve precision)
	public static ArrayList<Term> getNextQueryVector(){
		ArrayList<Term> nextQueryVector=initVector();
		ArrayList<Term> currentQueryVector=QueryVector.queryVector;
		
		ArrayList<Document> documents=(ArrayList<Document>) results;
		
		double relevant=getCurrentPrecision(results)*constant.TOP_NUMBER_OF_RESULT;
		double nonRelevant=constant.TOP_NUMBER_OF_RESULT-relevant;
		
		ArrayList<Term> relevantDocumentVectorSum=calculateSumOfDocumentVector(documents, true);
		ArrayList<Term> nonRelevantDocumentVectorSum=calculateSumOfDocumentVector(documents, false);
		
		for(int i=0; i<currentQueryVector.size(); i++){
			
			Term t=currentQueryVector.get(i);
			double qt=t.weight;
			
			//THE ROCCHIO ALGORITHM
			// Qnext=alpha*Qcurrent+beta/relevant*Sum(Dr)-gamma/nonrelevant*sum*(Dnr);
			double firstTerm=constant.alpha*qt;
			double secondTerm=constant.beta/relevant*relevantDocumentVectorSum.get(i).weight;
			double thirdTerm=constant.gamma/nonRelevant*nonRelevantDocumentVectorSum.get(i).weight;
			
			double sum=firstTerm+secondTerm-thirdTerm;
			nextQueryVector.get(i).weight+=sum; // adjust weight for term in modified query vector
		}
		
		for(Term t:nextQueryVector){
			if(t.weight<0){ // don't allow negative term weights
				t.weight=0.0;
			}
		}
		return nextQueryVector;
	}
	
	/* calculate the sum of the term weights for each term in the document vector, for all documents whose
	   relevance matches the relevant boolean parameter */
	public static ArrayList<Term> calculateSumOfDocumentVector(List<Document> documents, boolean relevant){
		ArrayList<Term> DocumentVectorSum=initVector();
		for(Document d:documents){
			if(d.relevant==relevant){
				d.calculateDocumentVector(); // calc doc vector, where each term weight = its tf-idf
				ArrayList<Term> dv=d.documentVector;
				
				for(int i=0; i<dv.size(); i++){
					Term t=dv.get(i);
					DocumentVectorSum.get(i).weight+=t.weight;
				}
			}
		}
		return DocumentVectorSum;
	}
	
	// Init the document vector with all 0 term weights
	public static ArrayList<Term> initVector(){
		ArrayList<Term> documentVectorSumInit=new ArrayList<Term>();
		ArrayList<String> terms=AllTerms.getAllTermsArray();
		
		for(int i=0; i<terms.size(); i++){
			Term t=new Term(terms.get(i), 0.0);
			documentVectorSumInit.add(t);
		}
		return documentVectorSumInit;
	}
	
	// How many docs contain the term
	public static void getDocumentFrequencies(){
		Document.documentFrequency=AllTerms.getDocumentFrequency(results);
	}
	
	// Number of relevant docs / number of total docs
	public static double getCurrentPrecision(List<Document> documents){
		int total=0;
		int relevant=0;
		for(Document d: documents){
			if(d.relevant){
				relevant++;
			}
			total++;
		}
		return (double)relevant/total;
	}
}
