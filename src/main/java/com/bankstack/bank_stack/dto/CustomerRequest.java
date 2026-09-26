package com.bankstack.bank_stack.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CustomerRequest {
    @NotBlank @Size(max = 80) private String firstName;
    @NotBlank @Size(max = 80) private String lastName;
    @Email @NotBlank @Size(max = 254) private String email;
//    @Pattern(regexp = "^\\+?(1-9)\\d(1,14)$",message = "E.164 format")
    private String phone;
    @NotBlank @Size(max = 512) private String address;
    @NotBlank @Size(max = 64) private String externalId;
}
