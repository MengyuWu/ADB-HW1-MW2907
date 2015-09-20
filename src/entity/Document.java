package entity;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Document {

	String id;
	String url;
	String title;
	String description;
	String displayUrl;
	public static HashMap<String,Integer> termFrequency=new HashMap<String,Integer>();
	public boolean relevant;
	
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
	
	public void  calculateTermFrequency(){
		//This first removes all punctuation characters, folds to lowercase, then splits the input,
		// method2: replaceAll("[^a-zA-Z0-9 ]", "")  keep only number and letters
		String[] titlearr = title.replaceAll("\\p{P}", " ").toLowerCase().split("\\s+");
		String[] descriptionarr=description.replaceAll("\\p{P}", " ").toLowerCase().split("\\s+");
		
		for(int i=0; i<titlearr.length; i++){
			String str=titlearr[i];
			System.out.println(str);
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
			System.out.println(str);
			if(termFrequency.containsKey(str)){
				int val=termFrequency.get(str);
				val++;
				termFrequency.put(str, val);
			}else{
				termFrequency.put(str, 1);
			}
		}
	}
}
