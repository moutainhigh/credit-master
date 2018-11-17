package com.zdmoney.credit.repay.service;

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
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.repay.dao.pub.IBasePrivateAccountInfoDao;
import com.zdmoney.credit.repay.domain.BasePrivateAccountInfo;
import com.zdmoney.credit.repay.service.pub.IBasePrivateAccountInfoService;
import com.zdmoney.credit.system.domain.SysDictionary;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;

@Service
public class BasePrivateAccountInfoServiceImpl implements IBasePrivateAccountInfoService {
    
    /**
     * 日期格式1
     */
    private final static String DATE_PATTERN_1 = "yyyyMMdd";
    /**
     * 日期格式2
     */
    private final static String DATE_PATTERN_2 = "HHmmss";
    
    /**
     * 日期格式3
     */
    private final static String DATE_PATTERN_3 = "HH:mm:ss";

    @Autowired
    private IBasePrivateAccountInfoDao basePrivateAccountInfoDao;
    
    @Autowired
    private ISequencesService sequencesService;
    
    @Autowired
    private ISysDictionaryService sysDictionaryService;

    /**
     * 按Id查询对私还款信息
     */
    public BasePrivateAccountInfo get(Long id) {
        return basePrivateAccountInfoDao.get(id);
    }

    /**
     * 查询对私还款信息（带分页查询）
     */
    public Pager findWithPg(BasePrivateAccountInfo basePrivateAccountInfo) {
        return basePrivateAccountInfoDao.findWithPg(basePrivateAccountInfo);
    }

    /**
     * 查询对私还款信息
     */
    public List<BasePrivateAccountInfo> findListByVo(BasePrivateAccountInfo basePrivateAccountInfo) {
        return basePrivateAccountInfoDao.findListByVo(basePrivateAccountInfo);
    }

