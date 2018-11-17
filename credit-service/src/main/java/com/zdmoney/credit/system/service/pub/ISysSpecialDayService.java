package com.zdmoney.credit.system.service.pub;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.zdmoney.credit.system.domain.SysSpecialDay;

public interface ISysSpecialDayService {
    /**
     * 获取指定日期前N个工作日的日期
     * @param date 指定的日期
     * @param beforeDays 之前多少天,这里传的是正整数
     * @return  日期前N个工作日
     */
    public Date getBeforeWorkday(Date date,int beforeDays);
    /**
     * 判断是否为特殊节假日
     *
     * @param sysSpecialDayList 节假日基础数据集合
     * @param date 需要判断的日期
     * @return true:特殊节假日,false:非特殊节假日
     */
    public boolean isSysSpecialDay(List<SysSpecialDay> sysSpecialDayList, Calendar date);
    /** 获取指定日期后n个工作日的日期
     * 
     * @param date
     * @param afterDays
     * @param sysSpecialDayList
     * @return
     */
    public Date getWorkday(Date date,int afterDays,List<SysSpecialDay> sysSpecialDayList);
    /**
     *  判断是否为工作日
     * @param SysSpecialDayList  节假日基础数据集合
     * @param date 当前时间
     * @return
     */
    public boolean  isWorkDay(Date date);
    /**
     *  判断是否为特殊工作日
     * @param sysSpecialDayList  节假日基础数据集合
     * @param date 当前时间
     * @return
     */
    public boolean  isSpecialWorkDay(List<SysSpecialDay> sysSpecialDayList, Calendar date);
    /**
     *  判断是否为支付日（节假日，周末）前一个工作日
     * @param date 当前时间
     * @return
     */
    public boolean  isRepayDayBeforeWorkDay(Date date) ;
    /**
     *  判断是否为支付日（节假日，周末）下一个工作日
     * @param date 当前时间
     * @return
     */
    public boolean  isRepayDayAfterWorkDay(Date date);
    /** 获取指定日期后n个工作日的日期
     * 
     * @param date
     * @param afterDays
     * @return
     */
    public Date getWorkday(Date date,int afterDays);
    /**
     * 获取指定日期前N个工作日的日期(包含指定日期)
     * @param date 指定的日期
     * @param beforeDays 之前多少天,这里传的是正整数
     * @return  日期前N个工作日
     */
    public Date getSpecifyOfBeforeWorkday(Date date,int beforeDays);
    /**
     * 两个日期之间的工作日数
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException 
     */
    public int getWorkday(Date startDate,Date endDate) throws ParseException;
    /**
     * 获取指定日期前N个工作日的日期
     * @param date 指定的日期
     * @param beforeDays 之前多少天,这里传的是正整数
     * @param SysSpecialDayList 数据库里设置的特殊节假日
     * @return  日期前N个工作日
     */
    public Date getBeforeWorkday(Date date,int beforeDays,List<SysSpecialDay> SysSpecialDayList);
}
