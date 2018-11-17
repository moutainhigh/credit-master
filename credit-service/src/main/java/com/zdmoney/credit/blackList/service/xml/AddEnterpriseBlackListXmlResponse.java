package com.zdmoney.credit.blackList.service.xml;


import javax.xml.bind.annotation.*;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddEnterpriseBlackListXmlResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "addEnterpriseBlackListXmlResult" })
@XmlRootElement(name = "AddEnterpriseBlackListXmlResponse")
public class AddEnterpriseBlackListXmlResponse {

	@XmlElement(name = "AddEnterpriseBlackListXmlResult")
	protected boolean addEnterpriseBlackListXmlResult;

	/**
	 * Gets the value of the addEnterpriseBlackListXmlResult property.
	 * 
	 */
	public boolean isAddEnterpriseBlackListXmlResult() {
		return addEnterpriseBlackListXmlResult;
	}

	/**
	 * Sets the value of the addEnterpriseBlackListXmlResult property.
	 * 
	 */
	public void setAddEnterpriseBlackListXmlResult(boolean value) {
		this.addEnterpriseBlackListXmlResult = value;
	}

}
