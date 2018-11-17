package com.zdmoney.credit.core.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zdmoney.credit.common.constant.CalculatorVersionEnum;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnumUtil;
import com.zdmoney.credit.common.constant.LoanFlowStateEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnumUtil;
import com.zdmoney.credit.common.constant.grant.FinanceGrantEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.domain.ContractTrialVo;
import com.zdmoney.credit.common.domain.LoanContractTrialRepVo;
import com.zdmoney.credit.common.domain.ResidualPactMoneyRepVo;
import com.zdmoney.credit.common.domain.ReturnLoanContractTrialVo;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelFunctionUtil;
import com.zdmoney.credit.common.util.IdcardInfoExtractorUtil;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.validator.IdcardValidator;
import com.zdmoney.credit.common.vo.core.LoanTrialVo;
import com.zdmoney.credit.common.vo.core.LoanVo;
import com.zdmoney.credit.common.vo.core.PersonVo;
import com.zdmoney.credit.common.vo.core.ReturnLoanContractVo;
import com.zdmoney.credit.common.vo.core.ReturnLoanTrialVo;
import com.zdmoney.credit.common.vo.core.ReturnLoanVo;
import com.zdmoney.credit.common.vo.core.ReturnQueryLoanVo;
import com.zdmoney.credit.common.vo.core.ReturnRepaymentDetailVo;
import com.zdmoney.credit.common.vo.core.UpdateFailVO;
import com.zdmoney.credit.core.EnumConvertor;
import com.zdmoney.credit.core.FieldNameMapper;
import com.zdmoney.credit.core.FieldNameMapper.FieldNameType;
import com.zdmoney.credit.core.FieldValueConvertor;
import com.zdmoney.credit.core.calculator.base.CalculatorBase;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.core.service.pub.ILoanCoreService;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import com.zdmoney.credit.framework.vo.lufax.entity.ContractConfDataInfoLufaxEntity;
import com.zdmoney.credit.framework.vo.lufax.entity.ContractConfFeeDetailEntity;
import com.zdmoney.credit.framework.vo.lufax.entity.ContractConfLoanInfoLufaxEntity;
import com.zdmoney.credit.framework.vo.lufax.entity.ContractConfRepayPlanEntity;
import com.zdmoney.credit.framework.vo.lufax.entity.RepayPlanLufaxEntity;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100003Vo;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100007Vo;
import com.zdmoney.credit.grant.domain.LoanBaseGrant;
import com.zdmoney.credit.loan.dao.pub.IJimuProductDao;
import com.zdmoney.credit.loan.dao.pub.ILoanBankDao;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.dao.pub.ILoanBsbMappingDao;
import com.zdmoney.credit.loan.dao.pub.ILoanContractDao;
import com.zdmoney.credit.loan.dao.pub.ILoanInitialInfoDao;
import com.zdmoney.credit.loan.dao.pub.ILoanProductDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.JimuProduct;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanBsbMapping;
import com.zdmoney.credit.loan.domain.LoanContract;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanManageSalesDepLog;
import com.zdmoney.credit.loan.domain.LoanProcessHistory;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBankService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseGrantService;
import com.zdmoney.credit.loan.service.pub.ILoanManageSalesDepLogService;
import com.zdmoney.credit.loan.service.pub.ILoanProcessHistoryService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailLufaxService;
import com.zdmoney.credit.loan.vo.LoanBaseVo;
import com.zdmoney.credit.offer.dao.pub.IOfferBankDicDao;
import com.zdmoney.credit.offer.dao.pub.IOfferYongyoubankBranchDao;
import com.zdmoney.credit.offer.dao.pub.IOfferYongyoubankHeadDao;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;
import com.zdmoney.credit.payment.service.pub.IThirdUnderLinePaymentService;
import com.zdmoney.credit.person.dao.pub.IPersonBankMapDao;
import com.zdmoney.credit.person.dao.pub.IPersonContactInfoDao;
import com.zdmoney.credit.person.dao.pub.IPersonThirdPartyAccountDao;
import com.zdmoney.credit.person.domain.PersonBankMap;
import com.zdmoney.credit.person.domain.PersonCarInfo;
import com.zdmoney.credit.person.domain.PersonContactInfo;
import com.zdmoney.credit.person.domain.PersonEntrepreneurInfo;
import com.zdmoney.credit.person.domain.PersonHouseInfo;
import com.zdmoney.credit.person.domain.PersonThirdPartyAccount;
import com.zdmoney.credit.person.service.pub.IPersonAddressInfoService;
import com.zdmoney.credit.person.service.pub.IPersonCarInfoService;
import com.zdmoney.credit.person.service.pub.IPersonContactInfoService;
import com.zdmoney.credit.person.service.pub.IPersonEntrepreneurInfoService;
import com.zdmoney.credit.person.service.pub.IPersonHouseInfoService;
import com.zdmoney.credit.person.service.pub.IPersonTelInfoService;
import com.zdmoney.credit.repay.vo.RepayDetail;
import com.zdmoney.credit.system.dao.pub.IComEmployeeDao;
import com.zdmoney.credit.system.dao.pub.IComOrganizationDao;
import com.zdmoney.credit.system.dao.pub.IPersonInfoDao;
import com.zdmoney.credit.system.dao.pub.IProdCreditProductInfoDao;
import com.zdmoney.credit.system.dao.pub.ISysFieldMapperDao;
import com.zdmoney.credit.system.dao.pub.ISysParamDefineDao;
import com.zdmoney.credit.system.dao.pub.SysDictionaryDao;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
import com.zdmoney.credit.system.domain.SysDictionary;
import com.zdmoney.credit.system.domain.SysFieldMapper;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.IProdCreditProductTermService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.zhuxue.dao.pub.IZhuxueOrganizationDao;
import com.zdmoney.credit.zhuxue.dao.pub.IZhuxueProductPlanDao;
import com.zdmoney.credit.zhuxue.domain.ZhuxueOrganization;
import com.zdmoney.credit.zhuxue.domain.ZhuxueProductPlan;

@Service
public class LoanCoreServiceImpl implements ILoanCoreService {

	private static final Logger logger = Logger.getLogger(LoanCoreServiceImpl.class);
	
	@Autowired
	IComEmployeeDao comEmployeeDao;//员工信息操作DAO
	@Autowired
	ILoanInitialInfoDao loanInitialInfoDao;//债权初始信息操作DAO
	@Autowired
	ILoanBaseDao loanBaseDao;//债权基本信息操作DAO
	@Autowired
	ILoanProductDao loanProductDao;//债权产品信息操作DAO
	@Autowired
	IPersonInfoDao personInfoDao;//人员信息操作DAO
	@Autowired
	IPersonContactInfoDao personContactInfoDao;//客户联系人操作DAO
	@Autowired
	ILoanBankDao loanBankDao;//银行操作DAO
	@Autowired
	IOfferBankDicDao offerBankDicDao;
	@Autowired
	IOfferYongyoubankHeadDao offerYongyoubankHeadDao;//用友总行操作DAO
	@Autowired
	IOfferYongyoubankBranchDao offerYongyoubankBranchDao;//用友支行操作DAO
	@Autowired
	IPersonThirdPartyAccountDao personThirdPartyAccountDao;//第三方账户信息操作DAO
	@Autowired
	IPersonBankMapDao personBankMapDao;//银行、客户关系映射操作DAO
	@Autowired
	ISysFieldMapperDao sysFieldMapperDao;//核心接口系统配置操作DAO
	@Autowired
	IComOrganizationDao comOrganizationDao;//组织机构操作DAO
	@Autowired
	ILoanContractDao loanContractDao;//生成合同操作DAO
	@Autowired
	ILoanRepaymentDetailDao loanRepaymentDetailDao;//还款计划操作DAO
	@Autowired
	IProdCreditProductInfoDao prodCreditProductInfoDao;
	@Autowired
	ISysParamDefineDao sysParamDefineDao;//系统参数操作DAO
	@Autowired
	IJimuProductDao jimuProductDao;//积木盒子产品操作DAO
	@Autowired
	SysDictionaryDao sysDictionaryDao;//数据字典操作DAO
	@Autowired
	IVLoanInfoDao vLoanInfoDao;
	@Autowired
	IZhuxueProductPlanDao zhuxueProductPlanDao;
	@Autowired
	IZhuxueOrganizationDao zhuxueOrganizationDao;
	
	@Autowired
	ISequencesService sequencesService;//数据库sequences操作Service
	@Autowired
	IPersonAddressInfoService personAddressInfoService;//借款人住址信息操作Service
	@Autowired
	IPersonTelInfoService personTelInfoService;//借款电话信息操作Service
	@Autowired
	ILoanProcessHistoryService loanProcessHistoryService;//债权日志操作Service
	@Autowired
	ILoanManageSalesDepLogService loanManageSalesDepLogService;//管理债权的销售部门日志操作Service
	@Autowired
	ILoanBankService loanBankService;//客户银行操作Service
	@Autowired
	IPersonInfoService personInfoService;//客户信息操作Service
	@Autowired
	IPersonContactInfoService personContactInfoService;//客户联系人信息操作Service
	@Autowired
	IPersonCarInfoService personCarInfoService;//借款人车的信息操作Service
	@Autowired
	IPersonEntrepreneurInfoService personEntrepreneurInfoService;//借款人企业的信息操作Service
	@Autowired
	IPersonHouseInfoService personHouseInfoService;//借款人住房的信息操作Service
	
	@Autowired
	IProdCreditProductTermService prodCreditProductTermServiceImpl;

	@Autowired
	IFinanceGrantService financeGrantService;//线上财务放款
	@Autowired
	ILoanBsbMappingDao loanBsbMappingDao;//业务流水号-借据号-借款IDAO
	
	@Autowired
	IThirdUnderLinePaymentService thirdUnderLinePaymentService; // 渤海信托、渤海2线下放款
	@Autowired
	ILoanRepaymentDetailLufaxService loanRepaymentDetailLufaxService;
	@Autowired
	ILoanBaseGrantService loanBaseGrantService;
	/**
	 * 保存债权数据
	 * @param params 入参
     * @return 返回债权信息是否保存成功的相关信息集合
	 */
	@Override
	@Transactional
	public Map<String, Object> importSingleAPSLoan(LoanVo params) throws SQLException, Exception {
		Map<String, Object> json = null;
		if (params.getUserCode() == null || "".equals(params.getUserCode()) || 
				params.getJsonStr() == null || "".equals(params.getJsonStr())) {
			json = MessageUtil.returnErrorMessage("失败：userCode或者债权数据不能为空");
			return json;
		}
		
		json = new HashMap<String, Object>();

		ComEmployee operator = comEmployeeDao.findEmployeeByUserCode(params.getUserCode());// 查询操作人员
		if (operator != null) {
			Map<String, Object> sourceMap = JSON.parseObject(params.getJsonStr());
			 //Map<String,Object> sourceMap = JSON.parseObject(getTestImportJsonStr());
			Map<String, Object> targetMap = null;

			/** 构建债权信息以及借款人信息 **/
			try {
				targetMap = getDomainMap(sourceMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("importSingleAPSLoan方法：失败:"+e.getMessage());
				
				json.put("code",Constants.DATA_ERR_CODE);
				json.put("message","失败："+e.getMessage());
				return json;
			}
			
			/**提取征审系统传过来的数据，并赋给核心系统相应的变量**/
			Map<String, String> fieldsNamesMap = getImportFieldsMap();
			try {
				for (Map.Entry<String, String> entry : fieldsNamesMap.entrySet()) {
					String apsJsonPath = entry.getKey();
					String coreJsonPath = entry.getValue();

					if (!StringUtils.isBlank(apsJsonPath) && !StringUtils.isBlank(coreJsonPath)) {
						if (coreJsonPath.contains("personContactInfo.") || coreJsonPath.contains("loanBank.")
								|| coreJsonPath .contains("loanBase.loanFlowState")) {
							continue;
						} else if (coreJsonPath.contains(".hasLoan")) {
							Object obj = PropertyUtils.getProperty(sourceMap, apsJsonPath);
							if (obj == null) {
								continue;
							}
							String apsLoan = obj.toString();
							if ("Y".equals(apsLoan.toUpperCase())) {
								PropertyUtils.setProperty(sourceMap, apsJsonPath, "t");
							} else if ("N".equals(apsLoan.toUpperCase())) {
								PropertyUtils.setProperty(sourceMap, apsJsonPath, "f");
							} else if ("END".equals(apsLoan.toUpperCase())) {
								PropertyUtils.setProperty(sourceMap, apsJsonPath, "f");
							} else if ("ING".equals(apsLoan.toUpperCase())) {
								PropertyUtils.setProperty(sourceMap, apsJsonPath, "t");
							} else if ("ALL".equals(apsLoan.toUpperCase())) {
								PropertyUtils.setProperty(sourceMap, apsJsonPath, "f");
							}
							populatePropValue(sourceMap, targetMap, apsJsonPath, coreJsonPath);
						} else {
							populatePropValue(sourceMap, targetMap, apsJsonPath, coreJsonPath);
						}
		            }
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("importSingleAPSLoan方法：失败:"+e.getMessage());
				
				json.put("code",Constants.DATA_ERR_CODE);
				json.put("message","失败："+e.getMessage());
				return json;
			}
			
			/**设置借款人地址**/
			PersonInfo borrower = (PersonInfo)targetMap.get("personInfo");
			try {/**提取征审系统传过来的数据，并赋给核心系统相应的变量**/
				String idIssuerAddress=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.idIssuerAddress").toString();
				String issuerState=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.issuerState").toString();
				String issuerCity=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.issuerCity").toString();
				String issuerZone=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.issuerZone").toString();
				if(idIssuerAddress.indexOf(issuerState)!=-1){
					idIssuerAddress=idIssuerAddress.replace(issuerState, ""); 
				}
				if(idIssuerAddress.indexOf(issuerCity)!=-1){
					idIssuerAddress=idIssuerAddress.replace(issuerCity, ""); 
				}
				if(idIssuerAddress.indexOf(issuerZone)!=-1){
					idIssuerAddress=idIssuerAddress.replace(issuerZone, ""); 
				}
				//户籍地址
			    borrower.setHrAddress(issuerState+issuerCity+issuerZone+idIssuerAddress);    
			    
				String corpAddress=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.corpAddress").toString();
			    String corpProvince=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.corpProvince").toString();
			    String corpCity=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.corpCity").toString();
			    String corpZone=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.corpZone").toString();
			    
			    if(corpAddress.indexOf(corpProvince)!=-1){
			    	corpAddress=corpAddress.replace(corpProvince, ""); 
			    }
			    if(corpAddress.indexOf(corpCity)!=-1){
			    	corpAddress=corpAddress.replace(corpCity, ""); 
			    }
			    if(corpAddress.indexOf(corpZone)!=-1){
			    	corpAddress=corpAddress.replace(corpZone, ""); 
			    }
			    //公司地址
			    borrower.setcAddress(corpProvince+corpCity+corpZone+corpAddress);
				
                String homeAddress=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.homeAddress").toString();
                String homeState=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.homeState").toString();
                String homeCity=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.homeCity").toString();
                String homeZone=PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.homeZone").toString();
                
                
                
			    if(homeAddress.indexOf(homeState)!=-1){
			    	homeAddress=homeAddress.replace(homeState, ""); 
			    }
			    if(homeAddress.indexOf(homeCity)!=-1){
			    	homeAddress=homeAddress.replace(homeCity, ""); 
			    }
			    if(homeAddress.indexOf(homeZone)!=-1){
			    	homeAddress=homeAddress.replace(homeZone, ""); 
			    }
			    //家庭住址
			    borrower.setAddress(homeState+homeCity+homeZone+homeAddress);
			
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("importSingleAPSLoan方法：设置联系人地址失败:" + e.getMessage());

				json.put("code", Constants.DATA_ERR_CODE);
				json.put("message", "失败：" + e.getMessage());
				return json;
			}
			System.out.println("客户身份证号："+borrower.getIdnum());
			IdcardValidator iv = new IdcardValidator();
			if (iv.isValidatedAllIdcard(borrower.getIdnum())) {
				IdcardInfoExtractorUtil ie = new IdcardInfoExtractorUtil(borrower.getIdnum());
				borrower.setBirthday(ie.getBirthday());
				borrower.setSex(ie.getGender());
			} else {
				throw new Exception("输入的身份证不合法");
			}

			LoanBase loanBase = (LoanBase) targetMap.get("loanBase");
			LoanInitialInfo loanInitialInfo = (LoanInitialInfo) targetMap.get("loanInitialInfo");
			LoanProduct loanProduct = (LoanProduct) targetMap.get("loanProduct");
			LoanBsbMapping loanBsbMapping = (LoanBsbMapping) targetMap.get("loanBsbMapping");

			/** 区分随意带与其他借款类型 **/
			String tempValue = null;
			try {
				tempValue = PropertyUtils.getProperty(sourceMap,"tmAppMain.productCd").toString();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("importSingleAPSLoan方法：失败:"+e.getMessage());
				
				json.put("code",Constants.DATA_ERR_CODE);
				json.put("message","失败："+e.getMessage());
				return json;
			}
			if ("00003".equals(tempValue)) {
				loanInitialInfo.setLoanType(LoanTypeEnum.随意贷A.getValue());
	        } else if ("00004".equals(tempValue)) {
	        	loanInitialInfo.setLoanType(LoanTypeEnum.随意贷B.getValue());
	        } else if ("00005".equals(tempValue)) {
	        	loanInitialInfo.setLoanType(LoanTypeEnum.随意贷C.getValue());
	        }
			loanInitialInfo.setRequestLoanType(loanInitialInfo.getLoanType());
			loanInitialInfo.setRequestTime(loanProduct.getTime());
			
			
			/**提取借款人的联系人和银行信息**/
			List<PersonContactInfo> contacts = null;
			LoanBank bank = null;
			try {
				contacts = handleContacts(borrower, sourceMap);//提取借款人的联系人信息
				bank = handleBanks(loanBase, sourceMap);//构建银行信息
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("importSingleAPSLoan方法：失败:"+e.getMessage());
				
				json.put("code",Constants.DATA_ERR_CODE);
				json.put("message","失败："+e.getMessage());
				return json;
			}

			/**优惠客户，判断优惠费率是否为空   为空的情况下返回异常信息   “该渠道不支持优惠客户签约，请改签。”**/
			if("Y".equals(loanInitialInfo.getIsRatePreferLoan())){//
				ProdCreditProductTerm prodCreditProductTerm = prodCreditProductTermServiceImpl.findBymap(loanProduct.getTime(),loanInitialInfo.getLoanType(),loanBase.getFundsSources());
				if(Strings.isEmpty(prodCreditProductTerm.getReloanRate())){
					throw new Exception("该渠道不支持优惠客户签约，请改签。");
				}
			}
			
			try {
				saveLoan(loanBase, loanInitialInfo, loanProduct, contacts, bank, borrower, operator,loanBsbMapping);// 保存债权信息
			} catch (SQLException se) {
				throw se;
			} catch (Exception e) {
				throw e;
			}
		} else {
			//该用户不存在
			json = MessageUtil.returnErrorMessage("失败：userCode输入有误，无法找到对应记录！");
			return json;
		}
		
		json.put("code", Constants.SUCCESS_CODE);
        json.put("message", "成功");
		return json;
	}
	
	/**
	 * 构建债权信息
	 * @param sourceMap
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getDomainMap(Map<String, Object> sourceMap) throws Exception {
		
		Map<String, Object> targetMap = new HashMap<String, Object>();
		PersonInfo personInfo = null;

		String appNo = PropertyUtils.getProperty(sourceMap, "tmAppMain.appNo").toString();
		LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByAppNo(appNo);// 根据appNo获取债权初始信息
		LoanBase loanBase = null;// 债权基本信息
		LoanProduct loanProduct = null;// 债权产品信息
		
		Object busNumber =PropertyUtils.getProperty(sourceMap, "tmAppMain.busnumber");//业务流水号
		String contractSource =EnumConvertor.convertToText("tmAppMain.contractSource", PropertyUtils.getProperty(sourceMap, "tmAppMain.contractSource").toString(), FieldNameType.aps4json);
		logger.info("tmAppMain.contractSource为："+PropertyUtils.getProperty(sourceMap, "tmAppMain.contractSource").toString()+"，对应的合同来源为："+contractSource);
		LoanBsbMapping loanBsbMapping = null;
		
		if (loanInitialInfo == null) {
			/**没有债权信息**/
			
			String idnum = PropertyUtils.getProperty(sourceMap, "tmAppPersonInfo.idNo").toString();
			personInfo = personInfoDao.findByIdnum(idnum);//根据身份证号查找客户信息
			
			if (null == personInfo) {
				/**没有客户信息，进行创建**/
				personInfo = new PersonInfo();
			} else {
				/**客户信息已经存在**/
				personContactInfoDao.deleteByPersonId(personInfo.getId());//删除该客户的联系人
			}
			personInfo = createCEHInfo(sourceMap,personInfo);//构建客户的公司、住房、车的信息
			personInfo.setNationality("中国");
			personInfo.setLanguage("汉语");
			personInfo.setIdtype("身份证");
			personInfo.setLivePersonCount(0);
			personInfo.setChildrenCount(0);
			personInfo.setCreatedDate(new Date());
			personInfo.setHasCreditCard("f");
			personInfo.setHasLoan("f");
			
			loanBase = new LoanBase();
			loanInitialInfo = new LoanInitialInfo();
			loanProduct = new LoanProduct();
			/*loanBsbMapping = new LoanBsbMapping();
			
			if(FundsSourcesTypeEnum.包商银行.getValue().equals(contractSource)){
				if(busNumber == null){
					throw new Exception("合同来源为包商银行时，业务流水号不能为空！");
				}
				loanBsbMapping.setBusNumber(busNumber.toString());
			}*/
			
			/**设置债权默认数据**/
			loanInitialInfo.setRequestDate(new Date());
			loanInitialInfo.setRequestMoney(new BigDecimal(0));
			loanProduct.setPactMoney(new BigDecimal(0));
			loanInitialInfo.setAuditingMoney(new BigDecimal(0));
			loanInitialInfo.setApproveMoney(new BigDecimal(0));
			loanInitialInfo.setApproveTime(0L);
			loanProduct.setResidualPactMoney(new BigDecimal(0));
			loanProduct.setReferRate(new BigDecimal(0));
			loanProduct.setManageRate(new BigDecimal(0));
			loanProduct.setManageRateForPartyC(new BigDecimal(0));
			loanProduct.setEvalRate(new BigDecimal(0));
			loanProduct.setRisk(new BigDecimal(0));
			loanProduct.setRateSum(new BigDecimal(0));
			loanProduct.setGrantMoney(new BigDecimal(0));
			loanProduct.setMargin(new BigDecimal(0));
			loanProduct.setRateem(new BigDecimal(0));
			loanProduct.setRateey(new BigDecimal(0));
			loanProduct.setTime(0L);
			loanInitialInfo.setRequestTime(0L);
			loanInitialInfo.setRestoreem(new BigDecimal(0));
			loanProduct.setAdvance(new BigDecimal(0));
			loanBase.setEndOfMonthOpened("f");
			loanProduct.setPromiseReturnDate(0L);
			loanBase.setIsSend("f");			
			
			/**设置征审传过来的债权数据**/
			String applyInputFlag = PropertyUtils.getProperty(sourceMap, "tmAppMain.applyInputFlag").toString();//进件标识(直通车、非直通车)
			ComOrganization organization = null;//客户管理营业部
			
			if("directApplyInput".equals(applyInputFlag)) {
				/**直通车业务营业部信息获取**/
				String contractBranch = PropertyUtils.getProperty(sourceMap, "tmAppMain.contractBranch").toString();//直通车签约营业部Code
				
				ComOrganization contractOrganization = new ComOrganization();
				contractOrganization.setOrgCode(contractBranch);
				ComOrganization cOrganization = comOrganizationDao.get(contractOrganization);
				if (cOrganization == null) {
					throw new Exception("无法查到营业部信息");
				}
				
				//直通车：签约营业部、管理营业部是同一个
				loanBase.setSignSalesDepId(cOrganization.getId());
				loanBase.setSalesDepartmentId(cOrganization.getId());
				
				organization = cOrganization;
			} else {
				/**签约营业部信息获取**/
				String contractBranch = null;
				contractBranch = PropertyUtils.getProperty(sourceMap, "tmAppMain.contractBranch").toString();//签约营业部
				if(null == contractBranch || "".equals(contractBranch)) {
					//如果签约营业部为空，默认取进件营业部
					contractBranch = PropertyUtils.getProperty(sourceMap, "tmAppMain.owningBranch").toString();//进件营业部
				}
				
				ComOrganization contractOrganization = new ComOrganization();
				contractOrganization.setOrgCode(contractBranch);
				ComOrganization cOrganization = comOrganizationDao.get(contractOrganization);
				if (cOrganization == null) {
					throw new Exception("无法查到营业部信息");
				}
				loanBase.setSignSalesDepId(cOrganization.getId());//设置签约营业部ID
				
				/**管理营业部信息获取**/
				String manageBranch = null;
				manageBranch = PropertyUtils.getProperty(sourceMap, "tmAppMain.manageBranch").toString();//管理营业部
				if(null == manageBranch || "".equals(manageBranch)) {
					//如果管理营业部为空，默认取进件营业部
					manageBranch = PropertyUtils.getProperty(sourceMap, "tmAppMain.owningBranch").toString();//进件营业部
				}
				
				ComOrganization manageOrganization = new ComOrganization();
				manageOrganization.setOrgCode(manageBranch);
				ComOrganization mOrganization = comOrganizationDao.get(manageOrganization);
				if (mOrganization == null) {
					throw new Exception("无法查到营业部信息");
				}
				loanBase.setSalesDepartmentId(mOrganization.getId());//设置管理营业部ID
				
				organization = mOrganization;
			}
			/*ComOrganization comOrganization = new ComOrganization();
			comOrganization.setOrgCode(owningBranch);
			ComOrganization organization = comOrganizationDao.get(comOrganization);
			if (organization == null) {
				throw new Exception("无法查到营业部信息");
			}
			loanBase.setSignSalesDepId(organization.getId());
			loanBase.setSalesDepartmentId(organization.getId());*/
			
			String createrUserCode = PropertyUtils.getProperty(sourceMap, "tmAppMain.createUser").toString();
			ComEmployee createUser = comEmployeeDao.findEmployeeByUserCode(createrUserCode);
			loanBase.setCrmId(createUser.getId());
			
			String salesmanCode = PropertyUtils.getProperty(sourceMap, "tmAppMain.branchManager").toString();
			ComEmployee salesman = comEmployeeDao.findEmployeeByUserCode(salesmanCode);
			if (salesman == null) {
				throw new Exception("无法查到销售人员信息");
			}
			loanBase.setSalesmanId(salesman.getId());
			loanBase.setSalesTeamId(salesman.getOrgId());
			
			loanBase.setLoanFlowState(LoanFlowStateEnum.合同签订.getValue());
			loanBase.setLoanState(LoanStateEnum.通过.getValue());
			//判断app进件
			Object appInputFlag = PropertyUtils.getProperty(sourceMap, "tmAppMain.appInputFlag");
			if(appInputFlag == null){
				loanBase.setAppInputFlag("");
			}else{
				loanBase.setAppInputFlag(appInputFlag.toString());
			}
			
			loanInitialInfo.setAuditDate(new Date());
			personInfo.setSalesDepartmentId(organization.getId());
		} else {
			//有债权信息
			
			loanBase = loanBaseDao.findByLoanId(loanInitialInfo.getLoanId());
			
			/**
			 * 如果已经放完款，则不允许对债权数据进行更改
			 */
			if (LoanStateEnum.正常.getValue().equals(loanBase.getLoanState()) && LoanStateEnum.正常.getValue().equals(loanBase.getLoanFlowState())) {
				throw new Exception("当前状态已为正常，不允许再进行操作！");
			}
			
			
			loanProduct = loanProductDao.findByLoanId(loanInitialInfo.getLoanId());
			
			loanBase.setLoanFlowState(LoanFlowStateEnum.合同签订.getValue());
			loanBase.setLoanState(LoanStateEnum.通过.getValue());
			//判断app进件
			Object appInputFlag = PropertyUtils.getProperty(sourceMap, "tmAppMain.appInputFlag");
			if(appInputFlag == null){
				loanBase.setAppInputFlag("");
			}else{
				loanBase.setAppInputFlag(appInputFlag.toString());
			}
			
			Long borrowerId = loanBase.getBorrowerId();
			personInfo = personInfoDao.get(borrowerId);
			
			personContactInfoDao.deleteByPersonId(personInfo.getId());//删除该客户的联系人
			personInfo = createCEHInfo(sourceMap,personInfo);//构建客户的公司、住房、车的信息
			
			/*loanBsbMapping = loanBsbMappingDao.getByLoanId(loanInitialInfo.getLoanId());
			if(loanBsbMapping!=null && FundsSourcesTypeEnum.包商银行.getValue().equals(contractSource)){
				if(busNumber == null){
					throw new Exception("合同来源为包商银行时，业务流水号不能为空！");
				}
				loanBsbMapping.setBusNumber(busNumber.toString());
			}*/
		}
		if(Strings.isNotEmpty(PropertyUtils.getProperty(sourceMap, "tmAppMain.isRatePreferLoan"))){
			String isRatePreferLoan = PropertyUtils.getProperty(sourceMap, "tmAppMain.isRatePreferLoan").toString();//是否是优惠费率客户
			loanInitialInfo.setIsRatePreferLoan(isRatePreferLoan);
		}
		
		if(Strings.isNotEmpty(PropertyUtils.getProperty(sourceMap, "tmAppMain.director"))){//保存进件业务主任字段
			String directorCode = PropertyUtils.getProperty(sourceMap, "tmAppMain.director").toString();
			ComEmployee ce = new ComEmployee();
			ce.setUsercode(directorCode);
			List<ComEmployee> directorList = comEmployeeDao.findListByVo(ce);
			if(CollectionUtils.isEmpty(directorList)){
				throw new Exception("进件业务主任在核心不存在！");
			}
			loanInitialInfo.setDirectorCode(directorCode);
		}
		
		targetMap.put("loanBase", loanBase);
		targetMap.put("loanInitialInfo", loanInitialInfo);
		targetMap.put("loanProduct", loanProduct);
		targetMap.put("personInfo", personInfo);
		if (FundsSourcesTypeEnum.包商银行.getValue().equals(contractSource)) {
			if (Strings.isNotEmpty(busNumber)) {
				loanBsbMapping = new LoanBsbMapping();
				loanBsbMapping.setBusNumber(busNumber.toString());
				targetMap.put("loanBsbMapping", loanBsbMapping);
			}
		} else if (FundsSourcesTypeEnum.陆金所.getValue().equals(contractSource)) {
			loanBsbMapping = new LoanBsbMapping();
			// 借款申请ID
			if(Strings.isNotEmpty(PropertyUtils.getProperty(sourceMap, "tmAppMain.lujsLoanReqId"))){
				String lujsLoanReqId = (String) PropertyUtils.getProperty(sourceMap, "tmAppMain.lujsLoanReqId");
				loanBsbMapping.setOrderNo(lujsLoanReqId);
			}
			// lufax网站用户名
			if(Strings.isNotEmpty(PropertyUtils.getProperty(sourceMap, "tmAppMain.lujsName"))){
				String lujsName = (String) PropertyUtils.getProperty(sourceMap, "tmAppMain.lujsName");
				loanBsbMapping.setBusNumber(lujsName);
			}
			targetMap.put("loanBsbMapping", loanBsbMapping);
		}
		return targetMap;
	}
	
