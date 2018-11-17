package com.zdmoney.credit.operation.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.operation.dao.pub.IBasePublicAccountInfoDao;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfo;
import com.zdmoney.credit.operation.service.pub.IBasePublicAccountInfoService;
import com.zdmoney.credit.system.domain.SysDictionary;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;

@Service
public class BasePublicAccountInfoServiceImpl implements IBasePublicAccountInfoService {

    @Autowired
    private IBasePublicAccountInfoDao basePublicAccountInfoDao;
    
    @Autowired
    private ISequencesService sequencesService;
    
    @Autowired
    private ISysDictionaryService sysDictionaryService;
    
    public BasePublicAccountInfo get(Long id) {
        return basePublicAccountInfoDao.get(id);
    }

    public Pager findWithPg(BasePublicAccountInfo basePublicAccountInfo) {
        return basePublicAccountInfoDao.findWithPg(basePublicAccountInfo);
    }

    public List<BasePublicAccountInfo> findListByVo(BasePublicAccountInfo basePublicAccountInfo) {
        return basePublicAccountInfoDao.findListByVo(basePublicAccountInfo);
    }
    
    public String checkReceiveStatus(){
        String beginTime = null;
        String endTime = null;
        SysDictionary sysDictionary = new SysDictionary();
        sysDictionary.setCodeType("business_Account_receive_time");
        sysDictionary.setCodeTitle("开始领取时间");
        // 查询开始领取时间
        List<SysDictionary> sysDictionaryList = sysDictionaryService.findDictionaryListByVo(sysDictionary);
        if (CollectionUtils.isEmpty(sysDictionaryList)){
            return "领取开始时间不存在或者没有配置";
        }
        beginTime = sysDictionaryList.get(0).getCodeValue();
        
        sysDictionary.setCodeTitle("结束领取时间");
        // 查询结束领取时间
        sysDictionaryList = sysDictionaryService.findDictionaryListByVo(sysDictionary);
        if (CollectionUtils.isEmpty(sysDictionaryList)){
            return "领取结束时间不存在或者没有配置";
        }
        endTime = sysDictionaryList.get(0).getCodeValue();
        
        // 时刻HH:mm正则表达式
        String regex = "(0\\d{1}|1\\d{1}|2[0-3]):([0-5]\\d{1})"; 
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(beginTime);
        if(!match.matches()){
            return "领取开始时间格式不正确，正确格式应类似09:05";
        }
        match = pattern.matcher(endTime);
        if(!match.matches()){
            return "领取结束时间格式不正确，正确格式应类似17:30";
        }
        // 系统时间
        Date now = new Date();
        // 当前日期
        String sysdate = Dates.getDateTime(now, "yyyy-MM-dd");
        // 领取开始时间转换为Date类型
        Date begin = Assert.notDate(sysdate + " " + beginTime + ":00", "yyyy-MM-dd HH:mm:ss", "领取开始时间");
        // 领取结束时间转换为Date类型
        Date end = Assert.notDate(sysdate + " " + endTime + ":00", "yyyy-MM-dd HH:mm:ss", "领取结束时间");
        // 判断当前系统时间是否在领取时间范围内
        if (begin.getTime() > now.getTime() || end.getTime() < now.getTime()) {
            return "对不起,不在领取时间范围内,领取时间为" + beginTime + '至' + endTime;
        }
        return null;
    }
    
