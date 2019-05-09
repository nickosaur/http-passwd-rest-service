package org.springframework.httprestservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No user with such uid")
public class UserNotFoundException extends RuntimeException {
}