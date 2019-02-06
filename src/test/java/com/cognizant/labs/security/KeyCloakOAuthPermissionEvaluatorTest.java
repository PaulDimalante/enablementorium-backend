package com.cognizant.labs.security;

import org.junit.Test;
import org.powermock.api.support.membermodification.MemberModifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeyCloakOAuthPermissionEvaluatorTest {

    @Test
    public void hasPermission() throws Exception {
        Map<String,List<String>> roles = new HashMap<>();
        roles.put("roles", Arrays.asList("PROJECT_MANAGER"));
        //now add that
        Map<String,Object> app = new HashMap<>();
        app.put("test-app",roles);
        //claims
        Map<String,Object> claims = new HashMap<>();
        claims.put("resource_access",app);

        KeyCloakOAuthPermissionEvaluator keyCloakOAuthPermissionEvaluator = new KeyCloakOAuthPermissionEvaluator();
        MemberModifier.field(KeyCloakOAuthPermissionEvaluator.class,"appName").set(keyCloakOAuthPermissionEvaluator,"test-app");
        Authentication authentication = mock(Authentication.class);
        Jwt jwtToken = mock(Jwt.class);
        when(jwtToken.getClaims()).thenReturn(claims);
        when(authentication.getCredentials()).thenReturn(jwtToken);
        //execute
        assertTrue(keyCloakOAuthPermissionEvaluator.hasPermission(authentication,null,"PROJECT_MANAGER"));
        //check false
        assertFalse(keyCloakOAuthPermissionEvaluator.hasPermission(authentication,null,"OTHER"));
    }

    @Test
    public void hasPermission1() {
        KeyCloakOAuthPermissionEvaluator keyCloakOAuthPermissionEvaluator = new KeyCloakOAuthPermissionEvaluator();
        assertFalse(keyCloakOAuthPermissionEvaluator.hasPermission(null,null,null,null));
    }
}