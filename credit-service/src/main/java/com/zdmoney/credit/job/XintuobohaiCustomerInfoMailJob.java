package com.zdmoney.credit.job;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.exceldata.XintuoguominData;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.xingtuo.dao.pub.IXintuoguominDataDao;
import com.zdmoney.credit.xintuo.domain.XintuoguominDataDomain;

/**
 * 渤海信托邮件发送job(每天16:15分发送)
 * @author fuhongxing
 * @date 2016年6月15日
 * @veersion 1.0.0
 */
@Service
public class XintuobohaiCustomerInfoMailJob {
	private static final Logger logger = Logger.getLogger(XintuobohaiCustomerInfoMailJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private ISendMailService sendMailService;
	@Autowired
	private IXintuoguominDataDao dao;
	
	
	
	public void doNoticeNormal() {
//		收件人：周莉莉，陈早
//		抄送人：赵竞宏，梁晓庆，李昊昱
		logger.info("========开始执行渤海信托邮件发送job定时任务========");
		
		//获取job是否开启(1开启，0不开启)
    	String isXintuoguominCustomerInfoMail = sysParamDefineService.getSysParamValue("sysJob", "isXintuobohaiCustomerInfoMail");
		if(!Const.isClosing.equals(isXintuoguominCustomerInfoMail)){
			try {
				//查询渤海信托相关的债权数据
				List<XintuoguominDataDomain> datas = dao.getXintuobohaiDataJob();
				List<XintuoguominData> excelDatas = new ArrayList<XintuoguominData>();
				XintuoguominData excelData = null;
				for (XintuoguominDataDomain data : datas) {
					excelData =  new XintuoguominData();
					BeanUtils.copyProperties(excelData, data);
					excelDatas.add(excelData);
				}
		        String date = Dates.getDateTime(new Date(), "yyyy年MM月dd日");
		        String fileName = "渤海信托" + date + "财务审核待放款客户信息";
		        StringBuffer stringBuffer= new StringBuffer();
		        
		        stringBuffer.append(" 渤海信托"+date+"财务审核待放款客户信息。");
				if (excelDatas.size() > 0) {
					logger.info("渤海信托财务审核待放款客户信息通知发送邮件处理开始........");
					loanLogService.createLog("XintuobohaiCustomerInfoMailJob", "info", "渤海信托财务审核待放款客户信息通知发送邮件处理开始........", "SYSTEM");
					String url = createXintuoExcel(excelDatas, fileName);
					//发送邮件
					sendMailService.sendMailWithFile(sysParamDefineService.getSysParamValueCache("xintuo","xintuobohai.notify.to"), 
							sysParamDefineService.getSysParamValueCache("xintuo","xintuobohai.notify.cc"), fileName, stringBuffer.toString(), url);
					logger.info("渤海信托财务审核待放款客户信息通知发送邮件结束");
					loanLogService.createLog("XintuobohaiCustomerInfoMailJob", "info", "渤海信托财务审核待放款客户信息通知发送邮件结束", "SYSTEM");
				} else {
					loanLogService.createLog("XintuobohaiCustomerInfoMailJob", "info", "无数据导出....", "SYSTEM");
				}
			} catch (Exception e) {
				logger.error("渤海信托邮件发送job异常！！！", e);
			}
		}else{
			loanLogService.createLog("XintuoguominCustomerInfoMailJob", "info", "定时开关isXintuobohaiCustomerInfoMail关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isXintuobohaiCustomerInfoMail关闭，此次不执行");
		}


	}
	
    /**
     * 渤海信托财务审核待放款客户信息Excel表格
     * @param list 表格数据
     * @param fileName 文件名
     * @return 返回excel文件所在位置
     */
    private String createXintuoExcel(List<XintuoguominData> list,String fileName){
    	
		String title[] = { "借款人姓名", "借款人性别", "借款人身份证号", "联系手机", "家庭住址", "借款人账户名", "借款人账户号", "借款人开户银行", "贷款本金金额（元）",
				"借款期限（月）", "借款用途", "应偿还的借款本金及利息总金额（元）", "信托贷款协议编号", "服务费（元）", "贷款种类" };
		
		XSSFWorkbook workbook = null;
        String url="";
        FileOutputStream outputStream = null;
        try{
        	//获取生成 的excel
        	workbook = (XSSFWorkbook) ExcelUtil.getWorkbook("信息", title, list, "YYYY-MM-DD hh:mm:ss");
        	String dir = sysParamDefineService.getSysParamValue("xintuo", "xintuo.bohai.upload");
            File filePath = new File(dir);
            
            filePath.mkdirs();
            File file = new File(filePath, fileName+".xlsx");
            url = file.getAbsolutePath();
            logger.info("文件上传路径： " + url);
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
        }catch (Exception e){
            logger.error("创建渤海信托excel邮件附件异常!", e);
        }finally {
        	try {
        		if(outputStream != null){
        			outputStream.flush();
        			outputStream.close();
        		}
        		
        		if(workbook != null){
        			workbook.close();
        		}
			} catch (Exception e) {
				logger.error("关闭文件流异常!", e);;
			}
		}
        return  url;
    }
}
