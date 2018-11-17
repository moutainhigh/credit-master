package com.zdmoney.credit.common.util;

/**
 * 字符合法性验证工具类
 * Created by 00232949 on 2015/5/14.
 */
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.GenericValidator;

/**
 *
 * @author  :ylzhang
 * @version : 1.00
 * Description :
 *             内容校验类
 */
public abstract class ValidateUtil implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 默认编码格式
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Function  : 检查字符串的格式
     * @author   : ylzhang
     * @param str           ：  被检查的字符串
     * @param isCN         ：  允许有中文
     * @param isNum        ： 允许有数字
     * @param isLetter     ： 允许有字母
     * @param specialChars  :  允许有特殊字符,输入方式:假设允许下划线则参数为,"_"  假如允许下划线和问号则参数为,"_","?"
     * example : 字母、数字、问号、句号和感叹号组成的字符串：isRealChar("xxxx",false,true,true,"?","。","!")
     * @return   匹配则返回true,不匹配返回false
     */
    public static boolean isRealChar(String str,boolean isCN,boolean isNum,boolean isLetter,String... specialChars){
        String regex_start = "^[";
        String regex_end = "]+$";

        if(isCN == true){
            regex_start = regex_start + "|\u4e00-\u9fa5";
        }if(isNum == true){
            regex_start = regex_start + "|0-9";
        }if(isLetter == true){
            regex_start = regex_start + "|a-zA-Z";
        }if(specialChars != null){
            for(int i = 0;i<specialChars.length;i++){
                regex_start = regex_start +"|"+specialChars[i];
            }
        }
        return match(regex_start+regex_end,str);
    }



    /**
     * @param str
     * @return
     * Description ： 验证邮箱的有效性
     *         成功匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isEmail(String str)
    {
        //return str.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+") ;
        String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        return match(regex, str);
    }

    /**
     * @param str
     * @return
     * Description ：验证IP地址
     *         匹配返回true
     *         不匹配返回false
     */
    public static boolean isIP(String str)
    {
        String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
        String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
        return match(regex, str);
    }

    /**
     *
     * @param str
     * @return
     * Description ： 校验网址(规则：以http://或https://开头)
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isUrl(String str)
    {
        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        return match(regex, str);
    }

    /**
     * @param str
     * Description ：验证是否是固定电话(规则：xxxx(区号，3或4位)-(连接线，必备)xxxxxx(电话，6到8位),如028-83035137)
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isTelephone(String str)
    {
        String regex = "^(\\d{3,4}-)?\\d{6,8}$";
        return match(regex, str);
    }

    /**
     * @param value
     * @return
     * Description ： 验证是否是手机号(规则：以1开头，接任意数字，11位)
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isMobilePhone(String value)
    {
        //String regex = "^[1]+[3,5]+\\d{9}$";
        String regex = "^[1]+\\d{10}";
        return value.matches(regex);
    }

    /**
     * @param str
     * @return
     * Description ：邮编地址
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isPostalcode(String str)
    {
        String regex = "^\\d{6}$";
        return match(regex, str);
    }

    /**
     * @param str
     * @return
     * Description ：验证15位或18位身份证号
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isIDcard(String str)
    {
        try {
            String value = CheckIdCard.IDCardValidate(str);
            if("true".equals(value)){
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
        return false;
    }

    /**
     * @param str     被验证数据
     * @param digit   验证长度：小数点后几位
     * @return
     * Description ： 验证是否符合指定位数的小数
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isDecimal(String str,int digit)
    {
        String regex = "^[0-9]+(.[0-9]{"+digit+"})?$";
        return match(regex, str);
    }

    /**
     * @param str
     * @return
     * Description ：验证是否是1~12月
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isMonth(String str)
    {
        String regex = "^(0?[1-9]|1[0-2])$";
        return match(regex, str);
    }

    /**
     * @param str     ： 实际日期
     * @param format  ：日期格式，如yyyy-MM-dd
     * @return
     * @throws ParseException
     * Description ： 判断是否是指定日期格式
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isDateFormat(String str, String format){
        if (GenericValidator.isBlankOrNull(str) || GenericValidator.isBlankOrNull(format)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(true);

        Date date;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            return false;
        }
        String str2 = sdf.format(date);

        if (!str.equals(str2)) {
            return false;
        }else{
            return true;
        }
    }

    /**
     * @param str
     * @param sign  : 三种"+","-","all",分别表示正数、负数、正负都行,默认全部
     * @return
     * Description ：校验正负整数
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isIntNumber(String str,String sign)
    {
        String regex = "";
        if("+".equals(sign)){
            regex = "^[+]?[0-9]*$";
        }else if("-".equals(sign)){
            regex = "^[-][0-9]*$";
        }else{
            regex = "^[+-]?[0-9]*$";
        }
        return match(regex, str);
    }


    /**
     * @param str
     * @return
     * Description ：验证大写字母
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isUpChar(String str)
    {
        String regex = "^[A-Z]+$";
        return match(regex, str);
    }

    /**
     * @param str
     * @return
     * Description ： 验证小写字母
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isLowChar(String str)
    {
        String regex = "^[a-z]+$";
        return match(regex, str);
    }

    /**
     * @param str
     * @return
     * Description ：验证输入的是字母
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static boolean isLetter(String str)
    {
        String regex = "^[A-Za-z]+$";
        return match(regex, str);
    }

    /**
     * @param str
     * @param encoding : 编码格式,如GBK,   不输入默认为UTF-8
     * @return ：返回指定编码格式下字符串的长度
     * Description ：选择不同编码，返回字符串长度
     */
    public static int checkLength(String str,String encoding) {
        int length;
        try {
            if(encoding == null||"".equals(encoding)){
                length = str.getBytes(DEFAULT_ENCODING).length;
            }else{
                length = str.getBytes(encoding).length;
            }
        } catch (UnsupportedEncodingException e) {
            length = str.getBytes().length;
        }
        return length;
    }




    /**
     * @param regex
     * @param str
     * @return
     * Description ： 正则表达式验证方法
     *         匹配表达式则返回true
     *         不匹配则返回false
     */
    private static boolean match(String regex, String str)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    
    
    public static void main(String[] args) {
    	System.out.println("isRealChar:" + ValidateUtil.isRealChar("testsrc", false, true, true, "_","-"));
    	
    	System.out.println("isEmail:" + ValidateUtil.isEmail("a@a.com"));
    	
    	System.out.println("isIP:" + ValidateUtil.isIP("192.168.0.1"));
    	
    	System.out.println("isUrl:" + ValidateUtil.isUrl("http://111.111.111.1:8080/a"));
    	
    	System.out.println("isTelephone:" + ValidateUtil.isTelephone("028-83035137"));
    	
    	System.out.println("isMobilePhone:" + ValidateUtil.isMobilePhone("13111111111"));
    	
    	System.out.println("isPostalcode:" + ValidateUtil.isPostalcode("200001"));
    	
    	System.out.println("isIDcard:" + ValidateUtil.isIDcard("111111111111111111"));
    	
    	System.out.println("isDateFormat:" + ValidateUtil.isDateFormat("20150505", "yyyyMMdd"));
    	
    	System.out.println("isMonth:" + ValidateUtil.isMonth("1"));
    	
    	System.out.println("isDateFormat:" + ValidateUtil.isDecimal("111.11", 2));
    	
    	System.out.println("isIntNumber:" + ValidateUtil.isIntNumber("111", "+"));
    	
    	System.out.println("isUpChar:" + ValidateUtil.isUpChar("AAAA"));
    	
    	System.out.println("isLowChar:" + ValidateUtil.isLowChar("aaa"));
    	
    	System.out.println("isLetter:" + ValidateUtil.isLetter("asdAAA"));
    	
    	System.out.println("checkLength:" + ValidateUtil.checkLength("123sdfsdf__", null));
    	
	}
}
