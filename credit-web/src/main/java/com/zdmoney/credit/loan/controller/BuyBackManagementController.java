package com.zdmoney.credit.loan.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.tpp.ReturnCodeEnum;
import com.zdmoney.credit.common.constant.tpp.TppPaySysNoEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanReturnRecord;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.loan.vo.LoanReturnVo;
import com.zdmoney.credit.offer.vo.OfferReturnVo;
import com.zdmoney.credit.person.service.pub.IPersonTelInfoService;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.ILoanReturnRecordService;
import com.zdmoney.credit.system.service.pub.ILoanReturnService;

/**
 * @author 10098  2017年2月20日 下午5:04:54
 */
@Controller
@RequestMapping(value="/buyBack")
public class BuyBackManagementController extends BaseController {

	public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	
	@Autowired
	private ILoanReturnService loanReturnService;
	@Autowired
	ILoanBaseService loanBaseService;
    @Autowired
    private IPersonTelInfoService personTelInfoService;
	@Autowired
	private IVLoanInfoService vLoanInfoService;
	@Autowired 
	private ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;
	@Autowired
	private ILoanReturnRecordService loanReturnRecordService;
	
	@RequestMapping(value="/importBuyBackLoanPage")
	public ModelAndView inportBuyBackLoanPage(HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView();
		ModelMap modelMap = modelAndView.getModelMap();
		modelMap.put("loanSources", new String[]{FundsSourcesTypeEnum.挖财2.getValue(), FundsSourcesTypeEnum.外贸信托.getValue()
				, FundsSourcesTypeEnum.国民信托.getValue(), FundsSourcesTypeEnum.华澳信托.getValue()
				,FundsSourcesTypeEnum.渤海信托.getValue(), FundsSourcesTypeEnum.渤海2.getValue()
				, FundsSourcesTypeEnum.华瑞渤海.getValue(), FundsSourcesTypeEnum.龙信小贷.getValue()
				, FundsSourcesTypeEnum.包商银行.getValue(),FundsSourcesTypeEnum.外贸3.getValue(),FundsSourcesTypeEnum.陆金所.getValue()});
		modelMap.put("loanBelongs", new String[]{FundsSourcesTypeEnum.证大P2P.getValue()});
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value="/buyBackLoanList")
	public String buyBackLoanList(LoanReturnVo loanReturnVo,int rows, int page, HttpServletRequest request, HttpServletResponse response){
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("pager", pager);
		paramMap.put("buyBackDateStart", loanReturnVo.getStart());
		paramMap.put("buyBackDateEnd", loanReturnVo.getEnd());
		paramMap.put("importDateStart", loanReturnVo.getImportStart());
		paramMap.put("importDateEnd", loanReturnVo.getImportEnd());
		paramMap.put("idnum", loanReturnVo.getIdnum());
		paramMap.put("fundsSources", loanReturnVo.getFundsSources());
		paramMap.put("loanBelongs", loanReturnVo.getLoanBelongs());
		paramMap.put("contractNum", loanReturnVo.getContractNum());
		pager = loanBaseService.findBuyBackLoanWithPg(paramMap);
		return toPGJSONWithCode(pager);
	}
	
	/**
	 * 债权回购导入
	 * @param file
	 * @param request
	 * @param response
	 */
	
