package com.zdmoney.credit.operation.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.PublicAccountOperateTypeEnum;
import com.zdmoney.credit.common.constant.PublicAccountStatusEnum;
import com.zdmoney.credit.common.constant.PublicAccountSystemTypeEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfo;
import com.zdmoney.credit.operation.service.pub.IBasePublicAccountInfoService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 提供给车企贷系统调用的接口
 * 车企贷系统通过调用此接口，更新信贷系统对公账户流水状态（包括认领、撤销认领、渠道确认、撤销渠道确认）
 * @author 00236640
 * @version $Id: PublicAccountInterfaceController.java, v 0.1 2015年10月10日 上午11:03:36 00236640 Exp $
 */
@Controller
@RequestMapping("/businessAccount")
public class PublicAccountInterfaceController extends BaseController {
    
    /**
     * 日志输出对象
     */
    private static final Logger logger = Logger.getLogger(PublicAccountInterfaceController.class);
    
    /**
     * 正常返回状态值
     */
    private static final String SUCCESS = "SUCCESS";
    
    /**
     * 异常返回状态值
     */
    private static final String FAIL = "FAIL";
    
    /**
     * 时间格式类型
     */
    private static final String PATTERN ="yyyy-MM-dd";
    
    /**
     * 接口返回错误提示信息
     */
    private static final String BASE_MESSAGE ="更新失败，状态不是";
    
    @Autowired
    private IBasePublicAccountInfoService basePublicAccountInfoService;
    
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    
    @Autowired
    private ILoanLogService loanLogService;
    
    @RequestMapping("/updateBusinessAccountStatus")
    @ResponseBody
    public String updateBusinessAccountStatus(HttpServletRequest request, HttpServletResponse response) {
        logger.info("开始调用信贷系统对公账户还款接口......");
        // 客户端请求IP地址
        String remoteIP = null;
        try {
            remoteIP = getIpAddress(request);
        } catch (IOException e) {
            logger.error("获取请求主机的IP地址异常："+e.getMessage());
            return "获取请求主机的IP地址异常";
        }
        // 本地配置的白名单IP地址
        String legalIP = sysParamDefineService.getSysParamValueCache("sysCar", "cqdServer");
        if(Strings.isEmpty(legalIP)){
            logger.info("服务器没有配置白名单IP，请联系管理员");
            return "服务器没有配置白名单IP，请联系管理员";
        }
        // 如果客户端请求IP不在本地配置的白名单IP地址中，则提示错误信息
        if(Strings.isNotEmpty(legalIP) && legalIP.indexOf(remoteIP) == -1 ){
            logger.info("无权访问");
            return "无权访问";
        }
        // 更新信贷系统对公账户流水状态
        String result = this.updateStatusFromCqd(request, remoteIP);
        logger.info("结束调用信贷系统对公账户还款接口......");
        return result;
    }
    
