package com.zdmoney.credit.loan.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.TradeTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.excel.ExcelTemplet.LoanReturnWCExcel;
import com.zdmoney.credit.common.util.excel.ExcelTemplet.ManagerSalesDeptInputExcel;
import com.zdmoney.credit.common.util.excel.ExcelTemplet.RepaymentInputExcel;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanReturn;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.loan.vo.LoanReturnVo;
import com.zdmoney.credit.loan.vo.TotalReturnLoanInfo;
import com.zdmoney.credit.offer.vo.RepaymentInputVo;
import com.zdmoney.credit.person.service.pub.IPersonTelInfoService;
import com.zdmoney.credit.system.domain.ComMessageIn;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IComMessageInService;
import com.zdmoney.credit.system.service.pub.ILoanReturnService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 随手记债权导入
 * 
 * @author zhangln
 *
 * 
 */
@Controller
@RequestMapping("/loan")
public class LoanSsjController extends BaseController {
	@Autowired
	private ILoanReturnService loanReturnService;
	@Autowired
	ILoanBaseService loanBaseService;

	@Autowired
	private IVLoanInfoService vLoanInfoService;

	@Autowired
	private ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;
	@Autowired
	private ISysParamDefineService sysParamDefineService;

	@Autowired
	private IPersonTelInfoService personTelInfoService;

	@Autowired
	@Qualifier("comMessageInServiceImpl")
	IComMessageInService comMessageInServiceImpl;

	@Autowired
	@Qualifier("personInfoServiceImpl")
	IPersonInfoService personInfoServiceImpl;

	public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@RequestMapping("/loanSsjDefault")
	public String loanReturnDefault(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		return "/loan/loanSsjDefault";

	}

	

