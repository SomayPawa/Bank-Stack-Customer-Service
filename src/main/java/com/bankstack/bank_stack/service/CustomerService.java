package com.bankstack.bank_stack.service;

import com.bankstack.bank_stack.dto.CustomerCreatedResponse;
import com.bankstack.bank_stack.dto.CustomerRequest;
import com.bankstack.bank_stack.mapper.CustomerMapper;
import com.bankstack.bank_stack.model.Customer;
import com.bankstack.bank_stack.model.KycStatus;
import com.bankstack.bank_stack.repository.CustomerRepository;
import com.bankstack.bank_stack.util.Fingerprints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerCreatedResponse create(CustomerRequest request){
        String externalId = request.getExternalId();
        String fp = Fingerprints.customerCreate(request.getFirstName(),request.getLastName(),request.getEmail(),request.getPhone(),request.getAddress());

        // Fast Path same externalId

        Optional<Customer> byExt = repository.findByExternalId(externalId);
        if(byExt.isPresent()){
            Customer ex = byExt.get();
            if(fp.equals(ex.getRequestFingerprint())){
                return mapper.toCreatedResponse(ex);  // idempotent key present
            }
            System.out.println("Conflict Discovered. this externalId exists");
        }

        Customer entity = mapper.toEntity(request);
        entity.setActive(false);
        entity.setKycStatus(KycStatus.PENDING);
        entity.setRequestFingerprint(fp);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // whole request + active+kyc+fingerprint
        Customer saved = repository.saveAndFlush(entity);
        return mapper.toCreatedResponse(saved);
    }
}
