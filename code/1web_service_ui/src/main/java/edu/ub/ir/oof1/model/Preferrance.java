/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.model;

import java.io.Serializable;
import java.util.HashSet;

/**
 *
 * @author san
 */
public class Preferrance extends AbstractModel implements Serializable{
    
    private HashSet<String> categories = new HashSet<String>();
    private HashSet<String> sub_categories = new HashSet<String>();

    public HashSet<String> getCategories() {
        return categories;
    }

    public HashSet<String> getSub_categories() {
        return sub_categories;
    }

    public void addCategory(String category){
        categories.add(category);
    }
    
    public void addSub_Category(String sub_category){
        sub_categories.add(sub_category);
    }
}
