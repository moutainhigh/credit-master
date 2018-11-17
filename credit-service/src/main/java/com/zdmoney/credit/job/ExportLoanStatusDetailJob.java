package com.zdmoney.credit.job;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.ctc.wstx.util.StringUtil;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class ExportLoanStatusDetailJob {
	
	private static final Logger logger = Logger.getLogger(ExportLoanStatusDetailJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private IJobFreeSqlDao dao;
	
	private static final String MANAGE_SALES_DEP_NAME = "manage_sales_dep_name";
	private static final String SALESMAN_CODE = "salseman_code";
	private static final String BORROWER_IDNUM = "borrower_idnum";
	private static final String PACT_MONEY = "pact_money";
	private static final String STARTRDATE = "startrdate";
	private static final String TIME = "time";
	private static final String LOAN_TYPE = "loan_type";
	private static final String PROMISE_RETURN_DATE = "promise_return_date";
	private static final String GRANT_MONEY_DATE = "grant_money_date";
	private static final String CRM_CODE = "crm_code";
	private static final String RESIDUAL_PACT_MONEY = "residual_pact_money";
	private static final String OVER_DAYS = "over_days";
	private static final String OVER_TERMS = "over_terms";
	private static final String ENDRDATE = "endrdate";
	private static final String SALESTEAM_MANAGER_CODE= "salesteam_manager_code";
	private static final String SALESTEAM_NAME= "salesteam_name";
	private static final String IS_OUTSOURCING = "is_outsourcing";
	private static final String APPLY_TYPE = "apply_type";
	private static final String APPLY_INPUT_FLAG = "apply_input_flag";
	private static final String REPAYMENTLEVEL = "repaymentlevel";
	private static final String CONTRACT_NUM="contract_num";
	private static final Integer LIMIT = 100000;
	private static final String LOAN_NUMBER = "loan_number";
	private static final String IS_TOP_UP = "is_top_up";
	
	@Autowired
	private BasicDataSource db;
	

	public void execute() {
		//全量放款状态
		ExportLoanStatusDetail("LoanStatusDetail.csv");
        //90天逾期放款明细
		ExportLoanStatusDetail("LoanStatusDetailTrimester.csv");
	}
	
	private Connection getConnection() throws Exception{
		Class.forName(db.getDriverClassName()).newInstance();
		Connection conn;
		try {
			conn = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		} catch (Exception e) {
			logger.error("getConnection:" + e);
			throw e;
		}
		return conn;
	}
	
	private void ExportLoanStatusDetail(String fileName) {
		String isExportLoanStatusDetail = sysParamDefineService.getSysParamValue("sysJob", "isExportLoanStatusDetail");
		if(!Const.isClosing.equals(isExportLoanStatusDetail)){
			logger.info("ExportLoanStatusDetailJob开始........");
			
			loanLogService.createLog("ExportLoanStatusDetailJob", "info", "ExportLoanStatusDetailJob开始........", "SYSTEM");

			//String fileName = "LoanStatusDetail.csv";
			Connection conn = null;
			String exPath = sysParamDefineService.getSysParamValue("export", "exportLoanStatusDetailPath");
			if(StringUtils.isEmpty(exPath)){
				logger.error("放款状态明细Job:没有对应的文件目录");
			}
		    String datePath = Dates.getDateTime(new Date(), "yyyyMMdd");
		    String realPath = exPath+datePath;
		    FileOutputStream out = null;
		    OutputStreamWriter osw = null;
		    BufferedWriter bw = null;
			try {
				Map<String, Object> params = new HashMap<String, Object>();
				Pager pager = new Pager();
				pager.setRows(LIMIT);
				params.put("pager", pager);
				int page = 1;
			    File outPath = new File(realPath);
			    if (!outPath.exists()) {
			    	outPath.mkdirs();
			    }
			    File file = new File(outPath, fileName);
				conn = getConnection();
				out = new FileOutputStream(file);
				osw = new OutputStreamWriter(out,"GBK");
				bw = new BufferedWriter(osw);
				
				bw.append("管理营业部").append(",").append("客户经理工号").append(",").append("证件号码").append(",").append("合同编号").append(",").append("合同金额")
				.append(",").append("首次还款日").append(",").append("还款期限").append(",").append("产品类型").append(",").append("还款日")
				.append(",").append("放款日期").append(",").append("客服工号").append(",").append("剩余本金").append(",").append("逾期天数")
				.append(",").append("逾期期数").append(",").append("还款截止日").append(",").append("销售团队").append(",").append("业务主任工号").append(",").append("是否委外")
				.append(",").append("申请类型").append(",").append("是否直通车进件").append(",").append("账户类别").append(",").append("客户贷款笔数").append(",").append("追加账户").append("\r");
		        
		        int i = 0;
				while (true) {
					String sql=null;
					if ("LoanStatusDetail.csv".equals(fileName)) {
						sql=dao.getSql("exportLoanStatusDetail", params);
					}else if ("LoanStatusDetailTrimester.csv".equals(fileName)) {
						sql=dao.getSql("exportLoanStatusDetailTrimester", params);
					}
					//String sql = dao.getSql("exportLoanStatusDetail", params);
					logger.info(sql);
					PreparedStatement stmt = conn.prepareStatement(
							sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					ResultSet rs = stmt.executeQuery();
					
					rs.last();
					if (rs.getRow() == 0) {
						break;
					} else {
						rs.beforeFirst();
					}
					try {
						int a = 0;
						while (rs.next()) {
							String s = "\t";
							bw.append(rs.getString(MANAGE_SALES_DEP_NAME)==null?"":rs.getString(MANAGE_SALES_DEP_NAME)).append(",");
							bw.append(s+(rs.getString(SALESMAN_CODE)==null?"":rs.getString(SALESMAN_CODE))).append(",");//客户经理工号
							bw.append(s+(rs.getString(BORROWER_IDNUM)==null?"":rs.getString(BORROWER_IDNUM))).append(",");//证件号码
							bw.append(s+(rs.getString(CONTRACT_NUM)==null?"":rs.getString(CONTRACT_NUM))).append(",");//合同编号
							bw.append(rs.getString(PACT_MONEY)==null?"":rs.getString(PACT_MONEY)).append(",");
							bw.append(s+(rs.getString(STARTRDATE)==null?"":rs.getString(STARTRDATE))).append(",");//首次还款日
							bw.append(rs.getString(TIME)==null?"":rs.getString(TIME)).append(",");
							bw.append(rs.getString(LOAN_TYPE)==null?"":rs.getString(LOAN_TYPE)).append(",");
							bw.append(rs.getString(PROMISE_RETURN_DATE)==null?"":rs.getString(PROMISE_RETURN_DATE)).append(",");
							bw.append(s+(rs.getString(GRANT_MONEY_DATE)==null?"":rs.getString(GRANT_MONEY_DATE))).append(",");//放款日期
							bw.append(s+(rs.getString(CRM_CODE)==null?"":rs.getString(CRM_CODE))).append(",");//客服工号
							bw.append(rs.getString(RESIDUAL_PACT_MONEY)==null?"":rs.getString(RESIDUAL_PACT_MONEY)).append(",");
							bw.append(rs.getString(OVER_DAYS)==null?"":rs.getString(OVER_DAYS)).append(",");
							bw.append(rs.getString(OVER_TERMS)==null?"":rs.getString(OVER_TERMS)).append(",");
							bw.append(s+(rs.getString(ENDRDATE)==null?"":rs.getString(ENDRDATE))).append(",");///还款截止日
							bw.append(s+(rs.getString(SALESTEAM_NAME)==null?"":rs.getString(SALESTEAM_NAME))).append(",");//销售团队
							bw.append(s+(rs.getString(SALESTEAM_MANAGER_CODE)==null?"":rs.getString(SALESTEAM_MANAGER_CODE))).append(",");//业务主任工号
							bw.append(rs.getString(IS_OUTSOURCING)==null?"":rs.getString(IS_OUTSOURCING)).append(",");
							bw.append(rs.getString(APPLY_TYPE)==null?"":rs.getString(APPLY_TYPE)).append(",");
							bw.append(rs.getString(APPLY_INPUT_FLAG)==null?"":rs.getString(APPLY_INPUT_FLAG)).append(",");
							bw.append(rs.getString(REPAYMENTLEVEL)==null?"":rs.getString(REPAYMENTLEVEL)).append(",");
							bw.append(rs.getString(LOAN_NUMBER)==null?"":rs.getString(LOAN_NUMBER)).append(",");
							bw.append(rs.getString(IS_TOP_UP)==null?"":rs.getString(IS_TOP_UP)).append("\r");
							i++;
						}

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						stmt.close();
						rs.close();
					}
					page += 1;
					pager.setPage(page);
				}  	        	
			} catch (Exception e) {
				logger.error(e.getMessage());
				int length = e.getMessage().length();
				loanLogService.createLog("ExportLoanStatusDetailJob", "error", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				if (bw != null) {
					try {
						bw.close();
						bw = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (osw != null) {
					try {
						osw.close();
						osw = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
						out = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				logger.info("ExportLoanStatusDetailJob结束........");
				loanLogService.createLog("ExportLoanStatusDetailJob", "info", "ExportLoanStatusDetailJob结束........", "SYSTEM");
			}

		}else{
			loanLogService.createLog("ExportLoanStatusDetailJob", "info", "定时开关isExportLoanStatusDetail关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isExportLoanStatusDetail关闭，此次不执行");
		}
	}
}
