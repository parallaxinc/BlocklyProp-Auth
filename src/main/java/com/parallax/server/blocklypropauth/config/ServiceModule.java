/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.config;

import com.google.inject.AbstractModule;
import com.parallax.server.blocklypropauth.services.AuthenticationService;
import com.parallax.server.blocklypropauth.services.impl.AuthenticationServiceImpl;

/**
 *
 * @author Michel
 */
public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
    }

}
