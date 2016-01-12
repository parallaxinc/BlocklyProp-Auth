/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.config;

import com.google.inject.servlet.ServletModule;
import com.parallax.server.blocklypropauth.servlets.AuthenticationServlet;

/**
 *
 * @author Michel
 */
public class ServletsModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/authenticate").with(AuthenticationServlet.class);
    }

}
