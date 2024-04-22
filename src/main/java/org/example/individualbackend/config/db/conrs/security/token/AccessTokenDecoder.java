package org.example.individualbackend.config.db.conrs.security.token;

public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}
