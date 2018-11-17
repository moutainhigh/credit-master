package com.zdmoney.credit.chexiao.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.chexiao.domain.LoanStateVo;
import com.zdmoney.credit.chexiao.domain.UndoManage;
import com.zdmoney.credit.chexiao.service.pub.IundoManageService;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;

@Controller
@RequestMapping ("/chexiao")
public class UndoManageController extends BaseController {
			
	@Autowired
	IundoManageService undoManageService ;
	
	/**
	 * 撤销管理页面加载
	 * 
	 */
	@RequestMapping ("/undoManage")
	 public ModelAndView viewUndoManagePage(HttpServletRequest request, HttpServletResponse response) {
	    	this.createLog(request, SysActionLogTypeEnum.查询, "撤销管理", "加载页面处理");
	    	ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("chexiao/undoManageList");
			return modelAndView;
	}
	
	/**
	 * 查询处理
	 */
	 @RequestMapping("/searchUndoManage")
	 @ResponseBody
	    public String search(UndoManage undoManage, int rows, int page, HttpServletRequest request, HttpServletResponse response) {
	    	this.createLog(request, SysActionLogTypeEnum.查询, "撤销管理", "执行查询操作");
	    	Map<String, Object> paramsMap = new HashMap<String, Object>();
	    	Pager pager = new Pager();
			pager.setRows(rows);
			pager.setPage(page);
			pager.setSidx("t1.ID");
			pager.setSort(Pager.DIRECTION_DESC);
			paramsMap.put("contract_num",undoManage.getContract_num() );
			paramsMap.put("pager", pager);
	    	
			pager = undoManageService.findWithPgByMap(paramsMap);
			
	    	return toPGJSONWithCode(pager);
	    }
	/**
	 * 
	 * 撤销操作
	 * @return
	 */
	 
	 @RequestMapping("/undo")
	    @ResponseBody
	    public String undo(@RequestParam String tradeno,@RequestParam Long  tradedate,@RequestParam int promise_return_date,
	    		HttpServletRequest request, HttpServletResponse response) {
	    	this.createLog(request, SysActionLogTypeEnum.删除, "撤销操作", "执行撤销操作");
	    		ResponseInfo responseInfo = null;
	    		String  msg = undoManageService.undo(tradeno,promise_return_date,tradedate);
	    		if(Strings.isNotEmpty(msg)){
	                responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),msg);
	            }else{
	            	responseInfo.setResCode(ResponseEnum.SYS_FAILD.getCode());
	            }
	    		 return toResponseJSON(responseInfo);
	    }
	 
	 /**
	  * 变更还款状态
	  * 
	  */
	 @RequestMapping("/updateState")
	    @ResponseBody
	    public String updateState(LoanStateVo loanStateVo,
	    		HttpServletRequest request, HttpServletResponse response) {
	    	this.createLog(request, SysActionLogTypeEnum.更新, "更新状态操作", "还款状态更新操作");
	    		ResponseInfo responseInfo = null;
	    		try {
	    			if (loanStateVo.getLoanid()==""||loanStateVo.getLoanid().isEmpty()||loanStateVo.getUpdateLoanState()==""||loanStateVo.getUpdateLoanState().isEmpty()) {
	    				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
					}else{
	    			undoManageService.updateState(loanStateVo);
	                responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
	                }
				} catch (Exception e) {
	            	responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
				}
	    		 return toResponseJSON(responseInfo);
	    }
	 
	 
}
