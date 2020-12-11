package com.mongodb.starter;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.starter.models.CustomerParameter;
import com.mongodb.starter.repositories.CustomerRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerIT {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate rest;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TestHelper testHelper;
    private String URL;

    @Autowired
    CustomerControllerIT(MongoClient mongoClient) {
        createComp_ParamCollectionIfNotPresent(mongoClient);
    }

    @PostConstruct
    void setUp() {
        URL = "http://localhost:" + port + "/api";
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @DisplayName("POST /CustomerParameter  with 1 Customer")
    @Test
    void postCustomer() {
        // GIVEN
        // WHEN
        ResponseEntity<CustomerParameter> result = rest.postForEntity(URL + "/Customer", testHelper.getCustomer1(), CustomerParameter.class);
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        CustomerParameter personResult = result.getBody();
        assertThat(personResult.getId()).isNotNull();
        assertThat(personResult).isEqualToIgnoringGivenFields(testHelper.getCustomer1(), "id", "createdAt");
    }

    @DisplayName("POST /CustomerParameter  with 2 Customers")
    @Test
    void postCustomers() {
        // GIVEN
        // WHEN
        HttpEntity<List<CustomerParameter>> body = new HttpEntity<>(testHelper.getListCustomerALL());
        ResponseEntity<List<CustomerParameter>> response = rest.exchange(URL + "/Customers", HttpMethod.
                POST, body, new ParameterizedTypeReference<List<CustomerParameter>>() {
        });
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).usingElementComparatorIgnoringFields("id", "createdAt")
                                      .containsExactlyInAnyOrderElementsOf(testHelper.getListCustomerALL());
    }

    @DisplayName("GET /CustomerParameter  with 2 Customers")
    @Test
    void getCustomers() {
        // GIVEN
        List<CustomerParameter> personsInserted = customerRepository.saveAll(testHelper.getListCustomerALL());
        // WHEN
        ResponseEntity<List<CustomerParameter>> result = rest.exchange(URL + "/Customers", HttpMethod.GET, null,
                                                            new ParameterizedTypeReference<List<CustomerParameter>>() {
                                                            });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactlyInAnyOrderElementsOf(personsInserted);
    }

    @DisplayName("GET /Customer/{id}")
    @Test
    void getCustomerById() {
        // GIVEN
        CustomerParameter personInserted = customerRepository.save(testHelper.getCustomer1());
        ObjectId idInserted = personInserted.getId();
        // WHEN
        ResponseEntity<CustomerParameter> result = rest.getForEntity(URL + "/Customer/" + idInserted, CustomerParameter.class);
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(personInserted);
    }

    @DisplayName("GET /Customers/{ids}")
    @Test
    void getCustomersByIds() {
        // GIVEN
        List<CustomerParameter> personsInserted = customerRepository.saveAll(testHelper.getListCustomerALL());
        List<String> idsInserted = personsInserted.stream().map(CustomerParameter::getId).map(ObjectId::toString).collect(toList());
        // WHEN
        String url = URL + "/Customers/" + String.join(",", idsInserted);
        ResponseEntity<List<CustomerParameter>> result = rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CustomerParameter>>() {
        });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactlyInAnyOrderElementsOf(personsInserted);
    }

    @DisplayName("GET /Customers/count")
    @Test
    void getCount() {
        // GIVEN
        customerRepository.saveAll(testHelper.getListCustomerALL());
        // WHEN
        ResponseEntity<Long> result = rest.getForEntity(URL + "/Customers/count", Long.class);
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(2L);
    }

    @DisplayName("DELETE /Customer/{id}")
    @Test
    void deleteCustomerById() {
        // GIVEN
        CustomerParameter personInserted = customerRepository.save(testHelper.getCustomer2());
        ObjectId idInserted = personInserted.getId();
        // WHEN
        ResponseEntity<Long> result = rest.exchange(URL + "/Customer/" + idInserted.toString(), HttpMethod.DELETE, null,
                                                    new ParameterizedTypeReference<Long>() {
                                                    });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(1L);
        assertThat(customerRepository.count()).isEqualTo(0L);
    }

    @DisplayName("DELETE /Customers/{ids}")
    @Test
    void deleteCustomersByIds() {
        // GIVEN
        List<CustomerParameter> personsInserted = customerRepository.saveAll(testHelper.getListCustomerALL());
        List<String> idsInserted = personsInserted.stream().map(CustomerParameter::getId).map(ObjectId::toString).collect(toList());
        // WHEN
        ResponseEntity<Long> result = rest.exchange(URL + "/Customers/" + String.join(",", idsInserted), HttpMethod.DELETE, null,
                                                    new ParameterizedTypeReference<Long>() {
                                                    });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(2L);
        assertThat(customerRepository.count()).isEqualTo(0L);
    }

    @DisplayName("DELETE /Customers")
    @Test
    void deleteCustomers() {
        // GIVEN
        customerRepository.saveAll(testHelper.getListCustomerALL());
        // WHEN
        ResponseEntity<Long> result = rest.exchange(URL + "/Customers", HttpMethod.DELETE, null,
                                                    new ParameterizedTypeReference<Long>() {
                                                    });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(2L);
        assertThat(customerRepository.count()).isEqualTo(0L);
    }

    @DisplayName("PUT /Customer")
    @Test
    void putCustomer() {
        // GIVEN
        CustomerParameter clientInserted = customerRepository.save(testHelper.getCustomer2());
        // WHEN
       
        HttpEntity<CustomerParameter> body = new HttpEntity<>(clientInserted);
        ResponseEntity<CustomerParameter> result = rest.exchange(URL + "/Customer", HttpMethod.PUT, body, new ParameterizedTypeReference<CustomerParameter>() {
        });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(customerRepository.findOne(clientInserted.getId().toString()));
       
        assertThat(customerRepository.count()).isEqualTo(1L);
    }

    @DisplayName("PUT /CustomerParameter  with 2 Customers")
    @Test
    void putCustomers() {
        // GIVEN
        List<CustomerParameter> comp_paramsInserted = customerRepository.saveAll(testHelper.getListCustomerALL());
        
        HttpEntity<List<CustomerParameter>> body = new HttpEntity<>(comp_paramsInserted);
        ResponseEntity<Long> result = rest.exchange(URL + "/Customers", HttpMethod.PUT, body, new ParameterizedTypeReference<Long>() {
        });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(2L);
         
        
        assertThat(customerRepository.count()).isEqualTo(2L);
    }

    

    private void createComp_ParamCollectionIfNotPresent(MongoClient mongoClient) {
        // This is required because it is not possible to create a new collection within a multi-documents transaction.
        // Some tests start by inserting 2 documents with a transaction.
        MongoDatabase db = mongoClient.getDatabase("test");
        if (!db.listCollectionNames().into(new ArrayList<>()).contains("CustomerParameter"))
            db.createCollection("CustomerParameter");
    }
}
