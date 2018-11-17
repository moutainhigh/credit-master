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

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
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
import com.zdmoney.credit.operation.dao.pub.IBasePublicAccountInfoWmDao;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfoWm;
import com.zdmoney.credit.operation.service.pub.IBasePublicAccountInfoWmService;
import com.zdmoney.credit.system.domain.SysDictionary;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;

@Service
public class BasePublicAccountInfoWmServiceImpl implements IBasePublicAccountInfoWmService {

    @Autowired
    IBasePublicAccountInfoWmDao basePublicAccountInfoWmDao;

    @Autowired
    private ISequencesService sequencesService;

    @Autowired
    private ISysDictionaryService sysDictionaryService;
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
    /**
     * 日期格式4
     */
    private final static String DATE_PATTERN_4 = "yyyy-MM-dd";

    public Pager findWithPg(BasePublicAccountInfoWm basePublicAccountInfo) {
        return basePublicAccountInfoWmDao.findWithPg(basePublicAccountInfo);
    }

    public List<BasePublicAccountInfoWm> findListByVo(BasePublicAccountInfoWm basePublicAccountInfo) {
        return basePublicAccountInfoWmDao.findListByVo(basePublicAccountInfo);
    }

    /**
     * 保存对私还款信息
     * 
     * @param map
     * @return
     */
    private int savePrivateAccountInfo(Map<String, String> map, User user) {
        // 交易日期
        Date tradeDate = Dates.parse((String) map.get("tradeDate"), DATE_PATTERN_1);
        // 交易日期转换失败
        if (Strings.isEmpty(tradeDate)) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "交易日期转换异常！");
            return 0;
        }
        // 交易时间
        String tradeTime = map.get("tradeTime");
        // 交易金额
        String tradeAmount = map.get("tradeAmount");
        // 对方姓名
        String secondName = map.get("secondName");
        // 校验对私还款信息是否存在
        boolean isExistAccountInfo = this.isExistAccountInfo(tradeDate, tradeTime, tradeAmount, secondName);
        if (isExistAccountInfo) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入的数据已存在！");
            return 0;
        }
        BasePublicAccountInfoWm accountInfo = new BasePublicAccountInfoWm();
        Long id = sequencesService.getSequences(SequencesEnum.BASE_PUBLIC_ACCOUNT_INFO_WM);
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
        accountInfo.setTradeDesc(map.get("tradeDesc"));
        accountInfo.setTradeRemark(map.get("tradeRemark"));
        accountInfo.setSerialNumber(map.get("serialNumber"));
        // 操作人
        accountInfo.setOperatorId(user.getId());
        accountInfo.setStatus("未认领");
        accountInfo.setLoanBelong(map.get("loanBelong"));
        accountInfo.setRepayNo(this.createRepayNo(accountInfo.getId()));
        try {
            // 新增对私还款信息
            basePublicAccountInfoWmDao.insert(accountInfo);
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入数据成功！");
        } catch (Exception e) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入数据发生异常！");
            return 0;
        }
        return 1;
    }

    /**
     * 保存对公还款流水信息
     */
    public void savePrivateAccountInfo(List<Map<String, String>> sheetDataList, String loanBelong) {
        // 获取当前登录用户的信息
        User user = UserContext.getUser();
        for (Map<String, String> map : sheetDataList) {
            // 交易日期
            String tradeDate = map.get("tradeDate");
            // 交易日期
            String tradeTime = map.get("tradeTime");
            // 交易金额
            String tradeAmount = map.get("tradeAmount");
            // 起息日
            String breathDate = map.get("breathDate");
            try {
                Assert.notNullAndEmpty(tradeDate, "交易日期");
                Assert.notNullAndEmpty(tradeTime, "交易时间");
                Assert.notNullAndEmpty(tradeAmount, "交易金额");
                if (map.size() > 10) {
                    Assert.notDate(tradeDate, DATE_PATTERN_1,"交易日期格式错误，应类似20140701");
                    Assert.notDate(breathDate, DATE_PATTERN_1, "起息日期格式错误，应类似20140701");
                    // 交易时间格式转换，主要为了将银行类似于20.30.30的时间转换为203030格式
                    Date tempDate = Assert.notDate(tradeTime, DATE_PATTERN_2, "交易时间格式错误，应类似203030格式");
                    map.put("tradeTime", Dates.getDateTime(tempDate, DATE_PATTERN_3));
                } else {
                    Date tempDate = Assert.notDate(tradeDate, DATE_PATTERN_4, "交易日期格式错误，若是文本应类似2014-07-01或使用短日期格式");
                    Assert.notDate(tradeTime, DATE_PATTERN_3, "交易时间格式错误，应类似20:30:30");
                    map.put("tradeDate", Dates.getDateTime(tempDate, DATE_PATTERN_1));
                }
                try {
                    new BigDecimal(tradeAmount);
                } catch (Exception e) {
                    throw new PlatformException(ResponseEnum.FULL_MSG, "交易金额类型不正确");
                }
                // 交易金额必须大于零的校验
                if (new BigDecimal(tradeAmount).doubleValue() <= 0) {
                    throw new PlatformException(ResponseEnum.FULL_MSG, "交易金额必须大于零");
                }
                map.put("loanBelong", loanBelong);
                // 保存导入数据
                this.savePrivateAccountInfo(map, user);
            } catch (PlatformException e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, e.getMessage());
            } catch (Exception e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, "发生不可预知的异常");
            }
        }
    }

    /**
     * 按条件查询对私还款信息是否存在
     * 
     * @param tradeDate
     * @param tradeTime
     * @param tradeAmount
     * @param secondName
     * @return
     */
    private boolean isExistAccountInfo(Date tradeDate, String tradeTime, String tradeAmount, String secondName) {
        BasePublicAccountInfoWm accountInfo = new BasePublicAccountInfoWm();
        accountInfo.setTradeDate(tradeDate);
        accountInfo.setTradeTime(tradeTime);
        accountInfo.setTradeAmount(BigDecimal.valueOf(Double.valueOf(tradeAmount)));
        accountInfo.setSecondName(secondName);
        List<BasePublicAccountInfoWm> accountInfoList = this.findListByVo(accountInfo);
        if (CollectionUtils.isNotEmpty(accountInfoList)) {
            return true;
        }
        return false;
    }

    /**
     * 查询对私还款已认领结果
     */
    public List<Map<String, Object>> findPrivateAccountReceiveInfo(BasePublicAccountInfoWm basePrivateAccountInfo) {
        return basePublicAccountInfoWmDao.findPrivateAccountReceiveInfo(basePrivateAccountInfo);
    }

    /**
     * 修改状态为已导出
     */
    public int updateAccountInfoForExport(Map<String, Object> params) {
        return basePublicAccountInfoWmDao.updateAccountInfoForExport(params);
    }

    /**
     * 查询对私还款信息
     */
    public List<BasePublicAccountInfoWm> findQueryResultList(BasePublicAccountInfoWm basePrivateAccountInfo) {
        return this.findListByVo(basePrivateAccountInfo);
    }

    /**
     * 对私还款领取状态校验，不能认领则返回提示信息
     */
    public String checkReceiveStatus() {
        String beginTime = null;
        String endTime = null;
        SysDictionary sysDictionary = new SysDictionary();
        sysDictionary.setCodeType("public_account_wm_receive_time");
        sysDictionary.setCodeName("start_receive_time");
        // 查询开始领取时间
        List<SysDictionary> sysDictionaryList = sysDictionaryService.findDictionaryListByVo(sysDictionary);
        if (CollectionUtils.isEmpty(sysDictionaryList)) {
            return "领取开始时间不存在或者没有配置";
        }
        beginTime = sysDictionaryList.get(0).getCodeValue();

        sysDictionary.setCodeName("end_receive_time");
        // 查询结束领取时间
        sysDictionaryList = sysDictionaryService.findDictionaryListByVo(sysDictionary);
        if (CollectionUtils.isEmpty(sysDictionaryList)) {
            return "领取结束时间不存在或者没有配置";
        }
        endTime = sysDictionaryList.get(0).getCodeValue();

        // 时刻HH:mm正则表达式
        String regex = "(0\\d{1}|1\\d{1}|2[0-3]):([0-5]\\d{1})";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(beginTime);
        if (!match.matches()) {
            return "领取开始时间格式不正确，正确格式应类似09:05";
        }
        match = pattern.matcher(endTime);
        if (!match.matches()) {
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
     * 按Id查询对公还款信息
     */
    public BasePublicAccountInfoWm get(Long id) {
        return basePublicAccountInfoWmDao.get(id);
    }

    /**
     * 撤销认领
     */
    public int updateAccountInfoForCancel(BasePublicAccountInfoWm basePrivateAccountInfo) {
        return basePublicAccountInfoWmDao.updateAccountInfoForCancel(basePrivateAccountInfo);
    }

    /**
     * 更新对私还款信息
     */
    public int updatePrivateAccountInfo(BasePublicAccountInfoWm basePrivateAccountInfo) {
        return basePublicAccountInfoWmDao.update(basePrivateAccountInfo);
    }

    /**
     * 获取对私还款领取时间范围信息
     */
    public Map<String, Object> findAccountReceiveTime() {
        Map<String, Object> result = new HashMap<String, Object>();
        // 查询开始领取时间
        SysDictionary sysDictionary = new SysDictionary();
        sysDictionary.setCodeType("public_account_wm_receive_time");
        sysDictionary.setCodeName("start_receive_time");
        List<SysDictionary> sysDictionaryList = sysDictionaryService.findDictionaryListByVo(sysDictionary);
        Assert.notCollectionsEmpty(sysDictionaryList, "开始领取时间不存在或者没有配置");
        result.put("startDictionaryId", sysDictionaryList.get(0).getId());
        result.put("startTime", sysDictionaryList.get(0).getCodeValue());
        // 查询结束领取时间
        sysDictionary.setCodeName("end_receive_time");
        sysDictionaryList = sysDictionaryService.findDictionaryListByVo(sysDictionary);
        Assert.notCollectionsEmpty(sysDictionaryList, "结束领取时间不存在或者没有配置");
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

    /**
     * 查询被认领客户贷款信息（分页查询）
     */
    public Pager findWithPgByMap(Map<String, Object> params) {
        return basePublicAccountInfoWmDao.findWithPgByMap(params);
    }

    @Override
    public void savePrivateAccountHRBHInfo(List<Map<String, String>> sheetDataList) {
        // 获取当前登录用户的信息
        User user = UserContext.getUser();
        for (Map<String, String> map : sheetDataList) {
            // 交易日期
            String tradeDate = map.get("tradeDate");
            // 交易日期
            String tradeTime = Dates.getDateTime(new Date(), DATE_PATTERN_3);
            // 交易金额
            String tradeAmount = map.get("tradeAmount");
            // 交易余额
            String accountBalance = map.get("accountBalance");

            try {
                Assert.notNullAndEmpty(tradeDate, "交易日期");
                Assert.notNullAndEmpty(tradeAmount, "交易金额");
                Date tempDate = Assert.notDate(tradeDate, DATE_PATTERN_1, "交易日期格式错误，应类似20140701");
                map.put("tradeDate", Dates.getDateTime(tempDate, DATE_PATTERN_1));
                map.put("tradeTime", tradeTime);
                try {
                    new BigDecimal(tradeAmount);
                } catch (Exception e) {
                    throw new PlatformException(ResponseEnum.FULL_MSG, "交易金额类型不正确");
                }
                try {
                    new BigDecimal(accountBalance);
                } catch (Exception e) {
                    throw new PlatformException(ResponseEnum.FULL_MSG, "交易余额类型不正确");
                }
                // 交易金额必须大于零的校验
                if (new BigDecimal(tradeAmount).doubleValue() <= 0) {
                    throw new PlatformException(ResponseEnum.FULL_MSG, "交易金额必须大于零");
                }
                // 保存导入数据
                this.savePrivateAccountHRBHInfo(map, user);
            } catch (PlatformException e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, e.getMessage());
            } catch (Exception e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, "发生不可预知的异常");
            }
        }
    }

    /**
     * 保存华瑞渤海对私还款信息
     * @param map
     * @return
     */
    private int savePrivateAccountHRBHInfo(Map<String, String> map, User user) {
        // 交易日期
        Date tradeDate = Dates.parse((String) map.get("tradeDate"), DATE_PATTERN_1);
        // 交易日期转换失败
        if (Strings.isEmpty(tradeDate)) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "交易日期转换异常！");
            return 0;
        }
        // 交易时间
        String tradeTime = map.get("tradeTime");
        // 交易金额
        String tradeAmount = map.get("tradeAmount");
        // 对方姓名
        String accountBalance = map.get("accountBalance");
        // 校验对私还款信息是否存在
        boolean isExistAccountInfo = this.isExistAccountHRBHInfo(tradeDate, tradeAmount, accountBalance);
        if (isExistAccountInfo) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入的数据已存在！");
            return 0;
        }
        BasePublicAccountInfoWm accountInfo = new BasePublicAccountInfoWm();
        Long id = sequencesService.getSequences(SequencesEnum.BASE_PUBLIC_ACCOUNT_INFO_WM);
        accountInfo.setId(id);
        accountInfo.setTradeDate(tradeDate);
        accountInfo.setTradeTime(tradeTime);
        accountInfo.setTradeAmount(new BigDecimal(tradeAmount));
        accountInfo.setSecondAccount(map.get("secondAccount"));
        accountInfo.setSecondName(map.get("secondName"));
        accountInfo.setTradeBank(map.get("tradeBank"));
        accountInfo.setTradeType(map.get("tradeType"));
        accountInfo.setTradeChannel(map.get("tradeChannel"));
        accountInfo.setTradePurpose(map.get("tradePurpose"));
        accountInfo.setTradeDesc(map.get("tradeDesc"));
        accountInfo.setTradeRemark(map.get("tradeRemark"));
        accountInfo.setSerialNumber(map.get("serialNumber"));
        accountInfo.setMemo(accountBalance);
        // 操作人
        accountInfo.setOperatorId(user.getId());
        accountInfo.setStatus("未认领");
        // 记录内部流水号
        accountInfo.setRepayNo(this.createRepayNo(accountInfo.getId()));
        // 线下还款待认领债权去向
        accountInfo.setLoanBelong(FundsSourcesTypeEnum.华瑞渤海.getValue());
        try {
            // 新增对私还款信息
            basePublicAccountInfoWmDao.insert(accountInfo);
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入数据成功！");
        } catch (Exception e) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入数据发生异常！");
            return 0;
        }
        return 1;
    }

    /**
     * 按条件查询华瑞渤海对私还款信息是否存在
     * @param tradeDate
     * @param tradeAmount
     * @param accountBalance
     * @return
     */
    private boolean isExistAccountHRBHInfo(Date tradeDate, String tradeAmount, String accountBalance) {
        BasePublicAccountInfoWm accountInfo = new BasePublicAccountInfoWm();
        accountInfo.setTradeDate(tradeDate);
        accountInfo.setTradeAmount(BigDecimal.valueOf(Double.valueOf(tradeAmount)));
        accountInfo.setMemo(accountBalance);
        List<BasePublicAccountInfoWm> accountInfoList = this.findListByVo(accountInfo);
        if (CollectionUtils.isNotEmpty(accountInfoList)) {
            return true;
        }
        return false;
    }

    /**
     * 校验和保存线下还款银行流水信息
     * @param sheetDataList
     * @param loanBelong
     */
    public void saveLufaxAccountInfo(List<Map<String, String>> sheetDataList, String loanBelong) {
        // 获取当前登录用户的信息
        User user = UserContext.getUser();
        for (Map<String, String> map : sheetDataList) {
            // 交易日期
            String tradeDate = map.get("tradeDate");
            // 交易金额
            String tradeAmount = map.get("tradeAmount");
            // 交易流水号
            String serialNumber = map.get("serialNumber");
            try {
                Assert.notNullAndEmpty(tradeDate, "交易日期");
                Assert.notNullAndEmpty(tradeAmount, "交易金额");
                Assert.notNullAndEmpty(serialNumber, "交易流水号");
                Assert.notDate(tradeDate, DATE_PATTERN_1, "交易日期格式错误，应为" + DATE_PATTERN_1 + "格式！");
                try {
                    new BigDecimal(tradeAmount);
                } catch (Exception e) {
                    throw new PlatformException(ResponseEnum.FULL_MSG, "交易金额类型不正确！");
                }
                // 交易金额必须大于零的校验
                if (new BigDecimal(tradeAmount).compareTo(new BigDecimal(0)) <= 0) {
                    throw new PlatformException(ResponseEnum.FULL_MSG, "交易金额必须大于零！");
                }
                map.put("loanBelong", loanBelong);
                // 保存银行还款流水信息
                this.saveLufaxAccountInfo(map, user);
            } catch (PlatformException e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, e.getMessage());
            } catch (Exception e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, "发生不可预知的异常");
            }
        }
    }

    /**
     * 保存银行还款流水信息
     * @param map
     * @param user
     * @return
     */
    private int saveLufaxAccountInfo(Map<String, String> map, User user) {
        // 交易日期
        Date tradeDate = Dates.parse((String) map.get("tradeDate"), DATE_PATTERN_1);
        // 交易金额
        String tradeAmount = map.get("tradeAmount");
        // 交易流水号
        String serialNumber = map.get("serialNumber");
        // 判断是否存在重复导入的银行还款流水记录
        boolean isExistAccountInfo = this.isExistLufaxAccountInfo(tradeDate, tradeAmount, serialNumber);
        if (isExistAccountInfo) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入的数据已存在！");
            return 0;
        }
        BasePublicAccountInfoWm accountInfo = new BasePublicAccountInfoWm();
        Long id = sequencesService.getSequences(SequencesEnum.BASE_PUBLIC_ACCOUNT_INFO_WM);
        accountInfo.setId(id);
        accountInfo.setTradeDate(tradeDate);
        accountInfo.setTradeTime(Dates.getDateTime(new Date(), "HH:mm:ss"));
        accountInfo.setTradeAmount(new BigDecimal(tradeAmount));
        accountInfo.setSecondAccount(map.get("secondAccount"));
        accountInfo.setSecondName(map.get("secondName"));
        accountInfo.setSerialNumber(serialNumber);
        // 摘要记录到交易方式字段中
        accountInfo.setTradeType(map.get("tradeRemark"));
        // 用途记录到交易摘要字段中
        accountInfo.setTradeRemark(map.get("tradePurpose"));
        accountInfo.setTradePurpose(map.get("tradePurpose"));
        // 操作人
        accountInfo.setOperatorId(user.getId());
        accountInfo.setStatus("未认领");
        accountInfo.setLoanBelong(map.get("loanBelong"));
        accountInfo.setRepayNo(this.createRepayNo(accountInfo.getId()));
        try {
            // 新增对私还款信息
            basePublicAccountInfoWmDao.insert(accountInfo);
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入数据成功！");
        } catch (Exception e) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "导入数据发生异常！");
            return 0;
        }
        return 1;
    }

    /**
     * 判断是否存在重复导入的银行还款流水记录 以交易时间、交易金额、交易流水号标识唯一一笔银行流水记录
     * 
     * @param tradeDate
     * @param tradeAmount
     * @param serialNumber
     * @return
     */
    private boolean isExistLufaxAccountInfo(Date tradeDate, String tradeAmount, String serialNumber) {
        BasePublicAccountInfoWm accountInfo = new BasePublicAccountInfoWm();
        accountInfo.setTradeDate(tradeDate);
        accountInfo.setTradeAmount(BigDecimal.valueOf(Double.valueOf(tradeAmount)));
        accountInfo.setSerialNumber(serialNumber);
        List<BasePublicAccountInfoWm> accountInfoList = this.findListByVo(accountInfo);
        if (CollectionUtils.isNotEmpty(accountInfoList)) {
            return true;
        }
        return false;
    }

    /**
     * 生成内部流水号
     * 
     * @param id
     * @return
     */
    private String createRepayNo(Long id) {
        return "PWM" + Dates.getDateTime(Dates.DATAFORMAT_YYYYMMDDHHMMSS) + id;
    }

	@Override
	public List<Map<String, Object>> findQueryResultMapList(BasePublicAccountInfoWm basePrivateAccountInfo) {
		return basePublicAccountInfoWmDao.findQueryResultMapList(basePrivateAccountInfo);
	}
}
