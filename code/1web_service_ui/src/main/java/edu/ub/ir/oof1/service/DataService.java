/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.service;

import edu.ub.ir.oof1.model.AbstractModel;
import edu.ub.ir.oof1.model.NewsResults;
import edu.ub.ir.oof1.model.Suggestions;
import java.util.List;

/**
 *
 * @author san
 */
public abstract class DataService {
 
    public abstract void addDoc(AbstractModel _inDoc);
    public abstract NewsResults query(String _query, Class<? extends AbstractModel> resultType, String _user_id);
    public abstract List<AbstractModel> query(String _query, Class<? extends AbstractModel> resultType);
    public abstract void updateDocClick(String _user_id, String _news_id);
    public abstract Suggestions getAutoSuggest(String _query);
}
