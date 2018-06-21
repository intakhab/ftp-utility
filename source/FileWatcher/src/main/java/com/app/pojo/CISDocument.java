package com.app.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CISDocument")
@XmlAccessorType(XmlAccessType.FIELD)
public class CISDocument {
	
	@XmlElement(name="ApiHeader")
	private ApiHeader apiHeader;
	
	@XmlElement(name="LoadTenderData")
	private LoadTenderData loadTenderData;

	public ApiHeader getApiHeader() {
		return apiHeader;
	}

	public void setApiHeader(ApiHeader apiHeader) {
		this.apiHeader = apiHeader;
	}

	public LoadTenderData getLoadTenderData() {
		return loadTenderData;
	}

	public void setLoadTenderData(LoadTenderData loadTenderData) {
		this.loadTenderData = loadTenderData;
	}

	@Override
	public String toString() {
		return "ClassPojo [ApiHeader = " + apiHeader + ", LoadTenderData = " + loadTenderData + "]";
	}
}
