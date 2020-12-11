package com.mongodb.starter.controllers;

import com.mongodb.starter.models.CustomerParameter;
import com.mongodb.starter.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static java.util.Arrays.asList;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerRepository customerRepository;
    
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping("Customer")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerParameter postCustomer(@RequestBody CustomerParameter customerParameter) {
        return customerRepository.save(customerParameter);
    }

    @PostMapping("Customers")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CustomerParameter> postCustomers(@RequestBody List<CustomerParameter> customerParameters) {
        return customerRepository.saveAll(customerParameters);
    }

    @GetMapping("Customers")
    public List<CustomerParameter> getCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("Customer/{id}")
    public ResponseEntity<CustomerParameter> getCustomer(@PathVariable String id) {
        CustomerParameter customerParameter = customerRepository.findOne(id);
        if (customerParameter == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(customerParameter);
    }

    @GetMapping("Customers/{ids}")
    public List<CustomerParameter> getCustomers(@PathVariable String ids) {
        List<String> listIds = asList(ids.split(","));
        return customerRepository.findAll(listIds);
    }

    @GetMapping("Customers/count")
    public Long getCount() {
        return customerRepository.count();
    }

    @DeleteMapping("Customer/{id}")
    public Long deleteCustomer(@PathVariable String id) {
        return customerRepository.delete(id);
    }

    @DeleteMapping("Customers/{ids}")
    public Long deleteCustomers(@PathVariable String ids) {
        List<String> listIds = asList(ids.split(","));
        return customerRepository.delete(listIds);
    }

    @DeleteMapping("Customers")
    public Long deleteCustomers() {
        return customerRepository.deleteAll();
    }

    @PutMapping("Customer")
    public CustomerParameter putCustomer(@RequestBody CustomerParameter customerParameter) {
        return customerRepository.update(customerParameter);
    }

    @PutMapping("Customers")
    public Long putCustomer(@RequestBody List<CustomerParameter> customerParameters) {
        return customerRepository.update(customerParameters);
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Exception handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return e;
    }

	 
}
