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
	
	
	HashMap<String,Integer> termFrequency;
	boolean relevant;
	
	public Document(JSONObject result) throws JSONException{
		
	}
	
}
