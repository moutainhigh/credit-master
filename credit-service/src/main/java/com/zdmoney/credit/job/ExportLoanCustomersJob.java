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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class ExportLoanCustomersJob {
	
	private static final Logger logger = Logger.getLogger(ExportLoanCustomersJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private IJobFreeSqlDao dao;
	
	private static final String MANAGEMENT = "management";
	private static final String COUNTSUM = "countsum";
	private static final String PACTSUM = "pactsum";
	private static final String C1_COUNT = "c1_count";
	private static final String C1_SUM = "c1_sum";
	private static final String C16_COUNT = "c16_count";
	private static final String C16_SUM = "c16_sum";
	private static final String M1_1COUNT = "m1_1count";
	private static final String M1_1SUM = "m1_1sum";
	private static final String M1_16COUNT = "m1_16count";
	private static final String M1_16SUM = "m1_16sum";
	private static final String M2_1COUNT = "m2_1count";
	private static final String M2_1SUM = "m2_1sum";
	private static final String M2_16COUNT = "m2_16count";
	private static final String M2_16SUM = "m2_16sum";
	private static final String M3_1COUNT = "m3_1count";
	private static final String M3_1SUM = "m3_1sum";
	private static final String M3_16COUNT = "m3_16count";
	private static final String M3_16SUM = "m3_16sum";
	private static final String M4_COUNT = "m4_count";
	private static final String M4_SUM = "m4_sum";
	private static final String M5_COUNT = "m5_count";
	private static final String M5_SUM = "m5_sum";
	private static final String M6_COUNT = "m6_count";
	private static final String M6_SUM = "m6_sum";
	private static final String M7_COUNT = "m7_count";
	private static final String M7_SUM = "m7_sum";
	private static final String OVERDUE_COUNT = "overdue_count";
	private static final String OVERDUE_SUM = "overdue_sum";
	private static final String ADD_COUNT = "add_count";
	private static final String ADD_SUM = "add_sum";
	private static final String IMPROVE1_COUNT = "improve1_count";
	private static final String IMPROVE1_SUM = "improve1_sum";
	private static final String IMPROVE16_COUNT = "improve16_count";
	private static final String IMPROVE16_SUM = "improve16_sum";
	private static final String PUSHBACK1_COUNT = "pushback1_count";
	private static final String PUSHBACK1_SUM = "pushback1_sum";
	private static final String PUSHBACK16_COUNT = "pushback16_count";
	private static final String PUSHBACK16_SUM = "pushback16_sum";
	private static final String M1_SETTLE1_COUNT= "M1_settle1_count";
	private static final String M1_SETTLE1_SUM = "M1_settle1_sum";
	private static final String M1_SETTLE16_COUNT = "M1_settle16_count";
	private static final String M1_SETTLE16_SUM = "M1_settle16_sum";
	private static final String M2_SETTLE1_COUNT = "M2_settle1_count";
	private static final String M2_SETTLE1_SUM = "M2_settle1_sum";
	private static final String M2_SETTLE16_COUNT = "M2_settle16_count";
	private static final String M2_SETTLE16_SUM = "M2_settle16_sum";
	
	private static final Integer LIMIT = 100000;
	
	@Autowired
	private BasicDataSource db;
	public void execute() {
		//放款客户质量追踪表
		ExportLoanStatusDetail("LoanCustomers.csv");
	    //直通车放款客户质量追踪表
		ExportLoanStatusDetail("TrainLoanCustomers.csv");	
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
		String isExportLoanCustomers = sysParamDefineService.getSysParamValue("sysJob", "isExportLoanStatusDetail");
		if(!Const.isClosing.equals(isExportLoanCustomers)){
			logger.info("ExportLoanCustomersJob开始........");
			
			loanLogService.createLog("ExportLoanCustomersJob", "info", "ExportLoanCustomersJob开始........", "SYSTEM");

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
				
				bw.append("管理营业部").append(",").append("累计放款账户数").append(",").append("累计放款合同额").append(",").append("1号端C#")
				.append(",").append("1号端C$").append(",").append("16号端C#").append(",").append("16号端C$").append(",").append("1号端M1#")
				.append(",").append("1号端M1$").append(",").append("16号端M1#").append(",").append("16号端M1$").append(",").append("1号端M2#")
				.append(",").append("1号端M2$").append(",").append("16号端M2#").append(",").append("16号端M2$").append(",").append("1号端M3#")
				.append(",").append("1号端M3$").append(",").append("16号端M3#").append(",").append("16号端M3$").append(",").append("M4#")
				.append(",").append("M4$").append(",").append("M5#").append(",").append("M5$").append(",").append("M6#").append(",").append("M6$")
				.append(",").append("M7+#").append(",").append("M7+$").append(",").append("首逾#").append(",").append("首逾$").append(",").append("新增#")
				.append(",").append("新增$").append(",").append("1号端好转#").append(",").append("1号端好转$").append(",").append("16号端好转#").append(",").append("16号端好转$")
				.append(",").append("1号端催回#").append(",").append("1号端催回$").append(",").append("16号端催回#").append(",").append("16号端催回$").append(",").append("1号端M1结清#")
				.append(",").append("1号端M1结清$").append(",").append("16号端M1结清#").append(",").append("16号端M1结清$").append(",").append("1号端M2+结清#").append(",").append("1号端M2+结清$")
				.append(",").append("16号端M2+结清#").append(",").append("16号端M2+结清$").append("\r");

				while (true) {
					String sql=null;
					if ("LoanCustomers.csv".equals(fileName)) {
						sql=dao.getSql("exportLoanCustomers", params);
					}else if ("TrainLoanCustomers.csv".equals(fileName)) {
						sql=dao.getSql("exportTrainLoanCustomers", params);
					}
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
						while (rs.next()) {
							int i = 0;
							bw.append(rs.getString(MANAGEMENT)==null?"":rs.getString(MANAGEMENT)).append(",");//管理营业部
							bw.append(rs.getString(COUNTSUM)==null?"":rs.getString(COUNTSUM)).append(",");//累计放款账户数
							bw.append(rs.getString(PACTSUM)==null?"":rs.getString(PACTSUM)).append(",");//累计放款合同额
							bw.append(rs.getString(C1_COUNT)==null?"":rs.getString(C1_COUNT)).append(",");
							bw.append(rs.getString(C1_SUM)==null?"":rs.getString(C1_SUM)).append(",");
							bw.append(rs.getString(C16_COUNT)==null?"":rs.getString(C16_COUNT)).append(",");
							bw.append(rs.getString(C16_SUM)==null?"":rs.getString(C16_SUM)).append(",");
							bw.append(rs.getString(M1_1COUNT)==null?"":rs.getString(M1_1COUNT)).append(",");
							bw.append(rs.getString(M1_1SUM)==null?"":rs.getString(M1_1SUM)).append(",");
							bw.append(rs.getString(M1_16COUNT)==null?"":rs.getString(M1_16COUNT)).append(",");
							bw.append(rs.getString(M1_16SUM)==null?"":rs.getString(M1_16SUM)).append(",");
							bw.append(rs.getString(M2_1COUNT)==null?"":rs.getString(M2_1COUNT)).append(",");
							bw.append(rs.getString(M2_1SUM)==null?"":rs.getString(M2_1SUM)).append(",");
							bw.append(rs.getString(M2_16COUNT)==null?"":rs.getString(M2_16COUNT)).append(",");
							bw.append(rs.getString(M2_16SUM)==null?"":rs.getString(M2_16SUM)).append(",");
							bw.append(rs.getString(M3_1COUNT)==null?"":rs.getString(M3_1COUNT)).append(",");
							bw.append(rs.getString(M3_1SUM)==null?"":rs.getString(M3_1SUM)).append(",");
							bw.append(rs.getString(M3_16COUNT)==null?"":rs.getString(M3_16COUNT)).append(",");
							bw.append(rs.getString(M3_16SUM)==null?"":rs.getString(M3_16SUM)).append(",");
							bw.append(rs.getString(M4_COUNT)==null?"":rs.getString(M4_COUNT)).append(",");
							bw.append(rs.getString(M4_SUM)==null?"":rs.getString(M4_SUM)).append(",");
							bw.append(rs.getString(M5_COUNT)==null?"":rs.getString(M5_COUNT)).append(",");
							bw.append(rs.getString(M5_SUM)==null?"":rs.getString(M5_SUM)).append(",");
							bw.append(rs.getString(M6_COUNT)==null?"":rs.getString(M6_COUNT)).append(",");
							bw.append(rs.getString(M6_SUM)==null?"":rs.getString(M6_SUM)).append(",");
							bw.append(rs.getString(M7_COUNT)==null?"":rs.getString(M7_COUNT)).append(",");
							bw.append(rs.getString(M7_SUM)==null?"":rs.getString(M7_SUM)).append(",");
							bw.append(rs.getString(OVERDUE_COUNT)==null?"":rs.getString(OVERDUE_COUNT)).append(",");
							bw.append(rs.getString(OVERDUE_SUM)==null?"":rs.getString(OVERDUE_SUM)).append(",");
							bw.append(rs.getString(ADD_COUNT)==null?"":rs.getString(ADD_COUNT)).append(",");
							bw.append(rs.getString(ADD_SUM)==null?"":rs.getString(ADD_SUM)).append(",");
							bw.append(rs.getString(IMPROVE1_COUNT)==null?"":rs.getString(IMPROVE1_COUNT)).append(",");
							bw.append(rs.getString(IMPROVE1_SUM)==null?"":rs.getString(IMPROVE1_SUM)).append(",");
							bw.append(rs.getString(IMPROVE16_COUNT)==null?"":rs.getString(IMPROVE16_COUNT)).append(",");
							bw.append(rs.getString(IMPROVE16_SUM)==null?"":rs.getString(IMPROVE16_SUM)).append(",");
							bw.append(rs.getString(PUSHBACK1_COUNT)==null?"":rs.getString(PUSHBACK1_COUNT)).append(",");
							bw.append(rs.getString(PUSHBACK1_SUM)==null?"":rs.getString(PUSHBACK1_SUM)).append(",");
							bw.append(rs.getString(PUSHBACK16_COUNT)==null?"":rs.getString(PUSHBACK16_COUNT)).append(",");
							bw.append(rs.getString(PUSHBACK16_SUM)==null?"":rs.getString(PUSHBACK16_SUM)).append(",");
							bw.append(rs.getString(M1_SETTLE1_COUNT)==null?"":rs.getString(M1_SETTLE1_COUNT)).append(",");
							bw.append(rs.getString(M1_SETTLE1_SUM)==null?"":rs.getString(M1_SETTLE1_SUM)).append(",");
							bw.append(rs.getString(M1_SETTLE16_COUNT)==null?"":rs.getString(M1_SETTLE16_COUNT)).append(",");
							bw.append(rs.getString(M1_SETTLE16_SUM)==null?"":rs.getString(M1_SETTLE16_SUM)).append(",");
							bw.append(rs.getString(M2_SETTLE1_COUNT)==null?"":rs.getString(M2_SETTLE1_COUNT)).append(",");
							bw.append(rs.getString(M2_SETTLE1_SUM)==null?"":rs.getString(M2_SETTLE1_SUM)).append(",");
							bw.append(rs.getString(M2_SETTLE16_COUNT)==null?"":rs.getString(M2_SETTLE16_COUNT)).append(",");
							bw.append(rs.getString(M2_SETTLE16_SUM)==null?"":rs.getString(M2_SETTLE16_SUM)).append("\r");
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
				loanLogService.createLog("ExportLoanCustomersJob", "error", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
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
				
				logger.info("ExportLoanCustomersJob结束........");
				loanLogService.createLog("ExportLoanCustomersJob", "info", "ExportLoanCustomersJob结束........", "SYSTEM");
			}

		}else{
			loanLogService.createLog("ExportLoanCustomersJob", "info", "定时开关isExportLoanCustomers关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isExportLoanCustomers关闭，此次不执行");
		}
	}
}
