package com.zdmoney.credit.offer.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.offer.domain.OfferNum;
import com.zdmoney.credit.offer.service.pub.IOfferNumService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;

@Controller
@RequestMapping ("/offerNum")
public class OfferNumController extends BaseController {
	protected static Log logger = LogFactory.getLog(OfferNumController.class);
	@Autowired 
	IOfferNumService offerNumService;
	@Autowired 
	IComEmployeeService comEmployeeService;
	@RequestMapping ("/offerNumPageInit")
	public String offerNumPageInit(HttpServletRequest request,HttpServletResponse response){
		return "/offer/offerNumList";
	}
	@RequestMapping ("/offerNumList")
	@ResponseBody
	public String offerNumList(HttpServletRequest request,HttpServletResponse response){
		Pager pager = new Pager();
		if (!StringUtils.isBlank(request.getParameter("rows"))&&!StringUtils.isBlank(request.getParameter("page"))) {
			int rows = Integer.parseInt(request.getParameter("rows"));
			int page = Integer.parseInt(request.getParameter("page"));
			pager.setRows(rows);
			pager.setPage(page);
		} 
		String name = request.getParameter("name");
		String usercode = request.getParameter("userCode");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		pager.setSidx("ID");
		pager.setSort("DESC");
		paramMap.put("pager", pager);
		paramMap.put("name", name);
		paramMap.put("userCode", usercode);
		pager = offerNumService.findWithPgByMap(paramMap);
		return toPGJSONWithCode(pager);
	}
	/**
	 * 新增划扣次数配置
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/insert")
	@ResponseBody
	public String insert(HttpServletRequest request, HttpServletResponse response){
		ResponseInfo responseInfo = null;
		String userCode = request.getParameter("userCode");
		String offerCount = request.getParameter("offerCount");
		try{
			Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userCode", userCode);
				paramMap.put("offerCount", offerCount);
			OfferNum	offerNum	= offerNumService.findOfferNumByCode(paramMap);
			
			if (offerNum == null) {
				OfferNum offerNum2 = new OfferNum();
				offerNum2.setOfferCount(offerCount);
				offerNum2.setUserCode(userCode);
				ComEmployee emp = comEmployeeService.findByUserCode(userCode);
				if (emp== null) {
					responseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
							ResponseEnum.SYS_FAILD.getDesc());
					responseInfo.setResMsg("该工号不存在！");
				} else {
					offerNumService.insert(offerNum2);
					this.createLog(request, SysActionLogTypeEnum.新增, "划扣次数", "划扣次数新增");
					// 正常返回
					responseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_SUCCESS.getCode(),
							ResponseEnum.SYS_SUCCESS.getDesc());
					responseInfo.setResMsg("操作成功");
				}
			} else {
				responseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
						ResponseEnum.SYS_FAILD.getDesc());
				responseInfo.setResMsg("该工号配置已存在！");
			}
		} catch (Exception e) {
			logger.error("划扣次数配置失败！");
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
		}
		return toResponseJSON(responseInfo);
	}
	/**
	 * 根据userCode更新划扣通道配置信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String update(HttpServletRequest request, HttpServletResponse response){
		ResponseInfo responseInfo = null;
		String userCode = request.getParameter("userCode");
		String offerCount = request.getParameter("offerCount");
		try{
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("userCode", userCode);
			paramMap.put("offerCount", offerCount);
		OfferNum	offerNum	= offerNumService.findOfferNumByCode(paramMap);
			
			if (offerNum != null) {
				offerNum.setOfferCount(offerCount );
				offerNum.setUserCode(userCode);
				 offerNumService.update(offerNum);
				this.createLog(request, SysActionLogTypeEnum.更新, "划扣次数", "划扣次数更新");
				// 正常返回
				responseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc());
				responseInfo.setResMsg("操作成功");
			} else {
				responseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
						ResponseEnum.SYS_FAILD.getDesc());
				responseInfo.setResMsg("该工号配置不存在！");
			}
		} catch (Exception e) {
			logger.error("划扣次数配置失败！");
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
		}
		return toResponseJSON(responseInfo);
	}
	
/**
	 * 根据userCode删除划扣通道配置信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(HttpServletRequest request, HttpServletResponse response){
		ResponseInfo responseInfo = null;
		String userCode = request.getParameter("userCode");
		try{
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("userCode", userCode);
     		OfferNum	offerNum	= offerNumService.findOfferNumByCode(paramMap);
			
			if (offerNum != null) {
				offerNum.setUserCode(userCode);
				offerNumService.delete(offerNum);
				this.createLog(request, SysActionLogTypeEnum.删除, "划扣次数", "划扣次数删除");
				// 正常返回
				responseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc());
				responseInfo.setResMsg("操作成功");
			} else {
				responseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
						ResponseEnum.SYS_FAILD.getDesc());
				responseInfo.setResMsg("该工号配置不存在！");
			}
		} catch (Exception e) {
			logger.error("划扣次数删除失败！");
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
		}
		return toResponseJSON(responseInfo);
	}
	
	
}
