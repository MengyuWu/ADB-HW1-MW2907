package helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.json.JSONArray;
import org.json.JSONObject;

import entity.Document;
import constant.constant;
public class bingHelper {
 
	 
	//Provide your account key here. 
	static String accountKey = constant.ACCOUNT_KEY;
	public static String queryTermsStr="";
	
  public static List<Document>  bingSearch() throws EncoderException{
	List<Document> documents;
	
	String bingUrl = String.format("https://api.datamarket.azure.com/Bing/Search/Web?$top=%d&$format=json",constant.TOP_NUMBER_OF_RESULT); 
	//String bingUrl="https://api.datamarket.azure.com/Bing/Search/Web?Query=%27gates%27&$top=10&$format=json";
	byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
	String accountKeyEnc = new String(accountKeyBytes);

	URLCodec urlCoder=new URLCodec();
	
	URL url=null;
	try {
		url = new URL(bingUrl+"&Query=%27"+urlCoder.encode(queryTermsStr)+"%27");
	} catch (MalformedURLException e) {
		
		e.printStackTrace();
	}
	
	URLConnection urlConnection=null;
	try {
		urlConnection = url.openConnection();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
			
	InputStream inputStream=null;
	try {
		inputStream = (InputStream) urlConnection.getContent();
	} catch (IOException e) {
	
		e.printStackTrace();
	}		
	
	
	byte[] contentRaw = new byte[urlConnection.getContentLength()];
	try {
		inputStream.read(contentRaw);
	} catch (IOException e) {
	
		e.printStackTrace();
	}
	String content = new String(contentRaw);

	documents=documentsFromResult(content);
	
	 return documents;
  }
  
  
  public static List<Document> documentsFromResult(String result){
	  JSONObject jsonObj = new JSONObject(result);
      JSONArray resultArray = jsonObj.getJSONObject("d").getJSONArray("results");
      
      List<Document> documents=new ArrayList<Document>();
      
      for(int i=0; i<resultArray.length(); i++){
    	  Document d=new Document(resultArray.getJSONObject(i));
    	  documents.add(d);
      }
      
      return documents;
  }
	
}
