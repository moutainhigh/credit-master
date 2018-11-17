package com.zdmoney.credit.trustOffer.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.AccountStateEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.PayOffFlagEnum;
import com.zdmoney.credit.common.constant.repay.RequestManagementConst;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.file.FileUtil;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanReturnRecord;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferFlowService;
import com.zdmoney.credit.repay.vo.RepayStateDetail;
import com.zdmoney.credit.system.domain.SysActionLog;
import com.zdmoney.credit.system.service.pub.ILoanReturnRecordService;
import com.zdmoney.credit.system.service.pub.ISysActionLogService;
import com.zdmoney.credit.trustOffer.service.pub.IBhTrustCommonService;

/**
 * @author YM10112 2018年1月8日 下午5:35:02
 */
@Service
public class BhTrustCommonServiceImpl implements IBhTrustCommonService {
	private static final Logger logger = Logger.getLogger(BhTrustCommonServiceImpl.class);
	private static final String CODE = "ZDCF02_paymentdetail_";
	private static final BigDecimal ZERO = new BigDecimal("0");
	@Autowired
	private IVLoanInfoService vLoanInfoService;
	@Autowired
    private IConnectBhxtFtpService connectBhxtFtpService;
	@Autowired
	private ILoanReturnRecordService loanReturnRecordService;
	@Autowired
    private ILoanRepaymentDetailService loanRepaymentDetailService;
	@Autowired
	private IOfferFlowService offerFlowService;
	@Autowired
	private ISysActionLogService sysActionLogService;
	
