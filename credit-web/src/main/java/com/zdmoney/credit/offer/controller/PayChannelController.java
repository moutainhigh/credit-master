package com.zdmoney.credit.offer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnumUtil;
import com.zdmoney.credit.common.constant.tpp.TppPaySysNoEnum;
import com.zdmoney.credit.common.constant.tpp.TppRealPaySysNoEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.offer.domain.OfferChannel;
import com.zdmoney.credit.offer.domain.PayChannel;
import com.zdmoney.credit.offer.service.pub.IPayChannelService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;

@Controller
@RequestMapping ("/payChannel")
public class PayChannelController extends BaseController{
	protected static Log logger = LogFactory.getLog(PayChannelController.class);
	@Autowired 
	ISysDictionaryService sysDictionaryServiceImpl;
	
	@Autowired 
	IPayChannelService payChannelService;
	
	/**
	 * 划扣通道配置页面初始化
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping ("/payChannelPage")
	public String getOfferChannelWithPageList(HttpServletRequest request, HttpServletResponse response,ModelMap model){
		model.addAttribute("fundsSources", FundsSourcesTypeEnum.values());
		model.addAttribute("paySysNos", new String []{TppPaySysNoEnum.快捷通.getValue(),TppPaySysNoEnum.通联代扣.getValue()});
		return "/offer/payChannelList";
	}
	
	/**
	 * 划扣通道配置查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping ("/payChannelPageList")
	@ResponseBody
	public String findOfferChannelPageList(HttpServletRequest request, HttpServletResponse response){
		int rows = Integer.parseInt(request.getParameter("rows"));
		int page = Integer.parseInt(request.getParameter("page"));
		String fundsSources = request.getParameter("fundsSources");
		if(Strings.isNotEmpty(fundsSources)){
			fundsSources = FundsSourcesTypeEnumUtil.getName(fundsSources);
		}
		String paySysNo = request.getParameter("paySysNo");
		if(Strings.isNotEmpty(paySysNo)){
			paySysNo = TppPaySysNoEnum.getString(paySysNo);
		}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("DESC");
		paramMap.put("pager", pager);
		paramMap.put("fundsSources", fundsSources);
		paramMap.put("paySysNo", paySysNo);
		pager = payChannelService.findWithPgByMap(paramMap);
		return toPGJSONWithCode(pager);
	}
	
	/**
	 * 更新或插入代付通道配置
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/saveOrUpdatePayChannel")
	@ResponseBody
	public String saveOrUpdateOfferChannel(HttpServletRequest request, HttpServletResponse response){
		ResponseInfo responseInfo = null;
		Long payChannelId = StringUtils.isNotEmpty(request.getParameter("payChannelId")) ? Long.valueOf(request.getParameter("payChannelId")) : null;
		String fundsSources = request.getParameter("fundsSourcesEdit");
		String paySysNo = request.getParameter("paySysNoEdit");
		if(Strings.isNotEmpty(paySysNo)){
			paySysNo = TppPaySysNoEnum.getString(paySysNo);
		}
		if(Strings.isNotEmpty(fundsSources)){
			fundsSources = FundsSourcesTypeEnumUtil.getName(fundsSources);
		}
		PayChannel payChannel = null;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		String reMsg = "";
		try {
			if(payChannelId == null){
				reMsg = "保存";
				paramMap.put("fundsSources", fundsSources);
				payChannel = payChannelService.findPayChannel(paramMap);
				if(payChannel != null){
					responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"该合同来源已配置过代付通道不能重复配置！");
					return toResponseJSON(responseInfo);
				}
				payChannel = new PayChannel();
				payChannel.setFundsSources(fundsSources);
				payChannel.setPaySysNo(paySysNo);
			}else{
				reMsg = "修改";
				payChannel = new PayChannel();
				payChannel.setId(payChannelId);
				payChannel.setPaySysNo(paySysNo);
			}
			payChannelService.saveOrUpdatePayChannel(payChannel);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),reMsg+"代付通道配置成功！");
		} catch (Exception e) {
			logger.error("代付通道配置失败！");
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
		}
		return toResponseJSON(responseInfo);
	}
	
	
}
