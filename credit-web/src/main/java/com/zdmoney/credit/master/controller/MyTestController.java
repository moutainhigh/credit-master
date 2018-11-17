package com.zdmoney.credit.master.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.master.domain.MyTest;
import com.zdmoney.credit.master.service.pub.IMyTestService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 测试示例 前端请求处理
 * @author Ivan
 *
 */
@Controller
@RequestMapping(value="/master/myTest")
public class MyTestController extends BaseController {
	
	@Autowired @Qualifier("myTestServiceImpl")
	IMyTestService myTestServiceImpl;
	
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
	/**
	 * 查询目标表数据
	 * @param myTest 前端查询条件实例
	 * @param rows 每页总行
	 * @param page 查询的页数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="search")
	@ResponseBody
	public String search(MyTest myTest,int rows, int page,HttpServletRequest request,HttpServletResponse response) {
		System.out.println("seq:" + sequencesServiceImpl.getSequences(SequencesEnum.MY_TEST));
		//定义分页实例
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("ASC");
		myTest.setPager(pager);
		//调用Service层查询数据
		pager = myTestServiceImpl.findWithPg(myTest);
		//将数据对象转换成JSON字符串，返回给前台
		return toPGJSONWithCode(pager);
	}
	
	/**
	 * 新增 修改
	 * @param myTest
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="saveOrUpdateData")
	@ResponseBody
	public String saveOrUpdateData(MyTest myTest,HttpServletRequest request,HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {
			myTestServiceImpl.saveOrUpdate(myTest);
			//正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode()
					,ResponseEnum.SYS_SUCCESS.getDesc(),"");
		} catch(Exception ex) {
			//系统忙
			logger.error(ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),Strings.convertValue(myTest.getId(),String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
	/**
	 * 加载数据
	 * @param id 记录主键编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="loadData/{id}")
	@ResponseBody
	public String loadData(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) {
		AttachmentResponseInfo<MyTest> attachmentResponseInfo = null;
		try {
			MyTest myTest = myTestServiceImpl.get(id);
			if (myTest == null) {
				/** 数据项为空 **/
				attachmentResponseInfo = new AttachmentResponseInfo<MyTest>(ResponseEnum.VALIDATE_ISNULL.getCode()
						,Strings.format(ResponseEnum.VALIDATE_ISNULL.getDesc(), "编号:" + id),Strings.convertValue(id,String.class));
			} else {
				//正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<MyTest>(ResponseEnum.SYS_SUCCESS.getCode()
						,ResponseEnum.SYS_SUCCESS.getDesc(),Strings.convertValue(id,String.class));
				attachmentResponseInfo.setAttachment(myTest);
			}
		} catch(Exception ex) {
			//系统忙
			logger.error(ex);
			attachmentResponseInfo = new AttachmentResponseInfo<MyTest>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),Strings.convertValue(id,String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
	/**
	 * 删除数据
	 * @param id 记录主键编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="deleteData/{id}")
	@ResponseBody
	public String deleteData(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) {
		AttachmentResponseInfo<MyTest> attachmentResponseInfo = null;
		try {
			MyTest myTest = myTestServiceImpl.get(id);
			if (myTest == null) {
				/** 数据项为空 **/
				attachmentResponseInfo = new AttachmentResponseInfo<MyTest>(ResponseEnum.VALIDATE_ISNULL.getCode()
						,Strings.format(ResponseEnum.VALIDATE_ISNULL.getDesc(), "编号:" + id),Strings.convertValue(id,String.class));
			} else {
				myTestServiceImpl.deleteById(id);
				//正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<MyTest>(ResponseEnum.SYS_SUCCESS.getCode()
						,ResponseEnum.SYS_SUCCESS.getDesc(),Strings.convertValue(id,String.class));
				attachmentResponseInfo.setAttachment(myTest);
			}
		} catch(Exception ex) {
			//系统忙
			logger.error(ex);
			attachmentResponseInfo = new AttachmentResponseInfo<MyTest>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),Strings.convertValue(id,String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
}