	/**
	 * 随手记导入债权模板
	 * 
	 * @param file
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("/loanSsjImport")
	public String loanSsjImport(
			@RequestParam(value = "uploadfile") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {

		String filePath = sysParamDefineService.getSysParamValueCache(
				"download", "ssj.dir");
		File files = new File(filePath);
		if (!files.exists() && !files.isDirectory()) {
			files.mkdirs();
		}
		if (files.isDirectory()) {
			String[] filelist = files.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(filePath.toString() + "/"
						+ filelist[i]);
				if (!readfile.isDirectory()) {
					readfile.delete();
				}
			}

		}

		Map<String, Object> map = new HashMap<String, Object>();
		int digMoneyTotal = 0;
		int successBeans = 0;
		int failBeans = 0;
		int errorTotal = 0;
		BigDecimal sumMoney = new BigDecimal(0.00);
		BigDecimal successMoney = new BigDecimal(0.00);
		BigDecimal faileMoney = new BigDecimal(0.00);
		map.put("digMoneyTotal", digMoneyTotal);
		map.put("successBeans", successBeans);
		map.put("failBeans", failBeans);
		map.put("sumMoney", sumMoney);
		map.put("successMoney", successMoney);
		map.put("faileMoney", faileMoney);
		map.put("errorTotal", errorTotal);
		AttachmentResponseInfo<Map<String, Object>> attachmentResponseInfo = null;
		try {
			// 文件校验
			UploadFile uploadFile = new UploadFile();
			uploadFile.setFile(file);
			uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
			uploadFile.setFileMaxSize(1024 * 1024 * 50);
			UploadFileUtil.valid(uploadFile);
			// 创建excel工作表
			InputStream in = new BufferedInputStream(file.getInputStream());
			Workbook workbook = WorkbookFactory.create(in);
			// 创建导入数据模板
			ExcelTemplet excelTemplet = new ExcelTemplet().new LoanSsjExcel();
			// 文件数据转换为List集合
			List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(
					workbook, excelTemplet);
			processOneLineData(sheetDataList, map);
			map.put("fileName", file.getOriginalFilename());
			String fileName = file.getOriginalFilename();
			FileOutputStream outputStream = new FileOutputStream(filePath + "/"
					+ fileName);

			ExcelUtil
					.addResultToWorkBook(workbook, sheetDataList, excelTemplet);
			try {
				workbook.write(outputStream);
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setAttachment(map);

		}  catch (PlatformException e) {
			attachmentResponseInfo =  new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
			attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc());
			attachmentResponseInfo.setAttachment(map);
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 数据校验
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 */
	public void processOneLineData(List<Map<String, String>> sheetDataList,
			Map<String, Object> mapValue) {
		int successBeans = 0;
		int failBeans = 0;
		BigDecimal sumMoney = new BigDecimal(0.00);
		BigDecimal successMoney = new BigDecimal(0.00);
		BigDecimal faileMoney = new BigDecimal(0.00);

		if (CollectionUtils.isEmpty(sheetDataList)) {
			mapValue.put("digMoneyTotal", 0);
			return;
		} else {
			mapValue.put("digMoneyTotal", sheetDataList.size());
		}
		for (Map<String, String> map : sheetDataList) {// 每一行记录遍历
			StringBuffer error = new StringBuffer();
			String name = Strings.convertValue(map.get("borrowerName"),
					String.class);
			// 借款人身份证号码
			String idNum = Strings.convertValue(map.get("idNum"), String.class);
			String zrMoney = Strings.convertValue(map.get("zrMoney"),
					String.class);
			// 还款起始日期
			String startDate = Strings.convertValue(map.get("startDate"),
					String.class);
			if (Strings.isEmpty(zrMoney)) {
				error.append("本次转让债权价值不能为空!");
				failBeans++;
				zrMoney = "0";
				sumMoney = sumMoney.add(new BigDecimal(zrMoney));
				mapValue.put("sumMoney", sumMoney);
				map.put("feedBackMsg", error.toString());
				continue;
			}
			if (Strings.isEmpty(name)) {
				error.append("姓名不能为空!");
				failBeans++;
				sumMoney = sumMoney.add(new BigDecimal(zrMoney));
				mapValue.put("sumMoney", sumMoney);
				map.put("feedBackMsg", error.toString());
				continue;
			}
			if (Strings.isEmpty(idNum)) {
				error.append("身份证号不能为空!");
				failBeans++;
				sumMoney = sumMoney.add(new BigDecimal(zrMoney));
				mapValue.put("sumMoney", sumMoney);
				map.put("feedBackMsg", error.toString());
				continue;
			}
			if (Strings.isEmpty(startDate)) {
				error.append("还款起始日期不能为空!");
				failBeans++;
				sumMoney = sumMoney.add(new BigDecimal(zrMoney));
				mapValue.put("sumMoney", sumMoney);
				map.put("feedBackMsg", error.toString());
				continue;
			}
			sumMoney = sumMoney.add(new BigDecimal(zrMoney));// 得到债权转让总值

			mapValue.put("sumMoney", sumMoney);

			PersonInfo personInfo = personInfoServiceImpl.findByIdNumAndName(
					name, idNum);
			if (personInfo == null) {
				map.put("feedBackMsg", "导入失败,借款人不存在");
				failBeans++;
				faileMoney = faileMoney.add(new BigDecimal(zrMoney));
				mapValue.put("failBeans", failBeans);
				mapValue.put("faileMoney", faileMoney);
			} else {
				Map<String, Object> loanParams = new HashMap<String, Object>();
				loanParams.put("idNum", idNum.trim());
				loanParams.put("startDate", startDate.trim());
				List<VLoanInfo> loanInfoList = vLoanInfoService
						.getVLoanByMap(loanParams);
				if (null == loanInfoList || loanInfoList.size() == 0) {
					map.put("feedBackMsg", "导入失败,债权不存在");
					failBeans++;
					faileMoney = faileMoney.add(new BigDecimal(zrMoney));
					mapValue.put("failBeans", failBeans);
					mapValue.put("faileMoney", faileMoney);
				} else if (loanInfoList.size() >= 2) {
					map.put("feedBackMsg", "导入失败,不能同时存在两笔债权");
					failBeans++;
					faileMoney = faileMoney.add(new BigDecimal(zrMoney));
					mapValue.put("failBeans", failBeans);
					mapValue.put("faileMoney", faileMoney);
				} else {
					successBeans++;
					successMoney = successMoney.add(new BigDecimal(zrMoney));
					mapValue.put("successBeans", successBeans);
					mapValue.put("successMoney", successMoney);
					map.put("feedBackMsg", "导入成功");
				}
			}
		}

	}

