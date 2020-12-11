package com.mongodb.starter.repositories;

import com.mongodb.starter.models.CustomerParameter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository {

    CustomerParameter save(CustomerParameter customerParameter);

    List<CustomerParameter> saveAll(List<CustomerParameter> customerParameters);

    List<CustomerParameter> findAll();

    List<CustomerParameter> findAll(List<String> ids);

    CustomerParameter findOne(String id);

    long count();

    long delete(String id);

    long delete(List<String> ids);

    long deleteAll();

    CustomerParameter update(CustomerParameter customerParameter);

    long update(List<CustomerParameter> customerParameters);

    

}
