/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.service;

import edu.ub.ir.oof1.Properties;
import edu.ub.ir.oof1.model.*;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * @author san
 */
public class Solr {
    private static SolrServer SERVER = new HttpSolrServer(Properties.SOLR_URL);
    
    public void addDoc(AbstractModel _inDoc) {
        try {
            SolrInputDocument _docToAdd = getSolrDocForModel(_inDoc);
            SERVER.add(_docToAdd);
            SERVER.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SolrInputDocument getSolrDocForModel(AbstractModel doc) {
        SolrInputDocument out = new SolrInputDocument();

        try {
            Class<?> _class = doc.getClass();

            //System.out.println("Unpacking Bean of Class : " + _class.getName());

            Method[] _declaredMethods = _class.getDeclaredMethods();
            ArrayList<Method> _getterMethods = getGetterMethodNames(_declaredMethods);

            for (Method m : _getterMethods) {
                Object returnVal = m.invoke(doc);
                String fieldName = m.getName().toLowerCase().replaceFirst("get", "");
                

                //System.out.println(m.getName() + "\t:\t" + returnVal);
                out.addField(fieldName, returnVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;

    }

    public static ArrayList<Method> getGetterMethodNames(Method[] _methods) {
        return getFilteredMethods(_methods, "get");
    }

    public static ArrayList<Method> getSetterMethodNames(Method[] _methods) {
        return getFilteredMethods(_methods, "set");
    }

    public static ArrayList<Method> getFilteredMethods(Method[] _methods, String filterType) {
        ArrayList<Method> out = new ArrayList<Method>();

        for (Method m : _methods) {
            if (m.getName().startsWith(filterType)) {
                out.add(m);
            }
        }

        return out;
    }

    public static void main(String[] args) {
        Solr test = new Solr();
        AbstractModel news = new News("NYT_121", null, null, null, null, null, 10.5f, 12, null, null, null, null, null, null, null, null, null, null, 10);
        Solr.getSolrDocForModel(news);
        //AbstractModel news = new NewsService("12", {"Sankar"}, "Title text", "News content");
    }

}
