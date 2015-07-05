/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.common.cloudsession.exceptions;

/**
 *
 * @author Michel
 */
public class UserBlockedException extends Exception {

    private static final String DEFAULT_MESSAGE = "User is blocked";

    public UserBlockedException() {
        super(DEFAULT_MESSAGE);
    }

    public UserBlockedException(String message) {
        super(message);
    }

}
