package com.scignup.search;

/**
 * Created by matt on 12/24/15.
 */
public class InvalidLoginException extends Exception {
    public InvalidLoginException(String username, String password) {
        super("Username " + username + " or password " + password + " invalid.");
    }
}