    /**
     * 保存对私还款信息
     */
    public void savePrivateAccountInfo(List<Map<String, String>> sheetDataList) {
        // 获取当前登录用户的信息
        User user = UserContext.getUser();
        for (Map<String, String> map : sheetDataList) {
            // 交易日期
            String tradeDate = map.get("tradeDate");
            // 交易日期
            String tradeTime = map.get("tradeTime");
            // 交易金额
            String tradeAmount = map.get("tradeAmount");
            try {
                Assert.notNullAndEmpty(tradeDate, "交易日期");
                Assert.notNullAndEmpty(tradeTime, "交易时间");
                Assert.notNullAndEmpty(tradeAmount, "交易金额");
                Assert.notDate(tradeDate, DATE_PATTERN_1, "交易日期格式错误，应类似20140701");
                // 交易时间格式转换，主要为了将银行类似于20.30.30的时间转换为203030格式
                //tradeTime = tradeTime.replaceAll("\\D", "");
                //tradeTime = this.editTradeTime(tradeTime);
                Date tempDate = Assert.notDate(tradeTime, DATE_PATTERN_2, "交易时间格式错误，应类似203030格式");
                map.put("tradeTime", Dates.getDateTime(tempDate, DATE_PATTERN_3));
                try {
                    new BigDecimal(tradeAmount);
                } catch (Exception e) {
                    throw new PlatformException(ResponseEnum.FULL_MSG,"交易金额类型不正确");
                }
                // 交易金额必须大于零的校验
                if(new BigDecimal(tradeAmount).doubleValue() <= 0){
                    throw new PlatformException(ResponseEnum.FULL_MSG,"交易金额必须大于零");
                }
                // 保存导入数据
                this.savePrivateAccountInfo(map,user);
            } catch (PlatformException e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, e.getMessage());
            } catch (Exception e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, "发生不可预知的异常");
            }
        }
    }
    
    /**
     * 转换时间格式
     * 20.30.30 ==》203030 （合法时间格式）
     * 20:30:30 ==》203030 （合法时间格式）
     * 9.9.9 ==》090909 （合法时间格式）
     * 9:9:9 ==》090909（合法时间格式）
     * 090909 ==》090909（合法时间格式）
     * 20.3030 ==》20.3030（非法时间格式）
     * 20:3030 ==》20:3030（非法时间格式）
     * 24:30:30 ==》24:30:30（非法时间格式）
     * 20:60:30 ==》20:60:30（非法时间格式）
     * @param tradeTime
     * @return
     */
    /*private String editTradeTime(String tradeTime){
        String[] timeArr = null;
        StringBuilder timeBuilder = new StringBuilder();
        if(tradeTime.indexOf("\\D") > 0){
            timeArr = tradeTime.split("\\D");
        }else if(tradeTime.indexOf(":") > 0){
            timeArr = tradeTime.split(":");
        }else if(tradeTime.indexOf(".") > 0){
            timeArr = tradeTime.split("\\.");
        }
        if(Strings.isEmpty(timeArr) || timeArr.length < 3){
            return tradeTime;
        }
        timeBuilder.append(timeArr[0].length() < 2 ? "0" + timeArr[0] : timeArr[0]);
        timeBuilder.append(timeArr[1].length() < 2 ? "0" + timeArr[1] : timeArr[1]);
        timeBuilder.append(timeArr[2].length() < 2 ? "0" + timeArr[2] : timeArr[2]);
        return timeBuilder.toString();
    }*/

    /**
     * 保存对私还款信息
     * @param map
     * @return
     */
    private int savePrivateAccountInfo(Map<String, String> map,User user) {
        // 交易日期
        Date tradeDate = Dates.parse((String) map.get("tradeDate"), DATE_PATTERN_1);
        // 交易日期转换失败
        if(Strings.isEmpty(tradeDate)){
            map.put(ExcelTemplet.FEED_BACK_MSG, "交易日期转换异常！");
            return 0;
        }
        // 交易时间
        String tradeTime = map.get("tradeTime");
        // 交易金额
        String  tradeAmount = map.get("tradeAmount");
        // 对方姓名
        String  secondName = map.get("secondName");
        // 校验对私还款信息是否存在
        boolean isExistAccountInfo=this.isExistAccountInfo(tradeDate,tradeTime,tradeAmount,secondName);
        if(isExistAccountInfo){
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入的数据已存在！");
            return  0;
        }
        BasePrivateAccountInfo accountInfo = new BasePrivateAccountInfo();
        Long id = sequencesService.getSequences(SequencesEnum.BASE_PRIVATE_ACCOUNT_INFO);
        accountInfo.setId(id);
        accountInfo.setTradeDate(tradeDate);
        accountInfo.setTradeTime(tradeTime);
        accountInfo.setTradeAmount(new BigDecimal(tradeAmount));
        accountInfo.setSecondAccount(map.get("secondAccount"));
        accountInfo.setSecondName(secondName);
        accountInfo.setTradeBank(map.get("tradeBank"));
        accountInfo.setTradeType(map.get("tradeType"));
        accountInfo.setTradeChannel(map.get("tradeChannel"));
        accountInfo.setTradePurpose(map.get("tradePurpose"));
//        accountInfo.setTradeDesc(map.get("tradeDesc"));
        accountInfo.setTradeRemark(map.get("tradeRemark"));
        // 操作人
        accountInfo.setOperatorId(user.getId());
        accountInfo.setStatus("未认领");
        String repayNo = "PRI"+Dates.getDateTime(Dates.DATAFORMAT_YYYYMMDDHHMMSS)+accountInfo.getId();
        accountInfo.setRepayNo(repayNo);
        try {
            // 新增对私还款信息
            basePrivateAccountInfoDao.insert(accountInfo);
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入数据成功！");
        } catch (Exception e) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入数据发生异常！");
            return 0;
        }
        return 1;
    }
    
    /**
     * 按条件查询对私还款信息是否存在
     * @param tradeDate
     * @param tradeTime
     * @param tradeAmount
     * @param secondName
     * @return
     */
    private boolean isExistAccountInfo(Date tradeDate, String tradeTime,String tradeAmount, String secondName) {
        BasePrivateAccountInfo accountInfo = new BasePrivateAccountInfo();
        accountInfo.setTradeDate(tradeDate);
        accountInfo.setTradeTime(tradeTime);
        accountInfo.setTradeAmount(BigDecimal.valueOf(Double.valueOf(tradeAmount)));
        accountInfo.setSecondName(secondName);
        List<BasePrivateAccountInfo> accountInfoList = this.findListByVo(accountInfo);
        if (CollectionUtils.isNotEmpty(accountInfoList)) {
            return true;
        }
        return false;
    }

    /**
     * 查询对私还款已认领结果
     */
    public List<Map<String, Object>> findPrivateAccountReceiveInfo(BasePrivateAccountInfo basePrivateAccountInfo) {
        return basePrivateAccountInfoDao.findPrivateAccountReceiveInfo(basePrivateAccountInfo);
    }

    /**
     * 查询对私还款信息
     */
    public List<BasePrivateAccountInfo> findQueryResultList(BasePrivateAccountInfo basePrivateAccountInfo) {
        return this.findListByVo(basePrivateAccountInfo);
    }

    /**
     * 查询被认领客户贷款信息（分页查询）
     */
    public Pager findWithPgByMap(Map<String, Object> params) {
        return basePrivateAccountInfoDao.findWithPgByMap(params);
    }

    /**
     * 更新对私还款信息
     */
    public int updatePrivateAccountInfo(BasePrivateAccountInfo basePrivateAccountInfo) {
        return basePrivateAccountInfoDao.update(basePrivateAccountInfo);
    }

    /**
     * 撤销认领
     */
    public int updateAccountInfoForCancel(BasePrivateAccountInfo basePrivateAccountInfo) {
        return basePrivateAccountInfoDao.updateAccountInfoForCancel(basePrivateAccountInfo);
    }

    /**
     * 修改状态为已导出
     */
    public int updateAccountInfoForExport(Map<String, Object> params) {
        return basePrivateAccountInfoDao.updateAccountInfoForExport(params);
    }
    
    /**
     * 对私还款领取状态校验，不能认领则返回提示信息
     */
    public String checkReceiveStatus() {
        String beginTime = null;
        String endTime = null;
        SysDictionary sysDictionary = new SysDictionary();
        sysDictionary.setCodeType("private_account_receive_time");
        sysDictionary.setCodeName("start_receive_time");
        // 查询开始领取时间
        List<SysDictionary> sysDictionaryList = sysDictionaryService.findDictionaryListByVo(sysDictionary);
        if (CollectionUtils.isEmpty(sysDictionaryList)){
            return "领取开始时间不存在或者没有配置";
        }
        beginTime = sysDictionaryList.get(0).getCodeValue();
        
        sysDictionary.setCodeName("end_receive_time");
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
            return "对不起，不在领取时间范围内，领取时间为" + beginTime + '至' + endTime;
        }
        return null;
    }

    /**
     * 获取对私还款领取时间范围信息
     */
    public Map<String, Object> findAccountReceiveTime() {
        Map<String,Object> result = new HashMap<String,Object>();
        // 查询开始领取时间
        SysDictionary sysDictionary = new SysDictionary();
        sysDictionary.setCodeType("private_account_receive_time");
        sysDictionary.setCodeName("start_receive_time");
        List<SysDictionary> sysDictionaryList = sysDictionaryService.findDictionaryListByVo(sysDictionary);
        Assert.notCollectionsEmpty(sysDictionaryList,"开始领取时间不存在或者没有配置");
        result.put("startDictionaryId", sysDictionaryList.get(0).getId());
        result.put("startTime", sysDictionaryList.get(0).getCodeValue());
        // 查询结束领取时间
        sysDictionary.setCodeName("end_receive_time");
        sysDictionaryList = sysDictionaryService.findDictionaryListByVo(sysDictionary);
        Assert.notCollectionsEmpty(sysDictionaryList,"结束领取时间不存在或者没有配置");
        result.put("endDictionaryId", sysDictionaryList.get(0).getId());
        result.put("endTime", sysDictionaryList.get(0).getCodeValue());
        return result;
    }

    /**
     * 更新对私还款领取时间
     */
    public void updateAccountReceiveTime(Map<String, Object> params) {
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
    public List<Map<String, Object>> findQueryResultMapList(BasePrivateAccountInfo basePrivateAccountInfo) {
        return basePrivateAccountInfoDao.findQueryResultMapList(basePrivateAccountInfo);
    }
}
