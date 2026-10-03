package com.bankstack.bank_stack.controller;

import com.bankstack.bank_stack.dto.*;
import com.bankstack.bank_stack.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/customers/{id}/Kyc-status")
    public ResponseEntity<Void> updateKycStatus(@PathVariable String id, @RequestBody UpdateKycStatusRequest request){
        Integer newVersion = service.updateKycStatus(id, request.getKycStatus());

        // Return 204 No Content, Only ETag
        return ResponseEntity.noContent()
                .eTag("\"" + newVersion + "\"")
                .build();

    }

    @GetMapping("/customers/{externalId}")
    public ResponseEntity<CustomerResponse> getCustomerByExternalId(@PathVariable String externalId){
        CustomerResponse dto = service.getByExternalId(externalId);
        return ResponseEntity.ok()
                .eTag("\"" + dto.getVersion() + "\"")
                .body(dto);
    }

    @GetMapping("/customers/exists")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email){
        return ResponseEntity.ok(service.existsByEmail(email));
    }

    @GetMapping("/customers/{externalId}/exists")
    public boolean exists(@PathVariable String externalId){
        return service.exists(externalId);
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<Void> updateCustomers(
            @PathVariable String id,
            @RequestHeader(name = "If-Match",required = true) String ifMatch,
            @RequestBody UpdateCustomerRequest request){
        Integer expected = parseIfMatch(ifMatch);
        Integer newVersion = service.updateCustomer(id,request,expected);

        return ResponseEntity.ok()
                .eTag("\"" + newVersion + "\"")
                .build();
    }

    private Integer parseIfMatch(String ifMatch) {
        if(ifMatch == null || ifMatch.isBlank()){
            return null;
        }
        String v = ifMatch.replace("\"","").trim();
        return Integer.valueOf(v);
    }


}
