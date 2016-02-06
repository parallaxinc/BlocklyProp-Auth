/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author Michel
 */
public class AuthenticationInfo {

    public static Long getUserId() {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            return (Long) currentUser.getPrincipal();
        } catch (Throwable t) {
            return null;
        }

    }

}
