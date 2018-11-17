package com.zdmoney.credit.system.controller;

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

import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
import com.zdmoney.credit.system.service.pub.IProdCreditProductTermService;

@Controller
@RequestMapping("/system/ProdCreditProductTerm")
public class ProdCreditProductTermController extends BaseController {
	protected static Log logger = LogFactory
			.getLog(ComEmployeeController.class);

	@Autowired
	@Qualifier("prodCreditProductTermServiceImpl")
	IProdCreditProductTermService prodCreditProductTermService;

	@RequestMapping(value = "search")
	@ResponseBody
	public String search(ProdCreditProductTerm prodCreditProductTerm, int rows,
			int page, HttpServletRequest request, HttpServletResponse response) {
		// 定义分页实例
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("ASC");
		prodCreditProductTerm.setPager(pager);
		// 调用Service层查询数据
		pager = prodCreditProductTermService.findWithPg(prodCreditProductTerm);
		// 将数据对象转换成JSON字符串，返回给前台
		return toPGJSONWithCode(pager);
	}

	@RequestMapping(value = "saveOrUpdateProdCreditProductTerm")
	@ResponseBody
	public String saveOrUpdateProdCreditProductTerm(
			ProdCreditProductTerm prodCreditProductTerm,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<String> attachmentResponseInfo = null;

		try {
			ProdCreditProductTerm pt = prodCreditProductTermService
					.get(prodCreditProductTerm);
			if (pt == null) {
				prodCreditProductTermService
						.saveOrUpdate(prodCreditProductTerm);
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<String>(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(), "");
			} else {
				attachmentResponseInfo = new AttachmentResponseInfo<String>(
						ResponseEnum.SYS_EXIST.getCode(),
						ResponseEnum.SYS_EXIST.getDesc(), "");
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
	public String deleteProdCreditProductTermById(@PathVariable Long id,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<String> attachmentResponseInfo = null;

		try {
			prodCreditProductTermService.deleteById(id);
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

	@RequestMapping("/prodCreditProductTermList")
	public String prodCreditProductTermList(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("员工列表");
		return "/prodCreditProductTerm/prodCreditProductTerm";

	}

	@RequestMapping(value = "loadDataProdCreditProductTerm/{id}")
	@ResponseBody
	public String loadDataProdCreditProductTerm(@PathVariable Long id,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<ProdCreditProductTerm> attachmentResponseInfo = null;
		try {
			ProdCreditProductTerm prodCreditProductTerm = prodCreditProductTermService
					.get(id);
			if (prodCreditProductTerm == null) {
				/** 数据项为空 **/
				attachmentResponseInfo = new AttachmentResponseInfo<ProdCreditProductTerm>(
						ResponseEnum.VALIDATE_ISNULL.getCode(), Strings.format(
								ResponseEnum.VALIDATE_ISNULL.getDesc(), "编号:"
										+ id), Strings.convertValue(id,
								String.class));
			} else {
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<ProdCreditProductTerm>(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(),
						Strings.convertValue(id, String.class));
				attachmentResponseInfo.setAttachment(prodCreditProductTerm);
			}

		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<ProdCreditProductTerm>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(id,
							String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
}
