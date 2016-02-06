/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.rest;

import com.cuubez.visualizer.annotation.Detail;
import com.cuubez.visualizer.annotation.Group;
import com.cuubez.visualizer.annotation.HttpCode;
import com.cuubez.visualizer.annotation.Name;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.parallax.client.cloudsession.CloudSessionLocalUserService;
import com.parallax.client.cloudsession.CloudSessionUserService;
import com.parallax.client.cloudsession.exceptions.PasswordVerifyException;
import com.parallax.client.cloudsession.exceptions.ServerException;
import com.parallax.client.cloudsession.exceptions.UnknownUserIdException;
import com.parallax.client.cloudsession.objects.User;
import com.parallax.server.blocklypropauth.security.AuthenticationInfo;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Michel
 */
@Path("/profile")
@Group(name = "/profile", title = "Change profile info")
@HttpCode("500>Internal Server Error,200>Success Response")
public class RestProfile {

    private CloudSessionLocalUserService cloudSessionLocalUserService;
    private CloudSessionUserService cloudSessionUserService;
    private Configuration configuration;

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        cloudSessionLocalUserService = new CloudSessionLocalUserService(configuration.getString("cloudsession.server"), configuration.getString("cloudsession.baseurl"));
        cloudSessionUserService = new CloudSessionUserService(configuration.getString("cloudsession.baseurl"));
    }

    @POST
    @Path("/base")
    @Detail("Save base profile data")
    @Name("Save base profile data")
    @Produces("application/json")
    public Response saveBase(@FormParam("screenname") String screenname) {
        JsonObject result = new JsonObject();
        if (Strings.isNullOrEmpty(screenname)) {
            result.addProperty("success", false);
            result.addProperty("message", "fields-missing");
            return Response.ok(result.toString()).build();
        } else {
            try {
                User user = cloudSessionUserService.changeUserInfo(AuthenticationInfo.getUserId(), screenname);
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

    @POST
    @Path("/password")
    @Detail("Save password data")
    @Name("Save password data")
    @Produces("application/json")
    public Response savePassword(@FormParam("oldpassword") String oldPassword, @FormParam("password") String password, @FormParam("confirmpassword") String confirmPassword) {
        JsonObject result = new JsonObject();

        if (Strings.isNullOrEmpty(oldPassword) || Strings.isNullOrEmpty(password) || Strings.isNullOrEmpty(confirmPassword)) {
            result.addProperty("success", false);
            result.addProperty("message", "fields-missing");
            return Response.ok(result.toString()).build();
        } else if (!password.equals(confirmPassword)) {
            result.addProperty("success", false);
            result.addProperty("message", "passwords-not-matching");
            return Response.ok(result.toString()).build();
        } else {
            try {
                if (cloudSessionLocalUserService.changePassword(AuthenticationInfo.getUserId(), oldPassword, password, confirmPassword)) {
                    result.addProperty("success", true);
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
            } catch (PasswordVerifyException pve) {
                result.addProperty("success", false);
                result.addProperty("message", "password-not-valid");
                return Response.ok(result.toString()).build();
            } catch (ServerException se) {
                result.addProperty("success", false);
                result.addProperty("message", "server-error");
                return Response.ok(result.toString()).build();
            }
        }
    }

}
