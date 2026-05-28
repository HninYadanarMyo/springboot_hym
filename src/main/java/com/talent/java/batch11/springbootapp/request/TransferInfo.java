package com.talent.java.batch11.springbootapp.request;

import lombok.Data;

@Data
public class TransferInfo {
    private String receiverPhone;
    private double amount;
    private String password;

}
