/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.common.cloudsession.rest;

import com.cuubez.visualizer.annotation.Detail;
import com.cuubez.visualizer.annotation.Group;
import com.cuubez.visualizer.annotation.HttpCode;
import com.cuubez.visualizer.annotation.Name;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.parallax.server.common.cloudsession.db.generated.tables.records.ResettokenRecord;
import com.parallax.server.common.cloudsession.db.generated.tables.records.UserRecord;
import com.parallax.server.common.cloudsession.db.utils.JsonResult;
import com.parallax.server.common.cloudsession.exceptions.UnknownUserException;
import com.parallax.server.common.cloudsession.exceptions.UnknownUserIdException;
import com.parallax.server.common.cloudsession.service.ConfirmTokenService;
import com.parallax.server.common.cloudsession.service.ResetTokenService;
import com.parallax.server.common.cloudsession.service.UserService;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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
public class RestLocalUserServices {

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
    @Name("Reset")
    @Produces("text/json")
    public Response requestReset(@PathParam("id") Long idUser) {
        try {
            ResettokenRecord resetToken = resetTokenService.createResetToken(idUser);
            JsonObject json = new JsonObject();
            if (resetToken != null) {
                json.addProperty("success", true);
                json.addProperty("token", resetToken.getToken());
            } else {
                json.addProperty("success", false);
            }
            return Response.ok(json.toString()).build();
        } catch (UnknownUserIdException uuie) {
            return Response.serverError().entity(JsonResult.getFailure(uuie)).build();
        }
    }

    @GET
    @Path("/reset/{email}")
    @Detail("Requests to send a request email to the specified user with a password reset token")
    @Name("Reset")
    @Produces("text/json")
    public Response requestReset(@PathParam("email") String email) {
        try {
            ResettokenRecord resetToken = resetTokenService.createResetToken(email);
            JsonObject json = new JsonObject();
            if (resetToken != null) {
                json.addProperty("success", true);
                json.addProperty("token", resetToken.getToken());
            } else {
                json.addProperty("success", false);
            }
            return Response.ok(json.toString()).build();
        } catch (UnknownUserException uue) {
            return Response.serverError().entity(JsonResult.getFailure(uue)).build();
        }
    }

    @POST
    @Path("/reset/{email}")
    @Detail("Requests to send a request email to the specified user with a password reset token")
    @Name("Reset")
    @Produces("text/json")
    public Response doReset(@PathParam("email") String email, @FormParam("token") String token, @FormParam("password") String password, @FormParam("password-confirm") String passwordConfirm) {
        try {
            boolean validResetToken = resetTokenService.isValidResetToken(token);
            JsonObject json = new JsonObject();
            if (validResetToken) {
                UserRecord userRecord = userService.resetPassword(email, token, password, passwordConfirm);
                if (userRecord != null) {
                    json.addProperty("success", true);
                } else {
                    json.addProperty("success", false);
                }
            }
            json.addProperty("success", true);
            return Response.ok(json.toString()).build();
        } catch (UnknownUserException uue) {
            return Response.serverError().entity(JsonResult.getFailure(uue)).build();
        }
    }

}
