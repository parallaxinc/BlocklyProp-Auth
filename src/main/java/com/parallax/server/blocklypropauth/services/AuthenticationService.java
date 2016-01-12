/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.services;

import com.parallax.server.blocklypropauth.AuthenticationResult;

/**
 *
 * @author Michel
 */
public interface AuthenticationService {

    AuthenticationResult authenticate(String username, String password, String browser, String ipAddress);

}
