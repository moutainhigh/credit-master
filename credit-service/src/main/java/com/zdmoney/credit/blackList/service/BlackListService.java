package com.zdmoney.credit.blackList.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.blackList.dao.pub.IBlackListDao;
import com.zdmoney.credit.blackList.service.pub.IBlackListService;
import com.zdmoney.credit.blackList.service.pub.RiskControlServiceSoap;
import com.zdmoney.credit.blacklist.domain.ComBlacklist;
import com.zdmoney.credit.blacklist.domain.Customer;
import com.zdmoney.credit.blacklist.domain.Enterprise;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 
 * @author 00232949
 *
 */
@Service
public class BlackListService implements IBlackListService{
	
	private static final Logger logger = Logger.getLogger(BlackListService.class);
	
	private String webServiceURL = null;

	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ISequencesService sequencesService ;
	@Autowired
	private IBlackListDao blackListDao;
	
	private RiskControlServiceSoap soap = null;
	
    public  String getWebServiceURL(){
//    	return "http://114.80.110.32:8119/RiskControlService.asmx";
       if(webServiceURL==null){
        	String value = sysParamDefineService.getSysParamValueCache("blackList", "blackList_webservice_URL");
        	if(Strings.isEmpty(value)){
        		logger.error("同步黑名单，获取webservice地址出错，请检查配置magicType：blackList，param_key：blackList_webservice_URL");
        		throw new RuntimeException("同步黑名单，获取webservice地址出错，请检查配置magicType：blackList，param_key：blackList_webservice_URL");
        	}
        	webServiceURL= value;
        }
        return  webServiceURL;
    }
    
    /**
     * 
     * @return
     */
    private RiskControlServiceSoap getSoap() {
		if(soap == null){
			soap = new RiskControlServiceClient().getRiskControlServiceSoap(getWebServiceURL());
		}
		return soap;
	}
    
    public boolean addCustomerBlackList(List<Customer> blackList){
        Document document = DocumentHelper.createDocument();
        Element dataItems=document.addElement("CustomerBlackList");
        for(Customer elem : blackList){
        	Element dataItem=dataItems.addElement("DataItem");
            Element code= dataItem.addElement("CODE");
            if(Strings.isNotEmpty(elem.getCode())){
            	code.setText(elem.getCode());
            }
            Element name=dataItem.addElement("NAME");
            if(Strings.isNotEmpty(elem.getName())){
            	name.setText(elem.getName());
            }
            Element idCard=dataItem.addElement("IDCARD");
            if(Strings.isNotEmpty(elem.getIdCard())) {
            	idCard.setText(elem.getIdCard());
            }
            Element spouseName=dataItem.addElement("SPOUSENAME");
            if(Strings.isNotEmpty(elem.getSpouseName())) {
            	spouseName.setText(elem.getSpouseName());
            }
            Element spouseIdCard=dataItem.addElement("SPOUSEIDCARD");
            if(Strings.isNotEmpty(elem.getSpouseIdCard())) {
            	spouseIdCard.setText(elem.getSpouseIdCard());
            }
            Element mobilePhone=dataItem.addElement("MOBILEPHONE");
            if(Strings.isNotEmpty(elem.getMobilePhone())){
            	mobilePhone.setText(elem.getMobilePhone());
            }
            Element telePhone=dataItem.addElement("TELEPHONE");
            if(Strings.isNotEmpty(elem.getTelePhone())){
            	telePhone.setText(elem.getTelePhone());
            }
            Element workUnit=dataItem.addElement("WORKUNIT");
            if(Strings.isNotEmpty(elem.getWorkUnit())) {
            	workUnit.setText(elem.getWorkUnit());
            }
            Element unitAddress=dataItem.addElement("UNITADDRESS");
            if(Strings.isNotEmpty(elem.getUnitAddress())) {
            	unitAddress.setText(elem.getUnitAddress());
            }
            Element unitPhone=dataItem.addElement("UNITPHONE");
            if(Strings.isNotEmpty(elem.getUnitPhone())) {
            	unitPhone.setText(elem.getUnitPhone());
            }
            Element riskLevel=dataItem.addElement("RISKLEVEL");
            if(Strings.isNotEmpty(elem.getRiskLevel())) {
            	riskLevel.setText(elem.getRiskLevel());
            }
            Element riskCase=dataItem.addElement("RISKCAUSE");
            if(Strings.isNotEmpty(elem.getRiskCase())) {
            	riskCase.setText(elem.getRiskCase());
            }
            Element riskTime=dataItem.addElement("RISKTIME");
            if(Strings.isNotEmpty(elem.getRiskTime())) {
            	riskTime.setText(elem.getRiskTime());
            }
            Element infoSource=dataItem.addElement("INFOSOURCE");
            if(Strings.isNotEmpty(elem.getInfoSource())) {
            	infoSource.setText(elem.getInfoSource());
            }
            Element createOrgan=dataItem.addElement("CREATEORGAN");
            if(Strings.isNotEmpty(elem.getCreateOrgan())){
            	createOrgan.setText(elem.getCreateOrgan());
            }
            Element creator=dataItem.addElement("CREATOR");
            if(Strings.isNotEmpty(elem.getCreator())) {
            	creator.setText(elem.getCreator());
            }
        }
        logger.debug("addCustomerBlackList: "+document.asXML());
        return getSoap().addCustomerBlackListXml(document.asXML());
    }


