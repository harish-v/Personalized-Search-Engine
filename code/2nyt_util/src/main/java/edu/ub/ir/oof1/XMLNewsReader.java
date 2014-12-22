package edu.ub.ir.oof1;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import edu.ub.ir.oof1.model.News;

/**
 *
 * @author kaush
 */
public class XMLNewsReader {

        static edu.ub.ir.oof1.service.Solr solr = new edu.ub.ir.oof1.service.Solr();
    
	//	private static Set<String> category = new HashSet<String>() ;
	//	private static HashMap<String,Integer> cat = new HashMap<String, Integer>() ;

	public static void readNYTNewsXML(String path) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builderFactory.setValidating(false);
			builderFactory.setNamespaceAware(true);
			builderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
			builderFactory.setFeature("http://xml.org/sax/features/validation", false);
			builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

			builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(new File(path));
			XPath xPath =  XPathFactory.newInstance().newXPath();
			//System.out.println("Is Validating : " + builderFactory.isValidating());

			HashMap<String,String> tags = new HashMap<String,String>();
			tags.put("newsID","/nitf/head/docdata/doc-id/@id-string");
			tags.put("url_alternate","/nitf/head/meta[@name='alternate_url']/@content");
			tags.put("url_ref","/nitf/head/pubdata/@ex-ref");
			tags.put("authors","/nitf/body/body.head/byline[@class='normalized_byline']");
			tags.put("newsLocation","/nitf/body/body.head/dateline");
			tags.put("title","/nitf/body[1]/body.head/hedline/hl1");
			tags.put("publishedDate","/nitf/head/pubdata/@date.publication");
			tags.put("classification","/nitf/head/meta[@name='online_sections']/@content");
			tags.put("tagsDescriptors","/nitf/head/docdata/identified-content/classifier[@class='indexing_service' and @type='descriptor']");
			tags.put("tagsDescriptors_01","/nitf/head/docdata/identified-content/classifier[@class='online_producer' and @type='general_descriptor']");
			tags.put("tagsDescriptors_02","/nitf/head/docdata/identified-content/classifier[@class='online_producer' and @type='descriptor']");
			tags.put("tagsLocations","/nitf/head/docdata/identified-content/location[@class='indexing_service']");
			tags.put("tagsPeople","/nitf/head/docdata/identified-content/person[@class='indexing_service']");
			tags.put("tagsOrganization","/nitf/head/docdata/identified-content/org[@class='online_producer']");
			tags.put("tagsNewsType","/nitf/head/docdata/identified-content/classifier[@class='online_producer' and @type='types_of_material']");
			tags.put("publisher","/nitf/head/docdata/doc.copyright/@holder");
			tags.put("content","/nitf/body/body.content/block[@class='full_text']");
			tags.put("category","/nitf/head/docdata/identified-content/classifier[@class='online_producer' and @type='taxonomic_classifier']");

			String val;
			Set<String> set, set1;
			ArrayList<String> list ;
			NodeList nodeList;
			News ny = new News();
                        ArrayList<String> user_id = new ArrayList<String>();
                        user_id.add("");
                        ny.setUser_id(user_id);
                        ny.setUser_count(user_id);
			//News ID
			val = xPath.compile(tags.get("newsID")).evaluate(xmlDocument);
			ny.setNews_id("NYT_" + val.trim());

			//URL
			val = xPath.compile(tags.get("url_alternate")).evaluate(xmlDocument);
			if(null == val)
				val = xPath.compile(tags.get("url_ref")).evaluate(xmlDocument);
			ny.setUrl(val.trim());

			//Authors
			val = xPath.compile(tags.get("authors")).evaluate(xmlDocument);
			String[] v = val.split(";");
			list = new ArrayList<String>();
			Collections.addAll(list, v);
			ny.setAuthors(list);

			//News Location
			val = xPath.compile(tags.get("newsLocation")).evaluate(xmlDocument);
			if (val.matches(".*\\d$"))
			{
				int pos = val.lastIndexOf(',');
				val = val.substring(0, pos);
			}
			ny.setPlace(val.trim());

			//Title
			val = xPath.compile(tags.get("title")).evaluate(xmlDocument);
			ny.setTitle(val.trim());

			//Published Date
			val = xPath.compile(tags.get("publishedDate")).evaluate(xmlDocument);
			ny.setPublished_date(val.substring(0, 8));

			//Category
			set = new HashSet<String>();
			set1 = new HashSet<String>();
			nodeList = (NodeList) xPath.compile(tags.get("category")).evaluate(xmlDocument, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				val = nodeList.item(i).getFirstChild().getNodeValue();
				String[] va = val.split("/");
				if(va.length > 1)
					set.add( va[1].trim() );
				if(va.length > 2)
					set1.add( va[2].trim() );
			}
			list = new ArrayList<String>(set);
			ny.setCategory(list);
			list = new ArrayList<String>(set1);
			ny.setSub_category(list);

			//**Tags**

			//Tags_Descriptors
			set = new HashSet<String>();
			nodeList = (NodeList) xPath.compile(tags.get("tagsDescriptors")).evaluate(xmlDocument, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				val = nodeList.item(i).getFirstChild().getNodeValue();
				set.add(val.trim());
			}