    /**
     * 更新信贷系统对公账户流水状态
     * @param request
     * @param remoteIP
     * @return
     */
    private String updateStatusFromCqd(HttpServletRequest request, String remoteIP){
        // 交易日期
        String repayDate = request.getParameter("repayDate");
        // 交易时间
        String repayTime = request.getParameter("repayTime");
        // 对方单位
        String secondCompany = request.getParameter("secondCompany");
        // 交易金额
        String amount = request.getParameter("amount");
        // 操作类型
        String type = request.getParameter("type");
        // 调用系统类型
        String systemType = request.getParameter("system");
        // 打印请求参数
        String tips = "客户端IP地址："+ remoteIP+"，调用信贷系统对公账户还款接口接收的参数：{repayDate:" + repayDate
                + ",repayTime:" + repayTime + ",secondCompany:" + secondCompany
                + ",amount:" + amount + ",type:" + type + ",systemType:" + systemType + "}";
        logger.info(tips);
        // 如果调用系统类型参数没有指定，则默认是车企贷调用
        if(Strings.isEmpty(systemType)){
            systemType = PublicAccountSystemTypeEnum.车企贷.getValue();
        }
        // 请求参数校验
        String errorMsg = this.paramsCheck(repayDate,repayTime,amount,type, systemType);
        if(Strings.isNotEmpty(errorMsg)){
            return errorMsg;
        }
        // 对公账户信息存在性校验
        BasePublicAccountInfo paramAccountInfo = new BasePublicAccountInfo();
        paramAccountInfo.setRepayDate(Dates.parse(repayDate.trim(), PATTERN));
        paramAccountInfo.setTime(repayTime.trim());
        if(Strings.isNotEmpty(secondCompany)){
            paramAccountInfo.setSecondCompany(secondCompany.trim());
        }
        paramAccountInfo.setAmount(new BigDecimal(amount.trim()));
        // 根据指定条件查询对公还款信息
        List<BasePublicAccountInfo> basePublicAccountInfoList = basePublicAccountInfoService.findListByVo(paramAccountInfo);
        if(CollectionUtils.isEmpty(basePublicAccountInfoList)){
            logger.info("NO_RECORD");
            return "NO_RECORD";
        }
        // 获取第一条对公账户信息
        BasePublicAccountInfo basePublicAccountInfo = basePublicAccountInfoList.get(0);
        String memo = "";
        String interName = "";
        if (PublicAccountOperateTypeEnum.认领.getValue().equals(type)) {
            interName = PublicAccountOperateTypeEnum.认领.name();
        } else if (PublicAccountOperateTypeEnum.撤销认领.getValue().equals(type)) {
            interName = PublicAccountOperateTypeEnum.撤销认领.name();
        } else if (PublicAccountOperateTypeEnum.渠道确认.getValue().equals(type)) {
            interName = PublicAccountOperateTypeEnum.渠道确认.name();
        } else if (PublicAccountOperateTypeEnum.撤销渠道确认.getValue().equals(type)) {
            interName = PublicAccountOperateTypeEnum.撤销渠道确认.name();
        }
        if(PublicAccountSystemTypeEnum.车企贷.getValue().equals(systemType)){
            memo = PublicAccountSystemTypeEnum.车企贷.name() + interName;
        }else if(PublicAccountSystemTypeEnum.证方.getValue().equals(systemType)){
            memo = PublicAccountSystemTypeEnum.证方.name() + interName;
        }
        try{
            // 日志记录
            loanLogService.createLog(basePublicAccountInfo.getId(), tips, PublicAccountInterfaceController.class.getSimpleName(), "info", memo);
        }catch(Exception e){
            logger.info("记录日志发生异常："+ e.getMessage());
        }
        // 对公账户信息状态更新
        return this.updateStatus(basePublicAccountInfo, type.trim(), systemType.trim());
    }
    
    /**
     * 请求参数校验
     * @param repayDate
     * @param repayTime
     * @param amount
     * @param type
     * @return
     */
    private String paramsCheck(String repayDate,String repayTime,String amount,String type, String systemType){
        String message = "";
        // 交易日期
        if(Strings.isEmpty(repayDate)){
            message = "交易日期不能为空";
            logger.info(message);
            return message;
        }
        // 交易时间
        if(Strings.isEmpty(repayTime)){
            message = "交易时间不能为空";
            logger.info(message);
            return message;
        }
        // 交易金额
        if(Strings.isEmpty(amount)){
            message = "交易金额不能为空";
            logger.info(message);
            return message;
        }
        // 操作类型
        if(Strings.isEmpty(type)){
            message = "操作类型不能为空";
            logger.info(message);
            return message;
        }
        // 系统区分类型
        if(Strings.isEmpty(systemType)){
            message = "系统区分类型不能为空";
            logger.info(message);
            return message;
        }
        // 交易日期格式校验
        try {
            DateFormat format = new SimpleDateFormat(PATTERN);
            // 设置此条件（严格解析）、则转换2015-02-29这样的格式的字符串日期将抛出异常
            format.setLenient(false);
            format.parse(repayDate);
        } catch (ParseException e) {
            message = "交易日期格式错误，正确格式应为："+PATTERN;
            logger.info(message);
            return message;
        }
        // 交易金额必须是数值的校验
        if(!Strings.isDecimal(amount)){
            message = "交易金额必须是数字类型";
            logger.info(message);
            return message;
        }
        // 操作类型格式校验
        if(!PublicAccountOperateTypeEnum.认领.getValue().equals(type) 
                && !PublicAccountOperateTypeEnum.撤销认领.getValue().equals(type)
                && !PublicAccountOperateTypeEnum.渠道确认.getValue().equals(type)
                && !PublicAccountOperateTypeEnum.撤销渠道确认.getValue().equals(type)){
            message = "操作类型格式不正确，应该为[1,2,3,4]其中之一";
            logger.info(message);
            return message;
        }
        // 系统区分类型格式校验
        if (!PublicAccountSystemTypeEnum.车企贷.getValue().equals(systemType)
                && !PublicAccountSystemTypeEnum.证方.getValue().equals(systemType)) {
            message = "系统区分类型格式不正确，应该为[car,zf]其中之一";
            logger.info(message);
            return message;
        }
        return null;
    }
    
