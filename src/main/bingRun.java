package main;

import java.util.ArrayList;
import java.util.Arrays;
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

	public static void main(String[] args) {
		
		while(true){
			
			try {
				results = bingHelper.bingSearch();
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
				 double currentPrecision=getCurrentPrecision(results);
				 System.out.println("current iteration using terms:"+ bingHelper.queryTermsStr);
				 System.out.println("current precision:"+currentPrecision);
				 
				 if(currentPrecision>=constant.PRECISION || currentPrecision==0 ){
					 System.out.println("----------------stop---------------");
					 break;
				 }
				 
				 //test: query vector:
				 Set<String> queryTerms=new HashSet<String>();
				 queryTerms.add("gates");
				 QueryVector.queryVectorinitialize(queryTerms);
				 System.out.println("number of terms:"+AllTerms.allTerms.size() );
				 
				 
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
				System.out.println("--------------next query vector----------------");
				for(Term t:nextQueryVector){
					System.out.println("term: "+t.term+" weight: "+t.weight);
				}
				
				
				
				 // tmp break
				 break;
				 
			} catch (EncoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        
        
       
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
				
				System.out.println("dv.size:"+dv.size());
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
		System.out.println("documentVecotrSumInit size:"+documentVectorSumInit.size()+"all term size:"+terms.size());
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
