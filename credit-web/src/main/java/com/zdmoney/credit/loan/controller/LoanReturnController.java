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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
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
import com.zdmoney.credit.person.service.pub.IPersonTelInfoService;
import com.zdmoney.credit.system.domain.PersonInfo;
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
public class LoanReturnController extends BaseController {
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
	
	public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	 	
	
	@RequestMapping("/loanReturnDefault")
	public String loanReturnDefault(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {
		return "/loanReturn/loanReturnDefault";
		
	}
	
	
	/**
	 * 默认页面展现数据
	 * @param baseMessage
	 * @param rows
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping("/listLoanReturn")	
	public String listLoanReturn(LoanReturnVo loanReturnVo,@RequestParam("startQueryDate") String startQueryDate,int rows, int page,HttpServletRequest request, HttpServletResponse response) throws ParseException{
		Map<String,Object> paramMap=new HashMap<String,Object>();
		Map<String,Object> params=new HashMap<String,Object>();
		Map<String,Object> paramSerach=new HashMap<String,Object>();
		List<Map<String, Object>> mapData=new ArrayList<Map<String, Object>>();
		if(Strings.isNotEmpty(startQueryDate)){
			paramMap.put("importDate", startQueryDate);
			 paramSerach.put("importDate", startQueryDate);
		}
		if(Strings.isNotEmpty(loanReturnVo.getName())){			
			 paramMap.put("name", loanReturnVo.getName());
			 paramSerach.put("name", loanReturnVo.getName());
		}
		if(Strings.isNotEmpty(loanReturnVo.getIdnum())){			
			 paramMap.put("idnum", loanReturnVo.getIdnum());
			 paramSerach.put("idnum", loanReturnVo.getIdnum());
		}
		if(Strings.isNotEmpty(loanReturnVo.getFundsSources())){			
			 paramMap.put("fundsSources", loanReturnVo.getFundsSources());
			 paramSerach.put("fundsSources", loanReturnVo.getFundsSources());
		}
		if(Strings.isNotEmpty(loanReturnVo.getContractNum())){			
			paramMap.put("contractNum", loanReturnVo.getContractNum());
			paramSerach.put("contractNum", loanReturnVo.getContractNum());
		}
		
		Pager pager = new Pager();	
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("id");
		pager.setSort("desc");
		paramMap.put("pager", pager);
		pager = loanBaseService.loanReturnWithPg(paramMap);
		TotalReturnLoanInfo totalReturnLoanInfo=loanBaseService.searchTotalReturnLoanInfo(paramSerach);
		params.put("totalAmount",totalReturnLoanInfo==null?0: totalReturnLoanInfo.getTotalAmount());
		params.put("totalPactMoney", totalReturnLoanInfo==null?0:totalReturnLoanInfo.getTotalPactMoney());	
		params.put("loanreturnListTotal", pager.getTotalCount());
		pager.setMapData(params);
		return toPGJSONWithCode(pager);	
	}

	
	/**
	 * 证大P2P债权导入
	 * @param file
	 * @param request
	 * @param response
	 */
	
	@ResponseBody
	@RequestMapping("/loanReturnImport")	
	 public void loanReturnImport(@RequestParam(value = "uploadfile") MultipartFile file,   HttpServletRequest request, HttpServletResponse response) {
		Workbook workbookNew = null;
	    ResponseInfo responseInfo = null;	       
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
	            ExcelTemplet excelTemplet = new ExcelTemplet().new LoanReturnExcel();
	            // 文件数据转换为List集合
	            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
	            processOneLine(sheetDataList); 	           
	            //验证和保存借款数据	         
	            String fileName = file.getOriginalFilename();
	            String[] labels = new String[] { "借款类型", "姓名", "身份证", "签约日期", "导入原因", "合同编号","导入结果", "导入详情"};
	            String[] fields = new String[] {"loanType", "borrowerName", "idNum", "signDate", "importReason", "contractNum","result", "remark"};	            
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
	 * 债权导入校验
	 * @param sheetDataList
	 * @param mapFlag
	 * @param paramLoanIds
	 */
	 private void processOneLine(List<Map<String, String>> sheetDataList) {
	    	for(Map<String, String> map:sheetDataList){//每一行记录遍历
	    		boolean flag = true;
	    		StringBuffer error = new StringBuffer();
	    		String name = Strings.convertValue(map.get("borrowerName"), String.class);
	            // 借款人身份证号码
	            String idNum = Strings.convertValue(map.get("idNum"), String.class);
	            // 借款类型
	            String loanType = Strings.convertValue(map.get("loanType"),String.class);
	            // 签约日期
	            String signDate = Strings.convertValue(map.get("signDate"),String.class); 
	            // 合同编号
	            String contractNum = Strings.convertValue(map.get("contractNum"),String.class); 
	            if(Strings.isEmpty(loanType)){
	            	error.append("借款类型不能为空!");	
	            	flag=false;
	            }else{
	            	boolean loanTypeTrue = false;
	    		  		for (LoanTypeEnum loanTypes : LoanTypeEnum.values()) {
	    		                if (loanTypes.toString().equals(map.get("loanType").toString())) {
	    		                    loanTypeTrue = true;
	    		                    break;
	    		                }
	    		            }
	    		            if (!loanTypeTrue) {
	    		                error.append("借款类型有误!");
	    		                flag=false;
	    		            }
	            }
	         	if (Strings.isEmpty(name)) {
	                error.append("姓名不能为空!");
	                flag=false;
	            }

	            if (Strings.isEmpty(idNum)) {
	                error.append("身份证号不能为空!");
	                flag=false;
	            }

	            if (Strings.isEmpty(signDate)) {
	                error.append("签约日期不能为空!");
	                flag=false;
	            } else {
	                if (!signDate.matches("\\d{4}-\\d{2}-\\d{2}")){
	                    error.append("签约日期格式有误!");
	                    flag=false;
	                }
	            }
	            if (Strings.isEmpty(contractNum)) {
	                error.append("合同编号不能为空!");
	                flag=false;
	            }
	            if(flag){
	            	 PersonInfo  personInfo= personTelInfoService.findPerson(map);
	            	                  
	            	 if(personInfo==null){	 	           
	                        map.put("result","导入失败");
	 	                    map.put("remark", "借款人不存在");
	 	                   continue;
	 	            }
	            	 Map<String, Object> loanParams = new HashMap<String, Object>();	
	            	 loanParams.put("idNum", idNum.trim());
	                 loanParams.put("name", name.trim());
	                 loanParams.put("loanType", loanType.trim());
	                 loanParams.put("signDate", signDate.trim());
	                 loanParams.put("contractNum", contractNum.trim());
	            	 VLoanInfo loanInfo = vLoanInfoService.getVLoanBySignDateAndBorrowerAndLoanType(loanParams);
	            	if(loanInfo==null){	 	           
	                        map.put("result","导入失败");
	 	                    map.put("remark", "债权不存在");
	 	                   continue;
	 	            }else{
	 	            	if(!(loanInfo.getLoanState().equals(LoanStateEnum.正常.getValue()) ||loanInfo.getLoanState().equals(LoanStateEnum.逾期.getValue())) ){
	 	            		map.put("result","导入失败");
	 	                    map.put("remark", "该债权状态是"+loanInfo.getLoanState());
	 	                   continue;
	 	            	}
	 	            	SimpleDateFormat sft=new SimpleDateFormat("yyyy-MM-dd");
	 	                if (sft.format(loanInfo.getGrantMoneyDate()) .equals(sft.format(new Date()))) {
	 	                	map.put("result","导入失败");
	 	                    map.put("remark", "放款时间为今天");
	 	                   continue;
	 	                }
	 	                if(null==loanInfo.getBatchNum() && (!"证大爱特".equals(loanInfo.getFundsSources()) && !"积木盒子".equals(loanInfo.getFundsSources())) ){
	 	                	map.put("result","导入失败");
	 	                    map.put("remark", "不是证大爱特或者积木盒子的债权");//既不是证大爱特 又不是积木盒子
	 	                   continue;
	 	                }
	 	                if(null!=loanInfo.getBatchNum()&& (!"XL".equals(loanInfo.getBatchNum().substring(0, 2)) && !"WC".equals(loanInfo.getBatchNum().substring(0, 2))&& !"AT".equals(loanInfo.getBatchNum().substring(0, 2)) && !"LX".equals(loanInfo.getBatchNum().substring(0, 2))   ) ){
	 	                	map.put("result","导入失败");
	 	                    map.put("remark", "不是新浪或者挖财或者挖财2或者证大爱特2或者龙信小贷的债券");
	 	                   continue;
	 	                 }
	 	                try {
	 	                	int count =0;
	 	                	LoanReturn lreturn=	new LoanReturn();
	 	                	lreturn.setLoanId(loanInfo.getId());
	 	                	count=loanReturnService.countLoanReturn(lreturn);
	 	                	if(count>0){
	 	                		map.put("result","导入失败");
	                            map.put("remark", "该债权已回接");
	 	                	}else{
	 	                		saveLoanReturnAndUpdateLoan(loanInfo,map);
		 	                	map.put("result","导入成功");
	                            map.put("remark", "成功");
	 	                	}
	 	                	
	 					} catch (Exception e) {
	 						
	 						map.put("result","导入失败");
	 		                map.put("remark","系统异常");
	 		               e.printStackTrace();  
	 					}
	 	            }
	            	 
	            }else{
	            	 map.put("remark", error.toString());
	            	 map.put("result","导入失败");
	            }
	           
	    	} 
		 
	 }
	
    //保存借款数据
    @Transactional
    public void saveLoanReturnAndUpdateLoan(VLoanInfo loanInfo, Map<String, String> map){
	    	SimpleDateFormat sft=new SimpleDateFormat("yyyy-MM-dd");
	    	LoanReturn loanReturn=new LoanReturn();
	    	loanReturn.setLoanId(loanInfo.getId());
	    	loanReturn.setBatchNum(loanInfo.getBatchNum());
	    	loanReturn.setFundsSources(loanInfo.getFundsSources());
	    	loanReturn.setImportReason(map.get("importReason"));
	    	loanReturn.setCreateTime(new Date());
	    	
	    	Date today = getYMD(new Date());
	    	Date nextRepayDate = getNextRepayDate(today);
	    	map.put("loanId", loanInfo.getId().toString());
	    	map.put("returnDate", sft.format(nextRepayDate));
		    LoanRepaymentDetail 	repaymentDetail= loanRepaymentDetailServiceImpl.findRepaymentDetailByLoanAndReturnDate(map);
		    if(Strings.isNotEmpty(repaymentDetail)){
		    	loanReturn.setCurrentTerm(repaymentDetail.getCurrentTerm());
		    }else{
		    	map.put("returnDate", sft.format(getNextRepayDate(nextRepayDate)));
		    	repaymentDetail=loanRepaymentDetailServiceImpl.findRepaymentDetailByLoanAndReturnDate(map);
		    	if(null==repaymentDetail){
		    		loanReturn.setCurrentTerm(null);
		    	}else{
		    		loanReturn.setCurrentTerm(repaymentDetail.getCurrentTerm());
		    	}
		    	
		    }
		    //保存loanReturn
		    loanReturnService.insertLoanReturn(loanReturn);
		    //更新loan_base
		    LoanBase loanBase=new LoanBase();
		    loanBase.setId(loanInfo.getId());
		    loanBase.setLoanBelong("证大P2P");
		    loanBaseService.updateLoanBaseByLoanReturn(loanBase);    	
    }
    
    private Date getYMD(Date date) {
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
			return sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
    }
    

    public static Date getNextRepayDate(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        if (cal.get(Calendar.DAY_OF_MONTH) >= 16){
            cal.add(Calendar.MONTH,1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }else {
            cal.set(Calendar.DAY_OF_MONTH, 16);
        }
        return cal.getTime();
    }

	public static Date getNextDay(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();
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
    

    /**
     * //挖财导入债权模板
     * @param file
     * @param request
     * @param response
     */
	@ResponseBody
	@RequestMapping("/digmoneyImport")	
	 public String digmoneyImport(@RequestParam(value = "uploadfile") MultipartFile file,   HttpServletRequest request, HttpServletResponse response) {
				// 存放文件的目录
//		        StringBuilder filePath = new StringBuilder();
//		        filePath.append(File.separatorChar);
//		        filePath.append("usr");
//		        filePath.append(File.separatorChar);
//		        filePath.append("tmp");
//		        filePath.append(File.separatorChar);
//		        filePath.append("digMoney");
		        String filePath = sysParamDefineService.getSysParamValueCache("download", "dig.money.dir");
		        File files = new File(filePath);
		        if  (!files.exists()  && !files.isDirectory())
		        {
		            files.mkdirs();
		        }
		        if (files.isDirectory()) {
		            String[] filelist = files.list();
		            for (int i = 0; i < filelist.length; i++) {
		                File readfile = new File(filePath.toString() + "/" + filelist[i]);
		                if (!readfile.isDirectory()) {
		                    readfile.delete();
		                }
		            }
		
		        }	
			
				Map<String, Object>  map=new HashMap<String, Object>();	
				 int digMoneyTotal=0;
			 	 int successBeans= 0;
			     int failBeans=0;
			     BigDecimal sumMoney=new BigDecimal(0.00);
			     BigDecimal successMoney=new BigDecimal(0.00);
			     BigDecimal faileMoney=new BigDecimal(0.00);
			     map.put("digMoneyTotal", digMoneyTotal);
		         map.put("successBeans", successBeans);
		         map.put("failBeans", failBeans);
		         map.put("sumMoney", sumMoney);
		         map.put("successMoney", successMoney);
		         map.put("faileMoney",faileMoney);  
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
		            ExcelTemplet excelTemplet = new ExcelTemplet().new LoanReturnWCExcel();
		            // 文件数据转换为List集合
		            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
		            processOneLineMoneyData(sheetDataList,map);
		            map.put("fileName", file.getOriginalFilename());
		            String fileName = file.getOriginalFilename();
		            String[] labels = new String[] { "序号", "借款人姓名", "借款人身份证号码","本次转让债权价值","需支付对价","借款人职业情况","借款人借款用途","还款期限（月）","剩余还款月数","预计债权收益率（年）", "合同编号", "结果"};
		            String[] fields = new String[] {"id", "borrowerName", "idNum","zrMoney","payMoney" ,"workInfo","purpose" ,"time","residualTime","rateEY","contractNum","result"};
		            // 工作表名称
		            String sheetName = "Sheet1";
	               // 创建工作簿
		            Workbook workbookNew  = ExcelExportUtil.createExcelByMapString(fileName, labels, fields, sheetDataList, sheetName); 
		            FileOutputStream outputStream =  new FileOutputStream(filePath+"/"+fileName);
		            try{
		            	workbookNew.write(outputStream);
		                outputStream.close();
		            }catch (Exception e){
		                e.printStackTrace();
		            }
		            attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
		    		attachmentResponseInfo.setAttachment(map); 
		          
		        } catch (PlatformException e) {
		        	attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD.getCode(),ResponseEnum.SYS_FAILD.getDesc());
					attachmentResponseInfo.setAttachment(map);
		        } catch (Exception e) {
		        	attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD.getCode(),ResponseEnum.SYS_FAILD.getDesc());
					attachmentResponseInfo.setAttachment(map);
		        }
		        return toResponseJSON(attachmentResponseInfo);
	    }

    /**
     * 挖财债权导入校验
     * @param sheetDataList
     */
    public void processOneLineMoneyData(List<Map<String, String>> sheetDataList,Map<String, Object>  mapValue) {
	 	 int successBeans= 0;
	     int failBeans=0;
	     BigDecimal sumMoney=new BigDecimal(0.00);
	     BigDecimal successMoney=new BigDecimal(0.00);
	     BigDecimal faileMoney=new BigDecimal(0.00);
    	
    	if(CollectionUtils.isEmpty(sheetDataList)){
    		mapValue.put("digMoneyTotal", 0);
    		return;
    	}else{
    		mapValue.put("digMoneyTotal", sheetDataList.size());
    	}
    	for(Map<String, String> map:sheetDataList){//每一行记录遍历
    		boolean flag = true;
    		StringBuffer error = new StringBuffer();
    		String name = Strings.convertValue(map.get("borrowerName"), String.class);
            // 借款人身份证号码
            String idNum = Strings.convertValue(map.get("idNum"), String.class);
            String zrMoney = Strings.convertValue(map.get("zrMoney"), String.class);
            //合同编号
            String contractNum = Strings.convertValue(map.get("contractNum"), String.class);
         	if (Strings.isEmpty(name)) {
                error.append("姓名不能为空!");
                flag=false;
            }
            if (Strings.isEmpty(idNum)) {
                error.append("身份证号不能为空!");
                flag=false;
            }
            if (Strings.isEmpty(zrMoney)) {
            	error.append("本次转让债权价值为空!");
            	flag=false;
            }          
            if (Strings.isEmpty(contractNum)) {
            	error.append("合同编号不能为空!");
            	flag=false;
            }          
            sumMoney=sumMoney.add(new BigDecimal(zrMoney));//得到债权转让总值
            mapValue.put("sumMoney", sumMoney) ;
            if(false==flag){
            	  map.put("remark", error.toString());
            	  continue;
            }
            PersonInfo  personInfo= personTelInfoService.findPerson(map);
            if(personInfo==null){	 	           
                map.put("result","导入失败");
                 map.put("remark", "借款人不存在");
            }else{
            	Map<String, Object> loanParams = new HashMap<String, Object>();
                loanParams.put("idNum", idNum.trim());
                loanParams.put("name", name.trim());
                loanParams.put("contractNum", contractNum.trim());
                
            	 VLoanInfo loanInfo = vLoanInfoService.getVLoanByIdnumAndName(loanParams);
            	 if(null==loanInfo){
            		  map.put("result","导入失败");
	                  map.put("remark", "异常");
	                  failBeans++;
	                  faileMoney=faileMoney.add(new BigDecimal(zrMoney));
	                  mapValue.put("failBeans", failBeans) ;
	                  mapValue.put("faileMoney", faileMoney) ;
            	 }else{
            		 successBeans++;
            		 successMoney=successMoney.add(new BigDecimal(zrMoney));
            		 mapValue.put("successBeans", successBeans) ;
	                  mapValue.put("successMoney", successMoney) ;
	                  map.put("result","导入成功");
                      map.put("remark", "") ;
            	 }
            }
    	} 
    	
    }
    
   
    @RequestMapping("/downLoadExcel")
    @ResponseBody
    public void downLoadExcel(HttpServletRequest request,HttpServletResponse response, @RequestParam("fileName") String fileName) {
//    	AttachmentResponseInfo<Map<String, Object>> attachmentResponseInfo = null;	
    	 OutputStream outStream=null;
    	 InputStream inputStream=null;
    	 try {
    		 // 存放文件的目录
    		 	String filePath = sysParamDefineService.getSysParamValueCache("download", "dig.money.dir");
		        response.reset();
		        response.setHeader("Content-disposition","attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		        File file = new File(filePath+"/" + fileName);
		        outStream = response.getOutputStream();
		         inputStream = new FileInputStream(file);
		        byte[] buffer = new byte[2048];
		        int count = -1; //每次读取字节数
		        while((count = inputStream.read(buffer)) != -1){
		            outStream.write(buffer, 0, count);
		        }
		        outStream.flush();
		        outStream.close();
		        inputStream.close();
    	 	} catch (IOException e) {
    	 		
             logger.error("下载文件失败：", e);
//             attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD.getCode(),"下载文件失败");
         }finally{
        	 try {
        		 if(null!=outStream){
             		outStream.close();
             	} 
             	if(null!=inputStream){
             		inputStream.close();
             	} 
			} catch (Exception e2) {
				// TODO: handle exception
			}
        	
         }
//    	 	attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
//    		return toResponseJSON(attachmentResponseInfo);
     }
    
    
  /*  @RequestMapping("/updateBatchNum")
    @ResponseBody
    public String updateBatchNum(HttpServletRequest request,HttpServletResponse response, @RequestParam("fileName") String fileName) {
    	AttachmentResponseInfo<Map<String, Object>> attachmentResponseInfo = null;		
    	try {
    		 Map<String, Object> paramMap=new HashMap<String, Object>();
    		// 存放文件的目录
	        StringBuilder filePath = new StringBuilder();
	        filePath.append(File.separatorChar);
	        filePath.append("usr");
	        filePath.append(File.separatorChar);
	        filePath.append("tmp");
	        filePath.append(File.separatorChar);
	        filePath.append("digMoney");
	        File file = new File(filePath.toString()+"/"+fileName);
            Workbook workbook = WorkbookFactory.create(file);
            ExcelTemplet excelTemplet = new ExcelTemplet().new LoanReturnWCExcel();
            // 文件数据转换为List集合
            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
            List<Long> list = new ArrayList<Long>();
            for(Map<String, String> map:sheetDataList){//每一行记录遍历
        	   Map<String, Object> loanParams = new HashMap<String, Object>();
               loanParams.put("idNum", map.get("idNum").toString().trim());
               loanParams.put("name", map.get("borrowerName").toString().trim());
	           VLoanInfo loanInfo = vLoanInfoService.getVLoanByIdnumAndName(loanParams);
	           System.out.println("1");
	           Long loanId = loanInfo.getId();
	           list.add(loanId);
            }
            paramMap.put("list", list);
            paramMap.put("org", "WC");
	        int result=  vLoanInfoService.updateBatchNum(paramMap);  
	        if(result==0){
	        	attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD.getCode(),"生成批次号失败"); 
	        	return toResponseJSON(attachmentResponseInfo);
	        }
		} catch (Exception e) {
			e.printStackTrace();
			 attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD.getCode(),"生成批次号失败");
			 return toResponseJSON(attachmentResponseInfo);
		}
    	attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
		return toResponseJSON(attachmentResponseInfo);
    	
    	
    }*/
    
    
}
