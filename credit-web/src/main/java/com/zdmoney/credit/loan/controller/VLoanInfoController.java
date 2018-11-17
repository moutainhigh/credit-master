package com.zdmoney.credit.loan.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.tree.EasyUITree;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanFilesInfoService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.repay.service.pub.ISalesDeptRepayInfoService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.domain.Picture;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.IPictureService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Controller
@RequestMapping("/system/vloanInfo")
public class VLoanInfoController extends BaseController {
	
	private static final ThreadLocal<FTPUtil> ftpLocal = new ThreadLocal<FTPUtil>();

	@Autowired
	@Qualifier("personInfoServiceImpl")
	IPersonInfoService personInfoServiceImpl;
	@Autowired
	@Qualifier("salesDeptRepayInfoServiceImpl")
	private ISalesDeptRepayInfoService salesDeptRepayInfoService;
	@Autowired
	@Qualifier("comOrganizationServiceImpl")
	IComOrganizationService comOrganizationServiceImpl;
	@Autowired
	@Qualifier("loanFilesInfoService")
	ILoanFilesInfoService loanFilesInfoService;
	@Autowired
	@Qualifier("VLoanInfoServiceImpl")
	IVLoanInfoService vLoanInfoServiceImpl;
	@Autowired
	@Qualifier("pictureServiceImpl")
	IPictureService pictureServiceImpl;
    @Autowired
    ISysParamDefineService sysParamDefineServiceImpl;
    @Autowired
    ILoanRepaymentDetailService LoanRepaymentDetailService;
    
    /**
     * 加载借款查询页面信息
     * @param request
     * @param response
     * @return
     */
	@RequestMapping("/vloanInfoList")
	public ModelAndView vloanInfoList(HttpServletRequest request,HttpServletResponse response) {
		createLog(request, SysActionLogTypeEnum.查询, "借款查询", "借款查询列表");
		// 获取当前登录用户的信息
		User userInfo = UserContext.getUser();
		String empNum = userInfo.getOrgCode();
		Map<String, Object> params = new HashMap<String, Object>();
		/*params.put("empNum", empNum);
		// 查询当前用户所属机构下的所有营业网点信息
	    params.put("vLevel", "V103");
	    // 查询当前用户所属机构下的所有营业网点信息
	    List<Map<String, Object>> salesDeptInfoList = salesDeptRepayInfoService.getSalesDeptInfo(params);*/
		// 管理营业部
	    params.put("empNum", empNum);
	    params.put("vLevel", ComOrganizationEnum.Level.V104.name());
	    List<Map<String, Object>>  salesTeamInfoList = salesDeptRepayInfoService.getSalesDeptInfo(params);
		// 还款营业部
	    params.put("orgCode", userInfo.getOrgCode());
	    params.put("vLevel",     ComOrganizationEnum.Level.V104.name());
	    List<Map<String, Object>> hkTeamInfoList = comOrganizationServiceImpl.findOrganization(params);
		//Map<String, Object> serviceResult = loanFilesInfoService.listPage();
		
	    // 存储系统地址
	    String storeServerUrl = sysParamDefineServiceImpl.getSysParamValue("system.thirdparty", "system.store.server.url");
	    
		ModelAndView modelAndView = new ModelAndView("/vloanInfo/vloanInfoList");
		
		// 管理营业部
		modelAndView.addObject("salesTeamInfoList", salesTeamInfoList);
		// 还款营业部
		modelAndView.addObject("hkTeamInfoList", hkTeamInfoList);
		// 借款类型
		modelAndView.addObject("loanTypes", LoanTypeEnum.values());
		// 借款状态
		modelAndView.addObject("loanStates", LoanStateEnum.values());
		//cs系统URL
		modelAndView.addObject("csUrl", sysParamDefineServiceImpl.getSysParamValue("system.thirdparty", "system.credit.cs.url"));
		//新的图片系统
		modelAndView.addObject("picUrl", sysParamDefineServiceImpl.getSysParamValue("system.thirdparty", "system.credit.pic.url"));
		//征审系统 通话记录url
		modelAndView.addObject("contactRecordUrl",sysParamDefineServiceImpl.getSysParamValue("system.thirdparty", "system.zhengshen.contactRecordUrl"));
		// 存储系统地址
		modelAndView.addObject("storeServerUrl", storeServerUrl);
		// 登录人信息
		modelAndView.addObject("user", userInfo);
		return modelAndView;

	}

