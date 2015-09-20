package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.codec.EncoderException;

import entity.Document;
import helper.bingHelper;
import constant.constant;
public class bingRun {

	public static void main(String[] args) {
		
		while(true){
			List<Document> results;
			try {
				results = bingHelper.bingSearch();
				 for(int i=0; i<results.size(); i++){
					 
			           System.out.printf("---------- Document %d-----------",i+1);
			           System.out.println();
			           
			          Document d=results.get(i); 
			          d.showDocumentContent();
			          
			          d.calculateTermFrequency();
			          
			       
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
				 
				 // tmp break
				 break;
				 
			} catch (EncoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        
        
       
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