	public boolean addEnterpriseBlackList(List<Enterprise> blackList){
        Document document = DocumentHelper.createDocument();
        Element dataItems=document.addElement("EnterpriseBlackList");
        for(Enterprise elem : blackList){
        	Element dataItem=dataItems.addElement("DataItem");
        	
            Element name=dataItem.addElement("NAME");
            name.setText(elem.getName()==null?"":elem.getName());
            Element legalName=dataItem.addElement("LEGALNAME");
            legalName.setText(elem.getLegalName()==null?"":elem.getLegalName());
            Element legalIdCard=dataItem.addElement("LEGALIDCARD");
            legalIdCard.setText(elem.getLegalIdCard()==null?"":elem.getLegalIdCard());
            Element address=dataItem.addElement("ADDRESS");
            address.setText(elem.getAddress()==null?"":elem.getAddress());
            Element licenseCode=dataItem.addElement("LICENSECODE");
            licenseCode.setText(elem.getLicenseCode()==null?"":elem.getLicenseCode());
            Element telePhone=dataItem.addElement("TELEPHONE");
            telePhone.setText(elem.getTelePhone()==null?"":elem.getTelePhone());
            Element riskLevel=dataItem.addElement("RISKLEVEL");
            riskLevel.setText(elem.getRiskLevel()==null?"":elem.getRiskLevel());
            Element riskCase=dataItem.addElement("RISKCAUSE");
            riskCase.setText(elem.getRiskCase()==null?"":elem.getRiskCase());
            Element riskTime=dataItem.addElement("RISKTIME");
            riskTime.setText(elem.getRiskTime()==null?"":elem.getRiskTime());
            Element infoSource=dataItem.addElement("INFOSOURCE");
            infoSource.setText(elem.getInfoSource()==null?"":elem.getInfoSource());
            Element createOrgan=dataItem.addElement("CREATEORGAN");
            createOrgan.setText(elem.getCreateOrgan()==null?"":elem.getCreateOrgan());
            Element creator=dataItem.addElement("CREATOR");
            creator.setText(elem.getCreator()==null?"":elem.getCreator());
        }
        logger.debug("addEnterpriseBlackList: "+document.asXML());
        
        return getSoap().addEnterpriseBlackListXml(document.asXML());
    }

