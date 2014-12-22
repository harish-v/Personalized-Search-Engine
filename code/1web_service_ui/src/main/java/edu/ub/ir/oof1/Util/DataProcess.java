/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.Util;

import edu.ub.ir.oof1.model.AbstractModel;
import edu.ub.ir.oof1.model.News;

/**
 *
 * @author san
 */
public class DataProcess {
    
    public static AbstractModel getModelForClass(Class<? extends AbstractModel> _type) throws Exception{
        AbstractModel out = null;
        
        switch(_type.getName()){
            case "edu.ub.ir.oof1.model.News":
                out = new edu.ub.ir.oof1.model.News();
        }
        
        return out;
    }
    
}
