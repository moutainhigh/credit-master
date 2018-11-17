package com.zdmoney.credit.chexiao.service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;














import com.zdmoney.credit.chexiao.dao.pub.IUndoManageDao;
import com.zdmoney.credit.chexiao.domain.LoanStateVo;
import com.zdmoney.credit.chexiao.domain.UndoManage;
import com.zdmoney.credit.chexiao.service.pub.IundoManageService;
import com.zdmoney.credit.chexiao.vo.UndoManageVo;
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
 * 撤销管理service接口定义
 * @author guocl
 */
@Service
public class UndoManageServiceImpl implements IundoManageService{

	@Autowired
	IUndoManageDao undoManageDao;
	
	/**
	 * 根据Map去查找
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager findWithPgByMap(Map<String, Object> param) {
		Pager pager = undoManageDao.findWithPgByMap(param);
		List<UndoManage> UndoManages = (List<UndoManage>)pager.getResultList();
		pager.setResultList(UndoManages);
		return pager;
	}
/**
 * 执行撤销操作
 */
	public String undo(String tradeno,int promise_return_date,Long tradedate) {
		String msg  = null;
		Calendar c1 = Calendar.getInstance();//可以对每个时间域单独修改
		Calendar c2 = Calendar.getInstance();//c1:流水生成时间，c2:当前系统时间
		c1.setTime(new Date(tradedate));
		if (c1.get(Calendar.MONTH)!=c2.get(Calendar.MONTH)||c1.get(Calendar.YEAR)!=c2.get(Calendar.YEAR)) {
			return 	msg = "不允许跨月撤销！！";
		}
		if (c1.get(Calendar.DATE)==promise_return_date) {
			return msg= "对应还款日的还款不能撤销！！";
		}
		if (promise_return_date == 16 && c1.get(Calendar.DATE) <=16&&c2.get(Calendar.DATE)>16) {
			return msg  = "16号端客户不能撤销1-16号的数据哦！！";
		}
		msg = 	undoManageDao.undo(tradeno);
		return msg;
	}
	
	/**
	 *更新还款状态 
	 * 
	 */
	
public void updateState(LoanStateVo loanStateVo) {
	
	undoManageDao.updateState(loanStateVo);
			
}

	
}
