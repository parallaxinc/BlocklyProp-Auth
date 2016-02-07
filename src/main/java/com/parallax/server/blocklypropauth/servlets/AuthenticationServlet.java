/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.servlets;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.parallax.server.blocklypropauth.AuthenticationResult;
import com.parallax.server.blocklypropauth.services.AuthenticationService;
import com.parallax.server.blocklypropauth.utils.JsonUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Michel
 */
@Singleton
public class AuthenticationServlet extends HttpServlet {

    private AuthenticationService authenticationService;

    @Inject
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        String remoteAddress = req.getRemoteAddr();
        String userAgent = req.getHeader("User-Agent");

//        System.out.println("username: " + username);
//        System.out.println("password: " + password);
//        System.out.println("Remote address: " + remoteAddress);
//        System.out.println("User agent: " + userAgent);
        AuthenticationResult authenticationResult = authenticationService.authenticate(username, password, userAgent, remoteAddress);

        if (authenticationResult == null) {
            resp.getWriter().write(JsonUtils.createJsonFailure().toString());
        } else {
            JsonObject result = new JsonObject();
            result.addProperty("id-user", authenticationResult.getUser().getId());
            result.addProperty("token", authenticationResult.getToken());
            resp.getWriter().write(JsonUtils.createJsonSuccess(result).toString());
        }

    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");
    }

}
