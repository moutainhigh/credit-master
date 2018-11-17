package com.zdmoney.credit.loan.controller;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerFileTypeEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerOperateTypeEnum;
import com.zdmoney.credit.common.constant.repay.RequestManagementConst;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.FileDownUtils;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.MapUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.FileUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.common.vo.core.LoanBaseAppVo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.IUploadLoanDataInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.loan.util.LoanExternalDebtUtil;
import com.zdmoney.credit.loan.vo.LoanImageFile;
import com.zdmoney.credit.loan.vo.LoanRepaymentDetailVo;
import com.zdmoney.credit.loan.vo.TotalMoney;
import com.zdmoney.credit.loan.vo.VLoanDebtFileInfo;
import com.zdmoney.credit.loan.vo.VLoanDebtInfo;
import com.zdmoney.credit.loan.vo.VeloanExport;
import com.zdmoney.credit.loan.vo.VloanDebtCheckInfo;
import com.zdmoney.credit.loan.vo.VloanJmhzExport;
import com.zdmoney.credit.loan.vo.VloanPersonInfo;
import com.zdmoney.credit.person.service.pub.IPersonTelInfoService;
import com.zdmoney.credit.repay.service.pub.IRequestManagementService;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.video.vo.DownLoadVideoDirVo;
import com.zdmoney.credit.video.vo.DownLoadVideoFileVo;

@Controller
@RequestMapping("/loan")
public class LoanExternalDebtController extends BaseController {
    
    public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    
    @Autowired
    private ILoanBaseService loanBaseService;
    
    @Autowired
    private IRequestManagementService requestManagementService;
    
    @Autowired
    private IVLoanInfoService vLoanInfoService;
    
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    
    @Autowired
    private IPersonTelInfoService personTelInfoService;
    @Autowired
    private IUploadLoanDataInfoService uploadLoanDataInfoService;
    @Autowired
    private IConnectBhxtFtpService connectBhxtFtpService;
    
