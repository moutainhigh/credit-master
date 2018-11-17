package com.zdmoney.credit.api.app.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.bsyh.dao.pub.IRepayBusLogDao;
import com.zdmoney.credit.bsyh.domain.RepayBusLog;
import com.zdmoney.credit.bsyh.service.pub.IRepayResultNotifyLogService;
import com.zdmoney.credit.common.constant.DealStateEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.LoanStatueLufaxEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.constant.grant.FinanceGrantEnum;
import com.zdmoney.credit.common.constant.lufax.LufaxConst;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.tpp.AccountTradeTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.MD5Util;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.RequestInfo;
import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.common.vo.core.RepayInfoLufaxVO;
import com.zdmoney.credit.core.service.pub.IFinanceCoreService;
import com.zdmoney.credit.core.service.pub.ILoanCoreService;
import com.zdmoney.credit.debit.service.pub.IDebitBaseInfoService;
import com.zdmoney.credit.debit.service.pub.IDebitQueueLogService;
import com.zdmoney.credit.debit.service.pub.IDebitTransactionService;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100011Vo;
import com.zdmoney.credit.framework.vo.lufax.output.Lufax100018OutputVo;
import com.zdmoney.credit.framework.vo.xdcore.entity.ButBackEntity;
import com.zdmoney.credit.framework.vo.xdcore.entity.DealResEntity;
import com.zdmoney.credit.framework.vo.xdcore.entity.LendResEntity;
import com.zdmoney.credit.framework.vo.xdcore.entity.PayPlanListEntity;
import com.zdmoney.credit.framework.vo.xdcore.entity.ResponseLendEntity;
import com.zdmoney.credit.framework.vo.xdcore.input.BYXY0010Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.BYXY0016Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.BYXY0018Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.Lufax800001Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.Lufax800011Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.Lufax800013Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.Lufax800020Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.Lufax800020Vo.DebitResultLufax;
import com.zdmoney.credit.framework.vo.xdcore.input.Lufax820050Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.Xdcore100001Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.Xdcore100004Vo;
import com.zdmoney.credit.framework.vo.xdcore.output.Xdcore100001OutputVo;
import com.zdmoney.credit.framework.vo.xdcore.output.Xdcore100004OutputVo;
import com.zdmoney.credit.job.TransmitLoanData2LufaxJob;
import com.zdmoney.credit.ljs.service.pub.ILoanStatusLufaxService;
import com.zdmoney.credit.ljs.service.pub.IPublicAccountDetailService;
import com.zdmoney.credit.loan.dao.pub.ILoanBsbMappingDao;
import com.zdmoney.credit.loan.domain.LoanBack;
import com.zdmoney.credit.loan.domain.LoanBsbMapping;
import com.zdmoney.credit.loan.domain.RepayResultNotifyLog;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanBackService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseGrantService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * Created by ym10094 on 2016/11/9.
 */
