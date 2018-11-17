package com.zdmoney.credit.blackList.service;



import javax.jws.WebService;

import com.zdmoney.credit.blackList.service.pub.RiskControlServiceSoap;

@WebService(serviceName = "RiskControlService", targetNamespace = "http://www.zendai.com/", endpointInterface = "com.zdmoney.credit.blackList.service.pub.RiskControlServiceSoap")
public class RiskControlServiceImpl
    implements RiskControlServiceSoap
{


    public boolean addEnterpriseBlackListXml(String blackListXml) {
        throw new UnsupportedOperationException();
    }

    public boolean addCustomerBlackListXml(String blackListXml) {
        throw new UnsupportedOperationException();
    }

    public String getEnterpriseBlackListXml(String enterpriseName, String legalName, String legalIdcard, String licenseCode, String telephone, String riskInfo) {
        throw new UnsupportedOperationException();
    }

    public String getCustomerBlackListXml(String custName, String idCard, String mobilePhone, String telephone, String workUnit, String riskInfo) {
        throw new UnsupportedOperationException();
    }

    public boolean updateEnterpriseBlackListXml(String blackListXml) {
        throw new UnsupportedOperationException();
    }

    public boolean updateCustomerBlackListXml(String blackListXml) {
        throw new UnsupportedOperationException();
    }

}
