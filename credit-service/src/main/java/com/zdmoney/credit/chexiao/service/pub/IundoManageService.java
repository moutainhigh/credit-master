package com.zdmoney.credit.chexiao.service.pub;

import java.util.Date;
import java.util.Map;

import com.zdmoney.credit.chexiao.domain.LoanStateVo;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.zhuxue.domain.ZhuxueOrganization;
/**
 * 撤销管理service接口定义
 * @author YM20033
 *
 */

 
public interface IundoManageService {

	
	
	/**
	 * 根据Map去查找
	 * @param param
	 * @return
	 */
	public Pager findWithPgByMap(Map<String, Object> param);

	public String undo(String tradeno,int promise_return_date,Long tradedate);

	public void updateState(LoanStateVo loanStateVo);
	
	
}
