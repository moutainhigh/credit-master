package com.zdmoney.credit.payment.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zdmoney.credit.common.constant.LoanFlowStateEnum;
import com.zdmoney.credit.common.constant.ThirdOfferStateEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.FileUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.payment.service.pub.IThirdLineOfferService;

/**
 * 第三方线下放款
 * @author fuhongxing
 * @date 2016年7月14日
 * @version 1.0.0
 */
@Controller
@RequestMapping("/loan/thirdOffLineLoan")
public class ThirdOffLineLoanController extends BaseController {
	private static final Logger logger = Logger.getLogger(ThirdOffLineLoanController.class);
	
	
	@Autowired
	private ILoanFeeInfoService iLoanFeeInfoService;
	
//	@Autowired
//	private IVLoanInfoService iVLoanInfoService;
	
	@Autowired
    private IThirdLineOfferService haTwoOfferService;
	/**
	 * 跳转线下放款主页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/jumpPage")
    public String loanReturnDefault(Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("sysdate", Dates.getDateTime(new Date(), "yyyyMMdd"));
        model.addAttribute("states", ThirdOfferStateEnum.values());
        return "thirdLine/thirdOffLineLoan";
    }
	
	
	/**
     * 查询第三方线下放款信息
     * @param rows
     * @param page
     * @param request
     * @param response
     * @param modelMap
     * @return
     */
    @ResponseBody
    @RequestMapping("/listThirdOffLineLoan")
    public String listThirdOffLineLoan(int rows, int page, HttpServletRequest request, HttpServletResponse response) {
    	User user = UserContext.getUser();
    	Map<String, Object> params = new HashMap<String, Object>();
    	//根据角色区分合同来源，查询第三方线下放款信息
    	Map<String, Object> role = user.getRoleMap();
    	List<String> list = new ArrayList<String>();
    	for (Map.Entry<String, Object> entry : role.entrySet()) {
    		if(entry.getValue().equals("系统管理员")){
    			//合同来源
    			list.add("龙信小贷");
    			list.add("外贸信托");
    	        params.put("fundsSources", list);
    	        break;
    		}
    		if(entry.getValue().equals("第三方线下放款-龙信小贷")){
    			list.add("龙信小贷");
    			params.put("fundsSources", list);
    			break;
    		}
    		if(entry.getValue().equals("第三方线下放款-外贸信托")){
    			list.add("外贸信托");
    			params.put("fundsSources", list);
    			break;
    		}
    	}
    	
        
        // 客户姓名
        String name = request.getParameter("name");
        if (Strings.isNotEmpty(name)) {
            params.put("name", name);
        }
        // 身份证号码
        String idnum = request.getParameter("idnum");
        if (Strings.isNotEmpty(idnum)) {
            params.put("idnum", idnum);
        }
        
        //状态
        params.put("loanFlowState", LoanFlowStateEnum.财务放款.getValue());
        
        Pager pager = new Pager();
        pager.setPage(page);
        pager.setRows(rows);
        params.put("pager", pager);
        pager = haTwoOfferService.searchOffLineLoanInfoWithPg(params);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
	
    
    
    /**
     * 导入龙信小贷放款信息
     * @param file
     * @param request
     * @param response
     */
    @RequestMapping("/importLoanExcel")
    @ResponseBody
    public void importLoanExcel(@RequestParam(value = "offLineLoanFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        ResponseInfo responseInfo = null;
        // 导入文件名
        String fileName = file.getOriginalFilename();
        User user = UserContext.getUser();
        boolean flag = true;
        for (Map.Entry<String, Object> entry : user.getRoleMap().entrySet()) {
    		if(entry.getValue().equals("系统管理员") || entry.getValue().equals("第三方线下放款-龙信小贷") || entry.getValue().equals("第三方线下放款-外贸信托")){
    	        flag = false;
    			break;
    		}
    	}
        if(flag){
        	try {
				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
				responseInfo.setResMsg("导入失败,您没有相关操作权限!");
				response.setContentType("text/html");
				response.getWriter().write(toResponseJSON(responseInfo));
				return;
			} catch (IOException e) {
				logger.error("第三方线下放款权限验证异常", e);
			}
        }
        
        //外贸信托导入操作
        if(fileName.endsWith(".txt")){
        	logger.info("外贸信托线下放款导入,导入文件：" + fileName);
        	try {
				List<String []> list = FileUtil.readFile(file.getInputStream());
				if(list==null || list.size()==0){
					responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
					responseInfo.setResMsg("导入失败!");
	                response.setContentType("text/html");
	                response.getWriter().write(toResponseJSON(responseInfo));
					return;
				}
				//数据验证
				List<String> result = iLoanFeeInfoService.checkWaiMaoXinTuo(list);
				//导出
				FileUtil.write(file.getInputStream(), result, request, response);
			} catch (IOException e) {
				logger.error("外贸信托导入异常");
				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
                try {
                	response.setContentType("text/html");
					response.getWriter().write(toResponseJSON(responseInfo));
				} catch (IOException e1) {
				}
			}
        	/*try {
                response.setContentType("text/html");
                response.getWriter().write(toResponseJSON(responseInfo));
            } catch (IOException e) {
            }*/
        	return;
        }
        
        logger.info("执行龙信小贷导入放款操作,导入文件：" + fileName);
        try {
            // 文件校验
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(UploadFileUtil.FILE_SIZE_DEFAULT_MAX);
            UploadFileUtil.valid(uploadFile);
            // 创建excel工作簿
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            // 创建导入数据模板
            ExcelTemplet excelTemplet = new ExcelTemplet().new importOffLineExcel();
            // 文件数据转换为List集合
            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
            // 校验和更新导入数据
            iLoanFeeInfoService.checkOffLineLoanInfo(sheetDataList);
            // 在excel文件的每行末尾追加单元格提示信息
            ExcelUtil.addResultToWorkBook(workbook, sheetDataList, excelTemplet);
            //(fileName.getBytes("utf-8"), "ISO8859-1");
            String userAgent = request.getHeader("USER-AGENT");
            String finalFileName = "";
            if(org.apache.commons.lang3.StringUtils.contains(userAgent, "MSIE") || org.apache.commons.lang3.StringUtils.contains(userAgent, "11.0")){//IE浏览器
                finalFileName = URLEncoder.encode(fileName,"UTF8");
	        }else if(org.apache.commons.lang3.StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
	            finalFileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
	        }else{
	            finalFileName = URLEncoder.encode(fileName,"UTF8");//其他浏览器
	        }
            response.setHeader("Content-Disposition", "attachment;filename="+finalFileName);
            response.setContentType("application/ynd.ms-excel;charset=UTF-8");
//            response.setContentType(file.getContentType());
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            return;
        } catch (Exception e) {
        	logger.info("导入第三方线下放款文件异常", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
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
	
}
