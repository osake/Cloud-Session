/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.common.cloudsession.rest;

import com.codahale.metrics.annotation.Timed;
import com.cuubez.visualizer.annotation.Detail;
import com.cuubez.visualizer.annotation.Group;
import com.cuubez.visualizer.annotation.HttpCode;
import com.cuubez.visualizer.annotation.Name;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.parallax.server.common.cloudsession.db.generated.tables.records.ConfirmtokenRecord;
import com.parallax.server.common.cloudsession.db.generated.tables.records.ResettokenRecord;
import com.parallax.server.common.cloudsession.db.generated.tables.records.UserRecord;
import com.parallax.server.common.cloudsession.db.utils.JsonResult;
import com.parallax.server.common.cloudsession.db.utils.Validation;
import com.parallax.server.common.cloudsession.exceptions.InsufficientBucketTokensException;
import com.parallax.server.common.cloudsession.exceptions.PasswordComplexityException;
import com.parallax.server.common.cloudsession.exceptions.PasswordVerifyException;
import com.parallax.server.common.cloudsession.exceptions.UnknownUserException;
import com.parallax.server.common.cloudsession.exceptions.UnknownUserIdException;
import com.parallax.server.common.cloudsession.service.ConfirmTokenService;
import com.parallax.server.common.cloudsession.service.ResetTokenService;
import com.parallax.server.common.cloudsession.service.UserService;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author Michel
 */
@Path("/local")
@Group(name = "/local", title = "Local user services using tokens")
@HttpCode("500>Internal Server Error,200>Success Response")
public class RestLocalUserService {

    private ResetTokenService resetTokenService;

    private ConfirmTokenService confirmTokenService;

    private UserService userService;

    @Inject
    public void setResetTokenService(ResetTokenService resetTokenService) {
        this.resetTokenService = resetTokenService;
    }

