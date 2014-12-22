/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author san
 */
public class News extends AbstractModel implements Serializable{
    
    private String news_id = "";
    private ArrayList<String> user_id = new ArrayList<String>();
    private ArrayList<String> user_count = new ArrayList<String>();
    private String url = "";
    private ArrayList<String> authors = new ArrayList<String>();
    private String place = "";
    private float lat;
    private float lon;
    private String title = "";
    private String published_date = "";
    private ArrayList<String> category = new ArrayList<String>();
    private ArrayList<String> sub_category = new ArrayList<String>();
    private ArrayList<String> tags_descriptors = new ArrayList<String>();
    private ArrayList<String> tags_locations = new ArrayList<String>();
    private ArrayList<String> tags_people = new ArrayList<String>();
    private ArrayList<String> tags_orgs = new ArrayList<String>();
    private String content = "";
    private String publisher = "";
    private int count;
    private double score;

    public News() {
    }

    public News(String news_id, ArrayList<String> user_id, ArrayList<String> user_count, String url, ArrayList<String> authors, String place, float lat, float lon, String title, String published_date, ArrayList<String> category, ArrayList<String> sub_category, ArrayList<String> tags_descriptors, ArrayList<String> tags_locations, ArrayList<String> tags_people, ArrayList<String> tags_orgs, String content, String publisher, int count, double score) {
        this.news_id = news_id;
        this.user_id = user_id;
        this.user_count = user_count;
        this.url = url;
        this.authors = authors;
        this.place = place;
        this.lat = lat;
        this.lon = lon;
        this.title = title;
        this.published_date = published_date;
        this.category = category;
        this.sub_category = sub_category;
        this.tags_descriptors = tags_descriptors;
        this.tags_locations = tags_locations;
        this.tags_people = tags_people;
        this.tags_orgs = tags_orgs;
        this.content = content;
        this.publisher = publisher;
        this.count = count;
        this.score = score;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public ArrayList<String> getUser_id() {
        return user_id;
    }

    public void setUser_id(ArrayList<String> user_id) {
        this.user_id = user_id;
    }

    public ArrayList<String> getUser_count() {
        return user_count;
    }

    public void setUser_count(ArrayList<String> user_count) {
        this.user_count = user_count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public ArrayList<String> getSub_category() {
        return sub_category;
    }

    public void setSub_category(ArrayList<String> sub_category) {
        this.sub_category = sub_category;
    }

    public ArrayList<String> getTags_descriptors() {
        return tags_descriptors;
    }

    public void setTags_descriptors(ArrayList<String> tags_descriptors) {
        this.tags_descriptors = tags_descriptors;
    }

    public ArrayList<String> getTags_locations() {
        return tags_locations;
    }

    public void setTags_locations(ArrayList<String> tags_locations) {
        this.tags_locations = tags_locations;
    }

    public ArrayList<String> getTags_people() {
        return tags_people;
    }

    public void setTags_people(ArrayList<String> tags_people) {
        this.tags_people = tags_people;
    }

    public ArrayList<String> getTags_orgs() {
        return tags_orgs;
    }

    public void setTags_orgs(ArrayList<String> tags_orgs) {
        this.tags_orgs = tags_orgs;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "News{" + "news_id=" + news_id + ", user_id=" + user_id + ", user_count=" + user_count + ", url=" + url + ", authors=" + authors + ", place=" + place + ", lat=" + lat + ", lon=" + lon + ", title=" + title + ", published_date=" + published_date + ", category=" + category + ", sub_category=" + sub_category + ", tags_descriptors=" + tags_descriptors + ", tags_locations=" + tags_locations + ", tags_people=" + tags_people + ", tags_orgs=" + tags_orgs + ", content=" + content + ", publisher=" + publisher + ", count=" + count + ", score=" + score + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.news_id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final News other = (News) obj;
        if (!Objects.equals(this.news_id, other.news_id)) {
            return false;
        }
        if (!Objects.equals(this.user_id, other.user_id)) {
            return false;
        }
        if (!Objects.equals(this.user_count, other.user_count)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.authors, other.authors)) {
            return false;
        }
        if (!Objects.equals(this.place, other.place)) {
            return false;
        }
        if (Float.floatToIntBits(this.lat) != Float.floatToIntBits(other.lat)) {
            return false;
        }
        if (Float.floatToIntBits(this.lon) != Float.floatToIntBits(other.lon)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.published_date, other.published_date)) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        if (!Objects.equals(this.sub_category, other.sub_category)) {
            return false;
        }
        if (!Objects.equals(this.tags_descriptors, other.tags_descriptors)) {
            return false;
        }
        if (!Objects.equals(this.tags_locations, other.tags_locations)) {
            return false;
        }
        if (!Objects.equals(this.tags_people, other.tags_people)) {
            return false;
        }
        if (!Objects.equals(this.tags_orgs, other.tags_orgs)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (!Objects.equals(this.publisher, other.publisher)) {
            return false;
        }
        if (this.count != other.count) {
            return false;
        }
        if (Double.doubleToLongBits(this.score) != Double.doubleToLongBits(other.score)) {
            return false;
        }
        return true;
    }

    
    
    
}
