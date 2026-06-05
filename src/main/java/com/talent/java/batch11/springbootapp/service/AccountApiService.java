package com.talent.java.batch11.springbootapp.service;

import com.talent.java.batch11.springbootapp.dto.request.LoginInfo;
import com.talent.java.batch11.springbootapp.dto.request.RegisterInfo;
import com.talent.java.batch11.springbootapp.dto.request.TransferInfo;
import com.talent.java.batch11.springbootapp.exception.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface AccountApiService {
    ResponseEntity<CommonResponse> handleLoginRequest(LoginInfo loginInfo);
    ResponseEntity<CommonResponse> getAccountByIdResponse(long id);
    ResponseEntity<CommonResponse> registerResponse(RegisterInfo registerInfo);
    ResponseEntity<CommonResponse> getAllTransactionsResponse(long accountId);
    ResponseEntity<CommonResponse> withdrawResponse(String email, int amount);
    ResponseEntity<CommonResponse> topUpResponse(String email, int amount);
    ResponseEntity<CommonResponse> depositResponse(String email, int amount);
    ResponseEntity<CommonResponse> transferResponse(String senderEmail, TransferInfo transferInfo);

    ResponseEntity<CommonResponse> getAllAccountsResponse();
}