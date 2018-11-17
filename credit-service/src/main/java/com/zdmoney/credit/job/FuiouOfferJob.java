package com.zdmoney.credit.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.offer.dao.pub.IFuiouOfferJobEntryDao;
import com.zdmoney.credit.offer.domain.FuiouOfferJobEntry;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zendaimoney.thirdpp.rmi.service.request.ThirdPaymentService;
import com.zendaimoney.thirdpp.vo.RequestVo;
import com.zendaimoney.thirdpp.vo.biz.BizTransferDocumentVo;
import com.zendaimoney.thirdpp.vo.enums.IsAsyn;
import com.zendaimoney.thirdpp.vo.enums.IsReSend;
import com.zendaimoney.thirdpp.vo.enums.RequestType;
import com.zendaimoney.thirdpp.vo.enums.ThirdPartyType;
import com.zendaimoney.thirdpp.vo.enums.ZendaiSys;
import com.zendaimoney.thirdpp.vo.expand.TPPEnum;

/**
 * 针对富有客户发起实名验证
 * @author 00236632
 *
 */
@Service
public class FuiouOfferJob {
	private static final Logger logger = Logger.getLogger(FuiouOfferJob.class);

	@Autowired
	private ISysParamDefineService sysParamDefineService;

	@Autowired
	private IFuiouOfferJobEntryDao dao;
	
	@Autowired
	private ILoanLogService loanLogService;
	
	/** 暂时取消调用 **/
	@Autowired(required = false)
	private ThirdPaymentService paymentService;
	
	public void fuiouOffer() {
		loanLogService.createLog("FuiouOfferJob", "info", "FuiouOfferJob开始........", "SYSTEM");
    	String isFuiouOffer = sysParamDefineService.getSysParamValue("sysJob", "isFuiouOffer");
		if(!Const.isClosing.equals(isFuiouOffer)){
			String dateStr = Dates.getDateTime(Dates.getCurrDate(), "yyyy-MM-dd");
			
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			
			List<FuiouOfferJobEntry> results = new ArrayList<FuiouOfferJobEntry>();
			
			if (hour == 22) {
				results = dao.getFuiouOffer22(dateStr);
			} else if (hour == 10 || hour == 13 || hour == 16 || hour == 20){
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("dateStr", dateStr);
				params.put("hour", hour);
				results = dao.getFuiouOffer(params);
			} else {
				logger.info("FuiouOfferJob结束........");
				return;
			}
			
			for (FuiouOfferJobEntry entry : results) {
				RequestVo requestVo= new RequestVo();
	            requestVo.setAsynTag(IsAsyn.IS_ASYN_TAG_0);// 0 非异步 不需要回调 1 异步 需要回调
	            requestVo.setReSendTag(IsReSend.IS_RESEND_TAG_0);// 不需要重发
	            requestVo.setRequestType(RequestType.SIGN_AGREEMENT);// 请求类型
	            requestVo.setRequestOperator("admin");// 操作员
	            requestVo.setRequestSystem(ZendaiSys.ZENDAI_2004_SYS);// 业务系统
	            requestVo.setThirdPartyType(ThirdPartyType.Fuiou);// 第三方
	            requestVo.setRequestDate(new Date());
	            Map<TPPEnum, String> expandProperties = new LinkedHashMap<TPPEnum, String>(); //预留扩张字段
	            BizTransferDocumentVo document = new BizTransferDocumentVo();
	            document.setBusType("AC01"); //代收代付类型  代收 AC01  ；代付AP01
	            document.setBankCode("00080002"); //银行行号
	            document.setUserName(entry.getName()); //姓名
	            document.setMobile(entry.getMphone()); //手机号
	            document.setBankType("0"); //账户类型 0银行卡  1 存折
	            document.setCardNo(entry.getIdNum()); //证件号码
	            document.setBankNo(entry.getAccount()); //银行卡号
	            document.setCardType(entry.getIdType()); //证件类型 0：身份证 1:护照2:军官证3:士兵证5:户口本7：其他
	            document.setIsCallback("0"); //回拨标志 0不回拨 ， 1回拨
	            document.setReserved("实名验证"); //备注
	            document.setExpandProperties(expandProperties);
	            requestVo.getRequestDetailInfo().getRequestDetails().add(document);//多条装多次，最多装10条
	            RequestVo returnRequestVo = null;
//	            RequestVo returnRequestVo = paymentService.signagreeement(requestVo);
	            if (true) {
	        		throw new PlatformException(ResponseEnum.FULL_MSG,"为了适应接口切换，取消signagreeement接口调用(RMI接口)");
	        	}
	            String returncode = returnRequestVo.getMemo(); //获取返回码 000000   表示成功，否则表示失败 （一条失败则全部信息皆录入失败）
	            String message = returnRequestVo.getMessage(); //返回描述信息（）
	            
	            loanLogService.createLog("FuiouOfferJob", "info", returncode+"  "+message, "SYSTEM");
			}
		}else{
			loanLogService.createLog("FuiouOfferJob", "info", "定时开关isFuiouOffer关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isFuiouOffer关闭，此次不执行");
		}
		loanLogService.createLog("FuiouOfferJob", "info", "FuiouOfferJob结束........", "SYSTEM");
		logger.info("FuiouOfferJob结束........");
		
		
	}

}
