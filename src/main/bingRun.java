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
   public static Comparator<Term> termcompartor=new Comparator<Term>(){
		@Override
		public int compare(Term a, Term b){
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
			System.out.println("Please follow the format: Bing_account_key precission query");
			System.exit(1);
		}
		
		constant.ACCOUNT_KEY=args[0];
		constant.PRECISION=Double.parseDouble(args[1]);
		
		for(int i=2; i<args.length; i++){
			bingHelper.queryTermsStr+=args[i]+" ";
		}
		
		//TODO: User input validation
		
		System.out.println("Your inital query terms: "+bingHelper.queryTermsStr+" Precision:"+constant.PRECISION);
		
		while(true){
			try {
				
				results = bingHelper.bingSearch();
				System.out.println("return document number:"+results.size());
				
				if(results.size()<10){
					System.out.println("There are fewer than 10 results, Stop!");
				}
				
				 for(int i=0; i<results.size(); i++){
					 
			           System.out.printf("---------- Document %d-----------",i+1);
			           System.out.println();
			           
			          Document d=results.get(i); 
			          d.showDocumentContent();
			          d.calculateTermFrequencyAndPutTermInSet();
			          
			       
			       Scanner in = new Scanner(System.in);
	               System.out.println("Is this document relevant? Please type (Y/N)");
			       String s=in.nextLine();
			       s=s.toLowerCase();
			       
			       while(!s.equals("y") && ! s.equals("n")){
			    	   System.out.println("Please type (Y/N)");
				       s=in.nextLine();
			       }
			       
			       if(s.equals("y")){
			    	   d.relevant=true;
			       }else{
			    	   d.relevant=false;
			       }
			        	
				 }
				 
				 //Summary, after getting all the user response for 10 documents
				 System.out.println("----------------summary----------------");
				 double currentPrecision=getCurrentPrecision(results);
				 System.out.println("current iteration using terms:"+ bingHelper.queryTermsStr);
				 System.out.println("current precision:"+currentPrecision);
				 
				 if(currentPrecision>=constant.PRECISION || currentPrecision==0 ){
					 System.out.println("----------------stop---------------");
					 break;
				 }
				 
				 //test: query vector:
				 queryTerms=new HashSet<String>();
				 String[] queryArr=bingHelper.queryTermsStr.split(" ");
				 for(int m=0; m<queryArr.length;m++){
					// System.out.println("querryARR[M]:"+queryArr[m]);
					 queryTerms.add(queryArr[m]); 
				 }
				 
				 QueryVector.queryVectorinitialize(queryTerms);
				 System.out.println("number of terms:"+AllTerms.allTerms.size() );
				 
				 // calculate the document frequency for each term
				 getDocumentFrequencies();
				 
				 
//				 for(int i=0; i<Document.documentFrequency.size(); i++){
//					 System.out.println("----------------------------------");
//					 Term qt=QueryVector.queryVector.get(i);
//					 Term dt=Document.documentFrequency.get(i);
//					 System.out.println("queryvectors:"+ "Term: "+ qt.term
//							             +" weight: "+qt.weight);
//					 System.out.println("documetnFrequency:"+"Term: "+ dt.term
//				             +" weight: "+dt.weight);
//				 }
//				 
//				for(Document d:results){
//					System.out.println("------document vector-------");
//					System.out.println(d.getTitle()+" relevant? "+d.relevant);
//					d.calculateDocumentVector();
//					for(Term t: d.documentVector){
//						System.out.println("term: "+t.term+" weight: "+t.weight);
//					}
//				}
//				 
				
				ArrayList<Term> nextQueryVector=getNextQueryVector();
				
				List<Term> newWords=createNewQueryTerms(nextQueryVector,queryTerms);
				List<Term> curretWords=getCurretQueryTerms(nextQueryVector,queryTerms);
				
				List<Term> nextQueryTerms=new ArrayList<Term>();
				nextQueryTerms.addAll(curretWords);
				nextQueryTerms.addAll(newWords);
				
				sortQueryTerms(nextQueryTerms);
				bingHelper.queryTermsStr=createQueryString(nextQueryTerms);
				System.out.println("next query terms:"+bingHelper.queryTermsStr);
				
				
				
				//At the end of each iteration, should clear previous dataset
				cleanHistory();
				
				 // tmp break
				// break;
				 
			} catch (EncoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        
        
       
	}
	
	public static void cleanHistory(){
		// the data set are chaning, need to recalculate vectors
		AllTerms.allTerms.clear();
		Document.documentFrequency.clear();
		results.clear();
		QueryVector.termArr.clear();
		QueryVector.queryVector.clear();
	}
	
	public static List<Term> createNewQueryTerms(ArrayList<Term> nextQueryVector, Set<String> queryTerms){
		ArrayList<Term> newWords=new ArrayList<Term>();
		
		Collections.sort(nextQueryVector,termcompartor );
		
		int max=2;
		
		System.out.println("--------------next query vector----------------");
//		for(Term t:nextQueryVector){
//			System.out.println("term: "+t.term+" weight: "+t.weight);
//		}

		
		for(Term t:nextQueryVector){
			if(max==0){
				break;
			}
			
			if(!queryTerms.contains(t.term)){
				System.out.println("new word:"+t.term+" weight:"+t.weight);
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
			nextQueryVector.get(i).weight+=sum;
			
			
		}
		
		for(Term t:nextQueryVector){
			if(t.weight<0){
				t.weight=0.0;
			}
		}
		
		return nextQueryVector;
	}
	
	
	public static ArrayList<Term> calculateSumOfDocumentVector(List<Document> documents, boolean relevant){
		ArrayList<Term> DocumentVectorSum=initVector();
		
		for(Document d:documents){
			if(d.relevant==relevant){
				d.calculateDocumentVector();
				ArrayList<Term> dv=d.documentVector;
				
				//System.out.println("dv.size:"+dv.size());
				for(int i=0; i<dv.size(); i++){
					Term t=dv.get(i);
					DocumentVectorSum.get(i).weight+=t.weight;
				}
				
			}
		
		}
		
		return DocumentVectorSum;
	}
	
	public static ArrayList<Term> initVector(){
		ArrayList<Term> documentVectorSumInit=new ArrayList<Term>();
		ArrayList<String> terms=AllTerms.getAllTermsArray();
		
		
		
		for(int i=0; i<terms.size(); i++){
			Term t=new Term(terms.get(i), 0.0);
			documentVectorSumInit.add(t);
		}
		//System.out.println("documentVecotrSumInit size:"+documentVectorSumInit.size()+"all term size:"+terms.size());
		return documentVectorSumInit;
	}
	
	public static void getDocumentFrequencies(){
		Document.documentFrequency=AllTerms.getDocumentFrequency(results);
	}
	
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
