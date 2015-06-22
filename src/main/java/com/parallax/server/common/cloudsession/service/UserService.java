/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.common.cloudsession.service;

import com.parallax.server.common.cloudsession.db.generated.tables.records.UserRecord;

/**
 *
 * @author Michel
 */
public interface UserService {

    UserRecord resetPassword(String email, String token, String password, String repeatPassword);

    UserRecord changePassword(Long id, String oldPassword, String password, String repeatPassword);

}
