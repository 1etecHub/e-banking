package com.ebanking.accountservice.service;

import com.ebanking.accountservice.dto.CustomerDetailsDto;

public interface CustomerService {


    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
