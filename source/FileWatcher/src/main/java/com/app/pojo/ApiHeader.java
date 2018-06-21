package com.app.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ApiHeader")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiHeader {
	
	@XmlElement(name="OperationName")
	private String operationName;
	
	
	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	@Override
	public String toString() {
		return "ClassPojo [OperationName = " + operationName + "]";
	}
}