	@RequestMapping("/downSsj")
	@ResponseBody
	public void downSsj(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("fileName") String fileName) {
		OutputStream outStream = null;
		InputStream inputStream = null;
		try {
			// 存放文件的目录
			String filePath = sysParamDefineService.getSysParamValueCache(
					"download", "ssj.dir");
			response.reset();
			response.setHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(fileName, "UTF-8"));
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			File file = new File(filePath + "/" + fileName);
			outStream = response.getOutputStream();
			inputStream = new FileInputStream(file);
			byte[] buffer = new byte[2048];
			int count = -1; // 每次读取字节数
			while ((count = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, count);
			}
			outStream.flush();
			outStream.close();
			inputStream.close();
		} catch (IOException e) {

			logger.error("下载文件失败：", e);
		} finally {
			try {
				if (null != outStream) {
					outStream.close();
				}
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (Exception e2) {
			}

		}
	}

	@RequestMapping("/updateBatchNumSsj")
	@ResponseBody
	public String updateBatchNumSsj(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("fileName") String fileName) {
		AttachmentResponseInfo<Map<String, Object>> attachmentResponseInfo = null;
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 存放文件的目录
			String filePath = sysParamDefineService.getSysParamValueCache(
					"download", "ssj.dir");
			File file = new File(filePath + "/" + fileName);
			FileInputStream fis = new FileInputStream(file);
			InputStream in = new BufferedInputStream(fis);
			Workbook workbook = WorkbookFactory.create(in);
			ExcelTemplet excelTemplet = new ExcelTemplet().new LoanSsjExcel();
			// 文件数据转换为List集合
			List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(
					workbook, excelTemplet);

			List<Long> list = new ArrayList<Long>();
			for (Map<String, String> map : sheetDataList) {// 每一行记录遍历
				Map<String, Object> loanParams = new HashMap<String, Object>();
				loanParams.put("idNum", map.get("idNum").toString().trim());
				/*loanParams.put("name", map.get("borrowerName").toString()
						.trim());*/
				loanParams.put("startDate", map.get("startDate").toString().trim());
				List<VLoanInfo> loanInfoList = vLoanInfoService
						.getVLoanByMap(loanParams);
				if(null==loanInfoList || loanInfoList.size() == 0){
					attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(
							ResponseEnum.SYS_FAILD.getCode(), "生成批次号失败");
					return toResponseJSON(attachmentResponseInfo);
				}else if(loanInfoList.size() >= 2){
					attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(
							ResponseEnum.SYS_FAILD.getCode(), "生成批次号失败");
					return toResponseJSON(attachmentResponseInfo);
				}else{
					for (VLoanInfo vLoanInfo : loanInfoList) {
						
						Long loanId = vLoanInfo.getId();
						list.add(loanId);
					}
				}
			}
			paramMap.put("list", list);
			paramMap.put("org", "SSJ");
			int result = vLoanInfoService.updateBatchNum(paramMap);

			if (result > 0) {
				file.delete();
				attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(), "");
				return toResponseJSON(attachmentResponseInfo);
			} else {
				attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(
						ResponseEnum.SYS_FAILD.getCode(), "生成批次号失败");
				return toResponseJSON(attachmentResponseInfo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(
					ResponseEnum.SYS_FAILD.getCode(), "生成批次号失败");
			return toResponseJSON(attachmentResponseInfo);
		}

	}
}
