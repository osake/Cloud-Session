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
public class PasswordComplexityException extends Exception {

    private static final String DEFAULT_MESSAGE = "Password is not complex enough";

    public PasswordComplexityException() {
        super(DEFAULT_MESSAGE);
    }

}