@Controller
@RequestMapping("/gateway")
public class GatewayController{

    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);
    public static final String bsPdfStoreDir = "/S1_PDF_SIGN";//包商银行存放pdf目录
    
    @Autowired
    private ILoanSpecialRepaymentService loanSpecialRepaymentService;
    
    @Autowired
    private ILoanBaseService loanBaseService;
    
    @Autowired
    private ILoanBackService loanBackService;
    
    @Autowired
    private IFinanceGrantService financeGrantService;
    
    @Autowired
    private ILoanBsbMappingDao loanBsbMappingDao;
    @Autowired
    private IVLoanInfoService vLoanInfoService;
    @Autowired
   	IRepayBusLogDao  repayBusLogDao;
    @Autowired
	IAfterLoanService afterLoanService;
    @Autowired 
	private ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;
    @Autowired
    private IRepayResultNotifyLogService repayResultNotifyLogService;
    @Autowired
    private ISequencesService sequencesService;
    @Value("${gateway.interface.url}")
    public String gatewayInterfaceUrl;
    @Autowired
    IDebitBaseInfoService debitBaseInfoService;
    @Autowired
    IDebitTransactionService debitTransactionService;
    @Autowired
    IDebitQueueLogService debitQueueLogService;
    @Autowired
    ISplitQueueLogService splitQueueLogService;
    @Autowired
    IVLoanInfoService vInfoService;
	@Autowired
	IFinanceCoreService financeCoreService;
	
	@Autowired
	ILoanBaseGrantService loanBaseGrantService;
	@Autowired
    private IPublicAccountDetailService publicAccountDetailService;
	@Autowired
	private ILoanStatusLufaxService loanStatusLufaxService;
	
    @Autowired
    private IFinanceGrantService financeGrantServiceImpl;
    @Autowired
    private ILoanCoreService loanCoreService;
    
	/**
	 * 返回陆金所状态码
	 */
	public static final String RETURN_CODE = "return_code"; 
	/**
	 * 返回陆金所信息
	 */
	public static final String RETURN_MSG = "return_msg";
    /**
     * 财务放款结果处理
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/disposeFinanceGrantResult",method = RequestMethod.POST,consumes = "application/json")
    public String disposeFinanceGrantPushResult(HttpServletRequest request,HttpServletResponse response,@RequestBody Xdcore100001Vo xdcore100001Vo){
        AttachmentResponseInfo<Xdcore100001OutputVo> attachmentResponseInfo = null;
        log.info("来自IP：{}的请求参数{}",request.getRemoteHost(),JSONUtil.toJSON(xdcore100001Vo));
        Xdcore100001OutputVo outVo = new Xdcore100001OutputVo();
        // 放款结果列表信息
        List<LendResEntity> listLendRes = xdcore100001Vo.getListLendRes();
        if(CollectionUtils.isEmpty(listLendRes)){
            attachmentResponseInfo = new AttachmentResponseInfo<Xdcore100001OutputVo>(ResponseEnum.FULL_MSG.getCode(),"放款结果列表信息不能为空！");
            return JSONUtil.toJSON(attachmentResponseInfo);
        }
        List<ResponseLendEntity> financeGrantResponses = new ArrayList<ResponseLendEntity>();
        // 合同编号
        String pactNo = null;
        for (LendResEntity fgRequest: listLendRes) {
            ResponseLendEntity financeGrantResponse = new ResponseLendEntity();
            pactNo = fgRequest.getPactNo();
            try {
                if (Strings.isEmpty(pactNo)) {
                    throw new PlatformException(ResponseEnum.GATEWAY_FINANCEGRANT_FAIL,"请求参数【合同号】不能为空！");
                }
                if (Strings.isEmpty(fgRequest.getDealSts())) {
                    throw new PlatformException(ResponseEnum.GATEWAY_FINANCEGRANT_FAIL,"请求参数【处理结果】不能为空！");
                }
                //校验requstId 是否存在。
                if (!financeGrantService.isExitpactNo(pactNo)) {
                    throw new PlatformException(ResponseEnum.GATEWAY_FINANCEGRANT_FAIL,"合同号不存在或合同号不处在放款申请中！");
                }
                //校验通过进行业务逻辑处理
                financeGrantService.disposeFinanceGrantPushResultService(pactNo, fgRequest.getDealSts(),fgRequest.getDealDesc());
                this.setFinanceGrantResponse(financeGrantResponse,pactNo,ResponseEnum.GATEWAY_FINANCEGRANT_SUCC,null);
            } catch (PlatformException e) {
                this.setFinanceGrantResponse(financeGrantResponse,pactNo,ResponseEnum.GATEWAY_FINANCEGRANT_FAIL,e.getMessage());
            } catch (Exception e) {
                this.setFinanceGrantResponse(financeGrantResponse,pactNo,ResponseEnum.GATEWAY_FINANCEGRANT_FAIL,e.getMessage());
            }
            financeGrantResponses.add(financeGrantResponse);
        }
        outVo.setListDealRes(financeGrantResponses);
        attachmentResponseInfo = new AttachmentResponseInfo<Xdcore100001OutputVo>(ResponseEnum.SYS_SUCCESS);
        attachmentResponseInfo.setAttachment(outVo);
        log.info("来自IP：{}请求的响应{}", request.getRemoteHost(), JSONUtil.toJSON(attachmentResponseInfo));
        return JSONUtil.toJSON(attachmentResponseInfo);
    }

    private void setFinanceGrantResponse(ResponseLendEntity financeGrantResponse,String pactNo,ResponseEnum responseEnum,String msg){
        financeGrantResponse.setPactNo(pactNo);
        financeGrantResponse.setStatus(responseEnum.getCode());
        financeGrantResponse.setMsg(responseEnum.getDesc());
        if(Strings.isNotEmpty(msg)){
            financeGrantResponse.setMsg(msg);
        }
    }
    
    /**
     * 推送待回购信息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pushBackLoan", method = RequestMethod.POST, consumes = "application/json")
    public String pushBackLoan(HttpServletRequest request, HttpServletResponse response, @RequestBody Xdcore100004Vo vo) {
        AttachmentResponseInfo<Xdcore100004OutputVo> attachmentResponseInfo = new AttachmentResponseInfo<Xdcore100004OutputVo>();
        log.info("来自IP：{}的请求参数{}", request.getRemoteHost(), JSONUtil.toJSON(vo));
        // 签名校验
        // 入参校验
        List<ButBackEntity> listBuyBack = vo.getListBuyBack();
        if(CollectionUtils.isEmpty(listBuyBack)){
            attachmentResponseInfo = new AttachmentResponseInfo<Xdcore100004OutputVo>(ResponseEnum.FULL_MSG.getCode(),"待回购列表信息不能为空！");
            return JSONUtil.toJSON(attachmentResponseInfo);
        }
        List<DealResEntity> listResponseVo = new ArrayList<DealResEntity>();
        for (ButBackEntity butBack : listBuyBack) {
            log.info("来自IP：{}的请求参数遍历执行{}", request.getRemoteHost(),JSONUtil.toJSON(butBack));
            DealResEntity responseVo = new DealResEntity();
            LoanBack loanBack = new LoanBack();
            try {
                // 必要入参不全
                if (Strings.isEmpty(vo.getRepAmt())
                        || Strings.isEmpty(vo.getRepIntst())
                        || Strings.isEmpty(vo.getRepTotal())
                        || Strings.isEmpty(butBack.getPactNo())
                        || Strings.isEmpty(butBack.getCustName())
                        || Strings.isEmpty(butBack.getRepAmt())
                        || Strings.isEmpty(butBack.getRepIntst())) {
                    responseVo.setPactNo(butBack.getPactNo());
                    responseVo.setStatus(ResponseEnum.GATEWAY_LOANBACK_PUSH_GRANTI_FAIL.getCode());
                    responseVo.setMsg(ResponseEnum.GATEWAY_LOANBACK_PUSH_REQ_DEFECT.getDesc());
                    listResponseVo.add(responseVo);
                    log.info("来自IP：{}的请求参数缺失", request.getRemoteHost());
                    continue;
                }
                Long _loanId = loanBaseService.findLoanIdByContractNum(butBack.getPactNo());
                // 未找到指定债权信息！
                if (_loanId == null) {
                    responseVo.setPactNo(butBack.getPactNo());
                    responseVo.setStatus(ResponseEnum.GATEWAY_LOANBACK_PUSH_GRANTI_FAIL.getCode());
                    responseVo.setMsg(ResponseEnum.GATEWAY_LOANBACK_PUSH_LOAN_NOTFOUND.getDesc());
                    listResponseVo.add(responseVo);
                    log.info("未根据来自IP：{}的请求参数找到相应的债权信息",request.getRemoteHost());
                    continue;
                }
                // 插入数据
                loanBack.setId(_loanId);
                loanBack.setLoanId(_loanId);
                loanBack.setPactNo(butBack.getPactNo());
                loanBack.setCustName(butBack.getCustName());
                loanBack.setRepAmt(butBack.getRepAmt());
                loanBack.setRepIntst(butBack.getRepIntst());
                loanBack.setTxDate(new Date());
                if (butBack.getTxDate() != null) {
                    loanBack.setTxDate(Dates.parse(butBack.getTxDate(), "yyyymmdd"));
                }
                loanBack.setBbState(0);
                loanBack.setpState(0);
                LoanBack _loanBack = loanBackService.insert(loanBack);
                // 插入成功
                if (_loanBack != null) {
                    responseVo.setPactNo(loanBack.getPactNo());
                    responseVo.setStatus(ResponseEnum.GATEWAY_LOANBACK_PUSH_GRANTI_SUCC.getCode());
                    responseVo.setMsg(ResponseEnum.GATEWAY_LOANBACK_PUSH_GRANTI_SUCC.getDesc());
                    listResponseVo.add(responseVo);
                    log.info("来自IP：{}的请求参数保存成功", request.getRemoteHost());
                }
            } catch (PlatformException pe) {
                responseVo.setPactNo(butBack.getPactNo());
                responseVo.setStatus(ResponseEnum.GATEWAY_LOANBACK_PUSH_GRANTI_FAIL.getCode());
                responseVo.setMsg(pe.getMessage());
                listResponseVo.add(responseVo);
                log.info("来自IP：{}的请求产生平台异常：{}", request.getRemoteHost(),pe.getMessage());
            } catch (Exception e) {
                responseVo.setPactNo(butBack.getPactNo());
                responseVo.setStatus(ResponseEnum.GATEWAY_LOANBACK_PUSH_GRANTI_FAIL.getCode());
                responseVo.setMsg(e.getMessage());
                listResponseVo.add(responseVo);
                log.info("来自IP：{}的请求产生异常：{}", request.getRemoteHost(),e.getMessage());
            }
        }
        Xdcore100004OutputVo outVo = new Xdcore100004OutputVo();
        outVo.setListDealRes(listResponseVo);
        attachmentResponseInfo = new AttachmentResponseInfo<Xdcore100004OutputVo>(ResponseEnum.SYS_SUCCESS);
        attachmentResponseInfo.setAttachment(outVo);
        log.info("来自IP：{}的请求执行成功", request.getRemoteHost());
        
        log.info("来自IP：{}请求的响应{}", request.getRemoteHost(),JSONUtil.toJSON(attachmentResponseInfo));
        return JSONUtil.toJSON(attachmentResponseInfo);
    }
    
    /**
     * 包银财务放款结果处理
     * @param request
     * @param response
     * @param xdcore1000016Vo
     * @return
     */
     
    @SuppressWarnings("unused")
	@ResponseBody
    @RequestMapping(value = "/loanResultNotify",method = RequestMethod.POST,consumes = "application/json")
    public String loanResultNotify(HttpServletRequest request,HttpServletResponse response,@RequestBody BYXY0016Vo byxy0016Vo){
    	log.info("【包商银行】放款结果通知接口，来自IP：{}的请求参数{}", request.getRemoteHost(), JSONUtil.toJSON(byxy0016Vo));
    	Map<String, Object> json = null;
    	String respcd = byxy0016Vo.getRespcd();//结果码
    	String resptx = byxy0016Vo.getResptx();//结果信息
        String busNumber = byxy0016Vo.getBusNumber();//业务申请流水号
        String orderNo = byxy0016Vo.getOrderNo();//借据号
        String loanDate = byxy0016Vo.getLoanDate();//放款时间
        Integer totalCnt  = byxy0016Vo.getTotalCnt();//总期数
        
        if (Strings.isEmpty(busNumber)){
       	 json = new HashMap<String, Object>();
       	 json.put("resCode", ResponseEnum.SYS_FAILD.getCode());
			 json.put("resMsg", "请求参数【业务申请流水号】不能为空！");
            return  JSONUtil.toJSON(json);
        }
        if("0000".equals(respcd)){
       	 if (Strings.isEmpty(orderNo)){
           	 json = new HashMap<String, Object>();
           	 json.put("resCode", ResponseEnum.SYS_FAILD.getCode());
    			 json.put("resMsg", "请求参数【借据号】不能为空！");
    			 return  JSONUtil.toJSON(json);
            }
       	 List<PayPlanListEntity> payPlanList = byxy0016Vo.getPayPlanList();
            if(payPlanList == null || payPlanList.isEmpty() || payPlanList.size() == 0){
           	 json = new HashMap<String, Object>();
           	 json.put("resCode", ResponseEnum.SYS_FAILD.getCode());
    			 json.put("resMsg", "【包银】生成的还款计划为空！");
    			 return  JSONUtil.toJSON(json);
            }
        }
        
        LoanBsbMapping loanBsbMapping = loanBsbMappingDao.getByBusNumber(busNumber);
        //校验 busNumber 是否存在。
        if (loanBsbMapping == null){
       	 json = new HashMap<String, Object>();
       	 json.put("resCode", ResponseEnum.SYS_FAILD.getCode());
			 json.put("resMsg", "【业务申请流水号】不存在！");
			 return  JSONUtil.toJSON(json);
        }
		try {
			json = financeGrantService.disposeFinanceGrantResult(byxy0016Vo);
			Object resMsg = json.get("resMsg");
			if("000000".equals(json.get("resCode"))){
				try {
					json = searchAgreementInterface(loanBsbMapping.getLoanId(),busNumber,orderNo);
					json.put("resMsg", resMsg+","+json.get("resMsg")+"");
				} catch(Exception ex) {
					log.error(ex.getMessage(),ex);
				}
				json.put("resCode", ResponseEnum.SYS_SUCCESS.getCode());
				log.info("查询协议接口返回的信息："+json);
				return  JSONUtil.toJSON(json);
			}
		}catch (PlatformException e) {
			json = new HashMap<String, Object>();
			log.error("异常信息："+e.getMessage(),e);
			json.put("resCode", ResponseEnum.SYS_FAILD.getCode());
			json.put("resMsg", e.getMessage());
		}catch(Exception e){
			json = new HashMap<String, Object>();
			log.error("异常信息："+e,e);
			json.put("resCode", ResponseEnum.SYS_FAILD.getCode());
			json.put("resMsg", e);
		}
		log.info("放款结果通知返回给网关的信息："+json);
		return  JSONUtil.toJSON(json);
	}
    
    /**
     * 查询协议接口
     * @param loanId
     * @param busNumber
     * @param orderNo
     * @return
     * @throws DecoderException 
     */
    @SuppressWarnings("unused")
    @RequestMapping(value="/searchAgreementInterface")
	private Map<String, Object> searchAgreementInterface(Long loanId,String busNumber,String orderNo) throws DecoderException{
    	Map<String, Object> json = new HashMap<String, Object>();
    	VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
    	String appNo = loanInfo.getAppNo();
    	Bsb100011Vo bsb11 = new Bsb100011Vo();
    	bsb11.setTxncd("BYXY0011");
    	bsb11.setOrderNo(orderNo);
    	bsb11.setProdSubNo("400001");
        RequestInfo requestInfoVo = null;
        String rsultStr = "";
        String url = gatewayInterfaceUrl;
        if (Strings.isEmpty(url)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"【包银】--接口路径不存在");
        }
        try{
        	requestInfoVo = GatewayUtils.getSendGatewayRequestVo(bsb11, GatewayFuncIdEnum.包商银行查询协议接口);
            log.info("【包商银行】--查询协议接口接口url:{}参数：{}", url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
            log.info("【包商银行】--查询协议接口接口url:{}响应：{}", url, StringUtils.abbreviate(JSONUtil.toJSON(rsultStr),64));
        }catch (Exception e){
            throw new PlatformException(ResponseEnum.FULL_MSG,"【包银】--查询协议接口异常");
        }
        JSONObject jsonObject = JSON.parseObject(rsultStr);
        boolean flag = false;
        if("0000".equals(jsonObject.get("resCode").toString())){
        	Object infos =  jsonObject.get("infos");
        	if(infos != null){
        		byte[] input = Hex.decodeHex(infos.toString().toCharArray());
        		InputStream inputStream = new ByteArrayInputStream(input);
        		try {
        			flag = uploadBSYHPdf(inputStream, loanInfo);
				} catch (PlatformException e) {
        			log.error("【包银】上传pdf到ftp失败...");
        			json.put("resCode", ResponseEnum.SYS_SUCCESS.getCode());
  				    json.put("resMsg", "上传pdf到ftp失败"+e.getMessage());
  				    return json;
				}
        	}
        }else{
        	flag = false;
        	log.error("获取【包银】pdf流失败,返回的code为："+jsonObject.get("resCode").toString()+",msg信息为："+jsonObject.get("respDesc").toString());
        	json.put("resCode", ResponseEnum.SYS_SUCCESS.getCode());
		    json.put("resMsg", "获取【包银】pdf流失败");
		    return json;
        }
        json.put("resCode", ResponseEnum.SYS_SUCCESS.getCode());
		json.put("resMsg", "查询协议接口成功！");
        return json;
    }
    
    /**
     * 上传pdf到指定的ftp目录
     * @param inputStream
     * @param loanInfo
     * @return
     */
    public boolean uploadBSYHPdf(InputStream inputStream,VLoanInfo loanInfo) throws PlatformException{
    	FTPUtil ftpUtil=new FTPUtil();
    	boolean succeStatus = false;
        try {
            //连接ftp服务器
        	ftpUtil.connectServer(ConnectBhxtFtpServiceImpl.host, ConnectBhxtFtpServiceImpl.port, ConnectBhxtFtpServiceImpl.userName, ConnectBhxtFtpServiceImpl.password, ConnectBhxtFtpServiceImpl.storeDir, ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
            log.info("上传包商银行PDF，连接ftp服务器成功!");
            String path = ConnectBhxtFtpServiceImpl.storeDir + loanInfo.getAppNo();
            String directory = path + bsPdfStoreDir;//存放包银合同pdf目录
            if(!ftpUtil.changeDirectory(directory)){
                if(!ftpUtil.changeDirectory(path)){
                	ftpUtil.createDirectory(path);
                    //throw new PlatformException(ResponseEnum.FULL_MSG,"找不到需要上传的父目录");
                }
                if(!ftpUtil.createDirectory(directory)){
                    throw new PlatformException(ResponseEnum.FULL_MSG,"上传目录不存在！");
                }
                if(!ftpUtil.changeDirectory(directory)){
                    throw new PlatformException(ResponseEnum.FULL_MSG,String.format("切换新创建存放贷款合同洗衣pdf文件的目录%s失败%s"));
                }
            };
            String data = System.currentTimeMillis()+"";
            String md5DateStr  = MD5Util.md5Hex(data,"UTF-8");
            String fileName = "S1_PDF_SIGN-"+md5DateStr+".pdf";
            log.info("包商银行上传PDF,存放目录："+directory+",文件名："+fileName);
            succeStatus = ftpUtil.uploadFile(inputStream,fileName);
            if(!succeStatus){
            	log.error("【包银】上传pdf,保存文件失败...");
            }
        }catch(Exception e){
        	try {
        		ftpUtil.closeServer();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return succeStatus;
    }
    
    /**
     * 自扣还款结果通知接口  T日执行
     * @param request
     * @param response
     * @param byxy0010Vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/autoDebitRepayResultNotify",method = RequestMethod.POST,consumes = "application/json")
    public String autoDebitRepayResultNotify(HttpServletRequest request,HttpServletResponse response,@RequestBody BYXY0010Vo byxy0010Vo){
    	 log.info("【包商银行】自扣还款结果通知接口BYXY0010Vo，来自IP：{}的请求参数{}", request.getRemoteHost(), JSONUtil.toJSON(byxy0010Vo));
    	 Map<String, Object> json = new HashMap<String, Object>();
    	 json.put("resCode", ResponseEnum.SYS_SUCCESS.getCode());
 		 json.put("resMsg", "【包商银行】自扣还款结果通知接收成功！");
    	 String bankCardNo = byxy0010Vo.getBankCardNo();
    	 String currency = byxy0010Vo.getCurrency();//默认156-人民币
    	 String orderNo = byxy0010Vo.getOrderNo();
    	 Integer period = byxy0010Vo.getPeriod();
    	 String scheduleDate = byxy0010Vo.getScheduleDate();
    	 String payResult = byxy0010Vo.getPayResult();
    	 String failReason = byxy0010Vo.getFailReason();
    	 BigDecimal totalamt = byxy0010Vo.getTotalamt();
    	 LoanBsbMapping bsb = loanBsbMappingDao.getByOrderNo(orderNo);//放款结果通知接口BYXY0016会把借据号(orderNo)更新到 loan_bsb_mapping中
    	 if(bsb == null){
    		 json = new HashMap<String, Object>();
        	 json.put("resCode", ResponseEnum.SYS_FAILD.getCode());
 			 json.put("resMsg", "【借据号"+orderNo+"】不存在！");
 			 return  JSONUtil.toJSON(json);
    	 }
    	 VLoanInfo loan = vLoanInfoService.findByLoanId(bsb.getLoanId());
    	 if(loan == null){
    		 json = new HashMap<String, Object>();
        	 json.put("resCode", ResponseEnum.SYS_FAILD.getCode());
 			 json.put("resMsg", "【借据号+"+orderNo+"】对应的债权不存在！");
 			 return  JSONUtil.toJSON(json);
    	 }
    	 Map<String, Object> paramsMap = new HashMap<String, Object>();
    	 paramsMap.put("loanId", bsb.getLoanId());
    	 paramsMap.put("period", period);
    	 paramsMap.put("scheduleDate", scheduleDate);
    	 paramsMap.put("deductState", "t");
    	 List<RepayResultNotifyLog> repayResultNotifyList = repayResultNotifyLogService.findListByMap(paramsMap);
    	 log.info("参数："+paramsMap+"，记录数："+repayResultNotifyList.size());
    	 if(CollectionUtils.isNotEmpty(repayResultNotifyList) && "1".equals(payResult)){//同一债权 同一期数 同一交易日期 且划扣成功 
    		 log.info("已存在同一个债权【"+bsb.getLoanId()+"】,相同期数【"+period+"】,相同交易日期【"+scheduleDate+"】且划扣成功的通知，不进行再次保存！");
    	 }else{
    		 RepayResultNotifyLog repayResultNotify = new RepayResultNotifyLog();
    		 repayResultNotify.setId(sequencesService.getSequences(SequencesEnum.REPAY_RESULT_NOTIFY_LOG));
    		 repayResultNotify.setLoanId(bsb.getLoanId());
    		 repayResultNotify.setBankCardNo(bankCardNo);
    		 repayResultNotify.setCurrency(currency);//人民币
    		 repayResultNotify.setOrderNo(orderNo);
    		 repayResultNotify.setPeriod(period);
    		 repayResultNotify.setScheduleDate(scheduleDate);
    		 repayResultNotify.setPayResult(payResult);
    		 repayResultNotify.setFailReason(failReason);
    		 repayResultNotify.setTotalamt(totalamt);
    		 repayResultNotify.setState("0");//消费状态 0未消费 1已消费
    		 repayResultNotify.setNotifyType("3");////1提前扣款2结清代偿3自扣还款
    		 if("1".equals(payResult)){// 1-成功 2-失败
    			 repayResultNotify.setDeductState("t");//划扣状态 t成功 f失败
    			 log.info("【包商银行】自扣还款结果通知接收成功,包银划扣成功！");
        	 }else if("2".equals(payResult)){
        		 repayResultNotify.setDeductState("f");//划扣状态 t成功 f失败
         		 json.put("resMsg", "【包商银行】自扣还款结果通知接收成功,包银划扣失败,失败原因："+failReason);
         		 log.info("【包商银行】自扣还款结果通知接收成功,包银划扣失败,失败原因："+failReason);
        	 }
        	 repayResultNotifyLogService.save(repayResultNotify);//保存包银自扣还款结果通知日志  
        	 
        	 //创建第三方划扣信息
        	 debitBaseInfoService.createCallbackThirdOffer(loan, 
        			 afterLoanService.getPerReapyAmount(new Date(), loan.getId()), 
        			 "1".equals(payResult) ? true : false, failReason);
    	 }
    	 
         return  JSONUtil.toJSON(json);
    }
	
	/**
     * 还款结果通知接口 
     * @param request
     * @param response
     * @param byxy0018Vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/advanceRepayResultNotify",method = RequestMethod.POST,consumes = "application/json")
    public String advanceRepayResultNotify(HttpServletRequest request,HttpServletResponse response,@RequestBody BYXY0018Vo byxy0018Vo){
    	 log.info("【包商银行】还款结果通知接口BYXY0018Vo，来自IP：{}的请求参数{}", request.getRemoteHost(), JSONUtil.toJSON(byxy0018Vo));
    	 Map<String, Object> json = new HashMap<String, Object>();
    	 String repayBusNumber = byxy0018Vo.getRepayBusNumber();
    	 String respcd = byxy0018Vo.getRespcd();
    	 String resptx = byxy0018Vo.getResptx();
    	 //todo 更新repay_bus_log 划扣状态 
    	 Map<String, Object> paramMap = new HashMap<String, Object>();
    	 paramMap.put("repayBusNumber", repayBusNumber);
    	 List<RepayBusLog> repayBusLogList = repayBusLogDao.findListByMap(paramMap);
    	 if (CollectionUtils.isEmpty(repayBusLogList)){
        	 json = new HashMap<String, Object>();
        	 json.put("resCode", ResponseEnum.SYS_FAILD.getCode());
 			 json.put("resMsg", "【还款申请业务流水号】"+repayBusNumber+"不存在！");
             return  JSONUtil.toJSON(json);
         }
    	 RepayBusLog repayBusLog = repayBusLogList.get(0);
    	 Long applyType = repayBusLog.getApplyType();
    	 repayBusLog.setId(repayBusLog.getId());
    	 repayBusLog.setRespCd(respcd);
		 repayBusLog.setReternMsg(resptx);
		 repayBusLog.setUpdateTime(new Date());
		 
		 List<RepayResultNotifyLog> repayResultNotifyLogList= repayResultNotifyLogService.findListByMap(paramMap);
		 RepayResultNotifyLog  repayResultNotify = null;
		 //如果已存在业务流水号这条数据，则不保存。
		 if(repayResultNotifyLogList == null||repayResultNotifyLogList.size()==0){
			 log.info("包银还款结果通知接口，保存结果至包银还款结果通知记录表...");
			 repayResultNotify = new RepayResultNotifyLog();
			 repayResultNotify.setId(sequencesService.getSequences(SequencesEnum.REPAY_RESULT_NOTIFY_LOG));
			 repayResultNotify.setLoanId(repayBusLog.getLoanId());
			 repayResultNotify.setRepayBusNumber(repayBusNumber);
			 repayResultNotify.setRespcd(respcd);
			 repayResultNotify.setResptx(resptx);
			 repayResultNotify.setState("0");//消费状态 0未消费 1已消费
			 repayResultNotify.setNotifyType(applyType.toString());//1提前扣款2结清代偿3自扣还款
		 }
    	 if("0000".equals(respcd)){//0000 交易成功
    		 log.info("【包商银行】还款结果通知划扣成功,还款业务申请流水号为："+repayBusNumber);
    		 if(repayResultNotify != null){
    			 repayResultNotify.setDeductState("t");
    			 repayResultNotifyLogService.save(repayResultNotify);//保存包银自扣还款结果通知日志
    		 }
			 repayBusLog.setDeductState("t");//划扣状态 为t成功
			 repayBusLogDao.update(repayBusLog);
    	 }else{//0100 交易失败
    		 log.info("【包商银行】还款结果通知划扣失败信息："+resptx+"，还款业务申请流水号为："+repayBusNumber);
    		 if(repayResultNotify != null){
    		     repayResultNotify.setDealState(DealStateEnum.NONE.getCode());
    			 repayResultNotify.setDeductState("f");
    			 repayResultNotifyLogService.save(repayResultNotify);//保存包银自扣还款结果通知日志
    		 }
    		 repayBusLog.setDeductState("f");//划扣状态 为f失败
			 repayBusLogDao.update(repayBusLog);
			 if(applyType == 1){//结束 提前扣款申请
				 log.info("包银还款结果通知划扣失败，债权id为："+repayBusLog.getLoanId()+",更新特殊还款表状态为结束！");
				 loanSpecialRepaymentService.updateSpecialRepaymentToEnd(repayBusLog.getLoanId(), SpecialRepaymentTypeEnum.提前扣款.getValue(), "申请");
			 }
    	 }
    	 try {
			debitTransactionService.callbackNotify(repayBusNumber, respcd, resptx, applyType.toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
    	 json.put("resCode", ResponseEnum.SYS_SUCCESS.getCode());
 		 json.put("resMsg", "【包商银行】还款结果通知接收成功！");
		 log.info("【包商银行】还款结果通知接收成功");
         return  JSONUtil.toJSON(json);
    }
    
	/**
	 * 陆金所 —— 接受陆金所放款结果通知 Lufax800001Vo vo
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/lendingResultNotice", method=RequestMethod.POST, consumes="application/json")
	public String lendingResultNotice(@RequestBody Lufax800001Vo vo, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> info = new HashMap<String, Object>();
		log.info("陆金所——放款结果通知接口，请求参数："+JSON.toJSONString(vo));
		try{
			String appNo = vo.getApply_no();
			Assert.notNullAndEmpty(appNo, "申请单号不能为空");
			if(appNo.indexOf(LufaxConst.ZDJR) < 0){
				throw new PlatformException(ResponseEnum.FULL_MSG, "申请件号格式不正确！");
			}
			appNo = appNo.replace(LufaxConst.ZDJR, "");
			VLoanInfo searchVo = new VLoanInfo();
			//searchVo.setAppNo(appNo);
			searchVo.setChannelPushNo(appNo);
			List<VLoanInfo> loanInfoList = vLoanInfoService.findListByVO(searchVo);
			if(CollectionUtils.isEmpty(loanInfoList)){
				throw new PlatformException(ResponseEnum.FULL_MSG, "申请件号不存在！");
			}
			VLoanInfo loanInfo = loanInfoList.get(0);
			// 通知状态码
			String notifyCode = vo.getNotify_code();
			if(LufaxConst.NOTIFY_CODE_008.equals(notifyCode)){
				log.info("陆金所——进件通知接口，上架成功处理开始");
				info.put(RETURN_CODE, "0000");
				info.put(RETURN_MSG, "上架成功处理完成！");
			} else if (LufaxConst.NOTIFY_CODE_009.equals(notifyCode)){
				log.info("陆金所——进件通知接口，投资成功处理开始");
				/*if (financeGrantServiceImpl.isApplyFinanceGrant(loanInfo.getId())){
					loanBaseGrantService.createLoanBaseGrant(loanInfo);		
				}*/
				info.put(RETURN_CODE, "0000");
				info.put(RETURN_MSG, "投资成功处理完成！");
			} else if (LufaxConst.NOTIFY_CODE_010.equals(notifyCode)){
				log.info("陆金所——进件通知接口，放款成功处理开始");
				FinanceVo financeVo = new FinanceVo();
				financeVo.setIds(Strings.convertValue(loanInfo.getId(), String.class));
				User user = UserContext.getUser();
				String userCode = "admin";
				if (user != null) {
					userCode = user.getUserCode();
				}
				financeVo.setUserCode(userCode);
				
				boolean success = loanBaseGrantService.checkLoanBaseGrantStatus(loanInfo.getId(), FinanceGrantEnum.放款成功.getCode());
	            if(success){
	                throw new PlatformException(ResponseEnum.FULL_MSG, "该笔债权放款通知成功已发，无需再发，loanId:"+loanInfo.getId());
	            }
				
				boolean flag = loanBaseGrantService.checkLoanBaseGrantStatus(loanInfo.getId(), FinanceGrantEnum.申请中.getCode());
				if(flag){
					/** 调用核心放款接口 **/
					Map<String, Object> result = financeCoreService.grantLoan(financeVo);
					if (result.containsKey("code") && result.get("code").equals(Constants.SUCCESS_CODE)) {
						loanBaseGrantService.updateLoanBaseGrantByLoanId(loanInfo.getId(), FinanceGrantEnum.放款成功.getCode());
						loanStatusLufaxService.addLoanStatusLufax(loanInfo.getId(), LoanStatueLufaxEnum.正常.getCode());
					}else{
						throw new PlatformException(ResponseEnum.FULL_MSG, "调用核心放款接口异常");
					}					
				}else{
					throw new PlatformException(ResponseEnum.FULL_MSG, "调用核心放款接口异常,没找到状态为【申请中】 放款申请记录，loanId:"+loanInfo.getId());
				}

				info.put(RETURN_CODE, "0000");
				info.put(RETURN_MSG, "放款成功处理完成！");
			}
		}catch(PlatformException e){
			log.error("陆金所调用进件通知接口异常："+ e);
			info.put(RETURN_CODE, "9999");
			info.put(RETURN_MSG, e.getMessage());
		}catch(Exception e){
			log.error("陆金所调用进件通知接口异常："+ e);
			info.put(RETURN_CODE, "9999");
			info.put(RETURN_MSG, e.getMessage());
		}
		return JSONUtil.toJSON(info);
	}
	
	/**
     * 陆金所划扣结束后通过【转发代扣结果给证大】
     * @param request
     * @param response
     * @param lufax800020Vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deductResultNotify",method = RequestMethod.POST,consumes = "application/json")
    public String deductResultNotify(HttpServletRequest request,HttpServletResponse response,@RequestBody Lufax800020Vo lufax800020Vo){
    	log.info("【陆金所】代扣结果通知接口Lufax800020Vo，来自IP：{}的请求参数{}", request.getRemoteHost(), JSONUtil.toJSON(lufax800020Vo));
        log.info("转发代扣结果通知中的总笔数为---->" + lufax800020Vo.getLine_sum());
        Map<String, Object> map = new HashMap<String, Object>();
        List<DebitResultLufax> detail = lufax800020Vo.getDetail();
        try{
        	List<RepayInfoLufaxVO> repayInfoList = new ArrayList<RepayInfoLufaxVO>();
        	for(DebitResultLufax item:detail){
        		RepayInfoLufaxVO  repayInfo = new RepayInfoLufaxVO();
        		String serialno = item.getSerialno();
        		String anshuo_loan_accept_id =  item.getAnshuo_loan_accept_id();
        		String lufax_loan_req_id = item.getLufax_loan_req_id();
        		String anshuo_batch_id = item.getAnshuo_batch_id();
        		String frozen_amount = item.getFrozen_amount();
        		String lufax_repay_req_no = item.getLufax_repay_req_no();
        		
        		repayInfo.setSerialno(serialno);
        		repayInfo.setAnshuo_loan_accept_id(anshuo_loan_accept_id);
        		repayInfo.setLufax_loan_req_id(lufax_loan_req_id);
        		repayInfo.setAnshuobatchid(anshuo_batch_id);
        		repayInfo.setFrozen_amount(frozen_amount);
        		repayInfo.setLufax_repay_req_no(lufax_repay_req_no);
        		repayInfoList.add(repayInfo);
        	}
        	splitQueueLogService.disposeDeductResultNotifyFromLufax(repayInfoList);
        	map.put(RETURN_CODE, "0000");
    		map.put(RETURN_MSG, "【陆金所】代扣结果通知接收成功！");
        }catch(Exception e){
        	log.info("【陆金所】代扣结果通知接口处理数据失败！"+e.getMessage(),e);
        	map.put(RETURN_CODE, "9999");
    		map.put(RETURN_MSG, "【陆金所】代扣结果通知接收失败！"+e.getMessage());
        }
        return JSONUtil.toJSON(map);
   }
    
    @ResponseBody
    @RequestMapping(value = "/overdueCompensatoryNotify",method = RequestMethod.POST,consumes = "application/json")
    public String overdueCompensatoryNotify(HttpServletRequest request,@RequestBody JSONObject jso){
        log.info("【陆金所】一次性代偿完成结果通知接口，来自IP：{}的请求参数{}", request.getRemoteHost(), jso.toString());
        log.info("一次性代偿完成结果通知中的总笔数为---->" + jso.getString("line_sum"));
        Map<String, Object> map = new HashMap<String, Object>();
        
        try{
            splitQueueLogService.dealOverdueCompensatory(jso.getJSONArray("detail"));
            map.put(RETURN_CODE, "0000");
            map.put(RETURN_MSG, "【陆金所】一次性代偿完成结果通知接收成功！");
        }catch(Exception e){
            log.info("【陆金所】一次性代偿完成结果通知接口处理数据失败！"+e.getMessage(),e);
            map.put(RETURN_CODE, "9999");
            map.put(RETURN_MSG, "【陆金所】一次性代偿完成结果通知接口处理数据失败！"+e.getMessage());
        }

        return JSONUtil.toJSON(map);
   }
    
    @ResponseBody
    @RequestMapping(value = "/marginAuditResultNotify",method = RequestMethod.POST,consumes = "application/json")
    public String marginAuditResultNotify(HttpServletRequest request,@RequestBody JSONObject jso){
        log.info("【陆金所】保证金审核结果通知接口，来自IP：{}的请求参数{}", request.getRemoteHost(), jso.toString());
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            splitQueueLogService.marginAuditResultNotify(jso.getJSONArray("verifyMapList"));
            map.put(RETURN_CODE, "0000");
            map.put(RETURN_MSG, "【陆金所】保证金审核结果通知接收成功！");
        }catch(Exception e){
            log.info("【陆金所】保证金审核结果通知接口处理数据失败！"+e.getMessage(),e);
            map.put(RETURN_CODE, "9999");
            map.put(RETURN_MSG, "【陆金所】保证金审核结果通知接收失败！");
        }
        return JSONUtil.toJSON(map);
   }
    
    /**
     * 陆金所-获取陆金所充值结果
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/rechargeResult",method = RequestMethod.POST,consumes = "application/json")
    public String rechargeResult(HttpServletRequest request, HttpServletResponse response,@RequestBody Lufax800011Vo vo) {
    	log.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆Lufax800011Vo陆金所返回充值结果信息："+JSONUtil.toJSON(vo));
    	Map<String, Object> map = new HashMap<String, Object>();
    	try {
//    		String result = vo.getRecharge_status();//成功：success；失败：fail
    		String errorMsg = vo.getError_message();//错误信息
    		String batchNo = vo.getAnshuo_batch_id();
    		BigDecimal amount =new BigDecimal(vo.getActual_amount());		
    		if(Strings.isEmpty(amount)|| amount.equals(BigDecimal.ZERO)){//失败
    			publicAccountDetailService.updateAccDetailByBatchNo(null, batchNo, AccountTradeTypeEnum.充值.getCode(), amount, null, "2",errorMsg);
    			map.put(RETURN_CODE, "9999");
        		map.put(RETURN_MSG, "充值失败");
    		}else{//充值成功
    			publicAccountDetailService.updateAccDetailByBatchNo(null, batchNo, AccountTradeTypeEnum.充值.getCode(), amount, null, "1",errorMsg); 
    			map.put(RETURN_CODE, "0000");
        		map.put(RETURN_MSG, "充值成功");
    		}
		} catch (Exception e) {
			log.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆Lufax800011Vo获取陆金所充值结果失败："+JSONUtil.toJSON(vo),e);
			map.put(RETURN_CODE, "9999");
    		map.put(RETURN_MSG, "充值失败："+e.getMessage());
		}
    	return JSONUtil.toJSON(map);	
    }
    /**
     * 陆金所-获取陆金所提现结果
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawResult",method = RequestMethod.POST,consumes = "application/json")
    public String withdrawResult(HttpServletRequest request, HttpServletResponse response,@RequestBody Lufax800013Vo vo) {
    	log.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆Lufax800013Vo陆金所返回提现结果信息："+JSONUtil.toJSON(vo));
    	Map<String, Object> map = new HashMap<String, Object>();
    	try {
    		String batchNo = vo.getWithdraw_req_no();
    		String result = vo.getWithdraw_result();//0失败,1成功
    		String msg = vo.getWithdraw_msg();//结果说明
    		if("1".equals(result)){//提现成功
    			publicAccountDetailService.updateAccDetailByBatchNo(null, batchNo, AccountTradeTypeEnum.提现.getValue(), null, null, "1",msg);
    			map.put(RETURN_CODE, "0000");
        		map.put(RETURN_MSG, "提现成功");
    		}else{//失败
    			publicAccountDetailService.updateAccDetailByBatchNo(null, batchNo, AccountTradeTypeEnum.提现.getValue(), null, null, "2",msg);
    			map.put(RETURN_CODE, "9999");
        		map.put(RETURN_MSG, "提现失败");
    		}	
		} catch (Exception e) {
			log.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆Lufax800013Vo获取陆金所提现结果失败："+JSONUtil.toJSON(vo),e);
			map.put(RETURN_CODE, "9999");
    		map.put(RETURN_MSG, "提现失败:"+e.getMessage());
		}       
    	return JSONUtil.toJSON(map);
    }
    
    /**
     * 陆金所核算推送投资时间到证大
     * @param request
     * @param response
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pushInvestTimeNotify",method = RequestMethod.POST,consumes = "application/json")
    public String pushInvestTimeNotify(HttpServletRequest request, HttpServletResponse response,@RequestBody Lufax820050Vo vo){
    	log.info("【陆金所】推送投资时间到证大-来自IP：{}的请求参数{}", request.getRemoteHost(), JSONUtil.toJSON(vo));
    	Map<String, Object> map = new HashMap<String, Object>();
    	String apply_no = vo.getApply_no();
    	String invest_time = vo.getInvest_time();
    	String appNo = apply_no.replace(LufaxConst.ZDJR, "");
    	VLoanInfo vLoanInfo = new VLoanInfo();
    	//vLoanInfo.setAppNo(appNo);
    	vLoanInfo.setChannelPushNo(appNo);
    	List<VLoanInfo> vLoanInafoList = vLoanInfoService.findListByVO(vLoanInfo);
    	if(CollectionUtils.isEmpty(vLoanInafoList)){
    		log.info("【陆金所】推送投资时间到证大接口：申请号不存在："+apply_no);
    		map.put(RETURN_CODE, "9999");
    		map.put(RETURN_MSG, "【陆金所】推送投资时间到证大接口：申请号不存在："+apply_no);
            return JSONUtil.toJSON(map);
    	}
    	vLoanInfo = vLoanInafoList.get(0);
    	Long loanId = vLoanInfo.getId();
    	LoanBsbMapping bsb = loanBsbMappingDao.getByLoanId(loanId);
	    if(bsb == null){
	    	log.info("【陆金所】推送投资时间到证大接口--申请号对应的债权信息不存在："+apply_no+",loanId"+loanId);
	    	map.put(RETURN_CODE, "9999");
	    	map.put(RETURN_MSG,  "申请号对应的核心债权信息不存在"+apply_no);
			return  JSONUtil.toJSON(map);
	    }
	    Date invest_time_right = Dates.parse(invest_time, Dates.DATAFORMAT_YYYYMMDDHHMMSS);
	    if(invest_time_right == null){
	    	log.info("【陆金所】推送投资时间到证大接口--时间格式不正确："+invest_time);
	    	map.put(RETURN_CODE, "9999");
	    	map.put(RETURN_MSG,  "时间格式不正确"+invest_time+",应该为yyyyMMddHHmmss");
			return  JSONUtil.toJSON(map);
	    }
    	try{
    		Lufax100018OutputVo out = splitQueueLogService.investTimeNotify(loanId,invest_time);
    		String jsonStr = JSON.toJSONString(out);
    		log.info("【陆金所】推送投资时间到证大接口返回给陆金所的信息:"+jsonStr);
    		return jsonStr;
    	}catch (PlatformException e) {
			log.error("【陆金所】推送投资时间到证大接口--处理失败：",e);
			map.put(RETURN_CODE, "9999");
    		map.put(RETURN_MSG, "【陆金所】推送投资时间到证大接口【重新生成还款计划失败】");
		}catch(Exception e){

    		log.error("【陆金所】推送投资时间到证大接口--处理失败：",e);
    		map.put(RETURN_CODE, "9999");
    		map.put(RETURN_MSG, "【陆金所】推送投资时间到证大接口--处理失败");
    	}
    	return JSONUtil.toJSON(map);
    }
    
    
    @Autowired
    private TransmitLoanData2LufaxJob trans;
    @ResponseBody
    @RequestMapping(value = "/test")
    public String withdrawResult(HttpServletRequest request, HttpServletResponse response) {
    	trans.execute();
    	return "lol";
    }
}
