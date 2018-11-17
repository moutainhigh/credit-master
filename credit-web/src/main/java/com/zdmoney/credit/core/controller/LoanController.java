package com.zdmoney.credit.core.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.vo.VloanPersonOrg;
import com.zdmoney.credit.person.domain.PersonContactInfo;
import com.zdmoney.credit.person.domain.PersonTelInfo;
import com.zdmoney.credit.person.service.pub.IPersonContactInfoService;
import com.zdmoney.credit.person.service.pub.IPersonTelInfoService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Controller
@RequestMapping("/loan")
public class LoanController extends BaseController {
	private static final Logger logger = Logger.getLogger(LoanController.class);
	
	@Autowired
    private ISysParamDefineService sysParamDefineService;

	@Autowired
	private IPersonTelInfoService personTelInfoService;
	
	@Autowired @Qualifier("personContactInfoServiceImpl")
	IPersonContactInfoService personContactInfoServiceImpl;
	
	@RequestMapping("/getLoanByPhone")
	@ResponseBody
	public String getLoanByPhone(@RequestParam("phone") String phone,HttpServletRequest request, HttpServletResponse response)throws IOException{
		Map<String, Object> json = new HashMap<String, Object>();
		List<PersonTelInfo> infoList = new ArrayList<PersonTelInfo>();
		List  listResult = new ArrayList<>();
		String remoteIP = getIpAddr(request); //ipv6 localhost = 0:0:0:0:0:0:0:1
		String legalIP = sysParamDefineService.getSysParamValue("codeHelper", "acdCrmServer");
		if(Strings.isNotEmpty(legalIP) && legalIP.indexOf(remoteIP) == -1  ){//该字段为空时不检查ip，为调试留有余地
	         return "no permission to query";
	    }
		 if(phone == null || phone.trim() == ""){
			json.put("loans", infoList);			  
	        return JSONObject.toJSONString(json);
	     }
		 if(phone.trim().indexOf("-")!=-1){
			 if(!isTelephone(phone)){
				 return "入参格式错误!";
			 }
		 }else{
			 if(phone.startsWith("0")){
				 if(!isTelephone(phone)){
					 return "入参格式错误!";
				 }
			 }else{
				 if(!isMobilePhone(phone)){
					 return "入参格式错误!";
				 }
			 }
		 }
		 String telPhone = phone.trim().replace("-","");
		 infoList=personTelInfoService.findByPhone(telPhone);
		 if(infoList == null ||infoList.size()==0){
				json.put("loans", infoList);			  
		        return JSONObject.toJSONString(json);
		 }
		 String idnumMask="";
		 for(PersonTelInfo personTelInfo:infoList){
			if("zdsys.Borrower".equals(personTelInfo.getClassName())){
				 List<VloanPersonOrg> list = personTelInfoService.findByPersonId(personTelInfo.getObjectId());			
				 for(VloanPersonOrg vloanPersonOrg :list){
					 Map<String,Object> map=new HashMap<String,Object>();
					 map.put("name", vloanPersonOrg.getName());
					 String idnum=vloanPersonOrg.getIdnum();
					 if(Strings.isNotEmpty(idnum)){
						 idnumMask=idnum.substring(0, 6)+ "********"+ idnum.substring(14);
						 map.put("idnum", idnumMask);
					 }else{
						 map.put("idnum", "");
					 }
					 map.put("salesDep", vloanPersonOrg.getSalesDep());
					 map.put("loanType", vloanPersonOrg.getLoanType());
					 map.put("time", vloanPersonOrg.getTime());
					 map.put("money", vloanPersonOrg.getMoney());
					 map.put("loanState", vloanPersonOrg.getLoanState());
					 listResult.add(map);
				 }
			}else if("zdsys.Contact".equals(personTelInfo.getClassName())){
				PersonContactInfo personContactInfo=new PersonContactInfo();
				personContactInfo.setId(personTelInfo.getObjectId());
				List<PersonContactInfo> list=personContactInfoServiceImpl.findListByVo(personContactInfo);
				for(PersonContactInfo obj:list){
					Map<String,Object> map=new HashMap<String,Object>();
					 map.put("name",obj.getName());
					 map.put("idnum",obj.getIdCard());
					 listResult.add(map);
				}
				
				
			}
			json.put("loans", listResult);	
		 }
		 logger.info("手机号:【"+telPhone+"】话务系统查询返回JSON："+json);
		return JSONObject.toJSONString(json);		 
	}
	
	
	 /**
     * 获取客户端请求的IP地址
     * @param request
     * @return ip地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        return ip;
    }
    /**
     * @param value
     * @return
     * Description ： 验证是否是手机号(规则：以1开头，接任意数字，11位)
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static  boolean isMobilePhone(String value)
    {
        //String regex = "^[1]+[3,5]+\\d{9}$";
        String regex = "^[1]+\\d{10}";
        return value.matches(regex);
    }
    /**
     * @param str
     * Description ：验证是否是固定电话(规则：xxxx(区号，3或4位)-(连接线，必备)xxxxxx(电话，6到8位),如028-83035137)
     *         匹配则返回true
     *         不匹配则返回false
     */
    public static  boolean isTelephone(String str)
    {
        String regex = "^(\\d{3,4}-)?\\d{6,8}$";
        return match(regex, str);
    }
    /**
     * @param regex
     * @param str
     * @return
     * Description ： 正则表达式验证方法
     *         匹配表达式则返回true
     *         不匹配则返回false
     */
    private static  boolean match(String regex, String str)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    public static void main(String[] args) {
		String s ="02883035137";
		System.out.println(s.indexOf("-"));
	}
}
