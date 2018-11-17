package com.zdmoney.credit.system.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.system.dao.pub.ISysSpecialDayDao;
import com.zdmoney.credit.system.domain.SysSpecialDay;
import com.zdmoney.credit.system.service.pub.ISysSpecialDayService;
@Service
public class SysSpecialDayServiceImpl implements ISysSpecialDayService{
	@Autowired
	private ISysSpecialDayDao sysSpecialDayDaoImpl;
	
	@Override
    public Date getBeforeWorkday(Date date,int beforeDays){
        @SuppressWarnings("unused")
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(date);//设置时间为传递过来的时间
        int workDays = 0;//统计n个工作日
        List<SysSpecialDay> sysSpecialDayList = sysSpecialDayDaoImpl.findAllList();
        while (workDays != beforeDays) {
            calendar.add(Calendar.DATE, -1);//时间推前一天
            //得到是否为特殊节假日
            boolean isSysSpecialDay = isSysSpecialDay(sysSpecialDayList,calendar);
            //如果不是周末,并且不是特殊节假日,那么工作日+1
            if (!isWeekend(calendar) && !isSysSpecialDay) {
                workDays++;
            }else{
                //如果是特殊节假日,并且为节假工作日,那么工作日++
                if(isSysSpecialDay && isSpecialWorkDay(sysSpecialDayList,calendar)){
                    workDays++;
                }
            }
        }
        return calendar.getTime();
    }
    
	@Override
    public boolean isSysSpecialDay(List<SysSpecialDay> sysSpecialDayList, Calendar date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(date == null){
            return false;
        }
        for (SysSpecialDay sysSpecialDay : sysSpecialDayList) {
            if(dateFormat.format(sysSpecialDay.getSpecailDay()).equals(dateFormat.format(date.getTime()))){
                return true;
//                break;
            }
        }
        return false;
    }
    
	@Override
    public Date getWorkday(Date date,int afterDays,List<SysSpecialDay> sysSpecialDayList){
        @SuppressWarnings("unused")
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(date);//设置时间为传递过来的时间
        int workDays = 0;//统计n个工作日

        while (workDays != afterDays) {
            calendar.add(Calendar.DATE, 1);//时间推后一天
            //得到是否为特殊节假日
            boolean isSysSpecialDay = isSysSpecialDay(sysSpecialDayList,calendar);
            //如果不是周末,并且不是特殊节假日,那么工作日+1
            if (!isWeekend(calendar) && !isSysSpecialDay) {
                workDays++;
            }else{
                //如果是特殊节假日,并且为节假工作日,那么工作日++
                if(isSysSpecialDay && isSpecialWorkDay(sysSpecialDayList,calendar)){
                    workDays++;
                }
            }
        }
        return calendar.getTime();
    }
    
	@Override
    public boolean  isWorkDay(Date date) {
        Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(date);//设置时间为传递过来的时间
        List<SysSpecialDay> sysSpecialDays = sysSpecialDayDaoImpl.findAllList();
        if(isWeekend(calendar)){
            if(isSpecialWorkDay(sysSpecialDays,calendar)){
                return true;
            }else{
                return false;
            }
        }else if(isSysSpecialDay(sysSpecialDays,calendar)){
            return false;
        }
        return true;
    }
    
