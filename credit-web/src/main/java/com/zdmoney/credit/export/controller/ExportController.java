package com.zdmoney.credit.export.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Controller
@RequestMapping("/export/exp/")
public class ExportController extends BaseController{
	@Autowired
	private ISysParamDefineService sysParamDefineService;	
	//放款状态明细表
	@RequestMapping("exportLoanStatusDetail")
	public String repayTrail(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {
		Calendar c = Calendar.getInstance();
		modelMap.put("reportDate", c.getTime());	
		return "/export/exportLoanStatusDetail";
	}

	@RequestMapping("/download/exportLoanStatusDetail")
	@ResponseBody
	public void exportLoanStatusDetail(@RequestParam("reportDate")String reportDate, HttpServletRequest request, HttpServletResponse response) {
		createLog(request, SysActionLogTypeEnum.导出, "导出" + reportDate + "数据", "放款状态明细全量数据导出下载操作。");
		
        ResponseInfo responseInfo = null;
        String dateDir = Dates.getDateTime(Dates.parse(reportDate, "yyyy-MM-dd"), "yyyyMMdd");
        try{
 		   // FTP服务器IP地址
            String host = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.host");
            // FTP服务器端口号
            String port = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.port");
            // FTP服务器登陆用户名
            String userName = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.userName");
            // FTP服务器I登陆密码
            String password = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.pwd");
            
            String path = sysParamDefineService.getSysParamValue("export", "exportLoanStatusDetailRemotePath");
        
        	//全量放款状态明细数据导出
            String fileName = "LoanStatusDetail.csv";
        	
            
        	FTPUtil ftp = new FTPUtil();
        	ftp.connectServer(host, port, userName, password, path + dateDir);
        	List<String> files = ftp.getFileList(path + dateDir);
        	
        	if (files.size() == 0) {
        		responseInfo = new ResponseInfo(ResponseEnum.FILE_NO_FILE, "文件未生成或文件已过期！");
        	} else {
        		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
            	ftp.download(fileName, outputStream);
            	
            	OutputStream returnStream = response.getOutputStream();
            	String downloadFileName = "放款状态明细" + dateDir + "全量.csv";
     			downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");
            	response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
            	outputStream.writeTo(returnStream);
            	responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
            	
            	returnStream.flush();
            	returnStream.close();
        	}
        	ftp.closeServer();
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }	
	//90天以内逾期放款状态明细表
	@RequestMapping("exportLoanStatusDetailTrimester")
	public String repayTrailTrimester(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {
		Calendar c = Calendar.getInstance();
		modelMap.put("reportDate", c.getTime());	
		return "/export/exportLoanStatusDetailTrimester";
	}

	@RequestMapping("/download/exportLoanStatusDetailTrimester")
	@ResponseBody
	public void exportLoanStatusDetailTrimester(@RequestParam("reportDate")String reportDate, HttpServletRequest request, HttpServletResponse response) {
		createLog(request, SysActionLogTypeEnum.导出, "导出" + reportDate + "数据", "90天以内逾期放款状态数据导出下载操作。");
		
        ResponseInfo responseInfo = null;
        String dateDir = Dates.getDateTime(Dates.parse(reportDate, "yyyy-MM-dd"), "yyyyMMdd");
        try{
 		   // FTP服务器IP地址
            String host = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.host");
            // FTP服务器端口号
            String port = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.port");
            // FTP服务器登陆用户名
            String userName = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.userName");
            // FTP服务器I登陆密码
            String password = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.pwd");
            
            String path = sysParamDefineService.getSysParamValue("export", "exportLoanStatusDetailRemotePath");
        
        	//90天以内逾期放款状态明细数据导出
            String fileName = "LoanStatusDetailTrimester.csv";
        	
            
        	FTPUtil ftp = new FTPUtil();
        	ftp.connectServer(host, port, userName, password, path + dateDir);
        	List<String> files = ftp.getFileList(path + dateDir);
        	
        	if (files.size() == 0) {
        		responseInfo = new ResponseInfo(ResponseEnum.FILE_NO_FILE, "文件未生成或文件已过期！");
        	} else {
        		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
            	ftp.download(fileName, outputStream);
            	
            	OutputStream returnStream = response.getOutputStream();
            	String downloadFileName = "90天以内逾期放款状态明细" + dateDir + "导出数据.csv";
     			downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");
            	response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
            	outputStream.writeTo(returnStream);
            	responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
            	
            	returnStream.flush();
            	returnStream.close();
        	}
        	ftp.closeServer();
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
	
	//放款客户质量追踪表
	@RequestMapping("exportLoanCustomers")
	public String LoanCustomers(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {
		Calendar c = Calendar.getInstance();
		modelMap.put("reportDate", c.getTime());	
		return "/export/exportLoanCustomers";
	}
	
	@RequestMapping("/download/exportLoanCustomers")
	@ResponseBody
	public void exportLoanCustomers(@RequestParam("reportDate")String reportDate, HttpServletRequest request, HttpServletResponse response) {
		createLog(request, SysActionLogTypeEnum.导出, "导出" + reportDate + "数据", "放款客户质量追踪表导出下载操作。");
		
        ResponseInfo responseInfo = null;
        String dateDir = Dates.getDateTime(Dates.parse(reportDate, "yyyy-MM-dd"), "yyyyMMdd");
        try{
 		   // FTP服务器IP地址
            String host = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.host");
            // FTP服务器端口号
            String port = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.port");
            // FTP服务器登陆用户名
            String userName = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.userName");
            // FTP服务器I登陆密码
            String password = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.pwd");
            
            String path = sysParamDefineService.getSysParamValue("export", "exportLoanStatusDetailRemotePath");
        
        	//放款客户质量追踪表导出
            String fileName = "LoanCustomers.csv";
        	
            
        	FTPUtil ftp = new FTPUtil();
        	ftp.connectServer(host, port, userName, password, path + dateDir);
        	List<String> files = ftp.getFileList(path + dateDir);
        	
        	if (files.size() == 0) {
        		responseInfo = new ResponseInfo(ResponseEnum.FILE_NO_FILE, "文件未生成或文件已过期！");
        	} else {
        		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
            	ftp.download(fileName, outputStream);
            	
            	OutputStream returnStream = response.getOutputStream();
            	String downloadFileName = "放款客户质量" + dateDir + "追踪表.csv";
     			downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");
            	response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
            	outputStream.writeTo(returnStream);
            	responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
            	
            	returnStream.flush();
            	returnStream.close();
        	}
        	ftp.closeServer();
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
	//直通车放款客户质量追踪表
	@RequestMapping("exportTrainLoanCustomers")
	public String TrainLoanCustomers(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {
		Calendar c = Calendar.getInstance();
		modelMap.put("reportDate", c.getTime());	
		return "/export/exportTrainLoanCustomers";
	}
	
	@RequestMapping("/download/exportTrainLoanCustomers")
	@ResponseBody
	public void exportTrainLoanCustomers(@RequestParam("reportDate")String reportDate, HttpServletRequest request, HttpServletResponse response) {
		createLog(request, SysActionLogTypeEnum.导出, "导出" + reportDate + "数据", "直通车放款客户质量追踪表导出下载操作。");
		
        ResponseInfo responseInfo = null;
        String dateDir = Dates.getDateTime(Dates.parse(reportDate, "yyyy-MM-dd"), "yyyyMMdd");
        try{
 		   // FTP服务器IP地址
            String host = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.host");
            // FTP服务器端口号
            String port = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.port");
            // FTP服务器登陆用户名
            String userName = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.userName");
            // FTP服务器I登陆密码
            String password = sysParamDefineService.getSysParamValueCache("exportDownload", "sftp.download.pwd");
            //文件传输路径
            String path = sysParamDefineService.getSysParamValue("export", "exportLoanStatusDetailRemotePath");
        
        	//直通车放款客户质量追踪表导出
            String fileName = "TrainLoanCustomers.csv";
            
        	FTPUtil ftp = new FTPUtil();
        	ftp.connectServer(host, port, userName, password, path + dateDir);
        	List<String> files = ftp.getFileList(path + dateDir);
        	
        	if (files.size() == 0) {
        		responseInfo = new ResponseInfo(ResponseEnum.FILE_NO_FILE, "文件未生成或文件已过期！");
        	} else {
        		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
            	ftp.download(fileName, outputStream);
            	
            	OutputStream returnStream = response.getOutputStream();
            	String downloadFileName = "直通车放款客户质量" + dateDir + "追踪表.csv";
     			downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");
            	response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
            	outputStream.writeTo(returnStream);
            	responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
            	
            	returnStream.flush();
            	returnStream.close();
        	}
        	ftp.closeServer();
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
}
