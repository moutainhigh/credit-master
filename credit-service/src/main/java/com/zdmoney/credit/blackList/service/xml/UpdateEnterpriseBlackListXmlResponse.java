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
 *         &lt;element name="UpdateEnterpriseBlackListXmlResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "updateEnterpriseBlackListXmlResult" })
@XmlRootElement(name = "UpdateEnterpriseBlackListXmlResponse")
public class UpdateEnterpriseBlackListXmlResponse {

	@XmlElement(name = "UpdateEnterpriseBlackListXmlResult")
	protected boolean updateEnterpriseBlackListXmlResult;

	/**
	 * Gets the value of the updateEnterpriseBlackListXmlResult property.
	 * 
	 */
	public boolean isUpdateEnterpriseBlackListXmlResult() {
		return updateEnterpriseBlackListXmlResult;
	}

	/**
	 * Sets the value of the updateEnterpriseBlackListXmlResult property.
	 * 
	 */
	public void setUpdateEnterpriseBlackListXmlResult(boolean value) {
		this.updateEnterpriseBlackListXmlResult = value;
	}

}
