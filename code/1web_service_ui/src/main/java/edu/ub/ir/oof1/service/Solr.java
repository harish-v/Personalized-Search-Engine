/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.service;

import edu.ub.ir.oof1.Properties;
import edu.ub.ir.oof1.Util.DataProcess;
import edu.ub.ir.oof1.model.*;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author san
 */
public class Solr extends DataService {

    private static SolrServer SERVER = new HttpSolrServer(Properties.SOLR_URL);

    @Override
    public void addDoc(AbstractModel _inDoc) {
        try {
            SolrInputDocument _docToAdd = getSolrDocForModel(_inDoc);
            SERVER.add(_docToAdd);
            SERVER.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Used only for searching a single news article
     *
     * @param _query
     * @param resultType
     * @return
     */
    @Override
    public List<AbstractModel> query(String _query, Class<? extends AbstractModel> resultType) {
        List<AbstractModel> out = new ArrayList<AbstractModel>();

        SolrQuery query = new SolrQuery();
        query.setQuery(_query);
        query.setRequestHandler(Properties.SOLR_QUERY_HANDLER);

        try {
            QueryResponse response = SERVER.query(query);
            SolrDocumentList docs = response.getResults();
            for (SolrDocument doc : docs) {
                AbstractModel model = null;

                String news_id = (String) doc.getFieldValue("news_id");
                //System.out.println("news_id : " + news_id);
                //System.out.println("highlighting : " + response.getHighlighting().get(news_id));
                if (news_id != null && response.getHighlighting().get(news_id) != null) {
                    model = getModelForSolrDoc(doc, resultType, response.getHighlighting().get(news_id));
                } else {
                    model = getModelForSolrDoc(doc, resultType);
                }

                out.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    /**
     * Used only for searching a single news article
     *
     * @param _query
     * @return
     */
    public Suggestions getAutoSuggest(String _query) {
        Suggestions out = new Suggestions();
        List<String> suggs = new ArrayList<String>();
        System.out.println("Solr query : " + _query);
        SolrQuery query = new SolrQuery();
        query.setQuery(_query);
        query.setRequestHandler(Properties.SOLR_AUTO_SUGGEST_HANDLER);

        try {
            QueryResponse response = SERVER.query(query);
            suggs = spellcheck(response);
            System.out.println("Solr sugg : " + suggs);
            out.setSuggestions(suggs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Final Solr: " + out);
        return out;
    }

    /**
     * Used for searching multiple news articles
     *
     * @param _query
     * @param resultType
     * @param _user_id
     * @return
     */
    @Override
    public NewsResults query(String _query, Class<? extends AbstractModel> resultType, String _user_id) {

        List<News> out = new ArrayList<News>();
        List<String> spellCheck = new ArrayList<String>();

        boolean queryBack = false;

        SolrQuery query = new SolrQuery();
        query.setQuery(_query + " AND " + _user_id);
        query.setRequestHandler(Properties.SOLR_QUERY_HANDLER);

        try {
            QueryResponse response = SERVER.query(query);
            SolrDocumentList docs = response.getResults();
            System.out.println("***Before queryBack***");
            queryBack = queryConstruct(docs, out, _user_id, response, resultType);

            if (queryBack) {
                System.out.println("Entered queryback");
                query = new SolrQuery();
                query.setQuery(_query + " NOT " + _user_id);
                query.setRequestHandler(Properties.SOLR_QUERY_HANDLER);

                response = SERVER.query(query);
                docs = response.getResults();
                queryBack = queryConstruct(docs, out, _user_id, response, resultType);

                if (queryBack) {
                    query = new SolrQuery();
                    query.setQuery(_query);
                    query.setRequestHandler("/spellcheck");

                    response = SERVER.query(query);
                    spellCheck = spellcheck(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new NewsResults(out, spellCheck);
    }

    public void updateDocClick(String _user_id, String _news_id) {
        try {
            
            SolrInputDocument updateDoc = new SolrInputDocument();
            SolrQuery query = new SolrQuery();
            query.setQuery("news_id:" + _news_id);
            query.setRequestHandler(Properties.SOLR_QUERY_HANDLER);
            
            QueryResponse response = SERVER.query(query);
            SolrDocumentList docs = response.getResults();
            if(docs == null){
                return;
            }
            if(docs.size() < 1){
                return ;
            }
            for (String attribute : docs.get(0).getFieldNames()) {
                //Retain all the records except userCount and Count
                if (!attribute.equals("user_count") && !attribute.equals("count") && !attribute.equals("score") && !attribute.equals("_version_")) {
                    updateDoc.addField(attribute, docs.get(0).getFieldValue(attribute));
                } else if (attribute.equals("user_count")) {
                    List<String> userCount = (List<String>) docs.get(0).getFieldValue("user_count");
                    List<String> updatedCount = new ArrayList<String>();
                    boolean userFound = false;
                    
                    if (userCount != null && userCount.size() > 0) {
                        for (String user : userCount) {
                            String[] buffer = user.split(":");
                            if (buffer.length > 1) {
                                if (buffer[0].equals(_user_id)) {
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
                                userFound = true;
                                updatedCount.add(_user_id + ":" + 1);
                            }
                        }
                        
                        if (userFound == false) //Means this user is reading this article for the first time
                        {
                            updatedCount.add(_user_id + ":" + 1);
                        }
                    } else {
                        //This logic will not happen in real time. Added for worst-case scenario
                        updatedCount.add(_user_id + ":" + 1);
                    }
                    
                    updateDoc.addField("user_count", updatedCount);
                } else if (attribute.equals("count")) {
                //Increment the global count for this article.
                    //Used to track the most viewed articles irrespective of the user
                    Map<String, Integer> countIncMap = new HashMap<String, Integer>(1);
                    countIncMap.put("inc", 1);
                    
                    updateDoc.addField("count", countIncMap);
                }
            }
            SERVER.add(updateDoc);
            SERVER.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Solr() {
    }

    private boolean queryConstruct(SolrDocumentList docs, List<News> out,
            String _user_id, QueryResponse response, Class<? extends AbstractModel> resultType) {
        boolean queryBack = false;
        int docCount = 0;
        try {
            //List<SolrInputDocument> updateDocList = new ArrayList<SolrInputDocument>();
			Map<String, Float> scoreMap = new HashMap<String, Float>();
            for (SolrDocument doc : docs) {
				int count = (Integer) doc.getFieldValue("count");
				int userCount = 0;
				float score = (float) doc.getFieldValue("score");
                                List<String> x = (List<String>) doc.getFieldValue("user_count");
				for(String user: x){
					String[] buffer = user.split(":");
					if(buffer[0].equals(_user_id)){
						userCount = Integer.parseInt(buffer[1]);
						break;
					}
				}
				
				score += count * 0.1;
				score += userCount * 0.25;
				
				scoreMap.put((String)doc.getFieldValue("news_id"), score);
			}
			
			ValueBasedSort cmp = new ValueBasedSort(scoreMap);
			TreeMap<String,Float> sortedMap = new TreeMap<String,Float>(cmp);
			sortedMap.putAll(scoreMap);
			
			for(String newsID: scoreMap.keySet()){
				for (SolrDocument doc : docs) {	
					if(newsID.equals(doc.getFieldValue("news_id"))){
						
                /**
                 * *********Highlight/Snippet************
                 */
                News model = null;

                String news_id = (String) doc.getFieldValue("news_id");
                if (news_id != null && response.getHighlighting().get(news_id) != null) {
                    model = (News) getModelForSolrDoc(doc, resultType, response.getHighlighting().get(news_id));
                } else {
                    model = (News) getModelForSolrDoc(doc, resultType);
                }

                out.add(model);
                docCount++;
						break;
            }
				}
			}
            //SERVER.add(updateDocList);
            //SERVER.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("docCount : " + docCount);
        if (docCount < 5) {
            queryBack = true;
        }

        return queryBack;
    }

    private List<String> spellcheck(QueryResponse resp) {
        List<String> out = null;
        try {
            SpellCheckResponse spellResp = resp.getSpellCheckResponse();

            if (!spellResp.isCorrectlySpelled()) {
                for (Suggestion suggest : resp.getSpellCheckResponse().getSuggestions()) {
                    System.out.println(suggest.getAlternatives());
                    out = suggest.getAlternatives();
                    if (out != null) {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private static AbstractModel getModelForSolrDoc(SolrDocument solrDoc, Class<? extends AbstractModel> _modelType) {
        AbstractModel out = null;

        try {
            //System.out.println("Packing Bean for Class : " + _modelType.getName());

            out = DataProcess.getModelForClass(_modelType);

            Method[] _declaredMethods = _modelType.getDeclaredMethods();
            ArrayList<Method> _setterMethods = getSetterMethods(_declaredMethods);

            for (Method m : _setterMethods) {
                String fieldName = m.getName().toLowerCase().replaceFirst("set", "");
                m.invoke(out, solrDoc.getFieldValue(fieldName));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;

    }

    private static AbstractModel getModelForSolrDoc(SolrDocument solrDoc, Class<? extends AbstractModel> _modelType, Map<String, List<String>> highlighting) {
        News out = null;

        try {
            System.out.println("Packing Bean for Class : " + _modelType.getName());

            out = (News) DataProcess.getModelForClass(_modelType);

            Method[] _declaredMethods = _modelType.getDeclaredMethods();
            ArrayList<Method> _setterMethods = getSetterMethods(_declaredMethods);

            for (Method m : _setterMethods) {
                String fieldName = m.getName().toLowerCase().replaceFirst("set", "");
                m.invoke(out, solrDoc.getFieldValue(fieldName));
                //System.out.println("*******im here************");
                if (highlighting.get(fieldName) != null) {
                    String flattenedSnippet = "";
                    List<String> snippets = highlighting.get(fieldName);
                    if (fieldName.equalsIgnoreCase("content")) {
                        for (String snip : snippets) {
                            flattenedSnippet += "...";
                            flattenedSnippet += snip;
                            flattenedSnippet += "...<br/>";
                        }
                    } else {
                        for (String snip : snippets) {
                            flattenedSnippet += snip + "&nbsp;";
                        }
                    }

                    m.invoke(out, flattenedSnippet);
                    //System.out.println("******************* high lighting *********************");
                    //System.out.println(highlighting.get(fieldName));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;

    }

    private static SolrInputDocument getSolrDocForModel(AbstractModel _in) {
        SolrInputDocument out = new SolrInputDocument();

        try {
            Class<?> _class = _in.getClass();

            System.out.println("Unpacking Bean of Class : " + _class.getName());

            Method[] _declaredMethods = _class.getDeclaredMethods();
            ArrayList<Method> _getterMethods = getGetterMethods(_declaredMethods);

            for (Method m : _getterMethods) {
                Object returnVal = m.invoke(_in);
                String fieldName = m.getName().toLowerCase().replaceFirst("get", "");

                System.out.println(m.getName() + "\t:\t" + returnVal);
                out.addField(fieldName, returnVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;

    }

    private static ArrayList<Method> getGetterMethods(Method[] _methods) {
        return getFilteredMethods(_methods, "get");
    }

    private static ArrayList<Method> getSetterMethods(Method[] _methods) {
        return getFilteredMethods(_methods, "set");
    }

    private static ArrayList<Method> getFilteredMethods(Method[] _methods, String filterType) {
        ArrayList<Method> out = new ArrayList<Method>();

        for (Method m : _methods) {
            if (m.getName().startsWith(filterType)) {
                out.add(m);
            }
        }

        return out;
    }

    public static void main(String[] args) {
        Solr solr = new Solr();

        /**
         * ************* News-1 *********************
         */
        News news_1 = new News();

        news_1.setNews_id("SAN_01");

        ArrayList<String> user_id_1 = new ArrayList<String>();
        user_id_1.add("harish.v");
        news_1.setUser_id(user_id_1);

        ArrayList<String> user_count_1 = new ArrayList<String>();
        user_count_1.add("harish.v:1");
        news_1.setUser_count(user_count_1);

        news_1.setUrl("www.sankar.com");

        ArrayList<String> authors_1 = new ArrayList<String>();
        authors_1.add("Kurshid Metha");
        news_1.setAuthors(authors_1);

        news_1.setPlace("Buffalo NY");

        news_1.setLat(10.1f);

        news_1.setLon(86.1f);

        news_1.setTitle("News Title");

        news_1.setPublished_date("Mar 12 2013");

        ArrayList<String> category_1 = new ArrayList<String>();
        category_1.add("Travel");
        category_1.add("Sports");
        news_1.setCategory(category_1);

        ArrayList<String> sub_category_1 = new ArrayList<String>();
        sub_category_1.add("U.S.");
        sub_category_1.add("Tennis");
        news_1.setSub_category(sub_category_1);

        ArrayList<String> tags_descriptors_1 = new ArrayList<String>();
        tags_descriptors_1.add("XXX");
        tags_descriptors_1.add("YYY");
        news_1.setTags_descriptors(tags_descriptors_1);

        ArrayList<String> tags_locations_1 = new ArrayList<String>();
        tags_locations_1.add("London");
        tags_locations_1.add("NY");
        news_1.setTags_locations(tags_locations_1);

        ArrayList<String> tags_people_1 = new ArrayList<String>();
        tags_people_1.add("Gandhi");
        tags_people_1.add("Nehru");
        news_1.setTags_people(tags_people_1);

        ArrayList<String> tags_orgs_1 = new ArrayList<String>();
        tags_orgs_1.add("TCS");
        tags_orgs_1.add("Pfizer");
        news_1.setTags_orgs(tags_orgs_1);

        news_1.setContent("News Content");

        news_1.setPublisher("New York Times");

        news_1.setCount(1);

        System.out.println(news_1);

        /**
         * ************* News-2 *********************
         */
        News news_2 = new News();

        news_2.setNews_id("SAN_02");

        ArrayList<String> user_id_2 = new ArrayList<String>();
        user_id_2.add("harish.v");
        news_2.setUser_id(user_id_2);

        ArrayList<String> user_count_2 = new ArrayList<String>();
        user_count_2.add("harish.v:1");
        news_2.setUser_count(user_count_2);

        news_2.setUrl("www.sankar.com");

        ArrayList<String> authors_2 = new ArrayList<String>();
        authors_2.add("Kurshid Metha");
        news_2.setAuthors(authors_2);

        news_2.setPlace("Buffalo NY");

        news_2.setLat(10.1f);

        news_2.setLon(86.1f);

        news_2.setTitle("News Title");

        news_2.setPublished_date("Mar 12 2013");

        ArrayList<String> category_2 = new ArrayList<String>();
        category_2.add("Travel");
        category_2.add("Sports");
        news_2.setCategory(category_2);

        ArrayList<String> sub_category_2 = new ArrayList<String>();
        sub_category_2.add("U.S.");
        sub_category_2.add("Tennis");
        news_2.setSub_category(sub_category_2);

        ArrayList<String> tags_descriptors_2 = new ArrayList<String>();
        tags_descriptors_2.add("XXX");
        tags_descriptors_2.add("YYY");
        news_2.setTags_descriptors(tags_descriptors_2);

        ArrayList<String> tags_locations_2 = new ArrayList<String>();
        tags_locations_2.add("London");
        tags_locations_2.add("NY");
        news_2.setTags_locations(tags_locations_2);

        ArrayList<String> tags_people_2 = new ArrayList<String>();
        tags_people_2.add("Gandhi");
        tags_people_2.add("Nehru");
        news_2.setTags_people(tags_people_2);

        ArrayList<String> tags_orgs_2 = new ArrayList<String>();
        tags_orgs_2.add("TCS");
        tags_orgs_2.add("Pfizer");
        news_2.setTags_orgs(tags_orgs_2);

        news_2.setContent("News Content Sankaravadivel Dhandapani");

        news_2.setPublisher("New York Times");

        news_2.setCount(1);

        System.out.println(news_2);

        //System.out.println(news_1.equals(news_2));
        /**
         * ******************** End of news Test Model data *****************
         */
        //solr.addDoc(news_2);
        /*
         NewsResults docs = solr.query("vehic", News.class, "ad.ada");

         System.out.println("************* Results from Solr *************");
         for (News doc : docs.getNews_results()) {
         System.out.println("\n************* Document START *************\n");
         System.out.println(doc);
         System.out.println("\n************* Document END *************\n");
         //System.out.println("Is Match???? " + news_1.equals(doc));
         }
        
         System.out.println("******* Suggestions *******");
         System.out.println(docs.getSuggestions());
         */
        //SolrInputDocument solr_doc = getSolrDocForModel(news_1);
        System.out.println("******** AUTO SUGGEST ************");
        System.out.println(solr.getAutoSuggest("buf"));
    }

}

//Source: http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
class ValueBasedSort implements Comparator<String>{
	Map<String, Float> map;
	
	ValueBasedSort(Map<String,Float> map){
		this.map = map;
	}
	
	public int compare(String set1, String set2){
		if(map.get(set1)>= map.get(set2))
			return -1;
		else
			return 1;
	}
}