	/**
	 * 创建借款人的公司、住房、车的信息
	 * @param person
	 * @return
	 */
	private PersonInfo createCEHInfo(Map<String, Object> sourceMap,PersonInfo person) throws Exception {
		/**构建公司信息**/
		if (person.getPersonEntrepreneurInfo() == null) {
			PersonEntrepreneurInfo personEntrepreneurInfo = null;
			if (null != person && null != person.getId()) {
				personEntrepreneurInfo = personEntrepreneurInfoService.findByPersonId(person.getId());
			}
			
			if (personEntrepreneurInfo == null) {
				personEntrepreneurInfo = new PersonEntrepreneurInfo();
			}
			person.setPersonEntrepreneurInfo(personEntrepreneurInfo);
		}
		
		/**构建汽车信息**/
		if (person.getPersonCarInfo() == null) {
			PersonCarInfo personCarInfo = null;
			if (null != person && null != person.getId()) {
				personCarInfo = personCarInfoService.findByPersonId(person.getId());
			}
			
			if (personCarInfo == null) {
				personCarInfo = new PersonCarInfo();
				personCarInfo.setHasLoan("f");
			}
			Object carLoanDate = PropertyUtils.getProperty(sourceMap, "tmAppCarInfo.carLoanDate");
			if(null!=carLoanDate){
				personCarInfo.setCarLoanDate(Dates.parse(carLoanDate.toString(), "yyyy-MM-dd"));		
			}
			person.setPersonCarInfo(personCarInfo);
		}
		
		/**构建住房信息**/
		if (person.getPersonHouseInfo() == null) {
			PersonHouseInfo personHouseInfo = null;
			if (null != person && null != person.getId()) {
				personHouseInfo = personHouseInfoService.findByPersonId(person.getId());
			}
			
			if (personHouseInfo == null) {
				personHouseInfo = new PersonHouseInfo();
				personHouseInfo.setHasLoan("f");
			}
			Object estateLoanDate = PropertyUtils.getProperty(sourceMap, "tmAppEstateInfo.estateLoanDate");
			if(null!=estateLoanDate){
				personHouseInfo.setEstateLoanDate(Dates.parse(estateLoanDate.toString(), "yyyy-MM-dd"));	
			}
			person.setPersonHouseInfo(personHouseInfo);
		}
		return person;
	}
	
	/**
	 * 提取借款人的联系人信息
	 * @param person 借款人
	 * @param sourceMap 征审系统传过来的数据（已转变成Map形式）
	 * @return
	 * @throws Exception
	 */
	private List<PersonContactInfo> handleContacts(PersonInfo person, Map<String, Object> sourceMap) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> contacts = (List<Map<String, Object>>)sourceMap.get("tmAppContactInfos");
		List<PersonContactInfo> contactList = new ArrayList<PersonContactInfo>();
		for(Map<String, Object> contact : contacts) {
            
			if ("Y".equals(contact.get("ifKnowLoan").toString().toUpperCase())) {
                contact.put("ifKnowLoan", "t");
            } else if ("N".equals(contact.get("ifKnowLoan").toString().toUpperCase())) {
                contact.put("ifKnowLoan", "f");
            }
            
            PersonContactInfo personContactInfo = new PersonContactInfo();
            personContactInfo.setContactType("其他");

            Map<String, Object> tempOutterSourceMap = new HashMap<String, Object>();
            tempOutterSourceMap.put("tmAppContactInfos", contact);
            Map<String, Object> tempOutterTargetMap = new HashMap<String, Object>();
            tempOutterTargetMap.put("personContactInfo", personContactInfo);
            Map<String, String> fieldsNamesMap = getContactImportFieldsMap();

            for(Map.Entry<String, String> entry : fieldsNamesMap.entrySet()){
                String apsJsonPath = entry.getKey();
                String coreJsonPath = entry.getValue();
                if(!StringUtils.isBlank(apsJsonPath) && !StringUtils.isBlank(coreJsonPath) && coreJsonPath.contains("personContactInfo.")){
                    populatePropValue(tempOutterSourceMap, tempOutterTargetMap, apsJsonPath, coreJsonPath);
                }
            }
            contactList.add(personContactInfo);
        }
		
