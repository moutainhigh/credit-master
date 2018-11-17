package com.zdmoney.credit.zhuxue.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.TradeTypeEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.ToolUtils;
import com.zdmoney.credit.loan.dao.pub.ILoanLedgerDao;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.offer.dao.pub.IOfferFlowDao;
import com.zdmoney.credit.zhuxue.dao.pub.IZhuxueOrganizationDao;
import com.zdmoney.credit.zhuxue.domain.ZhuxueOrganization;
import com.zdmoney.credit.zhuxue.service.pub.IZhuxueOrganizationService;
import com.zdmoney.credit.zhuxue.vo.ZhuxueOrganizationAccountCardVo;
import com.zdmoney.credit.zhuxue.vo.ZhuxueOrganizationVo;

/**
 * 助学贷组织机构service接口实现
 * @author 00234770
 * @date 2015年9月21日 下午3:07:09 
 *
 */
@Service
public class ZhuxueOrganizationServiceImpl implements IZhuxueOrganizationService {

	@Autowired
	IZhuxueOrganizationDao zhuxueOrganizationDao;
	@Autowired
	ILoanLedgerDao loanLedgerDao;
	@Autowired
	IOfferFlowDao offerFlowDao;
	
	@Override
	public ZhuxueOrganization findByPlanId(Long planId) {
		
		return zhuxueOrganizationDao.findByPlanId(planId);
	}
	
