/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1;

import edu.ub.ir.oof1.model.User;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author san
 */
public class AppDB {
    
    private static Set<User> users = new HashSet<User>();
    
    public static boolean addUser(User _user){
        return users.add(_user);
    }
    
    public static String authenticate(String user_id, String password){
        
        for(User user : users){
            if(user.getUser_id() != null && user.getUser_id().equals(user_id) 
                    && user.getPassword() != null && user.getPassword().equals(password)){
                return user.getUser_pk();
            }
        }
        return null;
    }
    
    /**
     * returns user primary key for given ip if present.
     * If not null
     * @param user_ip
     * @return 
     */
    public static String queryforIP(String user_ip){
        for(User user : users){
            if(user.getUser_ip() != null && user.getUser_ip().equals(user_ip)){
                return user.getUser_pk();
            }
        }
        return null;
    }
    
    public static boolean containsUsrPK(String user_pk){
        return users.contains(new User(user_pk));
    }
    
    public static void updateUsrInfoByUser_PK(String user_pk, String user_id, String password){
        for(User usr : users)
        {
            if(usr.getUser_pk().equals(user_pk)){
                usr.setUser_id(user_id);
                usr.setPassword(password);
            }
        }
    }
    
    
}