    /**
     * 对公账户信息状态更新
     * @param basePublicAccountInfo
     * @param type
     * @param systemType
     * @return
     */
    private String updateStatus(BasePublicAccountInfo basePublicAccountInfo, String type, String systemType) {
        String message = "";
        String memo = "";
        String interName = "";
        if (PublicAccountOperateTypeEnum.认领.getValue().equals(type)) {
            interName = PublicAccountOperateTypeEnum.认领.name();
        } else if (PublicAccountOperateTypeEnum.撤销认领.getValue().equals(type)) {
            interName = PublicAccountOperateTypeEnum.撤销认领.name();
        } else if (PublicAccountOperateTypeEnum.渠道确认.getValue().equals(type)) {
            interName = PublicAccountOperateTypeEnum.渠道确认.name();
        } else if (PublicAccountOperateTypeEnum.撤销渠道确认.getValue().equals(type)) {
            interName = PublicAccountOperateTypeEnum.撤销渠道确认.name();
        }
        if (PublicAccountSystemTypeEnum.车企贷.getValue().equals(systemType)) {
            // 校验和设置对公还款状态（车企贷）
            message = this.checkStatus4Car(basePublicAccountInfo, type);
            if (Strings.isNotEmpty(message)) {
                return message;
            }
            memo = PublicAccountSystemTypeEnum.车企贷.name() + interName;
        } else if (PublicAccountSystemTypeEnum.证方.getValue().equals(systemType)) {
            message = this.checkStatus4ZF(basePublicAccountInfo, type);
            if (Strings.isNotEmpty(message)) {
                return message;
            }
            memo = PublicAccountSystemTypeEnum.证方.name() + interName;
        }
        basePublicAccountInfo.setRecTime(new Date());
        try {
            basePublicAccountInfoService.updateBasePublicAccountInfo(basePublicAccountInfo);
            // 日志记录
            loanLogService.createLog(basePublicAccountInfo.getId(), "调用信贷对公还款接口成功", PublicAccountInterfaceController.class.getSimpleName(), "info", memo);
        } catch (Exception e) {
            logger.error("更新对公账户还款流水状态异常：" + e.getMessage());
            return FAIL;
        }
        return SUCCESS;
    }

    /**
     * 校验和设置对公还款状态（车企贷）
     * @param basePublicAccountInfo
     * @param type
     * @param status
     * @return
     */
    private String checkStatus4Car(BasePublicAccountInfo basePublicAccountInfo, String type) {
        String message = null;
        String subMsg = "已被车企贷";
        // 状态
        String status = basePublicAccountInfo.getStatus();
        // 确认领取
        if (PublicAccountOperateTypeEnum.认领.getValue().equals(type)) {
            if (PublicAccountStatusEnum.未认领.getValue().equals(status)) {
                basePublicAccountInfo.setStatus(PublicAccountStatusEnum.被车企贷认领.getValue());
            } else if (PublicAccountStatusEnum.被车企贷认领.getValue().equals(status)) {
                logger.info(subMsg + PublicAccountOperateTypeEnum.认领.getValue());
                return SUCCESS;
            } else {
                message = BASE_MESSAGE + PublicAccountStatusEnum.未认领.getValue();
                logger.info(message);
                return message;
            }
            // 撤销领取
        } else if (PublicAccountOperateTypeEnum.撤销认领.getValue().equals(type)) {
            if (PublicAccountStatusEnum.被车企贷认领.getValue().equals(status)) {
                basePublicAccountInfo.setStatus(PublicAccountStatusEnum.未认领.getValue());
            } else if (PublicAccountStatusEnum.未认领.getValue().equals(status)) {
                logger.info(subMsg + PublicAccountOperateTypeEnum.撤销认领.getValue());
                return SUCCESS;
            } else {
                message = BASE_MESSAGE + PublicAccountStatusEnum.被车企贷认领.getValue();
                logger.info(message);
                return message;
            }
            // 渠道确认
        } else if (PublicAccountOperateTypeEnum.渠道确认.getValue().equals(type)) {
            if (PublicAccountStatusEnum.未认领.getValue().equals(status)) {
                basePublicAccountInfo.setStatus(PublicAccountStatusEnum.被车企贷无需认领.getValue());
            } else if (PublicAccountStatusEnum.被车企贷无需认领.getValue().equals(status)) {
                logger.info(subMsg + PublicAccountOperateTypeEnum.渠道确认.getValue());
                return SUCCESS;
            } else {
                message = BASE_MESSAGE + PublicAccountStatusEnum.未认领.getValue();
                logger.info(message);
                return message;
            }
            // 撤销渠道确认
        } else if (PublicAccountOperateTypeEnum.撤销渠道确认.getValue().equals(type)) {
            if (PublicAccountStatusEnum.被车企贷无需认领.getValue().equals(status)) {
                basePublicAccountInfo.setStatus(PublicAccountStatusEnum.未认领.getValue());
            } else if (PublicAccountStatusEnum.未认领.getValue().equals(status)) {
                logger.info(subMsg + PublicAccountOperateTypeEnum.撤销渠道确认.getValue());
                return SUCCESS;
            } else {
                message = BASE_MESSAGE + PublicAccountStatusEnum.被车企贷无需认领.getValue();
                logger.info(message);
                return message;
            }
        } else {
            message = "操作类型错误";
            logger.info(message);
            return message;
        }
        return message;
    }
    
