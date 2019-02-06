package com.cognizant.labs.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class KeyCloakOAuthPermissionEvaluator implements PermissionEvaluator {

    private static final Log logger = LogFactory.getLog(KeyCloakOAuthPermissionEvaluator.class);

    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ROLES = "roles";

    @Value("${realm.appName}")
    private String appName;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        logger.info("has permission called");
        if ((authentication == null) ||  (permission == null)) {
            logger.warn("hasPermission not setup correctly");
            return false;
        }//end if
        //get the token out
        Jwt jwt = (Jwt) authentication.getCredentials();
        //get the claims
        Map<String,Object> claims = jwt.getClaims();
        Map<String,Object> resources = (Map<String, Object>) claims.get(RESOURCE_ACCESS);
        Map<String, List<String>> roles = (Map<String, List<String>>) resources.get(appName);
        //go through the roles
        for (String role : roles.get(ROLES)) {
            if (role.equalsIgnoreCase(permission.toString())) {
                //match
                return true;
            }//end if
        }//end for
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        logger.warn("hasPermission serializable, targetid, targettype is NOT configured for use");
        return false;
    }
}