			nodeList = (NodeList) xPath.compile(tags.get("tagsDescriptors_01")).evaluate(xmlDocument, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				val = nodeList.item(i).getFirstChild().getNodeValue();
				set.add(val.trim());
			}

			nodeList = (NodeList) xPath.compile(tags.get("tagsDescriptors_02")).evaluate(xmlDocument, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				val = nodeList.item(i).getFirstChild().getNodeValue();
				set.add(val.trim());
			}
			list = new ArrayList<String>(set);
			ny.setTags_descriptors(list);

			//Tags_Locations
			set = new HashSet<String>();
			nodeList = (NodeList) xPath.compile(tags.get("tagsLocations")).evaluate(xmlDocument, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				val = nodeList.item(i).getFirstChild().getNodeValue();
				set.add(val.trim());
			}
			list = new ArrayList<String>(set);
			ny.setTags_locations(list);

			//Tags_People
			set = new HashSet<String>();
			nodeList = (NodeList) xPath.compile(tags.get("tagsPeople")).evaluate(xmlDocument, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				val = nodeList.item(i).getFirstChild().getNodeValue();
				set.add(val.trim());
			}
			list = new ArrayList<String>(set);
			ny.setTags_people(list);

			//Tags_Organization
			set = new HashSet<String>();
			nodeList = (NodeList) xPath.compile(tags.get("tagsOrganization")).evaluate(xmlDocument, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				val = nodeList.item(i).getFirstChild().getNodeValue();
				set.add(val.trim());
			}
			list = new ArrayList<String>(set);
			ny.setTags_locations(list);

			//Publisher
			val = xPath.compile(tags.get("publisher")).evaluate(xmlDocument);
			ny.setPublisher(val.trim());

			//Content
			val = xPath.compile(tags.get("content")).evaluate(xmlDocument);
			ny.setContent(val.trim());


			//			System.out.println("\ncat    : "+ny.getCategory());
			//			System.out.println("subcat : "+ny.getSubCategory());

			//			nodeList = (NodeList) xPath.compile(tags.get("category")).evaluate(xmlDocument, XPathConstants.NODESET);
			//			for (int i = 0; i < nodeList.getLength(); i++) {
			//				val = nodeList.item(i).getFirstChild().getNodeValue();
			//				category.add(val.trim());
			//
			//				if(null == cat.get(val))
			//					cat.put(val, 1);
			//				else
			//					cat.put(val, cat.get(val)+1);
			//			}

			//System.out.println( ny.toString());
			//System.out.println( ny.getNewsID() + " : " + ny.getNewsID() );
			//System.out.println( ny.getNewsID() + " : " + ny.getUrl() );
			//System.out.println( ny.getNewsID() + " : " + ny.getAuthors() );
			//System.out.println( ny.getNewsID() + " : " + ny.getNewsLocation() );
			//System.out.println( ny.getNewsID() + " : " + ny.getTitle() );
			//System.out.println( ny.getNewsID() + " : " + ny.getPublishedDate() );
			//System.out.println( ny.getNewsID() + " : " + ny.getClassification() );
			//System.out.println( ny.getNewsID() + " Des : " + ny.getTagsDescriptors() );
			//System.out.println( ny.getNewsID() + " Loc : " + ny.getTagsLocations() );
			//System.out.println( ny.getNewsID() + " Peo : " + ny.getTagsPeople() );
			//System.out.println( ny.getNewsID() + " Org : " + ny.getTagsOrganization() );
			//System.out.println( ny.getNewsID() + " Tag : " + ny.getTagsNewsType() );
			//System.out.println( ny.getNewsID() + " : " + ny.getPublisher() );
			//System.out.println( ny.getNewsID() + " : " + ny.getContent() );
			//if("" != ny.getNewsLocation() )

                        
                        solr.addDoc(ny);
                        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {

		String corpusMainDir = args[0] + "/0"; //"/Users/san/UB/IR/Projects/Project-3/Data_Dump/NYT";
		Date startTime = new Date();

		for(int i=1 ; i<=6 ; i++) {
			String corpusDir = corpusMainDir + i; 
			System.out.println("corpusDir = "+ corpusDir);
			File ipDirectory = new File(corpusDir);
			String[] catDirectories = ipDirectory.list();
			String[] files;
			File dir;

			try {
				for (String cat : catDirectories) {
					dir = new File(corpusDir + File.separator + cat);
					files = dir.list();
					if (files == null)
						continue;
					for (String f : files) {
						try {
							String filePath = dir.getAbsolutePath() + File.separator + f;
							XMLNewsReader.readNYTNewsXML(filePath);
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//		System.out.println(" Category ");
		//		TreeMap<String, Integer> Tcat = (TreeMap<String, Integer>) Util.sort(cat);
		//		for(Entry<String, Integer> e : Tcat.entrySet())
		//		{
		//			System.out.println(e.getKey() +" : "+ e.getValue());
		//		}

		Date endTime = new Date();
		System.out.println(startTime);
		System.out.println(endTime);
	}	

}
