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
import com.zdmoney.credit.common.constant.tpp.TppRealPaySysNoEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.offer.domain.OfferChannel;
import com.zdmoney.credit.offer.service.pub.IOfferChannelService;
import com.zdmoney.credit.system.domain.SysDictionary;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;

@Controller
@RequestMapping ("/offerChannel")
public class OfferChannelController extends BaseController{
	protected static Log logger = LogFactory.getLog(OfferChannelController.class);
	@Autowired 
	ISysDictionaryService sysDictionaryServiceImpl;
	
	@Autowired 
	IOfferChannelService offerChannelService;
	
	/**
	 * 划扣通道配置页面初始化
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping ("/offerChannelPageInit")
	public String getOfferChannelWithPageList(HttpServletRequest request, HttpServletResponse response,ModelMap model){
		/** 查询债权去向数据源 **/
		SysDictionary sysDictionary = new SysDictionary();
		sysDictionary.setCodeType("loan_loanbelongs");
		List<SysDictionary> list = sysDictionaryServiceImpl.findDictionaryListByVo(sysDictionary);
		model.addAttribute("loanBelongs", list);
		/** 划扣通道枚举 **/
		model.addAttribute("paySysNos", TppRealPaySysNoEnum.values());
		return "/offer/offerChannelList";
	}
	
	/**
	 * 划扣通道配置查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping ("/offerChannelList")
	@ResponseBody
	public String getOfferChannelPageList(HttpServletRequest request, HttpServletResponse response){
		int rows = Integer.parseInt(request.getParameter("rows"));
		int page = Integer.parseInt(request.getParameter("page"));
		String loanBelong = request.getParameter("loanBelong");
		String paySysNo = request.getParameter("paySysNo");
		String state = request.getParameter("state");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("DESC");
		paramMap.put("pager", pager);
		paramMap.put("loanBelong", loanBelong);
		paramMap.put("paySysNo", paySysNo);
		paramMap.put("state", state);
		pager = offerChannelService.findWithPgByMap(paramMap);
		return toPGJSONWithCode(pager);
	}
	
	/**
	 * 更新或插入划扣通道配置
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/saveOrUpdateOfferChannel")
	@ResponseBody
	public String saveOrUpdateOfferChannel(HttpServletRequest request, HttpServletResponse response){
		ResponseInfo responseInfo = null;
		Long id = StringUtils.isNotEmpty(request.getParameter("id")) ? Long.valueOf(request.getParameter("id")) : null;
		String loanBelong = request.getParameter("loanBelong");//捞财宝
		String paySysNo = request.getParameter("paySysNo");//10
		String state = request.getParameter("state");
		String type = request.getParameter("type");
		OfferChannel offerChannel = null;
		//更新时需三个参数一同校验，新增只需校验债权去向和划扣通道入参
		try {
			Map<String,Object> paramMap = new HashMap<String,Object>();
			if("1".equals(type)){
				paramMap.put("paySysNo", paySysNo);
				paramMap.put("loanBelong", loanBelong);
				offerChannel = offerChannelService.findOfferChannelByPaySysNoAndLoanBelong(paramMap);
				if(paySysNo.equals(TppRealPaySysNoEnum.爱特代扣.getCode())){
					if(!loanBelong.equals("捞财宝")){
						responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"此债权去向暂不支持划扣通道为爱特代扣！");
						return toResponseJSON(responseInfo);
					}
				}
				
				if(loanBelong.equals(FundsSourcesTypeEnum.捞财宝.getValue())){
					if(!paySysNo.equals(TppRealPaySysNoEnum.爱特代扣.getCode()) && !paySysNo.equals(TppRealPaySysNoEnum.通联代扣.getCode())&&!paySysNo.equals(TppRealPaySysNoEnum.快捷通.getCode())&&!paySysNo.equals(TppRealPaySysNoEnum.上海银联支付.getCode())){
						responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"此债权去向的划扣通道仅支持爱特代扣、上海银联支付、通联代扣和快捷通！");
						return toResponseJSON(responseInfo);
					}
				}
				if(offerChannel != null){
					responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"该合同来源已配置过该通道不能重复配置！");
					return toResponseJSON(responseInfo);
				}
				offerChannel = new OfferChannel();
				offerChannel.setLoanBelong(loanBelong);
				offerChannel.setPaySysNo(paySysNo);
				offerChannel.setState(state);
			}else{
				offerChannel = new OfferChannel();
				offerChannel.setId(id);
				offerChannel.setState(state);
			}
			offerChannelService.saveOrUpdateOfferChannel(offerChannel);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
		} catch (Exception e) {
			logger.error("划扣通道配置失败！");
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
		}
		return toResponseJSON(responseInfo);
	}
	/**
	 * 根据id查询划扣通道配置信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/offerChannelById")
	@ResponseBody
	public String getOfferChannelById(HttpServletRequest request, HttpServletResponse response){
		AttachmentResponseInfo<OfferChannel> attachmentResponseInfo = null;
		try {
			Long id = Long.valueOf(request.getParameter("id"));
			OfferChannel offerChannel = offerChannelService.findOfferChannelById(id);
			attachmentResponseInfo = new AttachmentResponseInfo<OfferChannel>(ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setAttachment(offerChannel);
		} catch (Exception e) {
			// 系统忙
			logger.error(e, e);
			attachmentResponseInfo = new AttachmentResponseInfo<OfferChannel>(ResponseEnum.SYS_FAILD.getCode(),
				ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
}
