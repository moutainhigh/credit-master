package com.zdmoney.credit.person.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.PersonEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.IdcardInfoExtractorUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.validator.IdcardValidator;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanTransferInfo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.LoanBaseServiceImpl;
import com.zdmoney.credit.loan.service.pub.ILoanBankService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.person.domain.PersonCarInfo;
import com.zdmoney.credit.person.domain.PersonContactInfo;
import com.zdmoney.credit.person.domain.PersonEntrepreneurInfo;
import com.zdmoney.credit.person.domain.PersonHouseInfo;
import com.zdmoney.credit.person.service.pub.IPersonCarInfoService;
import com.zdmoney.credit.person.service.pub.IPersonEntrepreneurInfoService;
import com.zdmoney.credit.person.service.pub.IPersonHouseInfoService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;

/**
 * 客户请求处理
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/person")
public class PersonInfoController extends BaseController {
	
	@Autowired @Qualifier("personInfoServiceImpl")
	IPersonInfoService personInfoServiceImpl;
	
	@Autowired @Qualifier("personHouseInfoServiceImpl")
	IPersonHouseInfoService personHouseInfoServiceImpl;
	
	@Autowired @Qualifier("personCarInfoServiceImpl")
	IPersonCarInfoService personCarInfoServiceImpl;
	
	@Autowired @Qualifier("personEntrepreneurInfoServiceImpl")
	IPersonEntrepreneurInfoService personEntrepreneurInfoServiceImpl;
	
	@Autowired @Qualifier("comOrganizationServiceImpl")
	IComOrganizationService comOrganizationServiceImpl;
	
	@Autowired @Qualifier("VLoanInfoServiceImpl")
	IVLoanInfoService vLoanInfoServiceImpl;
	
	@Autowired @Qualifier("loanBankServiceImpl")
	ILoanBankService loanBankServiceImpl;
	
	@Autowired @Qualifier("comEmployeeServiceImpl")
	IComEmployeeService comEmployeeServiceImpl;
	@Autowired 
	ILoanTransferInfoService loanTransferInfoServiceImpl;
	/**
	 * 跳转到客户查询主页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/jumpPage")
	public ModelAndView jumpPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/person/person");
		modelAndView.addObject("contactTypeEnum",PersonEnum.ContactType.values());
		modelAndView.addObject("contactRelationEnum",PersonEnum.ContactRelation.values());
		return modelAndView;
	}
	
	/**
	 * 跳转到客户查询主页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/viewPersonDetailPage/{id}")
	public ModelAndView viewPersonDetailPage(@PathVariable Long id,HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/person/personDetail");
		modelAndView.addObject("id", id);
		return modelAndView;
	}
	
	/**
	 * 查询客户资料数据
	 * @param personInfo 前端查询条件实例
	 * @param rows 每页总行
	 * @param page 查询的页数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="search")
	@ResponseBody
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String search(PersonInfo personInfo,int rows, int page,HttpServletRequest request,HttpServletResponse response) {
		this.createLog(request, SysActionLogTypeEnum.查询, "客户查询", "查询客户数据");
		/** 本人姓名 **/
		String name = Strings.parseString(personInfo.getName());
		/** 本人电话 **/
		String mPhone = Strings.parseString(personInfo.getMphone());
		/** 身份证号 **/
		String idNum = Strings.parseString(personInfo.getIdnum());
		/** 房产证号 **/
		String realEstateLicenseCode = Strings.parseString(personInfo.getRealEstateLicenseCode());
		/** 联系人姓名 **/
		String contactName = Strings.parseString(request.getParameter("contactName"));
		/** 联系人电话 **/
		String contactMPhone = Strings.parseString(request.getParameter("contactMPhone"));
		
		//定义分页实例
		User user = UserContext.getUser();
		ComOrganization comOrganization =new ComOrganization();
		comOrganization.setOrgCode(user.getOrgCode());
		personInfo.setComOrganization(comOrganization);
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("ASC");
		personInfo.setPager(pager);
		
		if (Strings.isEmpty(name) && Strings.isEmpty(mPhone) && Strings.isEmpty(idNum) && 
				Strings.isEmpty(realEstateLicenseCode) && Strings.isEmpty(contactName) && Strings.isEmpty(contactMPhone)) {
			/** 未输入查询条件，返回空结果集 **/
			
		} else {
			PersonContactInfo personContactInfo = personInfo.getPersonContactInfo();
			if (personContactInfo == null) {
				personContactInfo = new PersonContactInfo();
				personInfo.setPersonContactInfo(personContactInfo);
			}
			personContactInfo.setName(contactName);
			personContactInfo.setMphone(contactMPhone);
			
			//调用Service层查询数据
			pager = personInfoServiceImpl.findWithPg(personInfo);
		}
		
		//将数据对象转换成JSON字符串，返回给前台
		return toPGJSONWithCode(pager);
	}
	
	/**
	 * 查询客户资料数据
	 * @param id 客户编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="loadPersonData/{id}")
	public ModelAndView loadPersonData(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) {
		String isEdit = Strings.parseString(request.getParameter("isEdit"));
		/** 查询客户详细信息 **/
		PersonInfo personInfo = personInfoServiceImpl.findById(id);
		
		if (personInfo != null) {
			/** 查询营业网点信息 **/
			Long orgId = personInfo.getSalesDepartmentId();
			ComOrganization comOrganization = comOrganizationServiceImpl.get(orgId);
			personInfo.setComOrganization(comOrganization);
		}
		
		/** 查询客户车辆信息 **/
		PersonCarInfo personCarInfo = new PersonCarInfo();
		personCarInfo.setPersonId(id);
		List<PersonCarInfo> cars = personCarInfoServiceImpl.findListByVo(personCarInfo);
		
		/** 查询客户房屋信息 **/
		PersonHouseInfo personHouseInfo = new PersonHouseInfo();
		personHouseInfo.setPersonId(id);
		List<PersonHouseInfo> houses = personHouseInfoServiceImpl.findListByVo(personHouseInfo);
		
		/** 查询客户企业经营信息 **/
		PersonEntrepreneurInfo personEntrepreneurInfo = new PersonEntrepreneurInfo();
		personEntrepreneurInfo.setPersonId(id);
		List<PersonEntrepreneurInfo> entrepreneurs = personEntrepreneurInfoServiceImpl.findListByVo(personEntrepreneurInfo);
		
		ModelAndView modelAndView = null;
		if ("1".equalsIgnoreCase(isEdit)) {
			/** 跳转到编辑页面 **/
			modelAndView = new ModelAndView("/person/editPersonDetail");
		} else {
			//将所属行业设置成中文
			personInfo.setIndustryType(PersonEnum.IndustryType.get(personInfo.getIndustryType()).getValue());
			/** 跳转到查看页面 **/
			modelAndView = new ModelAndView("/person/personDetail_info");
		}
		modelAndView.addObject("personInfo", personInfo);
		modelAndView.addObject("cars", cars);
		modelAndView.addObject("houses", houses);
		modelAndView.addObject("entrepreneurs", entrepreneurs);
		
		return modelAndView;
	}
	
	/**
	 * 查询客户借款信息
	 * @param personInfo 前端查询条件实例
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="loadPersonLoanData/{id}")
	public ModelAndView loadPersonLoanData(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/person/personDetail_loan");
		VLoanInfo vLoanInfo = new VLoanInfo();
		vLoanInfo.setBorrowerId(id);
		List<VLoanInfo> loans = vLoanInfoServiceImpl.findListByVO(vLoanInfo);
		for (int i=0;i<loans.size();i++) {
			vLoanInfo = loans.get(i);
			
			/** 还款银行 **/
			LoanBank giveloanBank = loanBankServiceImpl.findById(vLoanInfo.getGiveBackBankId());
			/** 放款银行 **/
			LoanBank grantloanBank = loanBankServiceImpl.findById(vLoanInfo.getGrantBankId());
			/** 营业网点 **/
			ComOrganization comOrganization = comOrganizationServiceImpl.get(vLoanInfo.getSalesDepartmentId());
			/** 客户经理 **/
			ComEmployee salesMan = comEmployeeServiceImpl.get(vLoanInfo.getSalesmanId());
			/** 客服 **/
			ComEmployee crmMan = comEmployeeServiceImpl.get(vLoanInfo.getCrmId());
			/** 借款人 **/
			PersonInfo personInfo = personInfoServiceImpl.findById(vLoanInfo.getBorrowerId());
			
			vLoanInfo.setGiveloanBank(giveloanBank);
			vLoanInfo.setGrantLoanBank(grantloanBank);
			vLoanInfo.setComOrganization(comOrganization);
			vLoanInfo.setSalesMan(salesMan);
			vLoanInfo.setCrmMan(crmMan);
			vLoanInfo.setPersonInfo(personInfo);
		}
		modelAndView.addObject("loans", loans);
		return modelAndView;
	}
	
	/**
	 * 查询客户资料数据
	 * @param personInfo 前端查询条件实例
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(PersonInfo personInfo,HttpServletRequest request,HttpServletResponse response) {
		this.createLog(request, SysActionLogTypeEnum.更新, "客户查询", "更新客户数据");
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		Long id = personInfo.getId();
		try {
			//检查该借款人是否有已转让过的债权
			boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(id,null);
			if(!flag){
				throw new PlatformException(ResponseEnum.FULL_MSG,Strings.errorMsg);
			}
			/** 验证身份证号合法性 **/
			IdcardValidator iv = new IdcardValidator();
			String idNum = personInfo.getIdnum();
			if (iv.isValidatedAllIdcard(idNum)) {
				IdcardInfoExtractorUtil ie = new IdcardInfoExtractorUtil(idNum);
				Date birthday = ie.getBirthday();
				String sex = ie.getGender();
				if (!Strings.isEmpty(birthday)) {
					personInfo.setBirthday(birthday);
				}
				if (!Strings.isEmpty(sex)) {
					personInfo.setSex(sex);
				}
			} else {
				throw new PlatformException(ResponseEnum.FULL_MSG,"输入的身份证不合法");
			}
			personInfoServiceImpl.saveOrUpdate(personInfo);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode()
					,ResponseEnum.SYS_SUCCESS.getDesc(),"");
		} catch(PlatformException ex) {
			logger.error(ex,ex);
			ResponseEnum responseEnum = ex.getResponseCode();
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(responseEnum.getCode()
					,ex.getMessage(),Strings.convertValue(id,String.class));
		} catch(Exception ex) {
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),Strings.convertValue(id,String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
}