package com.zdmoney.credit.offer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.bsyh.service.pub.IRepayResultNotifyLogService;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.OfferTypeEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.constant.tpp.TppPaySysNoEnum;
import com.zdmoney.credit.common.constant.tpp.TppRealPaySysNoEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.TPPHelper;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.OfferParamsVo;
import com.zdmoney.credit.debit.dao.pub.IDebitBaseInfoDao;
import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.debit.service.pub.IDebitTransactionService;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;
import com.zdmoney.credit.loan.domain.RepayResultNotifyLog;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.dao.pub.IOfferInfoDao;
import com.zdmoney.credit.offer.domain.OfferInfo;
import com.zdmoney.credit.offer.service.pub.IOfferConfigService;
import com.zdmoney.credit.offer.service.pub.IOfferCreateService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferTransactionService;
import com.zdmoney.credit.system.dao.pub.IComOrganizationDao;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zendaimoney.thirdpp.vo.enums.ThirdPartyType;

@Service
@Transactional
public class OfferCreateServiceImpl implements IOfferCreateService{
	private static final Logger logger = Logger.getLogger(OfferCreateServiceImpl.class);
	
	@Autowired
	private IOfferInfoService offerInfoService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private IVLoanInfoService vLoanInfoService;
	@Autowired
	private IOfferTransactionService offerTransactionService;
	@Autowired
	private IComOrganizationDao comOrganizationDao;
	@Autowired
	private IOfferInfoDao offerInfoDao;
	@Autowired
	private ILoanSpecialRepaymentService loanSpecialRepaymentService;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
    @Autowired
    private IOfferConfigService offerConfigService;
    @Autowired
	private ILoanFeeInfoService loanFeeInfoService;
    @Autowired
	private IAfterLoanService afterLoanService;
    @Autowired
	private IDebitTransactionService debitTransactionServiceImpl;
	@Autowired
	private IDebitBaseInfoDao debitBaseInfoDaoImpl;
	@Autowired
	private ISplitQueueLogService splitQueueLogService;
	@Autowired
    private IRepayResultNotifyLogService repayResultNotifyLogService;
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int createOfferRecordWithOrgAndType(ComOrganization salesDepartment, int type) {
        List<VLoanInfo> list = null;
        //根据类型得到相应的loan记录
        switch (type) {
        case IOfferInfoService.ZHENGCHANG:
            list=offerInfoService.findLoansByZCHK(salesDepartment);
            break;
        case IOfferInfoService.YUQI:
            list=offerInfoService.findLoansByYQHK(salesDepartment);
            break;
        case IOfferInfoService.TIQIANKOUKUAN:
            list=offerInfoService.findLoansByTQHK(salesDepartment);
            break;
        case IOfferInfoService.TIQIANJIEQING:
            list=offerInfoService.findLoansByTQJQ(salesDepartment);
            break;
        default:
            logger.error("类型无法解析，报盘文件未生成！传入类型："+type);
            return 0;
        }
        Integer indexCount = 0;
        String loanBelong = null;
        if(CollectionUtils.isNotEmpty(list)){
            for(VLoanInfo loan : list){
                // 创建报盘信息
                OfferInfo offer = null;
                DebitBaseInfo debitBaseInfo = null;
                // 债权去向
                loanBelong = loan.getLoanBelong();
                try {
                    if ((IOfferInfoService.TIQIANKOUKUAN == type || IOfferInfoService.TIQIANJIEQING == type) 
                        && FundsSourcesTypeEnum.包商银行.getValue().equals(loanBelong)) {
                        // 包商银行提前还款提前结清，不做自动报盘
                    } else if (FundsSourcesTypeEnum.外贸3.getValue().equals(loanBelong)
                            || (FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong) 
                                && (type == IOfferInfoService.ZHENGCHANG || type == IOfferInfoService.TIQIANKOUKUAN))) {
                        // 创建外部机构还款报盘
                        debitBaseInfo = this.createDebitBaseInfo(loan, salesDepartment, type);
                    } else {
                        // 创建TPP还款报盘
                        offer = this.createOfferInfo(loan, salesDepartment, type);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage()+",创建报盘文件错误 loanid="+loan.getId(), e);
                    continue;
                }
                if(offer!= null){
                    indexCount++;
                }
                if(debitBaseInfo!= null){
                    indexCount++;
                }
            }
        }
        return indexCount;
    }
    
    /**
     * 创建外部机构还款报盘
     * @param loan
     * @param salesDepartment
     * @param type
     * @return
     */
    private DebitBaseInfo createDebitBaseInfo(VLoanInfo loan, ComOrganization salesDepartment, int type) {
        // 查询该记录是否有未回盘情况
        if (debitTransactionServiceImpl.checkHasOffer(loan)) {
            logger.warn("有未回盘记录，该次自动报盘文件未生成！loan_id=" + loan.getId());
            // 记录日志表等待人工处理
            loanLogService.createLog("debitBaseInfoCreate", "warn", "有未回盘记录，该次自动报盘文件未生成！loan_id=" + loan.getId(), "system");
            return null;
        }
        return offerInfoService.createDebitOffer(loan, salesDepartment, type, null, null);
    }
    
    /**
     * 创建TPP还款报盘
     * @param loan
     * @param salesDepartment
     * @param type
     * @return
     */
    private OfferInfo createOfferInfo(VLoanInfo loan, ComOrganization salesDepartment, int type) {
        // 查询该记录是否有未回盘情况
        if (offerInfoService.checkHasOffer(loan)) {
            logger.warn("有未回盘记录，该次自动报盘文件未生成！loan_id=" + loan.getId());
            // 记录日志表等待人工处理
            loanLogService.createLog("offerCreate", "warn", "有未回盘记录，该次自动报盘文件未生成！loan_id=" + loan.getId(), "system");
            return null;
        }
        return offerInfoService.createOffer(loan, salesDepartment, type, null, null);
    }
    
    /**
     * 实时扣款推送接口方法<p>
     * @param paramsVo 报盘参数VO
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> createRealtimeOfferInfo(OfferParamsVo paramsVo) {
        Long loanId = paramsVo.getLoanId();
        // 根据债权编号对报盘信息进行校验
        String errorMsg = validateOffer(paramsVo);
        if (Strings.isNotEmpty(errorMsg)) {
            return MessageUtil.returnErrorMessage(errorMsg);// 校验失败，存在错误信息
        }
        VLoanInfo loan = vLoanInfoService.findByLoanId(loanId);
        ComOrganization organization = comOrganizationDao.get(loan.getSalesDepartmentId());
        // 是否申请了提前还款
        boolean isAdvanceRepayment = afterLoanService.isAdvanceRepayment(loanId);
        // 是否申请提前结清
        boolean isOneTimeRepayment = afterLoanService.isOneTimeRepayment(loanId);
        
        // 如果不显示划扣通道，需配置默认划扣通道
        this.againSetPaySysNo(paramsVo, loan, isOneTimeRepayment);
        
        // 实时划扣校验
        errorMsg = this.validateRealtimeOfferInfo(paramsVo, loan, isAdvanceRepayment, isOneTimeRepayment);
        if (Strings.isNotEmpty(errorMsg)) {
            // 校验失败，存在错误信息
            return MessageUtil.returnErrorMessage(errorMsg);
        }
        // 如果是外贸3的实时划扣、需发送给外贸信托机构去划扣
        if (FundsSourcesTypeEnum.外贸3.getValue().equals(loan.getLoanBelong())) {
            return this.createAndSendDebitOffer(paramsVo, loan, organization);
        }
        // 陆金所需发送到陆金所进行划扣
        if (FundsSourcesTypeEnum.陆金所.getValue().equals(loan.getLoanBelong())) {
            // 一次性结清实时划扣
            if (isOneTimeRepayment) {
                return this.creatAndSendOffer(paramsVo, loan, organization);
            }
            // 提前还款、还款日还款实时划扣
            if (isAdvanceRepayment|| LoanStateEnum.正常.getValue().equals(loan.getLoanState())) {
                return this.createAndSendDebitOffer(paramsVo, loan, organization);
            }
        }
        // 非外贸3的实时划扣、发送给TPP去划扣
        return this.creatAndSendOffer(paramsVo, loan, organization);
    }

	public boolean checkNeedCreateOffer(VLoanInfo loan){
		boolean flag=false;
		String loanState=loan.getLoanState();
		if(LoanStateEnum.逾期.name().equals(loanState)){
			flag=true;
		}else if(LoanStateEnum.正常.name().equals(loanState) && loan.getPromiseReturnDate()==new Date().getDate() && !loan.getLoanBelong().equals(FundsSourcesTypeEnum.包商银行.getValue())){
			flag=true;
		}
		LoanSpecialRepayment ltqkk = loanSpecialRepaymentService.findbyLoanAndType(loan.getId(), SpecialRepaymentTypeEnum.提前扣款.getValue(), SpecialRepaymentStateEnum.申请.getValue());
		LoanSpecialRepayment lycxhq = loanSpecialRepaymentService.findbyLoanAndType(loan.getId(), SpecialRepaymentTypeEnum.一次性还款.getValue(), SpecialRepaymentStateEnum.申请.getValue());
		if(ltqkk!=null || lycxhq!=null){
			return true;
		}
		 
		return flag;
	}
	
	/**
	 * 根据债权编号对报盘信息进行校验
	 * @param loanId 债权编号
	 * @return
	 */
	public String validateOffer(OfferParamsVo paramsVo){
		Long loanId=paramsVo.getLoanId();
		String usercode= paramsVo.getUserCode();
		String errorMsg=null;
		//判断是否在允许划扣时间段内
		String HHmm = sysParamDefineService.getSysParamValue("offer", "realTimeDeductCloseTime");
		if(Strings.isEmpty(HHmm)){
			HHmm = "20:00";//默认是20点
		}
		String dateTimes = Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT)+" "+ HHmm+":00";
		Date closedateTime = Dates.parse(dateTimes,Dates.DEFAULT_DATETIME_FORMAT);
		if(closedateTime==null){//格式化失败的情况
			logger.error("格式化realTimeDeductCloseTime失败！启用默认时间20：00");
			dateTimes = Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT)+" "+"20:00:00";
			closedateTime = Dates.parse(dateTimes,Dates.DEFAULT_DATETIME_FORMAT);
		}
		Date currdate = Dates.getNow();
		if(currdate.after(closedateTime)){
			errorMsg="当日自动划扣结束时间为"+dateTimes+",请明天再试！";
			return errorMsg;
		}
		
		// 自动报盘前后15分钟时间不允许进行实时划扣的校验
		errorMsg = this.validateDebitTime();
		if(Strings.isNotEmpty(errorMsg)){
			return errorMsg;
		}
		
		VLoanInfo loan=vLoanInfoService.findByLoanId(loanId);
		if(loan==null){
			errorMsg="无法找到对应的借款记录！";
			return errorMsg;
		}
		//校验1
		boolean isWeihuipan = offerInfoService.checkHasOffer(loan);
		if(isWeihuipan){
			errorMsg ="该客户有尚未回盘的划扣请求！";
			return errorMsg;
		}
		
		//校验1 第三方划扣
		boolean isWeihuipanDebit = debitTransactionServiceImpl.checkHasOffer(loan);
		if(isWeihuipanDebit){
			errorMsg ="第三方划扣：该客户有尚未回盘的划扣请求！";
			return errorMsg;
		}
				
		//校验2：
		OfferInfo offerVo  = new OfferInfo();
		offerVo.setLoanId(loanId);
		offerVo.setType(OfferTypeEnum.实时划扣.name());
		offerVo.setOfferDate(DateUtils.truncate(new Date(), Calendar.DATE));
		Integer countOffer = offerInfoDao.getOfferCountForYet3Deduct(offerVo);
		
		//校验2： 第三方划扣
		DebitBaseInfo debitBaseInfo = new DebitBaseInfo();
		debitBaseInfo.setLoanId(loanId);
		debitBaseInfo.setType(OfferTypeEnum.实时划扣.name());
		debitBaseInfo.setOfferDate(DateUtils.truncate(new Date(), Calendar.DATE));
		Integer countDebit = debitBaseInfoDaoImpl.getOfferCountForYet3Deduct(debitBaseInfo);
		
		Integer count = countOffer+countDebit;
				
		//员工最高划扣次数
		String maxnum=offerInfoService.getoffernum(usercode);
		//划扣次数、默认是3次
		int maxcount = 3;
		if (maxnum==null){
		/*	// 获取代扣通道
			String tppType = offerInfoService.getTppType(loan);
			// 代扣通道是用友（畅捷支付）
			if(ThirdPartyType.YYoupay.name().equals(tppType)){
				// 如果划扣这一天是还款日，则只能划扣1次
				if(vLoanInfoService.isRepaymentDay(loanId)){
					maxcount = 1;
				}else{
					// 非还款日每日保留2次手动实时报盘
					maxcount = 2;
				}
			}else{
				// 代扣通道非用友（畅捷支付），默认划扣3次
				maxcount = this.getMaxCount();
			}*/
			//配置表中获取一天最大实时划扣次数
			maxcount = this.getMaxCount();
		}else if(Strings.isEmpty(maxnum) || "0".equalsIgnoreCase(maxnum)){
			errorMsg ="该员工没有划扣权限！";
			return errorMsg;
		}else if(maxnum.equals("-1")){
			return null;
		}else{
			//划扣次数
			maxcount = Integer.parseInt(maxnum);
		}
		if(count>=maxcount){
			errorMsg ="今日该客户已经进行过"+count+"次实时划扣！";
			return errorMsg;
		}
		return errorMsg;
	}

	/**
	 * 根据债权编号对外贸3报盘信息进行校验
	 * @param loanId 债权编号
	 * @return
	 */
	public String validateOfferWM3(OfferParamsVo paramsVo){
		Long loanId=paramsVo.getLoanId();
		String usercode= paramsVo.getUserCode();
		String errorMsg=null;
		//判断是否在允许划扣时间段内
		String HHmm = sysParamDefineService.getSysParamValue("offer", "realTimeDeductCloseTime");
		if(Strings.isEmpty(HHmm)){
			HHmm = "20:00";//默认是20点
		}
		String dateTimes = Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT)+" "+ HHmm+":00";
		Date closedateTime = Dates.parse(dateTimes,Dates.DEFAULT_DATETIME_FORMAT);
		if(closedateTime == null){//格式化失败的情况
			logger.error("格式化realTimeDeductCloseTime失败！启用默认时间20：00");
			dateTimes = Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT)+" "+"20:00:00";
			closedateTime = Dates.parse(dateTimes,Dates.DEFAULT_DATETIME_FORMAT);
		}
		Date currdate = Dates.getNow();
		if(currdate.after(closedateTime)){
			errorMsg="当日自动划扣结束时间为"+dateTimes+",请明天再试！";
			return errorMsg;
		}
		
		// 自动报盘前后15分钟时间不允许进行实时划扣的校验
		errorMsg = this.validateDebitTime();
		if(Strings.isNotEmpty(errorMsg)){
			return errorMsg;
		}
		
		VLoanInfo loan=vLoanInfoService.findByLoanId(loanId);
		if(loan==null){
			errorMsg="无法找到对应的借款记录！";
			return errorMsg;
		}
		//校验1
		DebitBaseInfo debitBaseInfo = new DebitBaseInfo();
		debitBaseInfo.setLoanId(loanId);
		debitBaseInfo.setState(OfferStateEnum.已报盘.name());
		boolean isWeihuipan = debitTransactionServiceImpl.checkHasOffer(loan);
		if(isWeihuipan){
			errorMsg ="该客户有尚未回盘的划扣请求！";
			return errorMsg;
		}
		
		//校验2：
		debitBaseInfo = new DebitBaseInfo();
		debitBaseInfo.setLoanId(loanId);
		debitBaseInfo.setType(OfferTypeEnum.实时划扣.name());
		debitBaseInfo.setOfferDate(DateUtils.truncate(new Date(), Calendar.DATE));
		Integer count = debitBaseInfoDaoImpl.getOfferCountForYet3Deduct(debitBaseInfo);
		
		//员工最高划扣次数
		String maxnum=offerInfoService.getoffernum(usercode);
		//划扣次数、默认是3次
		int maxcount = 3;
		if (maxnum==null){
			// 获取代扣通道
			String tppType = offerInfoService.getTppType(loan);
			// 代扣通道是用友（畅捷支付）
			if(ThirdPartyType.YYoupay.name().equals(tppType)){
				// 如果划扣这一天是还款日，则只能划扣1次
				if(vLoanInfoService.isRepaymentDay(loanId)){
					maxcount = 1;
				}else{
					// 非还款日每日保留2次手动实时报盘
					maxcount = 2;
				}
			}else{
				// 代扣通道非用友（畅捷支付），默认划扣3次
				maxcount = this.getMaxCount();
			}
		}else if(Strings.isEmpty(maxnum) || "0".equalsIgnoreCase(maxnum)){
			errorMsg ="该员工没有划扣权限！";
			return errorMsg;
		}else if(maxnum.equals("-1")){
			return null;
		}else{
			//划扣次数
			maxcount = Integer.parseInt(maxnum);
		}
		if(count>=maxcount){
			errorMsg ="今日该客户已经进行过"+count+"次实时划扣！";
			return errorMsg;
		}
		return errorMsg;
	}
	
    /**
     * 自动报盘前后15分钟时间不允许进行实时划扣的校验
     * @return
     */
    private String validateDebitTime() {
        try {
            // 查询当天报盘所有划扣时间
            List<String> debitTimeList = offerConfigService.queryDebitTimeList();
            if (CollectionUtils.isNotEmpty(debitTimeList)) {
                for (String debitTime : debitTimeList) {
                    Date debitDate = Dates.parse(Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) + " " + debitTime, Dates.DEFAULT_DATETIME_FORMAT);
                    Calendar c0 = Calendar.getInstance();
                    // 发送报盘前15分钟
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(debitDate);
                    c1.set(Calendar.MINUTE, c1.get(Calendar.MINUTE) - 15);
                    // 发送报盘后15分钟
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(debitDate);
                    c2.set(Calendar.MINUTE, c2.get(Calendar.MINUTE) + 15);
                    // 当前时间介于发送报盘前后15分钟，则禁止实时划扣
                    if (c0.getTime().compareTo(c1.getTime()) >= 0 && c0.getTime().compareTo(c2.getTime()) <= 0) {
                        return "每天自动报盘前后15分钟时间不允许进行实时划扣操作！";
                    }
                }
            }
        } catch (Exception e) {
            return "实时划扣校验异常！";
        }
        return null;
    }
    
	/**
	 * 获取一天最大划扣次数
	 * @return
	 */
	private int getMaxCount() {
		int maxcount = 3;
		String maxcounts = sysParamDefineService.getSysParamValue("offer", "realTimeDeductCount");
		if(Strings.isNotEmpty(maxcounts)){
			try {
				maxcount = Integer.parseInt(maxcounts);
			} catch (Exception e) {
				maxcount = 3;
				logger.error("获取最大实时划扣次数转换出错！默认3次");
			}
		}
		return maxcount;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void createOfferInfoBySpecialRepay(Long specialRepayId) {
		createOfferInfoBySpecialRepay(specialRepayId,1);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void createOfferInfoBySpecialRepay(Long specialRepayId,int isThrow) {
		LoanSpecialRepayment loanSpecialRepayment = loanSpecialRepaymentService.findById(specialRepayId);
		
		if(loanSpecialRepayment.getRequestDate().compareTo(Dates.getCurrDate())==0){
			VLoanInfo loan = vLoanInfoService.findByLoanId(loanSpecialRepayment.getLoanId());
			
			//查询该记录是否有未回盘情况
			if(offerInfoService.checkHasOffer(loan)){
				logger.warn("create有未回盘记录，设置标记，在回盘后新建报盘文件！loan_id="+loan.getId());
				setOfferInfoSpecial(loan);
				return;
			}
			//自动关闭原来的报盘文件
			closeOfferInfo(loan);
			
			String specialRepaymentType = loanSpecialRepayment.getSpecialRepaymentType();
			//此处type设置优先级
			int type = 0;
			if(SpecialRepaymentTypeEnum.一次性还款.getValue().equals(specialRepaymentType)){
				type = IOfferInfoService.TIQIANJIEQING;
			}else if(SpecialRepaymentTypeEnum.提前扣款.getValue().equals(specialRepaymentType)){
				type = IOfferInfoService.TIQIANKOUKUAN;
			}else if(SpecialRepaymentTypeEnum.减免.getValue().equals(specialRepaymentType)){
				type = IOfferInfoService.ZHENGCHANG;//减免的话当正常扣款优先级处理
			}else if(SpecialRepaymentTypeEnum.正常费用减免.getValue().equals(specialRepaymentType)){
				type = IOfferInfoService.ZHENGCHANG;//减免的话当正常扣款优先级处理
			}
			
			//创建报盘信息
			ComOrganization organization = comOrganizationDao.get(loan.getSalesDepartmentId());
			OfferInfo offer = offerInfoService.createRealtimeOffer(loan, organization, type,null);//5：实时划扣
			if(offer==null){
				logger.warn("因该债权报盘金额为0，自动报盘文件未创建！loan_id="+loan.getId());
				if(isThrow==1){
					throw new PlatformException(ResponseEnum.CREATEOFFERINFO_AMOUNT_ZERO,"因该债权报盘金额为0，报盘文件未创建！loan_id="+loan.getId());
				}
			}
		}
		
		
	}

	/**
	 * 设置自动报盘有特殊还款创建或取消，在下次回盘后需要从新建报盘文件
	 * @param loan
	 */
	private void setOfferInfoSpecial(VLoanInfo loan) {
		//状态
		String[] states = {OfferStateEnum.未报盘.getValue(),OfferStateEnum.已回盘非全额.getValue(),OfferStateEnum.已报盘.getValue()};
		List<OfferInfo> list = offerInfoService.findOfferByOfferDateAndStates(loan.getId(),new Date(), states);
		
		if(list !=null && list.size()>0){
			for(OfferInfo offerInfo : list){
				offerInfo.setUpdateTime(new Date());
				offerInfo.setUpdator("system");
				offerInfo.setSpt1(IOfferInfoService.MOMO_REBUILD);
				offerInfoService.updateOffer(offerInfo);
			}
		}
	}
	
	/**
	 * 关闭这个loan的当日自动报盘
	 * @param loan
	 */
	public void closeOfferInfo(VLoanInfo loan) {
		String[] states = {OfferStateEnum.未报盘.getValue()};
		List<OfferInfo> list = offerInfoService.findOfferByOfferDateAndStates(loan.getId(),new Date(), states);
		
		if(list !=null && list.size()>0){
			for(OfferInfo offerInfo : list){
				offerInfo.setState(OfferStateEnum.已关闭.getValue());
				offerInfo.setUpdateTime(new Date());
				offerInfo.setUpdator("system");
				offerInfo.setMemo("因创建新报盘文件，该报盘关闭");
				offerInfoService.updateOffer(offerInfo);
			}
		}
	}
	
	/**
	 * 关闭这个loan的当日自动报盘  第三方机构
	 * @param loan
	 */
	public void closeDebitInfoThird(VLoanInfo loan) {
		String[] states = {OfferStateEnum.未报盘.getValue()};
		List<DebitBaseInfo> list = debitBaseInfoDaoImpl.findOfferByOfferDateAndStates(loan.getId(),new Date(), states);
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		for(DebitBaseInfo debitBaseInfo : list){
			debitBaseInfo.setState(OfferStateEnum.已关闭.getValue());
			debitBaseInfo.setMemo("因创建新报盘文件，该报盘关闭");
			debitBaseInfoDaoImpl.update(debitBaseInfo);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void revokeOfferInfoBySpecialRepay(Long specialRepayId) {
		LoanSpecialRepayment loanSpecialRepayment = loanSpecialRepaymentService.findById(specialRepayId);
		
		if(loanSpecialRepayment.getRequestDate().compareTo(Dates.getCurrDate())<=0){
			VLoanInfo loan = vLoanInfoService.findByLoanId(loanSpecialRepayment.getLoanId());
			
			//查询该记录是否有未回盘情况
			if(offerInfoService.checkHasOffer(loan)){
				logger.warn("revoke有未回盘记录，设置标记，在回盘后新建报盘文件！loan_id="+loan.getId());
				setOfferInfoSpecial(loan);
				return;
			}
			//自动关闭原来的报盘文件
			closeOfferInfo(loan);
			
			ComOrganization organization = comOrganizationDao.get(loan.getSalesDepartmentId());
			//恢复自动报盘信息
			OfferInfo offer = offerInfoService.createRealtimeOffer(loan, organization, IOfferInfoService.YUQI,null);//优先级按照逾期的来
			if(offer==null){
				logger.warn("因该债权报盘金额为0，自动报盘文件未恢复！loan_id="+loan.getId());
//				throw new PlatformException("因该债权报盘金额为0，自动报盘文件未恢复！loan_id="+loan.getId());
			}
		}
	}
	
    /**
     * 创建和发送TPP报盘（非外贸3）
     * @param paramsVo
     * @param loan
     * @param organization
     * @return
     */
    private Map<String, Object> creatAndSendOffer(OfferParamsVo paramsVo,VLoanInfo loan, ComOrganization organization) {
        Map<String, Object> json = new HashMap<String, Object>();
        //先关闭该债权其余的报盘文件
        closeOfferInfo(loan);
        //创建实时扣款未报盘信息
        OfferInfo offer=offerInfoService.createOffer(loan, organization, IOfferInfoService.SHISHIHUAKOU,paramsVo.getOfferAmount(),paramsVo.getPaySysNo());//5：实时划扣
        //发送报盘
        List<OfferInfo> offerList=new ArrayList<OfferInfo>();
        offerList.add(offer);
        //获得当前报盘使用的tpp系统版本号
        String ver = sysParamDefineService.getSysParamValue("codeHelper", "realtime_offer_tpp_ver");
        try {
            if(TPPHelper.TPP_VER20.equals(ver)){
                logger.info("系统配置发送至tpp2.0接口============");
                offerInfoService.sendOfferToTpp2(offer);
            }else{
                logger.info("系统配置发送至tpp1.0接口============");
                offerInfoService.sendOfferToTpp1(offer);
            }
            json=MessageUtil.returnHandleSuccessMessage();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            json=MessageUtil.returnErrorMessage(e.getMessage());
        }
        return json;
    }
    

    /**
     * 创建和发送第三方报盘信息至第三方
     * @param paramsVo
     * @param loan
     * @param organization
     * @return
     */
    private Map<String, Object> createAndSendDebitOffer(OfferParamsVo paramsVo, VLoanInfo loan, ComOrganization organization) {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            // 先关闭该债权其余的报盘文件
            closeDebitInfoThird(loan);
            // 创建实时扣款未报盘信息
            DebitBaseInfo debitBaseInfo = offerInfoService.createDebitOffer(loan, organization, IOfferInfoService.SHISHIHUAKOU,
                    paramsVo.getOfferAmount(), paramsVo.getPaySysNo());
            if(null == debitBaseInfo){
                return MessageUtil.returnErrorMessage("创建实时划扣报盘失败！");
            }
            List<DebitBaseInfo> debitBaseInfoList = new ArrayList<DebitBaseInfo>();
            debitBaseInfoList.add(debitBaseInfo);
            offerInfoService.sendOffer(debitBaseInfoList);
            json = MessageUtil.returnHandleSuccessMessage();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            json = MessageUtil.returnErrorMessage(e.getMessage());
        }
        return json;
    }
    
    /**
     * 实时划扣校验
     * @param paramsVo
     * @param loan
     * @param isAdvanceRepayment
     * @param isOneTimeRepayment
     * @return
     */
    private String validateRealtimeOfferInfo(OfferParamsVo paramsVo, VLoanInfo loan, boolean isAdvanceRepayment, boolean isOneTimeRepayment) {
        boolean flag = checkNeedCreateOffer(loan);
        if (!flag) {
            return "对应的借款今日无需扣款，请检查是否已申请特殊还款！";
        }
        Long loanId = loan.getId();
        // 针对龙信小贷、外贸信托、外贸2，判断是否已经完成划扣服务费，如果没有，则停止后续实时划扣操作
        if (!loanFeeInfoService.isAlreadyDebitServiceCharge(loanId)) {
            return "该笔借款没有完成划扣服务费，无法进行实时划扣操作！";
        }
        
        // 针对债权去向为包银的，且一次性结清已生效，划扣金额必须是一次性结清金额 update by liuyh [2017-03-17]
        BigDecimal totalAmt = afterLoanService.getAmount(Dates.getCurrDate(), loan.getId());
        if (FundsSourcesTypeEnum.包商银行.getValue().equals(loan.getLoanBelong())
                && isOneTimeRepayment
                && paramsVo.getOfferAmount().compareTo(totalAmt) != 0) {
            return "债权去向为包银的，且一次性结清已生效，划扣金额必须是一次性结清金额！";
        }
        // 针对债权去向为包银的，债权状态为正常的，且不是提前结清的状态，不能做实时划扣 update by liuyh [2017-03-17]
        if (FundsSourcesTypeEnum.包商银行.getValue().equals(loan.getLoanBelong())
                && !isOneTimeRepayment
                && LoanStateEnum.正常.name().equals(loan.getLoanState())) {
            return "债权去向为包银的，债权状态为正常的，不能做实时划扣！";
        }
        
        List<LoanRepaymentDetail> list = afterLoanService.getAllInterestOrLoan(Dates.getCurrDate(), loan.getId());// 所有未还款的记录
        BigDecimal fine = afterLoanService.getFine(list, Dates.getCurrDate());// 得到罚息
		BigDecimal dificit = afterLoanService.getCurrAmount(list,Dates.getCurrDate()); //当期应还
        BigDecimal overdueAmount = afterLoanService.getOverdueAmount(list, Dates.getCurrDate());// 得到逾期总金额
        if (FundsSourcesTypeEnum.陆金所.getValue().equals(loan.getLoanBelong())) {
			if(LoanStateEnum.正常.name().equals(loan.getLoanState())){
				if(isOneTimeRepayment){
					// 债权状态正常、申请了提前结清，不能选择陆金所划扣通道
					if(paramsVo.getPaySysNo().equals(TppRealPaySysNoEnum.陆金所.getCode())){
						return "债权去向为陆金所,申请提前结清的债权，划扣通道不能选择陆金所";
					}
				} else {
					// 债权状态正常、没有申请了提前结清，必须选择陆金所划扣通道
					if(!paramsVo.getPaySysNo().equals(TppRealPaySysNoEnum.陆金所.getCode())){
						return "债权去向为陆金所,没有申请提前结清的债权，划扣通道必须选择陆金所";
					}
					// 债权状态正常、没有申请了提前结清，划扣金额不能大于当期应还金额
					if(paramsVo.getOfferAmount().compareTo(dificit) > 0){
						return "债权去向为陆金所的，债权状态为正常的债权，非一次性还款金额不得大于当期应还";
					}
				}
			}

			if(LoanStateEnum.逾期.name().equals(loan.getLoanState())){
				// 债权状态逾期，划扣通道不能选择陆金所划扣
				if(paramsVo.getPaySysNo().equals(TppRealPaySysNoEnum.陆金所.getCode())){
					return "债权去向为陆金所,债权状态为逾期的债权，划扣通道不能选择陆金所";
				}
				// 债权状态逾期且没有申请一次性结清、划扣金额不能大于逾期应还总额
				if(!isOneTimeRepayment && paramsVo.getOfferAmount().compareTo(overdueAmount.add(fine)) > 0){
					return "债权去向为陆金所且没有申请提前结清的债权，还款金额不能大于逾期总和！";
				}
			}

			// 实时划扣 如果债权去向 属于陆金所 且一次性结清已生效，划扣金额必须是一次性结清金额
			if (isOneTimeRepayment && paramsVo.getOfferAmount().compareTo(totalAmt) != 0) {
				return "债权去向为陆金所且一次性结清已生效的债权，划扣金额必须是一次性结清金额！";
			}
			
			// 如果未消费repay_result_notify_log（存在未入账分账的金额，没有发送给陆金所分账信息），则提示 。
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("loanId", loanId);
			param.put("state", "0");
			param.put("deductState", "t");
			List<RepayResultNotifyLog> logList = repayResultNotifyLogService.findListByMap(param);
			if(CollectionUtils.isNotEmpty(logList)){
				return "债权去向为陆金所，存在未入账分账的信息，暂时不能进行实时划扣！";
			}
        }
        if(FundsSourcesTypeEnum.华澳信托.getValue().equals(loan.getFundsSources()) 
        		&& FundsSourcesTypeEnum.证大P2P.getValue().equals(loan.getLoanBelong())
        		&& TppPaySysNoEnum.通联代扣.getCode().equals(paramsVo.getPaySysNo())){
        	return "当前债权划扣通道不支持通联代扣，请选择其他通道。";
        }
        
        return null;
    }

    /**
     * 如果划扣通道不显示，重新配置划扣通道
     * @param paramsVo
     * @param loan
     * @param isOneTimeRepayment
     */
	private void againSetPaySysNo(OfferParamsVo paramsVo, VLoanInfo loan, boolean isOneTimeRepayment) {
		if(!paramsVo.isShowPayChannel()){
        	//债权去向为陆金所，设置特殊的划扣通道
        	if (FundsSourcesTypeEnum.陆金所.getValue().equals(loan.getLoanBelong())) {
        		if(LoanStateEnum.正常.name().equals(loan.getLoanState())){
        			if(isOneTimeRepayment){
        				paramsVo.setPaySysNo("999");
        			} else {
        				paramsVo.setPaySysNo(TppPaySysNoEnum.陆金所.getCode());
        			}
        		}
        		if(LoanStateEnum.逾期.name().equals(loan.getLoanState())){
        			paramsVo.setPaySysNo("999");
        		}
        	}else if(FundsSourcesTypeEnum.外贸3.getValue().equals(loan.getLoanBelong())){
        		paramsVo.setPaySysNo(TppPaySysNoEnum.外贸3.getCode());
        	}else{
        		//如果划扣通道为空 则设置为 999 走支付路由
        		paramsVo.setPaySysNo("999");
        	}
        }
	}
}
