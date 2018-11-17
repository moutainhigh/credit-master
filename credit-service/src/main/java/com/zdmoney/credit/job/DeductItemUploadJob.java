package com.zdmoney.credit.job;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.GatewayConstant;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.vo.RequestInfo;
import com.zdmoney.credit.framework.vo.wm3.entity.DeductItemBaseEntity;
import com.zdmoney.credit.framework.vo.wm3.entity.DeductItemFeeEntity;
import com.zdmoney.credit.framework.vo.wm3.entity.DeductItemPayOverEntity;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2301Vo;

@Service
public class DeductItemUploadJob {
	private static final Logger logger = Logger.getLogger(DeductItemUploadJob.class);
	//@Value("${gateway.interface.url}")
	public String gatewayInterfaceUrl = "http://172.16.5.182:8080/creditGateway-web/preRequest";
	
	public void deductItemUploadExecute(){
		WM3_2301Vo vo = new WM3_2301Vo();
		vo.setBrNo("005");
		vo.setBatNo("");
		vo.setDataCnt((long)1);
		
		DeductItemBaseEntity base = new DeductItemBaseEntity(); 
		base.setAcNo("");
		base.setPactNo("");
		base.setSerialNo("");
		base.setPayOver(new BigDecimal(1));
		base.setFeeRec(new BigDecimal(1));
		base.setRepayTotal(new BigDecimal(1));
		base.setRepayAmt(new BigDecimal(1));
		base.setRepayInte(new BigDecimal(1));
		base.setRepayOver(new BigDecimal(1));
		base.setFeeTotal(new BigDecimal(1));
		base.setmFeeTotal(new BigDecimal(1));
		base.setAcNo("");
		base.setCardChn("");
		
		DeductItemFeeEntity fee = new DeductItemFeeEntity();
		fee.setCnt((long)1);
		fee.setFeeType("");
		fee.setFeeKind("");
		fee.setFeeAmt(new BigDecimal(2));
		
		DeductItemPayOverEntity pay = new DeductItemPayOverEntity();
		pay.setCnt((long)1);
		pay.setTxPayOver(new BigDecimal(2));
		
		List<DeductItemBaseEntity> baseList  = new ArrayList<DeductItemBaseEntity>();
		List<DeductItemFeeEntity> feeList  = new ArrayList<DeductItemFeeEntity>();
		List<DeductItemPayOverEntity> payList  = new ArrayList<DeductItemPayOverEntity>();
		
		vo.setList(baseList);
		vo.setListFee(feeList);
		vo.setListPayOver(payList);
		
		RequestInfo requestInfoVo = null;
        String rsultStr = "";
        String url = gatewayInterfaceUrl;
        try{
        	requestInfoVo = GatewayUtils.getSendGatewayRequestVo(vo, GatewayFuncIdEnum.外贸3扣款信息上传);
            logger.info("【外贸3】扣款信息上传接口url:"+url+",参数："+JSONUtil.toJSON(requestInfoVo));
            rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
            logger.info("【外贸3】扣款信息上传接口url:"+url+",响应："+JSONUtil.toJSON(rsultStr));
        }catch (Exception e){
        	e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"【外贸3】扣款信息上传接口异常");
        }
        JSONObject jsonObject = JSON.parseObject(rsultStr);
        if (GatewayConstant.RESPONSE_SUCCESS.equals(jsonObject.get("resCode").toString())) {
            JSONObject grantResut = jsonObject.getJSONObject("infos");
            String respCode = (String)grantResut.get("respCode");//外贸3返回的结果
            String respDesc = (String)grantResut.get("respDesc");
            if("0000".equals(respCode)){
            	logger.info("调用【外贸3】扣款信息上传接口成功，返回信息："+respDesc);
			}else{
				logger.info("调用【外贸3】扣款信息上传接口失败：原因："+respDesc);
	            throw new PlatformException(ResponseEnum.FULL_MSG,respDesc);
			}
        }else{
        	logger.info("【外贸3】扣款信息上传接口,调用网关返回的失败信息："+jsonObject.get("respDesc").toString());
        }
	}
}
