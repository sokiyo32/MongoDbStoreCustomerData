package com.mongodb.starter;

 
 
import com.mongodb.starter.models.CustomerParameter;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
class TestHelper {

    CustomerParameter getCustomer1() {
        return new CustomerParameter().setTemperaturerequestedbytheuser("65f")
                           .setDeparturetimeforEV("16hour")
                            .setStateofchargeforstaticbatteries("full")
                            .setCommitmentlevelwithVPP("")
                            .setMaximumdischargeallowedonthebattery("")
                            
                           ;
    }

    CustomerParameter getCustomer2() {
    	  return new CustomerParameter().setTemperaturerequestedbytheuser("65f")
                  .setDeparturetimeforEV("16hour")
                   .setStateofchargeforstaticbatteries("full")
                   .setCommitmentlevelwithVPP("")
                   .setMaximumdischargeallowedonthebattery("")
                   
                  ;
    }

    List<CustomerParameter> getListCustomerALL() {
        return asList(getCustomer1(), getCustomer2());
    }
}
