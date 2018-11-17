package com.zdmoney.credit.system.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.domain.ProdCreditProductInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
import com.zdmoney.credit.system.service.pub.IProdCreditProductInfoService;

@Controller
@RequestMapping("/system/ProdCreditProductInfo")
public class ProdCreditProductInfoController extends BaseController{
	protected static Log logger = LogFactory
			.getLog(ComEmployeeController.class);

	
	@Autowired
	@Qualifier("prodCreditProductInfoServiceImpl")
	IProdCreditProductInfoService prodCreditProductInfoService;
	
	@RequestMapping(value = "findProdCreditProductInfoList")
	@ResponseBody
	public String findProdCreditProductInfoList(ProdCreditProductInfo prodCreditProductInfo,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<List<ProdCreditProductInfo> > attachmentResponseInfo = null;
		try {
			List<ProdCreditProductInfo> list=new ArrayList<ProdCreditProductInfo>();
			 list=prodCreditProductInfoService.findProdCreditProductInfoList();
			 this.createLog(request, SysActionLogTypeEnum.查询, "产品期限配置", "产品期限配置查询");
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<List<ProdCreditProductInfo> >(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(), "");
				attachmentResponseInfo.setAttachment(list);
		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<List<ProdCreditProductInfo> >(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	@RequestMapping("/ProdCreditProductInfoList")
	public String ProdCreditProductInfoList(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("产品期限配置列表");
		return "/prodCreditProductTerm/prodCreditProductInfo";

	}
	@RequestMapping(value = "search")
	@ResponseBody
	public String search(ProdCreditProductInfo prodCreditProductInfo, int rows,
			int page, HttpServletRequest request, HttpServletResponse response) {
		// 定义分页实例
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		prodCreditProductInfo.setPager(pager);
		// 调用Service层查询数据
		pager = prodCreditProductInfoService.findWithPg(prodCreditProductInfo);
		this.createLog(request, SysActionLogTypeEnum.查询, "产品期限配置", "产品期限配置查询");
		// 将数据对象转换成JSON字符串，返回给前台
		return toPGJSONWithCode(pager);
	}
	@RequestMapping(value = "saveOrUpdateProdCreditProductInfo")
	@ResponseBody
	public String saveOrUpdateProdCreditProductInfo(
			ProdCreditProductInfo prodCreditProductInfo,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<String> attachmentResponseInfo = null;

		try {
			ProdCreditProductInfo pro=	prodCreditProductInfoService.getIsThere(prodCreditProductInfo);
			if(pro==null){
				prodCreditProductInfoService
						.saveOrUpdate(prodCreditProductInfo);
				this.createLog(request, SysActionLogTypeEnum.更新, "产品期限配置", "产品期限配置新增或者修改");
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<String>(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(), "");
				attachmentResponseInfo.setResMsg("操作成功");
			}else
			{
				attachmentResponseInfo = new AttachmentResponseInfo<String>(
						ResponseEnum.SYS_EXIST.getCode(),
						ResponseEnum.SYS_EXIST.getDesc(), "");
				attachmentResponseInfo.setResMsg("产品已经存在，请重新输入");
			}
		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<String>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	@RequestMapping(value = "deleteProdCreditProductTermById/{id}")
	@ResponseBody
	public String deleteProdCreditProductTermById(@PathVariable String id,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<String> attachmentResponseInfo = null;

		try {
			ProdCreditProductInfo	prodCreditProductInfo=new ProdCreditProductInfo();
			String ids[]=id.split(",");
			id=ids[0];
			String termId=ids[1];
			ProdCreditProductTerm prodCreditProductTerm=new ProdCreditProductTerm();
			prodCreditProductTerm.setId(Long.parseLong(termId));
			prodCreditProductInfo.setId(Long.parseLong(id));
			prodCreditProductInfo.setTerm(prodCreditProductTerm);
			prodCreditProductInfoService.deleteById(prodCreditProductInfo);
			this.createLog(request, SysActionLogTypeEnum.查询, "产品期限配置", "产品期限配置删除");
			// 正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<String>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setResMsg("删除成功");
		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<String>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
	@RequestMapping(value = "loadDataProdCreditProductInfo/{id}")
	@ResponseBody
	public String loadDataProdCreditProductInfo(@PathVariable String id,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<ProdCreditProductInfo> attachmentResponseInfo = null;
		try {
			String ids[]=id.split(",");
			id=ids[0];
			String termId=ids[1];
			ProdCreditProductTerm prodCreditProductTerm=new ProdCreditProductTerm();
			prodCreditProductTerm.setId(Long.parseLong(termId));
			prodCreditProductTerm.setProductId(Long.parseLong(id));
			ProdCreditProductInfo prodCreditProductInfo = prodCreditProductInfoService
					.findById(prodCreditProductTerm);
			if (prodCreditProductInfo == null) {
				/** 数据项为空 **/
				attachmentResponseInfo = new AttachmentResponseInfo<ProdCreditProductInfo>(
						ResponseEnum.VALIDATE_ISNULL.getCode(), Strings.format(
								ResponseEnum.VALIDATE_ISNULL.getDesc(), "编号:"
										+ id), Strings.convertValue(id,
								String.class));
			} else {
				this.createLog(request, SysActionLogTypeEnum.查询, "产品期限配置", "产品期限配置查询");
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<ProdCreditProductInfo>(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(),
						Strings.convertValue(id, String.class));
				attachmentResponseInfo.setAttachment(prodCreditProductInfo);
			}

		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<ProdCreditProductInfo>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(id,
							String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
}
