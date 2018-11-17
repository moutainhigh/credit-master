package com.zdmoney.credit.core;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.SpringContextUtil;
import com.zdmoney.credit.person.dao.PersonNfcsWorkerCallDaoImpl;
import com.zdmoney.credit.person.dao.pub.IPersonNfcsWorkerCallDao;
import com.zdmoney.credit.person.domain.PersonNfcsWorkerCall;
import com.zdmoney.credit.system.service.SequencesServiceImpl;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zenda.nfcs.NFCSWebService;

public class NFCSWorkerHelper {
	private static String byteEncoding;
    private static String scertType; //证件类型为身份证
    private static String sreasoncode;//查询理由：02 贷款审批
    private static String sreasoncode2;//查询理由：01 贷后管理
    private static int iplate; // 1:网络金融版个人信用报告 ,3:网络金融特殊交易版信用报告
    private static int timeout; //超时设为1分钟，秒为单位。
    private static String directsecret; //直连接口密码
    private static String orgno; //机构号
    private static int term; //访问周期
    private static int termvalue;//初审征信报告访问周期

    private static IPersonNfcsWorkerCallDao personNfcsWorkerCallDao;
    private static ISequencesService sequencesService;
    
    static {
    	if (personNfcsWorkerCallDao == null) {
    		personNfcsWorkerCallDao = (PersonNfcsWorkerCallDaoImpl)SpringContextUtil.getBean("personNfcsWorkerCallDaoImpl");
		}
    	if (sequencesService == null) {
    		sequencesService = (SequencesServiceImpl)SpringContextUtil.getBean("sequencesServiceImpl");
		}
    	
        try {
        	Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("nfcs.properties"));
            byteEncoding = prop.getProperty("NFCSW.byteEncoding").trim();
            scertType = prop.getProperty("NFCSW.scertType").trim();
            sreasoncode = prop.getProperty("NFCSW.sreasoncode").trim();
            sreasoncode2 = prop.getProperty("NFCSW.sreasoncode2").trim();
            iplate = Integer.parseInt(prop.getProperty("NFCSW.iplate").trim());
            timeout = Integer.parseInt(prop.getProperty("NFCSW.timeout").trim());
            directsecret = prop.getProperty("directsecret").trim();
            orgno = prop.getProperty("orgno").trim();
            term = Integer.parseInt(prop.getProperty("NFCSW.term").trim());
            termvalue=Integer.parseInt(prop.getProperty("NFCSW.termvalue").trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**货款审批查询**/
    public static String getCreditReport(String scertno,String name){
        String resp = "";
        try{
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("idnum", scertno);
            paramMap.put("searchCode", "02");
            paramMap.put("orderField", "HANDLE_TIME");
            paramMap.put("orderType", "DESC");
            
            List<PersonNfcsWorkerCall> list = personNfcsWorkerCallDao.findListByMap(paramMap);
        	
            if(list != null && list.size()>0) {//1.先查询本地数据
            	PersonNfcsWorkerCall nwc= list.get(0);
                resp = nwc.getCallBackinfo();
                long handleTime = nwc.getHandleTime().getTime();
                long currentTime = System.currentTimeMillis();
                if(((currentTime - handleTime)/(1000*60*60*24)) > termvalue) { //1.1如果之前第一次操作时间大于15个自然日，连接第三方重新查询，并更新历史记录
                	NFCSWebService worker = new NFCSWebService();
                	resp = worker.queryCredit(orgno, directsecret, iplate, scertType, scertno, name, sreasoncode, "1", byteEncoding, timeout);
                    JSONObject json = JSONObject.parseObject(resp);
                    if(json.containsKey("state") && "1".equals(json.getString("state"))) {
                        nwc.setCallBackinfo(json.getString("result"));
                        nwc.setHandleTime(new Date());
                        saveOrUpdate(nwc);
                    } else {
                        if(json.containsKey("state") && "2".equals(json.getString("state"))) {
                            json.put("success", false);
                            resp = json.toString();
                            nwc.setHandleTime(new Date());
                            saveOrUpdate(nwc);
                        }
                        //resp = json.put("success", false).toString();
                        json.put("success", false);
                        resp = json.toString();
                    }
                }
            } else {//2连接第三方查询
            	NFCSWebService worker = new NFCSWebService();
            	resp = worker.queryCredit(orgno, directsecret, iplate, scertType, scertno, name, sreasoncode, "1", byteEncoding, timeout);
                JSONObject json = JSONObject.parseObject(resp);
                if(json.containsKey("state") && "1".equals(json.getString("state"))){
                    resp = json.getString("result").toString();
                    saveInfo(scertno,resp, "02", name);
                } else {
                    if(json.containsKey("state") && "2".equals(json.getString("state"))){
                        json.put("success", false);
                        resp = json.toString();
                        saveInfo(scertno,resp, "02", name);
                    }
                    json.put("success",false);
                    resp = json.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
    
    /*贷后管理查询*/
    public static String getCreditReport2(String scertno,String name) {
        String resp = "";
        Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(new Date());//设置时间为传递过来的时间
        calendar.add(Calendar.DATE, -term);

        try {
        	Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("idnum", scertno);
            paramMap.put("searchCode", "01");
            paramMap.put("comparator", "ge");//大于等于
            paramMap.put("handleTime", calendar.getTime());
            paramMap.put("orderField", "HANDLE_TIME");
            paramMap.put("orderType", "DESC");
            
            List<PersonNfcsWorkerCall> list = personNfcsWorkerCallDao.findListByMap(paramMap);
            
            if(list != null && list.size()>0) {
                resp = list.get(0).getCallBackinfo();
            } else {
                NFCSWebService worker = new NFCSWebService();
                resp = worker.queryCredit(orgno, directsecret, iplate, scertType, scertno, name, sreasoncode2, "1", byteEncoding, timeout);
                JSONObject json = JSONObject.parseObject(resp);
                if(json.containsKey("state") && "1".equals(json.getString("state"))){
                    resp = json.getString("result").toString();
                    saveInfo(scertno,resp, "01", calendar.getTime());
                }else{
                    //resp = json.put("success",false).toString();
                    json.put("success", false);
                    resp = json.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    public static String uploadReport(String filePath) {
        String resp="";
        try {
            resp = new NFCSWebService().uploadMessageFile(orgno, directsecret,new File(filePath), byteEncoding, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    private static void saveInfo(String idnum,String callBackinfo,String searchCode,String name){
        try {
        	PersonNfcsWorkerCall nfcsWorkerCall = new PersonNfcsWorkerCall();
        	nfcsWorkerCall.setCallBackinfo(callBackinfo);
        	nfcsWorkerCall.setHandleTime(new Date());
        	nfcsWorkerCall.setIdnum(idnum);
        	nfcsWorkerCall.setSearchCode(searchCode);
        	nfcsWorkerCall.setName(name);
        	saveOrUpdate(nfcsWorkerCall);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void saveInfo(String idnum,String callBackinfo,String searchCode,Date term){
        try {
        	Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("idnum", idnum);
            paramMap.put("searchCode", "01");
            paramMap.put("comparator", "le");//小于等于
            paramMap.put("handleTime", term);
            paramMap.put("orderField", "HANDLE_TIME");
            paramMap.put("orderType", "DESC");
            
            List<PersonNfcsWorkerCall> list = personNfcsWorkerCallDao.findListByMap(paramMap);
        	
            if(list != null && list.size()>0){
            	PersonNfcsWorkerCall nwc = list.get(0);
                nwc.setCallBackinfo(callBackinfo);
                nwc.setHandleTime(new Date());
                saveOrUpdate(nwc);
            }else{
                PersonNfcsWorkerCall nfcsWorkerCall = new PersonNfcsWorkerCall();
            	nfcsWorkerCall.setCallBackinfo(callBackinfo);
            	nfcsWorkerCall.setHandleTime(new Date());
            	nfcsWorkerCall.setIdnum(idnum);
            	nfcsWorkerCall.setSearchCode(searchCode);
            	saveOrUpdate(nfcsWorkerCall);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void saveOrUpdate(PersonNfcsWorkerCall nwc) {
    	Long id = nwc.getId();
    	if (id == null) {
    		nwc.setId(sequencesService.getSequences(SequencesEnum.PERSON_NFCS_WORKER_CALL));
    		personNfcsWorkerCallDao.insert(nwc);
		} else {
			personNfcsWorkerCallDao.update(nwc);
		}
    }
}