	@Override
	public Map<String, Object> createRepayStateDetail(Map<String, Object> params) {
		Map<String, Object> result = null;
		String currentDate = "";
		String beforeMonthLastDay = "";
		Calendar calendar = Calendar.getInstance();
        if(params == null){
            calendar.setTime(Dates.getCurrDate());
            calendar.add(Calendar.MONTH, -1);
            beforeMonthLastDay = Dates.getDateTime(Dates.getMonthLastDay(calendar.getTime()), Dates.DATAFORMAT_YYYYMMDD);
            currentDate = Dates.getDateTime(calendar.getTime(),Dates.DATAFORMAT_YYYYMM);
        }else{
        	String dateStr = params.get("date").toString();
        	Date date = Dates.getDateByString(dateStr, Dates.DATAFORMAT_YYYYMM);
        	calendar.setTime(date);
        	calendar.add(Calendar.MONTH, -1);
            beforeMonthLastDay = Dates.getDateTime(Dates.getMonthLastDay(calendar.getTime()), Dates.DATAFORMAT_YYYYMMDD);
        	currentDate = Dates.getDateTime(calendar.getTime(),Dates.DATAFORMAT_YYYYMM);
        }
        params =new HashMap<String, Object>();
        params.put("currentDate",currentDate);
        params.put("loanStates", new String[]{LoanStateEnum.正常.getValue(),LoanStateEnum.逾期.getValue(),LoanStateEnum.结清.getValue(),LoanStateEnum.预结清.getValue()});
        params.put("fundsSourcesList", new String[]{FundsSourcesTypeEnum.渤海2.getValue()});
		List<VLoanInfo> vLoanInfoList  = vLoanInfoService.findListByMap(params);
		if(CollectionUtils.isEmpty(vLoanInfoList)){
			logger.info("暂没有符合条件的债权信息需要同步到渤海2！");
			result = new HashMap<String, Object>();
			result.put("code", Constants.DATA_ERR_CODE);
			result.put("message", "失败：暂没有符合条件的债权信息需要同步到渤海2！");
			return result;
		}
		List<RepayStateDetail> detailList = new ArrayList<RepayStateDetail>();
		for(VLoanInfo vLoanInfo : vLoanInfoList){
			Long loanId = vLoanInfo.getId();
			params.put("loanId",loanId);
			Date buyBackTime = getLoanBuyBackTime(params);
			final Date buyBackTimeLastDay = buyBackTime == null? null : Dates.getMonthLastDay(buyBackTime);
			RepayStateDetail repayOverdue = vLoanInfoService.getRepayStateDetailOverdueValue(params);
			List<LoanRepaymentDetail> loanRepaymentDetailList = loanRepaymentDetailService.findListByMap(params);
			List<LoanRepaymentDetail> resultList = (List<LoanRepaymentDetail>)CollectionUtils.select(loanRepaymentDetailList,new Predicate<LoanRepaymentDetail>(){
				@Override
				public boolean evaluate(LoanRepaymentDetail detail) {
					Date factDate = detail.getFactReturnDate();
					Date returnDate = detail.getReturnDate();
					if(Strings.isNotEmpty(buyBackTimeLastDay) && returnDate.compareTo(buyBackTimeLastDay) <= 0){
						//如果债权被回购，则只统计还款日小于回购日期之前的
						return true;
					}
					if(Strings.isEmpty(factDate) && Strings.isEmpty(buyBackTimeLastDay)){
						//1：未结清，2：未还款 ，3：逾期 ，4：未回购
						return true;
					}
					if(Strings.isNotEmpty(factDate) && factDate.compareTo(returnDate) >= 0 && Strings.isEmpty(buyBackTimeLastDay)){
						//1：提前结清 ， 2：当期结清 ，3：未回购
						return true;
					}
					return false;
				}
			});
			if(CollectionUtils.isEmpty(resultList)){
				logger.info("暂没有符合条件的还款计划信息需要同步到渤海2！");
				continue;
			}
			List<LoanRepaymentDetail> loanDetailList = loanRepaymentDetailService.findLoanRepaymentDetailListByLoanId(loanId);
			LoanRepaymentDetail loanDetail = loanDetailList.get(loanDetailList.size()-1);//还款计划的最后一期
			Date advanceDate = null;
			if(Strings.isNotEmpty(loanDetail.getFactReturnDate()) && loanDetail.getFactReturnDate().compareTo(loanDetail.getReturnDate())<0){
				advanceDate = loanDetail.getFactReturnDate();//提前结清时间
			}
			LoanRepaymentDetail repayDetail = resultList.get(resultList.size()-1);
			String accountState = setAccountState(vLoanInfo,repayOverdue,buyBackTimeLastDay,repayDetail,advanceDate);
			String payOffDate = "";
			if(AccountStateEnum.结清.getCode().equals(accountState)){
				logger.info("债权【"+loanId+"】的结清日期为："+repayDetail.getFactReturnDate());
				payOffDate = Dates.getDateTime(repayDetail.getFactReturnDate(),Dates.DATAFORMAT_SLASHDATE);
			}
			Map<String, Object> loanRepayParams = new HashMap<String, Object>();
			for(LoanRepaymentDetail loanRepaymentDetail : resultList){
				if(vLoanInfo.getTime().compareTo(loanRepaymentDetail.getCurrentTerm()) == 0){
					loanRepayParams.put("lastTerm","true");//最后一期
					loanRepayParams.put("currentDate",currentDate);
				}
				loanRepayParams.put("loanId",loanRepaymentDetail.getLoanId());
				loanRepayParams.put("loanRepayDetailReturnDate", Dates.getDateTime(loanRepaymentDetail.getReturnDate(),Dates.DATAFORMAT_YYYYMM));
				RepayStateDetail repayReal = this.getRepayRealValue(loanRepayParams);
				RepayStateDetail detail = new RepayStateDetail();
				detail.setProjectCode(RequestManagementConst.TRUST_PROJECT_CODE2);
				detail.setContractNum(vLoanInfo.getContractNum());
				detail.setTerm(loanRepaymentDetail.getCurrentTerm());
				detail.setReturnDate(Dates.getDateTime(loanRepaymentDetail.getReturnDate(), Dates.DATAFORMAT_SLASHDATE));
				detail.setLastDay("");//宽限期最后一天
				detail.setReturnCorpus(loanRepaymentDetail.getReturneterm().subtract(loanRepaymentDetail.getCurrentAccrual()));//应还本金
				detail.setReturnAccrual(loanRepaymentDetail.getCurrentAccrual());//应还利息
				detail.setReturnCorpusOine(repayReal.getRealCorpusOine());//应还本金罚息
				detail.setReturnAccrualOine(ZERO);//应还利息罚息 0  利息罚息不统计
				detail.setRealReturnDate(repayReal.getRealReturnDate());//实际还款日期
				detail.setRealCorpus(repayReal.getRealCorpus());//实还本金
				detail.setRealAccrual(repayReal.getRealAccrual());//实还利息
				detail.setRealCorpusOine(repayReal.getRealCorpusOine());//实还本金罚息
				detail.setRealAccrualOine(ZERO);//实还利息罚息  0 利息罚息不统计
				detail.setCurrentPayOffFlag(setCurrentPayOffFlag(loanRepaymentDetail,repayReal,advanceDate));//本期结清标志  
				detail.setUpdate(Dates.getDateTime(new Date(),Dates.DATAFORMAT_SLASHDATE));//更新日期
				detail.setAccountState(accountState);
				detail.setPayOffDate(payOffDate);//结清日期
				detail.setOverdueCorpus(repayOverdue == null? ZERO : repayOverdue.getOverdueCorpus());//逾期本金
				detail.setOverdueAccrual(repayOverdue == null? ZERO : repayOverdue.getOverdueAccrual());//逾期利息
				detailList.add(detail);
			}
		}
		boolean flag = uploadFileToFtp(detailList,beforeMonthLastDay);
		result = new HashMap<String, Object>();
		result.put("code", Constants.SUCCESS_CODE);
		result.put("message", "上传还款状态文件成功！");
		if(!flag){
			result.put("code", Constants.DATA_ERR_CODE);
			result.put("message", "上传还款状态文件到渤海2失败:");
		}
		return result;
	}
	
