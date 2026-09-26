package com.bankstack.bank_stack.dto;

import lombok.Data;

@Data
public class UpdateKycStatusRequest {
    private String kycStatus; // e.g., PENDING, VERIFIED, FAILED
}
