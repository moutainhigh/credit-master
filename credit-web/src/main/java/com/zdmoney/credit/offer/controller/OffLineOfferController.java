package com.zdmoney.credit.offer.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.tpp.ReturnCodeEnum;
import com.zdmoney.credit.common.constant.tpp.TppPaySysNoEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.BeanUtils;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.common.vo.core.OfferParamsVo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.domain.OfferInfo;
import com.zdmoney.credit.offer.service.pub.IOffLineOfferService;
import com.zdmoney.credit.offer.service.pub.IOfferCreateService;
import com.zdmoney.credit.offer.vo.OfferReturnVo;
import com.zendaimoney.thirdpp.common.enums.ThirdType;

@Controller
@RequestMapping("/offLineOffer")
public class OffLineOfferController extends BaseController {
    
     /**
     * 导出线下还款报盘文件权限url
     */
    private static final String EXPORT_OFFER_URL = "/offLineOffer/offerList/exportOffer";
    
    /**
     * 线下还款报盘实时划扣权限url
     */
    private static final String REAL_TIME_DEDUCT_URL = "/offLineOffer/offerList/realTimeDeduct";
    
    /**
     * 线下还款报盘关闭权限url
     */
    private static final String CLOSE_OFFER_URL = "/offLineOffer/offerList/closeOffer";
    
    @Autowired
    private IOffLineOfferService offLineOfferService;
    
    @Autowired
    private IVLoanInfoService vLoanInfoService;
    
    @Autowired
    private IOfferCreateService offerCreateService;
    