	/**
	 * 根据参数得到债权的回购日期
	 * @param params
	 * @return
	 */
	public Date getLoanBuyBackTime(Map<String, Object> params){
		params.put("buyBackTime",params.get("currentDate").toString());
		List<LoanReturnRecord> loanReturnRecordList = loanReturnRecordService.findListByMap(params);
		Date buyBackTime = null;
		if(loanReturnRecordList != null && loanReturnRecordList.size()>0){
			buyBackTime = loanReturnRecordList.get(0).getBuyBackTime();
		}
		return buyBackTime;
	}
	
	/**
	 * 得到实际还款的值 本金 利息 本金罚息 
	 * @param params
	 * @return
	 */
	public RepayStateDetail getRepayRealValue(Map<String, Object> params){
		RepayStateDetail repayReal = offerFlowService.getRepayStateDetailRealValue(params);
		return repayReal;
	}
	
	/**
	 * 得到当期的结清标志 
	 * @param loanRepaymentDetail
	 * @param repayReal
	 * @return
	 */
	public String setCurrentPayOffFlag(LoanRepaymentDetail loanRepaymentDetail,RepayStateDetail repayReal,Date advanceDate){
		Date  factReturnDate = loanRepaymentDetail.getFactReturnDate();
		Date  returnDate = loanRepaymentDetail.getReturnDate();
		Date currentDate = Dates.format(Dates.addMonths(-1),Dates.DATAFORMAT_SLASHDATE);//统计月份的上个月
		String state = "";
		if(Strings.isNotEmpty(factReturnDate)){
			if(factReturnDate.compareTo(returnDate) == 0){
				state = PayOffFlagEnum.正常结清.getCode();
				if(Strings.isNotEmpty(advanceDate) && advanceDate.compareTo(factReturnDate) == 0){
					state = PayOffFlagEnum.提前结清.getCode();
				}
			}else if(factReturnDate.compareTo(returnDate) == -1){
				state = PayOffFlagEnum.提前结清.getCode();
			}else if(factReturnDate.compareTo(returnDate) == 1 && 
					(isEqualsMonth(factReturnDate, currentDate) || factReturnDate.compareTo(currentDate) < 0)){
		        state = PayOffFlagEnum.逾期结清.getCode();
			}else{
				state = PayOffFlagEnum.逾期未还款.getCode();
				if(BigDecimal.ZERO.compareTo(repayReal.getTradeAmount()) != 0){
					state = PayOffFlagEnum.逾期部分还款.getCode();
				}
			}
			return state;
		}
		//如果还款计划结清日期为空，为逾期
		state = PayOffFlagEnum.逾期未还款.getCode();
		if(BigDecimal.ZERO.compareTo(repayReal.getTradeAmount()) != 0){
			//offer_flow.trade_amount 交易金额 大于0
			state = PayOffFlagEnum.逾期部分还款.getCode();
		}
		return state;
	}
	
	/**
	 * 设置账户状态
	 * @param vLoanInfo
	 * @param repayOverdue
	 * @param buyBackTime
	 * @param detail
	 * @param advancePayOff
	 * @return
	 */
	public String setAccountState(VLoanInfo vLoanInfo,RepayStateDetail repayOverdue,Date buyBackTime,LoanRepaymentDetail detail,Date advanceDate){
		String accountState = "";
		if(Strings.isNotEmpty(buyBackTime)){
			accountState = AccountStateEnum.转出.getCode();
			return accountState;
		}
		if(Strings.isNotEmpty(advanceDate)){//提前结清
			accountState = AccountStateEnum.结清.getCode();
			return accountState;
		}
		Date factReturnDate = detail.getFactReturnDate();
		Date returnDate = detail.getReturnDate();
		Date currentDate = Dates.format(Dates.addMonths(-1),Dates.DATAFORMAT_SLASHDATE);//统计月份的上个月
		if(Strings.isNotEmpty(factReturnDate)){
			if(vLoanInfo.getTime().compareTo(detail.getCurrentTerm()) == 0 && isEqualsMonth(factReturnDate,returnDate)){
				//正常结清 还款日 = 结清日
				accountState = AccountStateEnum.结清.getCode();
			}else if(vLoanInfo.getTime().compareTo(detail.getCurrentTerm()) == 0 && 
					(isEqualsMonth(factReturnDate,currentDate) || factReturnDate.compareTo(currentDate) < 0)){
				//逾期结清
				accountState = AccountStateEnum.结清.getCode();
			}else{
				if(factReturnDate.compareTo(returnDate) <= 0){
					accountState = AccountStateEnum.正常.getCode();
				}else if(factReturnDate.compareTo(returnDate) == 1 && isEqualsMonth(factReturnDate,returnDate)){
					accountState = AccountStateEnum.正常.getCode();
				}else{
					accountState = AccountStateEnum.逾期.getCode();
					if(repayOverdue != null && repayOverdue.getOverdueDays() > 180){
						accountState = AccountStateEnum.呆账.getCode();
					}
				}
			}
			return accountState;
		} 
		accountState = AccountStateEnum.逾期.getCode();
		if(repayOverdue != null && repayOverdue.getOverdueDays() > 180){
			accountState = AccountStateEnum.呆账.getCode();
		}
		return accountState;
	}
	