    /**
     * 更新对公账户还款信息
     * @param map
     * @return
     */
    public int savePublicAccountImportData(Map<String, String> map) {
        // 还款日期
        Date repayDate = Dates.parse(Strings.convertValue(map.get("repayDate"), String.class), "yyyy-MM-dd");
        // 还款日期转换失败
        if(Strings.isEmpty(repayDate)){
            map.put(ExcelTemplet.FEED_BACK_MSG, "还款日期转换失败！");
            return 0;
        }
        // 还款时间
        String time = Strings.convertValue(map.get("time"), String.class);
        // 还款金额
        String  amount = Strings.convertValue(map.get("amount"), String.class);
        // 对方单位
        String  secondCompany = Strings.convertValue(map.get("secondCompany"), String.class);
        // 校验对公账户是否存在
        boolean isExistAccountInfo = this.validateAccountInfo(repayDate,time,amount,secondCompany);
        if(isExistAccountInfo){
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入的数据已存在！");
            return  0;
        }
        BasePublicAccountInfo accountInfo = new BasePublicAccountInfo();
        Long id = sequencesService.getSequences(SequencesEnum.BASE_PUBLIC_ACCOUNT_INFO);
        accountInfo.setId(id);
        accountInfo.setFirstAccount(map.get("firstAccount"));
        accountInfo.setSecondAccount(map.get("secondAccount"));
        accountInfo.setType(map.get("type"));
        accountInfo.setVoucherNo(map.get("voucherNo"));
        accountInfo.setSecondCompany(secondCompany);
        accountInfo.setSecondBank(map.get("secondBank"));
        accountInfo.setPurpose(map.get("purpose"));
        accountInfo.setRemark(map.get("remark"));
        accountInfo.setComments(map.get("comments"));
        accountInfo.setTime(time);
        accountInfo.setAmount(new BigDecimal(amount));
        accountInfo.setRepayDate(repayDate);
        accountInfo.setStatus("未认领");
        String repayNo = "PUB"+Dates.getDateTime(Dates.DATAFORMAT_YYYYMMDDHHMMSS)+accountInfo.getId();
        accountInfo.setRepayNo(repayNo);
        // 获取当前登录用户的信息
        User user = UserContext.getUser();
        // 操作人
        accountInfo.setOperatorId(user.getId());
        try {
            // 更新对公账户还款信息
            basePublicAccountInfoDao.insert(accountInfo);
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入数据成功！");
        } catch (Exception e) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入数据发生异常！");
            return 0;
        }
        return 1;
    }
    
    /**
     * 按条件查询对公账户信息是否存在
     * @param repayDate
     * @param time
     * @param amount
     * @param secondCompany
     * @return
     */
    private boolean validateAccountInfo(Date repayDate, String time, String amount, String secondCompany) {
        BasePublicAccountInfo accountInfo = new BasePublicAccountInfo();
        accountInfo.setRepayDate(repayDate);
        accountInfo.setTime(time);
        accountInfo.setAmount(BigDecimal.valueOf(Double.valueOf(amount)));
        accountInfo.setSecondCompany(secondCompany);
        List<BasePublicAccountInfo> accountInfoList = this.findListByVo(accountInfo);
        if (CollectionUtils.isNotEmpty(accountInfoList)) {
            return true;
        }
        return false;
    }

    public List<Map<String, Object>> findPublicAccountReceiveInfo(BasePublicAccountInfo basePublicAccountInfo) {
        return basePublicAccountInfoDao.findPublicAccountReceiveInfo(basePublicAccountInfo);
    }

    public List<Map<String, Object>> findPublicAccountRepayReceiveInfo(BasePublicAccountInfo basePublicAccountInfo) {
        return basePublicAccountInfoDao.findPublicAccountRepayReceiveInfo(basePublicAccountInfo);
    }

    public List<BasePublicAccountInfo> findQueryResultList(BasePublicAccountInfo basePublicAccountInfo) {
        return basePublicAccountInfoDao.findQueryResultList(basePublicAccountInfo);
    }

    public Pager findWithPgByMap(Map<String, Object> params) {
        return basePublicAccountInfoDao.findWithPgByMap(params);
    }

    public int updateBasePublicAccountInfo(BasePublicAccountInfo basePublicAccountInfo) {
        return basePublicAccountInfoDao.update(basePublicAccountInfo);
    }

    public int updateAccountInfoForCancel(BasePublicAccountInfo basePublicAccountInfo) {
        return basePublicAccountInfoDao.updateAccountInfoForCancel(basePublicAccountInfo);
    }

    public int updateAccountInfoForExport(Map<String, Object> params) {
        return basePublicAccountInfoDao.updateAccountInfoForExport(params);
    }
    
    public Map<String,Object> findBusinessAccountReceiveTime(){
        Map<String,Object> result = new HashMap<String,Object>();
        // 查询开始领取时间
        SysDictionary sysDictionary = new SysDictionary();
        sysDictionary.setCodeType("business_Account_receive_time");
        sysDictionary.setCodeTitle("开始领取时间");
        List<SysDictionary> sysDictionaryList = sysDictionaryService.findDictionaryListByVo(sysDictionary);
        Assert.notCollectionsEmpty(sysDictionaryList,"开始领取时间不存在或者没有配置");
        result.put("startDictionaryId", sysDictionaryList.get(0).getId());
        result.put("startTime", sysDictionaryList.get(0).getCodeValue());
        // 查询结束领取时间
        sysDictionary.setCodeTitle("结束领取时间");
        sysDictionaryList = sysDictionaryService.findDictionaryListByVo(sysDictionary);
        Assert.notCollectionsEmpty(sysDictionaryList,"结束领取时间不存在或者没有配置");
        result.put("endDictionaryId", sysDictionaryList.get(0).getId());
        result.put("endTime", sysDictionaryList.get(0).getCodeValue());
        return result;
    }
    
    public void updateBusinessAccountReceiveTime(Map<String,Object> params){
        // 领取开始时间对应的主键值
        Long startDictionaryId = (Long) params.get("startDictionaryId");
        // 领取结束时间对应的主键值
        Long endDictionaryId = (Long) params.get("endDictionaryId");
        // 领取开始时间
        String startTime = (String) params.get("startTime");
        // 领取结束时间
        String endTime = (String) params.get("endTime");
        // 更新开始领取时间
        SysDictionary sysDictionary = new SysDictionary();
        sysDictionary.setId(startDictionaryId);
        sysDictionary.setCodeValue(startTime);
        sysDictionaryService.saveOrUpdate(sysDictionary);
        // 更新结束领取时间
        sysDictionary = new SysDictionary();
        sysDictionary.setId(endDictionaryId);
        sysDictionary.setCodeValue(endTime);
        sysDictionaryService.saveOrUpdate(sysDictionary);
    }

    @Override
    public List<Map<String, Object>> findQueryResultMapList(BasePublicAccountInfo basePublicAccountInfo) {
        return basePublicAccountInfoDao.findQueryResultMapList(basePublicAccountInfo);
    }
}