    /**
     * 判断是否为周末
     *
     * @param date 需要判断的日期
     * @return true:周末,false:非周末
     */
    private boolean isWeekend(Calendar date) {
        if (date == null) {
            return false;
        }
        int day = date.get(Calendar.DAY_OF_WEEK);
        if (day == 1 || day == 7) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean  isSpecialWorkDay(List<SysSpecialDay> sysSpecialDayList, Calendar date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(date == null){
            return false;
        }
        for (SysSpecialDay sysSpecialDay : sysSpecialDayList) {
            //如果是特殊节假日,并且是工作日
            if(dateFormat.format(sysSpecialDay.getSpecailDay()).equals(dateFormat.format(date.getTime())) && sysSpecialDay.getWorkday().equals("t")){
                return true;
//                break;
            }
        }
        return false;
    }
    
    @Override
    public boolean  isRepayDayBeforeWorkDay(Date date) {
        Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(date);//设置时间为传递过来的时间
        @SuppressWarnings("unused")
		List<SysSpecialDay> SysSpecialDays = sysSpecialDayDaoImpl.findAllList();
        if(!isWorkDay(date)){
            return false;
        }
        for(int i=1;i<16;i++){
            calendar.add(Calendar.DATE, 1);
            if(calendar.get(Calendar.DAY_OF_MONTH) == 16 || calendar.get(Calendar.DAY_OF_MONTH) == 1){
                return true;
            } else if (isWorkDay(calendar.getTime())) {
            	return false;
            }
        }
        return false;
    }
    
    @Override
    public boolean  isRepayDayAfterWorkDay(Date date) {
        Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(date);//设置时间为传递过来的时间
        if(!isWorkDay(date)){
            return false;
        }
        for(int i=1;i<16;i++){
            calendar.add(Calendar.DATE, -1);
            if(calendar.get(Calendar.DAY_OF_MONTH) == 16 || calendar.get(Calendar.DAY_OF_MONTH) == 1){
                return true;
            } else if (isWorkDay(calendar.getTime())) {
            	return false;
            }
        }
        return false;
    }
    

    @Override
    public Date getWorkday(Date date,int afterDays){
        @SuppressWarnings("unused")
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(date);//设置时间为传递过来的时间
        int workDays = 0;//统计n个工作日
        //数据库里设置的特殊节假日
        List<SysSpecialDay> sysSpecialDayList = sysSpecialDayDaoImpl.findAllList();
        while (workDays != afterDays) {
            calendar.add(Calendar.DATE, 1);//时间推后一天
            //得到是否为特殊节假日
            boolean isSysSpecialDay = isSysSpecialDay(sysSpecialDayList,calendar);
            //如果不是周末,并且不是特殊节假日,那么工作日+1
            if (!isWeekend(calendar) && !isSysSpecialDay) {
                workDays++;
            }else{
                //如果是特殊节假日,并且为节假工作日,那么工作日++
                if(isSysSpecialDay && isSpecialWorkDay(sysSpecialDayList,calendar)){
                    workDays++;
                }
            }
        }
        return calendar.getTime();
    }
    
    @Override
    public Date getSpecifyOfBeforeWorkday(Date date,int beforeDays){
        @SuppressWarnings("unused")
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(date);//设置时间为传递过来的时间
        int workDays = 0;//统计n个工作日
        List<SysSpecialDay> sysSpecialDayList = sysSpecialDayDaoImpl.findAllList();
        while (workDays != beforeDays) {
            //得到是否为特殊节假日
            boolean isSysSpecialDay = isSysSpecialDay(sysSpecialDayList,calendar);
            //如果不是周末,并且不是特殊节假日,那么工作日+1
            if (!isWeekend(calendar) && !isSysSpecialDay) {
                workDays++;
            }else{
                //如果是特殊节假日,并且为节假工作日,那么工作日++
                if(isSysSpecialDay && isSpecialWorkDay(sysSpecialDayList,calendar)){
                    workDays++;
                }
            }
            calendar.add(Calendar.DATE, -1);//时间推前一天
        }
        return calendar.getTime();
    }
    
    @Override
    public int getWorkday(Date startDate,Date endDate) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        startDate = dateFormat.parse(dateFormat.format(startDate));
        endDate = dateFormat.parse(dateFormat.format(endDate));
        if(startDate.after(endDate)) return 0;
        Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(startDate);//设置时间为传递过来的时间
        int workDays = 0;//统计n个工作日
        List<SysSpecialDay> sysSpecialDayList = sysSpecialDayDaoImpl.findAllList(); 
        while (startDate.before(endDate)) {
            calendar.add(Calendar.DATE, 1);//时间推前一天
            startDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
            //得到是否为特殊节假日
            boolean isSysSpecialDay = isSysSpecialDay(sysSpecialDayList,calendar);
            //如果不是周末,并且不是特殊节假日,那么工作日+1
            if (!isWeekend(calendar) && !isSysSpecialDay) {
                workDays++;
            }else{
                //如果是特殊节假日,并且为节假工作日,那么工作日++
                if(isSysSpecialDay && isSpecialWorkDay(sysSpecialDayList,calendar)){
                    workDays++;
                }
            }
        }
        return workDays;
    }
    
    @Override
    public Date getBeforeWorkday(Date date,int beforeDays,List<SysSpecialDay> SysSpecialDayList){
        @SuppressWarnings("unused")
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(date);//设置时间为传递过来的时间
        int workDays = 0;//统计n个工作日
        while (workDays != beforeDays) {
            calendar.add(Calendar.DATE, -1);//时间推前一天
            //得到是否为特殊节假日
            boolean isSysSpecialDay = isSysSpecialDay(SysSpecialDayList,calendar);
            //如果不是周末,并且不是特殊节假日,那么工作日+1
            if (!isWeekend(calendar) && !isSysSpecialDay) {
                workDays++;
            }else{
                //如果是特殊节假日,并且为节假工作日,那么工作日++
                if(isSysSpecialDay && isSpecialWorkDay(SysSpecialDayList,calendar)){
                    workDays++;
                }
            }
        }
        return calendar.getTime();
    }
}