		return contactList;
	}
	
	/**
	 * 构建借款人的银行信息
	 * @param loanBase
	 * @param sourceMap
	 */
	private LoanBank handleBanks(LoanBase loanBase, Map<String, Object> sourceMap) throws Exception {
		
		LoanBank bank = null;
		List<LoanBank> banks = null;
		if (loanBase.getId() != null && loanBase.getId() > 0) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("loanId", loanBase.getId());
			banks = loanBankDao.findListByMap(paramMap);
		}
		
		if (banks != null && banks.size() > 0) {
			//银行信息已存在，查询获取该银行信息
			bank = banks.iterator().next();
		} else {
			//银行信息不存，创建银行信息
			bank = new LoanBank();
		}
		
		String bankCode = PropertyUtils.getProperty(sourceMap, "tmAppMain.applyBankName").toString();
		String branchBankCode = PropertyUtils.getProperty(sourceMap, "tmAppMain.applyBankBranch").toString();
		String account = PropertyUtils.getProperty(sourceMap, "tmAppMain.applyBankCardNo").toString();
		String source = PropertyUtils.getProperty(sourceMap, "tmAppMain.contractSource").toString();
		
		if("00005".equals(source)) {
			bank = null;
            bank = loanBankDao.findByAccount(account);
            if(bank == null) {
            	throw new Exception("没有查到绑卡信息，请确认是否已绑卡？");
            }
            bank.setBankCode(bankCode);
            bank.setBranchBankCode(branchBankCode);//存的ID
            bank.setAccount(account);
            bank.setBankName(offerYongyoubankHeadDao.findByCode(bankCode).getName());
            bank.setFullName(offerYongyoubankBranchDao.findByBankId(branchBankCode).getBankName());
            bank.setBankDicType("dic2");
        } else {
            bank.setBankCode(bankCode);
            bank.setBranchBankCode(null);//为空
            bank.setAccount(account);
            bank.setBankName(offerBankDicDao.findByCode(bankCode).getName());
            bank.setFullName(branchBankCode);//证大P2P传过来的是营业网点名称，而不是bank_code了
            bank.setBankDicType("dic1");
        }
		
		return bank;
	}
	
	/**
	 * 初始化核心接口配置信息
	 */
	public void initFieldMapper() {
		System.out.println("initFieldMapper dododo");
		
		List<SysDictionary> dictionaries = sysDictionaryDao.findSysDictionaryAllList();
		Map<String, Object> map = buildFieldEnumMap(dictionaries);
		EnumConvertor.init(map);
		
		List<SysFieldMapper> mappers = sysFieldMapperDao.findAllList();
		FieldNameMapper.getInstance().init(mappers);
		
		Set<String> dateFieldNamesSet = new HashSet<String>();
        for(Object o : mappers) {
            SysFieldMapper names  = (SysFieldMapper)o;
            if("Date".equalsIgnoreCase(names.getType())){
                dateFieldNamesSet.add(names.getApsNameJson());
            }
        }

        Map<String,String> id2InstanceMap = new HashMap<String,String>();
        for(Object o : mappers) {
            SysFieldMapper names  = (SysFieldMapper)o;
            if(StringUtils.isBlank(names.getType())==false && names.getType().startsWith("zdsys.")){
                id2InstanceMap.put(names.getApsNameJson(), names.getType());
            }
        }
		
        FieldValueConvertor.getInstance().initDateFieldNames(dateFieldNamesSet,id2InstanceMap);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> buildFieldEnumMap(List<SysDictionary> dicts) {
        Map<String, Object> outterMap = new HashMap<String, Object>();
        for (SysDictionary dict : dicts) {
            String fieldName = dict.getCodeType();
            String code = dict.getCodeName();
            String value = dict.getCodeTitle();
            Map<String, Object> fieldEnumMap = (Map<String, Object>)outterMap.get(fieldName);
            if (fieldEnumMap == null) {
                fieldEnumMap = new HashMap<String, Object>();
                outterMap.put(fieldName, fieldEnumMap);
            }
            fieldEnumMap.put(code, value);
        }
        return outterMap;
    }
	
	/**
	 * 获取核心系统接口与征审系统的映射信息
	 * @return
	 */
	private Map<String, String> getImportFieldsMap() {
		return FieldNameMapper.getInstance().getMapper(FieldNameType.aps4json, FieldNameType.core4json);
	}
	
	/**
	 * 获取联系人的映射信息
	 * @return
	 */
	private Map<String, String> getContactImportFieldsMap(){
		return FieldNameMapper.getInstance().getMapper(FieldNameType.aps4json, FieldNameType.core4json);
    }
	
	/**
	 * 根据映射关系，将征审系统传过来的数据
	 * 赋给核心系统接口对应的属性
	 * @param source
	 * @param target
	 * @param apsJsonPath
	 * @param coreJsonPath
	 */
	private void populatePropValue(Object source, Object target, String apsJsonPath, String coreJsonPath){
		try {
			if(StringUtils.isBlank(apsJsonPath) || StringUtils.isBlank(coreJsonPath)) {
				return;
            }

            Object value  = PropertyUtils.getProperty(source, apsJsonPath);
            if(value == null || (value instanceof  String && StringUtils.isBlank(value.toString()))) {
            	return;
            }
            /*if("tmAppMain.productCd".equals(apsJsonPath)) {
            	return;
            } else {
            	value = FieldValueConvertor.getInstance().convert(value,apsJsonPath, FieldNameType.aps4json);
                BeanUtils.setProperty(target, coreJsonPath, value);
            }*/
            
            value = FieldValueConvertor.getInstance().convert(value,apsJsonPath, FieldNameType.aps4json);
            BeanUtils.setProperty(target, coreJsonPath, value);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("coreJsonPath="+coreJsonPath+" apsJsonPath = " + apsJsonPath);
        }
    }
	
	/**
	 * 保存债权信息
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @param contacts
	 * @param bank
	 * @param operator
	 */
	public void saveLoan(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct, List<PersonContactInfo> contacts, 
			LoanBank bank, PersonInfo borrower,ComEmployee operator,LoanBsbMapping loanBsbMapping) throws SQLException, Exception {
		
		/**1. 保存银行信息**/
		LoanBank loanBank = loanBankService.saveOrUpdateCore(bank);

		/** 2. 保存借款人信息 **/
		PersonThirdPartyAccount thirdPartyAccount = personThirdPartyAccountDao.findByAccount(bank.getAccount());
		if (thirdPartyAccount != null) {
			borrower.setThirdPartyAccountId(thirdPartyAccount.getId());
		}
		PersonInfo personInfo = personInfoService.saveOrUpdateCore(borrower);

		/** 3. 保存借款人联系人信息 **/
		for (Iterator<PersonContactInfo> iter = contacts.iterator(); iter.hasNext();) {
			PersonContactInfo contact = iter.next();
			contact.setPersonId(personInfo.getId());
		}
		List<PersonContactInfo> personContacts = personContactInfoService.saveOrUpdateCore(contacts);
		
		/**4. 保存债权信息**/
		loanBase.setGiveBackBankId(loanBank.getId());
		loanBase.setGrantBankId(loanBank.getId());

		/** 第一次保存合同信息生成合同编号，后续更新保存，此合同编号保持不变 **/
		if (Strings.isEmpty(loanBase.getId())) {
			String cityNum = comOrganizationDao.get(operator.getOrgId()).getCityNum();// 查找城市编码
			String contractNum = getBorrowerContractNum(cityNum);// 生成合同编号
			loanBase.setContractNum(contractNum);// 保存合同编号
		}
		
		loanBase.setBorrowerId(personInfo.getId());// 设置借款人ID

		loanBase.setLoanBelong(loanBase.getFundsSources());// 合同去向

		/**设置默认计算器版本**/
		if(loanProduct.getCalculatorType() == null) {
			loanProduct.setCalculatorType(CalculatorVersionEnum.v1.getValue());
		}
		
		if (Strings.isEmpty(loanBase.getId())) {
			/** 保存债权数据 **/
			loanBase.setId(sequencesService.getSequences(SequencesEnum.LOAN_BASE));
			loanBaseDao.insert(loanBase);
			
			loanInitialInfo.setId(sequencesService.getSequences(SequencesEnum.LOAN_INITIAL_INFO));
			loanInitialInfo.setLoanId(loanBase.getId());
			loanInitialInfoDao.insert(loanInitialInfo);
			loanProduct.setId(sequencesService.getSequences(SequencesEnum.LOAN_PRODUCT));
			loanProduct.setLoanId(loanBase.getId());
			loanProductDao.insert(loanProduct);
			
			if (loanBsbMapping != null) {
				// 只针对包商银行和陆金所
				if (FundsSourcesTypeEnum.包商银行.getValue().equals(loanBase.getFundsSources())
						|| FundsSourcesTypeEnum.陆金所.getValue().equals(loanBase.getFundsSources())) {
					loanBsbMapping.setId(sequencesService.getSequences(SequencesEnum.LOAN_BSB_MAPPING));
					loanBsbMapping.setBusNumber(loanBsbMapping.getBusNumber());
					loanBsbMapping.setLoanId(loanBase.getId());
					loanBsbMappingDao.insert(loanBsbMapping);
				}
			}
		} else {
			/**更新债权数据**/
			loanBaseDao.update(loanBase);
			loanInitialInfoDao.update(loanInitialInfo);
			loanProductDao.update(loanProduct);
			if(loanBsbMapping != null){
				// 只针对包商银行和陆金所
				if(FundsSourcesTypeEnum.包商银行.getValue().equals(loanBase.getFundsSources())
						|| FundsSourcesTypeEnum.陆金所.getValue().equals(loanBase.getFundsSources())){
					LoanBsbMapping mapping = loanBsbMappingDao.getByLoanId(loanBase.getId());
					if(mapping == null){
						loanBsbMapping.setId(sequencesService.getSequences(SequencesEnum.LOAN_BSB_MAPPING));
						loanBsbMapping.setLoanId(loanBase.getId());
						loanBsbMappingDao.insert(loanBsbMapping);
					} else{
						mapping.setBusNumber(loanBsbMapping.getBusNumber());
						if(Strings.isNotEmpty(loanBsbMapping.getOrderNo())){
							mapping.setOrderNo(loanBsbMapping.getOrderNo());
						}
						loanBsbMappingDao.update(mapping);
					}
				}
			}
		}
		
		/**5. 更新借款人及其联系人的住址、联系方式等信息**/
		
		//更新借款人的信息
		personAddressInfoService.saveCorePersonAddress(personInfo);
		personTelInfoService.saveCorePersonTel(personInfo);
		
		//更新联系人的信息
		for (PersonContactInfo contact : personContacts) {
			personAddressInfoService.saveCorePersonContactAddress(contact);
			personTelInfoService.saveCorePersonContactTel(contact);
		}
		
		/**6. 保存银行、借款人映射关系**/
		Map<String, Object> pbMap = new HashMap<String, Object>();
		pbMap.put("personId", personInfo.getId());
		pbMap.put("bankId", loanBank.getId());
		if (!personBankMapDao.exists(pbMap)) {
			PersonBankMap personBankMap = new PersonBankMap();
			personBankMap.setId(sequencesService.getSequences(SequencesEnum.PERSON_BANK_MAP));
			personBankMap.setPersonId(personInfo.getId());
			personBankMap.setBankId(loanBank.getId());
			personBankMapDao.insert(personBankMap);
		}
		
		/**7. 保存或更新借款人的住房、汽车、公司信息**/
		PersonCarInfo personCarInfo = personInfo.getPersonCarInfo();
		personCarInfo.setPersonId(personInfo.getId());
		PersonEntrepreneurInfo personEntrepreneurInfo = personInfo.getPersonEntrepreneurInfo();
		personEntrepreneurInfo.setPersonId(personInfo.getId());
		PersonHouseInfo personHouseInfo = personInfo.getPersonHouseInfo();
		personHouseInfo.setPersonId(personInfo.getId());
		
		personCarInfoService.saveOrUpdate(personCarInfo);
		personEntrepreneurInfoService.saveOrUpdate(personEntrepreneurInfo);
		personHouseInfoService.saveOrUpdate(personHouseInfo);
		
		
		/**8. 记录日志**/
		LoanManageSalesDepLog loanManageSalesDepLog = loanManageSalesDepLogService.findByLoanId(loanBase.getId());
		if (loanManageSalesDepLog == null) {
			loanManageSalesDepLog = new LoanManageSalesDepLog();
			
			loanManageSalesDepLog.setLoanId(loanBase.getId());
			loanManageSalesDepLog.setSalesDepId(loanBase.getSalesDepartmentId());
			loanManageSalesDepLog.setBeginDate(loanInitialInfo.getRequestDate());
			loanManageSalesDepLog.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2999-12-31"));//待定
			loanManageSalesDepLog.setOldCrmId(null);
			loanManageSalesDepLog.setNewCrmId(loanBase.getCrmId());
			loanManageSalesDepLog.setOperatorId(operator.getId());
			loanManageSalesDepLogService.insertLoanManageSalesDeptLog(loanManageSalesDepLog);
		}
		
		LoanProcessHistory loanProcessHistory = new LoanProcessHistory();
		loanProcessHistory.setContent("征审数据同步，loan_state设置为：通过，loan_flow_state设置为：合同签订");
		loanProcessHistory.setLoanId(loanBase.getId());
		loanProcessHistory.setLoanFlowState(loanBase.getLoanFlowState());
		loanProcessHistory.setCreator(operator.getId().toString());
		loanProcessHistory.setLoanState(loanBase.getLoanState());
		loanProcessHistory.setCreateTime(new Date());
		loanProcessHistoryService.insert(loanProcessHistory);
	}
	
	/**
	 * 生成合同号
	 * @param cityNum
	 * @return
	 */
	public String getBorrowerContractNum(String cityNum) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		Long contractNum = sequencesService.getSequences(SequencesEnum.CONTRACT_NUMBER);
		
		String bContractVerNum = sysParamDefineDao.getSysParamValue("codeHelper", "b_contract_ver_num", false);
		
		String contractNo = "ZDB"+sf.format(new Date()) + cityNum + bContractVerNum + String.format("%06d",contractNum);
		
		return contractNo;
	}
	
	private String getTestImportJsonStr() {
		String jsonStr="{\"tmAppCarInfo\": { \"carLoan\": null, \"checkId\": 21899541500, \"monthPaymentAmt\":  0, \"carTypr\": \"\", \"operationStatus\": \"\", \"carLoanTerm\": 0, \"carSeat\": \"\", \"org\":  \"000000000000\", \"updateDate\": \"2016-05-26 14:47:12\", \"localPlate\": null,  \"appNo\": \"20160525130000124444\", \"createUser\": \"00235106\", \"transferDate\": \"\",  \"nakedCarPrice\": 0, \"carBuyDate\": \"\", \"createDate\": \"2016-05-25 13:27:51\",  \"carBuyPrice\": 0, \"jpaVersion\": 2, \"updateUser\": \"00231416\" },\"tmAppEstateInfo\":  { \"otherIdNo\": \"\", \"monthPaymentAmt\": 0, \"estateType\": \"\", \"houseOwnership\": \"\",  \"pengyuanCheck\": null, \"ifMe\": null, \"org\": \"000000000000\", \"referenceAmt\": 0,  \"appNo\": \"20160525130000124444\", \"estateState\": \"\", \"estateZone\": \"\",  \"estateId\": 460819470, \"estateCity\": \"\", \"createDate\": \"2016-05-25 13:27:51\",  \"estateLoanAmt\": 0, \"jpaVersion\": 2, \"otherName\": \"\", \"estateBuyDate\": \"\",  \"personBankCheck\": null, \"equityRate\": 0, \"estateAmt\": 0, \"updateDate\": \"2016- 05-26 14:47:12\", \"estateAddress\": \"\", \"createUser\": \"00235106\",  \"hasRepaymentNum\": 0, \"builtupArea\": 0, \"estateLoan\": \"\", \"updateUser\":  \"00231416\" },\"tmAppHistories\": [ { \"idNo\": \"120103198506020059\", \"appHstId\":  348349996, \"remark\": \"初审自动分派至余莉\", \"checkPerson\": \"\", \"finalRole\": \"\", \"proNum\":  \"\", \"org\": \"000000000000\", \"appNo\": \"20160525130000124444\", \"refuseCode\": \"\",  \"createUser\": \"系统自动\", \"proName\": \"\", \"finalPerson\": \"\", \"name\": \"五月三\",  \"createDate\": \"2016-05-26 14:48:00\", \"patchBolt\": \"\", \"approvalPerson\": \"\",  \"rtfState\": \"K37\", \"jpaVersion\": 0 }, { \"idNo\": \"120103198506020059\",  \"appHstId\": 348349997, \"remark\": \"由余莉改派至陈丹\", \"checkPerson\": \"\", \"finalRole\": \"\",  \"proNum\": \"\", \"org\": \"000000000000\", \"appNo\": \"20160525130000124444\",  \"refuseCode\": \"\", \"createUser\": \"0023\", \"proName\": \"\", \"finalPerson\": \"\",  \"name\": \"五月三\", \"createDate\": \"2016-05-26 14:48:41\", \"patchBolt\": \"\",  \"approvalPerson\": \"\", \"rtfState\": \"K39\", \"jpaVersion\": 0 }, { \"idNo\":  \"120103198506020059\", \"appHstId\": 348349990, \"remark\": \"\", \"checkPerson\": \"\",  \"finalRole\": \"\", \"proNum\": \"20160525130000122150\", \"org\": \"000000000000\",  \"appNo\": \"20160525130000124444\", \"refuseCode\": \"\", \"createUser\": \"00235106\",  \"proName\": \"\", \"finalPerson\": \"\", \"name\": \"五月三\", \"createDate\": \"2016-05-26  14:45:10\", \"patchBolt\": \"\", \"approvalPerson\": \"\", \"rtfState\": \"A10\",  \"jpaVersion\": 0 }, { \"idNo\": \"120103198506020059\", \"appHstId\": 348349991,  \"remark\": \"丰东股份\", \"checkPerson\": \"\", \"finalRole\": \"\", \"proNum\":  \"20160525130000122150\", \"org\": \"000000000000\", \"appNo\": \"20160525130000124444\",  \"refuseCode\": \"\", \"createUser\": \"00231416\", \"proName\": \"\", \"finalPerson\": \"\",  \"name\": \"五月三\", \"createDate\": \"2016-05-26 14:47:12\", \"patchBolt\": \"\",  \"approvalPerson\": \"\", \"rtfState\": \"B10\", \"jpaVersion\": 0 }, { \"idNo\":  \"120103198506020059\", \"appHstId\": 348349998, \"remark\": \"过分过分\", \"checkPerson\":  \"00225637\", \"finalRole\": \"\", \"proNum\": \"20160525130000122150\", \"org\":  \"000000000000\", \"appNo\": \"20160525130000124444\", \"refuseCode\": \"\", \"createUser\":  \"00225637\", \"proName\": \"\", \"finalPerson\": \"\", \"name\": \"五月三\", \"createDate\":  \"2016-05-26 14:51:29\", \"patchBolt\": \"\", \"approvalPerson\": \"\", \"rtfState\": \"F10\",  \"jpaVersion\": 0 }, { \"idNo\": \"120103198506020059\", \"appHstId\": 348349992,  \"remark\": \"电子邮件无效|家庭电话\", \"checkPerson\": \"\", \"finalRole\": \"\", \"proNum\": \"\",  \"org\": \"000000000000\", \"appNo\": \"20160525130000124444\", \"refuseCode\": \"\",  \"createUser\": \"系统自动\", \"proName\": \"\", \"finalPerson\": \"\", \"name\": \"五月三\",  \"createDate\": \"2016-05-26 14:47:12\", \"patchBolt\": \"\", \"approvalPerson\": \"\",  \"rtfState\": \"C05\", \"jpaVersion\": 0 }, { \"idNo\": \"120103198506020059\",  \"appHstId\": 348349993, \"remark\": \"申请欺诈检查完成\", \"checkPerson\": \"\", \"finalRole\": \"\",  \"proNum\": \"\", \"org\": \"000000000000\", \"appNo\": \"20160525130000124444\",  \"refuseCode\": \"\", \"createUser\": \"系统自动\", \"proName\": \"\", \"finalPerson\": \"\",  \"name\": \"五月三\", \"createDate\": \"2016-05-26 14:47:13\", \"patchBolt\": \"\",  \"approvalPerson\": \"\", \"rtfState\": \"E05\", \"jpaVersion\": 0 }, { \"idNo\":  \"120103198506020059\", \"appHstId\": 348349994, \"remark\": \"门店初次评分完成|分值:200\",  \"checkPerson\": \"\", \"finalRole\": \"\", \"proNum\": \"\", \"org\": \"000000000000\",  \"appNo\": \"20160525130000124444\", \"refuseCode\": \"\", \"createUser\": \"系统自动\",  \"proName\": \"\", \"finalPerson\": \"\", \"name\": \"五月三\", \"createDate\": \"2016-05-26  14:47:13\", \"patchBolt\": \"\", \"approvalPerson\": \"\", \"rtfState\": \"I10\",  \"jpaVersion\": 0 }, { \"idNo\": \"120103198506020059\", \"appHstId\": 348349995,  \"remark\": \"门店自动初审判定通过\", \"checkPerson\": \"\", \"finalRole\": \"\", \"proNum\": \"\", \"org\":  \"000000000000\", \"appNo\": \"20160525130000124444\", \"refuseCode\": \"\", \"createUser\":  \"系统自动\", \"proName\": \"\", \"finalPerson\": \"\", \"name\": \"五月三\", \"createDate\": \"2016-05- 26 14:47:13\", \"patchBolt\": \"\", \"approvalPerson\": \"\", \"rtfState\": \"H15\",  \"jpaVersion\": 0 }, { \"idNo\": \"120103198506020059\", \"appHstId\": 348349999,  \"remark\": \"再次评分计算完成|分值:33\", \"checkPerson\": \"\", \"finalRole\": \"\", \"proNum\":  \"8510344\", \"org\": \"000000000000\", \"appNo\": \"20160525130000124444\", \"refuseCode\":  \"\", \"createUser\": \"系统自动\", \"proName\": \"\", \"finalPerson\": \"\", \"name\": \"五月三\",  \"createDate\": \"2016-05-26 14:51:29\", \"patchBolt\": \"\", \"approvalPerson\": \"\",  \"rtfState\": \"I05\", \"jpaVersion\": 0 }, { \"idNo\": \"120103198506020059\",  \"appHstId\": 348350000, \"remark\":  \"授信额度计算:系统建议审批金额：20000|系统建议审批期限：12|系统建议核实收入：30000\", \"checkPerson\": \"\",  \"finalRole\": \"\", \"proNum\": \"8510344\", \"org\": \"000000000000\", \"appNo\":  \"20160525130000124444\", \"refuseCode\": \"\", \"createUser\": \"系统自动\", \"proName\": \"\",  \"finalPerson\": \"\", \"name\": \"五月三\", \"createDate\": \"2016-05-26 14:51:29\",  \"patchBolt\": \"\", \"approvalPerson\": \"\", \"rtfState\": \"J05\", \"jpaVersion\": 0 }, {  \"idNo\": \"120103198506020059\", \"appHstId\": 348350001, \"remark\": \"门店自动初审判定通过\",  \"checkPerson\": \"\", \"finalRole\": \"\", \"proNum\": \"8510344\", \"org\": \"000000000000\",  \"appNo\": \"20160525130000124444\", \"refuseCode\": \"\", \"createUser\": \"系统自动\",  \"proName\": \"\", \"finalPerson\": \"\", \"name\": \"五月三\", \"createDate\": \"2016-05-26  14:51:29\", \"patchBolt\": \"\", \"approvalPerson\": \"\", \"rtfState\": \"H05\",  \"jpaVersion\": 0 }, { \"idNo\": \"120103198506020059\", \"appHstId\": 348350002,  \"remark\": \"由null改派至戴毛习\", \"checkPerson\": \"\", \"finalRole\": \"\", \"proNum\": \"\",  \"org\": \"000000000000\", \"appNo\": \"20160525130000124444\", \"refuseCode\": \"\",  \"createUser\": \"0023\", \"proName\": \"\", \"finalPerson\": \"\", \"name\": \"五月三\",  \"createDate\": \"2016-05-26 14:52:32\", \"patchBolt\": \"\", \"approvalPerson\": \"\",  \"rtfState\": \"K40\", \"jpaVersion\": 0 }, { \"idNo\": \"120103198506020059\",  \"appHstId\": 348350003, \"remark\": \"润体乳\", \"checkPerson\": \"\", \"finalRole\": \"L2\",  \"proNum\": \"20160525130000122150\", \"org\": \"000000000000\", \"appNo\":  \"20160525130000124444\", \"refuseCode\": \"\", \"createUser\": \"00225640\", \"proName\":  \"\", \"finalPerson\": \"00225640\", \"name\": \"五月三\", \"createDate\": \"2016-05-26  14:54:14\", \"patchBolt\": \"\", \"approvalPerson\": \"\", \"rtfState\": \"K10\",  \"jpaVersion\": 0 }, { \"idNo\": \"120103198506020059\", \"appHstId\": 348349559,  \"remark\": \"\", \"checkPerson\": \"\", \"finalRole\": \"\", \"proNum\":  \"20160525130000122150\", \"org\": \"000000000000\", \"appNo\": \"20160525130000124444\",  \"refuseCode\": \"\", \"createUser\": \"00235106\", \"proName\": \"\", \"finalPerson\": \"\",  \"name\": \"五月三\", \"createDate\": \"2016-05-25 13:27:51\", \"patchBolt\": \"\",  \"approvalPerson\": \"\", \"rtfState\": \"A05\", \"jpaVersion\": 0 } ], \"tmAppDebtsInfos\":  [ { \"memo\": \"\", \"debtsId\": 18125944552, \"creditLoanAlsoPay\": 0, \"org\":  \"000000000000\", \"updateDate\": \"2016-05-26 14:51:20\", \"creditLoanDebt\": 0,  \"creditDebt\": 0, \"appNo\": \"20160525130000124444\", \"createUser\": \"00225637\",  \"creditLoanTerm\": 0, \"outDebtTotal\": 0, \"creditLoanLimit\": 0,  \"creditTotalLimit\": 20000, \"creditUsedLimit\": 0, \"name\": \"00225637\",  \"createDate\": \"2016-05-26 14:51:20\", \"jpaVersion\": 0, \"updateUser\": \"\" } ],  \"tmAppSalaryLoanInfo\": { \"ifRelativesHouseProperty\": null, \"appNo\":  \"20160525130000124444\", \"ifOwnHouseProperty\": null, \"createUser\": \"00235106\",  \"ifLocalRegister\": null, \"conditionType\": \"B\", \"createDate\": \"2016-05-25  13:27:51\", \"org\": \"000000000000\", \"updateDate\": \"2016-05-26 14:47:12\",  \"salaryLoanId\": 121650, \"jpaVersion\": 2, \"updateUser\": \"00231416\" },  \"tmAppCardLoanInfo\": { \"billAmt2\": 0, \"creditLimit\": 0, \"billAmt1\": 0,  \"billAmt4\": 0, \"billAmt3\": 0, \"issuerData\": \"\", \"org\": \"000000000000\",  \"updateDate\": \"2016-05-26 14:47:12\", \"appNo\": \"20160525130000124444\",  \"createUser\": \"00235106\", \"cardLoanId\": 119650, \"createDate\": \"2016-05-25  13:27:51\", \"payMonthAmt\": 0, \"jpaVersion\": 2, \"updateUser\": \"00231416\" },  \"tmAppMerchantLoanInfo\": { \"merchantLoanId\": 121150, \"org\": \"000000000000\",  \"sellerCreditLevel\": \"\", \"updateDate\": \"2016-05-26 14:47:12\", \"biullAmt6\": 0,  \"setupShopDate\": \"\", \"appNo\": \"20160525130000124444\", \"createUser\": \"00235106\",  \"regardedNum\": 0, \"biullAmt2\": 0, \"biullAmt3\": 0, \"biullAmt4\": 0, \"biullAmt5\":  0, \"biullAmt1\": 0, \"createDate\": \"2016-05-25 13:27:51\", \"sellerCreditType\": \"\",  \"payMonthAmt\": 0, \"jpaVersion\": 2, \"updateUser\": \"00231416\" },  \"tmAppProvidentInfo\": { \"providentInfo\": \"A\", \"providentId\": 125150,  \"openAccountDate\": \"\", \"paymentMonthNum\": 0, \"org\": \"000000000000\",  \"updateDate\": \"2016-05-26 14:47:12\", \"depositBase\": 0, \"monthDepositAmt\": 0,  \"appNo\": \"20160525130000124444\", \"createUser\": \"00235106\", \"depositRate\": 0,  \"paymentUnit\": \"\", \"createDate\": \"2016-05-25 13:27:51\", \"jpaVersion\": 2,  \"updateUser\": \"00231416\" }, \"tmAppCredit\": { \"existCorpOrg\": 0,  \"creditOverLtotal\": 0, \"ensureRemainder\": 0, \"isneedwritecredittips\": \"N\",  \"existOrgNum\": 0, \"appNo\": \"20160525130000124444\", \"jpaVersion\": 4,  \"isneedwriteloanoverinfo\": \"N\", \"dissentLableNum\": 0, \"existMaxAmount\": 0,  \"creditPhone\": \"N\", \"loaningCorpOrg\": 0, \"creditId\": 121150, \"loanOverCount\": 0,  \"createUser\": \"00235106\", \"isneedwriteloanover\": \"N\", \"loanFirstMonth\": \"\",  \"ensureAmount\": 0, \"creditCorpPhone\": \"N\", \"creditOverAccounts\": 0,  \"updateUser\": \"00231416\", \"existCreditTotal\": 0, \"semiCreditAccounts\": 0,  \"existAccount\": 0, \"org\": \"000000000000\", \"loaningMeanPay\": 0, \"loaningNum\": 0,  \"creditOverLmonth\": 0, \"creditAccounts\": 0, \"isneedwriteexistcredit\": \"N\",  \"createDate\": \"2016-05-25 13:27:51\", \"loanOverMonth\": 0, \"loaningContractTotal\":  0, \"selfStateBooklist\": \"\", \"loaningRemainSum\": 0, \"existUsedAmount\": 0,  \"semiCreditFirstMonth\": \"\", \"houseLoanNum\": 0, \"creditOverMonths\": 0,  \"updateDate\": \"2016-05-26 14:50:06\", \"existMeanAmount\": 0, \"creditHomePhone\":  \"N\", \"isneedwritecreditover\": \"N\", \"ortherLoanNum\": 0, \"ensureCount\": 0,  \"loanOverLmonths\": 0, \"isneedwriteensuremes\": \"N\", \"loaningOrgNums\": 0,  \"creditFirstMonth\": \"\", \"loanOverLtotal\": 0 }, \"tmAppApproveCheckDatas\": [ {  \"checkId\": 64066602, \"memo\": \"\", \"publicWaterTotal\": 0, \"otherCheckMes\":  \"false,false\", \"waterIncomeTotal\": 0, \"org\": \"000000000000\", \"updateDate\":  \"2016-05-26 14:51:20\", \"publicWater3\": 0, \"personalWaterTotal\": 0,  \"publicWater2\": 0, \"personalWater3\": 0, \"appNo\": \"20160525130000124444\",  \"createUser\": \"00225637\", \"threeMonthsCount\": 0, \"personalWater2\": 0,  \"publicWater1\": 0, \"name\": \"00225637\", \"personalWater1\": 0,  \"personMonthAverage\": 0, \"publicMonthAverage\": 0, \"oneMonthsCount\": 0,  \"createDate\": \"2016-05-26 14:51:20\", \"jpaVersion\": 0, \"updateUser\": \"\" } ],  \"tmAppMasterLoanInfo\": { \"buyerCreditType\": \"C\", \"org\": \"000000000000\",  \"shoppingMonthAmt\": 0, \"appNo\": \"20160525130000124444\", \"buyerCreditLevel\": \"B\",  \"lastYearPayAmt\": 5000, \"createDate\": \"2016-05-25 13:27:51\", \"whiteCreditValue\":  0, \"jiDongUserLevel\": \"\", \"jpaVersion\": 5, \"pastYearShoppingAmount\": 0,  \"payAmt1\": 1000, \"payAmt2\": 4000, \"payAmt3\": 3000, \"lastYearShoppingAmt\": 0,  \"masterLoadId\": 124150, \"updateDate\": \"2016-05-26 14:47:12\", \"createUser\":  \"00235106\", \"goodRate\": 98, \"shoppingAmt1\": 0, \"acctRegisterDate\": \"2016-05-01  12:00:00\", \"shoppingAmt3\": 0, \"shoppingAmt2\": 0, \"payMonthAmt\": 2667,  \"deliveryAddress\": \"\", \"updateUser\": \"00231416\" }, \"tmAppApprovalHistories\": [ {  \"approvalMonthPay\": 1280, \"approvalMemo\": \"对对对\", \"approvalProductCd\": \"00001\",  \"approvalApplyTerm\": \"24\", \"approvalApplyLimit\": 30000, \"org\": \"000000000000\",  \"updateDate\": \"2016-05-26 14:54:14\", \"approvalCheckIncome\": 30000, \"id\":  36285038755, \"appNo\": \"20160525130000124444\", \"approvalAllDebtRate\": 4,  \"createUser\": \"00225640\", \"approvalLimit\": 30000, \"createDate\": \"2016-05-26  14:54:07\", \"approvalTerm\": \"24\", \"approvalPerson\": \"00225640\",  \"approvalDebtTate\": 4, \"rtfState\": \"K05\", \"jpaVersion\": 1, \"updateUser\":  \"00225640\" }, { \"approvalMonthPay\": 1280, \"approvalMemo\": \"方法\",  \"approvalProductCd\": \"00001\", \"approvalApplyTerm\": \"24\", \"approvalApplyLimit\":  30000, \"org\": \"000000000000\", \"updateDate\": \"2016-05-26 14:51:29\",  \"approvalCheckIncome\": 30000, \"id\": 36285038754, \"appNo\":  \"20160525130000124444\", \"approvalAllDebtRate\": 4, \"createUser\": \"00225637\",  \"approvalLimit\": 30000, \"createDate\": \"2016-05-26 14:51:20\", \"approvalTerm\":  \"24\", \"approvalPerson\": \"00225637\", \"approvalDebtTate\": 4, \"rtfState\": \"F05\",  \"jpaVersion\": 1, \"updateUser\": \"00225637\" } ], \"tmAppMain\": { \"ifCreditRecord\":  \"N\", \"remark\": \"润体乳\", \"checkPerson\": \"00225637\", \"manageBranch\":  \"01010001005600007\", \"productCd\": \"00001\", \"groupForDirector\": \"续贷客户\", \"appNo\":  \"20160525130000124444\", \"sysAccTrem\": 12, \"signDate\": \"2016-05-26 00:00:00\",  \"ifLoanAgain\": \"N\", \"priority\": \"\", \"ifPri\": \"N\", \"branchManager\": \"00211928_1\",  \"applyType\": \"NEW\", \"contractSource\": \"00004\", \"ifPatchBolt\": \"N\",  \"applyBankBranch\": \"上海支行446\", \"ifEnd\": \"N\", \"jpaVersion\": 29, \"applyLmt\": 30000,  \"owningBranch\": \"01010001005600007\", \"contractTrem\": 24, \"checkAllotDate\":  \"2016-05-26 14:48:00\", \"status\": \"待签约\", \"finalRole\": \"L2\", \"sugLmt\": 0,  \"accLmt\": 30000, \"accTerm\": 24, \"proNum\": \"8510344\", \"pointResult\": 33,  \"director\": \"\", \"amoutIncome\": 30000, \"createUser\": \"00235106\", \"applyBankName\":  \"102\", \"bankPhone\": \"\", \"applyBankCardNo\": \"6222021001116245733\",  \"applyInputFlag\": \"applyInput\", \"isrollback\": \"\", \"appOrgName\": \"\", \"ifGrey\":  \"N\", \"updateUser\": \"00225640\", \"clientType\": \"1\", \"specialPlan\": \"\",  \"specialOrg\": \"\", \"creditApplication\": \"00001\", \"sysCheckLmt\": 30000,  \"contractNum\": \"ZDB20160012000351\", \"thirdId\": \"\", \"initProductCd\": \"00001\",  \"org\": \"000000000000\", \"finalAllotDate\": \"\", \"accDate\": \"2016-05-26 14:54:14\",  \"refuseCode\": \"\", \"owningBranchAttribute\": \"O\", \"proName\": \"000000\", \"ifRefuse\":  \"N\", \"contractLmt\": 42546.23, \"ensureAmtAmount\": 0, \"manageUpdateDate\": \"2016- 05-25 13:27:51\", \"loanBranch\": \"01010001005600007\", \"createDate\": \"2016-05-25  13:27:51\", \"repayDate\": 16, \"approvalPerson\": \"\", \"ifUrgent\": 50,  \"ifSuspectCheat\": \"N\", \"ifOldOrNewLogo\": null, \"contractBranch\": \"\",  \"appLoanPlan\": \"\", \"updateDate\": \"2016-05-27 13:34:37\", \"applyRate\": 0.1,  \"finalPerson\": \"00225640\", \"loanDate\": \"\", \"sysAccLmt\": 20000, \"applyTerm\":  \"24\", \"rtfState\": \"K10\", \"loanId\": \"150000158\" }, \"tmAppPolicyInfo\": {  \"paidTerm\": 0, \"insuranceTerm\": 0, \"org\": \"000000000000\", \"updateDate\": \"2016- 05-26 14:47:12\", \"appNo\": \"20160525130000124444\", \"policyRelation\": \"\",  \"createUser\": \"00235106\", \"policyCheck\": \"\", \"policyId\": 125150,  \"lastPaymentDate\": \"\", \"insuranceAmt\": 0, \"createDate\": \"2016-05-25 13:27:51\",  \"yaerPaymentAmt\": 0, \"paymentMethod\": \"\", \"jpaVersion\": 2, \"updateUser\":  \"00231416\" }, \"tmAppPersonInfo\": { \"corpPayWay\": \"00001\", \"idLastDate\": \"\",  \"cellphoneSec\": \"\", \"otherIncome\": 0, \"houseOwnership\": \"\", \"corpStandFrom\":  \"2010-05-28 12:00:00\", \"familyAvgeVenue\": 0, \"businessPlace\": \"\",  \"priEnterpriseType\": \"\", \"appNo\": \"20160525130000124444\", \"qualification\":  \"00001\", \"familyMonthPay\": 5000, \"homeCity\": \"市辖区\", \"homeAddress\": \"法规和规范\",  \"cellphone\": \"13405098445\", \"issuerPostcode\": \"\", \"age\": 30, \"idIssuerAddress\":  \"多个\", \"gender\": \"M\", \"houseRent\": 0, \"jpaVersion\": 8, \"idNo\":  \"120103198506020059\", \"corpFax\": \"\", \"occupation\": \"00002\", \"monthAmt\": 0,  \"graduationDate\": \"2010-05-13 12:00:00\", \"monthRent\": 0, \"enterpriseRate\": 0,  \"familyMember\": \"\", \"empStatus\": null, \"corpZone\": \"东城区\", \"homePostcode\": \"\",  \"issuerZone\": \"东城区\", \"childrenNum\": 1, \"corpStability\": \"\", \"createUser\":  \"00235106\", \"sharesName\": \"\", \"liquidAsset\": \"\", \"corpProvince\": \"北京市\",  \"corpCity\": \"市辖区\", \"email\": \"\", \"employeeNum\": 0, \"houseType\": \"00001\",  \"corpType\": \"00009\", \"corpPostcode\": \"\", \"corpPhoneSec\": \"\", \"qqNum\": \"\",  \"updateUser\": \"00231416\", \"birthday\": \"\", \"totalMonthSalary\": 30000,  \"issuerCity\": \"市辖区\", \"maritalStatus\": \"00002\", \"org\": \"000000000000\",  \"corpAddrCtryCd\": \"\", \"titleOfTechnical\": \"\", \"corpmemNo\": \"\",  \"privateOwnersFlag\": \"N\", \"setupDate\": \"\", \"applyPersonId\": 1040729820,  \"corpName\": \"大锅饭\", \"residencyCountryCd\": \"\", \"corpmemFlag\": null, \"corpPost\":  \"00004\", \"homeZone\": \"西城区\", \"name\": \"五月三\", \"businessNetWork\": \"A\",  \"corpStructure\": \"00004\", \"createDate\": \"2016-05-25 13:27:51\", \"corpPayday\": \"\",  \"wechatNum\": \"\", \"sharesIdNo\": \"\", \"sharesScale\": 0, \"corpWorkyears\": 0,  \"corpDepapment\": \"方法\", \"homeStandFrom\": \"\", \"monthSalary\": 30000, \"corpAddress\":  \"测vbvc\", \"monthMaxRepay\": 6000, \"updateDate\": \"2016-05-26 14:47:12\",  \"prOfCountry\": null, \"nationality\": \"\", \"corpPhone\": \"010-2550222\",  \"registerFunds\": 0, \"homePhone2\": \"\", \"homePhone1\": \"\", \"homeState\": \"北京市\",  \"homeAddrCtryCd\": \"\", \"issuerState\": \"北京市\" }, \"tmAppContactInfos\": [ {  \"contactCellphone\": \"13405098884\", \"contactCorpFax\": \"\", \"contactGender\": null,  \"org\": \"000000000000\", \"updateDate\": \"2016-05-26 14:45:10\", \"ifKnowLoan\": \"N\",  \"contactCorpPost\": \"\", \"sequenceNum\": 0, \"appNo\": \"20160525130000124444\",  \"createUser\": \"\", \"contactRelation\": \"00013\", \"contactId\": -1579595381,  \"contactName\": \"的非官方\", \"contactCorpPhone\": \"\", \"contactEmpName\": \"放到\",  \"contactIdNo\": \"\", \"createDate\": \"2016-05-26 14:42:13\", \"jpaVersion\": 1,  \"updateUser\": \"00235106\" }, { \"contactCellphone\": \"13405098883\",  \"contactCorpFax\": \"\", \"contactGender\": null, \"org\": \"000000000000\",  \"updateDate\": \"2016-05-26 14:45:10\", \"ifKnowLoan\": \"N\", \"contactCorpPost\": \"\",  \"sequenceNum\": 1, \"appNo\": \"20160525130000124444\", \"createUser\": \"00235106\",  \"contactRelation\": \"00001\", \"contactId\": -1579595546, \"contactName\": \"个梵蒂冈\",  \"contactCorpPhone\": \"\", \"contactEmpName\": \"多福多寿\", \"contactIdNo\": \"\",  \"createDate\": \"2016-05-25 13:27:51\", \"jpaVersion\": 1, \"updateUser\": \"\" }, {  \"contactCellphone\": \"13405098882\", \"contactCorpFax\": \"\", \"contactGender\": null,  \"org\": \"000000000000\", \"updateDate\": \"2016-05-26 14:45:10\", \"ifKnowLoan\": \"N\",  \"contactCorpPost\": \"\", \"sequenceNum\": 2, \"appNo\": \"20160525130000124444\",  \"createUser\": \"00235106\", \"contactRelation\": \"00003\", \"contactId\": -1579595545,  \"contactName\": \"对对对\", \"contactCorpPhone\": \"\", \"contactEmpName\": \"发光飞碟\",  \"contactIdNo\": \"\", \"createDate\": \"2016-05-25 13:27:51\", \"jpaVersion\": 1,  \"updateUser\": \"\" }, { \"contactCellphone\": \"13405098881\", \"contactCorpFax\": \"\",  \"contactGender\": null, \"org\": \"000000000000\", \"updateDate\": \"2016-05-26  14:45:10\", \"ifKnowLoan\": \"N\", \"contactCorpPost\": \"\", \"sequenceNum\": 3, \"appNo\":  \"20160525130000124444\", \"createUser\": \"00235106\", \"contactRelation\": \"00004\",  \"contactId\": -1579595544, \"contactName\": \"都改密码吗\", \"contactCorpPhone\": \"\",  \"contactEmpName\": \"发光飞碟\", \"contactIdNo\": \"\", \"createDate\": \"2016-05-25  13:27:51\", \"jpaVersion\": 1, \"updateUser\": \"\" }, { \"contactCellphone\":  \"13405098880\", \"contactCorpFax\": \"\", \"contactGender\": null, \"org\":  \"000000000000\", \"updateDate\": \"2016-05-26 14:45:10\", \"ifKnowLoan\":  \"N\", \"contactCorpPost\": \"\", \"sequenceNum\": 4, \"appNo\":  \"20160525130000124444\", \"createUser\": \"00235106\", \"contactRelation\":  \"00007\", \"contactId\": -1579595543, \"contactName\": \"丰东股份返回\", \"contactCorpPhone\":  \"\", \"contactEmpName\": \"大锅饭\", \"contactIdNo\": \"\", \"createDate\": \"2016-05-25  13:27:51\", \"jpaVersion\": 1, \"updateUser\": \"\" } ] }";
		return jsonStr;
	}

	/**
	 * 生成合同，创建还款计划
	 * @param params
	 * @return
	 */
	@Override
	@Transactional
	public Map<String, Object> createAPSLoan(LoanVo params) throws Exception {
		
		Map<String, Object> json = new HashMap<String, Object>();

		LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByAppNo(params.getAppNo());
		if (loanInitialInfo == null) {
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：appNo输入有误，未查到对应记录");
			return json;
		}

		/**
		 * 如果已经放完款，则不允许对债权数据进行更改
		 */
		LoanBase loanBase = loanBaseDao.findByLoanId(loanInitialInfo.getLoanId());
		if (LoanStateEnum.正常.getValue().equals(loanBase.getLoanState()) && LoanStateEnum.正常.getValue().equals(loanBase.getLoanFlowState())) {
			json.put("code", Constants.DATA_ERR_CODE);
			json.put("message", "当前状态已为正常，不允许再进行操作！");
			return json;
		}

		LoanProduct loanProduct = loanProductDao.findByLoanId(loanInitialInfo.getLoanId());
		
		try {
			loanInitialInfo.setAuditDate(new Date());
			
			/**调用生成合同及还款计划方法**/
			createRepaymentDetail(loanBase, loanInitialInfo, loanProduct);

			/** 查找合同信息 **/
			LoanContract contract = loanContractDao.findByLoanId(loanInitialInfo.getLoanId());

			/** 查询还款计划 **/
			List<LoanRepaymentDetail> repaymentDetails = loanRepaymentDetailDao.findByLoanId(loanInitialInfo.getLoanId());
 
			//陆金所保存用户名，借款申请id
			if(FundsSourcesTypeEnum.陆金所.getValue().equals(loanBase.getFundsSources())){
				if(Strings.isEmpty(params.getLujsName())){
					json.put("code",Constants.DATA_ERR_CODE);
					json.put("message","失败：lujsName不能为空！");
					return json;
				}
				if(Strings.isEmpty(params.getLujsLoanReqId())){
					json.put("code",Constants.DATA_ERR_CODE);
					json.put("message","失败：lujsLoanReqId不能为空！");
					return json;
				}
				LoanBsbMapping loanBsbMapping = loanBsbMappingDao.getByLoanId(loanBase.getId());
				loanBsbMapping.setBusNumber(params.getLujsName());
				loanBsbMapping.setOrderNo(params.getLujsLoanReqId());
				loanBsbMappingDao.update(loanBsbMapping);
			}
			
			json.put("code", Constants.SUCCESS_CODE);
			json.put("message", "成功");

			json.put("contract", toReturnLoanContractVo(contract, loanInitialInfo));
			json.put("loan", toReturnLoanVo(loanBase, loanInitialInfo, loanProduct, contract));
			json.put("repaymentDetail", toReturnRepaymentDetailVo(repaymentDetails));

		} catch (Exception e) {
			throw e;
		}
		
		return json;
	}
	
	/**
	 * 创建还款计划
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	private void createRepaymentDetail(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		loanInitialInfo.setSignDate(dateFormat.parse(dateFormat.format(new Date())));
		
		ProdCreditProductTerm prodCreditProductTerm = prodCreditProductTermServiceImpl.findBymap(loanProduct.getTime(),loanInitialInfo.getLoanType(),loanBase.getFundsSources());
		if (null == prodCreditProductTerm) {
			throw new Exception("没有查询到产品信息");
		}
		//是优惠综合费率客户
		if("Y".equals(loanInitialInfo.getIsRatePreferLoan())){
			if(!Strings.isEmpty(prodCreditProductTerm.getReloanRate())){
				prodCreditProductTerm.setRate(prodCreditProductTerm.getReloanRate());
				prodCreditProductTerm.setAccrualem(prodCreditProductTerm.getReloanAccrualem());
			}else{
				throw new Exception("不存在优惠费率！");
			}
		}
		/*ICalculatorService iCalculatorService = AdaptiveFundsSoures.getICalculatorServiceByFundsSoures(loanBase.getFundsSources());
		iCalculatorService.updateRate(loanBase, loanInitialInfo, loanProduct, prodCreditProductTerm);*/
		
		String endOfMonthOpened = sysParamDefineDao.getSysParamValue("codeHelper", "end_of_month_opened", false);
		String fundsSources = loanBase.getFundsSources();
		Date d = getStartReturnDate(loanInitialInfo, endOfMonthOpened,fundsSources);
		
		/**设置开始还款日**/
		if (d.getDate() >= 1 && d.getDate() <= 15) {
			loanProduct.setPromiseReturnDate(1L);
		} else {
			loanProduct.setPromiseReturnDate(16L);
		}
		loanProduct.setStartrdate(d);
		
		d = (Date)d.clone();
		d.setMonth(d.getMonth() + Integer.valueOf(loanProduct.getTime().toString()) - 1);
		loanProduct.setEndrdate(d);
		
		loanBase.setEndOfMonthOpened(endOfMonthOpened);
		
		//提前结清计算器修改
		/** 合同来源 枚举 **/
		FundsSourcesTypeEnum fundsSourcesTypeEnum = Assert.validEnum(FundsSourcesTypeEnum.class, loanBase.getFundsSources(), "");
		
		CalculatorBase alculatorBase = CalculatorFactoryImpl.createCalculatorBase(fundsSourcesTypeEnum);
		loanProduct.setCalculatorType(alculatorBase.getCalculatorVersion().getValue());
		//CalculatorVersionEnum calculatorVersionEnum = Assert.validEnum(CalculatorVersionEnum.class, loanProduct.getCalculatorType(), "");
		ICalculator calculator = CalculatorFactoryImpl.createCalculator(fundsSourcesTypeEnum, alculatorBase.getCalculatorVersion());		
		calculator.updateRate(loanBase, loanInitialInfo, loanProduct, prodCreditProductTerm);
		
		loanRepaymentDetailDao.deleteByLoanId(loanInitialInfo.getLoanId());
		
		if("t".equals(loanBase.getEndOfMonthOpened())) {
			SimpleDateFormat dfm= new SimpleDateFormat("yyyy-MM-dd");
            Date sysdate = dfm.parse(dfm.format(new Date()));
            if(sysdate.getDate() < 16) {
                sysdate.setMonth(sysdate.getMonth());
                sysdate.setDate(16);
                loanInitialInfo.setGrantMoneyDateTtp(sysdate);
            }else{
                sysdate.setMonth(sysdate.getMonth() + 1);
                sysdate.setDate(1);
                loanInitialInfo.setGrantMoneyDateTtp(sysdate);
            }
        }
		
		/*NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);*/
		
		/*LoanRepaymentDetail pri = new LoanRepaymentDetail();
		
		double payEterm4Jimu = "积木盒子".equals(loanBase.getFundsSources()) ? jimuheziCalReapyEterm(loanProduct) : 0;
		
		LoanRepaymentDetail c =  null;
		Double returnetermSUM = 0.0;
		Double currentAccrualSum = 0.0;
		
		for(int i =0; i < loanProduct.getTime(); i++) {
			c = new LoanRepaymentDetail();
			
			c.setId(sequencesService.getSequences(SequencesEnum.LOAN_REPAYMENT_DETAIL));
			c.setCurrentTerm(i + 1L);
			c.setCurrentAccrual(BigDecimal.valueOf(currentAccrual(pri,loanProduct,i+1)));
			c.setPrincipalBalance(BigDecimal.valueOf(principalBalance(pri,loanProduct,i+1)));
			c.setGiveBackRate(BigDecimal.valueOf(giveBackRate(loanBase, loanInitialInfo, loanProduct, i+1)));
		    c.setRepaymentAll(BigDecimal.valueOf(repaymentAll(c, loanProduct, i+1)));
		    c.setLoanId(loanBase.getId());
		    c.setDeficit(BigDecimal.valueOf(loanProduct.getPactMoney().doubleValue() * (loanProduct.getAccrualem().doubleValue() + 1/loanProduct.getTime().doubleValue())));
		    c.setReturneterm(c.getDeficit());
		    c.setRepaymentState(RepaymentStateEnum.未还款.name());
		    Date dx = (Date)loanProduct.getStartrdate().clone();
		    dx.setMonth(dx.getMonth() + i);
		    c.setReturnDate(dx);
		    c.setPenaltyDate(dx);
		    double accrualRevise = "积木盒子".equals(loanBase.getFundsSources()) ? jimuheziCalAccrualRevise(payEterm4Jimu, c.getReturneterm().doubleValue(), c.getCurrentAccrual().doubleValue()) : 0;
		    c.setAccrualRevise(BigDecimal.valueOf(accrualRevise));
		    
		    loanRepaymentDetailDao.insert(c);
		    pri = c;
		    
		    returnetermSUM += c.getReturneterm().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		    if (c.getCurrentTerm() < loanProduct.getTime()) {
		    	currentAccrualSum += c.getCurrentAccrual().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		}*/
		
		//提前结清涉及还款计划修改
		Map<String, Object> map = calculator.createLoanRepaymentDetail(loanBase, loanInitialInfo, loanProduct);
		
		loanBaseDao.update(loanBase);
    	loanInitialInfoDao.update(loanInitialInfo);
    	loanProductDao.update(loanProduct);
    	
		/**更新合同信息**/
		updateContract(loanBase, loanInitialInfo, loanProduct);
		
		/**调整精度**/
		//捞财宝，包商银行是从excel计算器取数据，不用调整精度
		if(!"包商银行".equals(loanBase.getLoanBelong()) 
                && !"捞财宝".equals(loanBase.getLoanBelong()) 
                && !"华瑞渤海".equals(loanBase.getLoanBelong()) 
                && !"陆金所".equals(loanBase.getLoanBelong())
                && !"渤海2".equals(loanBase.getLoanBelong())
				&& !"外贸3".equals(loanBase.getLoanBelong())
				&& !"证大P2P".equals(loanBase.getLoanBelong())
				&& !"龙信小贷".equals(loanBase.getLoanBelong())){
			LoanRepaymentDetail c =  (LoanRepaymentDetail)map.get("loanRepaymentDetail");
			Double returnetermSUM = (Double)map.get("returnetermSUM");
			Double currentAccrualSum = (Double)map.get("currentAccrualSum");
			modifyPrecision(returnetermSUM, currentAccrualSum, c, loanBase, loanProduct);
		}
	}
	
	/**
	 * 包商银行放款后调用生成还款计划接口
	 * @throws Exception
	 */
	public void createRepaymentDetailAfterGrantMoney(LoanBase loanBase,LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) {
		logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆包银放款后调取生成还款计划接口");
		loanRepaymentDetailDao.deleteByLoanId(loanInitialInfo.getLoanId());//删除还款计划
		FundsSourcesTypeEnum fundsSourcesTypeEnum = Assert.validEnum(FundsSourcesTypeEnum.class, loanBase.getFundsSources(), "");
		CalculatorBase alculatorBase = CalculatorFactoryImpl.createCalculatorBase(fundsSourcesTypeEnum);
		ICalculator calculator = CalculatorFactoryImpl.createCalculator(fundsSourcesTypeEnum, alculatorBase.getCalculatorVersion());
		calculator.createLoanRepaymentDetail(loanBase, loanInitialInfo, loanProduct);//生成还款计划			
		updateContract(loanBase, loanInitialInfo, loanProduct);//更新合同信息
		loanProductDao.update(loanProduct);//更新loanProduct
	}
	
	/**
	 * 积木盒子利率
	 * @param loanProduct
	 * @return
	 */
	public double jimuheziCalReapyEterm(LoanProduct loanProduct) {
		JimuProduct jimuProduct = jimuProductDao.findByTime(loanProduct.getTime());
		
		double rate4year =  Double.valueOf(jimuProduct.getRateey().toString());
        double rate4month = Math.pow(1+rate4year, 1/12)-1;
        // 已知：期初、期末、月利率、求每月还款额
        double payEterm =  -ExcelFunctionUtil.pmt(rate4month, Double.valueOf(loanProduct.getTime()), Double.valueOf(loanProduct.getPactMoney().toString()), 0);
        double payEterm2 = (double)(Math.round(payEterm * 10000) / 10000.0);//四舍五入，保留4位小数，（中间数值非最终结果）
        return payEterm2;
	}
	
	/**
	 * 对应第三方的贴息或扣息 (积木盒子)
	 * @param payEterm4Jimu
	 * @param returnETerm
	 * @param currentAccrual
	 * @return
	 */
	private double jimuheziCalAccrualRevise(double payEterm4Jimu, double returnETerm, double currentAccrual) {
		double amount = payEterm4Jimu - returnETerm;
        if(amount < 0 && -amount > currentAccrual) {
        	return amount = -currentAccrual;
        }
        return  Double.parseDouble(String.format("%.2f",amount));
	}
	
	/**
	 * 当期利息
	 * 
	 * @param pri
	 * @param loanProduct
	 * @param currentTerm
	 * @return
	 */
	/*private double currentAccrual(LoanRepaymentDetail pri,
			LoanProduct loanProduct, int currentTerm) {

		double returnETerm = Double.valueOf(loanProduct.getPactMoney()
				.toString())
				* (Double.valueOf(loanProduct.getAccrualem().toString()) + 1 / Double
						.valueOf(loanProduct.getTime().toString()));

		double rateYear = ToolUtils.rate(
				Double.valueOf(loanProduct.getPactMoney().toString()),
				returnETerm, loanProduct.getTime());
		if (currentTerm == 1) {
			return rateYear
					* Double.valueOf(loanProduct.getPactMoney().toString());
		}
		if (currentTerm > 1) {
			double r = Double.valueOf(pri.getPrincipalBalance().toString())
					* rateYear;
			if (r > 0) {
				return r;
			} else {
				return 0;
			}
		}
		return 0;
	}*/

	/**
	 * 开始还款日期
	 * @param loanInitialInfo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	private Date getStartReturnDate(LoanInitialInfo loanInitialInfo, String endOfMonthOpened,String fundsSources) throws Exception {
		Date auditDate = loanInitialInfo.getAuditDate();
		SimpleDateFormat dfm= new SimpleDateFormat("yyyy-MM-dd");
		Date date = dfm.parse(dfm.format(new Date()));
		
		if(auditDate.getMonth()==date.getMonth() && "t".equals(endOfMonthOpened)&&fundsSources.equals(FundsSourcesTypeEnum.证大P2P.getValue())){
            if(date.getDate()<16) {
                date.setMonth(date.getMonth()+1);
                date.setDate(16);
            }else{
                date.setMonth(date.getMonth()+2);
                date.setDate(1);
            }
        }else{
        	if (fundsSources.equals(FundsSourcesTypeEnum.捞财宝.getValue())) {
        		date=Dates.addDay(date, 2);
			}
            int promiseReturnDate = date.getDate() < 16 ? 1 : 16;
            date.setDate(promiseReturnDate);
            date.setMonth(date.getMonth() + 1);
        }
		
		return date;
	}

	/**
	 * 本金余额
	 * 
	 * @param pri
	 * @param loanProduct
	 * @param currentTerm
	 * @return
	 */
	/*private double principalBalance(LoanRepaymentDetail pri,
			LoanProduct loanProduct, int currentTerm) {

		double returnETerm = Double.valueOf(loanProduct.getPactMoney()
				.toString())
				* (Double.valueOf(loanProduct.getAccrualem().toString()) + 1 / Double
						.valueOf(loanProduct.getTime()));
		if (currentTerm == 1) {
			return Double.valueOf(loanProduct.getPactMoney().toString())
					- returnETerm + currentAccrual(pri, loanProduct, 1);
		}
		if (currentTerm > 1) {
			double r = Double.valueOf(pri.getPrincipalBalance().toString())
					- returnETerm
					+ currentAccrual(pri, loanProduct, currentTerm);
			if (r > 0) {
				return r;
			} else {
				return 0;
			}
		}
		return 0;
	}*/

	/**
	 * 当期退费
	 * 
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @param currentTeam
	 * @return
	 */
	/*private double giveBackRate(LoanBase loanBase,
			LoanInitialInfo loanInitialInfo, LoanProduct loanProduct,
			int currentTeam) {
		if (loanProduct.getTime() == 6) {
			return 0;
		}
		if (currentTeam >= 1 && currentTeam <= 2) {
			return 0;
		}

		double y3 = 0.0;
		if (FundsSourcesTypeEnum.积木盒子.getValue().equals(
				loanBase.getFundsSources())) {
			y3 = loanProduct.getPactMoney().doubleValue()
					- loanInitialInfo.getMoney().doubleValue()
					- loanProduct.getRisk().doubleValue()
					- loanProduct.getManageRateForPartyC().doubleValue()
					- loanProduct.getPactMoney().doubleValue() * 0.02;
		} else if (FundsSourcesTypeEnum.华澳信托.getValue().equals(
				loanBase.getFundsSources())
				|| FundsSourcesTypeEnum.国民信托.getValue().equals(
						loanBase.getFundsSources())) {
			y3 = loanProduct.getPactMoney().doubleValue()
					- loanInitialInfo.getMoney().doubleValue()
					- loanProduct.getRisk().doubleValue()
					- loanProduct.getManageRateForPartyC().doubleValue()
					- loanProduct.getPactMoney().doubleValue() * 0.1;
		} else {
			y3 = loanProduct.getPactMoney().doubleValue()
					- loanInitialInfo.getMoney().doubleValue()
					- loanProduct.getPactMoney().doubleValue() * 0.1;

			
			 * 以下为不用的老系统代码 def d1215 = new
			 * java.text.SimpleDateFormat('yyyy-MM-dd').parse('2014-12-15');
			 * if(loan.signDate != null && loan.signDate < d1215){
			 * y3=loan.pactMoney-loan.money-loan.risk - loan.pactMoney*0.06 }
			 
		}
    	
    	if (currentTeam == 3) {
			return y3 > 0 ? y3 : 0;
		}else if (currentTeam == 4) {
    		return (y3 - loanProduct.getPactMoney().doubleValue() * 0.03) > 0 ? (y3 - loanProduct.getPactMoney().doubleValue() * 0.03) : 0;
		} else if (currentTeam > 4) {
			double r = y3 - loanProduct.getPactMoney().doubleValue() * 0.03 - loanProduct.getPactMoney().doubleValue() * (currentTeam-4) * 0.01;
			if (r > 0) {
				return r;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}*/

	/**
	 * 一次性还款金额
	 * 
	 * @param detail
	 * @param loanProduct
	 * @param currentTeam
	 * @return
	 */
	/*private double repaymentAll(LoanRepaymentDetail detail,
			LoanProduct loanProduct, int currentTeam) {
		double retrunETerm = loanProduct.getPactMoney().doubleValue()
				* (loanProduct.getAccrualem().doubleValue() + 1 / loanProduct
						.getTime().doubleValue());
		detail.setPenalty(new BigDecimal(0.00));

		if (currentTeam <= 2) {
			return detail.getPrincipalBalance().doubleValue() + retrunETerm
					+ detail.getGiveBackRate().doubleValue();
		} else if (currentTeam >= 3 && currentTeam <= loanProduct.getTime() - 1) {
			detail.setPenalty(BigDecimal.valueOf(loanProduct.getPactMoney()
					.doubleValue() * 0.01));
			return detail.getPrincipalBalance().doubleValue() + retrunETerm
					- detail.getGiveBackRate().doubleValue()
					+ loanProduct.getPactMoney().doubleValue() * 0.01;
		} else {
			return retrunETerm;
		}
	}*/

	/**
	 * 更新合同信息
	 * 
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 */
	private void updateContract(LoanBase loanBase,
			LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) {
		LoanContract contract = loanContractDao.findByLoanId(loanInitialInfo
				.getLoanId());
		if (contract == null) {
			contract = new LoanContract();
			contract.setLoanId(loanBase.getId());
		}
    	
    	PersonInfo b = personInfoDao.get(loanBase.getBorrowerId());
    	
    	ComOrganization organization = comOrganizationDao.get(loanBase.getSalesDepartmentId());
    	
    	SysDictionary dic = sysDictionaryDao.findByCodeTypeAndCodeName("useFormatContract", loanBase.getSalesDepartmentId().toString());
    	
    	boolean useFormatContract = dic != null ? Boolean.parseBoolean(dic.getCodeValue()) : false;
    	
    	contract.setContractNum(loanBase.getContractNum());
    	contract.setBorrowerName(b.getName());
    	contract.setSex(b.getSex());
    	contract.setIdnum(b.getIdnum());
    	contract.setAddress(b.getAddress());
    	contract.setPostcode(b.getPostcode());
    	contract.setEmail(b.getEmail());
    	contract.setSignDate(loanInitialInfo.getSignDate());
    	contract.setStartrdate(loanProduct.getStartrdate());
    	contract.setEndrdate(loanProduct.getEndrdate());
    	contract.setTime(loanProduct.getTime());
    	contract.setSigningSite(useFormatContract ? "上海市浦东新区" : organization.getSite());
    	contract.setPactMoney(loanProduct.getPactMoney());
    	contract.setPromiseReturnDate(loanProduct.getPromiseReturnDate());
    	contract.setReferRate(loanProduct.getReferRate());
    	contract.setEvalRate(loanProduct.getEvalRate());
    	contract.setManageRate(loanProduct.getManageRate());
    	contract.setManagerRateForPartyc(loanProduct.getManageRateForPartyC());
    	contract.setRateSum(loanProduct.getRateSum());
    	contract.setRisk(loanProduct.getRisk());
    	contract.setGiveBackRateFor3term(loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(loanBase.getId(), 3L).getGiveBackRate());
    	contract.setGiveBackRateFor4term(loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(loanBase.getId(), 4L).getGiveBackRate());
    	contract.setGiveBackRateAfter4term(loanProduct.getTime() == 6 ? BigDecimal.valueOf(0) : new BigDecimal(String.format("%.2f",loanProduct.getPactMoney().doubleValue()*0.01)));
    	contract.setServiceTel(organization.getServiceTel());
    	
    	//ProdCreditProductInfo creditProduct = prodCreditProductInfoDao.findByLoanType(loanInitialInfo.getLoanType());
    	ProdCreditProductTerm prodCreditProductTerm = prodCreditProductTermServiceImpl.findBymap(loanProduct.getTime(),loanInitialInfo.getLoanType(),loanBase.getFundsSources());
    	
    	contract.setReturneterm(loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(loanBase.getId(), 2L).getReturneterm());
    	if (loanProduct.getPenaltyRate() == Const.PENALTY_FINE_RATE) {
    		contract.setOverduePenalty1day(new BigDecimal(String.format("%.2f", contract.getReturneterm().doubleValue() * loanProduct.getTime() * loanProduct.getPenaltyRate().doubleValue())));
    		contract.setOverduePenalty15day(new BigDecimal(String.format("%.2f", contract.getReturneterm().doubleValue() * loanProduct.getTime() * loanProduct.getPenaltyRate().doubleValue() * 15)));
		}else if("包商银行".equals(loanBase.getFundsSources())){//包商银行：第一期本金*罚息*天数
    		BigDecimal firstCurrentAccrual = loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(loanBase.getId(), 1L).getCurrentAccrual();//第一期利息
			contract.setOverduePenalty1day(new BigDecimal(String.format("%.2f", contract.getReturneterm().subtract(firstCurrentAccrual).multiply(loanProduct.getPenaltyRate()).doubleValue())));
    		contract.setOverduePenalty15day(new BigDecimal(String.format("%.2f", contract.getReturneterm().subtract(firstCurrentAccrual).multiply(loanProduct.getPenaltyRate()).doubleValue() * 15)));
		}else if(FundsSourcesTypeEnum.渤海2.getValue().equals(loanBase.getFundsSources())){
		    BigDecimal amt = loanProduct.getPactMoney().multiply(prodCreditProductTerm.getPenaltyRate()).setScale(2, RoundingMode.HALF_UP);
		    contract.setOverduePenalty1day(amt);
            contract.setOverduePenalty15day(amt.multiply(new BigDecimal("15")).setScale(2, RoundingMode.HALF_UP));
        }else {
			contract.setOverduePenalty1day(new BigDecimal(String.format("%.2f", loanProduct.getPactMoney().doubleValue() * prodCreditProductTerm.getPenaltyRate().doubleValue())));
    		contract.setOverduePenalty15day(new BigDecimal(String.format("%.2f", loanProduct.getPactMoney().doubleValue() * prodCreditProductTerm.getPenaltyRate().doubleValue() * 15)));
		}
    	
    	contract.setPurpose(loanInitialInfo.getPurpose());
    	
    	contract.setContractVersion("v" + sysParamDefineDao.getSysParamValue("codeHelper", "b_contract_ver_num", false));
    	
    	LoanBank bank = loanBankDao.get(loanBase.getGrantBankId());
    	contract.setBank(bank.getBankName());
    	contract.setBankFullName(bank.getFullName());
    	contract.setAccount(bank.getAccount());
    	contract.setGiveBackBank(bank.getBankName());
    	contract.setGbFullName(bank.getFullName());
    	contract.setGbAccount(bank.getAccount());
    	
    	/* 原系统中代码，已弃用
    	if(loan?.organization?.bankAccountType=="对私账户"){
    		contract.orgName = loan?.organization?.owner
    	}else if(loan?.organization?.bankAccountType=="对公账户"){
            contract.orgName = loan?.organization?.name
        }*/
    	
    	/**写入国民信托合同序列号**/
    	if ("国民信托".equals(loanBase.getFundsSources()) && contract.getZhongTaiSequence() == null) {
			contract.setZhongTaiSequence(sequencesService.getSequences(SequencesEnum.ZHONG_TAI).toString());
		}
    	
    	/**写入信托计划合同序列号**/
    	if ("华澳信托".equals(loanBase.getFundsSources()) && contract.getXtjhSequence() == null) {
			contract.setXtjhSequence(sequencesService.getSequences(SequencesEnum.XTJH).toString());
		}
    	
    	if (!Strings.isEmpty(contract.getId())) {
    		loanContractDao.update(contract);
		} else {
			contract.setId(sequencesService.getSequences(SequencesEnum.LOAN_CONTRACT));
			loanContractDao.insert(contract);
		}
    }
    
    /**
     * 调整精度
     * @param loanBase
     * @param loanProduct
     */
    private void modifyPrecision(double returnetermSUM, double currentAccrualSum,LoanRepaymentDetail lastRd, LoanBase loanBase, LoanProduct loanProduct) {
    	double currentAccrual = returnetermSUM - currentAccrualSum - loanProduct.getPactMoney().doubleValue();
    	lastRd.setCurrentAccrual(BigDecimal.valueOf(currentAccrual).setScale(2, BigDecimal.ROUND_HALF_UP));
    	if ("积木盒子".equals(loanBase.getFundsSources())) {
    		double payEterm4Jimu =  jimuheziCalReapyEterm(loanProduct);
            lastRd.setAccrualRevise(BigDecimal.valueOf(jimuheziCalAccrualRevise(payEterm4Jimu,lastRd.getReturneterm().doubleValue(), lastRd.getCurrentAccrual().doubleValue())));
		}
    	loanRepaymentDetailDao.update(lastRd);
    }
    
	/**
	 * 将合同数据转变成VO
	 * @param c
	 * @param loanInitialInfo
	 * @return
	 */
	private ReturnLoanContractVo toReturnLoanContractVo(LoanContract c, LoanInitialInfo loanInitialInfo) {
		ReturnLoanContractVo vo = new ReturnLoanContractVo();
		
		vo.setContractNum(c.getContractNum());
        vo.setBorrowerName(c.getBorrowerName());
        vo.setBorrowerName2(c.getBorrowerName2());
        vo.setSex(c.getSex());
        vo.setIdnum(c.getIdnum());
        vo.setIdnum2(c.getIdnum2());
        vo.setAddress(c.getAddress());
        vo.setPostcode(c.getPostcode());
        vo.setEmail(c.getEmail());
        vo.setSignDate(c.getSignDate());
        vo.setStartRDate(c.getStartrdate());
        vo.setEndRDate(c.getEndrdate());
        vo.setTime(c.getTime());
        //拼接市，区
        Map<String, Object> loanParam = new HashMap<String, Object>();
        loanParam.put("id",loanInitialInfo.getLoanId());
        List<VLoanInfo> loanList= vLoanInfoDao.findListByMap(loanParam);
        if (loanList.get(0).getFundsSources().equals(FundsSourcesTypeEnum.证大P2P.getValue())) {
        	 Long orgId = loanList.get(0).getSalesDepartmentId();
             Map<String, Object> orgParam = new HashMap<String, Object>();
             orgParam.put("orgId", orgId);
             List<Map<String, Object>> orgList = comOrganizationDao.findOrganization(orgParam);
             Map<String, Object> orgMap = orgList.get(0);
             String city = (String) orgMap.get("CITY");
             String zone = (String)orgMap.get("ZONE");
             if (StringUtils.isBlank(city)) {
				city = "一市";
			}
             if (StringUtils.isBlank(zone)) {
            	 zone = "一区";
 			}
             vo.setSigningSite(city+zone);
		} else {
			 vo.setSigningSite(c.getSigningSite());
		}
        vo.setPactMoney(c.getPactMoney());
        vo.setReturnETerm(c.getReturneterm());
        vo.setPromiseReturnDate(c.getPromiseReturnDate());

        vo.setReferRate(c.getReferRate());
        vo.setEvalRate(c.getEvalRate());
        vo.setManageRate(c.getManageRate());
        vo.setRisk(c.getRisk());
        vo.setManagerRateForPartyC(c.getManagerRateForPartyc());
        vo.setRateSum(c.getRateSum());
        vo.setGiveBackRateFor3Term(c.getGiveBackRateFor3term());
        vo.setGiveBackRateFor4Term(c.getGiveBackRateFor4term());
        vo.setGiveBackRateAfter4Term(c.getGiveBackRateAfter4term());

        vo.setServiceTel(c.getServiceTel());
        vo.setOverduePenalty1Day(c.getOverduePenalty1day());
        vo.setOverduePenalty15Day(c.getOverduePenalty15day());

        vo.setPurpose(c.getPurpose());
        
        vo.setBank(c.getBank());//银行名称
        vo.setBankFullName(c.getBankFullName());//银行支行
        vo.setGiveBackBank(c.getGiveBackBank());//还款银行
        vo.setGbFullName(c.getGbFullName());//还款支行
        vo.setGbAccount(c.getGbAccount());
        
        vo.setReturnETermForT1(c.getReturnetermFort1());
        vo.setTimeForT1(c.getTimeFort1());
        vo.setStartRDateForT1(c.getStartrdateFort1());
        vo.setEndRDateForT1(c.getEndrdateFort1());
        vo.setReturnETermForT2(c.getReturnetermFort2());
        vo.setTimeForT2(c.getTimeFort2());
        vo.setStartRDateForT2(c.getStartrdateFort2());
        vo.setEndRDateForT2(c.getEndrdateFort2());
        vo.setOrgName(c.getOrgName());
        vo.setZhongTaiSequence(c.getZhongTaiSequence());
        vo.setXtjhSequence(c.getXtjhSequence());

        vo.setLoanId(loanInitialInfo.getLoanId());
		
		BigDecimal backRate = c.getPactMoney().subtract(loanInitialInfo.getMoney());
		//vo.setGiveBackRateFor1Term(backRate.divide(BigDecimal.valueOf(c.getTime()), 4).setScale(2, BigDecimal.ROUND_HALF_UP));
		vo.setGiveBackRateFor1Term(backRate.divide(BigDecimal.valueOf(c.getTime()), 2,RoundingMode.HALF_UP));
		return vo;
	}
	
	/**
	 * 将债权信息转变VO
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @param contract
	 * @return
	 */
	private ReturnLoanVo toReturnLoanVo(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct, LoanContract contract) {
		ReturnLoanVo loanVo = new ReturnLoanVo();
		
		if(loanProduct.getRateey()==null){
			loanVo.setRateey("0");
		}else{
			loanVo.setRateey(String.format("%.4f", loanProduct.getRateey()));// 年化利率
		}
		if(loanProduct.getRate()==null){
			loanVo.setRate("0");
		}else{
			loanVo.setRate(String.format("%.4f", loanProduct.getRate()==null?0:loanProduct.getRate()));// 借款费率
		}
		
		loanVo.setPactMoney(String.format("%.2f", loanProduct.getPactMoney()));//借款金额
		loanVo.setGrantMoney(String.format("%.2f", loanInitialInfo.getMoney()));//放款金额
		
		PersonInfo borrower = personInfoDao.get(loanBase.getBorrowerId());
        PersonThirdPartyAccount tpa = personThirdPartyAccountDao.get(borrower.getThirdPartyAccountId());
        
        loanVo.setThirdPartyId(tpa == null ? "" : tpa.getThirdPartyId());//华澳信托:甲方指定其在畅捷支付开设的虚拟账户
        
        if (loanBase.getFundsSources().equals("积木盒子")) {
        	loanVo.setManagerRateForPartyCFinance(String.format("%.2f", loanProduct.getManageRateForPartyC().multiply(BigDecimal.valueOf(0.2))));//积木盒子：融资服务费：loan.getManagerRateForPartyC()*0.2
            loanVo.setManagerRateForPartyCTechnology(String.format("%.2f", loanProduct.getManageRateForPartyC().multiply(BigDecimal.valueOf(0.8))));//积木盒子：技术服务费：loan.getManagerRateForPartyC() - loan.getManagerRateForPartyC()*0.2
		} else {
			loanVo.setManagerRateForPartyCFinance("0");//积木盒子：融资服务费：loan.getManagerRateForPartyC()*0.2
	        loanVo.setManagerRateForPartyCTechnology("0");//积木盒子：技术服务费：loan.getManagerRateForPartyC() - loan.getManagerRateForPartyC()*0.2
		}

        loanVo.setTime(loanProduct.getTime());//贷款期限
       
        loanVo.setAccrualem(String.format("%.4f", loanProduct.getAccrualem()));//补偿利率
        if(FundsSourcesTypeEnum.外贸信托.getValue().equals(loanBase.getFundsSources())){
        	loanVo.setRateEM(String.format("%.4f", loanProduct.getRateem()));//贷款月利率
        }else if(FundsSourcesTypeEnum.渤海2.getValue().equals(loanBase.getFundsSources()) || FundsSourcesTypeEnum.华瑞渤海.getValue().equals(loanBase.getFundsSources())){
        	loanVo.setRateEM(String.format("%.4f", loanProduct.getRateem()));//贷款月利率
        }else{
        	loanVo.setRateEM(String.format("%.2f", loanProduct.getRateem()));//贷款月利率
        }
		
        loanVo.setRepaymentWay(ReturnLoanVo.HUAAO_REPAYMENT_WAY);//华澳信托：还款方式
        loanVo.setHuaAoThirdPartyId(ReturnLoanVo.HUAAO_THIRD_PARTY_ID);//虚拟账户

        loanVo.setFirstLegalRepresentative(ReturnLoanVo.FIRST_LEGAL_REPRESENTATIVE);
        loanVo.setFirstAddress(ReturnLoanVo.FIRST_ADDRESS);
        loanVo.setFirstPostcode(ReturnLoanVo.FIRST_POSTCODE);
        loanVo.setThirdLegalRepresentative(ReturnLoanVo.THIRD_LEGAL_REPRESENTATIVE);
        loanVo.setThirdAddress(ReturnLoanVo.THIRD_ADDRESS);
        loanVo.setThirdPostcode(ReturnLoanVo.THIRD_POSTCODE);
        loanVo.setMphone(borrower.getMphone());

        String peiOuName = "";
        PersonContactInfo personContactInfo = new PersonContactInfo();
        personContactInfo.setPersonId(borrower.getId());
        List<PersonContactInfo> contacts = personContactInfoDao.findListByVo(personContactInfo);
        for(PersonContactInfo contact : contacts){
            if (contact.getContactType().equals("配偶")) {
                peiOuName = contact.getName();
            }
        }
        loanVo.setPeiOuName(peiOuName);

        loanVo.setRepaymentTotal(String.format("%.2f",contract.getReturneterm().multiply(BigDecimal.valueOf(contract.getTime()))));//国民信托：需偿还的贷款本息总额
        loanVo.setMonthlyRepayment(String.format("%.2f",contract.getReturneterm()));//月偿还本息数额
       
        /** 合同来源 枚举 **/
		FundsSourcesTypeEnum fundsSourcesTypeEnum = Assert.validEnum(FundsSourcesTypeEnum.class, loanBase.getFundsSources(), "");
		
		CalculatorBase alculatorBase = CalculatorFactoryImpl.createCalculatorBase(fundsSourcesTypeEnum);
		loanProduct.setCalculatorType(alculatorBase.getCalculatorVersion().getValue());
        int loanType = alculatorBase.getLoanType(loanInitialInfo.getLoanType());
        loanVo.setLoanType(String.valueOf(loanType));
        if (loanBase.getFundsSources().equals("华澳信托")) {
            loanVo.setCorpName(ReturnLoanVo.HUAAO_CORP_NAME);
            loanVo.setCorpBankName(ReturnLoanVo.HUAAO_BANK_NAME);
            loanVo.setCorpBankAccount(ReturnLoanVo.HUAAO_BANK_ACCOUNT);
        } else if (loanBase.getFundsSources().equals("国民信托")) {
            loanVo.setCorpName(ReturnLoanVo.GUOMIN_CORP_NAME);
            loanVo.setCorpBankName(ReturnLoanVo.GUOMIN_BANK_NAME);
            loanVo.setCorpBankAccount(ReturnLoanVo.GUOMIN_BANK_ACCOUNT);
        } else {
            loanVo.setCorpName("");
            loanVo.setCorpBankName("");
            loanVo.setCorpBankAccount("");
        }

        return loanVo;
	}
	
	/**
	 * 将还款计划都domain List转变为Vo List
	 * @param repaymentDetails
	 * @return
	 */
	private List<ReturnRepaymentDetailVo> toReturnRepaymentDetailVo(List<LoanRepaymentDetail> repaymentDetails) {
		List<ReturnRepaymentDetailVo> list = new ArrayList<ReturnRepaymentDetailVo>();
        for (LoanRepaymentDetail rDetail : repaymentDetails) {
        	ReturnRepaymentDetailVo rDetailVO = new ReturnRepaymentDetailVo();
            rDetailVO.setCurrentAccrual(rDetail.getCurrentAccrual().setScale(2,BigDecimal.ROUND_HALF_UP));//当期利息
            rDetailVO.setCurrentTerm(rDetail.getCurrentTerm());//当前期数
            rDetailVO.setGiveBackRate(rDetail.getGiveBackRate().setScale(2,BigDecimal.ROUND_HALF_UP));//一次性还款退费
            rDetailVO.setLoanId(rDetail.getLoanId());//借款ID
            rDetailVO.setPrincipalBalance(rDetail.getPrincipalBalance().setScale(2,BigDecimal.ROUND_HALF_UP));//本金余额
            rDetailVO.setRepaymentAll(rDetail.getRepaymentAll().setScale(2,BigDecimal.ROUND_HALF_UP));//一次性还款金额
            rDetailVO.setReturnDate(rDetail.getReturnDate());//还款日期
            rDetailVO.setDeficit(rDetail.getDeficit().setScale(2,BigDecimal.ROUND_HALF_UP));//剩余欠款,用于记录不足额部分
            rDetailVO.setRepaymentState(rDetail.getRepaymentState());//当前还款状态
            rDetailVO.setFactReturnDate(rDetail.getFactReturnDate());//结清日期
            rDetailVO.setPenaltyDate(rDetail.getPenaltyDate());//罚息起算日期
            rDetailVO.setReturneterm(rDetail.getReturneterm().setScale(2,BigDecimal.ROUND_HALF_UP));//每期还款金额
            rDetailVO.setPenalty(rDetail.getPenalty().setScale(2,BigDecimal.ROUND_HALF_UP));//违约金
            rDetailVO.setAccrualRevise(rDetail.getAccrualRevise().setScale(2,BigDecimal.ROUND_HALF_UP));//对应第三方的贴息或扣息 (积木盒子)
            list.add(rDetailVO);
        }
        return list;
	}

	/**
	 * 更新借款状态接口
	 * @param params
	 * @return
	 */
	@Override
	@Transactional
	public Map<String, Object> updateLoanState(LoanVo params) {
		
		String appNo = params.getAppNo();
		String stateCode = params.getStateCode();
		String userCode = params.getUserCode();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if (null != appNo && null != stateCode && null != userCode) {
			LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByAppNo(appNo);
			if (null == loanInitialInfo) {
				resultMap.put("code", Constants.DATA_ERR_CODE);
				resultMap.put("message", "失败：appNo输入有误，未查到对应记录");
				return resultMap;
			}
			
			LoanBase loanBase = loanBaseDao.findByLoanId(loanInitialInfo.getLoanId());
			ComEmployee operator = comEmployeeDao.findEmployeeByUserCode(userCode);
			if (null != loanBase) {
				if (loanBase.getLoanState().equals(LoanStateEnum.正常.getValue()) && loanBase.getLoanFlowState().equals(LoanStateEnum.正常.getValue())) {
					resultMap.put("code",Constants.OTHER_ERR_CODE);
                    resultMap.put("message","当前状态已为正常，不允许再进行操作！");
				} else {
					if (null != operator) {
						
						/**构建日志信息**/
						LoanProcessHistory loanProcessHistory = new LoanProcessHistory();
						loanProcessHistory.setLoanId(loanBase.getId());
						loanProcessHistory.setCreator(operator.getId().toString());
						loanProcessHistory.setCreateTime(new Date());
						// 合同来源
						String fundsSource = loanBase.getFundsSources();
						switch (stateCode) {
						case "0010"://合同确认
							if (FundsSourcesTypeEnum.挖财2.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.海门小贷.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.龙信小贷.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.海门小贷.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.外贸信托.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.渤海信托.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.外贸2.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.华瑞渤海.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.外贸3.getValue().equals(fundsSource)) {
								// loanInitialInfo.setGrantMoneyDate(new Date());
								loanInitialInfo.setContractDate(new Date());
								loanInitialInfoDao.update(loanInitialInfo);
							}
							loanBase.setLoanState(LoanStateEnum.通过.getValue());
							loanBase.setLoanFlowState(LoanFlowStateEnum.财务审核.getValue());
							loanBaseDao.update(loanBase);
							
							//记录日志
							loanProcessHistory.setContent("合同确认");
							loanProcessHistory.setLoanFlowState(LoanFlowStateEnum.合同确认.getValue());
							loanProcessHistory.setLoanState(LoanStateEnum.通过.getValue());
							loanProcessHistoryService.insert(loanProcessHistory);
							
							resultMap.put("code", Constants.SUCCESS_CODE);
							resultMap.put("message", "借款状态更新成功");
							break;
						case "0011"://合同退回
							loanBase.setLoanState(LoanStateEnum.通过.getValue());
							loanBase.setLoanFlowState(LoanFlowStateEnum.网点经理退回.getValue());
							loanBaseDao.update(loanBase);
							
							//记录日志
							loanProcessHistory.setContent("网点经理退回");
							loanProcessHistory.setLoanFlowState(LoanFlowStateEnum.网点经理退回.getValue());
							loanProcessHistory.setLoanState(LoanStateEnum.通过.getValue());
							loanProcessHistoryService.insert(loanProcessHistory);
							
							resultMap.put("code", Constants.SUCCESS_CODE);
							resultMap.put("message", "借款状态更新成功");
							break;
						case "0020"://财务审核通过
							loanBase.setLoanState(LoanStateEnum.通过.getValue());
							loanBase.setLoanFlowState(LoanFlowStateEnum.财务放款.getValue());
							loanBaseDao.update(loanBase);
							
							//记录日志
							loanProcessHistory.setContent("财务审核");
							loanProcessHistory.setLoanFlowState(LoanFlowStateEnum.财务审核.getValue());
							loanProcessHistory.setLoanState(LoanStateEnum.通过.getValue());
							loanProcessHistoryService.insert(loanProcessHistory);
							
							resultMap.put("code", Constants.SUCCESS_CODE);
							resultMap.put("message", "借款状态更新成功");
							break;
						case "0021"://财务审核退回
							loanBase.setLoanState(LoanStateEnum.通过.getValue());
							loanBase.setLoanFlowState(LoanFlowStateEnum.财务审核退回.getValue());
							if (FundsSourcesTypeEnum.挖财2.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.外贸信托.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.龙信小贷.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.外贸2.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.包商银行.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.华瑞渤海.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.渤海信托.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.海门小贷.getValue().equals(fundsSource)){
								loanBase.setBatchNum(null);
							}
							loanBaseDao.updateAPSLoanState(loanBase);
							
							//记录日志
							loanProcessHistory.setContent("财务审核退回");
							loanProcessHistory.setLoanFlowState(LoanFlowStateEnum.财务审核退回.getValue());
							loanProcessHistory.setLoanState(LoanStateEnum.通过.getValue());
							loanProcessHistoryService.insert(loanProcessHistory);
							
							resultMap.put("code", Constants.SUCCESS_CODE);
							resultMap.put("message", "借款状态更新成功");
							break;
						case "0022"://财务放款退回
							loanBase.setLoanState(LoanStateEnum.通过.getValue());
							loanBase.setLoanFlowState(LoanFlowStateEnum.财务放款退回.getValue());
							if (FundsSourcesTypeEnum.挖财2.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.外贸信托.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.龙信小贷.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.外贸2.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.包商银行.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.华瑞渤海.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.渤海信托.getValue().equals(fundsSource)
								|| FundsSourcesTypeEnum.海门小贷.getValue().equals(fundsSource)) {
								loanBase.setBatchNum(null);
							}
							loanBaseDao.updateAPSLoanState(loanBase);
							
							//记录日志
							loanProcessHistory.setContent("财务放款退回");
							loanProcessHistory.setLoanFlowState(LoanFlowStateEnum.财务放款退回.getValue());
							loanProcessHistory.setLoanState(LoanStateEnum.通过.getValue());
							loanProcessHistoryService.insert(loanProcessHistory);
							
							// 如果是外贸2还需更新放款申请状态
							if (FundsSourcesTypeEnum.外贸2.getValue().equals(fundsSource) || FundsSourcesTypeEnum.外贸3.getValue().equals(fundsSource)) {
								financeGrantService.updateWaiMao2FinanceGrantApplyReturn(loanBase.getContractNum());
							}
							resultMap.put("code", Constants.SUCCESS_CODE);
							resultMap.put("message", "借款状态更新成功");
							break;
						case "0030"://拒绝
							loanBase.setLoanState(LoanStateEnum.拒绝.getValue());
							loanBase.setLoanFlowState(LoanFlowStateEnum.拒绝.getValue());
							if ("挖财2".equals(loanBase.getFundsSources())) {
								loanBase.setBatchNum(null);
							}
							loanBaseDao.updateAPSLoanState(loanBase);//取消、拒绝单独写一个更新方法
							
							//记录日志
							loanProcessHistory.setContent("拒绝");
							loanProcessHistory.setLoanFlowState(LoanFlowStateEnum.拒绝.getValue());
							loanProcessHistory.setLoanState(LoanStateEnum.拒绝.getValue());
							loanProcessHistoryService.insert(loanProcessHistory);
							
							resultMap.put("code", Constants.SUCCESS_CODE);
							resultMap.put("message", "借款状态更新成功");
							break;
						case "0031"://取消
							loanBase.setLoanState(LoanStateEnum.取消.getValue());
							loanBase.setLoanFlowState(LoanFlowStateEnum.取消.getValue());
							if ("挖财2".equals(loanBase.getFundsSources())) {
								loanBase.setBatchNum(null);
							}
							loanBaseDao.updateAPSLoanState(loanBase);//取消、拒绝单独写一个更新方法
							
							//记录日志
							loanProcessHistory.setContent("取消");
							loanProcessHistory.setLoanFlowState(LoanFlowStateEnum.取消.getValue());
							loanProcessHistory.setLoanState(LoanStateEnum.取消.getValue());
							loanProcessHistoryService.insert(loanProcessHistory);
							
							resultMap.put("code", Constants.SUCCESS_CODE);
							resultMap.put("message", "借款状态更新成功");
							break;
						case "0040"://合同签订
							loanBase.setLoanState(LoanStateEnum.通过.getValue());
							loanBase.setLoanFlowState(LoanFlowStateEnum.合同确认.getValue());
							loanBaseDao.update(loanBase);
							if(fundsSource.equals(FundsSourcesTypeEnum.陆金所.getValue())){
								loanInitialInfo.setSignDate(Dates.getCurrDate());
								loanInitialInfoDao.update(loanInitialInfo);
							}
							//记录日志
							loanProcessHistory.setContent("合同签订");
							loanProcessHistory.setLoanFlowState(LoanFlowStateEnum.合同签订.getValue());
							loanProcessHistory.setLoanState(LoanStateEnum.通过.getValue());
							loanProcessHistoryService.insert(loanProcessHistory);
							
							resultMap.put("code", Constants.SUCCESS_CODE);
							resultMap.put("message", "借款状态更新成功");
							break;
						default:
							resultMap.put("code", Constants.DATA_ERR_CODE);
							resultMap.put("message", "失败:传入状态码有误");
							break;
						}
					} else {
						resultMap.put("code", Constants.DATA_ERR_CODE);
						resultMap.put("message", "失败：userCode输入有误，无法找到对应记录");
					}
				}
			} else {
				resultMap.put("code", Constants.DATA_ERR_CODE);
				resultMap.put("message", "失败：appNo输入有误，未查到对应记录");
			}
		} else {
			resultMap.put("code", Constants.DATA_ERR_CODE);
			resultMap.put("message", "失败:缺少必要参数");
		}
		return resultMap;
	}
	
	/**
	 * 批量更新借款状态接口
	 * @param params
	 * @return
	 */
	@Override
	@Transactional
	public Map<String, Object> batchUpdateLoanState(LoanVo params) {
		
		
		List<String> appNos = Arrays.asList(params.getAppNos().split(","));
		List<String> stateCodes = Arrays.asList(params.getStateCodes().split(","));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if (null != appNos && null != stateCodes && appNos.size() == stateCodes.size() && appNos.size() > 0) {
			
			List<UpdateFailVO> updateFailVOs = new ArrayList<UpdateFailVO>();
            resultMap.put("code",Constants.SUCCESS_CODE);
            resultMap.put("message","借款状态更新成功");
			
			for (int i=0; i<appNos.size();i++) {
				String appNo = appNos.get(i);
                String stateCode = stateCodes.get(i);
                
                LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByAppNo(appNo);
    			if (null == loanInitialInfo) {
    				UpdateFailVO updateFailVO = new UpdateFailVO();
                    updateFailVO.setAppNo(appNo);
                    updateFailVO.setErrorMessage("失败：appNo输入有误，未查到对应记录");
                    updateFailVOs.add(updateFailVO);
    			} else {
    				LoanBase loanBase = loanBaseDao.findByLoanId(loanInitialInfo.getLoanId());
    				
    				if (null != loanBase) {
    					if (loanBase.getLoanState().equals(LoanStateEnum.正常.getValue()) && loanBase.getLoanFlowState().equals(LoanStateEnum.正常.getValue())) {
    						UpdateFailVO updateFailVO = new UpdateFailVO();
                            updateFailVO.setAppNo(appNo);
                            updateFailVO.setErrorMessage("当前状态已为正常，不允许再进行操作！");
                            updateFailVOs.add(updateFailVO);
						} else {
							switch (stateCode) {
							case "0030"://拒绝
								loanBase.setLoanState(LoanStateEnum.拒绝.getValue());
								loanBase.setLoanFlowState(LoanFlowStateEnum.拒绝.getValue());
								if ("挖财2".equals(loanBase.getFundsSources())) {
									loanBase.setBatchNum(null);
								}
								loanBaseDao.updateAPSLoanState(loanBase);//取消、拒绝单独写一个更新方法
								break;
							case "0031"://取消
								loanBase.setLoanState(LoanStateEnum.取消.getValue());
								loanBase.setLoanFlowState(LoanFlowStateEnum.取消.getValue());
								if ("挖财2".equals(loanBase.getFundsSources())) {
									loanBase.setBatchNum(null);
								}
								loanBaseDao.updateAPSLoanState(loanBase);//取消、拒绝单独写一个更新方法
								break;
							default:
								UpdateFailVO updateFailVO = new UpdateFailVO();
                                updateFailVO.setAppNo(appNo);
                                updateFailVO.setErrorMessage("失败:传入状态码有误");
                                updateFailVOs.add(updateFailVO);
								break;
							}
						}
    				} else {
    					UpdateFailVO updateFailVO = new UpdateFailVO();
                        updateFailVO.setAppNo(appNo);
                        updateFailVO.setErrorMessage("失败：appNo输入有误，未查到对应记录");
                        updateFailVOs.add(updateFailVO);
    				}
    			}
			}
			
			resultMap.put("updateFailVos", updateFailVOs);
		} else {
			resultMap.put("code",Constants.DATA_ERR_CODE);
            resultMap.put("message","失败:输入的参数有误");
		}
		return resultMap;
	}

	/**
	 * 还款状态查询接口
	 * @param params
	 * @return
	 */
    @Override
    public Map<String, Object> paymentStatus(PersonVo params) {
		PersonInfo personInfo = personInfoDao.findByIdnum(params.getIdnum());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String status = null;
		
		if (personInfo != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("borrowerId", personInfo.getId());
			List<VLoanInfo> vLoanInfos = vLoanInfoDao.findListByBorrowId(paramMap);
			VLoanInfo vLoanInfo = null;
			if (vLoanInfos != null && vLoanInfos.size() > 0) {
				/*Collections.sort(vLoanInfos, new Comparator<VLoanInfo>() {
					@Override
					public int compare(VLoanInfo loan1, VLoanInfo loan2) {
						// TODO Auto-generated method stub
						return loan2.getId().compareTo(loan1.getId());
					}
				});
				*/
				vLoanInfo = vLoanInfos.get(0);
			}
			
			if (vLoanInfo != null) {
				status = vLoanInfo.getLoanState();
				
				LoanRepaymentDetail loanRepaymentDetail = null;
				if (LoanStateEnum.结清.getValue().equals(status) || LoanStateEnum.预结清.getValue().equals(status)) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("loanId", vLoanInfo.getId());
					param.put("currentTerm", vLoanInfo.getTime());
					List<LoanRepaymentDetail> loanRepaymentDetails = loanRepaymentDetailDao.findListByMap(param);
					if (loanRepaymentDetails != null && loanRepaymentDetails.size() > 0) {
						loanRepaymentDetail = loanRepaymentDetails.get(0);
					}
				}
				
				if (loanRepaymentDetail != null && LoanStateEnum.结清.getValue().equals(status)) {
	                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	                String str = sdf.format(loanRepaymentDetail.getFactReturnDate());
	                resultMap.put("updateTime",str);
	                if (vLoanInfo.getAppNo() == null) {
	                	resultMap.put("isOldData","Y");
					} else {
						resultMap.put("isOldData","N");
					}
	            }
	            if (loanRepaymentDetail != null && LoanStateEnum.预结清.getValue().equals(status)) {
	                SimpleDateFormat sdf=new SimpleDateFormat("dd");
	                String str = sdf.format(loanRepaymentDetail.getReturnDate());
	                resultMap.put("repayDate",str);
	                if (vLoanInfo.getAppNo() == null) {
	                	resultMap.put("isOldData","Y");
					} else {
						resultMap.put("isOldData","N");
					}
	            }
				
	            ComOrganization organization = comOrganizationDao.get(vLoanInfo.getSalesDepartmentId());
	            
				resultMap.put("status", status);
				resultMap.put("salesDepartmentCode", organization != null ? organization.getOrgCode() : "");
				resultMap.put("code", "000000");
				resultMap.put("msg", "该用户在系统中");
			} else {
				resultMap.put("code","000000");
                resultMap.put("msg","该用户在系统中,但无债权信息");
			}
		} else {
			resultMap.put("code", "000000");
			resultMap.put("msg", "该用户不在系统中");
		}
		return resultMap;
    }
    
    /**
	 * 客户债权状态查询接口
	 * @param params
	 * @return
	 */
    @Override
    public Map<String, Object> loanStatus(PersonVo params) {
		PersonInfo personInfo = personInfoDao.findByIdnum(params.getIdnum());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if (personInfo != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("borrowerId", personInfo.getId());
			List<VLoanInfo> vLoanInfos = vLoanInfoDao.findListByMap(paramMap);
			if (vLoanInfos != null && vLoanInfos.size() > 0) {
				Collections.sort(vLoanInfos, new Comparator<VLoanInfo>() {
					@Override
					public int compare(VLoanInfo loan1, VLoanInfo loan2) {
						return loan1.getId().compareTo(loan2.getId());
					}
				});
			}
			
			if (vLoanInfos != null) {
				List<LoanBaseVo> loanBaseVos = new ArrayList<LoanBaseVo>();
				for (Iterator<VLoanInfo> iter = vLoanInfos.iterator(); iter.hasNext();) {
					VLoanInfo vLoanInfo = iter.next();
					
					LoanBaseVo loanBaseVo = new LoanBaseVo();
					loanBaseVo.setId(vLoanInfo.getId());
					loanBaseVo.setLoanState(vLoanInfo.getLoanState());
					if (LoanStateEnum.结清.getValue().equals(loanBaseVo.getLoanState())) {
						LoanRepaymentDetail endLoanRepaymentDetailvo= loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(loanBaseVo.getId(),vLoanInfo.getTime());
						if(endLoanRepaymentDetailvo.getFactReturnDate()==null||" ".equals(endLoanRepaymentDetailvo.getFactReturnDate())){
							logger.info("该笔结清债权factReturnDate为null--!");
						}
						loanBaseVo.setCreateTime(endLoanRepaymentDetailvo.getFactReturnDate());
					} else {
						loanBaseVo.setCreateTime(vLoanInfo.getCreateTime());
					}
					loanBaseVos.add(loanBaseVo);
				}
	            
				resultMap.put("code", "000000");
				resultMap.put("msg", "该用户在系统中有债权信息");
				resultMap.put("loan", loanBaseVos);
			} else {
				resultMap.put("code","000000");
                resultMap.put("msg","该用户在系统中,但无债权信息");
			}
		} else {
			resultMap.put("code", "000000");
			resultMap.put("msg", "该用户不在系统中");
		}
		return resultMap;
    }

    /**
	 * 贷前试算接口
	 * @param params
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> createLoanTrial(LoanTrialVo params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//通过code得到合同来源的中文name
		String fundsSources = FundsSourcesTypeEnumUtil.getName(params.getFundsSources());
		//通过code得到债权的中文name
		String loanType = LoanTypeEnumUtil.getName(params.getLoanType());

		LoanBase loanBase = new LoanBase();
		LoanInitialInfo loanInitialInfo = new LoanInitialInfo();
		LoanProduct loanProduct = new LoanProduct();
 		
		loanBase.setFundsSources(fundsSources);
		loanInitialInfo.setMoney(params.getMoney());
		loanProduct.setTime(params.getTime());

		if(null!=loanType) {
			loanInitialInfo.setLoanType(loanType);
		} else {
			loanInitialInfo.setLoanType((String) FieldValueConvertor.getInstance().convert(params.getLoanType(), "tmAppMain.productCd", FieldNameType.aps4json));
		}
		/*if ("00003".equals(params.getLoanType())) {
			loanInitialInfo.setLoanType(LoanTypeEnum.随意贷A.getValue());
        } else if ("00004".equals(params.getLoanType())) {
        	loanInitialInfo.setLoanType(LoanTypeEnum.随意贷B.getValue());
        } else if ("00005".equals(params.getLoanType())) {
        	loanInitialInfo.setLoanType(LoanTypeEnum.随意贷C.getValue());
        } else {
        	loanInitialInfo.setLoanType((String)FieldValueConvertor.getInstance().convert(params.getLoanType(), "tmAppMain.productCd", FieldNameType.aps4json));
        }*/
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(FundsSourcesTypeEnum.捞财宝.getValue().equals(fundsSources)){
			loanInitialInfo.setSignDate(dateFormat.parse(dateFormat.format(Dates.addDay(Dates.addMonths(params.getFirstRepaymentDate(),-1),-2))));
		}else{
			loanInitialInfo.setSignDate(dateFormat.parse(dateFormat.format(new Date())));
		}
		
		ProdCreditProductTerm prodCreditProductTerm = prodCreditProductTermServiceImpl.findBymap(loanProduct.getTime(),loanInitialInfo.getLoanType(),loanBase.getFundsSources());
		if (null == prodCreditProductTerm) {
			resultMap.put("code", Constants.DATA_ERR_CODE);
			resultMap.put("message", "失败：没有查询到产品信息");
			return resultMap;
		}
		//是优惠综合费率客户
		if("Y".equals(params.getIsRatePreferLoan())){
			if(!Strings.isEmpty(prodCreditProductTerm.getReloanRate())){
				prodCreditProductTerm.setRate(prodCreditProductTerm.getReloanRate());
				prodCreditProductTerm.setAccrualem(prodCreditProductTerm.getReloanAccrualem());
				loanInitialInfo.setIsRatePreferLoan("Y");
			}else{
				throw new Exception("不存在优惠费率！");
			}
		}
		
		/*ICalculatorService iCalculatorService = AdaptiveFundsSoures.getICalculatorServiceByFundsSoures(loanBase.getFundsSources());
		iCalculatorService.updateRate(loanBase, loanInitialInfo, loanProduct, prodCreditProductTerm);*/
		Date d =  params.getFirstRepaymentDate();

		/**设置开始还款日**/
		if (d.getDate() >= 1 && d.getDate() <= 15) {
			loanProduct.setPromiseReturnDate(1L);
		} else {
			loanProduct.setPromiseReturnDate(16L);
		}
		loanProduct.setStartrdate(d);

		d = (Date) d.clone();
		d.setMonth(d.getMonth() + Integer.valueOf(loanProduct.getTime().toString()) - 1);
		loanProduct.setEndrdate(d);
		//提前结清计算器修改
		/** 合同来源 枚举 **/
		FundsSourcesTypeEnum fundsSourcesTypeEnum = Assert.validEnum(FundsSourcesTypeEnum.class, loanBase.getFundsSources(), "");
		
		CalculatorBase alculatorBase = CalculatorFactoryImpl.createCalculatorBase(fundsSourcesTypeEnum);
		ICalculator calculator = CalculatorFactoryImpl.createCalculator(fundsSourcesTypeEnum, alculatorBase.getCalculatorVersion());
		calculator.updateRate(loanBase, loanInitialInfo, loanProduct, prodCreditProductTerm);
		
		/*NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		
		LoanRepaymentDetail pri = new LoanRepaymentDetail();
		List<LoanRepaymentDetail> repaymentDetails = new ArrayList<LoanRepaymentDetail>();
		for(int i =0; i < loanProduct.getTime(); i++) {
			LoanRepaymentDetail c = new LoanRepaymentDetail();
			
			c.setCurrentTerm(i + 1L);
			c.setCurrentAccrual(BigDecimal.valueOf(currentAccrual(pri,loanProduct,i+1)));
			c.setPrincipalBalance(BigDecimal.valueOf(principalBalance(pri,loanProduct,i+1)));
			c.setGiveBackRate(BigDecimal.valueOf(giveBackRate(loanBase, loanInitialInfo, loanProduct, i+1)));
		    c.setRepaymentAll(BigDecimal.valueOf(repaymentAll(c, loanProduct, i+1)));
		    c.setDeficit(BigDecimal.valueOf(loanProduct.getPactMoney().doubleValue() * (loanProduct.getAccrualem().doubleValue() + 1/loanProduct.getTime().doubleValue())));
		    c.setReturneterm(c.getDeficit());
		    c.setRepaymentState(RepaymentStateEnum.未还款.name());
		    Date dx = (Date)loanProduct.getStartrdate().clone();
		    dx.setMonth(dx.getMonth() + i);
		    c.setReturnDate(dx);
		    c.setPenaltyDate(dx);
		    
		    repaymentDetails.add(c);
		    pri = c;
		}*/
		
		//提前结清贷涉及贷前试算修改
		List<LoanRepaymentDetail> repaymentDetails = calculator.createLoanTrial(loanBase, loanInitialInfo, loanProduct);
		
		List<ReturnLoanTrialVo> list = new ArrayList<ReturnLoanTrialVo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (LoanRepaymentDetail rDetail : repaymentDetails) {
			ReturnLoanTrialVo rDetailVO = new ReturnLoanTrialVo();
			rDetailVO.setCurrentTerm(rDetail.getCurrentTerm()); // 当前期数
			rDetailVO.setReturnDate(sdf.format(rDetail.getReturnDate())); // 还款日期
			rDetailVO.setRepaymentAll(rDetail.getRepaymentAll().setScale(2, BigDecimal.ROUND_HALF_UP)); // 一次性还款金额
			rDetailVO.setReturneterm(rDetail.getReturneterm().setScale(2, BigDecimal.ROUND_HALF_UP)); // 每期还款金额
			list.add(rDetailVO);
		}

		resultMap.put("code", Constants.SUCCESS_CODE);
		resultMap.put("message", "成功");
		resultMap.put("repaymentDetail", list);
		return resultMap;
	}

	/**
	 * 查看借款接口
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> queryLoan(LoanVo params) throws Exception {
		Map<String, Object> resultMap = null;
		VLoanInfo vLoanInfo = null;
		if(Strings.isEmpty(params.getLoanId())){
			vLoanInfo = vLoanInfoDao.getVLoanInfoByIdnum(params.getIdNum());
		}else{
			vLoanInfo = vLoanInfoDao.get(params.getLoanId());
		}
		 
		
		if (vLoanInfo != null) {
			ReturnQueryLoanVo loanVo = new ReturnQueryLoanVo();
			
			PersonInfo personInfo = personInfoDao.get(vLoanInfo.getBorrowerId());
			loanVo.setBorrower(personInfo.getName());
			loanVo.setSex(personInfo.getSex());
			loanVo.setIdnum(personInfo.getIdnum());
			loanVo.setLoanType(vLoanInfo.getLoanType());
			
			ZhuxueProductPlan productPlan = null;
			if (vLoanInfo.getPlanId() != null) {
				productPlan = zhuxueProductPlanDao.get(vLoanInfo.getPlanId());
			}
			ZhuxueOrganization organization = null;
			if(productPlan != null){
				organization = zhuxueOrganizationDao.get(productPlan.getOrganizationId());
			}
			loanVo.setOrganizationName(organization != null ? organization.getName() : "");
			loanVo.setPlanName(productPlan != null ? productPlan.getName() : "");
			
			loanVo.setRequestMoney(vLoanInfo.getRequestMoney());
			loanVo.setRequestTime(vLoanInfo.getRequestTime().intValue());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			loanVo.setRequestDate(sdf.format(vLoanInfo.getRequestDate()));
			loanVo.setMoney(vLoanInfo.getMoney());
			loanVo.setTime(vLoanInfo.getTime().intValue());
			loanVo.setMaxMonthlyPayment(BigDecimal.ZERO);//2.0系统该字段删除，为了保证与1.0接口一直，该处默认设置为0
			loanVo.setLoanFlowState(vLoanInfo.getLoanFlowState());
			loanVo.setRestoreEM(vLoanInfo.getRestoreem());
			loanVo.setPurpose(vLoanInfo.getPurpose());
			ComOrganization comOrganization = comOrganizationDao.get(vLoanInfo.getSalesDepartmentId());
			loanVo.setSalesDepartment(comOrganization.getName());
			LoanBank giveBackBank = loanBankDao.get(vLoanInfo.getGiveBackBankId());
			loanVo.setGiveBackBank(giveBackBank.getBankName() + ":" + giveBackBank.getAccount());
			LoanBank grantBank = loanBankDao.get(vLoanInfo.getGrantBankId());
			loanVo.setGrantBank(grantBank.getBankName() + ":" + grantBank.getAccount());
			ComEmployee salesman = comEmployeeDao.get(vLoanInfo.getSalesmanId());
			loanVo.setSalesman(salesman.getName());
			ComEmployee crm = comEmployeeDao.get(vLoanInfo.getCrmId());
			loanVo.setCrm(crm.getName());
			loanVo.setFundsSources(vLoanInfo.getFundsSources());
			loanVo.setRemark("");//2.0系统该字段删除，为了保证与1.0接口一直，该处默认设置为""
			loanVo.setResidualTime(vLoanInfo.getResidualTime());
			loanVo.setGrantMoney(vLoanInfo.getGrantMoney());
			resultMap = MessageUtil.returnHandleSuccessMessage();
			resultMap.put("loan", loanVo);
		} else {
			resultMap = MessageUtil.returnErrorMessage("失败：loanId输入有误，未查到对应记录");
		}
		
		return resultMap;
	}
	
	/**
	 * 内部匹配查询债权状态
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> neibupipei(LoanVo params) {
		
		List<String> appNoList = Arrays.asList(params.getAppNos().split(","));
		List<String> appNos = new ArrayList<String>();
		List<String> loanIds = new ArrayList<String>();
		for (Iterator<String> iter = appNoList.iterator(); iter.hasNext();) {
			String str = iter.next();
			if (str != null && !"".equals(str)) {
				if(str.length() == 20){
					appNos.add(str);
				} else {
					loanIds.add(str);
				}
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if ((null != appNos && appNos.size() > 0) || (null != loanIds && loanIds.size() > 0)) {
			List<VLoanInfo> loanInfos = new ArrayList<VLoanInfo>();
			
			/**根据APPNO查询**/
			if(null != appNos && appNos.size() > 0) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
	            paramMap.put("appNos", appNos);
	            List<VLoanInfo> appNoLoans = vLoanInfoDao.findListByMap(paramMap);
	            loanInfos.addAll(appNoLoans);
			}
			
			/**根据LoanId查询**/
			if(null != loanIds && loanIds.size() > 0) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
	            paramMap.put("loanIds", loanIds);
	            List<VLoanInfo> loanIdLoans = vLoanInfoDao.findListByMap(paramMap);
	            
	            for (VLoanInfo vLoanInfo : loanIdLoans) {
	            	if(vLoanInfo != null && (vLoanInfo.getAppNo() == null || "".equals(vLoanInfo.getAppNo()))){
	            		vLoanInfo.setAppNo(vLoanInfo.getId().toString());
	            	}
				}
	            
	            loanInfos.addAll(loanIdLoans);
			}
			
            List<LoanVo> loans = new ArrayList<LoanVo>();
            
            if (loanInfos != null && loanInfos.size() > 0) {
            	resultMap.put("code",Constants.SUCCESS_CODE);
                resultMap.put("message","查询成功");
                
                for (Iterator<VLoanInfo> iter = loanInfos.iterator(); iter.hasNext();) {
					VLoanInfo info = iter.next();
					
					if (LoanStateEnum.正常.getValue().equals(info.getLoanState()) || LoanStateEnum.结清.getValue().equals(info.getLoanState()) ||
						LoanStateEnum.预结清.getValue().equals(info.getLoanState()) || LoanStateEnum.逾期.getValue().equals(info.getLoanState())) {
						LoanVo loan = new LoanVo();
						loan.setAppNo(info.getAppNo());
						loan.setLoanState(info.getLoanState());
						loans.add(loan);
					}
				}
                
                resultMap.put("loans",loans);
            } else {
            	resultMap.put("code",Constants.SUCCESS_CODE);
                resultMap.put("message","查询成功");
                resultMap.put("loans",loans);
            }
		} else {
			resultMap.put("code",Constants.DATA_ERR_CODE);
            resultMap.put("message","失败:输入的参数有误");
		}
		
		return resultMap;
	}
	
	/**
	 * 根据APP_NO查询债权状态
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> queryLoanState(LoanVo params) {
		
		List<String> appNos = Arrays.asList(params.getAppNos().split(","));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if (null != appNos && appNos.size() > 0) {
            
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("appNos", appNos);
            List<VLoanInfo> loanInfos = vLoanInfoDao.findListByMap(paramMap);
            logger.info("条数:"+(loanInfos == null?0:loanInfos.size()));
            
            List<LoanVo> loans = new ArrayList<LoanVo>();
            if (loanInfos != null && loanInfos.size() > 0) {
            	resultMap.put("code",Constants.SUCCESS_CODE);
                resultMap.put("message","查询成功");
                for (int i=0;i<loanInfos.size();i++) {
					VLoanInfo info = loanInfos.get(i);
					LoanVo loan = new LoanVo();
					loan.setLoanId(info.getId());
					loan.setAppNo(info.getAppNo());
					loan.setLoanState(info.getLoanState());
					loan.setLoanFlowState(info.getLoanFlowState());
					if (FundsSourcesTypeEnum.外贸2.getValue().equals(info.getFundsSources())
						|| FundsSourcesTypeEnum.包商银行.getValue().equals(info.getFundsSources())
						|| FundsSourcesTypeEnum.外贸3.getValue().equals(info.getFundsSources())
						|| FundsSourcesTypeEnum.陆金所.getValue().equals(info.getFundsSources())) {
						// 正处于放款申请中
						if(financeGrantService.isWaiMao2FinanceGrantApply(info.getAppNo())){
							loan.setLoanState("申请中");
							loan.setLoanFlowState("申请中");
						}
					}
					// 渤海2如果处于未报盘、已报盘、扣款成功的状态，则不能退回（扣款失败可以退回）
					if(!thirdUnderLinePaymentService.isLoanBack(loan.getLoanId())){
						loan.setLoanState("放款中");
						loan.setLoanFlowState("放款中");
					}
					loans.add(loan);
				}
                resultMap.put("loans",loans);
            } else {
            	resultMap.put("code",Constants.SUCCESS_CODE);
                resultMap.put("message","查询成功");
                resultMap.put("loans",loans);
            }
		} else {
			resultMap.put("code",Constants.DATA_ERR_CODE);
            resultMap.put("message","失败:输入的参数有误");
		}
		
		return resultMap;
	}

	@Override
	public void getRepayPlanAndFeePlay4Lufax(LoanVo params, Lufax100007Vo lufax100007Vo) {
		LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByAppNo(params.getAppNo());

		LoanBase loanBase = loanBaseDao.findByLoanId(loanInitialInfo.getLoanId());
		LoanProduct loanProduct = loanProductDao.findByLoanId(loanInitialInfo.getLoanId());
		List<LoanRepaymentDetail> repaymentDetails = loanRepaymentDetailDao.findByLoanId(loanInitialInfo.getLoanId());	
		List<RepayPlanLufaxEntity> repayPlanList = new ArrayList<RepayPlanLufaxEntity>();
		for(LoanRepaymentDetail repaymentDetail : repaymentDetails){
			RepayPlanLufaxEntity repayPlan = new RepayPlanLufaxEntity();
			repayPlan.setLoanNo(loanBase.getContractNum());
			repayPlan.setTermNo(repaymentDetail.getCurrentTerm().toString());
			repayPlan.setPayDate(Dates.getDateTime(repaymentDetail.getReturnDate(), Dates.DATAFORMAT_YYYYMMDD));
			repayPlan.setLoanRate(loanProduct.getRateey().multiply(new BigDecimal("100")).toString());
			repayPlan.setPayprincipalamt(repaymentDetail.getReturneterm().subtract(repaymentDetail.getCurrentAccrual()).toString());
			repayPlan.setPayinteamt(repaymentDetail.getCurrentAccrual().toString());
			repayPlan.setTermAmt(repaymentDetail.getReturneterm().toString());
			repayPlanList.add(repayPlan);
		}
		String loanNo ="SR"+ Dates.getDateTime(Dates.DATAFORMAT_YYYYMMDDHHMMSS)+loanBase.getId();
		lufax100007Vo.setLoanNo(loanNo);
		lufax100007Vo.setLoanAmt(loanInitialInfo.getMoney().toString());
		lufax100007Vo.setLoanTerm(loanProduct.getTime().toString());
		lufax100007Vo.setRepaymentType(BaseParamVo.REPAYMENT_TYPE_LUFAX);
		lufax100007Vo.setProductNo(params.getProductNo());
		lufax100007Vo.setDepNo(BaseParamVo.DEP_NO_LUFAX);
		lufax100007Vo.setProductType(params.getProductType());
		lufax100007Vo.setLoanRate(loanProduct.getRateey().multiply(new BigDecimal("100")).toString());
		lufax100007Vo.setRepayPlan(repayPlanList);
	}

	@Override
	public void getConfirmContractData4Lufax(LoanVo params, Lufax100003Vo lufax100003Vo) {
		LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByAppNo(params.getAppNo());
		if(loanInitialInfo == null){
			throw new PlatformException(ResponseEnum.FULL_MSG, "该appNo未找到债权信息");
		}
		LoanProduct loanProduct = loanProductDao.findByLoanId(loanInitialInfo.getLoanId());
		String paymentAccno = loanBankService.findNumByLoanId(loanInitialInfo.getLoanId());
		List<LoanRepaymentDetail> repaymentDetails = loanRepaymentDetailDao.findByLoanId(loanInitialInfo.getLoanId());
		LoanRepaymentDetail firstRepay = repaymentDetails.get(0);
		lufax100003Vo.setAps_apply_no(params.getApsApplyNo());
		lufax100003Vo.setApply_no(params.getApplyNo());
		ContractConfDataInfoLufaxEntity data_info = new ContractConfDataInfoLufaxEntity();
		data_info.setInterest_rate(loanProduct.getRateey().multiply(new BigDecimal(100)).toString());
		data_info.setPeriods_num(loanProduct.getTime().toString());
		data_info.setDefault_interest(loanProduct.getPenaltyRate().multiply(new BigDecimal(360)).multiply(new BigDecimal(100)).toString()); //罚息率
		lufax100003Vo.setData_info(data_info);
		ContractConfLoanInfoLufaxEntity loan_info = new ContractConfLoanInfoLufaxEntity();
		loan_info.setLOAN_NO(params.getApplyNo());
		loan_info.setLUFAX_LOAN_REQ_ID(params.getLufaxLoanReqId());
		loan_info.setPUTOUT_DATE(Dates.getDateTime(loanInitialInfo.getSignDate(),Dates.DATAFORMAT_YYYYMMDD));
//		loan_info.setPUTOUT_DATE(Dates.getDateTime(new Date(),Dates.DATAFORMAT_YYYYMMDD)); //期望放款日期？
		loan_info.setMATURITY_DATE(Dates.getDateTime(loanProduct.getEndrdate(),Dates.DATAFORMAT_YYYYMMDD));
		loan_info.setCURDEDUCT_DATE(Dates.getDateTime(firstRepay.getReturnDate(),Dates.DATAFORMAT_YYYYMMDD));
		loan_info.setDEFAULT_PAYDATE(loanProduct.getPromiseReturnDate().toString());
		loan_info.setNORMAL_PRINCIPAL(loanProduct.getPactMoney().toString()); //放款本金 
		loan_info.setNEXT_PAYDATE(Dates.getDateTime(firstRepay.getReturnDate(),Dates.DATAFORMAT_YYYYMMDD));
		loan_info.setRATE_NEXT_ADJUST_DATE(Dates.getDateTime(firstRepay.getReturnDate(),Dates.DATAFORMAT_YYYYMMDD));
		loan_info.setCur("01"); //01-人民币
		loan_info.setRate(loanProduct.getRateey().multiply(new BigDecimal(100)).toString());
		loan_info.setCapital(loanProduct.getPactMoney().toString()); //申请金额（合同金额）
		loan_info.setLoan_term(loanProduct.getTime().intValue());
		loan_info.setLoan_term_flag("020");//010-日 020-月 030-年
		loan_info.setLoan_state("4600");// 申请贷款状态 默认 4600
		loan_info.setGage_rate("0.00");
		loan_info.setGage_amount("0.00");
		//loan_info.setRefer_fee(loanProduct.getReferRate().toString());
		loan_info.setTotal_insurance_amount("0.00");
		//利息总额 为 每期应还*期数-合同金额
		BigDecimal totalInterestAmount = firstRepay.getReturneterm().multiply(new BigDecimal(loanProduct.getTime())).subtract(loanProduct.getPactMoney());
		loan_info.setTotal_interest_amount(totalInterestAmount.toString());
		loan_info.setTotal_repay_amount(firstRepay.getReturneterm().multiply(new BigDecimal(loanProduct.getTime())).toString());
		loan_info.setRepayment_method(BaseParamVo.REPAYMENT_TYPE_LUFAX);
		loan_info.setIsdirected("02");//02-借款人银行
		loan_info.setInsured_total_amount("0.00");
		List<ContractConfRepayPlanEntity> repayPlans = new ArrayList<ContractConfRepayPlanEntity>();
		loan_info.setRepayPlans(repayPlans);
		lufax100003Vo.setLoan_info(loan_info);
		for(LoanRepaymentDetail repayment:repaymentDetails){
			ContractConfRepayPlanEntity repayEntity = new ContractConfRepayPlanEntity();
			repayEntity.setPayment_accno(paymentAccno);
			repayEntity.setPflg("040"); //040-未付
			repayEntity.setRate(loanProduct.getRateey().multiply(new BigDecimal(100)).toString());
			repayEntity.setOrate(loanProduct.getPenaltyRate().multiply(new BigDecimal(360)).multiply(new BigDecimal(100)).toString());
			repayEntity.setTotal_amount(repayment.getDeficit().toString());
			repayEntity.setInsureamount("0.00");
			repayEntity.setTERM_NO(repayment.getCurrentTerm().intValue());
			repayEntity.setPAY_DATE(Dates.getDateTime(repayment.getReturnDate(),Dates.DATAFORMAT_YYYYMMDD));
			if(repayment.getCurrentTerm().intValue() == 1){
				repayEntity.setVALUE_DATE(Dates.getDateTime(loanInitialInfo.getSignDate(),Dates.DATAFORMAT_YYYYMMDD)); //第一期起息日为 放款日期？
			}else{
				repayEntity.setVALUE_DATE(Dates.getDateTime(Dates.addMonths(repayment.getReturnDate(),-1),Dates.DATAFORMAT_YYYYMMDD));				
			}
			repayEntity.setEXPIRY_DATE(Dates.getDateTime(Dates.addDay(repayment.getReturnDate(), -1),Dates.DATAFORMAT_YYYYMMDD));
			repayEntity.setLOAN_RATE(loanProduct.getRateey().multiply(new BigDecimal(100)).toString());
			repayEntity.setPAYPRINCIPALAMT(repayment.getReturneterm().subtract(repayment.getCurrentAccrual()).toString());
			repayEntity.setACTUAL_PAYPRINCIPALAMT("0.00");
			repayEntity.setPAYINTEAMT(repayment.getCurrentAccrual().toString());
			repayEntity.setACTUAL_PAYINTEAMT("0.00");
			repayEntity.setPAYFINEAMT("0.00");
			repayEntity.setACTUAL_PAYFINEAMT("0.00");
			repayEntity.setPAYCOMPOUND("0.00");
			repayEntity.setACTUAL_PAYCOMPOUND("0.00");
			//repayEntity.setFINE_CALCULATE_DATE(Dates.getDateTime(repayment.getPenaltyDate(),Dates.DATAFORMAT_YYYYMMDD));
			//repayEntity.setFINE_BASE(firstRepay.getDeficit().toString());//罚息本金
			//repayEntity.setCOMPOUND_CALCULATE_DATE("");
			//repayEntity.setCOMPOUND_BASE("");
			repayEntity.setPRINCIPAL_BALANCE(repayment.getPrincipalBalance().toString());
			repayEntity.setTERM_AMT(repayment.getReturneterm().toString());
			//repayEntity.setFINISH_DATE("");// 结清后才会赋值
			repayEntity.setSTATUS("01");// 01-正常
			repayPlans.add(repayEntity);
		}
		//费用明细
		List<ContractConfFeeDetailEntity> fee_detail = new ArrayList<ContractConfFeeDetailEntity>();
		ContractConfFeeDetailEntity evalFee = new ContractConfFeeDetailEntity();
		evalFee.setFee_name("服务费");
		evalFee.setFee_type("08");
		BigDecimal feeRate =  loanProduct.getPactMoney().subtract(loanInitialInfo.getMoney()).divide(loanInitialInfo.getMoney(),10,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100))
				.setScale(6,  BigDecimal.ROUND_HALF_UP);
		evalFee.setFee_rate(feeRate.toString());//费率
		evalFee.setFee_amount(BigDecimal.ZERO.toString());
		evalFee.setFee_total_amount(evalFee.getFee_amount());
		evalFee.setFee_time("1");//1：放款时收
		evalFee.setFee_frequency("1");//1:一次性收费  2：期收
		fee_detail.add(evalFee);
		loan_info.setFee_detail(fee_detail);
	}
	
	
	@SuppressWarnings("deprecation")
	public void createRepaymentDetailAfterGrantLufax(Long loanId,Date date){
		VLoanInfo vLoanInfo = vLoanInfoDao.get(loanId);
		ICalculator calculator = CalculatorFactoryImpl.createCalculator(vLoanInfo);
		LoanBase loanBase = loanBaseDao.get(loanId);
		LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByLoanId(loanId);
		LoanProduct loanProduct = loanProductDao.findByLoanId(loanId);
		Date d = getLoanProductStartrdate(date);
		if (d.getDate() >= 1 && d.getDate() <= 15) {
			loanProduct.setPromiseReturnDate(1L);
		} else {
			loanProduct.setPromiseReturnDate(16L);
		}
		loanProduct.setStartrdate(d);
		d = (Date)d.clone();
		d.setMonth(d.getMonth() + Integer.valueOf(loanProduct.getTime().toString()) - 1);
		loanProduct.setEndrdate(d);
		//loanBase.setEndOfMonthOpened(endOfMonthOpened);
		
		loanRepaymentDetailDao.deleteByLoanId(loanId);
		calculator.createLoanRepaymentDetail(loanBase, loanInitialInfo, loanProduct);
		loanProductDao.update(loanProduct);
	}
	
	/**
	 * 得到首期还款日
	 * @return
	 */
	private Date getLoanProductStartrdate(Date date){
	    Calendar  tradeDate = Calendar.getInstance();
	    tradeDate.setTime(date);
	    int promiseReturnDate = tradeDate.get(Calendar.DAY_OF_MONTH) < 16 ? 1 : 16;
	    tradeDate.set(Calendar.MONTH, tradeDate.get(Calendar.MONTH) + 1);
	    tradeDate.set(Calendar.DAY_OF_MONTH, promiseReturnDate);
	    Date startRepayDate = Dates.parse(Dates.getDateTime(tradeDate.getTime(), Dates.DEFAULT_DATE_FORMAT), Dates.DEFAULT_DATE_FORMAT);
	    return startRepayDate;
	  }
	
	/**
	 * 新增或更新包商银行业务流水号 接口
	 */
	public Map<String, Object> saveOrUpdateBsyhBusNo(String appNo,
			String busNumber) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//loanNo查出loanBase
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("appNo", appNo);
		List<LoanBase> loanBaseList = loanBaseDao.findListByMap(paramMap);
		if(null==loanBaseList || loanBaseList.size()!=1){//没有或者对应多个
			resultMap = MessageUtil.returnErrorMessage(Constants.PARAM_ERR_CODE,"appNo输入有误，未查到对应记录");
			return resultMap;
		}
		LoanBase loanBase = loanBaseList.get(0);
		
		LoanBsbMapping loanBsbMapping = loanBsbMappingDao.getByLoanId(loanBase.getId());
		if(null==loanBsbMapping){//插入流水号
			loanBsbMapping=new LoanBsbMapping();
			loanBsbMapping.setBusNumber(busNumber);
			loanBsbMapping.setLoanId(loanBase.getId());
			loanBsbMapping.setId(sequencesService.getSequences(SequencesEnum.LOAN_BSB_MAPPING));
			loanBsbMappingDao.insert(loanBsbMapping);
			resultMap.put("code", Constants.SUCCESS_CODE);
			resultMap.put("message","新增成功");
		}else{//更新流水号
			loanBsbMapping.setBusNumber(busNumber);
			loanBsbMappingDao.update(loanBsbMapping);
			resultMap.put("code", Constants.SUCCESS_CODE);
			resultMap.put("message","更新成功");
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> createLoanContractTrial(LoanContractTrialRepVo params) throws Exception {
		Map<String,Object> json = new HashMap<>();
		String loanType = params.getLoanType();
		String fundsSources = FundsSourcesTypeEnumUtil.getName(params.getFundsSources());
		LoanBase loanBase = new LoanBase();
		loanBase.setFundsSources(fundsSources);
		LoanInitialInfo loanInitialInfo = new LoanInitialInfo();
		loanInitialInfo.setAuditDate(Dates.getCurrDate());
		loanInitialInfo.setSignDate(Dates.getCurrDate());
		loanInitialInfo.setMoney(params.getMoney());
		loanInitialInfo.setLoanType(loanType);
		LoanProduct loanProduct = new LoanProduct();
		loanProduct.setTime(params.getTime());

		ProdCreditProductTerm prodCreditProductTerm = prodCreditProductTermServiceImpl.findBymap(params.getTime(),loanType,fundsSources);
		if (null == prodCreditProductTerm) {
			throw new Exception("没有查询到产品信息");
		}
		//是否存在优惠客户--优惠便重新设置汇率
		this.resetIsRatePreferLoan(prodCreditProductTerm,loanInitialInfo,params.getIsRatePreferLoan());
		String endOfMonthOpened = sysParamDefineDao.getSysParamValue("codeHelper", "end_of_month_opened", false);
		Date d = getStartReturnDate(loanInitialInfo, endOfMonthOpened,fundsSources);

		/**设置开始还款日**/
		if (d.getDate() >= 1 && d.getDate() <= 15) {
			loanProduct.setPromiseReturnDate(1L);
		} else {
			loanProduct.setPromiseReturnDate(16L);
		}
		loanProduct.setStartrdate(d);

		d = (Date)d.clone();
		d.setMonth(d.getMonth() + Integer.valueOf(loanProduct.getTime().toString()) - 1);
		loanProduct.setEndrdate(d);

		loanBase.setEndOfMonthOpened(endOfMonthOpened);
		FundsSourcesTypeEnum fundsSourcesTypeEnum = Assert.validEnum(FundsSourcesTypeEnum.class, fundsSources, "");

		CalculatorBase alculatorBase = CalculatorFactoryImpl.createCalculatorBase(fundsSourcesTypeEnum);
		loanProduct.setCalculatorType(alculatorBase.getCalculatorVersion().getValue());
		ICalculator calculator = CalculatorFactoryImpl.createCalculator(fundsSourcesTypeEnum, alculatorBase.getCalculatorVersion());
		calculator.updateRate(loanBase, loanInitialInfo, loanProduct, prodCreditProductTerm);
		List<LoanRepaymentDetail>  loanRepaymentDetails = calculator.createLoanTrial(loanBase, loanInitialInfo, loanProduct);
		ContractTrialVo contractTrialVo = generateContractTrialVo(loanProduct);
		List<ReturnLoanContractTrialVo> returnLoanContractTrialVos = this.generateReturnLoanContractTrialVoList(loanRepaymentDetails);
		json.put("code", Constants.SUCCESS_CODE);
		json.put("message", "成功");
		json.put("contract", contractTrialVo);
		json.put("repaymentDetail",returnLoanContractTrialVos);
		return json;
	}

	private void resetIsRatePreferLoan(ProdCreditProductTerm prodCreditProductTerm,LoanInitialInfo loanInitialInfo,String isRatePreferLoan) throws Exception{
		//是优惠综合费率客户
		if(Strings.isNotEmpty(isRatePreferLoan) && "Y".equals(isRatePreferLoan)){
			if(!Strings.isEmpty(prodCreditProductTerm.getReloanRate())){
				prodCreditProductTerm.setRate(prodCreditProductTerm.getReloanRate());
				prodCreditProductTerm.setAccrualem(prodCreditProductTerm.getReloanAccrualem());
				loanInitialInfo.setIsRatePreferLoan(isRatePreferLoan);
			}else{
				throw new Exception("不存在优惠费率！");
			}
		}
	}

	/**
	 * 设置 ReturnLoanContractTrialVo
	 * @param loanRepaymentDetails
	 * @return
	 */
	private List<ReturnLoanContractTrialVo> generateReturnLoanContractTrialVoList(List<LoanRepaymentDetail>  loanRepaymentDetails){
		List<ReturnLoanContractTrialVo> returnLoanContractVos = new ArrayList<>();
		for(LoanRepaymentDetail repaymentDetail : loanRepaymentDetails){
			ReturnLoanContractTrialVo contractTrialVo = new ReturnLoanContractTrialVo();
			contractTrialVo.setCurrentTerm(repaymentDetail.getCurrentTerm());
			contractTrialVo.setCurrentAccrual(Strings.isEmpty(repaymentDetail.getCurrentAccrual()) ? new BigDecimal(0.00) : repaymentDetail.getCurrentAccrual().setScale(2,BigDecimal.ROUND_HALF_UP));
			contractTrialVo.setGiveBackRate(Strings.isEmpty(repaymentDetail.getGiveBackRate()) ? new BigDecimal(0.00) : repaymentDetail.getGiveBackRate().setScale(2,BigDecimal.ROUND_HALF_UP));
			contractTrialVo.setPrincipalBalance(Strings.isEmpty(repaymentDetail.getPrincipalBalance()) ? new BigDecimal(0.00) : repaymentDetail.getPrincipalBalance().setScale(2,BigDecimal.ROUND_HALF_UP));
			contractTrialVo.setRepaymentAll(Strings.isEmpty(repaymentDetail.getRepaymentAll()) ? new BigDecimal(0.00) : repaymentDetail.getRepaymentAll().setScale(2,BigDecimal.ROUND_HALF_UP));
			contractTrialVo.setReturnDate(Dates.format(repaymentDetail.getReturnDate(),Dates.DEFAULT_DATE_FORMAT));
			contractTrialVo.setPenaltyDate(Dates.format(repaymentDetail.getPenaltyDate(),Dates.DEFAULT_DATE_FORMAT));
			contractTrialVo.setReturneterm(Strings.isEmpty(repaymentDetail.getReturneterm()) ? new BigDecimal(0.00) : repaymentDetail.getReturneterm().setScale(2,BigDecimal.ROUND_HALF_UP));
			contractTrialVo.setPenalty(Strings.isEmpty(repaymentDetail.getPenalty()) ? new BigDecimal(0.00) : repaymentDetail.getPenalty().setScale(2,BigDecimal.ROUND_HALF_UP));
			returnLoanContractVos.add(contractTrialVo);
		}
		return returnLoanContractVos;
	}

	private ContractTrialVo generateContractTrialVo(LoanProduct loanProduct){
		ContractTrialVo contractTrialVo = new ContractTrialVo();
		contractTrialVo.setPactMoney(Strings.isEmpty(loanProduct.getPactMoney()) ? new BigDecimal(0.00) : loanProduct.getPactMoney().setScale(2,BigDecimal.ROUND_HALF_UP));
		contractTrialVo.setTime(loanProduct.getTime());
		contractTrialVo.setPromiseReturnDate(loanProduct.getPromiseReturnDate());
		contractTrialVo.setStartrdate(Dates.format(loanProduct.getStartrdate(),Dates.DEFAULT_DATE_FORMAT));
		contractTrialVo.setEndrdate(Dates.format(loanProduct.getEndrdate(),Dates.DEFAULT_DATE_FORMAT));
		contractTrialVo.setReferRate(Strings.isEmpty(loanProduct.getReferRate()) ? new BigDecimal(0.00) : loanProduct.getReferRate().setScale(2,BigDecimal.ROUND_HALF_UP));
		contractTrialVo.setEvalRate(Strings.isEmpty(loanProduct.getEvalRate()) ? new BigDecimal(0.00) : loanProduct.getEvalRate().setScale(2,BigDecimal.ROUND_HALF_UP));
		contractTrialVo.setManageRate(Strings.isEmpty(loanProduct.getManageRate()) ? new BigDecimal(0.00) : loanProduct.getManageRate().setScale(2,BigDecimal.ROUND_HALF_UP));
		contractTrialVo.setRisk(Strings.isEmpty(loanProduct.getRisk()) ? new BigDecimal(0.00) : loanProduct.getRisk().setScale(2,BigDecimal.ROUND_HALF_UP));
		contractTrialVo.setManageRateForPartyC(Strings.isEmpty(loanProduct.getManageRateForPartyC()) ? new BigDecimal(0.00) : loanProduct.getManageRateForPartyC().setScale(2,BigDecimal.ROUND_HALF_UP));
		contractTrialVo.setRateSum(Strings.isEmpty(loanProduct.getRateSum()) ? new BigDecimal(0.00) : loanProduct.getRateSum().setScale(2,BigDecimal.ROUND_HALF_UP));
		contractTrialVo.setRateem(Strings.isEmpty(loanProduct.getRateem()) ? new BigDecimal(0.00) : loanProduct.getRateem().setScale(2,BigDecimal.ROUND_HALF_UP));
		contractTrialVo.setRateey(Strings.isEmpty(loanProduct.getRateey()) ? new BigDecimal(0.00) : loanProduct.getRateey().setScale(2,BigDecimal.ROUND_HALF_UP));
		return contractTrialVo;
	}

	@Override
	public Map<String, Object> getResidualPactMoney(ResidualPactMoneyRepVo params) {
		Map<String,Object> json = new HashMap<>();
		PersonInfo personInfo = personInfoService.findByIdNumAndName(params.getName(),params.getIdNum());
		if (personInfo == null){
			json.put("code", Constants.SUCCESS_CODE);
			json.put("message", "客户在核心系统不存在！");
			json.put("residualPactMoney",new BigDecimal(0.00));
			json.put("isExist","false");
			return json;
		}
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("borrowerId",personInfo.getId());
		paramMap.put("loanStates",new String[]{LoanStateEnum.正常.getValue(),LoanStateEnum.逾期.getValue(),LoanStateEnum.预结清.getValue()});
		List<VLoanInfo> vLoanInfoList = vLoanInfoDao.findListByMap(paramMap);
		BigDecimal residualPactMoney = new BigDecimal(0.00);
		for(VLoanInfo vLoanInfo : vLoanInfoList){
			residualPactMoney = residualPactMoney.add(vLoanInfo.getResidualPactMoney());
		}
		json.put("code", Constants.SUCCESS_CODE);
		json.put("message", "成功");
		json.put("residualPactMoney",residualPactMoney);
		json.put("isExist","true");
		return json;
	}
	
	@Override
	public Map<String, Object> findLoanDetails(String appNo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//loanNo查出loanBase
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("appNo", appNo);
		List<LoanBase> loanBaseList = loanBaseDao.findListByMap(paramMap);
		if(null==loanBaseList || loanBaseList.size()!=1){//没有或者对应多个
			resultMap = MessageUtil.returnErrorMessage(Constants.PARAM_ERR_CODE,"appNo输入有误，未查到对应记录");
			return resultMap;
		}
		LoanBase loanBase = loanBaseList.get(0);
		LoanProduct loanProduct = loanProductDao.findByLoanId(loanBase.getId());
		
		resultMap.put("loanNo", appNo);
		resultMap.put("pactMoney", loanProduct.getPactMoney());
		resultMap.put("grantMoney", loanProduct.getGrantMoney());
		resultMap.put("time", loanProduct.getTime());
		resultMap.put("rateey", loanProduct.getRateey());
		resultMap.put("firstRepayDate",Dates.getDateTime(loanProduct.getStartrdate(), "yyyy-MM-dd"));
		resultMap.put("referRate", loanProduct.getReferRate());
		resultMap.put("evalRate", loanProduct.getEvalRate());
		resultMap.put("manageRate", loanProduct.getManageRate());
		resultMap.put("manageRateForPartyC", loanProduct.getManageRateForPartyC());
		
		List<RepayDetail> plans= new ArrayList<RepayDetail>();
		List<LoanRepaymentDetail> list = loanRepaymentDetailDao.findByLoanId(loanBase.getId());
		for (LoanRepaymentDetail loanRepaymentDetail : list) {
			RepayDetail repayDetail=new RepayDetail();
			repayDetail.setCurrentTerm(loanRepaymentDetail.getCurrentTerm());
			repayDetail.setCurrentAccrual(loanRepaymentDetail.getCurrentAccrual());
			repayDetail.setCurrCorpus(loanRepaymentDetail.getReturneterm().subtract(loanRepaymentDetail.getCurrentAccrual()));
			repayDetail.setReturneterm(loanRepaymentDetail.getReturneterm());
			repayDetail.setReturnDate(Dates.getDateTime(loanRepaymentDetail.getReturnDate(), "yyyy-MM-dd"));
			repayDetail.setRepaymentAll(loanRepaymentDetail.getRepaymentAll());
			repayDetail.setPrincipalBalance(loanRepaymentDetail.getPrincipalBalance());
			repayDetail.setPenalty(loanRepaymentDetail.getPenalty());
			repayDetail.setGiveBackRate(loanRepaymentDetail.getGiveBackRate());
			plans.add(repayDetail);
		}
		resultMap.put("plans", plans);
		resultMap.put("code", Constants.SUCCESS_CODE);
		resultMap.put("message","查询成功");
		
		return resultMap;
	}
	
    @Override
    public Map<String, Object> queryLoanStateNew(LoanVo params) {
        List<String> appNos = Arrays.asList(params.getAppNos().split(","));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (CollectionUtils.isEmpty(appNos)) {
            resultMap.put("code", Constants.DATA_ERR_CODE);
            resultMap.put("message", "失败:输入的参数有误");
            return resultMap;
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appNos", appNos);
        List<VLoanInfo> loanInfoList = vLoanInfoDao.findListByMap(paramMap);
        List<LoanVo> loanList = new ArrayList<LoanVo>();
        if (CollectionUtils.isEmpty(loanInfoList)) {
            resultMap.put("code", Constants.SUCCESS_CODE);
            resultMap.put("message", "查询成功，核心系统没有对应的数据。");
            resultMap.put("loans", loanList);
            return resultMap;
        }
        
        String loanState = null;
        String loanBelong = null;
        for (VLoanInfo vLoanInfo : loanInfoList) {
            // 债权状态
            loanState = vLoanInfo.getLoanState();
            // 债权去向
            loanBelong = vLoanInfo.getLoanBelong();
            LoanVo loan = new LoanVo();
            loan.setAppNo(vLoanInfo.getAppNo());
            // 00：未放款 01：申请中 02：放款成功 03：放款失败
            if (LoanStateEnum.正常.getValue().equals(loanState)
                    || LoanStateEnum.逾期.getValue().equals(loanState)
                    || LoanStateEnum.结清.getValue().equals(loanState)
                    || LoanStateEnum.预结清.getValue().equals(loanState)) {
                // 债权状态为正常 逾期 结清 预结清的 02=放款成功
                loan.setLoanState(FinanceGrantEnum.放款成功.getCode());
            } else if (FundsSourcesTypeEnum.包商银行.getValue().equals(loanBelong) 
                    ||FundsSourcesTypeEnum.渤海2.getValue().equals(loanBelong) 
                    ||FundsSourcesTypeEnum.华瑞渤海.getValue().equals(loanBelong)
                    ||FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong)
                    ||FundsSourcesTypeEnum.外贸2.getValue().equals(loanBelong)
                    ||FundsSourcesTypeEnum.外贸3.getValue().equals(loanBelong)) {
                // 根据申请编号查询 放款申请表 得到最新的放款申请信息（可能存在多条债权）
                LoanBaseGrant LoanBaseGrant = loanBaseGrantService.findLoanBaseGrantByAppNo(vLoanInfo.getAppNo());
                if (LoanBaseGrant == null) {
                    // 放款申请表中没有该债权，则没有做过放款申请 00=未放款
                    loan.setLoanState(FinanceGrantEnum.未放款.getCode());
                } else if (FinanceGrantEnum.申请中.getCode().equals(LoanBaseGrant.getGrantState())) {
                    // 放款申请表中状态为 01=申请中
                    loan.setLoanState(FinanceGrantEnum.申请中.getCode());
                } else if (FinanceGrantEnum.放款失败.getCode().equals(LoanBaseGrant.getGrantState())) {
                    // 放款申请表中状态为 03=放款失败
                    loan.setLoanState(FinanceGrantEnum.放款失败.getCode());
                } else if (FinanceGrantEnum.放款成功.getCode().equals(LoanBaseGrant.getGrantState())) {
                    // 放款申请表中状态为 02=放款成功
                    loan.setLoanState(FinanceGrantEnum.放款成功.getCode());
                }
            } else {
                loan.setLoanState(FinanceGrantEnum.未放款.getCode());
            }
            loanList.add(loan);
        }
        resultMap.put("loans", loanList);
        resultMap.put("code", Constants.SUCCESS_CODE);
        resultMap.put("message", "查询成功");
        return resultMap;
    }
}
