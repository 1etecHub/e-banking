package com.ebanking.accountservice.service;

import com.ebanking.accountservice.dto.CustomerDto;

public interface AccountService {

    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);


    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);


}
