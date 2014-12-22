/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.ws;

import edu.ub.ir.oof1.AppDB;
import edu.ub.ir.oof1.Status;
import edu.ub.ir.oof1.User_Attrib;
import edu.ub.ir.oof1.service.AuthenticationService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author san
 */
@Path("auth")
public class AuthenticationResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AuthenticationResource
     */
    public AuthenticationResource() {
    }

    /**
     * Retrieves representation of an instance of edu.ub.ir.oof1.ws.AuthenticationResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/signup")
    @Produces("application/json")
    public String addUser(@QueryParam("user_id") String _user_id,
            @QueryParam("password") String _password, @CookieParam("user_pk") String _user_pk,
            @Context HttpServletRequest request) {
        
        if(_user_pk != null && !_user_pk.isEmpty() && AppDB.containsUsrPK(_user_pk)){
            AppDB.updateUsrInfoByUser_PK(_user_pk, _user_id, _password);
            return "{\"status\":\"SUCCESS\", \"out\":\""+_user_pk+"\"}";
        }
        
        String ipAddr = request.getRemoteAddr();
        Map<User_Attrib, String> user_attr = new HashMap<User_Attrib, String>();
        user_attr.put(User_Attrib.CALLED_FROM, "signup");
        user_attr.put(User_Attrib.IP_ADDR, ipAddr);
        user_attr.put(User_Attrib.USER_ID, _user_id);
        user_attr.put(User_Attrib.PASSWORD, _password);
        
        String usr_pk = AuthenticationService.addUser(user_attr);
        
        if(usr_pk != null){
            return "{\"status\":\"SUCCESS\", \"out\":\""+usr_pk+"\"}";
        }else{
            return "{\"status\":\"FAILURE\", \"out\":\"User Id already exists\"}";
            }
    }
    
    /**
     * Retrieves representation of an instance of edu.ub.ir.oof1.ws.AuthenticationResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/ip_register")
    @Produces("application/json")
    public String ipRegister(@Context HttpServletRequest request, @CookieParam("user_pk") String _user_pk) {
        
        if(_user_pk != null && !_user_pk.isEmpty()){
            return "{\"status\":\"SUCCESS\", \"out\":\""+_user_pk+"\"}";
        }
        
        String ipAddr = request.getRemoteAddr();
        Map<User_Attrib, String> user_attr = new HashMap<User_Attrib, String>();
        user_attr.put(User_Attrib.CALLED_FROM, "ip_register");
        user_attr.put(User_Attrib.IP_ADDR, ipAddr);
        
        String usr_pk = AuthenticationService.addUser(user_attr);
        
        if(usr_pk != null){
            return "{\"status\":\"SUCCESS\", \"out\":\""+usr_pk+"\"}";
        }else{
            return "{\"status\":\"FAILURE\", \"out\":\"IP registration failed\"}";
            }
            
    }
    
    @GET
    @Produces("application/json")
    public String authenticate(@QueryParam("user_id") String _user_id,
            @QueryParam("password") String _password) {
        
        String out = "";
        
        String usr_pk = AuthenticationService.authenticate(_user_id, _password);
        
        if(usr_pk != null){
            return "{\"status\":\"SUCCESS\", \"out\":\""+usr_pk+"\"}";
        }else{
            return "{\"status\":\"FAILURE\", \"out\":\"Invalid User Id (or) password. Try again.\"}";
            }
        
    }

    /**
     * PUT method for updating or creating an instance of AuthenticationResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
