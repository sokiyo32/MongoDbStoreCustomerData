package com.mongodb.starter.models;

import org.bson.types.ObjectId;

 
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
 
 
 
public class CustomerParameter {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String temperaturerequestedbytheuser;
    private String departuretimeforEV;
    private String  stateofchargeforstaticbatteries;
    private String commitmentlevelwithVPP;
    private String maximumdischargeallowedonthebattery;
	
    public CustomerParameter() {
    	
    }
    
	 

	public CustomerParameter(ObjectId id, String temperaturerequestedbytheuser, String departuretimeforEV,
			String stateofchargeforstaticbatteries, String commitmentlevelwithVPP,
			String maximumdischargeallowedonthebattery) {
		super();
		this.id = id;
		this.temperaturerequestedbytheuser = temperaturerequestedbytheuser;
		this.departuretimeforEV = departuretimeforEV;
		this.stateofchargeforstaticbatteries = stateofchargeforstaticbatteries;
		this.commitmentlevelwithVPP = commitmentlevelwithVPP;
		this.maximumdischargeallowedonthebattery = maximumdischargeallowedonthebattery;
	}



	public ObjectId getId() {
		return id;
	}


	public CustomerParameter setId(ObjectId id) {
		this.id = id;
		return this;
	}


	public String getTemperaturerequestedbytheuser() {
		return temperaturerequestedbytheuser;
	}
	public CustomerParameter setTemperaturerequestedbytheuser(String temperaturerequestedbytheuser) {
		this.temperaturerequestedbytheuser = temperaturerequestedbytheuser;
		return this;
	}
	public String getDeparturetimeforEV() {
		return departuretimeforEV;
	}
	public CustomerParameter setDeparturetimeforEV(String departuretimeforEV) {
		this.departuretimeforEV = departuretimeforEV;
		return this;
	}
	public String getStateofchargeforstaticbatteries() {
		return stateofchargeforstaticbatteries;
	}
	public CustomerParameter setStateofchargeforstaticbatteries(String stateofchargeforstaticbatteries) {
		this.stateofchargeforstaticbatteries = stateofchargeforstaticbatteries;
		return this;
	}
	

	public String getCommitmentlevelwithVPP() {
		return commitmentlevelwithVPP;
	}



	public CustomerParameter setCommitmentlevelwithVPP(String commitmentlevelwithVPP) {
		this.commitmentlevelwithVPP = commitmentlevelwithVPP;
		return this;
	}



	public String getMaximumdischargeallowedonthebattery() {
		return maximumdischargeallowedonthebattery;
	}



	public CustomerParameter setMaximumdischargeallowedonthebattery(String maximumdischargeallowedonthebattery) {
		this.maximumdischargeallowedonthebattery = maximumdischargeallowedonthebattery;
		return this;
	}



	@Override
	public String toString() {
		return "Comp_Param [id=" + id + ", temperaturerequestedbytheuser=" + temperaturerequestedbytheuser
				+ ", departuretimeforEV=" + departuretimeforEV + ", stateofchargeforstaticbatteries="
				+ stateofchargeforstaticbatteries + ", commitmentlevelwithVPP=" + commitmentlevelwithVPP
				+ ", maximumdischargeallowedonthebattery=" + maximumdischargeallowedonthebattery + "]";
	}



	 


	 
    

	
}