	/**
	 * 借款查询页面一览信息查询
	 * @param vLoanInfo
	 * @param rows
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "search")
	@ResponseBody
	public String search(VLoanInfo vLoanInfo, int rows, int page, HttpServletRequest request, HttpServletResponse response) {
		ResponseInfo responseInfo = null;
		try {
			createLog(request, SysActionLogTypeEnum.查询, "借款查询", "借款查询列表");
			/** 获取登陆者信息 **/
			Map<String, Object> paramMap = new HashMap<String, Object>();
			User user = UserContext.getUser();
			paramMap.put("orgCode", user.getOrgCode());
			paramMap.put("userCode", user.getUserCode());
			Pager pager = new Pager();
			pager.setPage(page);
			pager.setRows(rows);
			pager.setSidx("LOAN_ID");
			pager.setSort("ASC");
			paramMap.put("pager", pager);
			// 姓名
			paramMap.put("name", vLoanInfo.getPersonInfo().getName());
			// 身份证
			paramMap.put("idNum", vLoanInfo.getPersonInfo().getIdnum());
			// 联系电话 maphone
			paramMap.put("mphone", vLoanInfo.getPersonInfo().getMphone());
			// 借款状态
			paramMap.put("loanState", vLoanInfo.getLoanState());
			// 销售团队
			paramMap.put("salesTeamId", vLoanInfo.getSalesTeamId());
			if (null != vLoanInfo.getSalesmanId()) {
				// 客户经理
				paramMap.put("salesMan", vLoanInfo.getSalesmanId());
			}

			// 申请日期（起止）
			// 处理时间（起止）
			// 借款类型
			paramMap.put("loanType", vLoanInfo.getLoanType());
			// 房产证号
			paramMap.put("realEstateLicenseCode", vLoanInfo.getPersonInfo()
					.getRealEstateLicenseCode());
			// 管理营业部
			paramMap.put("salesDepartmentId", vLoanInfo.getSalesDepartmentId());
			// 机构名称
			// 放款营业部
			paramMap.put("signSalesDepId", vLoanInfo.getSignSalesDepId());
			// 联系人
			paramMap.put("personInfoId", vLoanInfo.getPersonInfo().getId());
			// 单位名称
			paramMap.put("company", vLoanInfo.getPersonInfo().getCompany());
			//合同编号
			paramMap.put("contractNum", vLoanInfo.getContractNum());
			//申请件号
			paramMap.put("appNo", vLoanInfo.getAppNo());
			pager = vLoanInfoServiceImpl.searchRepaymentLoanWithPg(paramMap);
			List list = pager.getResultList();

