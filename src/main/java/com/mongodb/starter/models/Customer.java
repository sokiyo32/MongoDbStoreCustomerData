package com.mongodb.starter.models;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Customer {
	private ObjectId id;
    private String firstName;
    private String lastName;
    private String email;
    private CustomerParameter customerParameter;
    
    public Customer() {
    	
    }

	public Customer(ObjectId id, String firstName, String lastName, String email, CustomerParameter customerParameter) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.customerParameter = customerParameter;
	}

	public ObjectId getId() {
		return id;
	}

	public Customer setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public Customer setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public Customer setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Customer setEmail(String email) {
		this.email = email;
		return this;
	}

	public CustomerParameter getCustomerParameter() {
		return customerParameter;
	}

	public void setCustomerParameter(CustomerParameter customerParameter) {
		this.customerParameter = customerParameter;
	}
    
    

}
