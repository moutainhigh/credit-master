package com.zdmoney.credit.lujinsuo.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.DebitRepayTypeEnum;
import com.zdmoney.credit.common.constant.SplitNotifyStateEnum;
import com.zdmoney.credit.common.constant.SplitResultStateEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.common.vo.core.RepayInfoLufaxVO;
import com.zdmoney.credit.common.vo.core.SplitQueueManangeVO;
import com.zdmoney.credit.debit.dao.pub.ISplitQueueLogDao;
import com.zdmoney.credit.debit.domain.SplitQueueLog;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.framework.controller.BaseController;

/**
 * 分账管理
 * @author YM10112
 *
 */
 
@Controller
@RequestMapping("/splitQueueManagement")
public class SplitQueueManagementController extends BaseController {

    protected static Log logger = LogFactory.getLog(SplitQueueManagementController.class);
    @Autowired
    private ISplitQueueLogService splitQueueLogService;
    @Autowired
    private ISplitQueueLogDao splitQueueLogDao;
    
    /**
     * 跳转到对分账管理页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/jumpSplitQueueManagementPage")
    public String jumpSplitQueueManagementPage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {    	
		modelMap.addAttribute("splitNotifyStates", SplitNotifyStateEnum.values());
		modelMap.addAttribute("splitResultStates", SplitResultStateEnum.values());
		modelMap.put("repayTypes", DebitRepayTypeEnum.values());
		return "/lufax/splitQueueManagement";
    }
    
    /**
     * 分账管理查询
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/splitQueueManagementList")
    @ResponseBody
    public String searchSplitQueueList(int rows, int page, HttpServletRequest request, HttpServletResponse response) { 
    	String name = request.getParameter("name");
		String idNum = request.getParameter("idNum");
		String contractNum = request.getParameter("contractNum");
		String startCreateTime = request.getParameter("startCreateTime");
		String endCreateTime = request.getParameter("endCreateTime");
		String splitNotifyState = request.getParameter("splitNotifyState");
		String splitResultState = request.getParameter("splitResultState");
		String repayType = request.getParameter("repayType");
		String payOffType = request.getParameter("payOffType");
		String batchId = request.getParameter("batchId");
		String debitNo = request.getParameter("debitNo");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("name",name);
		paramMap.put("idNum",idNum);
		paramMap.put("contractNum", contractNum);
		paramMap.put("startCreateTime",startCreateTime);
		paramMap.put("endCreateTime",endCreateTime);
		paramMap.put("splitNotifyState",splitNotifyState);
		paramMap.put("splitResultState",splitResultState);
		paramMap.put("repayType",repayType);
		paramMap.put("payOffType",payOffType);
		if(Strings.isNotEmpty(batchId)){
			paramMap.put("batchId", batchId);
		}
		if(Strings.isNotEmpty(debitNo)){
			paramMap.put("debitNo", debitNo);
		}
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("S.ID");
		pager.setSort("DESC");
		paramMap.put("pager", pager);
		pager = splitQueueLogService.findWithPgByMap(paramMap);	
		return toPGJSONWithCode(pager);
    }
    
    /**
     * 选择数据，发送分账信息
     * @param request
     * @param response
     * @param splitIds
     * @return
     */
    @ResponseBody
    @RequestMapping("/splitQueueRepeatSend")
    public String splitQueueRepeatSend(@RequestParam String repayTypes,HttpServletRequest request,HttpServletResponse response){
        //还款类型（01：委托还款， 02：机构还款，03：逾期代偿  ，04：一次性回购，05：提前结清，06：逾期还回，07：回购结清）repayTypes(02=40,06=39,01=37)
        ResponseInfo responseInfo = null;
        try {
            if (Strings.isEmpty(repayTypes)) {
                responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(), "请求参数为空");
                return toResponseJSON(responseInfo);
            }
            String[] repayTypeArray = repayTypes.split(",");
            HashSet<String> str01 = new HashSet<String>();
    		HashSet<String> str02 = new HashSet<String>();
    		HashSet<String> str03 = new HashSet<String>();
    		HashSet<String> str04 = new HashSet<String>();
    		HashSet<String> str05 = new HashSet<String>();
    		HashSet<String> str06 = new HashSet<String>();
			for (String str : repayTypeArray) {
				String repayType = str.substring(0, 2);
				String splitIds = str.substring(str.indexOf("=") + 1,str.length());
				if (DebitRepayTypeEnum.委托还款.getCode().equals(repayType)) {
					str01.add(splitIds);
					continue;
				} else if (DebitRepayTypeEnum.机构还款.getCode().equals(repayType)) {
					str02.add(splitIds);
					continue;
				} else if (DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType)) {
					str03.add(splitIds);
					continue;
				} else if (DebitRepayTypeEnum.一次性回购.getCode().equals(repayType)) {
					str04.add(splitIds);
					continue;
				} else if (DebitRepayTypeEnum.提前结清.getCode().equals(repayType)) {
					str05.add(splitIds);
					continue;
				} else if (DebitRepayTypeEnum.逾期还回.getCode().equals(repayType)) {
					SplitQueueLog splitQueueLog = splitQueueLogDao.get(Long.parseLong(splitIds)); 
					String splitResultState = splitQueueLog.getSplitResultState();
					if(SplitResultStateEnum.未分账.getCode().equals(splitResultState)){
						splitQueueLogService.updateRepaymentDetailAndLoanStatus4Lufax(splitQueueLog);
					}
					str06.add(splitIds);
					continue;
				}
			}
			if(str01.size()>0){
				logger.info("【陆金所】委托还款发送分账信息："+str01);
				splitQueueLogService.executeEntrustRepaymentInfo(str01);
			}
			if(str02.size()>0){
				logger.info("【陆金所】机构还款发送分账信息："+str02);
				splitQueueLogService.executeOrganRepaymentInfo(str02);
			}
			if(str03.size()>0){
				logger.info("【陆金所】逾期代偿发送分账信息："+str03);	
				splitQueueLogService.syncOverdueCompensatoryInfo(str03);
			}
			if(str04.size()>0){
				logger.info("【陆金所】一次性回购发送分账信息："+str04);
				splitQueueLogService.executeOneBuyBackRepaymentInfo(str04);
			}
			if(str05.size()>0){
				logger.info("【陆金所】提前结清发送分账信息："+str05);
				splitQueueLogService.executeAdvanceClearRepaymentInfo(str05);
			}
			if(str06.size()>0){
				logger.info("【陆金所】逾期还回发送分账信息："+str06);
				splitQueueLogService.executeOverdueRepaymentInfo(str06);
			}
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"操作成功");
        }catch (PlatformException pe){
        	logger.info("【陆金所】分账重发异常p："+pe.getMessage(),pe);
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),pe.getMessage());
        }catch (Exception e){
        	logger.info("【陆金所】分账重发异常e："+e.getMessage(),e);
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),"分账重发请求异常！"+e.getMessage());
        }
        return  toResponseJSON(responseInfo);
    }
    
    /**
     * 发送所有未分账，分账失败的信息一键重发
     * @param request
     * @param response
     * @param loanIds
     * @return
     */
    @ResponseBody
    @RequestMapping("/splitQueueOneAllSend")
    public String splitQueueOneAllSend(HttpServletRequest request,HttpServletResponse response){
    	//还款类型（01：委托还款， 02：机构还款，03：逾期代偿  ，04：一次性回购，05：提前结清，06：逾期还回，07：回购结清）
        ResponseInfo responseInfo = null;
        try {
        	Map<String,Object> map = new HashMap<String, Object>();
        	map.put("splitNotifyStates",new String[]{SplitNotifyStateEnum.待通知.getCode(),SplitNotifyStateEnum.通知失败.getCode()});
    		map.put("splitResultStates",new String[]{SplitResultStateEnum.未分账.getCode(),SplitResultStateEnum.分账失败.getCode()});
    		List<SplitQueueManangeVO> repayInfoList = splitQueueLogDao.findNotToThirdSplitList(map);
    		HashSet<String> str01 = new HashSet<String>();
    		HashSet<String> str02 = new HashSet<String>();
    		HashSet<String> str03 = new HashSet<String>();
    		HashSet<String> str04 = new HashSet<String>();
    		HashSet<String> str05 = new HashSet<String>();
    		HashSet<String> str06 = new HashSet<String>();
    		for(SplitQueueManangeVO vo : repayInfoList){
    			String repayType = vo.getRepayType();
    			String splitId = vo.getSplitId().toString();
    			if (DebitRepayTypeEnum.委托还款.getCode().equals(repayType)) {
					str01.add(splitId);
					continue;
				} else if (DebitRepayTypeEnum.机构还款.getCode().equals(repayType)) {
					str02.add(splitId);
					continue;
				} else if (DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType)) {
					str03.add(splitId);
					continue;
				} else if (DebitRepayTypeEnum.一次性回购.getCode().equals(repayType)) {
					str04.add(splitId);
					continue;
				} else if (DebitRepayTypeEnum.提前结清.getCode().equals(repayType)) {
					str05.add(splitId);
					continue;
				} else if (DebitRepayTypeEnum.逾期还回.getCode().equals(repayType)) {
					String splitResultState = vo.getSplitResultState();
					if(SplitResultStateEnum.未分账.getCode().equals(splitResultState)){
						SplitQueueLog log = new SplitQueueLog();
						log.setId(vo.getSplitId());
						log.setCreateTime(vo.getCreateTime());
						log.setLoanId(vo.getLoanId());
						log.setDebitNo(vo.getDebitNo());
						splitQueueLogService.updateRepaymentDetailAndLoanStatus4Lufax(log);
					}
					str06.add(splitId);
					continue;
				}
    		}
    		if(str01.size()>0){
				logger.info("【陆金所】委托还款发送分账信息："+str01);
				splitQueueLogService.executeEntrustRepaymentInfo(str01);
			}
			if(str02.size()>0){
				logger.info("【陆金所】机构还款发送分账信息："+str02);
				splitQueueLogService.executeOrganRepaymentInfo(str02);
			}
			if(str03.size()>0){
				logger.info("【陆金所】逾期代偿发送分账信息："+str03);	
				splitQueueLogService.syncOverdueCompensatoryInfo(str03);
			}
			if(str04.size()>0){
				logger.info("【陆金所】一次性回购发送分账信息："+str04);
				splitQueueLogService.executeOneBuyBackRepaymentInfo(str04);
			}
			if(str05.size()>0){
				logger.info("【陆金所】提前结清发送分账信息："+str05);
				splitQueueLogService.executeAdvanceClearRepaymentInfo(str05);
			}
			if(str06.size()>0){
				logger.info("【陆金所】逾期还回发送分账信息："+str06);
				splitQueueLogService.executeOverdueRepaymentInfo(str06);
			}
        	responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"操作成功");
        }catch (PlatformException pe){
        	logger.info("【陆金所】一键重发请求异常："+pe.getMessage(),pe);
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),pe.getMessage());
        }catch (Exception e){
        	logger.info("【陆金所】一键重发请求异常："+e.getMessage(),e);
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),"一键重发请求异常"+e.getMessage());
        }
        return  toResponseJSON(responseInfo);
    }
    
    /**
     * 同步还款计划
     * @param splitIds
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/syncRepaymentPlan")
    public String syncRepaymentPlan(RepayInfoLufaxVO vo, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            if (Strings.isEmpty(vo.getSplitId())) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "分账队列Id不能为空！");
            }
            if (Strings.isEmpty(vo.getRepayType())) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "还款类型不能为空！");
            }
            splitQueueLogService.syncRepaymentPlan(vo);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(), "操作完成");
        } catch (PlatformException e) {
            logger.error("【陆金所】同步还款计划异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), "【陆金所】同步还款计划异常：" + e.getMessage());
        } catch (Exception e) {
            logger.error("【陆金所】同步还款计划异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 同步还款记录
     * @param vo
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/syncRepaymentRecord")
    public String syncRepaymentRecord(RepayInfoLufaxVO vo, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            if (Strings.isEmpty(vo.getSplitId())) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "分账队列Id不能为空！");
            }
            if (Strings.isEmpty(vo.getRepayType())) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "还款类型不能为空！");
            }
            splitQueueLogService.syncRepaymentRecord(vo);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(), "操作完成");
        } catch (PlatformException e) {
            logger.error("【陆金所】同步还款记录异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), "【陆金所】同步还款记录异常：" + e.getMessage());
        } catch (Exception e) {
            logger.error("【陆金所】同步还款记录异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 同步还款明细
     * @param vo
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/syncRepaymentDetail")
    public String syncRepaymentDetail(RepayInfoLufaxVO vo, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            if (Strings.isEmpty(vo.getSplitId())) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "分账队列Id不能为空！");
            }
            if (Strings.isEmpty(vo.getRepayType())) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "还款类型不能为空！");
            }
            splitQueueLogService.syncRepaymentDetail(vo);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(), "操作完成");
        } catch (PlatformException e) {
            logger.error("【陆金所】同步还款明细异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), "【陆金所】同步还款明细异常：" + e.getMessage());
        } catch (Exception e) {
            logger.error("【陆金所】同步还款明细异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 更新分账队列数据
     * @param vo
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateSplitQueue")
    public String updateSplitQueue(SplitQueueLog vo, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            if (Strings.isEmpty(vo.getId())) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "分账队列Id不能为空！");
            }
            // 更新分账队列数据
            splitQueueLogService.updateSplitQueue(vo);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(), "操作完成");
        } catch (PlatformException e) {
            logger.error("更新分账队列数据异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), "更新分账队列数据：" + e.getMessage());
        } catch (Exception e) {
            logger.error("更新分账队列数据异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }
}
