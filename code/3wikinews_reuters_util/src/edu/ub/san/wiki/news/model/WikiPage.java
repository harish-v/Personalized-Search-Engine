/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.san.wiki.news.model;

/**
 *
 * @author san
 */
public class WikiPage {
    
    private String id;
    private String title;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WikiPage{\n\t" + "id=" + id + ",\n\ttitle=\"" + title + "\",\n\tcontent=\"" + content + "\"\n\t}\n\n";
    }
    
    
    
}
