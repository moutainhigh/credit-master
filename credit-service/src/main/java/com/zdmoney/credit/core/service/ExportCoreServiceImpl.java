package com.zdmoney.credit.core.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.core.EnumConvertor;
import com.zdmoney.credit.core.FieldNameMapper.FieldNameType;
import com.zdmoney.credit.core.service.pub.IExportCoreService;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.system.dao.pub.IBaseBatchInterfaceLogDao;
import com.zdmoney.credit.system.domain.BaseBatchInterfaceLog;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class ExportCoreServiceImpl implements IExportCoreService {

	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;
	@Autowired
	private IBaseBatchInterfaceLogDao baseBatchInterfaceLogDao;
	@Autowired
	private ISequencesService sequencesService;
	@Autowired
	private ISendMailService sendMailService;
	@Autowired
	private ILoanBaseDao loanBaseDao;
	@Autowired
	private BasicDataSource db;
		
	private static final Logger logger = Logger.getLogger(ExportCoreServiceImpl.class);
	
	private static final int LIMIT = 100000;
	
	
	public Connection getConnection() throws Exception{
		Class.forName(db.getDriverClassName()).newInstance();

		Connection conn;
		try {
			conn = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		} catch (Exception e) {
			logger.error("getConnection:" + e);
			throw e;
		}

//		Connection conn = null;
//		try {
//			conn = DataSourceUtils.getConnection(db);
//		} catch (CannotGetJdbcConnectionException e) {
//			logger.error("getConnection:" + e);
//			throw e;
//		}
		return conn;
	}

	private CSVPrinter getCSVPrinter(String filePath, String fileName)
			throws IOException {
		File f = new File(sysParamDefineService.getSysParamValueCache("cts",
				"cts.path") + "/" + filePath + "/" + fileName + ".csv");
		FileOutputStream fos = FileUtils.openOutputStream(f);// apache commons
																// io 自动创建父目录
		return new CSVPrinter(new OutputStreamWriter(fos, "UTF-8"),
				CSVFormat.EXCEL);
	}

	private String getOKFile(String filePath, String fileName) {
		String result = "处理成功";
		try {
			new File(sysParamDefineService.getSysParamValueCache("cts",
					"cts.path") + "/" + filePath + "/" + fileName + ".csv.ok")
					.createNewFile();
		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 得到前一天的日期返回字符串
	 */
	private String getPathDate() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.get(Calendar.DAY_OF_MONTH) - 1);
		String path = new SimpleDateFormat("yyyyMMdd").format(ca.getTime());
		return path;
	}

	@SuppressWarnings("unused")
	private String print(String path, String fileName,
			List<LinkedHashMap<String, Object>> datas,
			FieldNameType fieldNameType, CSVPrinter printer) throws IOException {

		String result = "处理成功";

		try {
			for (LinkedHashMap<String, Object> data : datas) {

				if (null != data) {
					if (null != fieldNameType) {
						EnumConvertor.convertToCode(data, fieldNameType);
					}
					printer.printRecord(data.values());
				}
			}
			
			printer.flush();
			
		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
		} finally {
			// printer.close();
		}

		return result;
	}
	
	private String print(String path, String fileName,
			ResultSet datas,
			FieldNameType fieldNameType, CSVPrinter printer) throws Exception {

		String result = "处理成功";

		try {
			ResultSetMetaData rsm = datas.getMetaData();
            int columnCount = datas.getMetaData().getColumnCount();
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            while (datas.next()) {
            	/*原代码为<=columnCount，分页后会选出rownum，故去掉"="，避免选出rownum*/
                for (int i = 1; i < columnCount; i++) {
                    map.put(rsm.getColumnName(i), datas.getObject(i));
                }
                if (fieldNameType != null) {
                    EnumConvertor.convertToCode(map, fieldNameType);
                }
                printer.printRecord(map.values());
            }
			
			printer.flush();
			
		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
			throw e;
		} finally {
			// printer.close();
		}

		return result;
	}

	@Override
	public void exportBorrowerCSV() throws Exception {
		String path = getPathDate();
		String fileName = "ICT_CUSTOMER";
		Map<String, Object> params = new HashMap<String, Object>();
		Pager pager = new Pager();
		pager.setRows(LIMIT);
		logger.info("exportBorrowerCSV begin---");
		params.put("pager", pager);
		String result = "处理成功";
		int page = 1;
		CSVPrinter printer = null;
		Connection conn = null;
		try {
			conn = getConnection();
			printer = getCSVPrinter(path, fileName);
//			List<LinkedHashMap<String, Object>> datas = new ArrayList<LinkedHashMap<String, Object>>();
			while (true) {
				
				String sql = jobFreeSqlDao.getSql("getExportBorrower", params);
//				System.out.println(sql);
				PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery();
				
				rs.last();
				if (rs.getRow() == 0) {
					break;
				} else {
					rs.beforeFirst();
				}
				try {

					result = print(path, fileName, rs, FieldNameType.cts4sql, printer);
				} catch (IOException e) {
					result = e.getMessage();
					e.printStackTrace();
					throw e;
				} finally {
					stmt.close();
					rs.close();
				}

				page += 1;

				pager.setPage(page);
				System.gc();
				
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			printer.close();
			conn.close();
		}

		if (result.equals("处理成功")) {
			result = getOKFile(path, fileName);
		}
		try {
			recordLog("exportBorrowerCSV-客户导出", result);
		} catch (Exception e) {
			logger.error("exportBorrowerCSV:" + e);
		}
		
		System.gc();
	}

	@Override
	public void exportLoanCSV() throws Exception {
		String path = getPathDate();
		String fileName = "ICT_ACCOUNT";
		Map<String, Object> params = new HashMap<String, Object>();
		Pager pager = new Pager();
		pager.setRows(LIMIT);
		logger.info("exportLoanCSV begin---");
		params.put("pager", pager);
		String result = "处理成功";
		int page = 1;
		CSVPrinter printer = null;
		Connection conn = null;
		try {
			conn = getConnection();
			printer = getCSVPrinter(path, fileName);
//			List<LinkedHashMap<String, Object>> datas = new ArrayList<LinkedHashMap<String, Object>>();
			while (true) {
//				
				String sql = jobFreeSqlDao.getSql("getExportLoan", params);
//				System.out.println(sql);
				PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery();
				
				rs.last();
				if (rs.getRow() == 0) {
					break;
				} else {
					rs.beforeFirst();
				}

				try {
					result = print(path, fileName, rs, FieldNameType.cts4sql, printer);
				} catch (IOException e) {
					result = e.getMessage();
					e.printStackTrace();
					throw e;
				} finally {
					stmt.close();
					rs.close();
				}
				page += 1;

				pager.setPage(page);

				System.gc();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			printer.close();
			conn.close();
		}

		if (result.equals("处理成功")) {
			result = getOKFile(path, fileName);
		}
		try {
			recordLog("exportLoanCSV-账户导出", result);
		} catch (Exception e) {
			logger.error("exportLoanCSV:" + e);
		}
		
		System.gc();
	}

	@Override
	public void exportContactCSV() throws Exception {
		String path = getPathDate();
		String fileName = "ICT_CONTACT";
		Map<String, Object> params = new HashMap<String, Object>();
		Pager pager = new Pager();
		pager.setRows(LIMIT);
		logger.info("exportContactCSV begin---");
		params.put("pager", pager);
		String result = "处理成功";
		int page = 1;
		CSVPrinter printer = null;
		Connection conn = null;
		try {
			conn = getConnection();
			printer = getCSVPrinter(path, fileName);
//			List<LinkedHashMap<String, Object>> datas = new ArrayList<LinkedHashMap<String, Object>>();
			while (true) {
				
				String sql = jobFreeSqlDao.getSql("getExportContact", params);
//				System.out.println(sql);
				PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery();
				
				rs.last();
				if (rs.getRow() == 0) {
					break;
				} else {
					rs.beforeFirst();
				}

				try {
					result = print(path, fileName, rs, FieldNameType.cts4sql, printer);
				} catch (IOException e) {
					result = e.getMessage();
					e.printStackTrace();
					throw e;
				} finally {
					stmt.close();
					rs.close();
				}
				page += 1;

				pager.setPage(page);
				System.gc();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			printer.close();
			conn.close();
		}

		if (result.equals("处理成功")) {
			result = getOKFile(path, fileName);
		}
		try {
			recordLog("exportContactCSV-联系人导出", result);
		} catch (Exception e) {
			logger.error("exportContactCSV:" + e);
		}
		
		System.gc();
	}

	@Override
	public void exportTelCSV() throws Exception {
		String path = getPathDate();
		String fileName = "ICT_TELEPHONE";
		Map<String, Object> params = new HashMap<String, Object>();
		Pager pager = new Pager();
		pager.setRows(LIMIT);
		logger.info("exportTelCSV begin---");
		params.put("pager", pager);
		String result = "处理成功";
		int page = 1;
		CSVPrinter printer = null;
		Connection conn = null;
		try {
			conn = getConnection();
			printer = getCSVPrinter(path, fileName);
//			List<LinkedHashMap<String, Object>> datas = new ArrayList<LinkedHashMap<String, Object>>();
			while (true) {
				
				String sql = jobFreeSqlDao.getSql("getExportTel", params);
//				System.out.println(sql);
				PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery();
				
				rs.last();
				if (rs.getRow() == 0) {
					break;
				} else {
					rs.beforeFirst();
				}

				try {
					result = print(path, fileName, rs, FieldNameType.cts4sql, printer);
				} catch (IOException e) {
					result = e.getMessage();
					e.printStackTrace();
					throw e;
				} finally {
					stmt.close();
					rs.close();
				}
				page += 1;

				pager.setPage(page);
				System.gc();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			printer.close();
			conn.close();
		}

		if (result.equals("处理成功")) {
			result = getOKFile(path, fileName);
		}
		try {
			recordLog("exportTelCSV-电话导出", result);
		} catch (Exception e) {
			logger.error("exportTelCSV:" + e);
		}
		
		System.gc();
	}

	@Override
	public void exportAddressCSV() throws Exception {
		String path = getPathDate();
		String fileName = "ICT_ADDRESS";
		Map<String, Object> params = new HashMap<String, Object>();
		Pager pager = new Pager();
		pager.setRows(LIMIT);
		logger.info("exportAddressCSV begin---");
		params.put("pager", pager);
		String result = "处理成功";
		int page = 1;
		CSVPrinter printer = null;
		Connection conn = null;
		try {
			conn = getConnection();
			printer = getCSVPrinter(path, fileName);
//			List<LinkedHashMap<String, Object>> datas = new ArrayList<LinkedHashMap<String, Object>>();
			while (true) {
				
				String sql = jobFreeSqlDao.getSql("getExportAddress", params);
//				System.out.println(sql);
				PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery();
				
				rs.last();
				if (rs.getRow() == 0) {
					break;
				} else {
					rs.beforeFirst();
				}

				try {
					result = print(path, fileName, rs, FieldNameType.cts4sql, printer);
				} catch (IOException e) {
					result = e.getMessage();
					e.printStackTrace();
					throw e;
				} finally {
					stmt.close();
					rs.close();
				}
				page += 1;

				pager.setPage(page);
				System.gc();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			printer.close();
			conn.close();
		}

		if (result.equals("处理成功")) {
			result = getOKFile(path, fileName);
		}
		try {
			recordLog("exportAddressCSV-地址导出", result);
		} catch (Exception e) {
			logger.error("exportAddressCSV:" + e);
		}
		
		System.gc();
	}

	@Override
	public void exportRepaymentDetialCSV() throws Exception {
		String path = getPathDate();
		String fileName = "ICT_REPAYPLAN";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dateStr",
				DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd"));

		Pager pager = new Pager();
		pager.setRows(LIMIT);

		params.put("pager", pager);
		String result = "处理成功";
		logger.info("exportRepaymentDetialCSV() begin---");
		int page = 1;
		CSVPrinter printer = null;
		Connection conn = null;
		try {
			conn = getConnection();

			printer = getCSVPrinter(path, fileName);
//			List<LinkedHashMap<String, Object>> datas = new ArrayList<LinkedHashMap<String, Object>>();
			while (true) {
//				List<LinkedHashMap<String, Object>> datas = jobFreeSqlDao.getExportRepaymentDetial(params);
				String sql = jobFreeSqlDao.getSql("getExportRepaymentDetial", params);
//				System.out.println(sql);
				PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery();
				
				rs.last();
				if (rs.getRow() == 0) {
					break;
				} else {
					rs.beforeFirst();
				}
				try {
//					result = print(path, fileName, datas, FieldNameType.cts4sql, printer);
					result = print(path, fileName, rs, FieldNameType.cts4sql, printer);
				} catch (IOException e) {
					result = e.getMessage();
					e.printStackTrace();
					throw e;
				} finally {
					stmt.close();
					rs.close();
				}
				
				
				page += 1;

				pager.setPage(page);
//				datas = null;
				System.gc();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			printer.close();
			conn.close();
		}

		if (result.equals("处理成功")) {
			result = getOKFile(path, fileName);
		}
		try {
			recordLog("exportRepaymentDetialCSV-还款计划导出", result);
		} catch (Exception e) {
			logger.error("exportRepaymentDetialCSV:" + e);
		}
		
		System.gc();
	}

	@Override
	public void exportRepayInfoCSV() throws Exception {
		String path = getPathDate();
		String fileName = "ICT_REPAYFLOW";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dateStr",
				DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd"));

		Pager pager = new Pager();
		pager.setRows(LIMIT);

		params.put("pager", pager);
		String result = "处理成功";
		int page = 1;
		logger.info("exportRepayInfoCSV begin---");
		CSVPrinter printer = null;
		Connection conn = null;
		try {
			conn = getConnection();
			printer = getCSVPrinter(path, fileName);
//			List<LinkedHashMap<String, Object>> datas = new ArrayList<LinkedHashMap<String, Object>>();
			while (true) {
				
				String sql = jobFreeSqlDao.getSql("getExportRepayInfo", params);
//				System.out.println(sql);
				PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery();
				
				rs.last();
				if (rs.getRow() == 0) {
					break;
				} else {
					rs.beforeFirst();
				}

				try {
					result = print(path, fileName, rs, FieldNameType.cts4sql, printer);
				} catch (IOException e) {
					result = e.getMessage();
					e.printStackTrace();
					throw e;
				} finally {
					stmt.close();
					rs.close();
				}
				page += 1;

				pager.setPage(page);
				System.gc();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			printer.close();
			conn.close();
		}

		if (result.equals("处理成功")) {
			result = getOKFile(path, fileName);
		}
		try {
			recordLog("exportRepayInfoCSV-还款流水导出", result);
		} catch (Exception e) {
			logger.error("exportRepayInfoCSV:" + e);
		}
		
		System.gc();
	}

	@Override
	public void exportFlowCSV() throws Exception {
		String path = getPathDate();
		String fileName = "ICT_FLOWSPLIT";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dateStr",
				DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd"));
		logger.info("exportFlowCSV() begin---");
		Pager pager = new Pager();
		pager.setRows(LIMIT);

		params.put("pager", pager);
		String result = "处理成功";
		int page = 1;
		CSVPrinter printer = null;
		Connection conn = null;
		try {
			conn = getConnection();
			printer = getCSVPrinter(path, fileName);
//			List<LinkedHashMap<String, Object>> datas = new ArrayList<LinkedHashMap<String, Object>>();
			while (true) {
				
				String sql = jobFreeSqlDao.getSql("getExportFlow", params);
//				System.out.println(sql);
				PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery();
				
				rs.last();
				if (rs.getRow() == 0) {
					break;
				} else {
					rs.beforeFirst();
				}

				try {
					result = print(path, fileName, rs, FieldNameType.cts4sql, printer);
				} catch (IOException e) {
					result = e.getMessage();
					e.printStackTrace();
					throw e;
				} finally {
					stmt.close();
					rs.close();
				}
				page += 1;

				pager.setPage(page);
				System.gc();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			printer.close();
			conn.close();
		}

		if (result.equals("处理成功")) {
			result = getOKFile(path, fileName);
		}
		try {
			recordLog("exportFlowCSV-流水分账导出", result);
		} catch (Exception e) {
			logger.error("exportFlowCSV:" + e);
		}
		
		System.gc();
	}

	/**
	 * 客户需要每天全量导出的信息
	 * 
	 * @return
	 * @throws IOException
	 */
	@Override
	public void exportPersonTotalCSV() throws Exception {
		String path = getPathDate();
		String fileName = "ICT_CUSTDEPARTMENT";
		Map<String, Object> params = new HashMap<String, Object>();
		Pager pager = new Pager();
		pager.setRows(LIMIT);
		logger.info("exportPersonTotalCSV begin---");
		params.put("pager", pager);
		String result = "处理成功";
		int page = 1;
		CSVPrinter printer = null;
		Connection conn = null;
		try {
			conn = getConnection();
			printer = getCSVPrinter(path, fileName);
//			List<LinkedHashMap<String, Object>> datas = new ArrayList<LinkedHashMap<String, Object>>();
			while (true) {
				
				String sql = jobFreeSqlDao.getSql("getExportPersonTotal", params);
//				System.out.println(sql);
				PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery();
				
				rs.last();
				if (rs.getRow() == 0) {
					break;
				} else {
					rs.beforeFirst();
				}

				try {
					result = print(path, fileName, rs, FieldNameType.cts4sql, printer);
				} catch (IOException e) {
					result = e.getMessage();
					e.printStackTrace();
					throw e;
				} finally {
					stmt.close();
					rs.close();
				}
				page += 1;

				pager.setPage(page);
				System.gc();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			printer.close();
			conn.close();
		}

		if (result.equals("处理成功")) {
			result = getOKFile(path, fileName);
		}
		try {
			recordLog("exportPersonTotalCSV-客户表指定字段全量导出", result);
		} catch (Exception e) {
			logger.error("exportPersonTotalCSV:" + e);
		}
		
		System.gc();
	}

	/**
	 * 客户需要每天全量导出的信息
	 * 
	 * @return
	 * @throws IOException
	 */
	@Override
	public void exportDepartmentCSV() throws Exception {
		String path = getPathDate();
		String fileName = "ICT_SALESDEPARTMENT";
		Map<String, Object> params = new HashMap<String, Object>();
		Pager pager = new Pager();
		pager.setRows(LIMIT);
		logger.info("exportDepartmentCSV begin---");
		params.put("pager", pager);
		String result = "处理成功";
		int page = 1;
		CSVPrinter printer = null;
		Connection conn = null;
		try {
			conn = getConnection();
			printer = getCSVPrinter(path, fileName);
//			List<LinkedHashMap<String, Object>> datas = new ArrayList<LinkedHashMap<String, Object>>();
			while (true) {
				
				String sql = jobFreeSqlDao.getSql("getExportDepartment", params);
//				System.out.println(sql);
				PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery();
				
				rs.last();
				if (rs.getRow() == 0) {
					break;
				} else {
					rs.beforeFirst();
				}

				try {
					result = print(path, fileName, rs, FieldNameType.cts4sql, printer);
				} catch (Exception e) {
					result = e.getMessage();
					e.printStackTrace();
					throw e;
				} finally {
					stmt.close();
					rs.close();
				}
				page += 1;

				pager.setPage(page);
				System.gc();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			printer.close();
			conn.close();
		}

		if (result.equals("处理成功")) {
			result = getOKFile(path, fileName);
		}
		try {
			recordLog("exportDepartmentCSV-门店表字段全量导出", result);
		} catch (Exception e) {
			logger.error("exportDepartmentCSV:" + e);
		}
		
		System.gc();
	}

	/**
	 * 更新Loan表发送字段
	 */
	@Override
	public void updateIsSendForLoan() throws Exception{
		logger.info("updateIsSendForLoan begin---");
			// Loan.executeUpdate("update Loan set isSend=:isSend where loanState=:loanState and isSend <>:isSend",[isSend:true,loanState:LoanState.逾期]);
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("isSendValue", "t");
			params1.put("isSendCondition", "f");
			List<String> states1 = new ArrayList<String>();
			states1.add(LoanStateEnum.逾期.toString());
			params1.put("states", states1);

			loanBaseDao.updateIssendByStateAndIssend(params1);

			// Loan.executeUpdate("update Loan set isSend=:isSend where loanState in(:loanState1,:loanState2,:loanState3) and isSend <>:isSend",[isSend:false,loanState1:LoanState.正常,loanState2:LoanState.结清,loanState3:LoanState.预结清]);
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("isSendValue", "f");
			params2.put("isSendCondition", "t");
			List<String> states2 = new ArrayList<String>();
			states2.add(LoanStateEnum.正常.toString());
			states2.add(LoanStateEnum.结清.toString());
			states2.add(LoanStateEnum.预结清.toString());
			params2.put("states", states2);

			loanBaseDao.updateIssendByStateAndIssend(params2);

	}

	private void recordLog(String source, String message) throws Exception{

			BaseBatchInterfaceLog log = new BaseBatchInterfaceLog();
			log.setSource(source);
			log.setCreator(Const.ENDOFDAY_TELLER);
			log.setMessage(message == null ? "null" : message);
			log.setId(sequencesService
					.getSequences(SequencesEnum.BASE_BATCH_INTERFACE_LOG));

			baseBatchInterfaceLogDao.insert(log);

	}

	@Override
	public void sendEmail() throws Exception{
		logger.info("sendEmail begin---");
		StringBuffer sb = new StringBuffer();
		String r;
		String c;
		try {
			BaseBatchInterfaceLog condition = new BaseBatchInterfaceLog();
			condition.setCreateTime(new Date());
			List<BaseBatchInterfaceLog> batchInterfaceLogList = baseBatchInterfaceLogDao
					.findListByVo(condition);
			for (BaseBatchInterfaceLog b : batchInterfaceLogList) {
				sb.append(b.getSource() + "------" + b.getMessage() + "\n");
			}
			r = sysParamDefineService.getSysParamValueCache("cts",
					"cts.email.to");
			c = sysParamDefineService.getSysParamValueCache("cts",
					"cts.email.cc");

			sendMailService.sendPaymentRiskMail(r, c, "催收-批量接口发送信息",
					sb.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.out.println("发送邮件失败");
			throw e;
		}

	}
}
