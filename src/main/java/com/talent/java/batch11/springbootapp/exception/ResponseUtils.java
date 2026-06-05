package com.talent.java.batch11.springbootapp.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public final class ResponseUtils {

    public  static <T> ResponseEntity<CommonResponse> makeCommonResponse(
            String apiName, HttpStatusCode httpStatusCode, T data, boolean isSuccess, String message){


        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setData(data);
        commonResponse.setSuccess(isSuccess);
        commonResponse.setMessage(message);
        commonResponse.setApiName(apiName);
        commonResponse.setSystemDateTime(LocalDateTime.now());
        commonResponse.setHttpStatusCode(httpStatusCode);

        return new ResponseEntity<>(commonResponse, httpStatusCode);

    }
}