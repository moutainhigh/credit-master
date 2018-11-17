package com.zdmoney.credit.lujinsuo.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.tpp.AccountTradeTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.LufaxUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100014Vo;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100015Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.Lufax800011Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.Lufax800013Vo;
import com.zdmoney.credit.ljs.domain.PublicAccountDetail;
import com.zdmoney.credit.ljs.domain.PublicVirtualAccount;
import com.zdmoney.credit.ljs.service.pub.IPublicAccountDetailService;
import com.zdmoney.credit.ljs.service.pub.IPublicVirtualAccountService;
import com.zdmoney.credit.ljs.vo.ImportAccountDetailVo;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 陆金所对公账户管理
 * @author YM10104
 *
 */
@Controller
@RequestMapping("/lujinsuo")
public class BasePublicController extends BaseController {

    protected static Log logger = LogFactory.getLog(BasePublicController.class);
    @Autowired
    private IPublicAccountDetailService publicAccountDetailService;
    @Autowired
    private IPublicVirtualAccountService publicVirtualAccountService;
    @Autowired
    private ISequencesService sequencesService;
    /**
     * 跳转到对公账户页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/public/jumpAccountDetailPage")
    public String jumpAccountDetailPage(HttpServletRequest request, HttpServletResponse response,ModelMap model) {    	
		/** 交易类型 **/
    	model.addAttribute("accountTradeTypes", AccountTradeTypeEnum.values());
    	return "/lufax/accountDetailList";
    }
    /**
     * 对公账户查询
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/public/accountDetailList")
    @ResponseBody
    public String accountDetailList(HttpServletRequest request, HttpServletResponse response,ModelMap model) {    	
    	int rows = Integer.parseInt(request.getParameter("rows"));
		int page = Integer.parseInt(request.getParameter("page"));
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String accountTradeType = request.getParameter("accountTradeType");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("DESC");
		paramMap.put("pager", pager);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("accountTradeType", accountTradeType);
		pager = publicAccountDetailService.findWithPgByMap(paramMap);	
    	//查询出当前余额
		PublicVirtualAccount publicVirtualAccount = publicVirtualAccountService.findByAccountType("00001");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalAmt", publicVirtualAccount.getTotalAmt());
		pager.setMapData(map);
		return toPGJSONWithCode(pager);
    }
    /**
     * 充值
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/public/recharge")
    @ResponseBody
    public String recharge(HttpServletRequest request, HttpServletResponse response) {
    	ResponseInfo responseInfo = new ResponseInfo();
    	BigDecimal amount = new BigDecimal(request.getParameter("amount")) ;//充值金额
    	String memo = request.getParameter("memo");//备注
    	if(amount.compareTo(BigDecimal.ZERO)!=1){
    	   responseInfo.setResCode(ResponseEnum.SYS_FAILD.getCode());
      	   responseInfo.setResMsg("充值金额必须大于0");
      	   return toResponseJSON(responseInfo);
    	}
    	
    	Lufax100014Vo lufax100014Vo=new Lufax100014Vo();
    	lufax100014Vo.setAmount(amount);//充值金额
    	lufax100014Vo.setAnshuo_batch_id(LufaxUtil.createAnshuoBatchId());//批次号
    	lufax100014Vo.setApply_at(Dates.getDateTime("yyyy-MM-dd hh:mm:ss"));//调用发起时间
    	JSONObject msg = GatewayUtils.callCateWayInterface(lufax100014Vo, "lufax100014");
    	logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆陆金所充值返回信息："+JSONUtil.toJSON(msg));
		if(Strings.isNotEmpty(msg)){			
           if("0000".equals(msg.get("ret_code"))){//调取接口成功
        	   publicAccountDetailService.insertAccDetail(null, lufax100014Vo.getAnshuo_batch_id(), AccountTradeTypeEnum.充值.getCode(), amount, memo, "0",null);
        	   responseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
        	   responseInfo.setResMsg("请求受理中");
           }else{//失败不插入流水
        	   responseInfo.setResCode(ResponseEnum.SYS_FAILD.getCode());
        	   responseInfo.setResMsg("充值失败");
           }
        }else{
        	responseInfo.setResCode(ResponseEnum.SYS_FAILD.getCode());
        	 responseInfo.setResMsg("充值失败");
        }
    	return toResponseJSON(responseInfo);
    }
    /**
     * 提现
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/public/withdrawals")
    @ResponseBody
    public String withdrawals(HttpServletRequest request, HttpServletResponse response) {
    	ResponseInfo responseInfo = new ResponseInfo();
    	BigDecimal amount = new BigDecimal(request.getParameter("amountTx")) ;//提现金额
    	String memo = request.getParameter("memoTx");//备注
    	if(amount.compareTo(BigDecimal.ZERO)!=1){
    		responseInfo.setResCode(ResponseEnum.SYS_FAILD.getCode());
       	   	responseInfo.setResMsg("提现金额必须大于0");
       	   	return toResponseJSON(responseInfo);
     	}  
    	//提现金额大于账户余额无法操作
    	PublicVirtualAccount publicVirtualAccount = publicVirtualAccountService.findByAccountType("00001");
    	if(amount.compareTo(publicVirtualAccount.getTotalAmt())==1){
    	    responseInfo.setResCode(ResponseEnum.SYS_FAILD.getCode());
    	  	responseInfo.setResMsg("提现金额不能大于账户余额");
    	  	return toResponseJSON(responseInfo);
    	}
    	
    	Lufax100015Vo lufax100015Vo=new Lufax100015Vo();
    	lufax100015Vo.setWithdraw_amount(amount);
    	lufax100015Vo.setWithdraw_req_no(LufaxUtil.createInterfaceReqId("100015"));
    	JSONObject msg = GatewayUtils.callCateWayInterface(lufax100015Vo, "lufax100015");
    	logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆陆金所提现返回信息："+JSONUtil.toJSON(msg));
    	if(Strings.isNotEmpty(msg)){
    		if("0000".equals(msg.get("ret_code"))){//调取接口成功
         	   publicAccountDetailService.insertAccDetail(null,lufax100015Vo.getWithdraw_req_no(), AccountTradeTypeEnum.提现.getCode(), amount, memo, "0",null);
         	   responseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
         	   responseInfo.setResMsg("受理中");
            }else{//失败不插入流水
         	   responseInfo.setResCode(ResponseEnum.SYS_FAILD.getCode());
         	   responseInfo.setResMsg("提现失败");
            }
        }else{
        	responseInfo.setResCode(ResponseEnum.SYS_FAILD.getCode());
        	responseInfo.setResMsg("提现失败");
        }
    	return toResponseJSON(responseInfo);
    }    
    /**
     * 明细导出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/public/importDetailToExc")
    @ResponseBody
    public void importDetailToExc(HttpServletRequest request, HttpServletResponse response) {  
    	ResponseInfo responseInfo = new ResponseInfo();
    	try {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String accountTradeType = request.getParameter("accountTradeType");
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("startDate", startDate);
			paramMap.put("endDate", endDate);
			paramMap.put("accountTradeType", accountTradeType);
			List<PublicAccountDetail> list=publicAccountDetailService.findByMap(paramMap);
			List<ImportAccountDetailVo> listExcel =new ArrayList<ImportAccountDetailVo>();//excel结果数据
			for (int i = 0; i < list.size(); i++) {
				ImportAccountDetailVo vo=new ImportAccountDetailVo();
				PublicAccountDetail publicAccountDetail=list.get(i);
				vo.setContractNum(publicAccountDetail.getContractNum());
				vo.setTradeDate(Dates.getDateTime(publicAccountDetail.getTradeDate(), "yyyy/MM/dd"));
				vo.setTradeTime(publicAccountDetail.getTradeTime());
				vo.setPay(publicAccountDetail.getPay());
				vo.setInCome(publicAccountDetail.getInCome());
				vo.setClosingBalance(publicAccountDetail.getClosingBalance());				
				if("0".equals(publicAccountDetail.getTradeType())){
					vo.setTradeType("充值");
					vo.setMemo(publicAccountDetail.getMemo());
				}else if("1".equals(publicAccountDetail.getTradeType())){
					vo.setTradeType("提现");
				}else if("2".equals(publicAccountDetail.getTradeType())){
					vo.setTradeType("还款");
				}else if("3".equals(publicAccountDetail.getTradeType())){
					vo.setTradeType("垫付");
				}else if("4".equals(publicAccountDetail.getTradeType())){
					vo.setTradeType("回购");
				}else{
					vo.setTradeType("");
				}				
				listExcel.add(vo);
			}
			
			String fileName="流水明细.xlsx";
			String[] headers={"借款合同号","交易日","交易时间","支","收","余额","摘要","交易类型"};
			ExcelUtil.exportExcel(fileName, headers, listExcel, request, response, "");			
			responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG,new Object[]{"下载成功！"});
			
			response.setContentType("text/html");
			response.getWriter().write(toResponseJSON(responseInfo));		
		} catch (Exception e) {
			/** 系统忙 **/
			logger.error(e, e);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		}		
    }
}