	/**
	 * 根据Map去查找
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager findWithPgByMap(Map<String, Object> param) {
		Pager pager = zhuxueOrganizationDao.findWithPgByMap(param);
		
		List<ZhuxueOrganization> zhuxueOrganizations = (List<ZhuxueOrganization>)pager.getResultList();
		List<ZhuxueOrganizationVo> ZhuxueOrganizationVos = new ArrayList<ZhuxueOrganizationVo>();
		for (ZhuxueOrganization org : zhuxueOrganizations) {
			ZhuxueOrganizationVo vo = new ZhuxueOrganizationVo();
			
			vo.setId(org.getId());
			vo.setAddress(org.getAddress());
			vo.setCode(org.getCode());
			vo.setDateSigned(org.getDateSigned());
			vo.setMarginRate(org.getMarginRate());
			vo.setMemo(org.getMemo());
			vo.setName(org.getName());
			vo.setOrgType(org.getOrgType());
			vo.setOwner(org.getOwner());
			vo.setOwnerIdnum(org.getOwnerIdnum());
			vo.setTel(org.getTel());
			vo.setPostCode(org.getPostCode());
			vo.setContact(org.getContact());
			vo.setContactTel(org.getContactTel());
			vo.setBankAccountType(org.getBankAccountType());
			
			LoanLedger loanLedger = loanLedgerDao.findByAccount(org.getId().toString());
			vo.setGuazhangjine(loanLedger != null ? loanLedger.getAmount() : BigDecimal.valueOf(0));
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("tradeCode", Const.TRADE_CODE_OPENACC_ASC);
			paramMap.put("acctTitle", Const.ACCOUNT_TITLE_OTHER_EXP);
			paramMap.put("appoacct", org.getId());
			vo.setTradeAmount(offerFlowDao.getTradeAmount(paramMap));
			
			ZhuxueOrganizationVos.add(vo);
		}
		pager.setResultList(ZhuxueOrganizationVos);
		
		return pager;
	}

	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	@Override
	public ZhuxueOrganization findById(Long id) {
		return zhuxueOrganizationDao.get(id);
	}

	/**
	 * 查找机构帐卡信息
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getZhuxueOrganizationAccountCard(Map<String, Object> paramMap) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("acctTitle1", Const.ACCOUNT_TITLE_AMOUNT);
		param.put("dorc1", "C");
		param.put("tradeType1", TradeTypeEnum.系统使用保证金.getValue());
		param.put("organizationId", paramMap.get("id"));
		param.put("tradeCode1", Const.TRADE_CODE_OPENACC_ASC);
		param.put("acctTitle2", Const.ACCOUNT_TITLE_OTHER_EXP);
		param.put("startDate", paramMap.get("startDate"));
		param.put("endDate", paramMap.get("endDate"));
		param.put("noDate", ToolUtils.getLastMonthDate(new Date()));
		
		param.put("tradeCode2", new String[]{Const.TRADE_CODE_ORGIN, Const.TRADE_CODE_ORGOUT});
		
		param.put("tradeCode3", new String[]{Const.TRADE_CODE_STDOUT, Const.TRADE_CODE_DRAWRISK_STUDENT});
		
		param.put("tradeCode4", new String[]{Const.TRADE_CODE_STDIN, Const.TRADE_CODE_NORMAL, Const.TRADE_CODE_ONEOFF});
		
		param.put("pager", paramMap.get("pager"));
		
		Pager pager = zhuxueOrganizationDao.findAccountCardPGByMap(param);
		
		List<String> inComes = new ArrayList<String>();
		inComes.add(Const.TRADE_CODE_OPENACC_ASC);
		inComes.add(Const.TRADE_CODE_ORGIN);
		inComes.add(Const.TRADE_CODE_STDIN);
		inComes.add(Const.TRADE_CODE_NORMAL);
		inComes.add(Const.TRADE_CODE_ONEOFF);
		
		List<String> payments = new ArrayList<String>();
		payments.add(Const.TRADE_CODE_MARGINREAY);
		payments.add(Const.TRADE_CODE_ORGOUT);
		payments.add(Const.TRADE_CODE_STDOUT);
		payments.add(Const.TRADE_CODE_DRAWRISK_STUDENT);
		
		
		Map<String, String> tradeCodeMap = new HashMap<String, String>();
		tradeCodeMap.put(Const.TRADE_CODE_NORMAL, "正常还款");
        tradeCodeMap.put(Const.TRADE_CODE_IN, "账户存款");
        tradeCodeMap.put(Const.TRADE_CODE_OUT, "账户取款");
        tradeCodeMap.put(Const.TRADE_CODE_STDIN, "学生向保证金账户存款");
        tradeCodeMap.put(Const.TRADE_CODE_STDOUT, "学生从保证金账户取款");
        tradeCodeMap.put(Const.TRADE_CODE_ORGIN, "保证金账户存款");
        tradeCodeMap.put(Const.TRADE_CODE_ORGOUT, "保证金账户取款");
        tradeCodeMap.put(Const.TRADE_CODE_SETTLE, "结清处理");
        tradeCodeMap.put(Const.TRADE_CODE_ONEOFF, "一次性（提前还款）");
        tradeCodeMap.put(Const.TRADE_CODE_OPENACC, "个贷开户");
        tradeCodeMap.put(Const.TRADE_CODE_OPENACC_ASC, "助学贷开户");
        tradeCodeMap.put(Const.TRADE_CODE_SPECIA, "减免特殊还款");
        tradeCodeMap.put(Const.TRADE_CODE_MARGINREAY, "保证金还款");
        tradeCodeMap.put(Const.TRADE_CODE_DRAWRISK_STUDENT, "提保证金");
		
		List<ZhuxueOrganizationAccountCardVo> accountCards = (List<ZhuxueOrganizationAccountCardVo>)pager.getResultList();
		for (ZhuxueOrganizationAccountCardVo accountCard : accountCards) {
			BigDecimal income = isExist(accountCard.getTradeCode(), inComes) ? accountCard.getTradeAmount() : BigDecimal.valueOf(0);
			accountCard.setIncome(income);
			
			BigDecimal payment = isExist(accountCard.getTradeCode(), payments) ? accountCard.getTradeAmount() : BigDecimal.valueOf(0);
			accountCard.setPayment(payment);
			
			accountCard.setTradeCodeStr(tradeCodeMap.containsKey(accountCard.getTradeCode()) ? tradeCodeMap.get(accountCard.getTradeCode()) : "");
		}
		
		pager.setResultList(accountCards);
		
		return pager;
	}
	
	/**
     * 判断某个值在List中是否存在
     * @param value
     * @param array
     * @return
     */
	private boolean isExist(String value, List<String> array) {
        boolean flag = false;
        for (String arr : array) {
            if (value.equals(arr)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
