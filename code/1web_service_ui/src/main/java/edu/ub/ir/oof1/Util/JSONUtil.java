/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ub.ir.oof1.model.AbstractModel;
import edu.ub.ir.oof1.model.NewsResults;
import java.util.List;

/**
 *
 * @author san
 */
public class JSONUtil {
    
    public static String modelsToJsonArray(List<AbstractModel> _models){
        String out = "";
        
        try {
            ObjectMapper mapper = new ObjectMapper(); 
            out = mapper.writeValueAsString(_models);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return out;
    }
    
    public static String newsResultsToJson(NewsResults _news_results){
        String out = "";
        
        try {
            ObjectMapper mapper = new ObjectMapper(); 
            out = mapper.writeValueAsString(_news_results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return out;
    }
    
    public static String modelToJsonObj(AbstractModel _model){
        String out = "";
        
        try {
            ObjectMapper mapper = new ObjectMapper(); 
            out = mapper.writeValueAsString(_model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return out;
    }
    
}
