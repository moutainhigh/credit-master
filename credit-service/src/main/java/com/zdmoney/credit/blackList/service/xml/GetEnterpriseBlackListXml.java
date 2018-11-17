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
 *         &lt;element name="enterpriseName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="legalName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="legalIdcard" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="licenseCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telephone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "", propOrder = { "enterpriseName", "legalName", "legalIdcard",
		"licenseCode", "telephone", "riskInfo" })
@XmlRootElement(name = "GetEnterpriseBlackListXml")
public class GetEnterpriseBlackListXml {

	protected String enterpriseName;
	protected String legalName;
	protected String legalIdcard;
	protected String licenseCode;
	protected String telephone;
	protected String riskInfo;

	/**
	 * Gets the value of the enterpriseName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEnterpriseName() {
		return enterpriseName;
	}

	/**
	 * Sets the value of the enterpriseName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEnterpriseName(String value) {
		this.enterpriseName = value;
	}

	/**
	 * Gets the value of the legalName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLegalName() {
		return legalName;
	}

	/**
	 * Sets the value of the legalName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLegalName(String value) {
		this.legalName = value;
	}

	/**
	 * Gets the value of the legalIdcard property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLegalIdcard() {
		return legalIdcard;
	}

	/**
	 * Sets the value of the legalIdcard property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLegalIdcard(String value) {
		this.legalIdcard = value;
	}

	/**
	 * Gets the value of the licenseCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLicenseCode() {
		return licenseCode;
	}

	/**
	 * Sets the value of the licenseCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLicenseCode(String value) {
		this.licenseCode = value;
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
