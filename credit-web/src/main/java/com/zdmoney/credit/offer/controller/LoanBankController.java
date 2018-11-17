package com.zdmoney.credit.offer.controller;

import java.io.IOException;
import java.util.HashMap;
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

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.service.pub.ILoanBankService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.person.dao.PersonBankMapDaoImpl;
import com.zdmoney.credit.person.dao.pub.IPersonBankMapDao;
import com.zdmoney.credit.person.domain.PersonBankMap;
import com.zdmoney.credit.system.domain.ComOrganization;


/**
 * 银行卡Controller
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/offer/loanBank")
public class LoanBankController extends BaseController {
	
	@Autowired @Qualifier("loanBankServiceImpl")
	ILoanBankService loanBankServiceImpl;
	@Autowired
	IPersonBankMapDao personBankMapDaoImpl;
	@Autowired 
	ILoanTransferInfoService loanTransferInfoServiceImpl;
	/**
	 * 查询客户绑定银行卡信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/queryMyBankInfo/{id}")
	@ResponseBody
	public String getBankInfo(@PathVariable Long id,int rows, int page,HttpServletRequest request, HttpServletResponse response) throws IOException {
		Pager pg = new Pager();
		pg.setRows(rows);
		pg.setPage(page);
		Map map = new HashMap();
		map.put("personId", id);
		map.put("pager", pg);
		pg = loanBankServiceImpl.findWithPgByMap(map);
		return toPGJSONWithCode(pg);
	}
	
	/**
	 * 更新银行卡信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/updateLoanBank")
	@ResponseBody
	public String updateLoanBank(LoanBank loanBank,HttpServletRequest request, HttpServletResponse response) throws IOException {
		AttachmentResponseInfo<LoanBank> attachmentResponseInfo = null;
		try {
			Long objectId = loanBank.getObjectId();
			//检查该借款人是否有已转让过的债权
			boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(objectId,null);
			if(!flag){
				throw new PlatformException(ResponseEnum.FULL_MSG,Strings.errorMsg);
			}
			this.createLog(request, SysActionLogTypeEnum.更新, "客户查询", "更新开户银行");
			loanBankServiceImpl.saveOrUpdate(loanBank);
			attachmentResponseInfo = new AttachmentResponseInfo<LoanBank>(ResponseEnum.SYS_SUCCESS.getCode()
					,ResponseEnum.SYS_SUCCESS.getDesc(),"");
			attachmentResponseInfo.setAttachment(loanBank);
		} catch (PlatformException ex) {
			attachmentResponseInfo = ex.<LoanBank>toAttachmentResponseInfo();
		} catch(Exception ex) {
			/** 系统忙 **/
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<LoanBank>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),"");
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
	/**
	 * 更新银行卡信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/loadLoanBankInfo/{id}")
	@ResponseBody
	public String loadLoanBankInfo(@PathVariable Long id,HttpServletRequest request, HttpServletResponse response) throws IOException {
		AttachmentResponseInfo<LoanBank> attachmentResponseInfo = null;
		try {
			LoanBank loanBank = loanBankServiceImpl.findById(id);
			if (loanBank == null) {
				attachmentResponseInfo = new AttachmentResponseInfo<LoanBank>(ResponseEnum.VALIDATE_RESULT_ISNULL.getCode()
						,Strings.format(ResponseEnum.VALIDATE_RESULT_ISNULL.getDesc(), "编号:" + id),Strings.convertValue(id,String.class));
			} else {
				attachmentResponseInfo = new AttachmentResponseInfo<LoanBank>(ResponseEnum.SYS_SUCCESS.getCode()
						,ResponseEnum.SYS_SUCCESS.getDesc(),Strings.convertValue(id,String.class));
				attachmentResponseInfo.setAttachment(loanBank);
			}
		} catch(Exception ex) {
			/** 系统忙 **/
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<LoanBank>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),Strings.convertValue(id,String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
}
