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
public class NewsResults extends AbstractModel implements Serializable{
    private List<News> news_results;
    private List<String> suggestions;

    public NewsResults() {
    }

    public NewsResults(List<News> news_results, List<String> suggestions) {
        this.news_results = news_results;
        this.suggestions = suggestions;
    }

    public List<News> getNews_results() {
        return news_results;
    }

    public void setNews_results(List<News> news_results) {
        this.news_results = news_results;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    
    
    
}
