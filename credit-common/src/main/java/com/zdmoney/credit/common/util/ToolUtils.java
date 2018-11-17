package com.zdmoney.credit.common.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

public class ToolUtils
{

	
    /*
         * 将小写的人民币转化成大写
         */
        public static String NumberToChinese(double number) {
                StringBuffer chineseNumber = new StringBuffer();
                String[] num = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
                String[] unit = { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟",
                                "亿", "拾", "佰", "仟", "万" };
                String tempNumber = String.valueOf(Math.round((number * 100)));
                int tempNumberLength = tempNumber.length();
                if ("0".equals(tempNumber)) {
                        return "零元整";
                }
                if (tempNumberLength > 15) {
                        try {
                                throw new Exception("超出转化范围.");
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }
                boolean preReadZero = true; // 前面的字符是否读零
                for (int i = tempNumberLength; i > 0; i--) {
                        if ((tempNumberLength - i + 2) % 4 == 0) {
                                // 如果在（圆,万,亿,万）位上的四个数都为零,如果标志为未读零,则只读零,如果标志为已读零,则略过这四位
                                if (i - 4 >= 0 && "0000".equals(tempNumber.substring(i - 4, i))) {
                                        if (!preReadZero) {
                                                chineseNumber.insert(0, "零");
                                                preReadZero = true;
                                        }
                                        i -= 3; // 下面还有一个i--
                                        continue;
                                }
                                // 如果当前位在（圆,万,亿,万）位上,则设置标志为已读零（即重置读零标志）
                                preReadZero = true;
                        }
                        Integer digit = Integer.parseInt(tempNumber.substring(i - 1, i));
                        if (digit == 0) {
                                // 如果当前位是零并且标志为未读零,则读零,并设置标志为已读零
                                if (!preReadZero) {
                                        chineseNumber.insert(0, "零");
                                        preReadZero = true;
                                }
                                // 如果当前位是零并且在（圆,万,亿,万）位上,则读出（圆,万,亿,万）
                                if ((tempNumberLength - i + 2) % 4 == 0) {
                                        chineseNumber.insert(0, unit[tempNumberLength - i]);
                                }
                        }
                        // 如果当前位不为零,则读出此位,并且设置标志为未读零
                        else {
                                chineseNumber
                                                .insert(0, num[digit] + unit[tempNumberLength - i]);
                                preReadZero = false;
                        }
                }
                // 如果分角两位上的值都为零,则添加一个"整"字
                if (tempNumberLength - 2 >= 0
                                && "00".equals(tempNumber.substring(tempNumberLength - 2,
                                                tempNumberLength))) {
                        chineseNumber.append("整");
                }
                return chineseNumber.toString();
        }


    /**
     * 实际利率法,同excel的Rate函数相同，参数顺序不同
     * @author Bean(mailto:mailxbs@126.com)
     * @param a 现值
     * @param b 年金
     * @param c 期数
     * @param cnt 运算次数
     * @param ina 误差位数
     * @return 利率
     */
    public static double rate(double a,double b,double c){
    	int cnt=200;int ina=10;
        double rate = 1,x,jd = 0.1,side = 0.1,i = 1;
        x = a/b - (Math.pow(1+rate, c)-1)/(Math.pow(rate+1, c)*rate);
        while(i++<cnt&&Math.abs(x)>=1/Math.pow(10, ina)){
            if(x*side>0){side = -side;jd *=10;}
            rate += side/jd;
            x = a/b - (Math.pow(1+rate, c)-1)/(Math.pow(rate+1, c)*rate);
        }
        if(i>cnt)return Double.NaN;
        return rate;
    }


    public static String getMoneyNumAt(double money,int i){
        String m = String.format("%.2f",money);
        String p = m.substring(0,m.indexOf("."));
        String e = m.substring(m.indexOf(".")+1,m.indexOf(".")+3);
        if(i>0){
           if(i>p.length()+1) return "";
           if(i==p.length()+1) return "￥";
           if(i<p.length()+1) return p.substring(p.length()-i,p.length()-i+1);
        }
        else{
            if(i==-1) return e.substring(0,1);
            if(i==-2) return e.substring(1,2);
        }
		return null;
    }

    public static void copyFile(String pathSrc,String pathDest) throws IOException
    {
        FileInputStream fi=new FileInputStream(pathSrc);
        FileOutputStream fo=new FileOutputStream(pathDest);
        byte[] data=new byte[fi.available()];
        fi.read(data);
        fo.write(data);
        fi.close();
        fo.close();
    }

    public static void createSmallImage(String from,String drawTo){

        try{
            String pathS=from;//源文件，必须是有文件
            String pathD=drawTo;//目标文件
            copyFile(pathS,pathD);

            File stadimgfile2 = new File(pathD);//生成缩小mimi图
            BufferedImage img2 = ImageIO.read(stadimgfile2);//图片缓存
            //得到图片的宽和高
            double width = img2.getWidth();
            double height = img2.getHeight();
            int miniwidth = 60;//120缩略图宽度
            int miniheight = 45;//90缩略图高度
            double ratew = miniwidth / width;
            double rateh = miniheight / height;
            //获得适合的缩放比率，即以在规定缩略尺寸中完整显示图片内容的同时又保证最大的缩放比率
            double rate = Math.min(ratew, rateh);
            rate = (Math.rint((rate * 100) + 0.5)) / 100;
            BufferedImage imgmini = new java.awt.image.BufferedImage(miniwidth, miniheight,BufferedImage.TYPE_USHORT_565_RGB);
            Graphics2D gmini = imgmini.createGraphics();
            gmini.setBackground(Color.WHITE);
            gmini.clearRect(0, 0, miniwidth, miniheight);
            AffineTransform trans = new AffineTransform();
            trans.scale(rate, rate);
            AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_BILINEAR);
            gmini.drawImage(img2, op, (int) (miniwidth - (width * rate)) / 2, (int) (miniheight - (height * rate)) / 2);
            ImageIO.write(imgmini, "jpg", stadimgfile2); 
        }
        catch (Exception e){
        	System.out.println(e.getMessage());
        }
        
        
    }
    //---------------------------------------------------------
    //mimi图加水印
    public static void addLogo(String imgFile,String logoFile){
        try{
            File stadimgfile2 = new File(imgFile);
            BufferedImage img3 = ImageIO.read(stadimgfile2);
            int mimi_width2 = img3.getWidth();
            int mimi_height2 = img3.getHeight();
            BufferedImage imgmimi2 = new java.awt.image.BufferedImage(mimi_width2, mimi_height2,BufferedImage.TYPE_USHORT_565_RGB);
            //logo文件的位置，必须是真是的
            BufferedImage watermark3 = ImageIO.read(new File(logoFile));
            Graphics2D gmimi2 = imgmimi2.createGraphics();
            gmimi2.drawImage(img3, null, 0, 0);
            gmimi2.drawImage(watermark3, null, mimi_width2 - watermark3.getWidth(), mimi_height2 - watermark3.getHeight());
            ImageIO.write(imgmimi2, "jpg", stadimgfile2);
        }
        catch (Exception e){
        	System.out.println(e.getMessage());
        }

    }


