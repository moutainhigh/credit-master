package com.zdmoney.credit.blackList.service.xml;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name="custName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idCard" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mobilePhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telephone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="workUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="riskInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "custName", "idCard", "mobilePhone",
		"telephone", "workUnit", "riskInfo" })
@XmlRootElement(name = "GetCustomerBlackListXml")
public class GetCustomerBlackListXml {

	protected String custName;
	protected String idCard;
	protected String mobilePhone;
	protected String telephone;
	protected String workUnit;
	protected String riskInfo;

	/**
	 * Gets the value of the custName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCustName() {
		return custName;
	}

	/**
	 * Sets the value of the custName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCustName(String value) {
		this.custName = value;
	}

	/**
	 * Gets the value of the idCard property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdCard() {
		return idCard;
	}

	/**
	 * Sets the value of the idCard property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIdCard(String value) {
		this.idCard = value;
	}

	/**
	 * Gets the value of the mobilePhone property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * Sets the value of the mobilePhone property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMobilePhone(String value) {
		this.mobilePhone = value;
	}

	/**
	 * Gets the value of the telephone property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * Sets the value of the telephone property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTelephone(String value) {
		this.telephone = value;
	}

	/**
	 * Gets the value of the workUnit property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getWorkUnit() {
		return workUnit;
	}

	/**
	 * Sets the value of the workUnit property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setWorkUnit(String value) {
		this.workUnit = value;
	}

	/**
	 * Gets the value of the riskInfo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRiskInfo() {
		return riskInfo;
	}

	/**
	 * Sets the value of the riskInfo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRiskInfo(String value) {
		this.riskInfo = value;
	}

}