	/**
	 * 比较两个时间是否在同一个月份
	 * @param date1
	 * @param date2
	 * @return
	 */
	public boolean isEqualsMonth(Date date1,Date date2){
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int month1 = calendar.get(Calendar.MONTH) + 1;
        calendar.setTime(date2);
        int month2 = calendar.get(Calendar.MONTH) + 1;
        if(month1 == month2){
        	 return true;
        }
       return false;
	}
	
	/**
     * 初始化sftp信息，上传文件
     * @param file
     * @return
     */
	private boolean uploadFileToFtp(List<RepayStateDetail> detailList,String beforeMonthLastDay){
    	JschSftpUtils bhsftpUtil =null;
    	String batch = this.getNewBatchBySysActionLog();
		String realName = CODE+beforeMonthLastDay+"_"+batch+".txt";//ZDCF02_paymentdetail_20171131_001.txt
		String path = ConnectBhxtFtpServiceImpl.repayStateDetailUploadDirBH2;
		try {
			InputStream inputStream = FileUtil.write(detailList, RequestManagementConst.getRepayStateDetailFields(), RequestManagementConst.SEPARATOR, "UTF-8");
			bhsftpUtil = connectBhxtFtpService.getFtpBhxtConnectJsch();
            if (!bhsftpUtil.login()) {
                logger.error("登录渤海sftp服务器失败!");
                return false;
            }
			logger.info("上传还款状态文件到渤海服务器连接FTP成功!");
			if(!bhsftpUtil.changeDir(path)){
				logger.error("找不到存放还款状态文件的目录"+path);
				throw new PlatformException(ResponseEnum.FULL_MSG, "找不到存放还款状态文件目录："+path);
            }
			boolean flag = bhsftpUtil.uploadFile(path,realName,inputStream);
			inputStream.close();
			logger.info("文件名：【"+realName+"】，上传还款状态文件到渤海服务器成功!");
			this.saveSysActionLog(realName, beforeMonthLastDay);//保存操作日志
			return flag;
		}catch (IOException e) {
			logger.error("上传还款状态文件到渤海服务器失败!"+e.getMessage());
			e.printStackTrace();
			return false;
		}finally{
			if (bhsftpUtil != null) {
				bhsftpUtil.logout();
            }
		}
    }
	
	/**
	 * 得到最新的批次号
	 * @return
	 */
	public String getNewBatchBySysActionLog(){
		SysActionLog sysActionLog = new SysActionLog();
		sysActionLog.setContent(CODE);
		List<SysActionLog> sysActionLogList = sysActionLogService.findListByVo(sysActionLog);
		String batch = "000";
		if(CollectionUtils.isNotEmpty(sysActionLogList)){
			String content =  sysActionLogList.get(0).getContent();
			batch = content.substring(content.length()-7, content.length()-4);
		}
		batch = Strings.aa(batch,3);//字符串递增
		return batch;
	}
	
	/**
	 * 上传渤海还款状态文件，保存日志
	 * @param fileName 文件名
	 * @param beforeMonthLastDay 截止到的日期
	 */
	public void saveSysActionLog(String fileName,String beforeMonthLastDay){
		User user = UserContext.getUser();
    	String name = "admin";
    	String code = "管理员";
    	if(user != null){
    		name = user.getName();
    		code = user.getUserCode();
    	}
    	Date date = Dates.parse(beforeMonthLastDay, Dates.DATAFORMAT_YYYYMMDD);//当前日期为201802 ，上个月月底 20180131
		Date firstDay = Dates.addDay(date, 1);//当月月初 20180201
		String dateStr = Dates.getDateTime(firstDay,Dates.DATAFORMAT_YYYYMM);//当月  201802 
    	String url = "http://newcredit.ezendai.com/credit-admin/bhTrustCommon/uploadRepayStateDetail?date="+dateStr;
		sysActionLogService.createLog(name, code, "上传文件", "上传渤海还款状态文件", fileName,url, null);
	}
	
}
