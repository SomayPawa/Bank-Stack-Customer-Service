package com.bankstack.bank_stack.controller;

import com.bankstack.bank_stack.dto.CustomerCreatedResponse;
import com.bankstack.bank_stack.dto.CustomerRequest;
import com.bankstack.bank_stack.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/vi")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;
    @PostMapping("/customers")
    public ResponseEntity<CustomerCreatedResponse> createCustomer(@Valid @RequestBody CustomerRequest request){
        /* @RequestBody -> convert json to java object
        *  @Valid -> validate the java object according to the validation annotations defined on its fields
        */
        CustomerCreatedResponse body = service.create(request);
        URI loc = URI.create("/api/vi/customers/"+body.getExternalId());
        return ResponseEntity.created(loc).body(body);

    }
}