	@ResponseBody
	@RequestMapping("/buyBackLoanImport")	
	public void buyBackLoanImport(@RequestParam(value = "uploadfile") MultipartFile uploadfile,   HttpServletRequest request, HttpServletResponse response) {
		Workbook workbookNew = null;
        ResponseInfo responseInfo = null;	       
        try {
            // 文件校验
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(uploadfile);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 50);
            UploadFileUtil.valid(uploadFile);
            // 创建excel工作表
            InputStream in = new BufferedInputStream(uploadfile.getInputStream());
            Workbook workbook = WorkbookFactory.create(in);
            // 创建导入数据模板
            ExcelTemplet excelTemplet = new ExcelTemplet().new BuyBackLoanExcel();
            // 文件数据转换为List集合
            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
            processOneLine(sheetDataList); 	           
            //验证和保存借款数据	         
            String fileName = uploadfile.getOriginalFilename();
            String[] labels = new String[] { "合同编号", "身份证号", "回购时间", "回购金额", "合同来源", "债权去向","导入结果", "导入详情"};
            String[] fields = new String[] {"contractNum", "idNum", "buyBackTime", "amount", "fundsSources", "loanBelongs","result", "remark"};	            
            // 工作表名称
            String sheetName = "Sheet1";
            // 创建工作簿
             workbookNew  = ExcelExportUtil.createExcelByMapString(fileName, labels, fields, sheetDataList, sheetName);    
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbookNew);	            
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
          
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }finally {
            if (null != workbookNew) {
                try {
                	workbookNew.close();
                } catch (Exception e) {
                    responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
                }
            }
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
	
	/**
	 * 债权回购导入 数据逻辑
	 * @param sheetDataList
	 */
    private void processOneLine(List<Map<String, String>> sheetDataList) {
    	for(Map<String, String> map:sheetDataList){
    		try{
	    		String contractNum = map.get("contractNum");//合同编号
	    		String idNum = map.get("idNum"); // 身份证号
	    		String buyBackTime = map.get("buyBackTime"); //回购时间
	    		String amount = map.get("amount"); //回购金额
	    		String fundsSources = map.get("fundsSources"); // 合同来源
	    		String loanBelongs = map.get("loanBelongs"); //债权去向
	    		if(Strings.isEmpty(contractNum)){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "合同号不能为空");
	    		}
	    		if(Strings.isEmpty(idNum)){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "身份证号不能为空");
	    		}
	    		if(Strings.isEmpty(buyBackTime)){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "回购时间不能为空");
	    		}else if (!buyBackTime.matches("\\d{4}-\\d{2}-\\d{2}")) {
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "回购时间格式应为yyyy-MM-dd");
	            }
	    		Date formatDate = Dates.getDateByString(buyBackTime, Dates.DEFAULT_DATE_FORMAT);
	    		if(!buyBackTime.equals(Dates.getDateTime(formatDate, Dates.DEFAULT_DATE_FORMAT))){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "回购时间格式不正确");
	    		}
	    		if(formatDate.after(new Date())){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "回购时间不可大于今天");
	    		}
	    		if(Strings.isEmpty(amount)){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "回购金额不能为空");
	    		}
	    		BigDecimal am = null;
	    		try {
                    am  = new BigDecimal(amount);
                } catch (Exception e) {
                    throw new PlatformException(ResponseEnum.FULL_MSG,"回购金额类型不正确");
                }
	    		if(am.compareTo(new BigDecimal(0))<0){
	    			throw new PlatformException(ResponseEnum.FULL_MSG,"回购金额不可为负数");
	    		}
	    		if(Strings.isEmpty(fundsSources)){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "合同来源不能为空");
	    		}
	    		if(Strings.isEmpty(loanBelongs)){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "债权去向不能为空");
	    		}
	    		PersonInfo  personInfo= personTelInfoService.findPerson(map);
	    		if(personInfo == null){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "借款人不存在");
	    		}
	    		VLoanInfo vLoanInfoVo = new VLoanInfo();
	    		vLoanInfoVo.setContractNum(contractNum);
	    		List list = vLoanInfoService.findListByVO(vLoanInfoVo);
	    		if(CollectionUtils.isEmpty(list)){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "合同号对应债权不存在");
	    		}
	    		VLoanInfo vLoanInfo = (VLoanInfo) list.get(0);
	    		if(!personInfo.getId().equals(vLoanInfo.getBorrowerId())){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "该笔债权与借款人身份证号不对应");
	    		}
	    		String source = vLoanInfo.getFundsSources();
	    		if(!fundsSources.equals(source)){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "合同来源于该笔债权合同来源不一致");
	    		}
	    		if(!source.equals(FundsSourcesTypeEnum.外贸信托.getValue()) &&!source.equals(FundsSourcesTypeEnum.挖财2.getValue())
	    				&&!source.equals(FundsSourcesTypeEnum.国民信托.getValue())&&!source.equals(FundsSourcesTypeEnum.华澳信托.getValue())
	    				&&!source.equals(FundsSourcesTypeEnum.渤海信托.getValue())&&!source.equals(FundsSourcesTypeEnum.渤海2.getValue())
	    				&&!source.equals(FundsSourcesTypeEnum.华瑞渤海.getValue())&&!source.equals(FundsSourcesTypeEnum.龙信小贷.getValue())
	    				&&!source.equals(FundsSourcesTypeEnum.包商银行.getValue())&&!source.equals(FundsSourcesTypeEnum.外贸3.getValue())
	    				&&!source.equals(FundsSourcesTypeEnum.陆金所.getValue())){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "该合同来源不支持回购");
	    		}
	    		if(!vLoanInfo.getLoanState().equals(LoanStateEnum.正常.getValue())&&!vLoanInfo.getLoanState().equals(LoanStateEnum.逾期.getValue())){
	    			throw new PlatformException(ResponseEnum.FULL_MSG, "该债权不支持回购");
	    		}
	    		LoanReturnRecord lrr = new LoanReturnRecord();
	    		lrr.setLoanId(vLoanInfo.getId());
	    		List<LoanReturnRecord> lrrList = loanReturnRecordService.findListByVo(lrr);
             	if(CollectionUtils.isNotEmpty(lrrList)){
                    throw new PlatformException(ResponseEnum.FULL_MSG, "该债权已回接");
             	}else{
             		loanReturnRecordService.saveBuyBackLoanAndUpdateLoan(vLoanInfo, map);
	                map.put("result","导入成功");
                    map.put("remark", "成功");
             	}
    		}catch(PlatformException e){
    			logger.error("债权回购导入报错"+map.toString(), e);
    			map.put("result","导入失败");
                map.put("remark",e.getMessage());
                continue;
    		}catch(Exception e){
    			logger.error("债权回购导入报错:"+map.toString(), e);
				map.put("result","导入失败");
                map.put("remark","系统异常");
                continue;
    		}
    	}
	}

  

	/**
     * 文件下载
     * @param request
     * @param response
     * @param fileName
     * @param workbook
     * @return
     */
    private String downLoadFile(HttpServletRequest request,HttpServletResponse response, String fileName, Workbook workbook) {
        OutputStream out = null;
         try {
//             response.reset();
             response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
             response.setContentType(CONTENT_TYPE);
             out = response.getOutputStream();
             workbook.write(out);
             out.flush();
         } catch (IOException e) {
             logger.error("下载文件失败：", e);
             return "下载文件失败";
         } 
         return null;
    }
    /* 导出回购债权信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/exportBuyBackLoanFile")
	@ResponseBody
	public void exportReturnFile(LoanReturnVo loanReturnVo,HttpServletRequest request, HttpServletResponse response) {
		createLog(request, SysActionLogTypeEnum.导出, "回购债权信息", "导出回购债权信息");
		ResponseInfo responseInfo = null;
		User user = UserContext.getUser();
		Workbook workbook = null;
		OutputStream out = null;
		try {
			int maxRecord = 50000;
			// 收集参数信息
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("buyBackDateStart", loanReturnVo.getStart());
			paramMap.put("buyBackDateEnd", loanReturnVo.getEnd());
			paramMap.put("importDateStart", loanReturnVo.getImportStart());
			paramMap.put("importDateEnd", loanReturnVo.getImportEnd());
			paramMap.put("idnum", loanReturnVo.getIdnum());
			paramMap.put("fundsSources", loanReturnVo.getFundsSources());
			paramMap.put("loanBelongs", loanReturnVo.getLoanBelongs());
			paramMap.put("contractNum", loanReturnVo.getContractNum());
			Pager pager = new Pager();
			pager.setRows(maxRecord);
			pager.setPage(1);
			paramMap.put("pager", pager);
			pager = loanBaseService.findBuyBackLoanWithPg(paramMap);
			List<LoanReturnVo> loanReturnVoList = (List<LoanReturnVo>)pager.getResultList();
			// 当前导出条件查询不到数据
			Assert.notCollectionsEmpty(loanReturnVoList, ResponseEnum.FULL_MSG, "暂无数据！");
			// excel文件的表头显示字段名
			List<String> labels = this.getLabels();
			// excel文件的数据字段名
			List<String> fields = this.getFields();
			// 文件名
			String fileName = "回购债权表" + Dates.getDateTime(new Date(), "yyyyMMdd") + ".xls";
			// 工作表名称
			String sheetName = "Export";
			// 创建excel工作簿对象
			workbook = ExcelExportUtil.createExcelByVo(fileName, labels, fields, loanReturnVoList, sheetName);
			// 文件下载
			// response.reset();
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setContentType(CONTENT_TYPE);
			out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			return;
		} catch (PlatformException e) {
			logger.error("导出回购债权信息：", e);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("导出回购债权信息：", e);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		} finally {
			try {
				if (null != out) {
					out.close();
				}
				if (null != workbook) {
					workbook.close();
				}
			} catch (IOException e) {
				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
				logger.error("导出回购债权信息：", e);
			}
		}
		try {
			response.setContentType("text/html");
			response.getWriter().write(toResponseJSON(responseInfo));
		} catch (IOException e) {
		}
	}
	/**
	 * excel文件的表头显示名
	 * 
	 * @return
	 */
	private List<String> getLabels() {
		List<String> labels = new ArrayList<String>();
		labels.add("合同编号");
		labels.add("身份证号");
		labels.add("回购时间");
		labels.add("回购金额");
		labels.add("合同来源");
		labels.add("债权去向");
		labels.add("导入时间");
		return labels;
	}

	/**
	 * excel文件的数据字段名
	 * 
	 * @return
	 */
	private List<String> getFields() {
		List<String> fields = new ArrayList<String>();
		fields.add("contractNum");
		fields.add("idnum");
		fields.add("buyBackTime");
		fields.add("amount");
		fields.add("fundsSources");
		fields.add("loanBelongs");
		fields.add("createTime");		
		return fields;
	}
}
