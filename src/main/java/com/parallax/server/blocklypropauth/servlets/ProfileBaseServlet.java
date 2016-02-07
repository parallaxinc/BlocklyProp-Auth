/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.servlets;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.parallax.client.cloudsession.CloudSessionLocalUserService;
import com.parallax.client.cloudsession.CloudSessionUserService;
import com.parallax.client.cloudsession.exceptions.ServerException;
import com.parallax.client.cloudsession.exceptions.UnknownUserIdException;
import com.parallax.client.cloudsession.objects.User;
import com.parallax.server.blocklypropauth.rest.RestProfile;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Michel
 */
@Singleton
public class ProfileBaseServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(RestProfile.class);

    private CloudSessionLocalUserService cloudSessionLocalUserService;
    private CloudSessionUserService cloudSessionUserService;
    private Configuration configuration;

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        cloudSessionLocalUserService = new CloudSessionLocalUserService(configuration.getString("cloudsession.server"), configuration.getString("cloudsession.baseurl"));
        cloudSessionUserService = new CloudSessionUserService(configuration.getString("cloudsession.baseurl"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idString = req.getParameter("id");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String screenname = req.getParameter("screenname");

        try {
            Long id = Long.parseLong(idString);
//            Response response = saveBase(id, us, password, screenname)
        } catch (NumberFormatException nfe) {

        } catch (NullPointerException npe) {

        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");
    }

    public Response saveBase(Long id, Long username, String password, String screenname) {
        System.out.println("Gets in method");
        JsonObject result = new JsonObject();
        if (Strings.isNullOrEmpty(screenname)) {
            result.addProperty("success", false);
            result.addProperty("message", "fields-missing");
            return Response.ok(result.toString()).build();
        } else {
            try {
                User user = cloudSessionUserService.changeUserInfo(id, screenname);
                if (user != null) {
                    result.addProperty("success", true);
                    result.addProperty("screenname", user.getScreenname());
                    return Response.ok(result.toString()).build();
                } else {
                    result.addProperty("success", false);
                    result.addProperty("message", "could-not-change");
                    return Response.ok(result.toString()).build();
                }
            } catch (UnknownUserIdException uuie) {
                result.addProperty("success", false);
                result.addProperty("message", "unknown-user");
                return Response.ok(result.toString()).build();
            } catch (ServerException se) {
                result.addProperty("success", false);
                result.addProperty("message", "server-error");
                return Response.ok(result.toString()).build();
            }
        }
    }

}
