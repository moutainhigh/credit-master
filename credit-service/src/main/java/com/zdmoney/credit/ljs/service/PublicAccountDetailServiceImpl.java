package com.zdmoney.credit.ljs.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.tpp.AccountTradeTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.ljs.dao.pub.IPublicAccountDetailDao;
import com.zdmoney.credit.ljs.dao.pub.IPublicVirtualAccountDao;
import com.zdmoney.credit.ljs.domain.PublicAccountDetail;
import com.zdmoney.credit.ljs.domain.PublicVirtualAccount;
import com.zdmoney.credit.ljs.service.pub.IPublicAccountDetailService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/***
 * 陆金所对公账户明细
 * @author YM10104
 *
 */
@Service
public class PublicAccountDetailServiceImpl  implements IPublicAccountDetailService{

	@Autowired
	IPublicAccountDetailDao publicAccountDetailDao;
	@Autowired
    private ISequencesService sequencesService;
	@Autowired
	IPublicVirtualAccountDao publicVirtualAccountDao;
	
	@Override
	public PublicAccountDetail save(PublicAccountDetail publicAccountDetail) {
		return publicAccountDetailDao.insert(publicAccountDetail);
	}

	@Override
	public Pager findWithPgByMap(Map<String, Object> paramMap) {
		return publicAccountDetailDao.findWithPgByMap(paramMap);
	}

	@Override
	public PublicAccountDetail getLastAccountDetail(String accountType) {
		return publicAccountDetailDao.getLastAccountDetail(accountType);
	}

	@Override
	public List<PublicAccountDetail> findByMap(Map<String, Object> paramMap) {
		return publicAccountDetailDao.findByMap(paramMap);
	}