    boolean updateCustomerBlackList(List<Customer> blackList){
        Document document = DocumentHelper.createDocument();
        Element dataItems=document.addElement("CustomerBlackList");
        for(Customer elem : blackList){
        	Element dataItem=dataItems.addElement("DataItem");
            Element guId= dataItem.addElement("GUID");
            if(Strings.isNotEmpty(elem.getGuId())){
            	guId.setText(elem.getGuId());
            }
            Element code= dataItem.addElement("CODE");
            if(Strings.isNotEmpty(elem.getCode())){
            	code.setText(elem.getCode());
            }
            Element name=dataItem.addElement("NAME");
            if(Strings.isNotEmpty(elem.getName())){
            	name.setText(elem.getName());
            }
            Element idCard=dataItem.addElement("IDCARD");
            if(Strings.isNotEmpty(elem.getIdCard())) {
            	idCard.setText(elem.getIdCard());
            }
            Element spouseName=dataItem.addElement("SPOUSENAME");
            if(Strings.isNotEmpty(elem.getSpouseName())) {
            	spouseName.setText(elem.getSpouseName());
            }
            Element spouseIdCard=dataItem.addElement("SPOUSEIDCARD");
            if(Strings.isNotEmpty(elem.getSpouseIdCard())) {
            	spouseIdCard.setText(elem.getSpouseIdCard());
            }
            Element mobilePhone=dataItem.addElement("MOBILEPHONE");
            if(Strings.isNotEmpty(elem.getMobilePhone())){
            	mobilePhone.setText(elem.getMobilePhone());
            }
            Element telePhone=dataItem.addElement("TELEPHONE");
            if(Strings.isNotEmpty(elem.getTelePhone())){
            	telePhone.setText(elem.getTelePhone());
            }
            Element workUnit=dataItem.addElement("WORKUNIT");
            if(Strings.isNotEmpty(elem.getWorkUnit())) {
            	workUnit.setText(elem.getWorkUnit());
            }
            Element unitAddress=dataItem.addElement("UNITADDRESS");
            if(Strings.isNotEmpty(elem.getUnitAddress())) {
            	unitAddress.setText(elem.getUnitAddress());
            }
            Element unitPhone=dataItem.addElement("UNITPHONE");
            if(Strings.isNotEmpty(elem.getUnitPhone())) {
            	unitPhone.setText(elem.getUnitPhone());
            }
            Element riskLevel=dataItem.addElement("RISKLEVEL");
            if(Strings.isNotEmpty(elem.getRiskLevel())) {
            	riskLevel.setText(elem.getRiskLevel());
            }
            Element riskCase=dataItem.addElement("RISKCAUSE");
            if(Strings.isNotEmpty(elem.getRiskCase())) {
            	riskCase.setText(elem.getRiskCase());
            }
            Element riskTime=dataItem.addElement("RISKTIME");
            if(Strings.isNotEmpty(elem.getRiskTime())) {
            	riskTime.setText(elem.getRiskTime());
            }
            Element infoSource=dataItem.addElement("INFOSOURCE");
            if(Strings.isNotEmpty(elem.getInfoSource())) {
            	infoSource.setText(elem.getInfoSource());
            }
            Element createOrgan=dataItem.addElement("CREATEORGAN");
            if(Strings.isNotEmpty(elem.getCreateOrgan())){
            	createOrgan.setText(elem.getCreateOrgan());
            }
            Element creator=dataItem.addElement("CREATOR");
            if(Strings.isNotEmpty(elem.getCreator())) {
            	creator.setText(elem.getCreator());
            }
        }
        return getSoap().updateCustomerBlackListXml(document.asXML());
    }


