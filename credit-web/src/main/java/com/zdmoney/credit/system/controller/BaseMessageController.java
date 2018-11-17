package com.zdmoney.credit.system.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.domain.BaseMessage;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.service.pub.IBaseMessageService;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;

/**
 * 消息中心
 * @author Anfq
 *
 * 2015年7月21日
 */
@Controller
@RequestMapping("/system/baseMessage")
public class BaseMessageController extends BaseController {
	protected static Log logger = LogFactory.getLog(BaseMessageController.class);

	@Autowired
	@Qualifier("comEmployeeServiceImpl")
	IComEmployeeService comEmployeeServiceImpl;
	
	@Autowired
	IBaseMessageService  baseMessageServiceImpl;
	
	
	@RequestMapping("/message")
	public String message(HttpServletRequest request, HttpServletResponse response) {
		return "/message/message";
		
	}
	
	/**
	 * 默认页面展现数据
	 * @param baseMessage
	 * @param rows
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listAllMessage")	
	public String listMessage(BaseMessage baseMessage,int rows, int page,HttpServletRequest request, HttpServletResponse response){
		User user = UserContext.getUser();
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("send_time");
		pager.setSort("ASC");
		baseMessage.setPager(pager);
		baseMessage.setReceiver(user.getId());
		pager = baseMessageServiceImpl.findWithPg(baseMessage);	 
		return toPGJSON(pager);
		
	}

	
	
	/**
	 * 新增 修改
	 * @param myTest
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/insert")
	@ResponseBody
	public String insert(BaseMessage baseMessage,HttpServletRequest request,HttpServletResponse response) {
		AttachmentResponseInfo<BaseMessage> attachmentResponseInfo = null;
		try {
			User user = UserContext.getUser();
			baseMessage.setSender(user.getId());
			baseMessage.setSendTime(new Date());
			baseMessage.setState("1");//未读
			baseMessageServiceImpl.inserBaseMessage(baseMessage);
			
			//正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<BaseMessage>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
			attachmentResponseInfo.setAttachment(baseMessage);
		} catch(Exception ex) {
			//系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<BaseMessage>(ResponseEnum.SYS_FAILD.getCode(),ResponseEnum.SYS_FAILD.getDesc(),Strings.convertValue(baseMessage.getId(),String.class));
			attachmentResponseInfo.setAttachment(baseMessage);
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	/**
	 * 获取所有员工数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getComEmployeeList")	
	public String getComEmployeeList(){
		List<ComEmployee> list=	comEmployeeServiceImpl.getComEmployeeList();
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();		
		for(ComEmployee  obj :list ){
			Map<String,String> map=new HashMap<String,String>();
			map.put("id", obj.getId().toString());
			//因为名字不具有唯一性，，改为工号
			map.put("name", obj.getUsercode());
			//map.put("name", obj.getName());
			
			result.add(map);
		}
		AttachmentResponseInfo<List<Map<String,String>>> attachmentResponseInfo = null;		
		attachmentResponseInfo = new AttachmentResponseInfo<List<Map<String,String>>>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
		attachmentResponseInfo.setAttachment(result);
		return toResponseJSON(attachmentResponseInfo);
	}
	
	/**
	 * 修改成未读状态
	 * @param ids
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updatebaseMessageByState")	
	public String updatebaseMessageByState(@RequestParam("ids")  String[] ids,HttpServletRequest request, HttpServletResponse response) {	
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;		
		if(null != ids && ids.length>0){
			List<Long> list = new ArrayList<Long>();
			for(String idStr : ids){
				Long msgId = Long.valueOf(idStr);
				list.add(msgId);
			}
			baseMessageServiceImpl.batchUpdateBaseMessageState(list);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
		}
		return toResponseJSON(attachmentResponseInfo);
		
	}
	
	/**
	 * 删除状态
	 * @param ids
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")	
	public String updatebaseMessageByStateDelete(@RequestParam("ids") String[] ids,HttpServletRequest request, HttpServletResponse response) {	
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;		
		if(null != ids && ids.length>0){
			List<Long> list = new ArrayList<Long>();
			for(String idStr : ids){
				Long msgId = Long.valueOf(idStr);
				list.add(msgId);
			}
			baseMessageServiceImpl.batchUpdateBaseMessageDelete(list);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
		}
		return toResponseJSON(attachmentResponseInfo);
		
	}
	
	/**
	 * 查看状态
	 * @param ids
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listOneMessage")	
	public String listMessageById(@RequestParam("ids") String[] ids,HttpServletRequest request, HttpServletResponse response) {	
		AttachmentResponseInfo<BaseMessage> attachmentResponseInfo = null;
		if(null != ids && ids.length>0){
			List<Long> list = new ArrayList<Long>();
			for(String idStr : ids){
				Long msgId = Long.valueOf(idStr);
				list.add(msgId);
			}
			baseMessageServiceImpl.batchUpdateBaseMessageView(list);
			BaseMessage baseMessage=baseMessageServiceImpl.get(Long.valueOf(ids[0]));	
			SimpleDateFormat dateFormat = new SimpleDateFormat(Dates.DEFAULT_DATETIME_FORMAT);    
			baseMessage.setSendTimeStr(dateFormat.format(baseMessage.getSendTime()));
			attachmentResponseInfo = new AttachmentResponseInfo<BaseMessage>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
			attachmentResponseInfo.setAttachment(baseMessage);
		}
	return toResponseJSON(attachmentResponseInfo);
}
	/**
	 * 查看未读的消息条数
	 * @param ids
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listCount")	
	public String listCount(HttpServletRequest request, HttpServletResponse response) {	
		HttpSession session = request.getSession();
		User user = UserContext.getUser();
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		BaseMessage baseMessage=new BaseMessage();
		baseMessage.setReceiver(user.getId());
		int messageCount=	baseMessageServiceImpl.countNoView(baseMessage);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
			attachmentResponseInfo.setAttachment(messageCount);
	return toResponseJSON(attachmentResponseInfo);
}
	
}
