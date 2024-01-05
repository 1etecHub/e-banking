package com.ebanking.accountservice.service.impl;


import com.ebanking.accountservice.dto.AccountsDto;
import com.ebanking.accountservice.dto.CardsDto;
import com.ebanking.accountservice.dto.CustomerDetailsDto;
import com.ebanking.accountservice.dto.LoansDto;
import com.ebanking.accountservice.entity.Accounts;
import com.ebanking.accountservice.entity.Customer;
import com.ebanking.accountservice.exception.ResourceNotFoundException;
import com.ebanking.accountservice.mapper.AccountsMapper;
import com.ebanking.accountservice.mapper.CustomerMapper;
import com.ebanking.accountservice.repository.AccountsRepository;
import com.ebanking.accountservice.repository.CustomerRepository;
import com.ebanking.accountservice.service.CustomerService;
import com.ebanking.accountservice.service.client.CardsFeignClient;
import com.ebanking.accountservice.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomersServiceImpl implements CustomerService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}