    boolean updateEnterpriseBlackList(List<Enterprise> blackList){
        Document document = DocumentHelper.createDocument();
        Element dataItems=document.addElement("EnterpriseBlackList");
        
        for(Enterprise elem : blackList){
        	Element dataItem=dataItems.addElement("DataItem");
        	
            Element guId=dataItem.addElement("GUID");
            guId.setText(elem.getGuId()==null?"":elem.getGuId());
        	Element name=dataItem.addElement("NAME");
            name.setText(elem.getName()==null?"":elem.getName());
            Element legalName=dataItem.addElement("LEGALNAME");
            legalName.setText(elem.getLegalName()==null?"":elem.getLegalName());
            Element legalIdCard=dataItem.addElement("LEGALIDCARD");
            legalIdCard.setText(elem.getLegalIdCard()==null?"":elem.getLegalIdCard());
            Element address=dataItem.addElement("ADDRESS");
            address.setText(elem.getAddress()==null?"":elem.getAddress());
            Element licenseCode=dataItem.addElement("LICENSECODE");
            licenseCode.setText(elem.getLicenseCode()==null?"":elem.getLicenseCode());
            Element telePhone=dataItem.addElement("TELEPHONE");
            telePhone.setText(elem.getTelePhone()==null?"":elem.getTelePhone());
            Element riskLevel=dataItem.addElement("RISKLEVEL");
            riskLevel.setText(elem.getRiskLevel()==null?"":elem.getRiskLevel());
            Element riskCase=dataItem.addElement("RISKCAUSE");
            riskCase.setText(elem.getRiskCase()==null?"":elem.getRiskCase());
            Element riskTime=dataItem.addElement("RISKTIME");
            riskTime.setText(elem.getRiskTime()==null?"":elem.getRiskTime());
            Element infoSource=dataItem.addElement("INFOSOURCE");
            infoSource.setText(elem.getInfoSource()==null?"":elem.getInfoSource());
            Element createOrgan=dataItem.addElement("CREATEORGAN");
            createOrgan.setText(elem.getCreateOrgan()==null?"":elem.getCreateOrgan());
            Element creator=dataItem.addElement("CREATOR");
            creator.setText(elem.getCreator()==null?"":elem.getCreator());
        }
        
        return getSoap().updateEnterpriseBlackListXml(document.asXML());
    }

   public List<Customer> getCustomerBlackList(String custName, String idCard, String mobilePhone, String telephone, String workUnit, String riskInfo) throws DocumentException{
        List<Customer> customerList=new ArrayList<Customer>();
        String xmlStr= getSoap().getCustomerBlackListXml(custName,idCard,mobilePhone,telephone,workUnit,riskInfo);
        Document document = DocumentHelper.parseText(xmlStr);
        Element rootElt = document.getRootElement();
        Iterator iter = rootElt.elementIterator("DataItem");
        while (iter.hasNext()) {
        	Customer customer=new Customer();
            Element recordEle = (Element) iter.next();
            customer.setGuId(recordEle.elementTextTrim("GUID"));
            customer.setCode(recordEle.elementTextTrim("CODE"));
            customer.setName(recordEle.elementTextTrim("NAME"));
            customer.setIdCard(recordEle.elementTextTrim("IDCARD"));
            customer.setSpouseName(recordEle.elementTextTrim("SPOUSENAME"));
            customer.setSpouseIdCard(recordEle.elementTextTrim("SPOUSEIDCARD"));
            customer.setMobilePhone(recordEle.elementTextTrim("MOBILEPHONE"));
            customer.setTelePhone(recordEle.elementTextTrim("TELEPHONE"));
            customer.setWorkUnit(recordEle.elementTextTrim("WORKUNIT"));
            customer.setUnitAddress(recordEle.elementTextTrim("UNITADDRESS"));
            customer.setUnitPhone(recordEle.elementTextTrim("UNITPHONE"));
            customer.setRiskLevel(recordEle.elementTextTrim("RISKLEVEL"));
            customer.setRiskCase(recordEle.elementTextTrim("RISKCAUSE"));
            customer.setRiskTime(recordEle.elementTextTrim("RISKTIME"));
            customer.setInfoSource(recordEle.elementTextTrim("INFOSOURCE"));
            customer.setCreateOrgan(recordEle.elementTextTrim("CREATEORGAN"));
            customerList.add(customer);
        }
        return   customerList;
    }

