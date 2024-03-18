package com.example.crud2.decorater;

import com.example.crud2.AllMessages.ResponseConstant;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.http.HttpStatus;

@JsonRootName(value = "status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Response {

    HttpStatus code;
    String status;
    String description;

    public static Response getOkResponse(){
        return new Response(HttpStatus.OK, ResponseConstant.SUCCESS, ResponseConstant.OK_DESCRIPTION);
    }

    public static Response getSuccessResponse() {
        return new Response(HttpStatus.OK, ResponseConstant.SUCCESS, ResponseConstant.OK_DESCRIPTION);
    }
    public static Response getSuccessResponse(String message) {
        return new Response(HttpStatus.OK, ResponseConstant.SUCCESS, message);
    }
    public static Response getCreatedResponse(String message){
        return new Response(HttpStatus.CREATED, ResponseConstant.SUCCESS, message);
    }

    public static Response getContinueResponse(String message) {
        return new Response(HttpStatus.OK, ResponseConstant.CONTINUE, message);
    }


    public static Response getUpdatedResponse(String message){
        return new Response(HttpStatus.OK, ResponseConstant.UPDATED, message);
    }

    public static Response getDeletedResponse(String message){
        return new Response(HttpStatus.OK, ResponseConstant.DELETED, message);
    }

    public static Response getInvalidRequestResponse(){
        return new Response(HttpStatus.BAD_REQUEST, ResponseConstant.ERROR, ResponseConstant.INVALID_REQUEST_DESCRIPTION);
    }

    public static Response getInvalidRequestResponse(String message){
        return new Response(HttpStatus.BAD_REQUEST, ResponseConstant.ERROR, message);
    }

    public static Response getIllegalArgumentException(){
        return new Response(HttpStatus.PRECONDITION_FAILED, ResponseConstant.ERROR, HttpStatus.PRECONDITION_FAILED.getReasonPhrase());
    }

    public static Response getNotFoundResponse(){
        return new Response(HttpStatus.BAD_REQUEST, ResponseConstant.ERROR, ResponseConstant.NOT_FOUND_DESCRIPTION);
    }
    public static Response getNotFoundResponse(String message){
        return new Response(HttpStatus.NOT_FOUND, ResponseConstant.ERROR, message);
    }


    public static Response getAlreadyExistsResponse(){
        return new Response(HttpStatus.CONFLICT, ResponseConstant.ERROR, ResponseConstant.RECORD_ALREADY_EXISTS_DESCRIPTION);
    }

    public static Response getVerifyResponse() {
        return new Response(HttpStatus.OK, ResponseConstant.VERIFY, ResponseConstant.VERIFY_DESCRIPTION);
    }

    public static Response getErrorResponse(String message, HttpStatus statusCode) {
        return new Response(statusCode, ResponseConstant.ERROR, message);
    }

    public static Response getInternalServerErrorResponse(String message) {
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ResponseConstant.ERROR, message);
    }
    public static Response getInternalServerErrorResponse() {
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ResponseConstant.ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
    public static Response getAuthErrorResponse(String message) {
        return new Response(HttpStatus.UNAUTHORIZED, ResponseConstant.ERROR, message);
    }
}
