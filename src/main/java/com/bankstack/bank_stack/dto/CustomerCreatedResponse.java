package com.bankstack.bank_stack.dto;

import com.bankstack.bank_stack.model.KycStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCreatedResponse {
    private String externalId;
    private KycStatus kycStatus;
    private Integer version;
    private LocalDateTime createdAt;
}
