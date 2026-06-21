package com.yuangang.settlement.customer;

import java.util.List;

import com.yuangang.settlement.audit.AuditService;
import com.yuangang.settlement.common.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AuditService auditService;

    public CustomerService(CustomerRepository customerRepository, AuditService auditService) {
        this.customerRepository = customerRepository;
        this.auditService = auditService;
    }

    @Transactional
    public CustomerResponse create(CreateCustomerRequest request) {
        customerRepository.findByEmail(request.email()).ifPresent(existing -> {
            throw new BusinessException("Customer email already exists", HttpStatus.CONFLICT);
        });

        Customer customer = customerRepository.save(new Customer(request.name(), request.email()));
        auditService.log("CUSTOMER_CREATED", "Customer", customer.getId(), customer.getEmail());
        return CustomerResponse.from(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> list() {
        return customerRepository.findAll().stream()
                .map(CustomerResponse::from)
                .toList();
    }
}
