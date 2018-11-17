
package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
/**
 * tpp放款结果查询
 * @author YM10104
 *
 */
@Service
public class TppGrantResultJob {
	private static final Logger logger = Logger.getLogger(TppGrantResultJob.class);
	@Autowired
    private IFinanceGrantService financeGrantService;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	public void tppGrantResultQuery(){
		String isTppGrantResultQueryStat = sysParamDefineService.getSysParamValue("sysJob", "isTppGrantResultQueryStat");
		if(!Const.isClosing.equals(isTppGrantResultQueryStat)){
			logger.info("☆☆☆☆☆☆☆☆☆☆tppGrantResultQuery查询tpp放款结果开始☆☆☆☆☆☆☆☆☆☆");
			
			financeGrantService.tppGrantResultQuery();
			
			logger.info("☆☆☆☆☆☆☆☆☆☆tppGrantResultQuery查询tpp放款结果结束☆☆☆☆☆☆☆☆☆☆");			
		}else{
			logger.info("☆☆☆☆☆☆☆☆☆☆tppGrantResultQuery查询  未开启☆☆☆☆☆☆☆☆☆☆");
		}
	}

}
