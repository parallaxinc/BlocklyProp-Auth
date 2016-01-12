/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.parallax.client.cloudsession.CloudSessionAuthenticateService;
import com.parallax.client.cloudsession.CloudSessionAuthenticationTokenService;
import com.parallax.client.cloudsession.exceptions.EmailNotConfirmedException;
import com.parallax.client.cloudsession.exceptions.ServerException;
import com.parallax.client.cloudsession.exceptions.UnknownUserException;
import com.parallax.client.cloudsession.exceptions.UnknownUserIdException;
import com.parallax.client.cloudsession.exceptions.UserBlockedException;
import com.parallax.client.cloudsession.objects.User;
import com.parallax.server.blocklypropauth.AuthenticationResult;
import com.parallax.server.blocklypropauth.services.AuthenticationService;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Michel
 */
@Singleton
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private static Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private Configuration configuration;

    private CloudSessionAuthenticateService authenticateService;
    private CloudSessionAuthenticationTokenService authenticationTokenService;

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        authenticateService = new CloudSessionAuthenticateService(configuration.getString("cloudsession.baseurl"));
        authenticationTokenService = new CloudSessionAuthenticationTokenService(configuration.getString("cloudsession.server"), configuration.getString("cloudsession.baseurl"));
    }

    @Override
    public AuthenticationResult authenticate(String username, String password, String browser, String ipAddress) {
        try {
            User user = authenticateService.authenticateLocalUser(username, password);
            String token = authenticationTokenService.request(user.getId(), browser, ipAddress);

            return new AuthenticationResult(user, token);
        } catch (UnknownUserException ex) {
            log.info("Unkown user: {}", username);
        } catch (UserBlockedException ex) {
            log.info("Blocked user: {}", username);
        } catch (EmailNotConfirmedException ex) {
            log.info("Email not confirmed: {}", username);
        } catch (UnknownUserIdException ex) {
            log.info("Unknown user id: {}", username);
        } catch (ServerException ex) {
            log.error("Server error: {}", username);
        }
        return null;
    }

}
