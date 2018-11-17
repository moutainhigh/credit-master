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
 *         &lt;element name="UpdateCustomerBlackListXmlResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "updateCustomerBlackListXmlResult" })
@XmlRootElement(name = "UpdateCustomerBlackListXmlResponse")
public class UpdateCustomerBlackListXmlResponse {

	@XmlElement(name = "UpdateCustomerBlackListXmlResult")
	protected boolean updateCustomerBlackListXmlResult;

	/**
	 * Gets the value of the updateCustomerBlackListXmlResult property.
	 * 
	 */
	public boolean isUpdateCustomerBlackListXmlResult() {
		return updateCustomerBlackListXmlResult;
	}

	/**
	 * Sets the value of the updateCustomerBlackListXmlResult property.
	 * 
	 */
	public void setUpdateCustomerBlackListXmlResult(boolean value) {
		this.updateCustomerBlackListXmlResult = value;
	}

}