	@Override
	public PublicAccountDetail insertAccDetail(String contractNum,
			String batchNo, String tradeType,BigDecimal amount,String memo,String state,String errorMsg) {
		PublicVirtualAccount publicVirtualAccount = publicVirtualAccountDao.findByAccountType("00001");
		PublicAccountDetail publicAccountDetail = new PublicAccountDetail();
		publicAccountDetail.setAccountId(publicVirtualAccount.getId());
		publicAccountDetail.setId(sequencesService.getSequences(SequencesEnum.PUBLIC_ACCOUNT_DETAIL));
		publicAccountDetail.setContractNum(contractNum);//借款合同编号
		publicAccountDetail.setBatchNo(batchNo);
		publicAccountDetail.setErrorMsg(errorMsg);
		publicAccountDetail.setReqMoney(amount);
		publicAccountDetail.setMemo(memo);//摘要
		publicAccountDetail.setTradeType(tradeType);//交易类型（0充值、1提现、2还款、3垫付、4回购）
		publicAccountDetail.setState(state);//状态（0:处理中,1:成功, 2:失败）
		publicAccountDetail.setCreateTime(Dates.getNow());

		
		if(state.equals("1")&&(tradeType.equals(AccountTradeTypeEnum.还款.getCode()) || tradeType.equals(AccountTradeTypeEnum.垫付.getCode()))
				|| tradeType.equals(AccountTradeTypeEnum.回购.getCode())){//还款并且成功 或 垫付并且成功
//			if(amount.compareTo(publicVirtualAccount.getTotalAmt())>0){//还款金额大于账户金额 则余额不足
//				throw new PlatformException(ResponseEnum.FULL_MSG,"虚拟账户余额不足！");
//			}
			publicAccountDetail.setClosingBalance(publicVirtualAccount.getTotalAmt().subtract(amount));
			publicAccountDetail.setPay(amount);
			publicAccountDetail.setInCome(new BigDecimal("0"));
			publicAccountDetail.setInitialBalance(publicVirtualAccount.getTotalAmt());
			publicAccountDetail.setTradeDate(Dates.getCurrDate());
			publicAccountDetail.setTradeTime(Dates.getDateTime("HH:mm:ss"));
			publicAccountDetailDao.insert(publicAccountDetail);
			//更新虚拟账户总账表 减少总金额
      	   	publicVirtualAccountDao.subAmtByAccountType("00001",amount);
		}else{
			publicAccountDetailDao.insert(publicAccountDetail);
		}
		Log.info("※※※※※※※※※※保存public_account_detail成功：" + JSON.toJSONString(publicAccountDetail));
		return publicAccountDetail;
	}	
	@Override
	public void updateAccDetailByBatchNo(String contractNum,String batchNo, String tradeType,BigDecimal amount,String memo,String state,String errorMsg) {	
		PublicVirtualAccount publicVirtualAccount = publicVirtualAccountDao.findByAccountType("00001");
		PublicAccountDetail publicAccountDetail = publicAccountDetailDao.findByBatchNo(batchNo);
		if(null==publicAccountDetail){
			throw new PlatformException(ResponseEnum.FULL_MSG,"不存在该笔充值记录！");
		}
		if(!"0".equals(publicAccountDetail.getState())){//该笔流水状态不是处理中 则不更新
			throw new PlatformException(ResponseEnum.FULL_MSG,"该笔充值已经处理，不允许再次处理！");
		}
		publicAccountDetail.setState(state);
		publicAccountDetail.setErrorMsg(errorMsg);
		publicAccountDetail.setUpdateTime(Dates.getNow());
		publicAccountDetail.setTradeDate(Dates.getCurrDate());
		publicAccountDetail.setTradeTime(Dates.getDateTime("HH:mm:ss"));
		if("1".equals(state)){//成功
			if(AccountTradeTypeEnum.充值.getCode().equals(tradeType)){//0充值
				publicAccountDetail.setInitialBalance(publicVirtualAccount.getTotalAmt());
				publicAccountDetail.setClosingBalance(publicVirtualAccount.getTotalAmt().add(amount));	
				publicAccountDetail.setInCome(amount);
				publicAccountDetail.setPay(new BigDecimal("0"));
				publicAccountDetailDao.updateAccDetailByBatchNo(publicAccountDetail);
				//更新虚拟账户总账表 增加总金额	
	      	   	publicVirtualAccountDao.addAmtByAccountType("00001",amount);
			}else{//其他都是支出
				publicAccountDetail.setInitialBalance(publicVirtualAccount.getTotalAmt());
				if(Strings.isEmpty(amount)){//陆金所返回金额是空，取请求金额
//					if(publicAccountDetail.getReqMoney().compareTo(publicVirtualAccount.getTotalAmt())>0){//余额不足
//						throw new PlatformException(ResponseEnum.FULL_MSG,"虚拟账户余额不足！");
//					}
					publicAccountDetail.setClosingBalance(publicVirtualAccount.getTotalAmt().subtract(publicAccountDetail.getReqMoney()));
					publicAccountDetail.setPay(publicAccountDetail.getReqMoney());	
					publicAccountDetail.setInCome(new BigDecimal("0"));
				}
//				else if(amount.compareTo(publicVirtualAccount.getTotalAmt())>0){//返回金额大于账户金额 则余额不足
//					throw new PlatformException(ResponseEnum.FULL_MSG,"虚拟账户余额不足！");
//				}
				else{
					publicAccountDetail.setClosingBalance(publicVirtualAccount.getTotalAmt().subtract(amount));
					publicAccountDetail.setPay(amount);	
					publicAccountDetail.setInCome(new BigDecimal("0"));
				}
				publicAccountDetailDao.updateAccDetailByBatchNo(publicAccountDetail);
				//更新虚拟账户总账表 减少总金额	
	      	   	publicVirtualAccountDao.subAmtByAccountType("00001",publicAccountDetail.getPay());
			}
			
		}else if("2".equals(state)){//失败	
			publicAccountDetail.setInCome(new BigDecimal("0"));//收入	
			publicAccountDetail.setPay(new BigDecimal("0"));//支出
			publicAccountDetail.setInitialBalance(publicVirtualAccount.getTotalAmt());
			publicAccountDetail.setClosingBalance(publicVirtualAccount.getTotalAmt());
			publicAccountDetailDao.updateAccDetailByBatchNo(publicAccountDetail);
		}
	}
}
