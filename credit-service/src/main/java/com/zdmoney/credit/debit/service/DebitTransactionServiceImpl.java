package com.zdmoney.credit.debit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.constant.OfferTypeEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.tpp.ReturnCodeEnum;
import com.zdmoney.credit.common.exception.OfferBackException;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.debit.dao.pub.IDebitBaseInfoDao;
import com.zdmoney.credit.debit.dao.pub.IDebitTransactionDao;
import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.debit.service.pub.IDebitTransactionService;
import com.zdmoney.credit.framework.vo.wm3.entity.DeductMoneyListEntity;
import com.zdmoney.credit.framework.vo.wm3.entity.FalseRecordListEntity;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2311Vo;
import com.zdmoney.credit.framework.vo.wm3.output.WM3_2311OutputVo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.offer.service.pub.IOfferTransactionService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * @ClassName:     DebitTransactionServiceImpl.java
 * @Description:   
 * @author         柳云华
 * @version        V1.0  
 * @Since          JDK 1.7
 * @Date           2017年3月22日 下午5:47:26
 */
@Service
@Transactional
public class DebitTransactionServiceImpl implements IDebitTransactionService {
	
	private static final Logger logger = Logger.getLogger(DebitTransactionServiceImpl.class);
	
	@Autowired
	private ISequencesService sequencesService;
	
	@Autowired
	private IAfterLoanService afterLoanService;
	
	@Autowired
	private IDebitTransactionDao debitTransactionDao;
	
	@Autowired
	private IDebitBaseInfoDao debitBaseInfoDao;
	
	@Autowired
	private IOfferTransactionService offerTransactionServiceImpl;
	
