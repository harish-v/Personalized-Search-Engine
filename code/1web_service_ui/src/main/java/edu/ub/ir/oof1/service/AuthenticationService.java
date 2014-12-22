/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.service;

import edu.ub.ir.oof1.AppDB;
import edu.ub.ir.oof1.Status;
import edu.ub.ir.oof1.User_Attrib;
import edu.ub.ir.oof1.model.User;
import java.util.Map;

/**
 *
 * @author san
 */
public class AuthenticationService {

    public static String addUser(Map<User_Attrib, String> user_attr) {
        
        switch (user_attr.get(User_Attrib.CALLED_FROM)) {
            case "ip_register":
            {
                String request_IP = user_attr.get(User_Attrib.IP_ADDR);
                String user_pk = AppDB.queryforIP(request_IP);
                if(user_pk != null){
                    return user_pk;
                } else{
                  User usr = new User();
                  usr.setUser_ip(request_IP);
                  usr.setUser_pk(User.getNextPk());
                  if(AppDB.addUser(usr)){
                      return usr.getUser_pk();
                  }
                }
            }
                break;
            case "signup":
            {
                String request_IP = user_attr.get(User_Attrib.IP_ADDR);
                String user_id = user_attr.get(User_Attrib.USER_ID);
                String password = user_attr.get(User_Attrib.PASSWORD);
                
                String user_pk = AppDB.queryforIP(request_IP);
                if(user_pk != null){
                    //Anonymous user merging with new User ID creation
                    AppDB.updateUsrInfoByUser_PK(user_pk, user_id, password);
                    return user_pk;
                } else {
                    //Brand new user
                    User usr = new User(user_id, password, User.getNextPk(), request_IP);
                    if(AppDB.addUser(usr)){
                        return usr.getUser_pk();
                    }
                }
            }
                break;
            default:
                
        }
        return null;
        //return (AppDB.addUser(new User(user_id, password)) ? Status.SUCCESS : Status.FAILURE);
    }

    public static String authenticate(String user_id, String password) {
        return AppDB.authenticate(user_id, password);
    }

}
