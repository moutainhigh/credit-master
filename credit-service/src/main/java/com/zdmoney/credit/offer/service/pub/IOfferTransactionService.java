package com.zdmoney.credit.offer.service.pub;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.TppCallBackData;
import com.zdmoney.credit.common.vo.TppCallBackData20;
import com.zdmoney.credit.offer.domain.OfferInfo;
import com.zdmoney.credit.offer.domain.OfferTransaction;
import com.zdmoney.credit.system.domain.BaseMessage;
import com.zendaimoney.thirdpp.vo.RequestVo;
import com.zendaimoney.thirdpp.vo.enums.ThirdPartyType;

/**
 * 报盘交易表service
 * @author 00232949
 *
 */
public interface IOfferTransactionService {

	/**
	 * 得到状态是未回盘的信息
	 * @param id
	 * @return
	 */
	public List<OfferTransaction> getWeiHuiPanTraByloan(Long id);

	

	/**
	 * 处理tpp1.0回盘信息
	 * @param objects
	 * @return 
	 */
	public Boolean executeOfferBack(TppCallBackData objects);
	
	/**
	 * 处理tpp2.0回盘信息
	 * @param objects
	 * @return 
	 */
	public Boolean executeOfferBack20(TppCallBackData20 objects);
	
	/**
	 * 创建交易明细，并设置tpp发送对象
	 * @param requestVo
	 * @param offerInfo
	 * @param tppType
	 */
	public OfferTransaction setRequestDetailVo(RequestVo requestVo, OfferInfo offerInfo,
			ThirdPartyType tppType);



	/**
	 * 跟新状态为已回盘
	 * @param list
	 */
	public void updateOfferStateToYibaopan(List<OfferTransaction> list);


	/**
	 * 查询报盘信息
	 * @param params
	 * @return
	 */
	public Pager searchOfferInfoWithPgByMap(Map<String, Object> params);
	
	/**
	 * 查询代扣回盘信息
	 * @param params
	 * @return
	 */
	public Pager searchOfferTransactionInfoWithPgByMap(Map<String, Object> params);

	/**
	 * 查询报盘明细
	 * @param baseMessage
	 * @return
	 */
	public Pager findWithPg(OfferTransaction offerTransaction);



	/**
	 * 创建交易明细，并设置tpp2.0发送对象
	 * @param requestVo
	 * @param offerInfo
	 * @return
	 */
	public OfferTransaction setRequestDetailVo20(
			com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo requestVo,
			OfferInfo offerInfo);


	/**
	 * 更新错误信息至报盘流水表和报盘文件表,独立事物
	 * @param msg 
	 * @param offerInfo 
	 * @param offerTran
	 */
	public void updateErrorMsgNow(String msg, OfferInfo offerInfo, OfferTransaction offerTran);


	/**
	 *  跨日回盘 添加到消息中心
	 * @param offerDate
	 * @param loanId
	 */
	public void addBaseMessage (Date offerDate,Long loanId);
}
