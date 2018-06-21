package com.app.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LoadTenderData")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoadTenderData {
	
	@XmlElement(name = "CarrierCode")
	private String carrierCode;
	
	@XmlElement(name = "SystemLoadID")
	private String systemLoadID;
	
	@XmlElement(name = "CurrentLoadOperationalStatusEnumVal")
	private String currentLoadOperationalStatusEnumVal;
	//
	@XmlElement(name = "TenderAcceptedBy")
	private String tenderAcceptedBy;
	
	@XmlElement(name = "TrackingNumber")
	private String trackingNumber;

	
	
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getSystemLoadID() {
		return systemLoadID;
	}

	public void setSystemLoadID(String systemLoadID) {
		this.systemLoadID = systemLoadID;
	}

	public String getCurrentLoadOperationalStatusEnumVal() {
		return currentLoadOperationalStatusEnumVal;
	}

	public void setCurrentLoadOperationalStatusEnumVal(String currentLoadOperationalStatusEnumVal) {
		this.currentLoadOperationalStatusEnumVal = currentLoadOperationalStatusEnumVal;
	}

	public String getTenderAcceptedBy() {
		return tenderAcceptedBy;
	}

	public void setTenderAcceptedBy(String tenderAcceptedBy) {
		this.tenderAcceptedBy = tenderAcceptedBy;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	@Override
	public String toString() {
		return "ClassPojo [CarrierCode = " + carrierCode + ", TenderAcceptedBy = " + tenderAcceptedBy
				+ ", TrackingNumber = " + trackingNumber + ", SystemLoadID = " + systemLoadID + "]";

	}
}
