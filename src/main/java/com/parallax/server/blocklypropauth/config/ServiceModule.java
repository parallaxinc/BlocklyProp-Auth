/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.config;

import com.google.inject.AbstractModule;
import com.parallax.server.blocklypropauth.services.SessionService;
import com.parallax.server.blocklypropauth.services.impl.SessionServiceImpl;

/**
 *
 * @author Michel
 */
public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SessionService.class).to(SessionServiceImpl.class).asEagerSingleton();
    }

}
