package com.zdmoney.credit.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.lufax.LufaxRepayInterfaceEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.LufaxUtil;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.vo.lufax.entity.LoanDetailLufax;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100016Vo;
import com.zdmoney.credit.ljs.service.pub.ILoanStatusLufaxService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * @author 10098  2017年5月11日 下午5:28:40
 */
@Service
public class TransmitLoanData2LufaxJob {
	
    private Logger logger = LoggerFactory.getLogger(TrustOfferFlowAccountingJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanStatusLufaxService loanStatusLufaxService;
	public void execute() {
    	logger.info("开始执行同步借据信息给陆金所JOB");
    	String isTransmitLoanData2Lufax = sysParamDefineService.getSysParamValue("sysJob", "isTransmitLoanData2Lufax");
		if(Strings.isEmpty(isTransmitLoanData2Lufax) || Const.isClosing.equals(isTransmitLoanData2Lufax)){
			logger.info("isTransmitLoanData2Lufax执行关闭！");
            return ;
		}
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("currDate", Dates.getDateTime());
			List<LoanDetailLufax> list = loanStatusLufaxService.findLoanDetailLufaxList2Transmit(params);
			Lufax100016Vo lufax100016Vo = new Lufax100016Vo();
			lufax100016Vo.setDetail(list);
			lufax100016Vo.setLine_sum(String.valueOf(list.size()));
			lufax100016Vo.setInterface_id(LufaxRepayInterfaceEnum.证大借据信息给核算.getCode());
			lufax100016Vo.setLufax_batch_id(LufaxUtil.createAnshuoBatchId());
			lufax100016Vo.setSign(LufaxUtil.createInterfaceReqId(lufax100016Vo.getInterface_id()));
			JSONObject jsonResult = GatewayUtils.callCateWayInterface(lufax100016Vo, GatewayFuncIdEnum.证大同步借据信息给交易核算接口.getCode());
			logger.info("【陆金所】证大同步借据信息给交易核算接口,返回数据："+jsonResult);
			String ret_code = (String)jsonResult.get("ret_code");
			String ret_msg = (String)jsonResult.get("ret_msg");
			if("0000".equals(ret_code)){
				// 同步借据信息后记录操作日志
				loanStatusLufaxService.createOperateLog(list);
				logger.info("【陆金所】证大同步借据信息给交易核算接口成功:"+ret_msg);
			}else{
				logger.info("【陆金所】证大同步借据信息给交易核算接口失败:"+ret_msg);
			}
		}catch(Exception e){
			logger.error("同步借据信息给陆金所JOB异常", e);
		}
	}
}
