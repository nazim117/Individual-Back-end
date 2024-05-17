package org.example.individualbackend.config.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
