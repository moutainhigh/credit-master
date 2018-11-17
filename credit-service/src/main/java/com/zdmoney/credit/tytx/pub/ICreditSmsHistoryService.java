package com.zdmoney.credit.tytx.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.tytx.domain.CreditSmsHistory;
/**
 * 统一通信平台service接口
 * @author fuhongxing
 * @date 2016年7月27日
 * @version 1.0.0
 */
public interface ICreditSmsHistoryService {
	/**
	 * 新增短信记录
	 * @param creditdxSmsHistory
	 * @return
	 */
	public CreditSmsHistory save(CreditSmsHistory creditSmsHistory);
	/**
	 * 根据手机号码查询短信发送信息
	 * @param map
	 * @return
	 */
	public List<CreditSmsHistory> findByMobile(Map<String, Object> map);
}
