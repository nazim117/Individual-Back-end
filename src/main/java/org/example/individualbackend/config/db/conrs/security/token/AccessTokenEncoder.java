package org.example.individualbackend.config.db.conrs.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