			List<Map> ss = new ArrayList<Map>();
			for (int i = 0; i < list.size(); i++) {
				VLoanInfo v = (VLoanInfo) list.get(i);
				Map<String,Object> map = new HashMap<String,Object>();
				// 客户
				ComEmployee crmMan = v.getCrmMan();
				if (crmMan != null) {
					// 客户经理
					map.put("salesManName", crmMan.getName());
				} else {
					map.put("salesManName", "");
				}
				// 客服经理
				ComEmployee salesMan = v.getSalesMan();
				if (salesMan != null) {
					// 客服经理
					map.put("personInfoName", salesMan.getName());
				} else {
					map.put("personInfoName", "");
				}
				// 借款人信息
				PersonInfo personInfo = v.getPersonInfo();
				if (personInfo != null) {
					// 借款人
					map.put("name", personInfo.getName());
					// 身份证号码
					map.put("idnum", personInfo.getIdnum());
					// 职业类型
					map.put("profession", personInfo.getProfession());
				} else {
					// 借款人
					map.put("name", "");
					// 身份证号码
					map.put("idnum", "");
					// 职业类型
					map.put("profession", "");
				}
				map.put("borrowerId", v.getBorrowerId());
				map.put("id", v.getId());
				// 借款类型
				map.put("loanType", v.getLoanType());
				// 借款用途
				map.put("purpose", v.getPurpose());
				// 合同金额
				map.put("pactMoney", v.getPactMoney());
				// 审批金额
				map.put("money", v.getMoney());
				// 借款期限
				map.put("time", v.getTime());
				// 状态
				map.put("loanState", v.getLoanState());
				// ftp下载图片用
				map.put("appNo", v.getAppNo());
				//map.put("appNo", "20150331170000001104");
				//合同编号
				map.put("contractNum", v.getContractNum());
				// 放款时间
				map.put("grantMoneyDate", v.getGrantMoneyDate());
				//还款等级  
				Map<String, Object> levelMap = new HashMap<String, Object>();
				levelMap.put("loanId",v.getId());
			    String repaymentLevel =  LoanRepaymentDetailService.findRepaymentLevel(levelMap);
			    map.put("repaymentLevel", repaymentLevel);
				map.put("userCode", user.getUserCode());
				ss.add(map);
			}
			pager.setResultList(ss);
			return toPGJSONWithCode(pager);
		} catch (Exception e) {
			/** 系统忙 **/
			logger.error(e,e);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(responseInfo);
	}

	@RequestMapping(value = "ftpDownload/{appNo}")
	@ResponseBody
	public String ftpDownload(@PathVariable String appNo,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {
			String host =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.host");
			String port =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.port");
			String userName =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.userName");
			String pwd =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.pwd");
			String remoteFileName = "/aps/";//sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.dir");
			String tempSavePath =sysParamDefineServiceImpl.getSysParamValueCache("download", "ftp.temp.dir");
			String contion =sysParamDefineServiceImpl.getSysParamValueCache("download", "ftp.temp.contion");
			String dateStr = new SimpleDateFormat("yyyyMMddhhmmssSS")
					.format(new Date());
			List<String> list = null;

			FTPUtil ftp = new FTPUtil();
			ftp.connectServer(host, port, userName, pwd, remoteFileName + appNo);
			list = ftp.getDicFileList(remoteFileName + appNo + "/");
			EasyUITree tree = new EasyUITree("0", "根目录");

			for (int i = 0; i < list.size(); i++) {
				String fileName = list.get(i);
				String fileNameId = fileName;
				fileName = fileNameZY(fileName);
				List<String> lists = ftp.getFileList(remoteFileName + appNo
						+ "/" + fileNameId + "/");
				EasyUITree et = new EasyUITree(fileNameId, fileName);
				tree.getChildren().add(et);
				File zf = new File(tempSavePath + appNo + "/" + fileNameId);
				if (!zf.exists() && !zf.isDirectory()) {
					zf.mkdir();
				}
				for (int j = 0; j < lists.size(); j++) {
					String f = lists.get(j);
					Picture picture = new Picture();
					picture.setAppNo(appNo);
					picture.setSaveNmae(f);
					picture.setContion(contion);
					picture.setSubclassSort(fileNameId);
					String imgName = pictureServiceImpl
							.findPictureFileName(picture);
					String id = appNo + "," + fileNameId + "," + f;
					EasyUITree t = new EasyUITree(id, imgName);
					et.getChildren().add(t);

				}

			}
			List a = new ArrayList<EasyUITree>();
			a.add(tree);
			this.createLog(request, SysActionLogTypeEnum.查询, "借款查询", "附件列表");
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc());
			attachmentResponseInfo.setAttachment(a);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return toResponseJSON(attachmentResponseInfo);
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
			fileName = "淘宝资料";
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
		} else if (fileName.equals("T")) {
			fileName = "爱特签约合同";
		} else if (fileName.equals("S1_PDF")) {
			fileName = "贷款合同PDF";
		} else if (fileName.equals("S8_PDF")) {
			fileName = "咨询服务协议PDF";
		}
		return fileName;
	}

	@RequestMapping("ftpDownloadDetile/{appNo}")
	public ModelAndView ftpDownloadDetile(@PathVariable String appNo,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/vloanInfo/ftpDownload");
		modelAndView.addObject("appNo", appNo);
		return modelAndView;

	}

	@RequestMapping(value = "download/{appNo}")
	@ResponseBody
	public void download(@PathVariable String appNo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String host =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.host");
			String port =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.port");
			String userName =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.userName");
			String pwd =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.pwd");
			String path = "/";
			String[] input = appNo.split(",");
			appNo = input[0];
			String parentName = input[1];
			String fileName = input[2];

			String remoteFileName = "/aps/"+appNo+"/"+parentName;
			
			FTPUtil ftpUtil = ftpLocal.get();
			if (ftpUtil == null) {
				ftpUtil = new FTPUtil();
				/** 连接FTP服务端 **/
				ftpUtil.connectServer(host, port, userName, pwd, path);
				ftpLocal.set(ftpUtil);
			} 
			
			ftpUtil.changeDirectory(remoteFileName);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
			ftpUtil.download(fileName, outputStream);
			response.setContentType("image/jpeg; charset=UTF-8");
			/** 得到向客户端输出二进制数据的对象 **/

			OutputStream outputStream1 = response.getOutputStream();
			outputStream.writeTo(response.getOutputStream());

			outputStream1.flush();
			outputStream1.close();

		} catch (Exception ex) {
			throw ex;
		} 
			ftpLocal.remove();
		}

	@RequestMapping(value = "downloadFile/{appNo}")
	@ResponseBody
	public void downloadFile(@PathVariable String appNo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String host =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.host");
			String port =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.port");
			String userName =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.userName");
			String pwd =sysParamDefineServiceImpl.getSysParamValueCache("download", "sftp.download.pwd");
			String path = "/";
			String[] input = appNo.split(",");
			appNo = input[0];
			String parentName = input[1];
			String fileName = input[2];
	
			String remoteFileName = "/aps/"+appNo+"/"+parentName;
			
			FTPUtil ftpUtil = ftpLocal.get();
			if (ftpUtil == null) {
				ftpUtil = new FTPUtil();
				/** 连接FTP服务端 **/
				ftpUtil.connectServer(host, port, userName, pwd, path);
				ftpLocal.set(ftpUtil);
			} 
			
			ftpUtil.changeDirectory(remoteFileName);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
			response.setHeader(
					"Content-disposition",
					"attachment;filename="
							+ URLEncoder.encode(fileName, "utf-8"));
			ftpUtil.download(fileName, outputStream);
			response.setContentType("image/jpeg; charset=UTF-8");
			/** 得到向客户端输出二进制数据的对象 **/
	
			OutputStream outputStream1 = response.getOutputStream();
			outputStream.writeTo(response.getOutputStream());
	
			outputStream1.flush();
			outputStream1.close();
	
		} catch (Exception ex) {
			throw ex;
		} 
		ftpLocal.remove();
	}

	/**
	 * 获取客户联系号码列表
	 * @param objectId
	 * @param rows
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "findContactInfo/{objectId}")
	@ResponseBody
	public String findContactInfo(@PathVariable Long objectId, int rows, int page, HttpServletRequest request, HttpServletResponse response) {
		ResponseInfo responseInfo = null;
		try {
			Map<String, Object> params = new HashMap<>();
			Pager pager = new Pager();
			pager.setPage(page);
			pager.setRows(rows);
			params.put("pager", pager);
			params.put("personId",objectId);

			return toPGJSONWithCode(pager);
		} catch (Exception e) {
			logger.error("借款查询-获取通话性情解析异常",e);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(responseInfo);
	}
}
