package com.zdmoney.credit.job;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zdmoney.credit.common.constant.GatewayConstant;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import oracle.net.aso.e;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.RandomNumberUtils;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.vo.RequestInfo;
import com.zdmoney.credit.framework.vo.abs.entity.PaidInEntity;
import com.zdmoney.credit.framework.vo.abs.input.Abs100007Vo;
import com.zdmoney.credit.loan.dao.pub.IPaidInEntityDao;
import com.zdmoney.credit.loan.domain.PaidInEntityMemo;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 
 * @ClassName: PaidInEntityJob 
 * @Description: 对公还款定时任务
 * @author liyl 
 * @date 2016年11月23日 下午5:12:41
 *
 */
@SuppressWarnings("all")
@Service
public class PaidInEntityJob {
	
	private static final Logger logger = Logger.getLogger(PaidInEntityJob.class);
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private IPaidInEntityDao paidInEntityDao;
	
	public void execute() {
		
		String isPaidInEntity = sysParamDefineService.getSysParamValue("sysJob", "isPaidInEntity");
		if(!Const.isClosing.equals(isPaidInEntity)){
			logger.info("PaidInEntityJob开始........");
			loanLogService.createLog("PaidInEntityJob", "info", "PaidInEntityJob开始........", "SYSTEM");
			RequestInfo requestInfoVo = null;
			String rsultStr = "";
			try {
			    //每天5点查询前一天的数据
                List<PaidInEntity> list = this.paidInEntityDao.queryPaidEntityList();
                //System.out.println("list:"+list.toString());
                Abs100007Vo param = new Abs100007Vo();
                param.setSysSource("A02");
                String batNo = new SimpleDateFormat("yyyyMMdd").format(new Date())+ RandomNumberUtils.getfourRandomNumber();
                param.setBatNo(batNo);
                param.setProjNo(GatewayConstant.PROJ_NO);
                param.setBankNo("待定");
                if (list != null && list.size() > 0) {
                    param.setDataCnt(list.size());
                    BigDecimal totalAmt = new BigDecimal(0);
                    for (PaidInEntity paidInEntity : list) {
                        totalAmt = totalAmt.add(paidInEntity.getPayTotal());
                        PaidInEntityMemo pe = new PaidInEntityMemo();
                        pe.setBatNo(batNo);
                        String loanId = this.paidInEntityDao.selectLoanId(paidInEntity.getPactNo());
                        pe.setLoanId(loanId);
                        pe.setPactNo(paidInEntity.getPactNo());
                        pe.setCnt(paidInEntity.getCnt());
                        pe.setPayDate(paidInEntity.getPayDate());
                        pe.setPayTotal(paidInEntity.getPayTotal());
                        pe.setPayAmt(paidInEntity.getPayAmt());
                        pe.setPayInte(paidInEntity.getPayInte());
                        pe.setPayOver(paidInEntity.getPayOver());
                        pe.setFeeTotal(paidInEntity.getFeeTotal());
                        pe.setCreateTime(new Date());
                        //获取登录人员信息
                        User user = UserContext.getUser();
                        if (user == null ) {
                            pe.setCreator("admin");
                        }else {
                            pe.setCreator(user.getName());
                        }
                        pe.setUpdateTime(pe.getCreateTime());
                        pe.setUpdator(pe.getCreator());
                        //调用DAO向数据库插入数据
                        this.paidInEntityDao.insertRequestParam(pe);
                    }
                    param.setTotalAmt(totalAmt);
                    param.setListPaidIn(list);
                }
                
                //传输地址
                String url = sysParamDefineService.getSysParamValue("url", "gatewayInterfaceUrl");
//                String url ="http://10.8.30.48:8080/creditGateway-web/preRequest";
                if (Strings.isEmpty(url)) {
                    throw new PlatformException(ResponseEnum.FULL_MSG,"接口路径不存在");
                }
                try {
                    requestInfoVo = GatewayUtils.getSendGatewayRequestVo(param, GatewayFuncIdEnum.对公还款);
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                    throw new PlatformException(ResponseEnum.FULL_MSG,"生成调用网关的对公还款接口签名异常");
                }catch (Exception e){
                    e.printStackTrace();
                    throw new PlatformException(ResponseEnum.FULL_MSG,"生成调用网关的对公还款接口的Vo异常");
                }
                try{
                logger.info("请求网关--提前清贷申请接口url:"+url+"; 参数："+JSONUtil.toJSON(requestInfoVo));
                rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
//                rsultStr = "{'infos':{'dataCnt':0,'status':'SUCCESS','msg':'对公还款开始结束','listErr':[{'pactNo':'123','url':'4','dealDesc':'567'}]},'resCode': '0000',    'respDesc': '操作成功'}";
                rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
                logger.info("请求网关--提前清贷申请接口url:"+url+"; 响应："+rsultStr);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new PlatformException(ResponseEnum.FULL_MSG,"调用网关对公还款接口异常");
                }

                JSONObject jsonObject = JSON.parseObject(rsultStr);
                if ("0000".equals(jsonObject.get("resCode").toString())) {
                    JSONObject grantResut = jsonObject.getJSONObject("infos");
                    if ("SUCCESS".equals(grantResut.get("status"))) {
                        logger.info("请求网关--响应状态:"+grantResut.getString("status")+"; 响应内容："+grantResut.getString("msg"));
                        logger.info("受理记录数:"+grantResut.getString("dataCnt"));
                        try {
                            String listErr = grantResut.getString("listErr");
                            if (listErr != null && listErr.length()> 0) {
                                logger.info("失败的记录:"+listErr);
                            }
                        } catch (RuntimeException e) {
                            logger.info("无失败记录");
                        }
                        
                    }else {
                        logger.error("请求网关--响应状态:"+grantResut.getString("status")+"响应内容："+grantResut.getString("msg"));
                    }
                }else {
                    logger.error("请求网关--响应错误！响应码:"+jsonObject.get("resCode").toString()+" 响应内容："+jsonObject.get("msg").toString());
                }
                
			} catch (Exception e) {
				logger.error(e.getMessage());
				int length = e.getMessage().length();
				loanLogService.createLog("PaidInEntityJob", "error", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
			}
			logger.info("PaidInEntityJob结束........");
			loanLogService.createLog("PaidInEntityJob", "info", "PaidInEntityJob结束........", "SYSTEM");
		}else{
			loanLogService.createLog("PaidInEntityJob", "info", "定时开关isPaidInEntity关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isPaidInEntity关闭，此次不执行");
		}
	}

}
