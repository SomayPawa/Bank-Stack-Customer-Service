package com.bankstack.bank_stack.service;

import com.bankstack.bank_stack.dto.CustomerCreatedResponse;
import com.bankstack.bank_stack.dto.CustomerRequest;
import com.bankstack.bank_stack.dto.CustomerResponse;
import com.bankstack.bank_stack.dto.UpdateCustomerRequest;
import com.bankstack.bank_stack.mapper.CustomerMapper;
import com.bankstack.bank_stack.model.Customer;
import com.bankstack.bank_stack.model.KycStatus;
import com.bankstack.bank_stack.repository.CustomerRepository;
import com.bankstack.bank_stack.util.Fingerprints;
import com.commons.exception.*;
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
        String email = request.getEmail();
        String fp = Fingerprints.customerCreate(request.getFirstName(),request.getLastName(),request.getEmail(),request.getPhone(),request.getAddress());

        // Fast Path same externalId

        Optional<Customer> byExt = repository.findByExternalId(externalId);
        if(byExt.isPresent()){
            Customer ex = byExt.get();
            if(fp.equals(ex.getRequestFingerprint())){
                return mapper.toCreatedResponse(ex);  // idempotent key present
            }
            throw new ConflictException("Same External Id is used for different data");
        }

        // Fast Path same emailid

        Optional<Customer> byEmail = repository.findByEmail(email);
        if(byEmail.isPresent()){
            Customer ex = byEmail.get();
            if(fp.equals(ex.getRequestFingerprint())){
                return mapper.toCreatedResponse(ex);  // idempotent key present
            }
            throw new ConflictException("Same Email Id is used for different data");
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

    public Integer updateKycStatus(String id,String KycStatus){
        Customer c = repository.findByExternalId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with externalID: "+id));

        if("VERIFIED".equalsIgnoreCase(KycStatus)){
            c.setKycStatus(com.bankstack.bank_stack.model.KycStatus.VERIFIED);
            c.setActive(true);
            // create customer login
            repository.save(c);
        }else{
            c.setKycStatus(com.bankstack.bank_stack.model.KycStatus.REJECTED);
            repository.save(c);
        }
        return c.getVersion();
    }

    public CustomerResponse getByExternalId(String externalId){
        Customer customer = repository.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Not found with externalId: "+externalId));
        return mapper.toResponse(customer);
    }

    public boolean exists(String externalId){
        return repository.findByExternalId(externalId).isPresent();
    }

    public boolean existsByEmail(String email){
        return repository.findByEmail(email).isPresent();
    }


    public Integer updateCustomer(String id, UpdateCustomerRequest request, Integer expected) {
        Customer c = repository.findByExternalId(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not Found: "+id));

        if(expected == null){
            throw new PreconditionRequiredException("If-Match header required");
        }
        if(!expected.equals(c.getVersion())){
            throw new VersionMismatchException("Stale Version. Current= "+c.getVersion() + ", If-Match= "+expected);
        }

        mapper.updateCustomerFromRequest(request,c);
        Customer saved = repository.save(c); // hibernate increate the version
        return mapper.toResponse(saved).getVersion();
    }
}
