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
//        serve("/pong").with(PingServlet.class);
//
//        serve("/project").with(ProjectServlet.class);
//        serve("/user").with(UserServlet.class);
//        serve("/register").with(RegisterServlet.class);
//        serve("/profile").with(ProfileServlet.class);
//
//        serve("/confirmrequest").with(ConfirmRequestServlet.class);
//        serve("/confirm").with(ConfirmServlet.class);
//
//        serve("/resetrequest").with(PasswordResetRequestServlet.class);
//        serve("/reset").with(PasswordResetServlet.class);
//
//        serve("/createproject").with(ProjectCreationServlet.class);
//
//        // Textile pages
//        serve("/public/license").with(TextileLicenseServlet.class);
//        serve("/public/libraries").with(TextileLibrariesServlet.class);
//
//        // Help
//        serve("/public/help").with(HelpServlet.class);
//        serve("/public/helpsearch").with(HelpSearchServlet.class);
    }

}