    /**
     * 校验和设置对公还款状态（证方）
     * @param basePublicAccountInfo
     * @param type
     * @param status
     * @return
     */
    private String checkStatus4ZF(BasePublicAccountInfo basePublicAccountInfo, String type) {
        String message = null;
        String subMsg = "已被证方";
        // 状态
        String status = basePublicAccountInfo.getStatus();
        // 确认领取
        if (PublicAccountOperateTypeEnum.认领.getValue().equals(type)) {
            if (PublicAccountStatusEnum.未认领.getValue().equals(status)) {
                basePublicAccountInfo.setStatus(PublicAccountStatusEnum.被证方认领.getValue());
            } else if (PublicAccountStatusEnum.被证方认领.getValue().equals(status)) {
                logger.info(subMsg + PublicAccountOperateTypeEnum.认领.getValue());
                return SUCCESS;
            } else {
                message = BASE_MESSAGE + PublicAccountStatusEnum.未认领.getValue();
                logger.info(message);
                return message;
            }
            // 撤销领取
        } else if (PublicAccountOperateTypeEnum.撤销认领.getValue().equals(type)) {
            if (PublicAccountStatusEnum.被证方认领.getValue().equals(status)) {
                basePublicAccountInfo.setStatus(PublicAccountStatusEnum.未认领.getValue());
            } else if (PublicAccountStatusEnum.未认领.getValue().equals(status)) {
                logger.info(subMsg + PublicAccountOperateTypeEnum.撤销认领.getValue());
                return SUCCESS;
            } else {
                message = BASE_MESSAGE + PublicAccountStatusEnum.被证方认领.getValue();
                logger.info(message);
                return message;
            }
            // 渠道确认
        } else if (PublicAccountOperateTypeEnum.渠道确认.getValue().equals(type)) {
            if (PublicAccountStatusEnum.未认领.getValue().equals(status)) {
                basePublicAccountInfo.setStatus(PublicAccountStatusEnum.被证方无需认领.getValue());
            } else if (PublicAccountStatusEnum.被证方无需认领.getValue().equals(status)) {
                logger.info(subMsg + PublicAccountOperateTypeEnum.渠道确认.getValue());
                return SUCCESS;
            } else {
                message = BASE_MESSAGE + PublicAccountStatusEnum.未认领.getValue();
                logger.info(message);
                return message;
            }
            // 撤销渠道确认
        } else if (PublicAccountOperateTypeEnum.撤销渠道确认.getValue().equals(type)) {
            if (PublicAccountStatusEnum.被证方无需认领.getValue().equals(status)) {
                basePublicAccountInfo.setStatus(PublicAccountStatusEnum.未认领.getValue());
            } else if (PublicAccountStatusEnum.未认领.getValue().equals(status)) {
                logger.info(subMsg + PublicAccountOperateTypeEnum.撤销渠道确认.getValue());
                return SUCCESS;
            } else {
                message = BASE_MESSAGE + PublicAccountStatusEnum.被证方无需认领.getValue();
                logger.info(message);
                return message;
            }
        } else {
            message = "操作类型错误";
            logger.info(message);
            return message;
        }
        return message;
    }
}
