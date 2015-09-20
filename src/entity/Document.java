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
	public HashMap<String,Integer> termFrequency=new HashMap<String,Integer>();
	public boolean relevant;
	public ArrayList<Term> documentVector=new ArrayList<Term>();
	
	public static ArrayList<Term> documentFrequency=new ArrayList<Term>();
	
	public Document(JSONObject obj) throws JSONException{
		url=obj.getString("Url");
		displayUrl=obj.getString("DisplayUrl");
		description=obj.getString("Description");
		title=obj.getString("Title");
		id=obj.getString("ID");
		
		
	}
	
	public void showDocumentContent(){
		System.out.println("url: "+displayUrl);
		System.out.println("Title: "+title);
		System.out.println("Description: "+description);
		
	}
	
	
	public void calculateDocumentVector(){
		documentVector.clear();
		 for(Term  t: documentFrequency){
			 String term=t.term;
			 
			 double w=0.0;
			 double tf=0.0;
			 double idft=0.0;
			 double df=t.weight;
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
		//This first removes all punctuation characters, folds to lowercase, then splits the input,
		//TODO: method2: replaceAll("[^a-zA-Z0-9 ]", "")  keep only number and letters
		//TODO: considering remove all stop words like a, and, the
		String[] titlearr = title.replaceAll("\\p{P}", " ").toLowerCase().split("\\s+");
		String[] descriptionarr=description.replaceAll("\\p{P}", " ").toLowerCase().split("\\s+");
		
		
		
		for(int i=0; i<titlearr.length; i++){
			String str=titlearr[i];
			//System.out.println(str);
			
			// put the term into allTerms set, ignore the stop words
			
			if(!AllTerms.allTerms.contains(str) && !constant.stopWords.contains(str)){
				AllTerms.allTerms.add(str);
			}
			
			// calculate term frequency
			if(termFrequency.containsKey(str)){
				int val=termFrequency.get(str);
				val++;
				termFrequency.put(str, val);
			}else{
				termFrequency.put(str, 1);
			}
		}
		
		
		for(int i=0; i<descriptionarr.length; i++){
			String str=descriptionarr[i];
			//System.out.println(str);
			// calculate term frequency
			
			// put the term into allTerms set
			if(!AllTerms.allTerms.contains(str) && !constant.stopWords.contains(str)){
				AllTerms.allTerms.add(str);
			}
			
			if(termFrequency.containsKey(str)){
				int val=termFrequency.get(str);
				val++;
				termFrequency.put(str, val);
			}else{
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