   /*public static void main(String[] args)
    {
        String fsource = "d:/temp/2011-10-05+22.26.45.jpg"
        String fdest = "d:/temp/dest.jpg"
        Date d = new Date()
        d.format()
    }*/

    /**
     * 判断是否为周末
     *
     * @param date 需要判断的日期
     * @return true:周末,false:非周末
     */
    public static boolean isWeekend(Calendar date) {
        if (date == null) {
            return false;
        }
        int day = date.get(Calendar.DAY_OF_WEEK);
        if (day == 1 || day == 7) {
            return true;
        }
        return false;
    }

    /**
     *  判断是否为工作日
     * @param SysSpecialDayList  节假日基础数据集合
     * @param date 当前时间
     * @return
     */
    public static boolean  isRepayDay(Date date) {
        Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(date);//设置时间为传递过来的时间
//        List<SysSpecialDay> sysSpecialDays = sysSpecialDayDaoImpl.findAllList();
        if(calendar.get(Calendar.DAY_OF_MONTH)==16 || calendar.get(Calendar.DAY_OF_MONTH)==1){
            return true;
        }
        return false;
    }

    /**
     * 获取指定时间的上两个月的日期
     * @author  zhanghao
     * @param  date 指定的日期
     * @return  指定日期的上两个月日期
     */
    public static String getLastMonthDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,-15);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 验证是否为科学计数法
     * @param scientificNotation
     * @return
     */
    public static boolean isScientificNotation(String scientificNotation){
//        return scientificNotation.matches("^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))\$");
        return false;
    }

    /**
     * 验证是否为小数
     * @param val
     * @return
     */
    public static boolean  isDouble(String val){
        if(isScientificNotation(val)){
            return true;
        }
        return val.matches("(\\d+)(.(\\d+))?");
    }

    public static boolean isJobServer(){
		return false;
//        CodeHelper codeHelper = CodeHelper.findAll()[0];
//        return   NetworkInterface.getNetworkInterfaces().any{it.getInetAddresses().any{it.getHostAddress().equals(codeHelper.runJobServer)}}
    }

    public static boolean isSMSJobServer(){
		return false;
//        CodeHelper codeHelper = CodeHelper.findAll()[0];
//        return   NetworkInterface.getNetworkInterfaces().any{it.getInetAddresses().any{it.getHostAddress().equals(codeHelper.runSMSJobServer)}}
    }

    public static boolean isTransactionServer(){
		return false;
//        CodeHelper codeHelper = CodeHelper.findAll()[0];
//        return   NetworkInterface.getNetworkInterfaces().any{it.getInetAddresses().any{it.getHostAddress().equals(codeHelper.transactionServer)}}
    }
