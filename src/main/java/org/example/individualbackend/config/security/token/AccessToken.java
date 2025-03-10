package org.example.individualbackend.config.security.token;

import java.util.Set;

public interface AccessToken {
    String getSubject();

    Integer getUserId();

    Set<String> getRoles();

    boolean hasRole(String roleName);
}