    /**
     * 加载线下还款报盘页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offerList/load")
    public ModelAndView loadOfferListPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("offer/offLineOfferList");
        mav.addObject("offerDate", Dates.getDateTime("yyyy-MM-dd"));
        mav.addObject("states", new String[] { 
                OfferStateEnum.未报盘.getValue(), OfferStateEnum.已报盘.getValue(), 
                OfferStateEnum.已回盘.getValue(), OfferStateEnum.已关闭.getValue() });
        mav.addObject("paySysNos", ThirdType.values());
        mav.addObject("fundsSources", this.getFundsSources());
        User user = UserContext.getUser();
        // 是否有导出线下还款报盘文件权限
        boolean isCanExportOffer = user.ifAnyGranted(EXPORT_OFFER_URL);
        // 是否有实时划扣权限
        boolean isCanRealTimeDeduct = user.ifAnyGranted(REAL_TIME_DEDUCT_URL);
        // 是否有关闭报盘权限
        boolean isCanCloseOffer= user.ifAnyGranted(CLOSE_OFFER_URL);
        mav.addObject("isCanExportOffer", isCanExportOffer);
        mav.addObject("isCanRealTimeDeduct", isCanRealTimeDeduct);
        mav.addObject("isCanCloseOffer", isCanCloseOffer);
        return mav;
    }
    
    /**
     * 查询线下还款报盘信息
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offerList/search")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public String search(int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "线下还款报盘", "查询线下还款报盘信息");
        // 收集请求参数
        Map<String, Object> params = this.getRequestParamsForOfferList(request);
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        pager.setSidx("ID");
        pager.setSort("DESC");
        params.put("pager", pager);
        // 调用Service层查询数据信息 
        pager = offLineOfferService.searchOfferInfoWithPgByMap(params);
        List querytList = pager.getResultList();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isNotEmpty(querytList)) {
            for (int i = 0; i < querytList.size(); i++) {
                OfferInfo offerInfo = (OfferInfo) querytList.get(i);
                // 将bean转化成map
                Map<String,Object> resultMap = BeanUtils.toMap(offerInfo);
                // 债权id
                Long loanId = offerInfo.getLoanId();
                // 根据债权id，查询债权信息
                VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
                // 得到合同编号
                resultMap.put("contractNum", vLoanInfo.getContractNum());
                
                // 判断导出状态
                resultMap.put("exportState", "未导出");
                if(offLineOfferService.isExportOffer(offerInfo.getId())){
                    resultMap.put("exportState", "已导出");
                }
                /** 转换TPP划扣通道名称 **/
                try {
                    if(Strings.isEmpty(TppPaySysNoEnum.get(offerInfo.getPaySysNo()))){
						resultMap.put("paySysNo","");//TppPaySysNoEnum中没有此划扣通道
					}else{
						resultMap.put("paySysNo",TppPaySysNoEnum.get(offerInfo.getPaySysNo()).getValue());			
					}
                } catch (Exception ex) {
                    resultMap.put("paySysNo", "未知");
                    logger.warn(ex);
                }
                list.add(resultMap);
            }
        }
        pager.setResultList(list);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 导出线下还款报盘文件
     * @param request
     * @param response
     */
    @RequestMapping("/offerList/exportOffer")
    public void exportOffer(HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "线下还款报盘", "导出线下还款报盘数据");
            // 收集请求参数
            Map<String, Object> params = new HashMap<String, Object>();
            // 合同来源限制
            params.put("fundsSourcesList", this.getFundsSources());
            // 只导出未报盘的数据
            params.put("state", OfferStateEnum.已报盘.getValue());
            // 查询线下还款报盘信息
            List<Map<String,Object>> offerInfoList = offLineOfferService.queryOffLineOfferInfo(params);
            if(CollectionUtils.isEmpty(offerInfoList)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "导出记录不存在，可能原因有：1、没有已报盘的记录；2、没有设置银行卡行别行号；3、报盘文件已导出。");
            }
            // 保存报盘流水导出记录
            this.saveOfferExportRecord(offerInfoList);
            // 下载导出文件
            String fileName = "线下还款报盘导出文件_" + Dates.getDateTime(new Date(), "yyyyMMddHHmmss") + ".xls";
            String[] labels = new String[] { "合同号", "行别代码", "银行行号", "扣款帐号","扣款帐号户名", "扣款金额"};
            String[] fields = new String[] { "contractNum", "bankType", "bankNo","bankAcct", "name","offerAmount"};
            // 工作表名称
            String sheetName = "线下还款报盘信息";
            // 创建工作簿
            Workbook workbook = ExcelExportUtil.createExcelByMap(fileName, labels,fields, offerInfoList, sheetName);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("线下还款报盘导出异常：", e);
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
     * 线下还款实时划扣
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offerList/realTimeDeduct")
    @ResponseBody
    public String realTimeDeduct(HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            User user = UserContext.getUser();
            String orgCode = user.getOrgCode();
            // 客户姓名
            String name = request.getParameter("name");
            // 合同编号
            String contractNum = request.getParameter("contractNum");
            // 划扣金额
            String offerAmount = request.getParameter("offerAmount");
            // 校验债权人和身份证信息是否与数据库匹配
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("name", name);
            params.put("contractNum", contractNum);
            params.put("code", orgCode);
            // 查询客户对应的债权信息
            List<VLoanInfo> loanInfoList = vLoanInfoService.searchVLoanInfoList(params);
            if (CollectionUtils.isEmpty(loanInfoList)) {
                responseInfo = new ResponseInfo(ResponseEnum.REALTIMEDEDUCTPRE_FAILD_ERROR.getCode(), "输入用户名和合同号不匹配！");
                return toResponseJSON(responseInfo);
            }
            VLoanInfo loanInfo = loanInfoList.get(0);
            if(!this.isOffLineLoan(loanInfo.getFundsSources())){
                throw new PlatformException(ResponseEnum.FULL_MSG, "该笔借款不能线下还款实时划扣！");
            }
            OfferParamsVo paramsVo = new OfferParamsVo();
            paramsVo.setUserCode(user.getUserCode());
            paramsVo.setLoanId(loanInfo.getId());
            paramsVo.setOfferAmount(new BigDecimal(offerAmount));
            // 实时扣款处理
            Map<String, Object> json = offerCreateService.createRealtimeOfferInfo(paramsVo);
            // 扣款成功与否返回码
            String resCode = (String) json.get("code");
            String message = (String) json.get("message");
            responseInfo = new ResponseInfo(ResponseEnum.REALTIMEDEDUCTPRE_SUCCESS, ResponseEnum.REALTIMEDEDUCTPRE_SUCCESS.getDesc());
            if (!ResponseEnum.SYS_SUCCESS.getCode().equals(resCode)) {
                responseInfo = new ResponseInfo(resCode, message);
            }
        }catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("线下还款报盘实时划扣异常:" + e.getMessage());
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } 
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 线下还款报盘（关闭报盘）
     * @param offerId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offerList/closeOffer")
    @ResponseBody
    public String closeOffer(@RequestParam Long offerId, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            // 关闭线下还款报盘
            offLineOfferService.closeOffer(offerId);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("线下还款报盘关闭报盘异常:" + e.getMessage());
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 加载线下还款回盘页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offerReturn/load")
    public ModelAndView loadOfferReturnPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("offer/offLineOfferReturnList");
        mav.addObject("offerDate", Dates.getDateTime("yyyy-MM-dd"));
        mav.addObject("paySysNos", ThirdType.values());
        mav.addObject("fundsSources", this.getFundsSources());
        mav.addObject("returnCodes", ReturnCodeEnum.values());
        return mav;
    }
    
    /**
     * 查询线下还款回盘信息
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offerReturn/search")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public String searchOfferReturnInfo(int rows, int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "线下还款回盘", "查询线下还款回盘信息");
        // 收集参数信息
        Map<String, Object> params = this.getRequestParamsForOfferReturn(request);
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        pager.setSidx("T1.REQ_TIME,T1.ID");
        pager.setSort("ASC");
        params.put("pager", pager);
        // 查询线下还款回盘信息
        pager = offLineOfferService.searchOfferTransactionInfoWithPgByMap(params);
        List resultList = pager.getResultList();
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (int i = 0; i < resultList.size(); i++) {
                OfferReturnVo offerReturnVo = (OfferReturnVo) resultList.get(i);
                /** 转换TPP划扣通道名称 **/
                try {
                    if (offerReturnVo.getPaySysNo()==null) {
						offerReturnVo.setPaySysNo("");
					}else if(Strings.isEmpty(TppPaySysNoEnum.get(offerReturnVo.getPaySysNo()))){
						offerReturnVo.setPaySysNo("");//TppPaySysNoEnum中没有此划扣通道
					}else{
						offerReturnVo.setPaySysNo(TppPaySysNoEnum.get(offerReturnVo.getPaySysNo()).getValue());					
					}
                } catch (Exception ex) {
                    offerReturnVo.setPaySysNo("未知");
                    logger.warn(ex);
                }
                
                /** 转换TPP划扣状态名称 **/
                try {
                    if (Strings.isEmpty(offerReturnVo.getRtnCode())) {
                        offerReturnVo.setRtnCode(offerReturnVo.getTrxState());
                    } else {
                        ReturnCodeEnum returnCodeEnum = ReturnCodeEnum.get(offerReturnVo.getRtnCode());
                        offerReturnVo.setRtnCode(returnCodeEnum.getDesc());
                    }
                } catch (Exception ex) {
                    offerReturnVo.setRtnCode("未知");
                    logger.warn(ex);
                }
            }
        }
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 导入回盘（线下还款回盘）
     * @param file
     * @param request
     * @param response
     */
    @RequestMapping("/offerReturn/importReturnOffer")
    @ResponseBody
    public void importReturnOffer(@RequestParam(value = "returnOfferFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        ResponseInfo responseInfo = null;
        // 导入文件名
        String fileName = file.getOriginalFilename();
        try {
            createLog(request, SysActionLogTypeEnum.导入, "线下还款回盘", "批量导入线下还款回盘信息");
            // 文件校验
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 10);
            UploadFileUtil.valid(uploadFile);
            // 创建excel工作簿
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            // 创建导入数据模板
            ExcelTemplet excelTemplet = new ExcelTemplet().new OffLineReturnOfferInputExcel();
            // 文件数据转换为List集合
            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
            if(CollectionUtils.isEmpty(sheetDataList)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"导入文件不能为空！");
            }
            // 线下还款回盘导入处理
            offLineOfferService.importReturnOffer(sheetDataList);
            // 在excel文件的每行末尾追加单元格提示信息
            ExcelUtil.addResultToWorkBook(workbook, sheetDataList, excelTemplet);
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType(file.getContentType());
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("线下还款回盘导入异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }finally{
            if(null!=outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
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
    
    /**
     * 导出回盘（线下还款回盘）
     * @param request
     * @param response
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/offerReturn/exportReturnOffer")
    public void exportReturnOffer(HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "线下还款回盘", "导出线下还款回盘信息");
            int maxRecord = 50000;
            // 收集参数信息
            Map<String, Object> params = this.getRequestParamsForOfferReturn(request);
            Pager pager = new Pager();
            pager.setRows(maxRecord);
            pager.setPage(1);
            pager.setSidx("T1.REQ_TIME,T1.ID");
            pager.setSort("ASC");
            params.put("pager", pager);
            // 查询线下还款回盘信息
            pager = offLineOfferService.searchOfferTransactionInfoWithPgByMap(params);
            List resultList = pager.getResultList();
            Assert.notCollectionsEmpty(resultList, ResponseEnum.FULL_MSG, "导出条件查询不到回盘数据！");
            for (int i = 0; i < resultList.size(); i++) {
                OfferReturnVo offerReturnVo = (OfferReturnVo) resultList.get(i);
                /** 转换TPP划扣通道名称 **/
                try {
                	if (offerReturnVo.getPaySysNo()==null) {
						offerReturnVo.setPaySysNo("");
					}else if(Strings.isEmpty(TppPaySysNoEnum.get(offerReturnVo.getPaySysNo()))){
						offerReturnVo.setPaySysNo("");//TppPaySysNoEnum中没有此划扣通道
					}else{
						offerReturnVo.setPaySysNo(TppPaySysNoEnum.get(offerReturnVo.getPaySysNo()).getValue());					
					}
                } catch (Exception ex) {
                    offerReturnVo.setPaySysNo("未知");
                    logger.warn(ex);
                }
                
                /** 转换TPP划扣状态名称 **/
                try {
                    if (Strings.isEmpty(offerReturnVo.getRtnCode())) {
                        offerReturnVo.setRtnCode(offerReturnVo.getTrxState());
                    } else {
                        ReturnCodeEnum returnCodeEnum = ReturnCodeEnum.get(offerReturnVo.getRtnCode());
                        offerReturnVo.setRtnCode(returnCodeEnum.getDesc());
                    }
                } catch (Exception ex) {
                    offerReturnVo.setRtnCode("未知");
                    logger.warn(ex);
                }
            }
            // 下载导出文件
            String fileName = "线下还款回盘导出文件_" + Dates.getDateTime(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            List<String> labels = this.getOfferReturnLabels();
            List<String> fields = this.getOfferReturnFields();
            // 工作表名称
            String sheetName = "线下还款回盘信息";
            // 创建工作簿
            Workbook workbook = ExcelExportUtil.createExcelByVo(fileName, labels,fields, resultList, sheetName);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("线下还款回盘导出异常：", e);
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
     * 加载行别行号配置页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offLineConfig/load")
    public ModelAndView loadOffLineConfig(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("offer/offLineConfig");
        mav.addObject("offerDate", Dates.getDateTime("yyyy-MM-dd"));
        mav.addObject("fundsSources", this.getFundsSources());
        return mav;
    }
    
    /**
     * 行别行号配置页面数据展示
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offLineConfig/search")
    @ResponseBody
    public String searchOffLineLoanInfo(int rows, int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "行号配置", "查询行号配置信息");
        // 收集参数信息
        Map<String, Object> params = this.getRequestParamsForOfferConfig(request);
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        pager.setSidx("T1.ID");
        pager.setSort("DESC");
        params.put("pager", pager);
        // 分页查询线下还款债权相关信息
        pager = offLineOfferService.queryOffLineLoanInfo(params);
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 行别、行号设置
     * @param bankIds
     * @param regionType
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offLineConfig/setRegionType")
    @ResponseBody
    public String setRegionType(@RequestParam String bankIds, @RequestParam String regionType, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            // 债权银行账户ID
            String[] bankIdArr = bankIds.split(",");
            if(null == bankIdArr || bankIdArr.length == 0){
                throw new PlatformException(ResponseEnum.FULL_MSG,"请至少选中一条记录更新！");
            }
            // 行别、行号设置
            offLineOfferService.setRegionType(bankIdArr, regionType);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), "行别、行号设置异常：" + e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 保存报盘流水导出记录
     * @param offerInfoList
     */
    private void saveOfferExportRecord(List<Map<String, Object>> offerInfoList) {
        for(Map<String, Object> offerInfo : offerInfoList){
            // 报盘流水Id
            Long transId = Strings.convertValue(offerInfo.get("id"), Long.class);
            offLineOfferService.saveOfferExportRecord(transId);
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
             response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
             response.setContentType(UploadFileUtil.FILE_TYPE_EXCEL[0]);
             out = response.getOutputStream();
             workbook.write(out);
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

    /**
     * 收集请求参数（线下还款报盘）
     * @param request
     * @return
     */
    private Map<String, Object> getRequestParamsForOfferList(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        // 客户姓名
        String name = request.getParameter("name");
        if (Strings.isNotEmpty(name)) {
            params.put("name", name);
        }
        // 报盘日期，如果没指定，则默认设置为系统日期
        params.put("offerDate", Dates.getDateTime(new Date(), "yyyy-MM-dd"));
        String offerDate = request.getParameter("offerDate");
        if (Strings.isNotEmpty(offerDate)) {
            params.put("offerDate", offerDate);
        }
        // 证件号码
        String idNum = request.getParameter("idNum");
        if (Strings.isNotEmpty(idNum)) {
            params.put("idNum", idNum);
        }
        // 报盘状态
        String state = request.getParameter("state");
        if (Strings.isNotEmpty(state)) {
            params.put("state", state);
        }
        // 合同编号
        String contractNum = request.getParameter("contractNum");
        if (Strings.isNotEmpty(contractNum)) {
            params.put("contractNum", contractNum);
        }
        // 划扣通道
        String paySysNo = request.getParameter("paySysNo");
        if (Strings.isNotEmpty(paySysNo)) {
            params.put("paySysNo", paySysNo);
        }
        // 开户银行
        String bankCode = Strings.defaultValue(request.getParameter("bankCode"), "0", "");
        if (Strings.isNotEmpty(bankCode)) {
            params.put("bankCode", bankCode);
        }
        // 合同来源
        String fundsSource = Strings.parseString(request.getParameter("fundsSource"));
        if (Strings.isNotEmpty(fundsSource)) {
            params.put("fundsSource", fundsSource);
        }
        // 机构区域码
        //params.put("orgCode", user.getOrgCode());
        // 合同来源限制
        params.put("fundsSourcesList", this.getFundsSources());
        return params;
    }
    
    /**
     * 收集参数信息（线下还款回盘）
     * @param request
     * @param user
     * @return
     */
    private Map<String, Object> getRequestParamsForOfferReturn(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        // 姓名
        String name = request.getParameter("name");
        if (Strings.isNotEmpty(name)) {
            params.put("name", name);
        }
        // 身份证号
        String idNum = request.getParameter("idNum");
        if (Strings.isNotEmpty(idNum)) {
            params.put("idNum", idNum);
        }
        // 回盘日期（开始日期）
        String startOfferDate = request.getParameter("startOfferDate");
        if (Strings.isNotEmpty(startOfferDate)) {
            params.put("startOfferDate", startOfferDate);
        }
        // 回盘日期（截止日期）
        String endOfferDate = request.getParameter("endOfferDate");
        if (Strings.isNotEmpty(endOfferDate)) {
            params.put("endOfferDate", endOfferDate);
        }
        // 报盘状态
        String state = request.getParameter("state");
        if (Strings.isNotEmpty(state)) {
            params.put("state", state);
        }
        // 合同来源
        String fundsSource = request.getParameter("fundsSource");
        if (Strings.isNotEmpty(fundsSource)) {
            params.put("fundsSource", fundsSource);
        }
        // 合同编号
        String contractNum = request.getParameter("contractNum");
        if (Strings.isNotEmpty(contractNum)) {
            params.put("contractNum", contractNum);
        }
        // 划扣通道
        String paySysNo = request.getParameter("paySysNo");
        if (Strings.isNotEmpty(paySysNo)) {
            params.put("paySysNo", paySysNo);
        }
        // 是否划扣成功
        String returnCode = request.getParameter("returnCode");
        if (Strings.isNotEmpty(returnCode)) {
            params.put("returnCode", returnCode);
        }
        // 开户银行
        String bankCode = Strings.defaultValue(request.getParameter("bankCode"), "0", "");
        if (Strings.isNotEmpty(bankCode)) {
            params.put("bankCode", bankCode);
        }
        // 债权去向
        String loanBelong = Strings.parseString(request.getParameter("loanBelong"));
        if (Strings.isNotEmpty(loanBelong)) {
            params.put("loanBelong", loanBelong);
        }
        // 机构区域码
        //params.put("orgCode", user.getOrgCode());
        // 合同来源限制
        params.put("fundsSourcesList", this.getFundsSources());
        return params;
    }
    
    
    /**
     * 收集参数信息（行号配置）
     * @param request
     * @param user
     * @return
     */
    private Map<String, Object> getRequestParamsForOfferConfig(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        // 姓名
        String name = request.getParameter("name");
        if (Strings.isNotEmpty(name)) {
            params.put("name", name);
        }
        // 身份证号
        String idNum = request.getParameter("idNum");
        if (Strings.isNotEmpty(idNum)) {
            params.put("idNum", idNum);
        }
        // 合同来源
        String fundsSource = request.getParameter("fundsSource");
        if (Strings.isNotEmpty(fundsSource)) {
            params.put("fundsSource", fundsSource);
        }
        // 合同编号
        String contractNum = request.getParameter("contractNum");
        if (Strings.isNotEmpty(contractNum)) {
            params.put("contractNum", contractNum);
        }
        // 银行卡所属地区
        String regionType = request.getParameter("regionType");
        if (Strings.isNotEmpty(regionType)) {
            params.put("regionType", regionType);
        }
        // 开户银行
        String bankCode = Strings.defaultValue(request.getParameter("bankCode"), "0", "");
        if (Strings.isNotEmpty(bankCode)) {
            params.put("bankCode", bankCode);
        }
        // 合同来源限制
        params.put("fundsSourcesList", this.getFundsSources());
        return params;
    }
    
    /**
     * excel文件的表头显示名
     * @return
     */
    private List<String> getOfferReturnLabels() {
        List<String> labels = new ArrayList<String>();
        labels.add("合同编号");
        labels.add("合同来源");
        labels.add("借款人");
        labels.add("身份证号");
        labels.add("银行");
        labels.add("账号");
        labels.add("报盘日期");
        labels.add("报盘金额");
        labels.add("回盘金额");
        labels.add("回盘日期");
        labels.add("划扣方式");
        labels.add("划扣通道");
        labels.add("划扣状态");
        labels.add("债权去向");
        labels.add("备注");
        return labels;
    }

    /**
     * excel文件的数据字段名
     * @return
     */
    private List<String> getOfferReturnFields() {
        List<String> fields = new ArrayList<String>();
        fields.add("contractNum");
        fields.add("fundsSources");
        fields.add("name");
        fields.add("idNum");
        fields.add("bankName");
        fields.add("bankAcct");
        fields.add("reqTime");
        fields.add("payAmount");
        fields.add("actualAmount");
        fields.add("rspReceiveTime");
        fields.add("type");
        fields.add("paySysNo");
        fields.add("rtnCode");
        fields.add("loanBelong");
        fields.add("memo");
        return fields;
    }
    
    /**
     * 设置合同来源（用于加载页面下拉框）
     * @return
     */
    private List<String> getFundsSources(){
        List<String> fundsSourcesList = new ArrayList<String>();
        fundsSourcesList.add(FundsSourcesTypeEnum.外贸信托.getValue());
        return fundsSourcesList;
    }
    
    /**
     * 判断是否是线下还款的债权
     * @param fundsSource
     * @return
     */
    private boolean isOffLineLoan(String fundsSource){
        if(this.getFundsSources().contains(fundsSource)){
            return true;
        }
        return false;
    }
}