    @RequestMapping("/loanExternalDebtDefault")
    public String message(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {
        User user=UserContext.getUser();        
        Date  monthStartDate=  new Date();
        Date  currentDate=  new Date();
        modelMap.put("monthStartDate", monthStartDate);    
        modelMap.put("currentDate", currentDate);    
        modelMap.put("userCode", user.getUserCode());
        return "/loan/loanExternalDebtDefault";
    }
    
    /**
     * 加载查询批次号的数据
     * @param org
     * @param loan
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/listExternalDebt")    
    public String listLoanBase( @RequestParam("org") String org , @RequestParam("batchNum") String batchNum ,LoanBase loan,int rows, int page,HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> paramMap =new HashMap<String, Object>();    
        Pager pager = new Pager();    
        paramMap.put("loan", loan);
        pager.setRows(rows);
        pager.setPage(page);
        pager.setSidx("batchNum");
        pager.setSort("desc");
        paramMap.put("pager", pager);        
        paramMap.put("org", org);        
        paramMap.put("batchNum", batchNum); 
        pager = loanBaseService.getLoanBaseBybatchNum(paramMap);
        return toPGJSONWithCode(pager);    
    }
    
    /**
     *  加载当天可生成批次号的数据
     * @param org
     * @param loan
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/listCurrentDayExternalDebt")    
    public String listCurrentDayLoanBase( @RequestParam("org") String org ,LoanBase loan,int rows, int page,HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> paramMap =new HashMap<String, Object>();    
        Pager pager = new Pager();    
        paramMap.put("loan", loan);
        pager.setRows(rows);
        pager.setPage(page);
        pager.setSidx("GRANT_MONEY");
        pager.setSort("asc");
        paramMap.put("pager", pager);    
        paramMap.put("org", org);
        User user=UserContext.getUser();
        paramMap.put("orgCode", user.getOrgCode());    
        pager = vLoanInfoService.getCurrentDayLoanBaseBybatchNum(paramMap);
        return toPGJSONWithCode(pager);    
    }
    
    /**
     * 查询批次号查询进入默认页面
     * @param org
     * @param loan
     * @param request
     * @param modelMap
     */
    @RequestMapping("/querBatchNumDefault")    
    public void querBatchNumDefault( ModelMap modelMap){
    }
    
    /**
     * 查询当天批次次号查询进入默认页面
     * @param org
     * @param loan
     * @param request
     * @param modelMap
     */
    @RequestMapping("/querCurrentDayBatchNumDefault")    
    public void querCurrentDayBatchNumDefault(ModelMap modelMap){
    }
    /**
     * 查询批次号进入默认页面
     * @param batNum
     * @param modelMap
     */
    @RequestMapping("/querBatchNumDetail")    
    public void querBatchNumDetail( @RequestParam("batNum") String batNum,ModelMap modelMap){
        modelMap.put("batNum", batNum);
    }
    
    /**
     * 查询积木盒子进入默认页面
     * @param org
     * @param loan
     * @param request
     * @param modelMap
     */
    @RequestMapping("/querBatchNumJmhzDefault")    
    public void querBatchNumJmhzDefault(ModelMap modelMap){        
        
    }
    
    /**
     * 点击批次号，进入详细查询页面
     * @param batNum
     * @param loan
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/listquerBatchNumDetail")
    public String listquerBatchNumDetail(@RequestParam("batNum") String batNum, @RequestParam("org") String org, 
            int rows, int page,HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        pager.setSidx("batch_num");
        pager.setSort("desc");
        paramMap.put("pager", pager);
        paramMap.put("batchNum", batNum);
        paramMap.put("org", org);
        // 系统当前日期
        String sysdate = Dates.getDateTime(new Date(), "yyyyMMdd");
        // 当天批次号
        if (sysdate.equals(batNum.substring(2, 10)) || sysdate.equals(batNum.substring(4, 12))) {
            if ("WC2-".equals(batNum.substring(0, 4))) {
                pager = vLoanInfoService.listquerBatchNumWC2Detail(paramMap);
                this.setChangeFlag(pager,"1");
            }else if("LXXD".equals(batNum.substring(0, 4))){//龙信小贷
            	 pager = vLoanInfoService.listquerBatchNumLXXDDetail(paramMap);
                 this.setChangeFlag(pager,"1");
            }else if("WMXT".equals(batNum.substring(0, 4))){//外贸信托
           	     pager = vLoanInfoService.listquerBatchNumWMXTDetail(paramMap);
                 this.setChangeFlag(pager,"1");
            }else if ("HM".equals(batNum.substring(0, 2))) { // 海门小贷
                pager = vLoanInfoService.listquerBatchNumHMDetail(paramMap);
                this.setChangeFlag(pager,"1");
            } else if("BHXT".equals(batNum.substring(0,4))){
            	pager = vLoanInfoService.listquerBatchNumBHXTDetail(paramMap);                   
                this.setChangeFlag(pager,"1");
            }else if("BH2-".equals(batNum.substring(0,4))){
            	pager = vLoanInfoService.listquerBatchNumBHXT2Detail(paramMap);	
            }else if("HRBH".equals(batNum.substring(0,4))){
            	pager = vLoanInfoService.listquerBatchNumBHXT3Detail(paramMap);
            }else if("WM2-".equals(batNum.substring(0,4))){
            	pager = vLoanInfoService.listquerBatchNumWM2Detail(paramMap);	
            }else if("BSYH".equals(batNum.substring(0,4))){//包商银行
            	pager = vLoanInfoService.listquerBatchNumBSYHDetail(paramMap);
            	this.setChangeFlag(pager,"1");
            }else {
                pager = vLoanInfoService.listquerBatchNumNotWC2Detail(paramMap);
                this.setChangeFlag(pager,"1");
            }
        } else {// 非当天批次
            pager = vLoanInfoService.listquerBatchNumDetail(paramMap);
            this.setChangeFlag(pager,"0");
        }
        return toPGJSONWithCode(pager);
    }

    /**
     * 设置历史批次与当前批次的标示字段（0：表示当天批次，1：表示历史批次）
     * @param pager
     * @param changeFlag
     */
    @SuppressWarnings("unchecked")
    private void setChangeFlag(Pager pager,String changeFlag) {
        if (CollectionUtils.isNotEmpty(pager.getResultList())) {
            List<VloanPersonInfo> vLoanList = pager.getResultList();
            for (VloanPersonInfo vLoan : vLoanList) {
                vLoan.setChangeFlag(changeFlag);
            }
        }
    }

    @ResponseBody
    @RequestMapping("/listTotalGrantMoney")    
    public String listquerBatchNumDetail(@RequestParam("batNum") String batNum){
        Map<String, Object> paramMap =new HashMap<String, Object>();
        AttachmentResponseInfo<Object> attachmentResponseInfo = null;
        paramMap.put("batchNum", batNum);    
        List<TotalMoney> list=vLoanInfoService.getTotalMoney(paramMap);         
        attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
        attachmentResponseInfo.setAttachment(list==null?"0":list.get(0));
        return toResponseJSON(attachmentResponseInfo);
    }
    @ResponseBody
    @RequestMapping("/listTotalPactMoney")    
    public String listTotalPactMoney(@RequestParam("batNum") String batNum){
        Map<String, Object> paramMap =new HashMap<String, Object>();
        AttachmentResponseInfo<Object> attachmentResponseInfo = null;
        paramMap.put("batchNum", batNum);    
        List<TotalMoney> list=vLoanInfoService.getTotalMoney(paramMap);         
        attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
        attachmentResponseInfo.setAttachment(list==null?"0":list.get(0).getTotalPactMoney());
        return toResponseJSON(attachmentResponseInfo);
    }
    
    /**
     * 生成批次
     * @param ids
     * @param org
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/createBatchNum")
    public String createBatchNum(@RequestParam("ids") String[] ids, @RequestParam("org") String org, 
            HttpServletRequest request, HttpServletResponse response) {
        AttachmentResponseInfo<Object> attachmentResponseInfo = null;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        List<Long> list = new ArrayList<Long>();
        if(null == ids || ids.length == 0){
            attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_FAILD,"无债权编号，参数非法！");
            return toResponseJSON(attachmentResponseInfo);
        }
        for (String idStr : ids) {
            Long loanId = Long.valueOf(idStr);
            list.add(loanId);
        }
        // 检查批次号是否重复
        int count = vLoanInfoService.checkBatchNum(list);
        if (count > 0) {
            attachmentResponseInfo = new AttachmentResponseInfo<Object>(
                    ResponseEnum.LOANEXTERNALDEBT_FAIL.getCode(),
                    ResponseEnum.LOANEXTERNALDEBT_FAIL.getDesc());
            return toResponseJSON(attachmentResponseInfo);
        }
        paramMap.put("list", list);
        paramMap.put("org", org);
        if ("AT".equals(org)) {
            vLoanInfoService.updateBatchNumAT(list);
        }  
        vLoanInfoService.updateBatchNum(paramMap);
        if("LXXD".equals(org)){
        	String batchNum = (String)paramMap.get("batchNum");
        	uploadLoanDataInfoService.uploadLoanDataThread(batchNum);
        }
        attachmentResponseInfo = new AttachmentResponseInfo<Object>(
                ResponseEnum.SYS_SUCCESS.getCode(),
                ResponseEnum.SYS_SUCCESS.getDesc(), "");
        return toResponseJSON(attachmentResponseInfo);
    }
    
    /**
     * 龙信小贷手动触发 上传债券资料
     * @param batchNum
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/uploadLoanData2LXXDByBatchNum")
    public String createBatchNum(@RequestParam("batchNum") String batchNum, HttpServletRequest request, HttpServletResponse response) {
    	AttachmentResponseInfo<Object> attachmentResponseInfo = null;
    	try{
    		if(Strings.isEmpty(batchNum)){
    			throw new PlatformException(ResponseEnum.FULL_MSG, "batchNum不能为空");
    		}
	    	VLoanInfo vLoanInfo = new VLoanInfo();
	    	vLoanInfo.setBatchNum(batchNum);
	    	List<VLoanInfo> list = vLoanInfoService.findListByVO(vLoanInfo);
	    	if(CollectionUtils.isEmpty(list)){
	    		throw new PlatformException(ResponseEnum.FULL_MSG, "查询该批次下债权为空");
	    	}
	    	String fundsSource = list.get(0).getFundsSources();
	    	if(!FundsSourcesTypeEnum.龙信小贷.getValue().equals(fundsSource)){
	    		throw new PlatformException(ResponseEnum.FULL_MSG, "该批次合同来源非龙信小贷");
	    	}
	    	uploadLoanDataInfoService.uploadLoanDataThread(batchNum);
	        attachmentResponseInfo = new AttachmentResponseInfo<Object>(
	                ResponseEnum.SYS_SUCCESS.getCode(),
	                ResponseEnum.SYS_SUCCESS.getDesc(), "");
    	}catch(PlatformException p){
    		attachmentResponseInfo = new AttachmentResponseInfo<>(p.getResponseCode().getCode(), p.getMessage());
    	}
    	return toResponseJSON(attachmentResponseInfo);
    }
    @ResponseBody
    @RequestMapping("/updateBatchNum")    
    @Transactional
    public String updateBatchNum(@RequestParam("ids") String[] ids ,@RequestParam("org") String org,@RequestParam("batchNum") String batchNum , HttpServletRequest request, HttpServletResponse response){
        AttachmentResponseInfo<Object> attachmentResponseInfo = null;
        Map<String, Object> paramMap=new HashMap<String, Object>();
        List<Long> list = new ArrayList<Long>();
        if(null != ids && ids.length>0){
            for(String idStr : ids){
                Long loanId = Long.valueOf(idStr);
                list.add(loanId);
            }
        }else{
        	list.add(Long.valueOf(0));
        }   
            paramMap.put("list", list);
            paramMap.put("batchNum", batchNum);
            int count=vLoanInfoService.checkBatchNumUpdate(paramMap);
            if(count>0){
                attachmentResponseInfo=new AttachmentResponseInfo<Object>(ResponseEnum.LOANEXTERNALDEBT_FAIL.getCode(), ResponseEnum.LOANEXTERNALDEBT_FAIL.getDesc());
                return toResponseJSON(attachmentResponseInfo);                
            }
           
            if("AT".equals(org)){                
                vLoanInfoService.updateBatchNumATByBatchNum(paramMap);
            }
                vLoanInfoService.updateBatchNumByBatchNumNull(paramMap);
                vLoanInfoService.updateBatchNumByBatchNum(paramMap);    
            if("AT".equals(org)){                
                vLoanInfoService.updateBatchNumAT(list);
            }    
        
            if("LXXD".equals(org)){
            	uploadLoanDataInfoService.uploadLoanDataThread(batchNum);
            }
        attachmentResponseInfo=new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),"");
        return toResponseJSON(attachmentResponseInfo);
    }
    
    /**
     * 爱特债权导出
     * @param repaymentDetail
     * @param request
     * @param response
     */
    @RequestMapping("/eloanExport")
    @ResponseBody
    public void eloanExport(VeloanExport veloanExport,@RequestParam("batchNum") String batchNum,HttpServletRequest request, HttpServletResponse response){
        ResponseInfo responseInfo = null;
        // 查询最大件数s
        String lastReapyday1 = new SimpleDateFormat("yyyy-MM-dd").format(Dates.getLastRepayDate(new Date()));
        String lastReapyday2 = new SimpleDateFormat("yyyy-MM-dd").format(Dates.getLastRepayDate(Dates.getLastRepayDate(new Date())));
        try {
            // 查询线上还款明细信息，最大件数不超过20000件
            List<VeloanExport> list=new ArrayList<VeloanExport>();
            Map<String, Object> paramMap =new HashMap<>();
            paramMap.put("max", "2000");
            paramMap.put("batchNum",  batchNum);
            paramMap.put("lastReapyday1", lastReapyday1);
            paramMap.put("lastReapyday2", lastReapyday2);
            User user=UserContext.getUser();
            paramMap.put("orgCode", user.getOrgCode());
            String flag=batchNum.substring(0, 2);
            String name="";
            String[] labels = new String[]{"借款ID","合同编号",  "产品类型","借款标题", "借款用途", "合同金额","审批金额",  "服务费",  "乙方咨询费",  "乙方评估费", "乙方管理费","丙方管理费", "风险金",  "借款期限",  "月利率","第三期退费", "第四期退费",  "第四期之后退费","首次逾期1天罚息", "首次逾期15天罚息", "首还款日期", "客服电话",  "现居住地址","邮政编码",  "合同签署地", "合同来源",  "第三方债权id", "月综合费率",  "是否加急", "用户昵称","真实姓名", "性别", "身份证号","邮箱",  "手机号码", "最高学历","放款营业部",  "开户人姓名", "开户银行", "银行分支机构",  "提现银行卡号", "补偿利率", "已还期数",  "产品批次号", "放款日","年龄","婚姻状况",    "单位性质","本工作开始日期","职位","车型","购买价格","房产类型","建筑面积",  "经营主体类型","成立年限","融资人持股比例"};
            String[] fields = new String[]{"id","contract_num","loan_type","aa","purpose","pact_money","approve_money","rate_sum","refer_rate","eval_rate","manage_rate","manager_rate_for_partyc","risk","time","rateEM","give_back_rate_for3term","give_back_rate_for4term","give_back_rate_after4term","overdue_penalty1day","overdue_penalty15day","startrdate","service_tel","address","postcode","signing_site","funds_sources","bb","rate","cc","dd","acct_name","borrower_sex","borrower_idnum","borrower_email","borrower_mphone","borrower_edLevel","signSalesDep_fullName","borrower_name","giveBackBank_bankName","giveBackBank_fullName","giveBackBank_account","accrualem","backTerm","batchNum","grant_money_date","age","married","c_type","c_enter_time","official_rank","car_type","price","house_type","building_area","enterprise_type","time_founded","shareholding_ratio"};
            if(flag.equals("XL")){
                name="新浪";
            }else if(flag.equals("WC")){
                name="挖财";
            }else if(flag.equals("AT") || flag.equals("SS")){
            	if(flag.equals("AT")){
            		name="爱特";
            	}
            	if(flag.equals("SS")){
            		name="随手记";
            	}
                labels=new String[]{"借款ID","合同编号", "产品类型","借款标题","借款用途","合同金额","审批金额","服务费","乙方咨询费","乙方评估费","乙方管理费", "丙方管理费","风险金","借款期限", "月利率","第三期退费","第四期退费", "第四期之后退费", "首次逾期1天罚息","首次逾期15天罚息","首还款日期","客服电话", "现居住地址","邮政编码", "合同签署地", "合同来源", "第三方债权id",  "月综合费率",  "是否加急",  "用户昵称", "真实姓名",  "性别", "身份证号",  "邮箱", "手机号码",  "最高学历", "放款营业部",  "开户人姓名", "开户银行",  "银行分支机构", "提现银行卡号","补偿利率"};
                fields = new String[]{"id","contract_num","loan_type","aa","purpose","pact_money","approve_money","rate_sum","refer_rate","eval_rate", "manage_rate", "manager_rate_for_partyc", "risk", "time", "rateEM","give_back_rate_for3term", "give_back_rate_for4term", "give_back_rate_after4term", "overdue_penalty1day", "overdue_penalty15day", "startrdate", "service_tel", "address", "postcode", "signing_site", "funds_sources", "bb", "rate", "cc", "dd", "acct_name", "borrower_sex", "borrower_idnum", "borrower_email", "borrower_mphone", "borrower_edLevel", "signSalesDep_fullName", "borrower_name", "giveBackBank_bankName",  "giveBackBank_fullName", "giveBackBank_account", "accrualem"};
            }
            list=vLoanInfoService.getVeloanExportList(paramMap);
            Assert.notCollectionsEmpty(list,"当前导出条件查询不到数据");
            String fileName = "批次号" +batchNum+name+"债权_供爱特"+ ".xls";
            // 工作表名称
            String sheetName = "Export";
            // 创建工作簿
            Workbook workbook = ExcelExportUtil.createExcelByVo(fileName, labels,fields, list, sheetName);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
    
    /**
     * 积木盒子债权导出
     * @param repaymentDetail
     * @param request
     * @param response
     */
    @RequestMapping("/eloanExportJmhz")
    @ResponseBody
    public void eloanExportJmhz(VloanJmhzExport vloanJmhzExport,@RequestParam("startQueryDate") String startQueryDate,@RequestParam("endQueryDate") String endQueryDate,HttpServletRequest request, HttpServletResponse response){
        ResponseInfo responseInfo = null;
        // 查询最大件数s
        vloanJmhzExport.setMax(Long.valueOf(20000));
        try {
            // 查询线上还款明细信息，最大件数不超过20000件
            List<VloanJmhzExport> list=new ArrayList<VloanJmhzExport>();
            Map<String, Object> paramMap =new HashMap<>();
            User user=UserContext.getUser();
            paramMap.put("orgCode", user.getOrgCode());
            paramMap.put("startQueryDate", startQueryDate);
            paramMap.put("endQueryDate", endQueryDate);
            paramMap.put("max", "2000");            
            list=vLoanInfoService.getVeloanJmhzExportList(paramMap);
            
            List<Map<String,Object>> listData=new ArrayList<Map<String,Object>>();
            String[] doubleKeys = {"pact_money","grant_money","servicerate","houseprice","shareholding_ratio","monthly_net_profit","six_month_shouru","lastreturneterm","returneterm","lastreturneterm"};
            for(VloanJmhzExport vloanJmhzExportTemp : list ){
            	Map<String,Object> temp  = new HashMap<String,Object>();
            	MapUtil.vObjectConvertToMap(vloanJmhzExportTemp, temp, true);
            	for(String key : doubleKeys){
    				try {
    					double value = Double.parseDouble(temp.get(key)+"");
    					temp.put(key, value);
    				} catch (Exception e) {
    					temp.put(key, "");
    				}
            	}
            	listData.add(temp);
            }
            
            Assert.notCollectionsEmpty(list,"当前导出条件查询不到数据");
            String fileName = "债权信息表"+ ".xls";
            String[] labels = new String[] {"项目编号","产品类型","申请城市","意向融资金额（万元）","融资人实收金额（万元）","证大服务费（元）","借款期限（月）","贷款用途","还款来源","签约时间","房产类型","建筑面积（平米）","购买日期（年）","房产总价（万元）","姓名","年龄（岁）","性别","身份证号","学历","通讯地址","户籍地址", "婚姻状况","单位性质","本工作开始日期（年）","职位","总工龄（年）","开户银行账号","开户银行代号","开户银行省份","开户银行地区","开户银行支行","经营主体类型","成立年限（年）","融资人持股比例","经营场所产权情况","员工数量（位）", "经营实体账户月均流入金额（万元）","近6个月月均银行账户流入（万元）","融资用户名","月偿还本息数额","最后一期还本息数额","还款日","还款起日期","还款止日期" };
            String[] fields = new String[] {"project_no","loan_type","city_name","pact_money","grant_money","servicerate","loan_time","purpose","huankuanlaiyuan","sign_date","house_type","building_area","buy_time","houseprice","person_name","age","sex","idnum","ed_level","address","hr_address","married","c_type","c_enter_time_year","official_rank","zonggongling","gb_account","give_back_bank","kaihubankshengfen","gb_full_name","bank_full_name","enterprise_type","time_founded_year","shareholding_ratio","premises_type","employee_amount","monthly_net_profit","six_month_shouru", "finance_name","returneterm","lastreturneterm","promise_return_date","startrdate","endrdate" };
            // 工作表名称
            String sheetName = "Export";
            // 创建工作簿
            Workbook workbook = ExcelExportUtil.createExcelByMap(fileName, labels,fields, listData, sheetName);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
    
    /**
     * 还款计划导出
     * @param repaymentDetail
     * @param request
     * @param response
     */
    @RequestMapping("/exportExtDebtReyment")
    @ResponseBody
    public void exportExtDebtReyment(LoanRepaymentDetailVo loanRepaymentDetail,@RequestParam("batchNum") String batchNum, HttpServletRequest request, HttpServletResponse response){
        ResponseInfo responseInfo = null; 
        try {            
            List<LoanRepaymentDetailVo> list=new ArrayList<LoanRepaymentDetailVo>();
            Map<String, Object> paramMap =new HashMap<>();
            paramMap.put("batchNum", batchNum);            
            paramMap.put("max", "2000");    
            String flag=batchNum.substring(0, 2);
            String name="";
            if(flag.equals("XL")){
                name="新浪";
            }else if(flag.equals("WC")){
                name="挖财";
            }else if(flag.equals("AT")){
                name="爱特2";
            }
            list=vLoanInfoService.getVeloanReymentExportList(paramMap);
            Assert.notCollectionsEmpty(list,"当前导出条件查询不到数据");
            String fileName = "批次号" +batchNum+name+"还款计划_供爱特"+ ".xls";
            String[] labels = new String[]{"借款ID",  "期数", "还款日", "还款金额",  "当期一次性还款金额"} ;
            String[] fields = new String[] {"loanId","currentTerm","returnDate","returneterm","repaymentAll"};
            // 工作表名称
            String sheetName = "Export";
            // 创建工作簿
            Workbook workbook = ExcelExportUtil.createExcelByVo(fileName, labels,fields, list, sheetName);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }

    /**
     * 债权导出供理财
     * @param repaymentDetail
     * @param request
     * @param response
     */
    @RequestMapping("/exportExtDebtInfo")
    @ResponseBody
    public void exportExtDebtInfo(VLoanDebtInfo vLoanDebtInfo,@RequestParam("batchNum") String batchNum,
            HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        String lastReapyday1 = new SimpleDateFormat("yyyy-MM-dd").format(Dates.getLastRepayDate(new Date()));
        String lastReapyday2 = new SimpleDateFormat("yyyy-MM-dd").format(Dates.getLastRepayDate(Dates.getLastRepayDate(new Date())));
        try {
            List<VLoanDebtInfo> list = new ArrayList<VLoanDebtInfo>();
            Map<String, Object> paramMap = new HashMap<>();
            User user = UserContext.getUser();
            paramMap.put("orgCode", user.getOrgCode());
            paramMap.put("batchNum", batchNum);
            paramMap.put("max", "2000");
            paramMap.put("lastReapyday1", lastReapyday1);
            paramMap.put("lastReapyday2", lastReapyday2);
            String[] labels = new String[] { "借款ID", "合同编号", "产品类型", "借款用途",
                    "合同金额", "借款期限", "已还期数", "首还款日期", "真实姓名", "性别", "身份证号","产品批次号" };
            String[] fields = new String[] { "id", "contract_num", "loan_type",
                    "purpose", "pact_money", "time", "backTerm", "startrdate",
                    "acct_name", "borrower_sex", "borrower_idnum", "batchNum" };
            String flag = batchNum.substring(0, 2);
            String name = "";
            if ("XL".equals(flag)) {
                name = "新浪";
            } else if ("WC2-".equals(batchNum.substring(0, 4)) || "HM".equals(flag)) { // 挖财2、海门小贷
                name = "挖财2";
                if("HM".equals(flag)){
                    name = "海门小贷";
                }
                labels = new String[] { "借款ID", "合同编号", "产品类型", "借款用途", "合同金额",
                        "借款期限", "已还期数", "首还款日期", "真实姓名", "性别", "身份证号", "产品批次号",
                        "放款银行卡开户行", "支行", "卡号", "放款金额", "服务费金额", "债权签约日期",
                        "到期日", "每月还款金额", "放款营业部" };
                fields = new String[] { "id", "contract_num", "loan_type",
                        "purpose", "pact_money", "time", "backTerm",
                        "startrdate", "acct_name", "borrower_sex",
                        "borrower_idnum", "batchNum", "bank_name", "full_name",
                        "account", "grant_money", "fuwufei", "sign_Date",
                        "endRDate", "returneterm", "signSalesDep_fullName" };
            } else if ("WC".equals(flag)) {
                name = "挖财";
            } else if ("AT".equals(flag)) {
                name = "爱特";
            }else if ("LX".equals(flag)) {
                  name = "龙信";
//                labels = new String[] { "序号", "合同号", "借款人姓名",  "身份证号", "来源营业部","首还款日","贷款期限(月)","合同金额(元)"};
//                fields = new String[] { "idNum", "contract_num","acct_name","borrower_idnum","signSalesDep_fullName","startrdate","time","pact_money"};
                  if("BSYH".equals("BSYH".equals(batchNum.substring(0, 4)))){
                	  name = "包商银行";
                  }
                  labels = new String[] { "借款ID", "合同编号", "产品类型", "借款用途", "合同金额",
                        "借款期限", "已还期数", "首还款日期", "真实姓名", "性别", "身份证号", "产品批次号",
                        "放款银行卡开户行", "支行", "卡号", "放款金额", "服务费金额", "债权签约日期",
                        "到期日", "每月还款金额", "放款营业部","录单营业部类型" };
                  fields = new String[] { "id", "contract_num", "loan_type",
                        "purpose", "pact_money", "time", "backTerm",
                        "startrdate", "acct_name", "borrower_sex",
                        "borrower_idnum", "batchNum", "bank_name", "full_name",
                        "account", "grant_money", "fuwufei", "sign_Date",
                        "endRDate", "returneterm", "signSalesDep_fullName","applyInputFlag" };
            }else if("BH".equals(flag)){
                name = "渤海";
                labels = new String[] { "借款ID", "合同编号", "产品类型", "借款用途", "合同金额",
                        "借款期限", "已还期数", "首还款日期", "真实姓名", "性别", "身份证号", "产品批次号",
                        "放款银行卡开户行", "支行", "卡号", "放款金额", "服务费金额", "债权签约日期",
                        "到期日", "每月还款金额", "放款营业部" };
                fields = new String[] { "id", "contract_num", "loan_type",
                        "purpose", "pact_money", "time", "backTerm",
                        "startrdate", "acct_name", "borrower_sex",
                        "borrower_idnum", "batchNum", "bank_name", "full_name",
                        "account", "grant_money", "fuwufei", "sign_Date",
                        "endRDate", "returneterm", "signSalesDep_fullName" };
            }else if("WM".equals(flag)){
            	name = "外贸";
                labels = new String[] { "借款ID", "合同编号", "产品类型", "借款用途", "合同金额",
                        "借款期限", "已还期数", "首还款日期", "真实姓名", "性别", "身份证号", "产品批次号",
                        "放款银行卡开户行", "支行", "卡号", "放款金额", "服务费金额", "债权签约日期",
                        "到期日", "每月还款金额", "放款营业部" };
                fields = new String[] { "id", "contract_num", "loan_type",
                        "purpose", "pact_money", "time", "backTerm",
                        "startrdate", "acct_name", "borrower_sex",
                        "borrower_idnum", "batchNum", "bank_name", "full_name",
                        "account", "grant_money", "fuwufei", "sign_Date",
                        "endRDate", "returneterm", "signSalesDep_fullName" };
            }else if("HRBH".equals(batchNum.substring(0, 4))){
                name = "华瑞渤海";
                labels = new String[] { "借款ID", "合同编号", "产品类型", "借款用途", "合同金额",
                        "借款期限", "已还期数", "首还款日期", "真实姓名", "性别", "身份证号", "产品批次号",
                        "放款银行卡开户行", "支行", "卡号", "放款金额", "服务费金额", "债权签约日期",
                        "到期日", "每月还款金额", "放款营业部" };
                fields = new String[] { "id", "contract_num", "loan_type",
                        "purpose", "pact_money", "time", "backTerm",
                        "startrdate", "acct_name", "borrower_sex",
                        "borrower_idnum", "batchNum", "bank_name", "full_name",
                        "account", "grant_money", "fuwufei", "sign_Date",
                        "endRDate", "returneterm", "signSalesDep_fullName" };
            }
            
            list = vLoanInfoService.getVLoanDebtInfoExportList(paramMap);
            Assert.notCollectionsEmpty(list, "当前导出条件查询不到数据");
            String fileName = "批次号" + batchNum + name + "债权_供理财" + ".xls";
            // 工作表名称
            String sheetName = "Export";
            // 创建工作簿
            Workbook workbook = ExcelExportUtil.createExcelByVo(fileName,labels, fields, list, sheetName);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
    
    /**
     * 债权审核导出
     * @param repaymentDetail
     * @param request
     * @param response
     */
    @RequestMapping("/exportExtDebtCheckInfo")
    @ResponseBody
    public void exportExtDebtCheckInfo(VloanDebtCheckInfo vloanDebtCheckInfo,
            @RequestParam("batchNum") String batchNum,
            HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            List<VloanDebtCheckInfo> list = new ArrayList<VloanDebtCheckInfo>();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("batchNum", batchNum);
            paramMap.put("max", "2000");
            String[] labels = new String[] { "产品类型", "借款金额（万元）", "借款期限（期）",
                    "借款用途", "姓名", "年龄", "性别", "身份证号", "学历", "现居住地址", "婚姻状况",
                    "单位性质", "本工作开始日期", "职位", "车型", "购买价格", "房产类型", "建筑面积",
                    "经营主体类型", "成立年限", "融资人持股比例" };
            String[] fields = new String[] { "loan_type", "grant_money",
                    "time", "purpose", "name", "age", "sex", "idnum",
                    "ed_level", "address", "married", "c_type", "c_enter_time",
                    "official_rank", "car_type", "price", "house_type",
                    "building_area", "enterprise_type", "time_founded","shareholding_ratio" };
            String flag = batchNum.substring(0, 2);
            String name = "";
            if ("XL".equals(flag)) {
                name = "新浪";
            } else if ("WC2-".equals(batchNum.substring(0, 4))) {
                name = "挖财2";
            } else if ("WC".equals(flag)) {
                name = "挖财";
            } else if ("HM".equals(flag)) {
                name = "海门小贷";
            } else if ("LX".equals(flag)) {
                name = "龙信";
            } else if ("BH".equals(flag)) {
                if ("BH2-".equals(batchNum.substring(0, 4))) {
                    name = "渤海2";
                } else {
                    name = "渤海";
                }
            }else if ("HRBH".equals(batchNum.substring(0, 4))) {
                name = "华瑞渤海";
            } else if ("WM".equals(flag)) {
                if ("WM2-".equals(batchNum.substring(0, 4))) {
                    name = "外贸2";
                } else {
                    name = "外贸";
                }
            } else if ("BSYH".equals(batchNum.substring(0, 4))) {
                name = "包商银行";
            }
            if (!"XL".equals(flag) || !"BSYH".equals(batchNum.substring(0, 4))) {
                labels = new String[] { "产品类型", "借款金额（万元）", "借款期限（期）",
                        "借款发放日期", "还款日期", "月还款额", "借款到期日期", "借款用途", "姓名", "年龄",
                        "性别", "身份证号", "学历", "现居住地址", "婚姻状况", "单位性质", "本工作开始日期",
                        "职位", "车型", "购买价格", "房产类型", "建筑面积", "经营主体类型", "成立年限",
                        "融资人持股比例" };
                fields = new String[] { "loan_type", "grant_money", "time",
                        "grant_money_date", "promise_return_date",
                        "returneterm", "endrdate", "purpose", "name", "age",
                        "sex", "idnum", "ed_level", "address", "married",
                        "c_type", "c_enter_time", "official_rank", "car_type",
                        "price", "house_type", "building_area",
                        "enterprise_type", "time_founded", "shareholding_ratio" };
            }
            list = vLoanInfoService.getVLoanDebtCheckInfoExportList(paramMap);
            Assert.notCollectionsEmpty(list, "当前导出条件查询不到数据");
            String fileName = "批次号" + batchNum + name + "债权审核_供理财" + ".xls";
            // 工作表名称
            String sheetName = "Export";
            // 创建工作簿
            Workbook workbook = ExcelExportUtil.createExcelByVo(fileName,labels, fields, list, sheetName);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response, fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,downLoadError);
            }
            return;
        } catch (PlatformException e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
    
    /**
     * 文件下载
     * @param request
     * @param response
     * @param fileName
     * @param workbook
     * @return
     */
    private String downLoadFile(HttpServletRequest request,HttpServletResponse response, String fileName, Workbook workbook) {
        OutputStream out = null;
         try {
//             response.reset();
             response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
             response.setContentType(CONTENT_TYPE);
             out = response.getOutputStream();
             workbook.write(out);
             out.flush();
         } catch (IOException e) {
             logger.error("下载文件失败：", e);
             return "下载文件失败";
         } 
         return null;
    }
    
    /**
     * 批量附件下载或者单点合同、信审下载
     * @param ary
     * @param ary_xs
     * @param ary_ht
     * @param request
     * @param response
     */
    /*@RequestMapping("/getLoanBatchImg")
    @ResponseBody
    public String downloadBatchLoanImg(@RequestParam("ary") String ary, @RequestParam("ary_xs") String ary_xs, @RequestParam("ary_ht") String ary_ht, @RequestParam("type") String type, @RequestParam("ary_zx") String ary_zx, HttpServletRequest request,HttpServletResponse response) {
    	AttachmentResponseInfo<Object> attachmentResponseInfo = null;	
        String batchNum="";
        String dateStr = new SimpleDateFormat("yyyyMMddhhmmssSS").format(new Date());
        
        try {
        	  //单点信审或者合同
	          if(Strings.isNotEmpty(type)){
	        	LoanBaseAppVo loanBaseAppVo=null;
	        	Map<String, Object> mapXs=new HashMap<String, Object>(1);	 
	        	  if("XS".equals(type)){     
	        		  this.createLog(request, SysActionLogTypeEnum.导出, "债权第三方导出", "债权第三方导出(单点信审)");
		        		  if(Strings.isEmpty(ary_xs)){
		        			  attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(),"该债权信息不存在,LoanId为空【信审下载】","");  
		        			  return toResponseJSON(attachmentResponseInfo);
		        		  }
		        		  mapXs.put("id", Long.valueOf(ary_xs));
		        		  loanBaseAppVo=loanBaseService.findAppNo(mapXs);
		        		   if(null==loanBaseAppVo || null==loanBaseAppVo.getAppNo()){
		        			   attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(),"该债权信息不存在或未找到附件信息【信审下载】","");
		        			   return toResponseJSON(attachmentResponseInfo);
		        		   }
		        		   // FTP服务器IP地址
			                 String host = sysParamDefineService.getSysParamValueCache("download", "sftp.download.host");
			                 // FTP服务器端口号
			                 String port = sysParamDefineService.getSysParamValueCache("download", "sftp.download.port");
			                 // FTP服务器登陆用户名
			                 String userName = sysParamDefineService.getSysParamValueCache("download", "sftp.download.userName");
			                 // FTP服务器I登陆密码
			                 String password = sysParamDefineService.getSysParamValueCache("download", "sftp.download.pwd");
			                 // FTP服务器下载文件路径
			                 String remoteFileName = "/aps/";//sysParamDefineService.getSysParamValueCache("download", "sftp.download.dir");
			                 batchNum=loanBaseAppVo.getBatchNum();
			                 List<DownLoadVideoDirVo> dirList = new ArrayList<DownLoadVideoDirVo>();
		        			FTPUtil ftp = new FTPUtil();
		                	String  remotePath = remoteFileName +  loanBaseAppVo.getAppNo() +  "/";//下载信审
			                ftp.connectServer(host, port, userName, password, remotePath); 
//		                    DownLoadVideoDirVo  downLoadVideoDirVoXs = new DownLoadVideoDirVo();
//		                    dirList.add(downLoadVideoDirVoXs);	                    
//		                    downLoadVideoDirVoXs.setFullPath( loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"信审");//下载信审
//		                    List<DownLoadVideoFileVo> fileListXs = new ArrayList<DownLoadVideoFileVo>();
//		                    downLoadVideoDirVoXs.setFileList(fileListXs);	                    
		                    // 获取服务器上的所有文件名称
		                    List<String>  fileListXsFile = ftp.getDicFileList(remotePath);
		                    for (String fileName : fileListXsFile) {
		                        if (fileName.indexOf('S')<0) {
		                        	String fileNameXS=fileNameZY(fileName);
		                        	DownLoadVideoDirVo  downLoadVideoDirVoXs = new DownLoadVideoDirVo();
		                        	downLoadVideoDirVoXs.setFullPath( loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"信审"+"/"+fileNameXS);//下载信审
		 		                    dirList.add(downLoadVideoDirVoXs);	                    
		 		                    List<DownLoadVideoFileVo> fileListXs = new ArrayList<DownLoadVideoFileVo>();
		 		                    downLoadVideoDirVoXs.setFileList(fileListXs);	
		                            List<String> fileNameList = ftp.getFileList(remotePath + fileName +  "/");
		                            for (String name : fileNameList) {
		                            	DownLoadVideoFileVo downLoadVideoFileVo = new DownLoadVideoFileVo();
		    	                    	downLoadVideoFileVo.setFileName(name);	    	                    	
		    	                    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
		    	                    	boolean downloadFlag = ftp.download(remotePath + fileName +  "/"+name, outputStream);
		    	                    	downLoadVideoFileVo.setOutputStream(outputStream);
		    	                    	fileListXs.add(downLoadVideoFileVo);	
		                            }
		                        }
		                    }
		                    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(2048);
			     			 ZipOutputStream zipOut = new ZipOutputStream(byteOutputStream);	                 
			                 for (DownLoadVideoDirVo dirVo : dirList) {
			                	 List<DownLoadVideoFileVo> fileList = dirVo.getFileList();
			                	 String fullPath = dirVo.getFullPath();
			                	 for (DownLoadVideoFileVo fileVo : fileList) {
			                		 String fileName = fileVo.getFileName();
			                		 ByteArrayOutputStream fileOutputStream = (ByteArrayOutputStream) fileVo.getOutputStream();
			 	     				*//** 设置文件名 **//*
			 	     				zipOut.putNextEntry(new ZipEntry(fullPath + "/" + fileName));
			 	     				fileOutputStream.writeTo(zipOut);
			 	     				zipOut.closeEntry();
			                	 }
			                 }
			                 zipOut.close();
			                OutputStream outputStream = response.getOutputStream();
			     			String downloadFileName = batchNum+ "_"+dateStr+".zip";
			     			downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");
			     			response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);

			     			byteOutputStream.writeTo(outputStream);
			     			outputStream.flush();
			     			outputStream.close();
//		        		   this.ftpDownloadFile(loanBaseAppVo,type,request,response);//去征审下载
		        		   return null;
	        		   
	        	  }else if("HT".equals(type)){
	        		  this.createLog(request, SysActionLogTypeEnum.导出, "债权第三方导出", "债权第三方导出(单点合同)");
		        		  if(Strings.isEmpty(ary_ht)){
		        			  attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(),"该债权信息不存在,LoanId为空【合同下载】","");  
		        			  return toResponseJSON(attachmentResponseInfo);
		        		  }	        		  
		        		  mapXs.put("id", Long.valueOf(ary_ht));
		        		  loanBaseAppVo=loanBaseService.findAppNo(mapXs);
		        		  if(null==loanBaseAppVo || null==loanBaseAppVo.getAppNo()){
		        			   attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(),"该债权信息不存在或未找到附件信息【合同下载】","");
		        			   return toResponseJSON(attachmentResponseInfo);
		        		  }
		        		  
		        		  // FTP服务器IP地址
		                 String host = sysParamDefineService.getSysParamValueCache("download", "sftp.download.host");
		                 // FTP服务器端口号
		                 String port = sysParamDefineService.getSysParamValueCache("download", "sftp.download.port");
		                 // FTP服务器登陆用户名
		                 String userName = sysParamDefineService.getSysParamValueCache("download", "sftp.download.userName");
		                 // FTP服务器I登陆密码
		                 String password = sysParamDefineService.getSysParamValueCache("download", "sftp.download.pwd");
		                 // FTP服务器下载文件路径
		                 String remoteFileName = "/aps/";//sysParamDefineService.getSysParamValueCache("download", "sftp.download.dir");
		                 batchNum=loanBaseAppVo.getBatchNum();
	                	FTPUtil ftp = new FTPUtil();
	                	String  remotePath = remoteFileName +  loanBaseAppVo.getAppNo() +  "/";//下载合同
		                ftp.connectServer(host, port, userName, password, remotePath);
		                List<DownLoadVideoDirVo> dirList = new ArrayList<DownLoadVideoDirVo>();
	                	List<String>  fileListHtFile = ftp.getDicFileList(remotePath);
	                    for (String fileName : fileListHtFile) {
	                        if (fileName.indexOf('S')>=0) {
	                        	String fileNameHt=fileNameZY(fileName);
	                        	DownLoadVideoDirVo  downLoadVideoDirVoHt = new DownLoadVideoDirVo();
	                        	downLoadVideoDirVoHt.setFullPath( loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"合同"+"/"+fileNameHt);//下载信审
	 		                    dirList.add(downLoadVideoDirVoHt);	                    
	 		                    List<DownLoadVideoFileVo> fileListHt = new ArrayList<DownLoadVideoFileVo>();
	 		                    downLoadVideoDirVoHt.setFileList(fileListHt);	
	                            List<String> fileNameList = ftp.getFileList(remotePath + fileName +  "/");
	                            for (String name : fileNameList) {
	                            	DownLoadVideoFileVo downLoadVideoFileVo = new DownLoadVideoFileVo();
	    	                    	downLoadVideoFileVo.setFileName(name);	    	                    	
	    	                    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
	    	                    	boolean downloadFlag = ftp.download(remotePath + fileName +  "/"+name, outputStream);
	    	                    	downLoadVideoFileVo.setOutputStream(outputStream);
	    	                    	fileListHt.add(downLoadVideoFileVo);	
	                            }
	                        }
	                    }
	                    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(2048);
		     			 ZipOutputStream zipOut = new ZipOutputStream(byteOutputStream);	                 
		                 for (DownLoadVideoDirVo dirVo : dirList) {
		                	 List<DownLoadVideoFileVo> fileLists = dirVo.getFileList();
		                	 String fullPath = dirVo.getFullPath();
		                	 for (DownLoadVideoFileVo fileVo : fileLists) {
		                		 String fileName = fileVo.getFileName();
		                		 ByteArrayOutputStream fileOutputStream = (ByteArrayOutputStream) fileVo.getOutputStream();
		 	     				*//** 设置文件名 **//*
		 	     				zipOut.putNextEntry(new ZipEntry(fullPath + "/" + fileName));
		 	     				fileOutputStream.writeTo(zipOut);
		 	     				zipOut.closeEntry();
		                	 }
		                 }
		                 zipOut.close();
		                OutputStream outputStream = response.getOutputStream();
		     			String downloadFileName = batchNum+ "_"+dateStr+".zip";
		     			downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");
		     			response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);

		     			byteOutputStream.writeTo(outputStream);
		     			outputStream.flush();
		     			outputStream.close();
//		        		  this.ftpDownloadFile(loanBaseAppVo,type,request,response);//去征审下载
		        		  return null;
	        	 }else if("ZX".equals(type)){
	        		 this.createLog(request, SysActionLogTypeEnum.导出, "债权第三方导出", "债权第三方导出(单点征信)");
	        		  if(Strings.isEmpty(ary_zx)){
	        			  attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(),"该债权信息不存在,LoanId为空【征信下载】","");  
	        			  return toResponseJSON(attachmentResponseInfo);
	        		  }	        		  
	        		  mapXs.put("id", Long.valueOf(ary_zx));
	        		  loanBaseAppVo=loanBaseService.findAppNo(mapXs);
	        		  if(null==loanBaseAppVo || Strings.isEmpty(loanBaseAppVo.getIdNum())){
	        			   attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(),"该债权信息不存在或未找到用户证件信息【征信下载】","");
	        			   return toResponseJSON(attachmentResponseInfo);
	        		  }
	        		  batchNum=loanBaseAppVo.getBatchNum();
	        		  StringBuffer outputStringBuffer = this.getZxHtmlOutputString(loanBaseAppVo);
	        		  String downLoadPath = loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"征信";
	        		  ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(2048);
	        		  ZipOutputStream zipOut = new ZipOutputStream(byteOutputStream);
	        		  *//** 设置文件名 **//*
	        		  zipOut.putNextEntry(new ZipEntry(downLoadPath + "/" + "征信报告.html"));
	        		  zipOut.write(outputStringBuffer.toString().getBytes("UTF-8"));
	        		  zipOut.closeEntry();
		              zipOut.close();
		              OutputStream outputStream = response.getOutputStream();
		              String downloadFileName = batchNum+ "_"+dateStr+".zip";
		              downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");
		              response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
		              byteOutputStream.writeTo(outputStream);
		              outputStream.flush();
		              outputStream.close();
	        		  return null; 
	        	 }
	        }else{//批量下载
	        	 this.createLog(request, SysActionLogTypeEnum.导出, "债权第三方导出", "债权第三方导出(批量下载)");
	        		List<Long> paramLoanIdCheck=new ArrayList<Long>();
	        		List<Long> paramLoanIdAll = new ArrayList<Long>();
	            	List<Long> paramLoanIdXs = new ArrayList<Long>();
	            	List<Long> paramLoanIdHt = new ArrayList<Long>();
	            	List<Long> paramLoanIdZX = new ArrayList<Long>();
	            	List<LoanBaseAppVo> loanBaseAppVoAll=new ArrayList<LoanBaseAppVo>();
	            	List<LoanBaseAppVo> loanBaseAppVoXs=new ArrayList<LoanBaseAppVo>();
	            	List<LoanBaseAppVo> loanBaseAppVoHt=new ArrayList<LoanBaseAppVo>();
	            	List<LoanBaseAppVo> loanBaseAppVoZX = new ArrayList<LoanBaseAppVo>();
	            	if(Strings.isEmpty(ary)&&Strings.isEmpty(ary_xs)&&Strings.isEmpty(ary_ht)&&Strings.isEmpty(ary_zx)){
	            		 attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(),"请选择合同或者信审、征信附件进行下载","");  
	        			 return toResponseJSON(attachmentResponseInfo);
	            	}
	            	
	            	//同条数据信审合同都下载
	                 for(String loanId:ary.split(",")){
	                	 if(Strings.isNotEmpty(loanId)){
	                		 paramLoanIdAll.add(Long.valueOf(loanId));
	                		 paramLoanIdCheck.add(Long.valueOf(loanId));
	                	 }
	                 }
	                 if(null!=paramLoanIdAll &&paramLoanIdAll.size()>0){
	                	 Map<String, Object> mapAll=new HashMap<String, Object>(1);	
		                 mapAll.put("loanIds", paramLoanIdAll);
		                 loanBaseAppVoAll=   loanBaseService.findAppNoList(mapAll);
	                 }	                
	                 //单个信审下载
	                 for(String loanId:ary_xs.split(",")){
	                	 if(Strings.isNotEmpty(loanId)){
	                		 paramLoanIdXs.add(Long.valueOf(loanId));
	                		 paramLoanIdCheck.add(Long.valueOf(loanId));
	                	 }
	                	 
		             }
	                 if(null!=paramLoanIdXs &&paramLoanIdXs.size()>0){
	                	 Map<String, Object> mapXs=new HashMap<String, Object>(1);	
		                 mapXs.put("loanIds", paramLoanIdXs);
		                 loanBaseAppVoXs=loanBaseService.findAppNoList(mapXs);
	                 }	                
	                 //单个合同下载
	                 for(String loanId:ary_ht.split(",")){
	                	 if(Strings.isNotEmpty(loanId)){
	                		 paramLoanIdHt.add(Long.valueOf(loanId));
	                		 paramLoanIdCheck.add(Long.valueOf(loanId));
	                	 }
	                	 
		             }	
	                 if(null!=paramLoanIdHt &&paramLoanIdHt.size()>0){
	                	 Map<String, Object> mapHt=new HashMap<String, Object>(1);	
		                 mapHt.put("loanIds", paramLoanIdHt);
		                 loanBaseAppVoHt=loanBaseService.findAppNoList(mapHt);	
	                 }
	                 //征信点击下载
	                 for(String loanId:ary_zx.split(",")){
	                	 if(Strings.isNotEmpty(loanId)){
	                		 paramLoanIdZX.add(Long.valueOf(loanId));
	                		 if(!paramLoanIdCheck.contains(Long.valueOf(loanId))){
	                			 paramLoanIdCheck.add(Long.valueOf(loanId)); 
	                		 }	                		 
	                	 }	                	 
		             }	
	                 if(null!=paramLoanIdZX &&paramLoanIdZX.size()>0){
	                	 Map<String, Object> mapZX=new HashMap<String, Object>(1);	
		                 mapZX.put("loanIds", paramLoanIdZX);
		                 loanBaseAppVoZX=loanBaseService.findAppNoList(mapZX);	
	                 }
	                 
	                 //批量下载最大件数限制
	                 if(paramLoanIdCheck.size()>5){
	                	 attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(),"批量下载最大只能下载5笔债权附件","");  
	        			 return toResponseJSON(attachmentResponseInfo); 
	                 }
	                 
	                 // 本地临时保存路径
	                 String tempSavePath = sysParamDefineService.getSysParamValueCache("download", "ftp.temp.dir");  
	                 // 目录不存在，则创建目录
	                 // 目录不存在，则创建目录
	                 File tempDir = new File(tempSavePath);
	                 if (!tempDir.exists() && !tempDir.isDirectory()) {
	                     tempDir.mkdirs();
	                 }
	                 // 目录不存在，则创建目录
	                 File zipfile = new File(tempSavePath + "zip");
	                 if (!zipfile.exists() && !zipfile.isDirectory()) {
	                     zipfile.mkdirs();
	                 }
	                 // 本地保存目录
	                 File file = null;
	                 File fileXs = null;
	                 File fileHt = null;
	                 // FTP服务器IP地址
	                 String host = sysParamDefineService.getSysParamValueCache("download", "sftp.download.host");
	                 // FTP服务器端口号
	                 String port = sysParamDefineService.getSysParamValueCache("download", "sftp.download.port");
	                 // FTP服务器登陆用户名
	                 String userName = sysParamDefineService.getSysParamValueCache("download", "sftp.download.userName");
	                 // FTP服务器I登陆密码
	                 String password = sysParamDefineService.getSysParamValueCache("download", "sftp.download.pwd");
	                 // FTP服务器下载文件路径
	                 String remoteFileName = "/aps/";//sysParamDefineService.getSysParamValueCache("download", "sftp.download.dir");
//	                 List<String> list=new ArrayList<String>();
	                 // 本地临时保存路径
	                 long A=System.currentTimeMillis();
	             	 List<DownLoadVideoDirVo> dirList = new ArrayList<DownLoadVideoDirVo>();
	                 for(LoanBaseAppVo loanBaseAppVo:loanBaseAppVoAll){//全选
	                	batchNum=loanBaseAppVo.getBatchNum();
	                	FTPUtil ftp = new FTPUtil();
	                	String  remotePath = remoteFileName +  loanBaseAppVo.getAppNo() +  "/";//下载合同
		                ftp.connectServer(host, port, userName, password, remotePath);                    
	                    // 获取服务器上的所有文件名称
	                    List<String>  fileListFile = ftp.getDicFileList(remotePath);
	                    //信审文件下载
	                    for (String fileName : fileListFile) {
	                        if (fileName.indexOf("S")<0) {
	                        	String fileNameXS=fileNameZY(fileName);
	                        	DownLoadVideoDirVo  downLoadVideoDirVoXs = new DownLoadVideoDirVo();
	    	                    dirList.add(downLoadVideoDirVoXs);	                    
	    	                    downLoadVideoDirVoXs.setFullPath( loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"信审"+"/"+fileNameXS);//下载信审
	    	                    List<DownLoadVideoFileVo> fileListXs = new ArrayList<DownLoadVideoFileVo>();
	    	                    downLoadVideoDirVoXs.setFileList(fileListXs);	      
	                            List<String> fileNameList = ftp.getFileList(remotePath + fileName +  "/");
	                            for (String name : fileNameList) {
	                            	DownLoadVideoFileVo downLoadVideoFileVo = new DownLoadVideoFileVo();
	    	                    	downLoadVideoFileVo.setFileName(name);	    	                    	
	    	                    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
	    	                    	boolean downloadFlag = ftp.download(remotePath + fileName +  "/"+name, outputStream);
	    	                    	downLoadVideoFileVo.setOutputStream(outputStream);
	    	                    	fileListXs.add(downLoadVideoFileVo);	
	                            }
	                        }
	                    }
	                    //合同相关文件下载
	                    for (String fileName : fileListFile) {
	                        if (fileName.indexOf("S")>=0) {
	                        	String fileNameHT=fileNameZY(fileName);
	                        	DownLoadVideoDirVo  downLoadVideoDirVoHt = new DownLoadVideoDirVo();
	    	                    dirList.add(downLoadVideoDirVoHt);	                    
	    	                    downLoadVideoDirVoHt.setFullPath( loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"合同"+"/"+fileNameHT);//下载信审
	    	                    List<DownLoadVideoFileVo> fileListHt = new ArrayList<DownLoadVideoFileVo>();
	    	                    downLoadVideoDirVoHt.setFileList(fileListHt);	      
	                            List<String> fileNameList = ftp.getFileList(remotePath + fileName +  "/");
	                            for (String name : fileNameList) {
	                            	DownLoadVideoFileVo downLoadVideoFileVo = new DownLoadVideoFileVo();
	    	                    	downLoadVideoFileVo.setFileName(name);	    	                    	
	    	                    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
	    	                    	boolean downloadFlag = ftp.download(remotePath + fileName +  "/"+name, outputStream);
	    	                    	downLoadVideoFileVo.setOutputStream(outputStream);
	    	                    	fileListHt.add(downLoadVideoFileVo);	
	                            }
	                        }
	                    }
	                 }
	                 for(LoanBaseAppVo loanBaseAppVo:loanBaseAppVoHt){//单合同下载
	                  	batchNum=loanBaseAppVo.getBatchNum();
	                	FTPUtil ftp = new FTPUtil();
	                	String  remotePath = remoteFileName +  loanBaseAppVo.getAppNo() +  "/";//下载合同
		                ftp.connectServer(host, port, userName, password, remotePath); 
	                    //合同相关文件下载
		                List<String>  fileListHtFile = ftp.getDicFileList(remotePath);
	                    for (String fileName : fileListHtFile) {
	                        if (fileName.indexOf("S")>=0) {
	                        	String fileNameHT=fileNameZY(fileName);
	                        	DownLoadVideoDirVo  downLoadVideoDirVoHt = new DownLoadVideoDirVo();
	    	                    dirList.add(downLoadVideoDirVoHt);	                    
	    	                    downLoadVideoDirVoHt.setFullPath( loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"合同"+"/"+fileNameHT);//下载信审
	    	                    List<DownLoadVideoFileVo> fileListHt = new ArrayList<DownLoadVideoFileVo>();
	    	                    downLoadVideoDirVoHt.setFileList(fileListHt);	      
	                            List<String> fileNameList = ftp.getFileList(remotePath + fileName +  "/");
	                            for (String name : fileNameList) {
	                            	DownLoadVideoFileVo downLoadVideoFileVo = new DownLoadVideoFileVo();
	    	                    	downLoadVideoFileVo.setFileName(name);	    	                    	
	    	                    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
	    	                    	boolean downloadFlag = ftp.download(remotePath + fileName +  "/"+name, outputStream);
	    	                    	downLoadVideoFileVo.setOutputStream(outputStream);
	    	                    	fileListHt.add(downLoadVideoFileVo);	
	                            }
	                        }
	                    }
	                 }
	                 for(LoanBaseAppVo loanBaseAppVo:loanBaseAppVoXs){//单信审下载
	                		FTPUtil ftp = new FTPUtil();
		                	String  remotePath = remoteFileName +  loanBaseAppVo.getAppNo() +  "/";//下载信审
			                ftp.connectServer(host, port, userName, password, remotePath); 
//		                    DownLoadVideoDirVo  downLoadVideoDirVoXs = new DownLoadVideoDirVo();
//		                    dirList.add(downLoadVideoDirVoXs);	                    
//		                    downLoadVideoDirVoXs.setFullPath( loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"信审");//下载信审
//		                    List<DownLoadVideoFileVo> fileListXs = new ArrayList<DownLoadVideoFileVo>();
//		                    downLoadVideoDirVoXs.setFileList(fileListXs);	                    
		                    // 获取服务器上的所有文件名称
		                    List<String>  fileListXsFile = ftp.getDicFileList(remotePath);
		                    for (String fileName : fileListXsFile) {
		                        if (fileName.indexOf("S")<0) {
		                        	String fileNameXS=fileNameZY(fileName);
		                        	DownLoadVideoDirVo  downLoadVideoDirVoXs = new DownLoadVideoDirVo();
		 		                    dirList.add(downLoadVideoDirVoXs);	                    
		 		                    downLoadVideoDirVoXs.setFullPath( loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"信审"+"/"+fileNameXS);//下载信审
		 		                    List<DownLoadVideoFileVo> fileListXs = new ArrayList<DownLoadVideoFileVo>();
		 		                    downLoadVideoDirVoXs.setFileList(fileListXs);	                    
		                            List<String> fileNameList = ftp.getFileList(remotePath + fileName +  "/");
		                            for (String name : fileNameList) {
		                            	DownLoadVideoFileVo downLoadVideoFileVo = new DownLoadVideoFileVo();
		    	                    	downLoadVideoFileVo.setFileName(name);	    	                    	
		    	                    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
		    	                    	boolean downloadFlag = ftp.download(remotePath + fileName +  "/"+name, outputStream);
		    	                    	downLoadVideoFileVo.setOutputStream(outputStream);
		    	                    	fileListXs.add(downLoadVideoFileVo);	
		                            }
		                        }
		                    }
	                 }
	                 for(LoanBaseAppVo loanBaseAppVo:loanBaseAppVoZX){//征信下载
	                	 DownLoadVideoDirVo downLoadVideoDirVo = new DownLoadVideoDirVo();
	                	 downLoadVideoDirVo.setFullPath(loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"征信");
		                 dirList.add(downLoadVideoDirVo);
		                 List<DownLoadVideoFileVo> fileListZX = new ArrayList<DownLoadVideoFileVo>();
		                 downLoadVideoDirVo.setFileList(fileListZX);
		                 DownLoadVideoFileVo fileZX = new DownLoadVideoFileVo();
		                 fileZX.setFileName("征信报告.html");
		        		 StringBuffer outputStringBuffer = this.getZxHtmlOutputString(loanBaseAppVo); 
		        		 OutputStream outputStream = new ByteArrayOutputStream();
		        		 outputStream.write(outputStringBuffer.toString().getBytes("UTF-8"));
		        		 fileZX.setOutputStream(outputStream);
		        		 fileListZX.add(fileZX);
	                 }
	                 ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(2048);
	     			 ZipOutputStream zipOut = new ZipOutputStream(byteOutputStream);	                 
	                 for (DownLoadVideoDirVo dirVo : dirList) {
	                	 List<DownLoadVideoFileVo> fileList = dirVo.getFileList();
	                	 String fullPath = dirVo.getFullPath();
	                	 for (DownLoadVideoFileVo fileVo : fileList) {
	                		 String fileName = fileVo.getFileName();
	                		 ByteArrayOutputStream fileOutputStream = (ByteArrayOutputStream) fileVo.getOutputStream();
	 	     				*//** 设置文件名 **//*
	 	     				zipOut.putNextEntry(new ZipEntry(fullPath + "/" + fileName));
	 	     				fileOutputStream.writeTo(zipOut);
	 	     				zipOut.closeEntry();
	                	 }
	                 }
	                zipOut.close();
	                OutputStream outputStream = response.getOutputStream();
	     			String downloadFileName = batchNum+ "_"+dateStr+".zip";
	     			downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");
	     			response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);

	     			byteOutputStream.writeTo(outputStream);
	     			outputStream.flush();
	     			outputStream.close();
	                 
	        }
        } catch (PlatformException e) {
        	attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(),e.getMessage(),"");
    		return toResponseJSON(attachmentResponseInfo);
        } catch (IOException e){
        	e.printStackTrace();
        	logger.error(e.getMessage());
        	attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG,"接口连接异常","");
            return toResponseJSON(attachmentResponseInfo);
        } catch (Exception e) {
        	e.printStackTrace();
        	logger.error(e.getMessage());
        	attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG,e.getMessage(),"");
            return toResponseJSON(attachmentResponseInfo);
        }
        attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG,"未找到相关附件信息","");
        return toResponseJSON(attachmentResponseInfo);
        
    }*/
    
    //获取征信报告
    private StringBuffer getZxHtmlOutputString(LoanBaseAppVo loanBaseAppVo) throws IOException {
    	Map<String, String> param = new HashMap<String, String>();
		  param.put("customerName", loanBaseAppVo.getName());
		  param.put("customerIdCard", loanBaseAppVo.getIdNum());
		  param.put("queryDate", Dates.getDateTime("yyyy-MM-dd"));
		  String zxUrl  = sysParamDefineService.getSysParamValueCache("download", "http.download.zxUrl");
		  return HttpUtils.URLPost(zxUrl, param); //获取征信接口返回流
	}

	/**
     * 查询债权导出信息
     * @param loanIds
     * @param imageNames
     * @return
     */
    private List<LoanImageFile> findLoanImageList(String[] loanIds,String[] imageNames){
        Map<String ,Object> params = new HashMap<String ,Object>();
        // 字符串数组转换为Long类型集合
        List<Long> paramLoanIds = new ArrayList<Long>();
        for(String loanId:loanIds){
            paramLoanIds.add(Long.valueOf(loanId));
        }
        params.put("loanIds", paramLoanIds);
        params.put("imageNames", imageNames);
        // 查询债权导出信息
        return loanBaseService.findLoanImageList(params);
    }
    
    /**
     * ftp下载征审系统附件信息
     * @param appNo
     * @param type
     * @param request
     * @param response
     * @throws IOException 
     */
    private void ftpDownloadFile(LoanBaseAppVo loanBaseAppVo, String type,HttpServletRequest request, HttpServletResponse response) throws IOException {
            // FTP服务器IP地址
            String host = sysParamDefineService.getSysParamValueCache("download", "sftp.download.host");
            // FTP服务器端口号
            String port = sysParamDefineService.getSysParamValueCache("download", "sftp.download.port");
            // FTP服务器登陆用户名
            String userName = sysParamDefineService.getSysParamValueCache("download", "sftp.download.userName");
            // FTP服务器I登陆密码
            String password = sysParamDefineService.getSysParamValueCache("download", "sftp.download.pwd");
            // FTP服务器下载文件路径
            String remoteFileName = "/aps/";//sysParamDefineService.getSysParamValueCache("download", "sftp.download.dir");
            // 本地临时保存路径
            String tempSavePath = sysParamDefineService.getSysParamValueCache("download", "ftp.temp.dir");           
            List<String> fileList = null;
            String dateStr = new SimpleDateFormat("yyyyMMddhhmmssSS").format(new Date());
            // 目录不存在，则创建目录
            File tempDir = new File(tempSavePath);
            if (!tempDir.exists() && !tempDir.isDirectory()) {
                tempDir.mkdirs();
            }
            // 目录不存在，则创建目录
            File zipfile = new File(tempSavePath + "zip");
            if (!zipfile.exists() && !zipfile.isDirectory()) {
                zipfile.mkdirs();
            }
            // Ftp工具类对象
            FTPUtil ftp = new FTPUtil();
            // 远程下载源路径
            String remotePath = null;
            // 本地保存目标路径
            String localPath = null;
            // 本地保存目录
            File file = null;            
            if ("HT".equals(type)) {// 合同下载 
                // 远程下载源路径
                remotePath = remoteFileName +  loanBaseAppVo.getAppNo() + "/" + "S" +  "/";
                // 本地保存目标路径
                localPath= tempSavePath+loanBaseAppVo.getBatchNum()+ "_"+dateStr+"/"+loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"合同"+"/";                
                // 创建本地保存目录
                file = new File(localPath);
                if (!file.exists()&& !file.isDirectory()) {
                    file.mkdirs();
                }
                // 连接远程Ftp服务器
                ftp.connectServer(host, port, userName, password, remotePath);
                // 获取服务器上的所有文件名称
                fileList = ftp.getFileList(remotePath);
                for (String fileName : fileList) {
                    ftp.download(remotePath + fileName,localPath + fileName);
                }
            } else if ("XS".equals(type)) {// 信审下载
                // 远程下载源路径
                remotePath =remoteFileName + loanBaseAppVo.getAppNo() + "/";
                // 本地保存目标路径
                localPath= tempSavePath+loanBaseAppVo.getBatchNum()+ "_"+dateStr+"/"+loanBaseAppVo.getName()+loanBaseAppVo.getIdNum()+"/"+"信审"+"/"; 
                // 创建本地保存目录
                file = new File(localPath);
                if (!file.exists() && !file.isDirectory()) {
                	file.mkdirs();
                }
                // 连接远程Ftp服务器
                ftp.connectServer(host, port, userName, password, remotePath);
                // 获取服务器上的所有文件名称
                fileList = ftp.getDicFileList(remotePath);
                for (String fileName : fileList) {
                    if (!"S".equalsIgnoreCase(fileName)) {
                        List<String> fileNameList = ftp.getFileList(remotePath + fileName +  "/");
                        for (String name : fileNameList) {
                            ftp.download(remotePath + fileName +  "/" + name, localPath + name);
                        }
                    }
                }
            }
            String zipFilePath = tempSavePath + "zip"+  "/" + loanBaseAppVo.getBatchNum() + "_"+dateStr+ ".zip";
            String zipTargetFilePath=tempSavePath+loanBaseAppVo.getBatchNum()+ "_"+dateStr;//压缩文件路径
            // 压缩文件
            FileDownUtils.zipFiles(zipTargetFilePath, zipFilePath);
            // 文件下载
            LoanExternalDebtUtil.downloadFile(tempSavePath + "zip" +  "/", loanBaseAppVo.getBatchNum()+ "_"+dateStr + ".zip", request, response);
            // 删除目录下的所有子目录及文件
            FileDownUtils.deleteDir(file);
    }
    
    
  
   

    
    /**
     * 上传黑名单
     * @param file
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/importListFile")
    public void importListFile(@RequestParam(value = "uploadfile") MultipartFile file,HttpServletRequest request, HttpServletResponse response) {
        Workbook workbookNew = null;
        ResponseInfo responseInfo = null;
        try {
            // 文件校验
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 10);
            UploadFileUtil.valid(uploadFile);
            // 创建excel工作表
            InputStream in = new BufferedInputStream(file.getInputStream());
            Workbook workbook = WorkbookFactory.create(in);
            // 创建导入数据模板
            ExcelTemplet excelTemplet = new ExcelTemplet().new blackNameInfoExcel();
            // 文件数据转换为List集合
            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
            Map<String ,Boolean> mapFlag =new HashMap<String ,Boolean>();
            List<Long> paramLoanIds = new ArrayList<Long>();
//            paramLoanIds.add((long)41658);//1
//            paramLoanIds.add((long)41216);//2
//            paramLoanIds.add((long)41876); //3
            mapFlag.put("mapFlag", true);
          //处理EXCEL 数据，数据又误则是{mapFlag=false}
            processOneLine(sheetDataList,mapFlag,paramLoanIds);
            boolean flag= mapFlag.get("mapFlag");
            if(flag){//EXCEL 数据正确，下载附件
                List<LoanImageFile> loanImageFileList = new ArrayList<LoanImageFile>(); 
                 Map<String ,Object> params = new HashMap<String ,Object>();
                 params.put("loanIds", paramLoanIds);
                loanImageFileList=loanBaseService.findLoanImageListBlackName(params); 
                String[] path = LoanExternalDebtUtil.zipImages(loanImageFileList);
                if(path.length==1){
               	 throw new PlatformException(ResponseEnum.FULL_MSG, path[0]);
                }
                // 下载压缩文件
                String downLoadError = LoanExternalDebtUtil.downloadFile(path[0], path[1], request,response);
                if(Strings.isNotEmpty(downLoadError)){
                    throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
                }
                return;                
            }else{   //EXCEL 数据不正确，直接返回EXCEL 结果      
                 String fileName = file.getOriginalFilename();
                 String[] labels = new String[] { "产品类型", "姓名", "身份证号", "签约日期", "", };	
                 String[] fields = new String[] { "loanType", "borrowerName", "idNum","signDate", "E" };
                 // 工作表名称
                 String sheetName = "Sheet1";
                 // 创建工作簿
                 workbookNew = ExcelExportUtil.createExcelByMapString(fileName, labels, fields, sheetDataList, sheetName);
                 // 文件下载
                 String downLoadError = this.downLoadFile(request, response,fileName, workbookNew);
                 // 文件下载失败
                 if (Strings.isNotEmpty(downLoadError)) {
                     throw new PlatformException(ResponseEnum.FULL_MSG,downLoadError);
                 }
                 return;
            }
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {        	
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
            if (null != workbookNew) {
                try {
                    workbookNew.close();
                } catch (Exception e) {
                    responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
                }
            }
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
    
    private void processOneLine(List<Map<String, String>> sheetDataList,Map<String ,Boolean> mapFlag,List<Long> paramLoanIds) {
        for(Map<String, String> map:sheetDataList){//每一行记录遍历
            String name = Strings.convertValue(map.get("borrowerName"), String.class);
            // 借款人身份证号码
            String idNum = Strings.convertValue(map.get("idNum"), String.class);
            // 借款类型
            String loanType = Strings.convertValue(map.get("loanType"),String.class);
            // 签约日期
            String signDate = Strings.convertValue(map.get("signDate"),String.class); 
            PersonInfo  personInfo= personTelInfoService.findPerson(map);
            if(personInfo==null){
                map.put("E", "客户不存在");                
                mapFlag.put("mapFlag", false);
                continue;
                
            }
            try {
                new SimpleDateFormat("yyyy-MM-dd").parse(signDate);
            } catch (Exception e) {
                map.put("E", "日期格式有误");
                mapFlag.put("mapFlag", false);    
                continue;
            }  
            Map<String, Object> loanParams = new HashMap<String, Object>();
            loanParams.put("idNum", idNum.trim());
            loanParams.put("name", name.trim());
            loanParams.put("loanType", loanType.trim());
            loanParams.put("signDate", signDate.trim());
            VLoanInfo   loanInfo = vLoanInfoService.getVLoanBlackInfo(loanParams);
            if(loanInfo==null){
                 map.put("E","无此借款");
                 mapFlag.put("mapFlag", false);
                 continue;
            }else{
                paramLoanIds.add(Long.valueOf(loanInfo.getId()));
                map.put("E","");                
                mapFlag.put("mapFlag", true);
                continue;
            }
        }
    }
    private String fileNameZY(String fileName) {
    	if (fileName.equals("A")) {
			fileName = "申请表";
		} else if (fileName.equals("B")) {
			fileName = "身份证明";
		} else if (fileName.equals("D")) {
			fileName = "工作收入流水";
		} else if (fileName.equals("C")) {
			fileName = "个人流水";
		} else if (fileName.equals("E")) {
			fileName = "公积金资料";
		} else if (fileName.equals("F")) {
			fileName = "房产信息";
		} else if (fileName.equals("G")) {
			fileName = "车产信息";
		} else if (fileName.equals("H")) {
			fileName = "保单信息";
		} else if (fileName.equals("N")) {
			fileName = "网购资料";
		} else if (fileName.equals("K")) {
			fileName = "私营业主资料";
		} else if (fileName.equals("L")) {
			fileName = "征信报告";
		} else if (fileName.equals("M")) {
			fileName = "其他";
		} else if (fileName.equals("S")) {
			fileName = "合同其他资料";
		} else if (fileName.equals("S1")) {
			fileName = "贷款合同";
		} else if (fileName.equals("S2")) {
			fileName = "温馨还款提示函";
		} else if (fileName.equals("S3")) {
			fileName = "委托扣款授权书";
		} else if (fileName.equals("S4")) {
			fileName = "身份证";
		} else if (fileName.equals("S5")) {
			fileName = "银行卡";
		} else if (fileName.equals("S6")) {
			fileName = "流水复核表";
		} else if (fileName.equals("S7")) {
			fileName = "通话详单";
		} else if (fileName.equals("S8")) {
			fileName = "咨询服务协议";
		} else if (fileName.equals("S1_PDF")) {
			fileName = "贷款合同PDF";
		} else if (fileName.equals("S8_PDF")) {
			fileName = "咨询服务协议PDF";
		} else if (fileName.equals("T")) {
			fileName = "爱特合同签约";
		}
		return fileName;
	}
    
    /**
     * 还款计划导出（供外贸信托）
     * @param repaymentDetail
     * @param request
     * @param response
     */
    @RequestMapping("/exportExtRepaySchedule")
    @ResponseBody
    public void exportExtRepaySchedule(LoanRepaymentDetailVo loanRepaymentDetail,@RequestParam("batchNum") String batchNum, HttpServletRequest request, HttpServletResponse response){
        ResponseInfo responseInfo = null; 
        try {            
            List<LoanRepaymentDetailVo> list=new ArrayList<LoanRepaymentDetailVo>();
            Map<String, Object> paramMap =new HashMap<>();
            paramMap.put("batchNum", batchNum);            
            list=vLoanInfoService.getWxmtLoanReymentExportList(paramMap);
            Assert.notCollectionsEmpty(list,"当前导出条件查询不到数据");
            //String fileBatchNum=requestManagementService.getRequestManagerFileToDayBatchNum(ReqManagerFileTypeEnum.还款计划导出txt.getFileType());
            String fileSeq = requestManagementService.findFileSeqByBatchNum(batchNum, ReqManagerFileTypeEnum.还款计划导出txt.getFileType());
            // 下载文件名称编辑
            String fileName = Strings.getFileSeq4Title(fileSeq, 2);
            fileName = this.getRepayScheduleFileNameByBatchNum(fileName, ReqManagerFileTypeEnum.还款计划导出txt);
            for(LoanRepaymentDetailVo loanRepay:list){
            	loanRepay.setOrgCode(RequestManagementConst.WMXT_ORG_CODE);
            	loanRepay.setReturnDateStr(Dates.getDateTime(loanRepay.getReturnDate(), "yyyy-MM-dd"));
            	loanRepay.setReturnBalance(loanRepay.getReturneterm().add(loanRepay.getCurrentAccrual().negate()));     	
            }
            InputStream is = null;
            OutputStream out = null;
            is = FileUtil.write(list, RequestManagementConst.getRetuPlanFields(), RequestManagementConst.SEPARATOR,"gbk");
            if(null == is){
                throw new PlatformException(ResponseEnum.FULL_MSG, "创建还款计划TXT文件失败！");
            }
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.setContentType("application/octet-stream; charset=gbk");
            out = response.getOutputStream();
            byte [] temp = new byte[1024];
            int len = 0;
            while((len = is.read(temp))>0){
            	out.write(temp,0,len);
            }
            logger.info(out.toString());
            out.flush();		
            requestManagementService.checkRequestManagerOperateRecord(batchNum,ReqManagerFileTypeEnum.还款计划导出txt.getFileType(), ReqManagerOperateTypeEnum.downLoad.getOperateType(),fileSeq);
            return;
        } catch (PlatformException e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
    
    /**
     * 债权导出txt文件（供理财）
     * @param batchNum
     * @param request
     * @param response
     */
    @RequestMapping("/exportTxtDebtInfo")
    @ResponseBody
    public void exportTxtDebtInfo(@RequestParam String batchNum, HttpServletRequest request, HttpServletResponse response){
    	ResponseInfo responseInfo = null;
        try {
            List<VLoanDebtFileInfo> list = new ArrayList<VLoanDebtFileInfo>();
            Map<String, Object> paramMap = new HashMap<>();
            User user = UserContext.getUser();
            paramMap.put("orgCode", user.getOrgCode());
            paramMap.put("batchNum", batchNum);
            paramMap.put("max", "2000");
            list = vLoanInfoService.getExportDebtInfo4WMXT(paramMap);
            Assert.notCollectionsEmpty(list, "当前导出条件查询不到数据");
            String fileSeq = requestManagementService.findFileSeqByBatchNum(batchNum, ReqManagerFileTypeEnum.债权导出供理财txt.getFileType());
            String fileName = Strings.getFileSeq4Title(fileSeq, 2);
            fileName = this.getRepayScheduleFileNameByBatchNum(fileName, ReqManagerFileTypeEnum.债权导出供理财txt);
            InputStream is = null;
            OutputStream out = null;
            logger.info("channelSource:"+list.get(0).getChannelSource());
            is = FileUtil.write(list, RequestManagementConst.getDebtInfoFields(), RequestManagementConst.SEPARATOR,"gbk");
            if(null == is){
                throw new PlatformException(ResponseEnum.FULL_MSG, "债权导出txt文件！");
            }
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.setContentType("application/octet-stream; charset=gbk");
            out = response.getOutputStream();
            byte [] temp = new byte[1024];
            int len = 0;
            while((len = is.read(temp))>0){
            	out.write(temp,0,len);
            }
            logger.info(out.toString());
            out.flush();
            requestManagementService.checkRequestManagerOperateRecord(batchNum,ReqManagerFileTypeEnum.债权导出供理财txt.getFileType(), ReqManagerOperateTypeEnum.downLoad.getOperateType(),fileSeq);
            return;
    	} catch (PlatformException e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e){
    		e.printStackTrace();
    		responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
    	}
    	try{
    		response.setContentType("text/html");
    		response.getWriter().write(toResponseJSON(responseInfo));
    	} catch (Exception e) {
    	}
    }
    
    //根据文件批次号获取 还款计划导出文件名
    private String getRepayScheduleFileNameByBatchNum(String fileBatchNum, ReqManagerFileTypeEnum reqFileEnum) {
    	 StringBuffer sb = new StringBuffer();
         sb.append(Dates.getDateTime("yyyyMMdd"));
         sb.append("_");
         sb.append(RequestManagementConst.WMXT_PROJECT_CODE);
         sb.append("_");
         sb.append(fileBatchNum);
         sb.append(reqFileEnum.getFileCode());
         sb.append(reqFileEnum.getFileExtension());
         return sb.toString();
	}

	//通过list下载文件
    private String downLoadFileByList(HttpServletRequest request, HttpServletResponse response, String fileName, List<LoanRepaymentDetailVo> list) {
        try {
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("text/plain");
            BufferedWriter out = new BufferedWriter(response.getWriter());
	        for(LoanRepaymentDetailVo rd:list){
	        	StringBuffer sb = new StringBuffer();
	        	sb.append("01603"); //机构代码
	        	sb.append("|+|");
	        	sb.append(rd.getContractNum()); //合同号
	        	sb.append("|+|");
	        	sb.append(rd.getCurrentTerm()); //期次
	        	sb.append("|+|");
	        	sb.append(Dates.getDateTime(rd.getReturnDate(), "yyyy-MM-dd")); //应还款日期
	        	sb.append("|+|");
	        	sb.append( rd.getReturneterm().add(rd.getCurrentAccrual().negate())); //应还本金
	        	sb.append("|+|");
	        	sb.append(rd.getCurrentAccrual());  //应还利息
	        	sb.append("|+|");
	        	sb.append(rd.getPrincipalBalance()); // 剩余本金
	        	out.write(sb.toString());
	        	out.newLine();
	        }
        	out.flush();
        } catch (IOException e) {
            logger.error("下载文件失败：", e);
            return "下载文件失败";
        } 
		return null;
	}
    
    
    /**
     * 批量附件下载或者单点合同、信审下载
     * @param ary
     * @param ary_xs
     * @param ary_ht
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/getLoanBatchImg")
    public String downloadBatchLoanImg(@RequestParam("ary") String ary, @RequestParam("ary_xs") String ary_xs, @RequestParam("ary_ht") String ary_ht, @RequestParam("type") String type,
                                       @RequestParam("ary_zx") String ary_zx, HttpServletRequest request, HttpServletResponse response) {
        AttachmentResponseInfo<Object> attachmentResponseInfo = null;
        try {
            if (Strings.isNotEmpty(type)) {
                if ("XS".equals(type)) {
                    createLog(request, SysActionLogTypeEnum.导出, "债权第三方导出", "债权第三方导出(单点信审)");
                    this.downloadBatchXS(request, response, ary_xs);
                }else if("HT".equals(type)) {
                    createLog(request, SysActionLogTypeEnum.导出, "债权第三方导出", "债权第三方导出(单点合同)");
                    this.downloadBatchHT(request,response,ary_ht);
                }else if("ZX".equals(type)) {
                    createLog(request, SysActionLogTypeEnum.导出, "债权第三方导出", "债权第三方导出(单点征信)");
                    this.downloadBatchZX(request,response,ary_zx);
                }
            }else {
                //批量下载
                createLog(request, SysActionLogTypeEnum.导出, "债权第三方导出", "债权第三方导出(批量下载)");
                this.downloadBatchAll(ary,ary_xs,ary_ht,type,ary_zx,request,response);
            }
        }catch (PlatformException e) {
            attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(),e.getMessage(),"");
            return toResponseJSON(attachmentResponseInfo);
        } catch (IOException e){
            e.printStackTrace();
            logger.error(e.getMessage());
            attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG,"接口连接异常","");
            return toResponseJSON(attachmentResponseInfo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG,e.getMessage(),"");
            return toResponseJSON(attachmentResponseInfo);
        }
        attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS);
        return toResponseJSON(attachmentResponseInfo);
    }

    /**
     * 下载信审
     * @param request
     * @param response
     * @param ary_xs
     * @throws Exception
     */
    public void downloadBatchXS(HttpServletRequest request, HttpServletResponse response, String ary_xs) throws Exception {
        Map<String, Object> mapXs = new HashMap<String, Object>(1);
        if (Strings.isEmpty(ary_xs)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "该债权信息不存在,LoanId为空【信审下载】");
        }
        mapXs.put("id", Long.valueOf(ary_xs));
        LoanBaseAppVo loanBaseAppVo = loanBaseService.findAppNo(mapXs);
        if (null == loanBaseAppVo || null == loanBaseAppVo.getAppNo()) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "该债权信息不存在或未找到附件信息【信审下载】");
        }
        FTPUtil ftp = connectBhxtFtpService.getApsFtpConnectFtpClient();
        // FTP服务器下载文件路径
        String remoteFileName = ConnectBhxtFtpServiceImpl.storeDir;
        String batchNum = loanBaseAppVo.getBatchNum();
        List<DownLoadVideoDirVo> dirList = new ArrayList<DownLoadVideoDirVo>();
        String remotePath = remoteFileName + loanBaseAppVo.getAppNo() + "/";//下载信审
        // 获取服务器上的所有文件名称
        List<String> fileListXsFile = ftp.getDicFileList(remotePath);
        this.setDownLoadXSdirList(dirList, fileListXsFile, remotePath, ftp, loanBaseAppVo);
        ByteArrayOutputStream byteArrayOutputStream = this.compressedFiles(dirList);
        String dateStr = new SimpleDateFormat("yyyyMMddhhmmssSS").format(new Date());
        String downloadFileName = batchNum + "_" + dateStr + ".zip";
        downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");
        this.downLoadCompressFile(request, response, downloadFileName, byteArrayOutputStream);
    }

    /**
     * 设置信审下载的路径集合
     * @param dirList
     * @param fileListXsFile
     * @param remotePath
     * @param ftp
     * @param loanBaseAppVo
     * @throws Exception
     */
    public void setDownLoadXSdirList(List<DownLoadVideoDirVo> dirList, List<String> fileListXsFile, String remotePath, FTPUtil ftp, LoanBaseAppVo loanBaseAppVo) throws Exception {
        for (String fileName : fileListXsFile) {
            if (fileName.indexOf('S') < 0) {
                String fileNameXS = fileNameZY(fileName);
                DownLoadVideoDirVo downLoadVideoDirVoXs = new DownLoadVideoDirVo();
                downLoadVideoDirVoXs.setFullPath(loanBaseAppVo.getName() + loanBaseAppVo.getIdNum() + "/" + "信审" + "/" + fileNameXS);//下载信审
                dirList.add(downLoadVideoDirVoXs);
                List<DownLoadVideoFileVo> fileListXs = new ArrayList<DownLoadVideoFileVo>();
                downLoadVideoDirVoXs.setFileList(fileListXs);
                List<String> fileNameList = ftp.getFileList(remotePath + fileName + "/");
                for (String name : fileNameList) {
                    DownLoadVideoFileVo downLoadVideoFileVo = new DownLoadVideoFileVo();
                    downLoadVideoFileVo.setFileName(name);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
                    ftp.download(remotePath + fileName + "/" + name, outputStream);
                    downLoadVideoFileVo.setOutputStream(outputStream);
                    fileListXs.add(downLoadVideoFileVo);
                }
            }
        }
    }

    /**
     * 下载合同
     * @param request
     * @param response
     * @param ary_ht
     * @throws Exception
     */
    public void downloadBatchHT(HttpServletRequest request, HttpServletResponse response, String ary_ht) throws Exception {
        Map<String, Object> mapXs = new HashMap<String, Object>(1);
        if (Strings.isEmpty(ary_ht)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "该债权信息不存在,LoanId为空【合同下载】");
        }
        mapXs.put("id", Long.valueOf(ary_ht));
        LoanBaseAppVo loanBaseAppVo = loanBaseService.findAppNo(mapXs);
        if (null == loanBaseAppVo || null == loanBaseAppVo.getAppNo()) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "该债权信息不存在或未找到附件信息【合同下载】");
        }
        // FTP服务器下载文件路径
        String remoteFileName = ConnectBhxtFtpServiceImpl.storeDir;
        String batchNum = loanBaseAppVo.getBatchNum();
        FTPUtil ftp = connectBhxtFtpService.getApsFtpConnectFtpClient();
        String remotePath = remoteFileName + loanBaseAppVo.getAppNo() + "/";//下载合同
        List<DownLoadVideoDirVo> dirList = new ArrayList<DownLoadVideoDirVo>();
        List<String> fileListHtFile = ftp.getDicFileList(remotePath);
        this.setDownLoadHTdirList(dirList, fileListHtFile, ftp, remotePath, loanBaseAppVo);
        ByteArrayOutputStream byteArrayOutputStream = this.compressedFiles(dirList);
        String dateStr = new SimpleDateFormat("yyyyMMddhhmmssSS").format(new Date());
        String downloadFileName = batchNum + "_" + dateStr + ".zip";
        this.downLoadCompressFile(request, response, downloadFileName, byteArrayOutputStream);
    }

    /**
     * 设置下载合同的路径集合
     * @param dirList
     * @param fileListHtFile
     * @param ftp
     * @param remotePath
     * @param loanBaseAppVo
     * @throws Exception
     */
    public void setDownLoadHTdirList(List<DownLoadVideoDirVo> dirList, List<String> fileListHtFile, FTPUtil ftp, String remotePath, LoanBaseAppVo loanBaseAppVo) throws Exception {
        for (String fileName : fileListHtFile) {
            if (fileName.indexOf('S') >= 0) {
                String fileNameHt = fileNameZY(fileName);
                DownLoadVideoDirVo downLoadVideoDirVoHt = new DownLoadVideoDirVo();
                downLoadVideoDirVoHt.setFullPath(loanBaseAppVo.getName() + loanBaseAppVo.getIdNum() + "/" + "合同" + "/" + fileNameHt);//下载信审
                dirList.add(downLoadVideoDirVoHt);
                List<DownLoadVideoFileVo> fileListHt = new ArrayList<DownLoadVideoFileVo>();
                downLoadVideoDirVoHt.setFileList(fileListHt);
                List<String> fileNameList = ftp.getFileList(remotePath + fileName + "/");
                for (String name : fileNameList) {
                    DownLoadVideoFileVo downLoadVideoFileVo = new DownLoadVideoFileVo();
                    downLoadVideoFileVo.setFileName(name);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
                    ftp.download(remotePath + fileName + "/" + name, outputStream);
                    downLoadVideoFileVo.setOutputStream(outputStream);
                    fileListHt.add(downLoadVideoFileVo);
                }
            }
        }
    }

    /**
     * 下载征信报告文件
     * @param request
     * @param response
     * @param ary_zx
     * @throws Exception
     */
    public void downloadBatchZX(HttpServletRequest request, HttpServletResponse response, String ary_zx) throws Exception {
        Map<String, Object> mapXs = new HashMap<String, Object>(1);
        if (Strings.isEmpty(ary_zx)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "该债权信息不存在,LoanId为空【征信下载】");
        }
        mapXs.put("id", Long.valueOf(ary_zx));
        LoanBaseAppVo loanBaseAppVo = loanBaseService.findAppNo(mapXs);
        if (null == loanBaseAppVo || Strings.isEmpty(loanBaseAppVo.getIdNum())) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "该债权信息不存在或未找到用户证件信息【征信下载】");
        }
        String batchNum = loanBaseAppVo.getBatchNum();
        StringBuffer outputStringBuffer = this.getZxHtmlOutputString(loanBaseAppVo);
        String downLoadPath = loanBaseAppVo.getName() + loanBaseAppVo.getIdNum() + "/" + "征信";
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(2048);
        ZipOutputStream zipOut = new ZipOutputStream(byteOutputStream);
        /** 设置文件名 **/
        zipOut.putNextEntry(new ZipEntry(downLoadPath + "/" + "征信报告.html"));
        zipOut.write(outputStringBuffer.toString().getBytes("UTF-8"));
        zipOut.closeEntry();
        zipOut.close();
        OutputStream outputStream = response.getOutputStream();
        String dateStr = new SimpleDateFormat("yyyyMMddhhmmssSS").format(new Date());
        String downloadFileName = batchNum + "_" + dateStr + ".zip";
        downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
        byteOutputStream.writeTo(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 批量下载所以文件
     * @param ary
     * @param ary_xs
     * @param ary_ht
     * @param type
     * @param ary_zx
     * @param request
     * @param response
     * @throws Exception
     */
    public void downloadBatchAll(String ary, String ary_xs, String ary_ht, String type, String ary_zx, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String batchNum = "";
        List<Long> paramLoanIdCheck = new ArrayList<Long>();
        List<Long> paramLoanIdAll = new ArrayList<Long>();
        List<Long> paramLoanIdXs = new ArrayList<Long>();
        List<Long> paramLoanIdHt = new ArrayList<Long>();
        List<Long> paramLoanIdZX = new ArrayList<Long>();
        List<LoanBaseAppVo> loanBaseAppVoAll = new ArrayList<LoanBaseAppVo>();
        List<LoanBaseAppVo> loanBaseAppVoXs = new ArrayList<LoanBaseAppVo>();
        List<LoanBaseAppVo> loanBaseAppVoHt = new ArrayList<LoanBaseAppVo>();
        List<LoanBaseAppVo> loanBaseAppVoZX = new ArrayList<LoanBaseAppVo>();
        if (Strings.isEmpty(ary) && Strings.isEmpty(ary_xs) && Strings.isEmpty(ary_ht) && Strings.isEmpty(ary_zx)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "请选择合同或者信审、征信附件进行下载");
        }

        //同条数据信审合同都下载
        for (String loanId : ary.split(",")) {
            if (Strings.isNotEmpty(loanId)) {
                paramLoanIdAll.add(Long.valueOf(loanId));
                paramLoanIdCheck.add(Long.valueOf(loanId));
            }
        }
        if (null != paramLoanIdAll && paramLoanIdAll.size() > 0) {
            Map<String, Object> mapAll = new HashMap<String, Object>(1);
            mapAll.put("loanIds", paramLoanIdAll);
            loanBaseAppVoAll = loanBaseService.findAppNoList(mapAll);
        }
        //单个信审下载
        for (String loanId : ary_xs.split(",")) {
            if (Strings.isNotEmpty(loanId)) {
                paramLoanIdXs.add(Long.valueOf(loanId));
                paramLoanIdCheck.add(Long.valueOf(loanId));
            }

        }
        if (null != paramLoanIdXs && paramLoanIdXs.size() > 0) {
            Map<String, Object> mapXs = new HashMap<String, Object>(1);
            mapXs.put("loanIds", paramLoanIdXs);
            loanBaseAppVoXs = loanBaseService.findAppNoList(mapXs);
        }
        //单个合同下载
        for (String loanId : ary_ht.split(",")) {
            if (Strings.isNotEmpty(loanId)) {
                paramLoanIdHt.add(Long.valueOf(loanId));
                paramLoanIdCheck.add(Long.valueOf(loanId));
            }

        }
        if (null != paramLoanIdHt && paramLoanIdHt.size() > 0) {
            Map<String, Object> mapHt = new HashMap<String, Object>(1);
            mapHt.put("loanIds", paramLoanIdHt);
            loanBaseAppVoHt = loanBaseService.findAppNoList(mapHt);
        }
        //征信点击下载
        for (String loanId : ary_zx.split(",")) {
            if (Strings.isNotEmpty(loanId)) {
                paramLoanIdZX.add(Long.valueOf(loanId));
                if (!paramLoanIdCheck.contains(Long.valueOf(loanId))) {
                    paramLoanIdCheck.add(Long.valueOf(loanId));
                }
            }
        }
        if (null != paramLoanIdZX && paramLoanIdZX.size() > 0) {
            Map<String, Object> mapZX = new HashMap<String, Object>(1);
            mapZX.put("loanIds", paramLoanIdZX);
            loanBaseAppVoZX = loanBaseService.findAppNoList(mapZX);
        }

        //批量下载最大件数限制
        if (paramLoanIdCheck.size() > 5) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "批量下载最大只能下载5笔债权附件");
        }

