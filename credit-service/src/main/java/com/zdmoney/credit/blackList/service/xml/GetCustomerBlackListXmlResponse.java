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
 *         &lt;element name="GetCustomerBlackListXmlResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getCustomerBlackListXmlResult" })
@XmlRootElement(name = "GetCustomerBlackListXmlResponse")
public class GetCustomerBlackListXmlResponse {

	@XmlElement(name = "GetCustomerBlackListXmlResult")
	protected String getCustomerBlackListXmlResult;

	/**
	 * Gets the value of the getCustomerBlackListXmlResult property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGetCustomerBlackListXmlResult() {
		return getCustomerBlackListXmlResult;
	}

	/**
	 * Sets the value of the getCustomerBlackListXmlResult property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGetCustomerBlackListXmlResult(String value) {
		this.getCustomerBlackListXmlResult = value;
	}

}
