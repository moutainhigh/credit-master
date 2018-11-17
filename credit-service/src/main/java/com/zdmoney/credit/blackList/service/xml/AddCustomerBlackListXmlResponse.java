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
 *         &lt;element name="AddCustomerBlackListXmlResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "addCustomerBlackListXmlResult" })
@XmlRootElement(name = "AddCustomerBlackListXmlResponse")
public class AddCustomerBlackListXmlResponse {

	@XmlElement(name = "AddCustomerBlackListXmlResult")
	protected boolean addCustomerBlackListXmlResult;

	/**
	 * Gets the value of the addCustomerBlackListXmlResult property.
	 * 
	 */
	public boolean isAddCustomerBlackListXmlResult() {
		return addCustomerBlackListXmlResult;
	}

	/**
	 * Sets the value of the addCustomerBlackListXmlResult property.
	 * 
	 */
	public void setAddCustomerBlackListXmlResult(boolean value) {
		this.addCustomerBlackListXmlResult = value;
	}

}
