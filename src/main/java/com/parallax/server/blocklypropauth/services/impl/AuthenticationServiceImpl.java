/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.services.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
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
import com.parallax.server.blocklypropauth.security.IdAuthenticationToken;
import com.parallax.server.blocklypropauth.services.AuthenticationService;
import javax.servlet.http.HttpSession;
import org.apache.commons.configuration.Configuration;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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

    private Provider<HttpSession> sessionProvider;

    private CloudSessionAuthenticateService authenticateService;
    private CloudSessionAuthenticationTokenService authenticationTokenService;

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        authenticateService = new CloudSessionAuthenticateService(configuration.getString("cloudsession.server"), configuration.getString("cloudsession.baseurl"));
        authenticationTokenService = new CloudSessionAuthenticationTokenService(configuration.getString("cloudsession.server"), configuration.getString("cloudsession.baseurl"));
    }

    @Inject
    public void setSessionProvider(Provider<HttpSession> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Override
    public AuthenticationResult authenticate(String username, String password, String browser, String ipAddress) {
        try {
            User user = authenticateService.authenticateLocalUser(username, password);
            String token = authenticationTokenService.request(user.getId(), browser, ipAddress);

            doAuthentication(user);

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

    private void doAuthentication(User user) {
        Subject currentUser = SecurityUtils.getSubject();
        IdAuthenticationToken idAuthenticationToken = new IdAuthenticationToken(user.getId());

        try {
            currentUser.login(idAuthenticationToken);

            sessionProvider.get().setAttribute("idUser", user.getId());

            log.info("User logged in: {}", user.getId());
        } catch (Throwable t) {
            log.error("Error while authenticating", t);
        }
    }

}
