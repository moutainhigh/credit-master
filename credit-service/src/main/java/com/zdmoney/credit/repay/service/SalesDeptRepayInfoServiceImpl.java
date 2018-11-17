package com.zdmoney.credit.repay.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanManageSalesDepLog;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanManageSalesDepLogService;
import com.zdmoney.credit.repay.dao.pub.ISalesDeptRepayInfoDao;
import com.zdmoney.credit.repay.domain.SalesDeptRepayInfo;
import com.zdmoney.credit.repay.service.pub.ISalesDeptRepayInfoService;
import com.zdmoney.credit.system.dao.pub.IPersonInfoDao;
import com.zdmoney.credit.system.domain.PersonInfo;

@Service
public class SalesDeptRepayInfoServiceImpl implements ISalesDeptRepayInfoService {

    /**
     * 日志输出对象
     */
    private static final Logger logger = Logger.getLogger(SalesDeptRepayInfoServiceImpl.class);
    
    @Autowired
    private ISalesDeptRepayInfoDao salesDeptRepayInfoDao;
    
    @Autowired
    private ILoanBaseDao loanBaseDao;
    
    @Autowired
    private IPersonInfoDao personInfoDao;
    
    @Autowired
    private ILoanManageSalesDepLogService loanManageSalesDepLogService;
    
    public List<Map<String, Object>> getSalesDeptInfo(Map<String, Object> params) {
        return salesDeptRepayInfoDao.getSalesDeptInfo(params);
    }

    public Pager findWithPg(SalesDeptRepayInfo salesDeptRepayInfo) {
        return salesDeptRepayInfoDao.findWithPg(salesDeptRepayInfo);
    }

    public int updateLoanBaseInfo(LoanBase loanBase) {
        return loanBaseDao.update(loanBase);
    }
    
    /**
     * 更新借款人债权相关信息
     * @param loanInfo
     * @param oldCrmId
     * @return
     */
    public boolean updateBorrowerLoanInfo(VLoanInfo loanInfo, Long oldCrmId) {
        // 登录用户的信息
        User userInfo = UserContext.getUser();
        // 债权Id
        Long loanId = loanInfo.getId();
        // 管理营业机构Id
        Long salesDepartmentId = loanInfo.getSalesDepartmentId();
        try {
            // 变更借款人债权信息
            LoanBase loanBase = new LoanBase();
            loanBase.setId(loanId);
            loanBase.setSalesDepartmentId(salesDepartmentId);
            loanBase.setCrmId(loanInfo.getCrmId());
            loanBaseDao.update(loanBase);
            // 更新借款人的所属管理部门
            PersonInfo personInfo = new PersonInfo();
            personInfo.setId(loanInfo.getBorrowerId());
            personInfo.setSalesDepartmentId(salesDepartmentId);
            personInfoDao.update(personInfo);
            // 变更和记录日志
            writeLog(loanInfo,oldCrmId,userInfo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 变更和记录管理门店变更日志
     * @param loanInfo
     * @param oldCrmId
     * @param userInfo
     */
    private void writeLog(VLoanInfo loanInfo, Long oldCrmId,User userInfo){
        // 债权Id
        Long loanId = loanInfo.getId();
        // 管理营业机构Id
        Long salesDepartmentId = loanInfo.getSalesDepartmentId();
        // 客服
        Long crmId = userInfo.getId();
        // 登录用户名
        String userName = userInfo.getName();
        // 系统时间
        Date sysdate = new Date();
        try {
            // 更新最近一条管理部门变更日志的记录（结束时间修改为当前时间）
            LoanManageSalesDepLog updateLog = new LoanManageSalesDepLog();
            updateLog.setLoanId(loanId);
            updateLog.setEndDate(sysdate);
            updateLog.setUpdateTime(sysdate);
            updateLog.setUpdator(userName);
            loanManageSalesDepLogService.updateLastManageSalesDeptLog(updateLog);
            LoanManageSalesDepLog loanManageSalesDepLog = new LoanManageSalesDepLog();
            loanManageSalesDepLog.setLoanId(loanId);
            // 营业机构Id
            loanManageSalesDepLog.setSalesDepId(salesDepartmentId);
            // 开始时间设置为当前系统时间
            loanManageSalesDepLog.setBeginDate(sysdate);
            // 结束时间默认设置为：2999-12-31
            Date endDate = Dates.parse("2999-12-31", "yyyy-MM-dd");
            loanManageSalesDepLog.setEndDate(endDate);
            // 旧客服Id
            loanManageSalesDepLog.setOldCrmId(oldCrmId);
            // 新客服Id
            loanManageSalesDepLog.setNewCrmId(loanInfo.getCrmId());
            // 操作客服Id
            loanManageSalesDepLog.setOperatorId(crmId);
            // 创建时间
            loanManageSalesDepLog.setCreateTime(sysdate);
            // 创建人
            loanManageSalesDepLog.setCreator(userName);
            // 修改时间
            loanManageSalesDepLog.setUpdateTime(sysdate);
            // 更新人
            loanManageSalesDepLog.setUpdator(userName);
            // 记录管理部门变更的日志记录
            loanManageSalesDepLogService.insertLoanManageSalesDeptLog(loanManageSalesDepLog);
        } catch (Exception e) {
            logger.error("变更和记录管理门店变更日志异常：" + e.getMessage());
        }
    }
}
