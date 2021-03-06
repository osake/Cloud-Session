/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.common.cloudsession.config;

import com.google.inject.AbstractModule;
import com.parallax.server.common.cloudsession.service.AuthenticationTokenService;
import com.parallax.server.common.cloudsession.service.BucketService;
import com.parallax.server.common.cloudsession.service.ConfirmTokenService;
import com.parallax.server.common.cloudsession.service.DatabaseMaintenanceService;
import com.parallax.server.common.cloudsession.service.MailService;
import com.parallax.server.common.cloudsession.service.PasswordValidationService;
import com.parallax.server.common.cloudsession.service.ResetTokenService;
import com.parallax.server.common.cloudsession.service.UserService;
import com.parallax.server.common.cloudsession.service.impl.AuthenticationTokenServiceImpl;
import com.parallax.server.common.cloudsession.service.impl.BucketServiceImpl;
import com.parallax.server.common.cloudsession.service.impl.ConfirmTokenServiceImpl;
import com.parallax.server.common.cloudsession.service.impl.DatabaseMaintenanceServiceImpl;
import com.parallax.server.common.cloudsession.service.impl.MailServiceImpl;
import com.parallax.server.common.cloudsession.service.impl.PasswordValidationServiceImpl;
import com.parallax.server.common.cloudsession.service.impl.ResetTokenServiceImpl;
import com.parallax.server.common.cloudsession.service.impl.UserServiceImpl;

/**
 *
 * @author Michel
 */
public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DatabaseMaintenanceService.class).to(DatabaseMaintenanceServiceImpl.class).asEagerSingleton();
        bind(MailService.class).to(MailServiceImpl.class);
        bind(PasswordValidationService.class).to(PasswordValidationServiceImpl.class);

        bind(ResetTokenService.class).to(ResetTokenServiceImpl.class);
        bind(ConfirmTokenService.class).to(ConfirmTokenServiceImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(BucketService.class).to(BucketServiceImpl.class);
        bind(AuthenticationTokenService.class).to(AuthenticationTokenServiceImpl.class);
    }

}
