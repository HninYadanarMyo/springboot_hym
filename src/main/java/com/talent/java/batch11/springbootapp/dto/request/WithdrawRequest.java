package com.talent.java.batch11.springbootapp.dto.request;

import lombok.Data;

@Data
public class WithdrawRequest {
    String accountId;
    int amount;
}