package org.example.individualbackend.business.GeneralExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
//added stuf
public class InvalidCredentialsException extends ResponseStatusException {
    public InvalidCredentialsException() {
        super(HttpStatus.BAD_REQUEST, "INVALID_CREDENTIALS");
    }
}
