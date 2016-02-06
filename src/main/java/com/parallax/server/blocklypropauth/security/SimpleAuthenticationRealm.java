/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Michel
 */
public class SimpleAuthenticationRealm extends AuthorizingRealm {

    private static Logger log = LoggerFactory.getLogger(SimpleAuthenticationRealm.class);

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof IdAuthenticationToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("AUTHORIZATION");
        AuthorizationInfo authorizationInfo = new SimpleAccount();

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        try {
            if (token instanceof IdAuthenticationToken) {
                Long idUser = (Long) token.getPrincipal();

                try {
                    return new SimpleAccount(idUser, "", "SimpleAuthentication");
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        } catch (NullPointerException npe) {
            log.warn("NullPointer", npe);
        } catch (Throwable t) {
            log.warn("Throwable", t);
        }
        return null;
    }

}