        // 本地临时保存路径
        String tempSavePath = sysParamDefineService.getSysParamValueCache("download", "ftp.temp.dir");
        // 目录不存在，则创建目录
        // 目录不存在，则创建目录
        File tempDir = new File(tempSavePath);
        if (!tempDir.exists() && !tempDir.isDirectory()) {
            tempDir.mkdirs();
        }
        // 目录不存在，则创建目录
        File zipfile = new File(tempSavePath + "zip");
        if (!zipfile.exists() && !zipfile.isDirectory()) {
            zipfile.mkdirs();
        }
        // 本地保存目录
        // FTP服务器下载文件路径
        String remoteFileName = ConnectBhxtFtpServiceImpl.storeDir;//sysParamDefineService.getSysParamValueCache("download", "sftp.download.dir");
//	                 List<String> list=new ArrayList<String>();
        // 本地临时保存路径
        List<DownLoadVideoDirVo> dirList = new ArrayList<DownLoadVideoDirVo>();
        for (LoanBaseAppVo loanBaseAppVo : loanBaseAppVoAll) {
            batchNum = loanBaseAppVo.getBatchNum();
            FTPUtil ftp = connectBhxtFtpService.getApsFtpConnectFtpClient();
            String remotePath = remoteFileName + loanBaseAppVo.getAppNo() + "/";//下载合同
            // 获取服务器上的所有文件名称
            List<String> fileListFile = ftp.getDicFileList(remotePath);
            //信审文件下载
            this.setDownLoadXSdirList(dirList, fileListFile, remotePath, ftp, loanBaseAppVo);
            //合同相关文件下载
            this.setDownLoadHTdirList(dirList, fileListFile, ftp, remotePath, loanBaseAppVo);
        }
        for (LoanBaseAppVo loanBaseAppVo : loanBaseAppVoHt) {//单合同下载
            batchNum = loanBaseAppVo.getBatchNum();
            FTPUtil ftp = connectBhxtFtpService.getApsFtpConnectFtpClient();
            String remotePath = remoteFileName + loanBaseAppVo.getAppNo() + "/";//下载合同
            //合同相关文件下载
            List<String> fileListHtFile = ftp.getDicFileList(remotePath);
            this.setDownLoadHTdirList(dirList, fileListHtFile, ftp, remotePath, loanBaseAppVo);
        }
        for (LoanBaseAppVo loanBaseAppVo : loanBaseAppVoXs) {//单信审下载
            FTPUtil ftp = connectBhxtFtpService.getApsFtpConnectFtpClient();
            String remotePath = remoteFileName + loanBaseAppVo.getAppNo() + "/";//下载信审
            // 获取服务器上的所有文件名称
            List<String> fileListXsFile = ftp.getDicFileList(remotePath);
            this.setDownLoadXSdirList(dirList, fileListXsFile, remotePath, ftp, loanBaseAppVo);
        }
        for (LoanBaseAppVo loanBaseAppVo : loanBaseAppVoZX) {//征信下载
            DownLoadVideoDirVo downLoadVideoDirVo = new DownLoadVideoDirVo();
            downLoadVideoDirVo.setFullPath(loanBaseAppVo.getName() + loanBaseAppVo.getIdNum() + "/" + "征信");
            dirList.add(downLoadVideoDirVo);
            List<DownLoadVideoFileVo> fileListZX = new ArrayList<DownLoadVideoFileVo>();
            downLoadVideoDirVo.setFileList(fileListZX);
            DownLoadVideoFileVo fileZX = new DownLoadVideoFileVo();
            fileZX.setFileName("征信报告.html");
            StringBuffer outputStringBuffer = this.getZxHtmlOutputString(loanBaseAppVo);
            OutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(outputStringBuffer.toString().getBytes("UTF-8"));
            fileZX.setOutputStream(outputStream);
            fileListZX.add(fileZX);
        }
        ByteArrayOutputStream byteOutputStream = this.compressedFiles(dirList);
        String dateStr = new SimpleDateFormat("yyyyMMddhhmmssSS").format(new Date());
        String downloadFileName = batchNum + "_" + dateStr + ".zip";
        this.downLoadCompressFile(request, response, downloadFileName, byteOutputStream);
    }

    /**
     * 压缩文件
     *
     * @param dirList
     * @return
     * @throws Exception
     */
    private ByteArrayOutputStream compressedFiles(List<DownLoadVideoDirVo> dirList) throws Exception {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(2048);
        ZipOutputStream zipOut = new ZipOutputStream(byteOutputStream);
        for (DownLoadVideoDirVo dirVo : dirList) {
            List<DownLoadVideoFileVo> fileList = dirVo.getFileList();
            String fullPath = dirVo.getFullPath();
            for (DownLoadVideoFileVo fileVo : fileList) {
                String fileName = fileVo.getFileName();
                ByteArrayOutputStream fileOutputStream = (ByteArrayOutputStream) fileVo.getOutputStream();
                /** 设置文件名 **/
                zipOut.putNextEntry(new ZipEntry(fullPath + "/" + fileName));
                fileOutputStream.writeTo(zipOut);
                zipOut.closeEntry();
            }
        }
        zipOut.close();
        return byteOutputStream;
    }

    /**
     * 下载打包压缩的文件
     * @param request
     * @param response
     * @param fileName
     * @param byteArrayOutputStream
     * @return
     */
    private String downLoadCompressFile(HttpServletRequest request, HttpServletResponse response, String fileName, ByteArrayOutputStream byteArrayOutputStream) {
        OutputStream out = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "GBK"));
            out = response.getOutputStream();
            byteArrayOutputStream.writeTo(out);
            out.flush();
        } catch (IOException e) {
            logger.error("下载文件失败：", e);
            return "下载文件失败";
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                logger.error("下载文件失败：", e);
                return "下载文件失败";
            }
        }
        return null;
    }
}
