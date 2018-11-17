package com.zdmoney.credit.loan.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.service.pub.ILoanReturnService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 债权导入
 * @author Anfq
 *
 * 2015年9月20日
 */
@Controller
@RequestMapping("/loanReturn")
public class LoanReturnCreatBatchController extends BaseController {
	protected static Log logger = LogFactory.getLog(LoanReturnCreatBatchController.class);
	@Autowired
	private ILoanReturnService loanReturnService;
	
	@Autowired
	private IVLoanInfoService vLoanInfoService;
	
	@Autowired
    private ISysParamDefineService sysParamDefineService;
	

    
    @RequestMapping("/updateBatchNum")
    @ResponseBody
    public String updateBatchNum(HttpServletRequest request,HttpServletResponse response, @RequestParam("fileName") String fileName) {
    	AttachmentResponseInfo<Map<String, Object>> attachmentResponseInfo = null;		
    	try {
    		 Map<String, Object> paramMap=new HashMap<String, Object>();
    		// 存放文件的目录
    		String filePath = sysParamDefineService.getSysParamValueCache("download", "dig.money.dir");
	        File file = new File(filePath+"/"+fileName);
	        FileInputStream fis=new FileInputStream(file); 
	        InputStream in = new BufferedInputStream(fis);
            Workbook workbook = WorkbookFactory.create(in);
            ExcelTemplet excelTemplet = new ExcelTemplet().new LoanReturnWCExcel();
            // 文件数据转换为List集合
            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
           
            List<Long> list = new ArrayList<Long>();
            for(Map<String, String> map:sheetDataList){//每一行记录遍历
        	   Map<String, Object> loanParams = new HashMap<String, Object>();
               loanParams.put("idNum", map.get("idNum").toString().trim());
               loanParams.put("name", map.get("borrowerName").toString().trim());
	           VLoanInfo loanInfo = vLoanInfoService.getVLoanByIdnumAndName(loanParams);
	           Long loanId = loanInfo.getId();
	           list.add(loanId);
            }
            paramMap.put("list", list);
            paramMap.put("org", "WC");
	        int result=  vLoanInfoService.updateBatchNum(paramMap);  
	      
	        if(result>0){
	        	file.delete();
	        	attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
	    		return toResponseJSON(attachmentResponseInfo);
	        }else{
	        	attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD.getCode(),"生成批次号失败"); 
	        	return toResponseJSON(attachmentResponseInfo);
	        }
		} catch (Exception e) {
			logger.error(e.getMessage());
			 attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD.getCode(),"生成批次号失败");
			 return toResponseJSON(attachmentResponseInfo);
		}
    	
    	
    }
    
    
}
