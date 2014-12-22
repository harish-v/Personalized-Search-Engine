/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.service;

import edu.ub.ir.oof1.Util.JSONUtil;
import edu.ub.ir.oof1.model.AbstractModel;
import edu.ub.ir.oof1.model.News;
import edu.ub.ir.oof1.model.NewsResults;
import edu.ub.ir.oof1.model.Suggestions;
import java.util.List;

/**
 *
 * @author san
 */
public class NewsService {

    DataService ds = new Solr(); 
    
    public NewsResults query(String _query, String _user_id) {
        return ds.query(_query, News.class, _user_id);
    }

    public List<AbstractModel> query(String _query) {
        return ds.query(_query, News.class);
    }
    
    public String getNewsArrayForUI_InJSON(String _query, String _user_id) {
        return JSONUtil.newsResultsToJson(query(_query, _user_id));
    }
    
    public String getNewsObjectForUI_InJSON(String _query) {
        String out = "{}";
        
        List<AbstractModel> results = query(_query);
        if(results!= null && results.size() > 0){
            out = JSONUtil.modelToJsonObj(results.get(0));
        }
        
        return out;
    }
    
    public void updateDocClickForUser(String _user_id, String _news_id){
        ds.updateDocClick(_user_id, _news_id);
    }
    
    public Suggestions getAutoSuggestions(String _query){
        return ds.getAutoSuggest(_query);
    }
}
