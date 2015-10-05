package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vectors.AllTerms;
import constant.constant;

public class Document {
	String id;
	String url;
	String title;
	String description;
	String displayUrl;
	// How many times terms appear in the doc
	public HashMap<String,Integer> termFrequency=new HashMap<String,Integer>();
	public boolean relevant;
	public ArrayList<Term> documentVector=new ArrayList<Term>();
	
	public static ArrayList<Term> documentFrequency=new ArrayList<Term>();
	
	// Create the Document, extracting its essential attributes
	public Document(JSONObject obj) throws JSONException{
		url = obj.getString("Url");
		displayUrl = obj.getString("DisplayUrl");
		description = obj.getString("Description");
		title = obj.getString("Title");
		id = obj.getString("ID");
	}
	
	// Attributes to show to the user
	public void showDocumentContent(){
		System.out.println("url: "+displayUrl);
		System.out.println("Title: "+title);
		System.out.println("Description: "+description);
	}
	
	// calculate the tf–idf for each term, reflecting the importance of a word to a document
	public void calculateDocumentVector(){
		documentVector.clear();
		 for(Term t: documentFrequency){
			 String term=t.term;
			 
			 double w=0.0;
			 double tf=0.0; // term frequency
			 double idft=0.0; // inverse doc frequency
			 double df=t.weight; // document frequency
			 if(termFrequency.containsKey(term)){
				tf=(double)termFrequency.get(term); 
			 }
			 
			 idft=Math.log10((double)constant.TOP_NUMBER_OF_RESULT/df);
			 
			 w=tf*idft;
			 
			 Term dterm=new Term(term,w);
			 documentVector.add(dterm);
		 }
	}
	
	public void  calculateTermFrequencyAndPutTermInSet(){
		// Removes all punctuation characters, converts to lowercase, then splits the input
		String[] titlearr = title.replaceAll("\\p{P}", " ").toLowerCase().split("\\s+");
		String[] descriptionarr=description.replaceAll("\\p{P}", " ").toLowerCase().split("\\s+");
		
		// Go through each term in the title, calculating the term frequency and putting terms into set
		for(int i = 0; i < titlearr.length; i++){
			String str = titlearr[i];
			
			// Put the term into allTerms set, ignore the stop words
			if(!AllTerms.allTerms.contains(str) && !constant.stopWords.contains(str)){
				AllTerms.allTerms.add(str);
			}
			
			// Calculate term frequency
			if(termFrequency.containsKey(str)){
				int val = termFrequency.get(str);
				val++;
				termFrequency.put(str, val);
			} else{
				termFrequency.put(str, 1);
			}
		}
		
		// Do the same for the terms in the document's description
		for(int i = 0; i < descriptionarr.length; i++){
			String str = descriptionarr[i];

			if(!AllTerms.allTerms.contains(str) && !constant.stopWords.contains(str)){
				AllTerms.allTerms.add(str);
			}
			
			if(termFrequency.containsKey(str)){
				int val=termFrequency.get(str);
				val++;
				termFrequency.put(str, val);
			} else{
				termFrequency.put(str, 1);
			}
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
