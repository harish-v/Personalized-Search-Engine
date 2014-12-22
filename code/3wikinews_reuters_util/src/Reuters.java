import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CommonParams;

public class Reuters {
        //args[0] --> "C:\\Users\\harish.v\\Downloads\\enwikinews-20140308-pages-articles-multistream.xml\\enwikinews-20140308-pages-articles-multistream.xml"
        //args[1] --> "http://192.168.0.6:8983/solr"
	public static void main(String args[]){
		String url = args[1];
		SolrServer server = new HttpSolrServer(url);
		String[] dirs = {"retail", "trade", "gas"};
				
		for(String foo: dirs) {
			try {
				File dir = new File(args[0] + File.separator + foo);
				String[] files = dir.list();
				
				for(String f: files){
					SolrInputDocument doc = new SolrInputDocument();
					parse(dir.getAbsolutePath() + File.separator + f ,doc);
					server.add(doc);
					server.commit();
					
					System.out.println(dir.getAbsolutePath() + File.separator + f);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}	    
		
//		query(server);
	}
	
	public static enum Tags {

        TITLE, AUTHOR, PLACEDATE, CONTENT
    }
	
	public static void parse(String filename, SolrInputDocument doc){
		
		ArrayList<StringBuilder> contentList = new ArrayList<StringBuilder>();
        BufferedReader buf = null;

        try {

 //           if (filename != null && !filename.isEmpty()) {
            try {

                buf = new BufferedReader(new FileReader(filename));
                String line = null;

                while ((line = buf.readLine()) != null) {
                	contentList.add(new StringBuilder(line));
                }
            } finally {
                try {
                    if (buf != null) {
                        buf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }

            Tags tag = Tags.TITLE;
            boolean titleStarted = false;
            StringBuilder title = new StringBuilder();
            String[][] authors = new String[2][];
            String place = "";
            String date = "";
            StringBuilder content = new StringBuilder("");
            
            for (int index = 0, size = contentList.size(); index < size; index++) {
                switch (tag) {
                    //Extract Until Title is complete
                    case TITLE:
                        if (!titleStarted && contentList.get(index).toString().equals("")) {
                            continue;
                        } else if (contentList.get(index).toString().equals("")) {
                            tag = Tags.AUTHOR;
                        } else {
                            titleStarted = true;
                            title.append(contentList.get(index));
                        }
                        break;
                    //Extract Author
                    case AUTHOR:
                        if (StringManipulator.isAuthorTag(contentList.get(index))) {
                            //It is an author tag. So extracting it
                            authors = StringManipulator.parseAuthorTag(contentList.get(index));
                            tag = Tags.PLACEDATE;
                            break;
                        }
                    //Extract Place
                    case PLACEDATE:
                        place = placeCleanUp(StringManipulator.regexStringReturn(contentList.get(index), ParserRegEx.PLACE, 2));
                        date = StringManipulator.safeTrim( StringManipulator.regexStringReturn(contentList.get(index), ParserRegEx.DATE, 1) );
                        content = new StringBuilder(StringManipulator.regexStringReturn(contentList.get(index), ParserRegEx.REMAINING_TEXT_FIRSTLINE, 1));
                        tag = Tags.CONTENT;
                        break;
                    //Extract remaining content
                    case CONTENT:
                        content.append(" " + contentList.get(index));
                        break;
                    default:
                        throw new IllegalStateException();
                }
            }

            String[] token = filename.split(Pattern.quote(File.separator));
            doc.addField("news_id", "RT_" + token[token.length - 1]);
            doc.addField("user_id", "");
            doc.addField("user_count", "");
            doc.addField("category", token[token.length - 2]);
            doc.addField("title", title.toString());
            doc.addField("authors", authors[1]);
            doc.addField("place", place);
            doc.addField("published_date", date);
            doc.addField("content", content.toString());
            doc.addField("publisher", "reuters");
            doc.setField("count", 0);

        } catch (Exception e) {
            e.printStackTrace();
        }		
	}
	
	private static String placeCleanUp(String in) {
        String out = null;

        if (in != null) {
            if (!in.isEmpty()) {
                in = in.trim();
                char lastChar = in.charAt(in.length() - 1);
                if (lastChar == ',') {
                    out = in.substring(0, in.length() - 1);
                } else {
                    out = in;
                }
            } else {
                out = in;
            }

        }

        return out;
    }
}

class StringManipulator {

    private static Pattern p_anyLowerCase = Pattern.compile("[a-z]");
    private static Matcher m_anyLowerCase = p_anyLowerCase.matcher(" ");
    
    private static Pattern p_camelCase = Pattern.compile("^[A-Z][a-z][a-z0-9[\\p{Punct}]]*$");
    private static Matcher m_camelCase = p_camelCase.matcher(" ");
    
    /**
     * This method will return the matching string say "Author", "Place", Date
     * from the input StringBuilder It will also remove the found matching
     * referenced in the StringBuilder
     * @param input
     * @param regexPatFind
     * @param groupid
     * @return
     */
    public static String regexStringReturn(StringBuilder input, Pattern regexPatFind, int groupid) {
        Matcher matcher;
        String output = "";

        if (input != null && input.length() > 0) {

            matcher = regexPatFind.matcher(input);
            if (matcher.find()) {
                //This line pastes the matching pattern content to the output String
                output = matcher.group(groupid);
            }
        }

        return output;
    }

    public static boolean isEmptyLine(StringBuilder input, Pattern regexPatFind) {
        boolean output = false;

        output = regexPatFind.matcher(input).matches();

        return output;
    }

    /**
     * out[1] --> Has String[1]:      is the Org name - if present, 
     *                                if none - then has empty string.
     * out[2] --> Has String[1 to 2]: is/are the Author name(s) - if present, 
     *                                if none - then returns null 
     *                                if using - always check for null.
     * @param authorTagLine
     * @return 
     */
    public static String[][] parseAuthorTag(StringBuilder inAuthorTagLine) {
        String[][] out = new String[2][];
        String authorTagLine = ""; 
        //Author tag is cleaned and inner content assigned ignoring tag and by
        authorTagLine = regexStringReturn(inAuthorTagLine, ParserRegEx.AUTHOR_TAG_CONTENT, 4);

        int indexOfCommaB4Org = authorTagLine.lastIndexOf(',');
        int lengthOfContent = authorTagLine.length();
        String [] org = {""};
        
        //Sets Org if present
        if (indexOfCommaB4Org > 0) {
            //Org is present is extracted
            org[0] = safeTrim(authorTagLine.substring(indexOfCommaB4Org + 1, lengthOfContent));
            out[0] = org;
            //Org if present is cleaned from the text
            authorTagLine = authorTagLine.substring(0, indexOfCommaB4Org);
        }

        //Sets Author
        out[1] = safeArrayTrim(authorTagLine.split("(?i)(\\s+and\\s+)"));
        return out;
    }
    
    public static boolean isAuthorTag(StringBuilder inAuthorTagLine) {
        boolean out = false;
    	Matcher matcher;
        
        if (inAuthorTagLine != null && inAuthorTagLine.length() > 0) {
            matcher = ParserRegEx.IS_AUTHOR_TAG.matcher(inAuthorTagLine);
            out = matcher.matches();
        }

        return out;
    }

    //Null and Empty String safe Trim
    public static String safeTrim(String in) {
        String out = null;
        
        if(in != null && !in.isEmpty()){
            out = in.trim();
        } else {
            out = in;
        }
        
        return out;
    }

    //Null and Empty String safe Trim
    public static String[] safeArrayTrim(String [] in) {
        String [] out = null;
        
        if(in != null){
            out = new String [in.length];
            for(int i = 0, size = in.length; i < size; i++){
                out[i] = safeTrim(in[i]);
            }
        }
        
        return out;
    }
    
    public static boolean isAllUpperCase_safe(String s){
        boolean out = true;
        if(s == null){
            return false;
        }
        
        m_anyLowerCase.reset(s);
        if(m_anyLowerCase.find()){
            return false;
        }
        return out;
    }
    
    public static boolean isCamelCase_safe(String s){
        boolean out = false;
        if(s != null && !s.isEmpty()){
            m_camelCase.reset(s);
            if(m_camelCase.matches()){
                return true;
            }
        }
        return out;
    }
    
    //Padded with '|'
    public static String getIntCodeForString(String s){
        
        if(s.isEmpty())
            return "|00000|";
        
        StringBuilder sb = new StringBuilder();
        char[] sCh = s.toCharArray();
        for(char t : sCh){
            int val = (int) t;
            sb.append(val).append('|');
        }
        return "|" + sb.toString() + "|";
    }
}

class ParserRegEx {
    
    /**
     * Assumption: 
     * ===========
     * STARTS with Line Start ^ (to the beginning)
     * Followed by - 0 or more space (or) tab (or) any white space character
     * Followed by - First three characters of date spelled correct Case Insensitive
     * Followed by - 0 to 6 alphabets. (As September being the longest with 9 chars and first 3 char is taken care above
     * Followed by - 1 or more space (or) tab (or) any white space character
     * Followed by - 1 to 2    digits to represent date.
     * Followed by - 0 or more space (or) tab (or) any white space character
     * ENDS with 1 dash "-" (to the end)
     * Pass Cases :: ", Jan  02", ",Febru 27-", ",   AprIL     2    -"
     * Return     :: Group 2
     */        
    public static final Pattern DATE = Pattern.compile(",(\\s*((?i)(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec){1}[a-zA-Z]{0,6}\\s+\\d{1,2}))\\s*-");
    
    
    /**
     * Assumption: 
     * ===========
     * STARTS with Line Start ^ (to the beginning)
     * Followed by - 0 or more space (or) tab (or) any white space character
     * Followed by - 1 or more characters (a-zA-Z) (0-1) (Special Chars) (White Space) Excluding comma
     * Followed by - 1 comma
     * Followed by - 0 or 1 combination of (Followed by - 0 or more characters (a-zA-Z) (0-1) (Special Chars) (White Space) Excluding comma AND Followed by - 1 comma)
     * Followed by - 1 or more space (or) tab (or) any white space character)
     * Followed by - 0 or more any characters
     * ENDS with dash (-) (to the end)
     * Pass Cases :: "     COMMACK, N.Y., Feb 21 - ", "N.Y.C., CT., -", "     New Jersey, - "
     * Return     :: Group 2
     */       
//    public static final Pattern PLACE = Pattern.compile("^(\\s*((.[^,])+,{1}((.[^,])*,{1})?)\\s+).*\\d{1,2}\\s*-");
    public static final Pattern PLACE = Pattern.compile("^(\\s*((.[^,])+,{1}((.[^,])*,{1})?)\\s+).*-");
    
    
    public static final Pattern EMPTYLINE = Pattern.compile("^\\s*$");
    
    /**
     * Assumption: 
     * ===========
     * STARTS with dash (-)
     * Followed by - 0 or more characters (Can be any char including white space and alphanumeric and special char)
     * ENDS Line ending
     * Pass Cases :: " -   asdasd"
     * Return     :: Group 1
     */   
    public static final Pattern REMAINING_TEXT_FIRSTLINE = Pattern.compile("-(.*)$");
    
    
    //Regex for extracting Author tag context after removing the tag and by
    //Return     :: Group 4
    public static final Pattern AUTHOR_TAG_CONTENT = Pattern.compile("^(?i)(\\s*(<AUTHOR>){1}\\s*(by){1}\\s*(.*)\\s*(</AUTHOR>){1}\\s*)$");
    
    public static final Pattern IS_AUTHOR_TAG = Pattern.compile("^(?i)(\\s*(<AUTHOR>){1}(.*)(</AUTHOR>){1}\\s*)$");
}
