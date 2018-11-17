package com.zdmoney.credit.payment.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.grant.FinanceGrantEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.BeanUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.grant.vo.LoanBaseGrantVo;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;

/**
 * Created by ym10094 on 2016/11/8.
 */
@Controller
@RequestMapping("/grant/finance")
public class FinanceGrantController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(FinanceGrantController.class);

    @Autowired
    private IFinanceGrantService financeGrantServiceImpl;

    @RequestMapping("/financeGrantOnLine")
    public ModelAndView financeGrantOnLinePage(HttpServletRequest request,HttpServletResponse response){
        createLog(request, SysActionLogTypeEnum.查询,"财务放款（线上）","加载财务放款初始化页面");
        //String beginDate = Dates.getDateTime(new Date(),"yyyy-MM") + "-01";
        //String endDate = Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT);
        String [] fundsSources = new String[]{FundsSourcesTypeEnum.外贸2.getValue(),FundsSourcesTypeEnum.包商银行.getValue()
        		,FundsSourcesTypeEnum.外贸3.getValue(),FundsSourcesTypeEnum.陆金所.getValue(),FundsSourcesTypeEnum.渤海2.getValue(),FundsSourcesTypeEnum.华瑞渤海.getValue()};
        ModelAndView model = new ModelAndView();
        //model.addObject("beginDate",beginDate);
        //model.addObject("endDate",endDate);
        model.addObject("states",FinanceGrantEnum.values());
        model.addObject("fundsSources", fundsSources);
        return  model;
    }

    /**
     * 分页
     * @param loanBaseGrantVo
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/searchFinnaceGrantOnLine")
    public String searchFinnaceGrantOnLine(LoanBaseGrantVo loanBaseGrantVo, int rows, int page,HttpServletRequest request,HttpServletResponse response){
        Pager pager = new Pager();
        try {
            // bean对象转换成map对象
            Map<String,Object> paramMap = BeanUtils.toMap(loanBaseGrantVo);
            pager.setRows(rows);
            pager.setPage(page);
            paramMap.put("pager", pager);
            pager = financeGrantServiceImpl.queryFinanceGrantPage(paramMap);
        }catch (Exception e){
            logger.error("查询异常："+ e.getMessage());
           return JSONObject.toJSONString(new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.FULL_MSG.getCode(),"查询异常"));
        }
        return toPGJSONWithCode(pager);
    }

    /**
     * 导出财务放款（线上）
     * @param loanBaseGrantVo
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/exportFinanceGrantOnLineExl")
    public void exportFinanceGrantOnLineExl(LoanBaseGrantVo loanBaseGrantVo,HttpServletRequest request,HttpServletResponse response){
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "财务放款（线上）导出", "财务放款（线上）导出（XLS）文件");
            // bean对象转换成map对象
            Map<String,Object> paramMap = BeanUtils.toMap(loanBaseGrantVo);
            String fileName = "财务放款导出表.xls";
            Workbook workbook = financeGrantServiceImpl.getFinanceGrantWorkBook(paramMap, fileName);
            if (workbook == null) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "创建财务放款(线上)（XLS）文件失败！");
            }
            String downLoadError = this.downLoadFile(request, response, fileName, workbook);
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
        }catch (PlatformException pe){
            responseInfo = new ResponseInfo(pe.getResponseCode().getCode(),pe.getMessage());
        }catch (Exception e){
            logger.error("导出财务放款（线上）文件异常：",e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        try{
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        }catch (IOException e){

        }
    }

    /**
     * 申请外贸信托财务放款审核
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/financeGrantApply")
    public String financeGrantApply(HttpServletRequest request,HttpServletResponse response,@RequestParam("loanIds")String loanIds){
        log.info("loanIds:{}", JSONUtil.toJSON(loanIds));
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "财务放款（线上）申请放款", "财务放款（线上）调用数信申请接口发起放款申请");
            if (Strings.isEmpty(loanIds)) {
                responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(), "请求参数为空");
                return toResponseJSON(responseInfo);
            }
            String[] loanIdArray = loanIds.split(",");
            if (loanIdArray.length == 1) {
                Long loanId = Long.valueOf(loanIdArray[0]);
                if (financeGrantServiceImpl.isApplyFinanceGrant(loanId)){
                    financeGrantServiceImpl.requestFinanceGrantApply(loanId);
                    responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"操作成功");
                }else {
                    responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"借款已申请线上放款，不能重复申请线上放款。");
                }
                return toResponseJSON(responseInfo);
            }
            String batchRequestResult = financeGrantServiceImpl.batchRequestFinanceGrantApply(loanIdArray);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),batchRequestResult);
        }catch (PlatformException pe){
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),pe.getMessage());
            return  toResponseJSON(responseInfo);
        }catch (Exception e){
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),"请求异常");
        }
        return  toResponseJSON(responseInfo);
    }

    /**
     * 查询放款详情
     * @param loanId
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/searchFinaceGrantDetails")
    public String searchFinaceGrantDetails(String loanId, int rows, int page,HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> paramMap = new HashMap<>();
        Pager pager = new Pager();
        try {
            pager.setRows(rows);
            pager.setPage(page);
            pager.setSort("asc");
            pager.setSidx("grant_apply_date");
            paramMap.put("loanId", loanId);
            paramMap.put("pager", pager);
            pager = financeGrantServiceImpl.queryFinanceGrantDetailsPage(paramMap);
        }catch (Exception e){
            logger.error("查询异常："+ e.getMessage());
            return JSONObject.toJSONString(new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.FULL_MSG.getCode(),"查询异常"));
        }
        return toPGJSONWithCode(pager);
    }
    /**
     * 文件下载
     * @param request
     *
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
}
