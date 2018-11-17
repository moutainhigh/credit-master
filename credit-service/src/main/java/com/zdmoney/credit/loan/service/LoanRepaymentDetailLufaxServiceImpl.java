package com.zdmoney.credit.loan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100010Vo;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.RepaymentStateLufaxEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailLufaxDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetailLufax;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailLufaxService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
@Transactional
public class LoanRepaymentDetailLufaxServiceImpl implements ILoanRepaymentDetailLufaxService{

	private static final Logger logger = Logger.getLogger(LoanRepaymentDetailLufaxServiceImpl.class);

	@Autowired
	ILoanRepaymentDetailLufaxDao loanRepaymentDetailLufaxDaoImpl;
	@Autowired
	ISequencesService sequencesService;
	@Autowired
	ISplitQueueLogService SplitQueueLogServiceImpl;
	@Autowired
	IVLoanInfoDao ivLoanInfoDao;
	@Override
	public List<LoanRepaymentDetailLufax> createLufaxRepayment(List<LoanRepaymentDetail> repaymentDetails) {
		List<LoanRepaymentDetailLufax> list = null;
		if(CollectionUtils.isNotEmpty(repaymentDetails)){
			list = new ArrayList<LoanRepaymentDetailLufax>();
			for(LoanRepaymentDetail repaymentDetail: repaymentDetails){
				LoanRepaymentDetailLufax detailLufax = new LoanRepaymentDetailLufax();
				detailLufax.setAccrualRevise(repaymentDetail.getAccrualRevise());
				detailLufax.setCurrentAccrual(repaymentDetail.getCurrentAccrual());
				detailLufax.setCurrentTerm(repaymentDetail.getCurrentTerm());
				detailLufax.setDeficit(repaymentDetail.getDeficit());
				detailLufax.setFactReturnDate(repaymentDetail.getFactReturnDate());
				detailLufax.setGiveBackRate(repaymentDetail.getGiveBackRate());
				detailLufax.setLoanId(repaymentDetail.getLoanId());
				detailLufax.setPenalty(repaymentDetail.getPenalty());
				detailLufax.setPenaltyDate(repaymentDetail.getPenaltyDate());
				detailLufax.setPrincipalBalance(repaymentDetail.getPrincipalBalance());
				detailLufax.setRepaymentAll(repaymentDetail.getRepaymentAll());
				detailLufax.setRepaymentState(repaymentDetail.getRepaymentState());
				detailLufax.setReturnDate(repaymentDetail.getReturnDate());
				detailLufax.setReturneterm(repaymentDetail.getReturneterm());
				detailLufax.setId(sequencesService.getSequences(SequencesEnum.LOAN_REPAYMENT_DETAIL_LUFAX));
				loanRepaymentDetailLufaxDaoImpl.insert(detailLufax);
				list.add(detailLufax);
			}
		}
		return list;
	}
	
	
	@Override
	public LoanRepaymentDetailLufax findByLoanAndReturnDate(VLoanInfo loanInfo,
			Date lastRepayDate) {
		LoanRepaymentDetailLufax loanRepaymentDetailLufax = new LoanRepaymentDetailLufax();
        loanRepaymentDetailLufax.setLoanId(loanInfo.getId());
        loanRepaymentDetailLufax.setReturnDate(lastRepayDate);
        List<LoanRepaymentDetailLufax> list = loanRepaymentDetailLufaxDaoImpl.findListByVo(loanRepaymentDetailLufax);
        if(list!=null && list.size()>0){
            if(list.size()>1){
                throw new RuntimeException("得到陆金所指定还款日期的还款计划,得到过多的结果集！");
            }
            return list.get(0);
        }
        return null;
	}


	@Override
	public LoanRepaymentDetailLufax createLufaxRepaymentDetail(LoanRepaymentDetail repaymentDetail) {
		LoanRepaymentDetailLufax detailLufax = new LoanRepaymentDetailLufax();
		detailLufax.setAccrualRevise(repaymentDetail.getAccrualRevise());
		detailLufax.setCurrentAccrual(repaymentDetail.getCurrentAccrual());
		detailLufax.setCurrentTerm(repaymentDetail.getCurrentTerm());
		detailLufax.setDeficit(repaymentDetail.getDeficit());
		detailLufax.setFactReturnDate(repaymentDetail.getFactReturnDate());
		detailLufax.setGiveBackRate(repaymentDetail.getGiveBackRate());
		detailLufax.setLoanId(repaymentDetail.getLoanId());
		detailLufax.setPenalty(repaymentDetail.getPenalty());
		detailLufax.setPenaltyDate(repaymentDetail.getPenaltyDate());
		detailLufax.setPrincipalBalance(repaymentDetail.getPrincipalBalance());
		detailLufax.setRepaymentAll(repaymentDetail.getRepaymentAll());
		detailLufax.setRepaymentState(repaymentDetail.getRepaymentState());
		detailLufax.setReturnDate(repaymentDetail.getReturnDate());
		detailLufax.setReturneterm(repaymentDetail.getReturneterm());
		detailLufax.setLoanStatusLufax(RepaymentStateLufaxEnum.未付.getCode());
		detailLufax.setId(sequencesService.getSequences(SequencesEnum.LOAN_REPAYMENT_DETAIL_LUFAX));
		return loanRepaymentDetailLufaxDaoImpl.insert(detailLufax);
	}

	@Override
	public boolean pushOverdueRepaymentPlan() {
		try{
			VLoanInfo vLoanInfo = new VLoanInfo();
			vLoanInfo.setLoanBelong(FundsSourcesTypeEnum.陆金所.getValue());
			vLoanInfo.setLoanState(LoanStateEnum.逾期.getValue());
			List<VLoanInfo> list = ivLoanInfoDao.findListByVo(vLoanInfo);
			if(CollectionUtils.isEmpty(list)){
				logger.info("推送逾期还款计划至陆金所。。。没有待推送的记录");
				return true;
			}
			Lufax100010Vo vo = SplitQueueLogServiceImpl.getLufax100010VoByRepaymentPlan(list);
			JSONObject grantResut = null;
			grantResut = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.证大同步还款计划给陆金所.getCode());
			logger.info("推送逾期还款计划至陆金所。。。陆金所返回的数据："+grantResut);
			String ret_code = (String)grantResut.get("ret_code");
			String ret_msg = (String)grantResut.get("ret_msg");
			if("0000".equals(ret_code)){
				logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"推送逾期还款计划至陆金所。。。接口调用成功！"+ret_msg);
				return true;
			}else{
				logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"推送逾期还款计划至陆金所。。。接口调用失败：原因："+ret_msg);
				return false;
			}
		}catch(Exception e){
			logger.warn("推送逾期还款计划至陆金所异常："+e.getMessage(),e);
			throw new PlatformException(ResponseEnum.FULL_MSG,"【陆金所】推送逾期还款计划接口异常"+e.getMessage());
		}
	}
	
	@Override
	public void deleteByLoanId(Long loanId) {
		loanRepaymentDetailLufaxDaoImpl.deleteByLoanId(loanId);
	}
	
}
