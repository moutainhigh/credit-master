package com.zdmoney.credit.system.service.pub;

import java.net.UnknownHostException;
import java.util.List;

import com.zdmoney.credit.common.util.exceldata.JimuData;
import com.zdmoney.credit.common.util.exceldata.XintuoData;

public interface ISendMailService {
    public void sendMail(String[] to, String[] cc, String title, String content, String[] files);
    public void sendTextMail(String receiverEmail, String title, String content) throws UnknownHostException;
    /**
     * 积木盒子发送邮件
     * @param receiverEmail 收件邮箱
     * @param Cc        抄送邮箱
     * @param title     邮箱标题
     * @param content   邮箱内容
     * @throws UnknownHostException
     */
    public void sendPaymentRiskMail(String receiverEmail,String Cc , String title, String content) throws UnknownHostException;
    /**
     * 发送带有附件的邮件
     * @param receiverEmail 收件邮箱
     * @param Cc        抄送邮箱
     * @param title     邮箱标题
     * @param content   邮箱内容
     * @param list      附件数据
     * @throws UnknownHostException
     */
    public void sendXintuoCustomerAttachMail(String receiverEmail,String Cc , String title, String content,List<XintuoData> list) throws UnknownHostException;
    /**
     * 发送带有附件的邮件
     * @param receiverEmail 收件邮箱
     * @param Cc        抄送邮箱
     * @param title     邮箱标题
     * @param content   邮箱内容
     * @param list      附件数据
     * @throws UnknownHostException
     */
    public void sendPaymentRiskAttachMail(String receiverEmail,String Cc , String title, String content,List<JimuData> list) throws UnknownHostException;
    /**
     * 创建信托白名单Excel表格
     * @param list 表格数据
     * @return
     */
    public String CreateXintuoExcel(List<XintuoData> list);
    public String CreateExcel(List<JimuData> list);
    /**
     * 发送带有附件的邮件
     * @param receiverEmail 收件邮箱
     * @param Cc        抄送邮箱
     * @param title     邮箱标题
     * @param content   邮箱内容
     * @param fileUrl      附件路径
     * @throws UnknownHostException
     */
    public void sendMailWithFile(String receiverEmail,String Cc , String title, String content,String fileUrl) throws UnknownHostException;
    
    /**
     * 向用户userCode的email地址发送重置密码链接邮件
     * @param userCode
     * @param email
     * @param token 操作令牌
     * @throws Exception
     */
    public void sendForgetPwdMail(String userCode, String email, String token) throws Exception;
}