	@Override
	public DebitTransaction buildDebitTransaction(DebitBaseInfo offer) {
		DebitTransaction transaction = new DebitTransaction();
		transaction.setId(sequencesService.getSequences(SequencesEnum.DEBIT_TRANSACTION));
		transaction.setDebitId(offer.getId());
		transaction.setSerialNo(transaction.getId().toString());
		transaction.setLoanId(offer.getLoanId());
		transaction.setPayAmount(offer.getOfferAmount());
		transaction.setReqTime(new Date());
		transaction.setState(OfferTransactionStateEnum.未发送.getValue());
		transaction.setCreateTime(new Date());
		transaction.setCreator("admin");
		debitTransactionDao.insert(transaction);
		return transaction;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateOfferStateToYibaopan(DebitTransaction transaction, String serialNo) {
		transaction.setSerialNo(serialNo);
		transaction.setState(OfferTransactionStateEnum.已发送.getValue());
		debitTransactionDao.updateByPrimaryKeySelective(transaction);
		
		DebitBaseInfo offer = new DebitBaseInfo();
		offer.setId(transaction.getDebitId());
		offer.setState(OfferStateEnum.已报盘.getValue());
		debitBaseInfoDao.updateByPrimaryKeySelective(offer);
		
		logger.info("债权去向为包商银行的债权：【"+transaction.getLoanId() + "】已报盘并发送成功！！！");
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateErrorMsgNow(String code, String msg, DebitBaseInfo offer, DebitTransaction transaction) {
		if(offer!=null){
			offer.setMemo(msg);
			debitBaseInfoDao.updateByPrimaryKeySelective(offer);
		}
		if(transaction!=null){
			transaction.setMemo(msg);
			transaction.setSerialNo(transaction.getId().toString());
			debitTransactionDao.updateByPrimaryKeySelective(transaction);
		}
		logger.info("债权去向为包商银行的债权：【"+transaction.getLoanId() + "】申请报盘失败！！！");
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void callbackNotify(String serialNo,String code,String msg,String applyType){
		//结清通知并不记录
		if("2".equals(applyType)){
			return;
		}
		DebitTransaction transaction = debitTransactionDao.findBySerialNo(serialNo);
		if(transaction==null){
			throw  new OfferBackException("找不到交易流水号:" + serialNo + ",所对应的报盘数据!");
		}
		
		if(OfferTransactionStateEnum.扣款成功.getValue().equals(transaction.getState()) ||
				OfferTransactionStateEnum.扣款失败.getValue().equals(transaction.getState())){
			throw  new OfferBackException("交易流水号:" + serialNo + "已经回盘完成,不再处理!");
		}
		//得到报盘信息
		DebitBaseInfo offer = debitBaseInfoDao.get(transaction.getDebitId());
		if(offer==null){
			throw new OfferBackException("交易流水号:" + serialNo + "未找到DebitBaseInfo，回盘处理失败!");
		}
		transaction.setResTime(new Date());
		transaction.setUpdateTime(transaction.getResTime());
		transaction.setUpdator("admin");
		/*transaction.setRtnCode(code);
		transaction.setRtnInfo(msg);*/
		transaction.setMemo(msg);
		BigDecimal successAmount = BigDecimal.ZERO;
		BigDecimal perReapyAmount = afterLoanService.getPerReapyAmount(new Date(), offer.getLoanId());
		if("0000".equals(code)){
			transaction.setPayAmount(perReapyAmount);
			successAmount = perReapyAmount;
			transaction.setActualAmount(successAmount);
			transaction.setState(OfferTransactionStateEnum.扣款成功.getValue());
			transaction.setRtnCode(ReturnCodeEnum.交易成功.getCode());
			offer.setState(OfferStateEnum.已回盘全额.getValue());
		} else{
			transaction.setPayAmount(perReapyAmount);
			transaction.setActualAmount(successAmount);
			transaction.setState(OfferTransactionStateEnum.扣款失败.getValue());
			transaction.setRtnCode(ReturnCodeEnum.交易失败.getCode());
			offer.setState(OfferStateEnum.已回盘非全额.getValue());
		}
		debitTransactionDao.updateByPrimaryKeySelective(transaction);
		
		offer.setUpdateTime(transaction.getResTime());
		offer.setUpdator("admin");
		offer.setMemo(msg);
		/** 更新报盘金额 报盘金额=报盘金额-本次成功扣款金额 **/
		offer.setOfferAmount(offer.getOfferAmount().subtract(successAmount));
		/** 更新成功扣款总金额 **/
		offer.setActualAmount(offer.getActualAmount().add(successAmount));
		debitBaseInfoDao.updateByPrimaryKeySelective(offer);
		
		//跨日回盘 添加到消息中心
        offerTransactionServiceImpl.addBaseMessage(offer.getOfferDate(), offer.getLoanId());
	}
	
    /**
     * 检查是否有未回盘的情况 查询该loan报盘明细DebitTransaction的所有记录，查看是否有未回盘的，如果有，不能再次生成
     * @param loan
     * @return
     */
    @Override
    public boolean checkHasOffer(VLoanInfo loan) {
        // 检查是否有未回盘的情况，如果未回盘，返回true
        List<DebitTransaction> debitTransactionList = getWeiHuiPanTraByloan(loan.getId());
        if(CollectionUtils.isNotEmpty(debitTransactionList)){
            DebitTransaction debitTransaction = debitTransactionList.get(0);
            logger.info("流水号：" + debitTransaction.getSerialNo() + "还未回盘，loanid=" + debitTransaction.getLoanId() + " ,报盘时间："
                    + Dates.getDateTime(debitTransaction.getReqTime(), null));
            return true;
        }
        // 查看今天是否有生成过报盘文件
        DebitBaseInfo debitBaseInfo = new DebitBaseInfo();
        debitBaseInfo.setLoanId(loan.getId());
        debitBaseInfo.setOfferDate(Dates.getCurrDate());
        debitBaseInfo.setType(OfferTypeEnum.自动划扣.getValue());
        debitBaseInfo.setState(OfferStateEnum.已报盘.getValue());
        List<DebitBaseInfo> debitBaseInfos = debitBaseInfoDao.findListByVo(debitBaseInfo);
        if(CollectionUtils.isNotEmpty(debitBaseInfos)){
            logger.info("loanId：" + loan.getId() + " 当日已有自动报盘文件");
            return true;
        }
        return false;
    }
    
    @Override
    public List<DebitTransaction> getWeiHuiPanTraByloan(Long loanId) {
        List<DebitTransaction> debitTransaction = debitTransactionDao.findByStateAndLoan(OfferTransactionStateEnum.已发送,loanId);
        return debitTransaction;
    }

    /**
     * 更新状态（外贸3）
     * @param offerList
     * @param offerState
     */
    public void updateOfferStateWM3ToYibaopan(WM3_2311Vo wM3_2311Vo, WM3_2311OutputVo wM3_2311OutputVo) {
        List<DeductMoneyListEntity> deductMoneyList = wM3_2311Vo.getList();
        //更新所有报盘数据状态
        for(DeductMoneyListEntity deductMoneyEntity : deductMoneyList){
            //流水信息更新
            DebitTransaction searchVo = new DebitTransaction();
            // 报盘流水号
            String serialNo = deductMoneyEntity.getSerialNo();
            logger.info("外贸3报盘流水号："+serialNo);
            searchVo.setSerialNo(serialNo);
            List<DebitTransaction> debitTransactions = debitTransactionDao.findListByVo(searchVo);
            logger.info("根据报盘流水号查询到的结果集大小："+ debitTransactions.size());
            DebitTransaction debitTransaction = debitTransactions.get(0);
            debitTransaction.setState(OfferTransactionStateEnum.已发送.getValue());
            debitTransactionDao.update(debitTransaction);
            
            //报盘信息更新
            DebitBaseInfo debitBaseInfo = debitBaseInfoDao.get(debitTransaction.getDebitId());
            debitBaseInfo.setState(OfferStateEnum.已报盘.getValue());
            debitBaseInfoDao.update(debitBaseInfo);
        }
        
        // 还款报盘接收失败的数据
        List<FalseRecordListEntity> falseRecordList = wM3_2311OutputVo.getList();
        if(CollectionUtils.isEmpty(falseRecordList)){
            logger.info("外贸3批次号："+wM3_2311OutputVo.getBatNo()+"的还款报盘数据全部接收成功！");
            return;
        }
        //更新失败数据状态
        for(FalseRecordListEntity falseRecordEntity : falseRecordList){
            //流水信息更新
            DebitTransaction searchVo = new DebitTransaction();
            searchVo.setBatNo(wM3_2311OutputVo.getBatNo());
            searchVo.setPactNo(falseRecordEntity.getPactNo());
            List<DebitTransaction> debitTransactions = debitTransactionDao.findListByVo(searchVo);
            DebitTransaction debitTransaction = debitTransactions.get(0);
            debitTransaction.setState(OfferTransactionStateEnum.扣款失败.getValue());
            debitTransaction.setMemo("由于外贸3接收数据失败导致报盘失败");
            debitTransactionDao.update(debitTransaction);
            
            //报盘信息更新
            DebitBaseInfo debitBaseInfo = debitBaseInfoDao.get(debitTransaction.getDebitId());
            debitBaseInfo.setState(OfferStateEnum.已回盘非全额.getValue());
            debitBaseInfo.setMemo("由于外贸3接收数据失败导致报盘失败");
            debitBaseInfoDao.update(debitBaseInfo);
        }
    }
    
    
    /**
     * 更新状态（第三方结构划扣）
     * @param offerList
     * @param offerState
     */
    @Override
    public void updateDebitStateToYibaopan(List<DebitTransaction> debitTransactionList) {
    	// 获取系统时间为拼接批次号
    	String batchNo = "LJS_" + DateTime.now().toString("yyyyMMddHHmmssSSS");
        //更新所有报盘数据状态
        for(DebitTransaction debitTransaction : debitTransactionList){
        	debitTransaction.setBatNo(batchNo);
        	debitTransaction.setState(OfferTransactionStateEnum.已发送.getValue());
            debitTransactionDao.update(debitTransaction);//第三方划扣流水表
            
            DebitBaseInfo debitBaseInfo = debitBaseInfoDao.get(debitTransaction.getDebitId());
            debitBaseInfo.setState(OfferStateEnum.已报盘.getValue());
            debitBaseInfoDao.update(debitBaseInfo);//第三方划扣信息表
        }
    }
    
    public DebitTransaction findDebitTransactionBySerialNo(String serialNo) {
        DebitTransaction searchVo = new DebitTransaction();
        searchVo.setSerialNo(serialNo);
        List<DebitTransaction> debitTransactionList = debitTransactionDao.findListByVo(searchVo);
        if (CollectionUtils.isNotEmpty(debitTransactionList)) {
            return debitTransactionList.get(0);
        }
        return null;
    }
}
