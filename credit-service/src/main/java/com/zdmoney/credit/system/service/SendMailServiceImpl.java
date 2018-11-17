package com.zdmoney.credit.system.service;

/** 
 * @(#)SendEmailUtil.java 1.0.0 2013年12月23日 下午3:01:18  
 *  
 * Copyright © 2013 证大财富.  All rights reserved.  
 */


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.SystemUtil;
import com.zdmoney.credit.common.util.exceldata.JimuData;
import com.zdmoney.credit.common.util.exceldata.XintuoData;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 
 * 
 * @author zhanghao
 * @version $Revision:1.0.0, $Date: 2013年12月23日 下午3:01:18 $
 */
@Service
public class SendMailServiceImpl implements ISendMailService{
	private static Logger logger = Logger.getLogger(SendMailServiceImpl.class);

	@Autowired
	private ISysParamDefineService sysParamDefineService;

	@Override
    public void sendMail(String[] to, String[] cc, String title, String content, String[] files) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        MimeMessage message = mailSender.createMimeMessage();

        mailSender.setHost(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.host"));
        mailSender.setUsername(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));
        mailSender.setPassword(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.password"));
        mailSender.setDefaultEncoding("UTF-8");
        Properties property = new Properties();
        property.put("mail.smtp.auth", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.auth"));
        property.put("mail.smtp.timeout", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.timeout"));
        mailSender.setJavaMailProperties(property);

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));
            helper.setTo(checkAddress(to));
            helper.setCc(checkAddress(cc));
            helper.setSubject(title);
            helper.setText(content);

            for(String filePath : files){
                FileSystemResource file = new FileSystemResource(filePath);
                helper.addAttachment(MimeUtility.encodeWord(file.getFilename(), "UTF-8", null), file);
            }

        }catch (MessagingException e) {
            throw new MailParseException(e);
        }catch (UnsupportedEncodingException e){
            throw new MailParseException(e);
        }
        mailSender.send(message);
    }

    private String[] checkAddress(String[] addrs){
        List<String> list = new ArrayList<String>();
        for(String addr : addrs){
            if(addr == null || "".equals(addr.trim()))
                continue;
            list.add(addr.trim());
        }
        return (String[])list.toArray(new String[]{});
    }
	
    @Override
	public void sendTextMail(String receiverEmail, String title, String content) throws UnknownHostException {

        title = title + "(" + SystemUtil.getSystemLocalIp() + ")";
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		SimpleMailMessage mail = new SimpleMailMessage();
		mailSender.setHost(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.host"));
		mailSender.setUsername(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));
		mailSender.setPassword(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.password"));
		mailSender.setDefaultEncoding("UTF-8");
		Properties property = new Properties();
		property.put("mail.smtp.auth", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.auth"));
		property.put("mail.smtp.timeout", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.timeout"));
		mailSender.setJavaMailProperties(property);
		String[] tos = StringUtils.isBlank(receiverEmail) ? sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.to").split(";") : receiverEmail.split(";");
		try {
			for (int i = 0; i < tos.length; i++) {
				mail.setTo(tos[i]);
				mail.setSubject(title);
				mail.setText(content == null ? "" : content);
				mail.setFrom(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));
				mailSender.send(mail);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
    public void sendPaymentRiskMail(String receiverEmail,String Cc , String title, String content) throws UnknownHostException {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        MimeMessage mailMessage = mailSender.createMimeMessage();
        mailSender.setHost(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.host"));
        mailSender.setUsername(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));
        mailSender.setPassword(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.password"));
        mailSender.setDefaultEncoding("UTF-8");
        Properties property = new Properties();
        String auth = sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.auth");
        property.put("mail.smtp.auth", auth);
        String timeout = sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.timeout");
        property.put("mail.smtp.timeout", timeout);
        mailSender.setJavaMailProperties(property);
        String[] tos = StringUtils.isBlank(receiverEmail) ? sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.to").split(";") : receiverEmail.split(";");
        String[] copys = Cc.split(";");
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true,"UTF-8");
           // for (int i = 0; i < tos.length; i++) {

                messageHelper.setTo(tos);
                if(copys.length>0){
                    messageHelper.setCc(copys);
                }
                messageHelper.setSubject(title);
                messageHelper.setText(content == null ? "" : content);
                messageHelper.setFrom(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));
                mailSender.send(mailMessage);
                System.out.println("邮件发送成功..");
            // }

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("邮件发送失败..");
            System.out.println(e.getMessage());
            return;
        }
    }

    @Override
    public void sendXintuoCustomerAttachMail(String receiverEmail,String Cc , String title, String content,List<XintuoData> list) throws UnknownHostException {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        MimeMessage mailMessage = mailSender.createMimeMessage();
        mailSender.setHost(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.host"));
        mailSender.setUsername(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));
        mailSender.setPassword(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.password"));
        mailSender.setDefaultEncoding("UTF-8");
        Properties property = new Properties();
        property.put("mail.smtp.auth", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.auth"));
        property.put("mail.smtp.timeout", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.timeout"));
        mailSender.setJavaMailProperties(property);
        String[] tos = StringUtils.isBlank(receiverEmail) ? sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.to").split(";") : receiverEmail.split(";");
        String[] copys = Cc.split(";");
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true,"UTF-8");

            messageHelper.setTo(tos);
            if(copys.length>0){
                messageHelper.setCc(copys);
            }
            messageHelper.setSubject(title);
            messageHelper.setText(content == null ? "" : content);
            messageHelper.setFrom(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));

            //表格路径
            String url = CreateXintuoExcel(list);
            File file = new File(url);
            messageHelper.addAttachment(MimeUtility.encodeWord(file.getName(), "UTF-8", null), file);

            mailSender.send(mailMessage);
            System.out.println("邮件发送成功..");
            file.delete();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("邮件发送失败..");
            return;
        }
    }

    @Override
    public void sendPaymentRiskAttachMail(String receiverEmail,String Cc , String title, String content,List<JimuData> list) throws UnknownHostException {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        MimeMessage mailMessage = mailSender.createMimeMessage();
        mailSender.setHost(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.host"));
        mailSender.setUsername(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));
        mailSender.setPassword(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.password"));
        mailSender.setDefaultEncoding("UTF-8");
        Properties property = new Properties();
        property.put("mail.smtp.auth", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.auth"));
        property.put("mail.smtp.timeout", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.timeout"));
        mailSender.setJavaMailProperties(property);
        String[] tos = StringUtils.isBlank(receiverEmail) ? sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.to").split(";") : receiverEmail.split(";");
        String[] copys = Cc.split(";");
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true,"UTF-8");

            messageHelper.setTo(tos);
            if(copys.length>0){
                messageHelper.setCc(copys);
            }
            messageHelper.setSubject(title);
            messageHelper.setText(content == null ? "" : content);
            messageHelper.setFrom(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));

            //表格路径
            String url = CreateExcel(list);
            File file = new File(url);
            messageHelper.addAttachment(MimeUtility.encodeWord(file.getName(), "UTF-8", null), file);

           mailSender.send(mailMessage);
           System.out.println("邮件发送成功..");
            file.delete();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("邮件发送失败..");
            return;
        }
    }

    @Override
    public String CreateXintuoExcel(List<XintuoData> list){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("信息");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("身份证号码");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("手机号码");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("银行卡号");
        cell.setCellStyle(style);

        for (int i = 0; i < list.size(); i++)
        {
            row = sheet.createRow(i + 1);
            XintuoData data =  list.get(i);
            row.createCell(0).setCellValue(data.getName());
            row.createCell(1).setCellValue(data.getIdnum());
            row.createCell(2).setCellValue(data.getPhone());
            row.createCell(3).setCellValue(data.getAccount());
        }
        String url="";
        FileOutputStream fout = null;
        try{
        	String dir = sysParamDefineService.getSysParamValue("xintuo", "xintuo.huaao.upload");
            File filePath = new File(dir);
            filePath.mkdir();
            File file = new File(filePath,"华澳信托白名单客户信息.xls");
            url = file.getAbsolutePath();
            System.out.print(url);
            fout = new FileOutputStream(file);
            wb.write(fout);
            
        }catch (Exception e){
            e.printStackTrace();
            System.out.print(url);
        } finally {
        	try {
				wb.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        }
        return  url;
    }
    /**
     * 创建Excel表格
     * @param list 表格数据
     * @return
     */
    @Override
    public String CreateExcel(List<JimuData> list){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("信息");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("客户姓名");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("身份证号码");
        cell.setCellStyle(style);

        for (int i = 0; i < list.size(); i++)
        {
            row = sheet.createRow(i + 1);
            JimuData data =  list.get(i);
            row.createCell(0).setCellValue(data.getName());
            row.createCell(1).setCellValue(data.getIdnum());
        }
        String url="";
        FileOutputStream fout = null;
        try{
        	String dir = sysParamDefineService.getSysParamValue("jimu", "jimu.uploads");
            File filePath = new File(dir);
            filePath.mkdir();
            File file = new File(filePath,"积木盒子债权提前结清信息.xls");
            url = file.getAbsolutePath();
            System.out.print(url);
            fout = new FileOutputStream(file);
            wb.write(fout);
            fout.close();
        }catch (Exception e){
        	logger.error(url);
            logger.error("创建Excel表格异常", e);
        }finally {
        	try {
				wb.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        }
        return  url;
    }


    @Override
    public void sendMailWithFile(String receiverEmail,String Cc , String title, String content,String fileUrl) throws UnknownHostException {
    	logger.info("邮件收件人：" + receiverEmail + " --->抄送: "+Cc);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        MimeMessage mailMessage = mailSender.createMimeMessage();
        mailSender.setHost(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.host"));
        mailSender.setUsername(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));
        mailSender.setPassword(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.password"));
        mailSender.setDefaultEncoding("UTF-8");
        Properties property = new Properties();
        property.put("mail.smtp.auth", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.auth"));
        property.put("mail.smtp.timeout", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.timeout"));
        mailSender.setJavaMailProperties(property);
        String[] tos = StringUtils.isBlank(receiverEmail) ? sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.to").split(";") : receiverEmail.split(";");
        String[] copys = Cc.split(";");
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true,"UTF-8");

            messageHelper.setTo(tos);
            if(copys.length>0){
                messageHelper.setCc(copys);
            }
            messageHelper.setSubject(title);
            messageHelper.setText(content == null ? "" : content);
            messageHelper.setFrom(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));

            //表格路径
            File file = new File(fileUrl);
            messageHelper.addAttachment(MimeUtility.encodeWord(file.getName(), "UTF-8", null), file);

            mailSender.send(mailMessage);
            logger.info("邮件发送成功...");
            file.delete();

        } catch (Exception e) {
            logger.error("邮件发送失败..", e);
            return;
        }
    }

	@Override
	public void sendForgetPwdMail(String userCode, String email, String token) throws Exception {
		String link = Strings.format(sysParamDefineService.getSysParamValueCache("codeHelper", "resetpwd.address"), userCode, token);
		String html = "<html>"
				+ "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/><title>重置密码</title></head>"
				+ "<body>"
				+ "<p>亲爱的用户" + userCode + "：您好！</p>"
				+ "<p>您收到这封邮件是因为您请求重置您的密码。如不是您本人发起的请求，请忽略此邮件。</p>"
				+ "<p>请点击以下链接，或将其复制到浏览器的地址输入框，然后单击回车访问重置密码页面。</p>"
				+ "<a href=\""+ link +"\">" + link + "</a>"
				+ "<p>请在("+ Dates.getDateTime() + ")一小时之内完成操作。</p>"
				+ "</body>"
				+ "</html>";
		
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        MimeMessage message = mailSender.createMimeMessage();

        mailSender.setHost(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.host"));
        mailSender.setUsername(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));
        mailSender.setPassword(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.password"));
        mailSender.setDefaultEncoding("UTF-8");
        Properties property = new Properties();
        property.put("mail.smtp.auth", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.auth"));
        property.put("mail.smtp.timeout", sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.timeout"));
        mailSender.setJavaMailProperties(property);

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sysParamDefineService.getSysParamValueCache("mail", "mail.smtp.username"));
            helper.setTo(checkAddress(new String[]{email}));
            helper.setSubject("信贷系统-密码重置");
            helper.setText(html, true);

        }catch (MessagingException e) {
            throw new MailParseException(e);
        }
        mailSender.send(message);
		
	}




	public static void main(String[] args) {
		try {
			new SendMailServiceImpl().sendTextMail("anfq@zendaimoney.com", "test", "33333333333333333");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}


}
