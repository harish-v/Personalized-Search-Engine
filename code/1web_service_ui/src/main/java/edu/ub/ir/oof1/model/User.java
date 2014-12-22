/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author san
 */
public class User extends AbstractModel implements Serializable{
    private static int PK_COUNTER;
    private String user_pk;
    private String user_id;
    private String password;
    private Preferrance pref;
    private String user_ip;

    public User(String user_id, String password, String user_pk, String user_ip) {
        this.user_id = user_id;
        this.password = password;
        this.user_pk = user_pk;
        this.user_ip = user_ip;
    }

    public User(String user_pk) {
        this.user_pk = user_pk;
    }
    
    public User() {
    }

    public final static String getNextPk(){
        return ""+PK_COUNTER++;
    }
    
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Preferrance getPref() {
        return pref;
    }

    public void setPref(Preferrance pref) {
        this.pref = pref;
    }

    public String getUser_pk() {
        return user_pk;
    }

    public void setUser_pk(String user_pk) {
        this.user_pk = user_pk;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.user_pk);
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
        final User other = (User) obj;
        if (!Objects.equals(this.user_pk, other.user_pk)) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
