package com.dbserver.developertest.exception;

public class MoreThanOneVotePerDayException extends RuntimeException {

    public MoreThanOneVotePerDayException(String message) {
        super(message);
    }
}
