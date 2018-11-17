package com.zdmoney.credit.job;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.exceldata.XintuoguominData;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.xingtuo.dao.pub.IXintuoguominDataDao;
import com.zdmoney.credit.xintuo.domain.XintuoguominDataDomain;

@Service
public class XintuoguominCustomerInfoMailJob {
	private static final Logger logger = Logger.getLogger(XintuoguominCustomerInfoMailJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private ISendMailService sendMailService;
	@Autowired
	private IXintuoguominDataDao dao;

	public void doNoticeNormal() {
    	String isXintuoguominCustomerInfoMail = sysParamDefineService.getSysParamValue("sysJob", "isXintuoguominCustomerInfoMail");
		if(!Const.isClosing.equals(isXintuoguominCustomerInfoMail)){
			try {
				List<XintuoguominDataDomain> datas = dao.getXintuoguominDataJob();
				
				List<XintuoguominData> excelDatas = new ArrayList<XintuoguominData>();
				for (XintuoguominDataDomain data : datas) {
					XintuoguominData excelData = data.toXintuoguominData();
					excelDatas.add(excelData);
				}
				
		        String now = Dates.getDateTime(new Date(), "yyyy年MM月dd日");
		        String title = "国民信托"+now+"财务审核待放款客户信息";
				
		        StringBuffer stringBuffer= new StringBuffer();
		        stringBuffer.append("  附件为"+now+"，国民信托财务审核待放款客户信息，请查收，谢谢！");
				if (excelDatas.size() > 0) {
					logger.info("国民信托财务审核待放款客户信息通知发送邮件处理开始........");
					loanLogService.createLog("XintuoguominCustomerInfoMailJob", "info", "国民信托财务审核待放款客户信息通知发送邮件处理开始........", "SYSTEM");
					String url = createXintuoExcel(excelDatas, title);
					
					sendMailService.sendMailWithFile(sysParamDefineService.getSysParamValueCache("xintuo","xintuoguomin.notify.to"), 
							sysParamDefineService.getSysParamValueCache("xintuo","xintuoguomin.notify.cc"), title, stringBuffer.toString(), url);
					logger.info("国民信托财务审核待放款客户信息通知发送邮件结束");
					loanLogService.createLog("XintuojihuaCustomerInfo4PMJob", "info", "国民信托财务审核待放款客户信息通知发送邮件结束", "SYSTEM");
				} else {
					loanLogService.createLog("XintuoguominCustomerInfoMailJob", "info", "无数据导出....", "SYSTEM");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			loanLogService.createLog("XintuoguominCustomerInfoMailJob", "info", "定时开关isXintuoguominCustomerInfoMail关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isXintuoguominCustomerInfoMail关闭，此次不执行");
		}


	}
	
    /**
     * 国民信托财务审核待放款客户信息Excel表格
     * @param list 表格数据
     * @return
     */
    private String createXintuoExcel(List<XintuoguominData> list,String title){
        @SuppressWarnings("resource")
		HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("信息");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("借款人姓名");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("借款人性别");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("借款人身份证号");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("联系手机");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("家庭住址");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("借款人账户名");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("借款人账户号");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("借款人开户银行");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("贷款本金金额（元）");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("借款期限（月）");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("借款用途");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("应偿还的借款本金及利息总金额（元）");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("信托贷款协议编号");
        cell.setCellStyle(style);
        cell = row.createCell(13);
        cell.setCellValue("服务费（元）");
        cell.setCellStyle(style);
        cell = row.createCell(14);
        cell.setCellValue("贷款种类");
        cell.setCellStyle(style);


        for (int i = 0; i < list.size(); i++)
        {
            row = sheet.createRow(i + 1);
            XintuoguominData data =  list.get(i);
            row.createCell(0).setCellValue(data.getName());
            row.createCell(1).setCellValue(data.getSex());
            row.createCell(2).setCellValue(data.getIdnum());
            row.createCell(3).setCellValue(data.getMphone());
            row.createCell(4).setCellValue(data.getAddress());
            row.createCell(5).setCellValue(data.getBorrower_name());
            row.createCell(6).setCellValue(data.getAccount());
            row.createCell(7).setCellValue(data.getBank_full_name());
            row.createCell(8).setCellValue(data.getPact_money().doubleValue());
            row.createCell(9).setCellValue(data.getTime());
            row.createCell(10).setCellValue(data.getPurpose());
            row.createCell(11).setCellValue(data.getRepayment_all().doubleValue());
            row.createCell(12).setCellValue(data.getContract_num());
            row.createCell(13).setCellValue(data.getRate_sum().doubleValue());
            row.createCell(14).setCellValue(data.getLoan_type());

        }
        String url="";
        try{
        	String dir = sysParamDefineService.getSysParamValue("xintuo", "xintuo.guomin.upload");
            File filePath = new File(dir);
            filePath.mkdirs();
            File file = new File(filePath,title+".xls");
            url = file.getAbsolutePath();
            System.out.print(url);
            FileOutputStream fout = new FileOutputStream(file);
            wb.write(fout);
            fout.flush();
            wb.close();
            
            fout.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.print(url);
        }
        return  url;
    }
}