   public List<Enterprise> getEnterpriseBlackList(String enterpriseName, String legalName, String legalIdcard, String licenseCode, String telephone, String riskInfo) throws DocumentException{
        List<Enterprise> enterpriseList=new ArrayList<Enterprise>();
        String xmlStr= getSoap().getEnterpriseBlackListXml(enterpriseName,legalName,legalIdcard,licenseCode,telephone,riskInfo);
        Document document = DocumentHelper.parseText(xmlStr);
        Element rootElt = document.getRootElement();
        Iterator iter = rootElt.elementIterator("DataItem");
        while (iter.hasNext()) {
        	Enterprise enterprise=new Enterprise();
            Element recordEle = (Element) iter.next();
            enterprise.setGuId(recordEle.elementTextTrim("GUID"));
            enterprise.setName(recordEle.elementTextTrim("NAME"));
            enterprise.setLegalName(recordEle.elementTextTrim("LEGALNAME"));
            enterprise.setLegalIdCard(recordEle.elementTextTrim("LEGALIDCARD"));
            enterprise.setAddress(recordEle.elementTextTrim("ADDRESS"));
            enterprise.setLicenseCode(recordEle.elementTextTrim("LICENSECODE"));
            enterprise.setTelePhone(recordEle.elementTextTrim("TELEPHONE"));
            enterprise.setRiskLevel(recordEle.elementTextTrim("RISKLEVEL"));
            enterprise.setRiskLevel(recordEle.elementTextTrim("RISKCAUSE"));
            enterprise.setRiskTime(recordEle.elementTextTrim("RISKTIME"));
            enterprise.setInfoSource(recordEle.elementTextTrim("INFOSOURCE"));
            enterprise.setCreateOrgan(recordEle.elementTextTrim("CREATEORGAN"));
            enterpriseList.add(enterprise);
        }
        return   enterpriseList;
    }


	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void createComBlacklist(Customer customer) {
		ComBlacklist blackList = new ComBlacklist();
		blackList.setId(sequencesService.getSequences(SequencesEnum.COM_BLACKLIST));
		blackList.setComeFrome("外部导入");
		blackList.setName(customer.getName());
		blackList.setIdnum(customer.getIdCard());
		blackList.setCompany(customer.getWorkUnit());
		blackList.setMphone(customer.getMobilePhone());
		blackList.setTel(customer.getTelePhone());
		blackList.setCanSubmitRequestDays(-1l);
		blackList.setLoanType("all");
		blackList.setMemo(customer.getRiskCase());
		blackList.setRejectDate(Strings.isNotEmpty(customer.getRiskTime())?Dates.parse(customer.getRiskTime(), "yyyy/MM/dd HH:mm:ss"):new Date());
		blackList.setOperatorId(null);
		blackList.setCreateTime(new Date());
		blackListDao.insert(blackList);
		
	}

	@Override
	public ComBlacklist findByNameAndIdnum(String name, String idCard) {
		ComBlacklist comBlacklist = new ComBlacklist();
		comBlacklist.setName(name);
		comBlacklist.setIdnum(idCard);
		List<ComBlacklist> list = blackListDao.findListByVo(comBlacklist);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void createComBlacklist(Enterprise enterprise) {
		ComBlacklist blackList = new ComBlacklist();
		blackList.setId(sequencesService.getSequences(SequencesEnum.COM_BLACKLIST));
		blackList.setComeFrome("外部导入");
		blackList.setName(enterprise.getLegalName());
		blackList.setIdnum(enterprise.getLegalIdCard());
		blackList.setCompany(enterprise.getName());
		blackList.setTel(enterprise.getTelePhone());
		blackList.setCanSubmitRequestDays(-1l);
		blackList.setLoanType("all");
		blackList.setMemo(enterprise.getRiskCase());
		blackList.setRejectDate(Strings.isNotEmpty(enterprise.getRiskTime())?Dates.parse(enterprise.getRiskTime(), "yyyy/MM/dd HH:mm:ss"):new Date());
		blackList.setOperatorId(null);
		blackList.setCreateTime(new Date());
		blackListDao.insert(blackList);
		
	}

	@Override
	public ComBlacklist findByCompany(String name) {
		ComBlacklist comBlacklist = new ComBlacklist();
		comBlacklist.setCompany(name);
		List<ComBlacklist> list = blackListDao.findListByVo(comBlacklist);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<ComBlacklist> findLocalCustomerBlacklist(Date date) {
		
		return blackListDao.findLocalCustomerBlacklist(date);
	}

	@Override
	public List<ComBlacklist> findLocalEnterpriseBlacklist(Date date) {
		
		return blackListDao.findLocalEnterpriseBlacklist(date);
	}

}
