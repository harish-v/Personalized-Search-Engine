/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author san
 */
public class Suggestions extends AbstractModel implements Serializable{
    private List<String> suggestions = new ArrayList<String>();

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public String toString() {
        return "Suggestions{" + "suggestions=" + suggestions + '}';
    }
    
    
    
}