/*
    public static void main(String[] args) throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<SysSpecialDay> SysSpecialDayList = new ArrayList<SysSpecialDay>();

        //元旦
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2011-12-31"),true));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-1"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-2"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-3"),false));

        //春节
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-21"),true));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-22"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-23"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-24"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-25"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-26"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-27"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-28"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-1-29"),true));

        //清明节3-31,4-1上班,4-2，4-3，4-4放假
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-3-31"),true));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-4-1"),true));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-4-2"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-4-3"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-4-4"),false));

        //劳动节
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-4-28"),true));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-4-29"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-4-30"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-5-1"),false));

        //端午节
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-6-22"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-6-23"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-6-24"),false));

        //中秋国庆
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-9-29"),true));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-9-30"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-10-1"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-10-2"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-10-3"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-10-4"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-10-5"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-10-6"),false));
        SysSpecialDayList.add(new SysSpecialDay(dateFormat.parse("2012-10-7"),false));


        //Date date = getWorkday(new Date(),5,SysSpecialDayList)
        Date date = getBeforeWorkday(new Date(),12,SysSpecialDayList);
        System.out.println(dateFormat.format(date));
    }
*/
    public static Date getNextRepayDate(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        if (cal.get(Calendar.DAY_OF_MONTH) >= 16){
            cal.add(Calendar.MONTH,1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }else {
            cal.set(Calendar.DAY_OF_MONTH, 16);
        }
        return cal.getTime();
    }

    public static Date getNextRepayDateIncludeToday(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        if (cal.get(Calendar.DAY_OF_MONTH) > 16){
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }else if (cal.get(Calendar.DAY_OF_MONTH) > 1){
            cal.set(Calendar.DAY_OF_MONTH, 16);
        } else {
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }
        return cal.getTime();
    }

    public static Date getLastRepayDate(Date d) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        if (cal.get(Calendar.DAY_OF_MONTH) > 16){
            cal.set(Calendar.DAY_OF_MONTH, 16);
        }else if (cal.get(Calendar.DAY_OF_MONTH) > 1){
            cal.set(Calendar.DAY_OF_MONTH, 1);
        } else {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            cal.set(Calendar.DAY_OF_MONTH, 16);
        }
        return cal.getTime();
    }

    public static Date getLastRepayDateIncludeToday(Date d) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        if (cal.get(Calendar.DAY_OF_MONTH) >= 16){
            cal.set(Calendar.DAY_OF_MONTH, 16);
        }else if (cal.get(Calendar.DAY_OF_MONTH) >= 1){
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }
        return cal.getTime();
    }

    public static Date getLastRepayDateIncludeToday(Date d, int promiseRepayDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.DAY_OF_MONTH, promiseRepayDay);
        if(cal.getTime().after(d))
            cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public static Date getNextDay(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();
    }

    public static Date getDateAfter(Date date, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }


}