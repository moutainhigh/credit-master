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
 *         &lt;element name="GetEnterpriseBlackListXmlResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getEnterpriseBlackListXmlResult" })
@XmlRootElement(name = "GetEnterpriseBlackListXmlResponse")
public class GetEnterpriseBlackListXmlResponse {

	@XmlElement(name = "GetEnterpriseBlackListXmlResult")
	protected String getEnterpriseBlackListXmlResult;

	/**
	 * Gets the value of the getEnterpriseBlackListXmlResult property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGetEnterpriseBlackListXmlResult() {
		return getEnterpriseBlackListXmlResult;
	}

	/**
	 * Sets the value of the getEnterpriseBlackListXmlResult property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGetEnterpriseBlackListXmlResult(String value) {
		this.getEnterpriseBlackListXmlResult = value;
	}

}
