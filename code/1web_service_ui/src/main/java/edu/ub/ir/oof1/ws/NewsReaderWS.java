/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.ir.oof1.ws;

import edu.ub.ir.oof1.Util.JSONUtil;
import edu.ub.ir.oof1.model.Suggestions;
import edu.ub.ir.oof1.service.AuthenticationService;
import edu.ub.ir.oof1.service.NewsService;
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
@Path("/news")
public class NewsReaderWS {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of NewsReaderWS
     */
    public NewsReaderWS() {
    }

    
    /**
     * Retrieves All News Articles
     *
     * @return a JSON Object Array
     */
    @GET
    @Produces("application/json")
    public String getAllNewsTest(@QueryParam("query") String _query, 
            @Context HttpServletRequest request, @CookieParam("user_pk") String _user_pk) {
        
        NewsService news_service = new NewsService();
        
        //if(_user_pk != null && ! _user_pk.isEmpty()){
            //This should only be the option as signUp/IP-registration is mandated
            return news_service.getNewsArrayForUI_InJSON(_query, _user_pk);
        /*  
        } else {
            _user_pk = request.getRemoteAddr();
            AuthenticationService.addUser(_user_pk, "");
            return news_service.getNewsArrayForUI_InJSON(_query, _user_pk);
        
        }
        */
    }
    
    /**
     * Retrieves a single News Article
     *
     * @return a JSON Object
     */
    @GET
    @Path("/{news_id}")
    @Produces("application/json")
    public String getSingleNewsArticleTest(@PathParam("news_id") String news_id) {
        
        NewsService news_service = new NewsService();
        return news_service.getNewsObjectForUI_InJSON("news_id:"+news_id);

    }
    
    /**
     * Retrieves a single News Article
     *
     * @return a JSON Object
     */
    @GET
    @Path("/news_click/{news_id}")
    @Produces("text/plain")
    public String clickedNews(@PathParam("news_id") String news_id, @CookieParam("user_pk") String _user_pk) {
        
        new NewsService().updateDocClickForUser(_user_pk, news_id);
        
        return "SUCCESS";

    }
    
    /**
     * Retrieves a single News Article
     *
     * @return a JSON Object
     */
    @GET
    @Path("/auto_suggest/{query}")
    @Produces("application/json")
    public String getAutoSuggestions(@PathParam("query") String query) {
        System.out.println("query : " + query);
        NewsService news_service = new NewsService();
        Suggestions suggestions = news_service.getAutoSuggestions(query);
        System.out.println(suggestions);
        return JSONUtil.modelToJsonObj(suggestions);

    }
}