    @Inject
    public void setConfirmTokenService(ConfirmTokenService confirmTokenService) {
        this.confirmTokenService = confirmTokenService;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/resetById/{id}")
    @Detail("Requests to send a request email to the specified user with a password reset token")
    @Name("Request password reset")
    @Produces("text/json")
    @Timed(name = "requestResetById")
    public Response requestReset(@HeaderParam("server") String server, @PathParam("id") Long idUser) {
        Validation validation = new Validation();
        validation.addRequiredField("server", server);
        validation.addRequiredField("id", idUser);
        if (!validation.isValid()) {
            return validation.getValidationResponse();
        }

        try {
            ResettokenRecord resetToken = resetTokenService.createResetToken(server, idUser);
            JsonObject json = new JsonObject();
            if (resetToken != null) {
                json.addProperty("success", true);
                //     json.addProperty("token", resetToken.getToken());
            } else {
                json.addProperty("success", false);
            }
            return Response.ok(json.toString()).build();
        } catch (UnknownUserIdException uuie) {
            return Response.serverError().entity(JsonResult.getFailure(uuie)).build();
        } catch (InsufficientBucketTokensException ibte) {
            return Response.serverError().entity(JsonResult.getFailure(ibte)).build();
        }
    }

    @GET
    @Path("/reset/{email}")
    @Detail("Requests to send a request email to the specified user with a password reset token")
    @Name("Request password reset")
    @Produces("text/json")
    @Timed(name = "requestResetByEmail")
    public Response requestReset(@HeaderParam("server") String server, @PathParam("email") String email) {
        Validation validation = new Validation();
        validation.addRequiredField("email", email);
        validation.checkEmail("email", email);
        if (!validation.isValid()) {
            return validation.getValidationResponse();
        }

        try {
            ResettokenRecord resetToken = resetTokenService.createResetToken(server, email);
            JsonObject json = new JsonObject();
            if (resetToken != null) {
                json.addProperty("success", true);
                //      json.addProperty("token", resetToken.getToken());
            } else {
                json.addProperty("success", false);
            }
            return Response.ok(json.toString()).build();
        } catch (UnknownUserException uue) {
            return Response.serverError().entity(JsonResult.getFailure(uue)).build();
        } catch (InsufficientBucketTokensException ibte) {
            return Response.serverError().entity(JsonResult.getFailure(ibte)).build();
        }
    }

    @POST
    @Path("/reset/{email}")
    @Detail("Reset the users password with use of a password reset token")
    @Name("Do password reset")
    @Produces("text/json")
    @Timed(name = "doReset")
    public Response doReset(@PathParam("email") String email, @FormParam("token") String token, @FormParam("password") String password, @FormParam("password-confirm") String passwordConfirm) {
        Validation validation = new Validation();
        validation.addRequiredField("email", email);
        validation.addRequiredField("token", token);
        validation.addRequiredField("password", password);
        validation.addRequiredField("password-confirm", passwordConfirm);
        validation.checkEmail("email", email);
        if (!validation.isValid()) {
            return validation.getValidationResponse();
        }

        try {
            JsonObject json = new JsonObject();
            UserRecord userRecord = userService.resetPassword(email, token, password, passwordConfirm);
            if (userRecord != null) {
                json.addProperty("success", true);
            } else {
                json.addProperty("success", false);
                json.addProperty("code", 530);
            }

            return Response.ok(json.toString()).build();
        } catch (UnknownUserException uue) {
            return Response.serverError().entity(JsonResult.getFailure(uue)).build();
        } catch (PasswordVerifyException pve) {
            return Response.serverError().entity(JsonResult.getFailure(pve)).build();
        } catch (PasswordComplexityException pce) {
            return Response.serverError().entity(JsonResult.getFailure(pce)).build();
        }
    }

    @POST
    @Path("/confirm")
    @Detail("Confirm the users emailadress using the token")
    @Name("Do email confirm")
    @Produces("text/json")
    @Timed(name = "doConfirm")
    public Response doConfirm(@FormParam("email") String email, @FormParam("token") String token) {
        Validation validation = new Validation();
        validation.addRequiredField("email", email);
        validation.addRequiredField("token", token);
        validation.checkEmail("email", email);
        if (!validation.isValid()) {
            return validation.getValidationResponse();
        }

        try {
            JsonObject json = new JsonObject();
            UserRecord userRecord = userService.confirmEmail(email, token);
            if (userRecord != null) {
                json.addProperty("success", true);
            } else {
                json.addProperty("success", false);
                json.addProperty("code", 510);
            }
            return Response.ok(json.toString()).build();
        } catch (UnknownUserException uue) {
            return Response.serverError().entity(JsonResult.getFailure(uue)).build();
        }
    }

    @GET
    @Path("/confirm/{email}")
    @Detail("Request to email a new email confirmation token to the specified user")
    @Name("Request new confirm token")
    @Produces("text/json")
    @Timed(name = "requestConfirm")
    public Response requestConfirm(@HeaderParam("server") String server, @PathParam("email") String email) {
        Validation validation = new Validation();
        validation.addRequiredField("server", server);
        validation.addRequiredField("email", email);
        validation.checkEmail("email", email);
        if (!validation.isValid()) {
            return validation.getValidationResponse();
        }

        try {
            UserRecord userRecord = userService.getLocalUser(email);
            ConfirmtokenRecord confirmtoken = confirmTokenService.createConfirmToken(server, userRecord.getId());
            JsonObject json = new JsonObject();
            if (confirmtoken != null) {
                json.addProperty("success", true);
                //           json.addProperty("token", confirmtoken.getToken());
            } else {
                json.addProperty("success", false);
                json.addProperty("message", "Account already verified");
                json.addProperty("code", 520);
            }

            return Response.ok(json.toString()).build();
        } catch (UnknownUserException uue) {
            return Response.serverError().entity(JsonResult.getFailure(uue)).build();
        } catch (InsufficientBucketTokensException ibte) {
            return Response.serverError().entity(JsonResult.getFailure(ibte)).build();
        } catch (UnknownUserIdException uuie) {
            return Response.serverError().entity(JsonResult.getFailure(uuie)).build();
        }
    }

    @POST
    @Path("/password/{id}")
    @Detail("Change the users password")
    @Name("Do password change")
    @Produces("text/json")
    @Timed(name = "doPasswordChange")
    public Response doPasswordChange(@PathParam("id") Long idUser, @FormParam("old-password") String oldPassword, @FormParam("password") String password, @FormParam("password-confirm") String passwordConfirm) {
        Validation validation = new Validation();
        validation.addRequiredField("id", idUser);
        validation.addRequiredField("old-password", oldPassword);
        validation.addRequiredField("password", password);
        validation.addRequiredField("password-confirm", passwordConfirm);
        if (!validation.isValid()) {
            return validation.getValidationResponse();
        }

        try {
            JsonObject json = new JsonObject();
            UserRecord userRecord = userService.changePassword(idUser, oldPassword, password, passwordConfirm);
            if (userRecord != null) {
                json.addProperty("success", true);
            } else {
                json.addProperty("success", false);
                json.addProperty("code", 530);
            }

            return Response.ok(json.toString()).build();
        } catch (UnknownUserIdException uue) {
            return Response.serverError().entity(JsonResult.getFailure(uue)).build();
        } catch (PasswordVerifyException pve) {
            return Response.serverError().entity(JsonResult.getFailure(pve)).build();
        } catch (PasswordComplexityException pce) {
            return Response.serverError().entity(JsonResult.getFailure(pce)).build();
        }
    }

}
