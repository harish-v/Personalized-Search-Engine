/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.san.wiki.news.parser;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiPageIterator;
import edu.jhu.nlp.wikipedia.WikiXMLDOMParser;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author san
 */
public class WikiNewsParser {
    
    //args[0] --> "C:\\Users\\harish.v\\Downloads\\enwikinews-20140308-pages-articles-multistream.xml\\enwikinews-20140308-pages-articles-multistream.xml"
    //args[1] --> "http://192.168.0.6:8983/solr"
    public static void main(String[] args) {
        
//        ArrayList<edu.ub.san.wiki.news.model.WikiPage> wikiDumpCol = new ArrayList<edu.ub.san.wiki.news.model.WikiPage>();
        WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(args[0]); //"C:\\Users\\harish.v\\Downloads\\enwikinews-20140308-pages-articles-multistream.xml\\enwikinews-20140308-pages-articles-multistream.xml"
                
        try {
                  
            wxsp.setPageCallback(new PageCallbackHandler() { 
                           public void process(WikiPage srcPage) {
                        	   SolrServer server = new HttpSolrServer(args[1]);
                        	   SolrInputDocument doc = new SolrInputDocument();
                        	   
                        	   try {
	                               edu.ub.san.wiki.news.model.WikiPage toPg = new edu.ub.san.wiki.news.model.WikiPage();
		                            doc.addField("news_id","WIKI_" + srcPage.getID().trim());
		                            doc.addField("title", srcPage.getTitle().trim().replaceAll("\n", "").replaceAll("\\W+", " "));
		                            doc.addField("content", srcPage.getText().trim().replaceAll("\n", "<br />").replaceAll("\\W+", " "));
		                            
		                            List<String> categoryList = new ArrayList<String>();
		                            for(String category: srcPage.getCategories()) {
		                            	categoryList.add(category.trim().replaceAll("\n", "").replaceAll("\\W+", " "));
		                            }
		                            doc.addField("category", categoryList);
		                            doc.addField("count", 0);
		                            doc.addField("user_id", "");
		                            doc.addField("user_count", "");
		                            doc.addField("publisher", "WIKI NEWS");
		                            
		                            server.add(doc);
		                            server.commit();
                        	   } catch (Exception e){
                        		   e.printStackTrace();
                        	   }
//                                wikiDumpCol.add(toPg);
//                                System.out.println("Processed News :: \"" + toPg.getTitle() + "\"");
                                
                           }
            });
                
           wxsp.parse();
           
        }catch(Exception e) {
                e.printStackTrace();
        }
        
//        for(edu.ub.san.wiki.news.model.WikiPage pg: wikiDumpCol){
//                System.out.println(pg);
//                System.out.println("*******************************************************************************************");
//                System.out.println("\n\n");
//            }
        
    }
    
}
