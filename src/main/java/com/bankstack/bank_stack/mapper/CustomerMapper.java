package com.bankstack.bank_stack.mapper;

import com.bankstack.bank_stack.dto.CustomerCreatedResponse;
import com.bankstack.bank_stack.dto.CustomerRequest;
import com.bankstack.bank_stack.dto.CustomerResponse;
import com.bankstack.bank_stack.dto.UpdateCustomerRequest;
import com.bankstack.bank_stack.model.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerRequest request);
    CustomerResponse toResponse(Customer customer);

    /*  Behind the scene
    @Override
    public CustomerResponse toResponse(Customer customer){
        if(customer == null){
            return null;
        }
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        return response;
    */

    CustomerCreatedResponse toCreatedResponse(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromRequest(UpdateCustomerRequest request, @MappingTarget Customer customer);

    /* @MappingTarget -> it tells MapStruct update the existing customer, don't create a new customer
    * nullValuePropertyMappingStrategy -> this means if a field in a request
    * */
}
