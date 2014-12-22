package edu.ub.ir.oof1.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrDocument;


public class Solrj {
    
	public static void main(String args[]){
		String url = "http://localhost:8983/solr";
		SolrServer server = new HttpSolrServer(url);
		
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("newsID", "NYT_1815846" );
		
		Map<String,String> temp = new HashMap<String,String>();
		temp.put("add", "dsgsgw");
		doc.addField("userID","");
		doc.addField("userCount", "");
		doc.addField("URL", "http://www.nytimes.com/2007/01/01/nyregion/01mbrfs-collision.html");
		doc.addField("Authors", "MICHAEL WILSON");
		
		doc.addField("Place","New York");
		doc.addField("Lat", 3.45);
		doc.addField("Lon", 65.4);
		doc.addField("Title", "driver Metro Briefing | New York: Queens: Man Charged In Accident");
		doc.addField("PublishedDate", "Thu, 06 Nov 2014 19:52:22 GMT");
		doc.addField("Category", "New York and Region");
		doc.addField("Sub-Category","Buffalo");
		
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("Roads and Traffic");
		tags.add("Drunken and Reckless Driving");
		tags.add("Accidents and Safety");
		doc.addField("Tags_Descriptors",tags);
		
		tags = new ArrayList<String>();		
		tags.add("Buffalo, NY");
		tags.add("Niagara Falls, NY");
		
		doc.addField("Tags_Locations", tags);
		
		tags = new ArrayList<String>();
		tags.add("Wilson, Michael");
		tags.add("Quiles, Alexie");
		doc.addField("Tags_People", tags);
		doc.addField("Content", "A driver from New Rochelle ran another vehicle off the Long Island Expressway yesterday morning, then pulled over, and at the sight of a severely injured passenger in the other vehicle, fled on foot, the police said. Two of the three men in the injured passenger's vehicle, a GMC Yukon, and a passing off-duty police officer, Michael Leone, caught up with the driver, the police said. The collision occurred shortly before 6 a.m. on the eastbound lanes of the expressway at 69th Street in Maspeth, Queens, the police said. The man's sport utility vehicle veered into the GMC Yukon, pushing it onto the shoulder, the police said. The badly injured passenger, a woman who was in the back seat, was said to be in critical condition yesterday evening at New York Hospital Medical Center of Queens. All three men in the Yukon suffered minor injuries. The driver of the first vehicle, Alexie Quiles, 26, was charged with vehicular assault and driving while intoxicated, the police said. He was also charged with illegal possession of a weapon after officers found a gun in his vehicle, the police said.");
		doc.addField("Publisher", "New York Times");
				
		doc.addField("Count", 0);
		
		try {
			server.add(doc);
			server.commit();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SolrQuery query = new SolrQuery();
	    query.setQuery( "New York Times" );
	    query.setRequestHandler("/query");
//	    query.setHighlight(true).setHighlightSnippets(3).setParam("hl.fl", "Content", "Title");
	    
	    
	    try {
	    	QueryResponse resp = server.query(query);
	    	Iterator<SolrDocument> iter = resp.getResults().iterator();
	    	String id = "";
	    	
	    	//Iterate through the articles returned for the requested query
	    	while (iter.hasNext()){
	    		SolrDocument resultDoc = iter.next();
	    		SolrInputDocument updateDoc = new SolrInputDocument();
	    		String userToBeAdded = "harish";
	    		id = (String) resultDoc.getFieldValue("newsID");
	    		System.out.println("For article:" + id);
	    		
	    		for(String attribute: resultDoc.getFieldNames()){
	    			//Retain all the records except userCount and Count
	    			if(!attribute.equals("userCount") && !attribute.equals("Count"))
	    				updateDoc.addField(attribute, resultDoc.getFieldValue(attribute));
	    			else if(attribute.equals("userCount")){
	    				List<String> userCount = (List<String>) resultDoc.getFieldValue("userCount");
	    				List<String> updatedCount = new ArrayList<String>();
	    				boolean userFound = false;
	    				
	    				if(userCount != null && userCount.size() > 0){
		    				for(String user: userCount){
		    					String[] buffer = user.split(":");
		    					if(buffer.length > 1){
		    						if(buffer[0].equals(userToBeAdded)){
		    							int count = Integer.parseInt(buffer[1]);
		    							updatedCount.add(buffer[0] + ":" + ++count);
		    							userFound = true;
		    						} else {
		    							//This is not the user who read this article at the moment
		    							updatedCount.add(user);
		    						}
		    					} else {
		    						//Means this user is reading this article for the first time or
		    						//This is the first time any user is reading this article
		    						updatedCount.add(userToBeAdded + ":" + 1);
		    					}
		    				}
		    				
		    				if(userFound == false) //Means this user is reading this article for the first time
		    					updatedCount.add(userToBeAdded + ":" + 1);
	    				} else {
	    					//This logic will not happen in real time. Added for worst-case scenario
	    					updatedCount.add(userToBeAdded + ":" + 1);
	    				}
	    				
	    				updateDoc.addField("userCount", updatedCount);
	    			} else {
	    				//Increment the global count for this article.
	    				//Used to track the most viewed articles irrespective of the user
	    				Map<String,Integer> tempMap = new HashMap<String,Integer>(1);
	    	    		tempMap.put("inc", 1);
	    	    		
	    	    		updateDoc.addField("Count", tempMap);
	    			}
	    			
	    			//To get the tokens required to be highlighted
		    		if(resp.getHighlighting().get(id) != null) {
			    		List<String> snip = resp.getHighlighting().get(id).get(attribute);
			    		
			    		if(snip != null && snip.size() > 0){
				    		for(String foo: snip)
				    			System.out.println(foo);
			    		}
			    	}
	    		}
	    		server.add(updateDoc);
    			server.commit();
	    	}
	    	
//	    	if (iter.hasNext()){
//	    		SolrDocument resultDoc = iter.next();
//	    		SolrInputDocument updateDoc = new SolrInputDocument();
//	    		
//	    		for(String attribute: resultDoc.getFieldNames()){
//	    			if(!attribute.equals("count"))
//	    			updateDoc.addField(attribute, resultDoc.getFieldValue(attribute));
//	    		}
//	    		Map<String,Integer> tempMap = new HashMap<String,Integer>(1);
//	    		tempMap.put("inc", 1);
//	    		
//	    		updateDoc.addField("count", tempMap);
//	    		
//    			server.add(updateDoc);
//    			server.commit();
//    			server.shutdown();
//	    	}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    
	}
}
