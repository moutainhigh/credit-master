package com.zdmoney.credit.job;


import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.trustOffer.service.pub.IBhTrustCommonService;

/**
 * 渤海需求相关定时任务处理类
 * 后续渤海新增的定时任务都加入到这个类中
 * @author YM10112 2017年12月26日 下午3:20:01
 */
 
@Service
public class BHBusinessJob {
    private static final Logger logger = Logger.getLogger(BHBusinessJob.class);
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private IBhTrustCommonService bhTrustCommonService;
    
    /**
     * 上传还款状态文件到渤海FTP服务器
     * 触发频率：每个月1号3点30
    */
    public void uploadRepayStateDetail(){
        logger.info("上传还款状态文件到渤海FTP服务器定时任务执行开始...");
        String isExecute = sysParamDefineService.getSysParamValue("sysJob", "uploadRepaymentState");
        if(!Const.isClosing.equals(isExecute)) {
        	try{
        		Map<String, Object> json = bhTrustCommonService.createRepayStateDetail(null);
        		if(Constants.SUCCESS_CODE.equals(json.get("code"))){
        			logger.info("上传还款状态文件到渤海FTP服务器定时任务执行结束...上传成功！");
        		} 
        	}catch(Exception e){
        		logger.info("上传还款状态文件到渤海FTP服务器:失败！"+e.getMessage());
        		e.printStackTrace();
        	}
        }
    }
}
