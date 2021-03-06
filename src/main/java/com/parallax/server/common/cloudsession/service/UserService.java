/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.common.cloudsession.service;

import com.parallax.server.common.cloudsession.db.generated.tables.records.UserRecord;
import com.parallax.server.common.cloudsession.exceptions.EmailNotConfirmedException;
import com.parallax.server.common.cloudsession.exceptions.InsufficientBucketTokensException;
import com.parallax.server.common.cloudsession.exceptions.NonUniqueEmailException;
import com.parallax.server.common.cloudsession.exceptions.PasswordComplexityException;
import com.parallax.server.common.cloudsession.exceptions.PasswordVerifyException;
import com.parallax.server.common.cloudsession.exceptions.ScreennameUsedException;
import com.parallax.server.common.cloudsession.exceptions.UnknownUserException;
import com.parallax.server.common.cloudsession.exceptions.UnknownUserIdException;
import com.parallax.server.common.cloudsession.exceptions.UserBlockedException;

/**
 *
 * @author Michel
 */
public interface UserService {

    UserRecord resetPassword(String email, String token, String password, String repeatPassword) throws PasswordVerifyException, UnknownUserException, PasswordComplexityException;

    UserRecord changePassword(Long id, String oldPassword, String password, String repeatPassword) throws PasswordVerifyException, UnknownUserIdException, PasswordComplexityException;

    UserRecord confirmEmail(String email, String token) throws UnknownUserException;

    UserRecord register(String server, String email, String password, String passwordConfirm, String locale, String screenname) throws PasswordVerifyException, NonUniqueEmailException, PasswordComplexityException, ScreennameUsedException;

    UserRecord authenticateLocal(String email, String password) throws UnknownUserException, InsufficientBucketTokensException, EmailNotConfirmedException, UserBlockedException;

    UserRecord getLocalUser(String email) throws UnknownUserException;

    UserRecord getUser(String email) throws UnknownUserException;

    UserRecord getUser(Long id) throws UnknownUserIdException;

    UserRecord changeInfo(Long idUser, String screenname) throws UnknownUserIdException, ScreennameUsedException;

    UserRecord changeLocale(Long idUser, String locale) throws UnknownUserIdException;

}
