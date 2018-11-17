package com.zdmoney.credit.core.calculator.fundssource.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.excel.vo.VExcelInfo;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
/**
 * 通过计算器获取数据
 * 传入：审批金额，借款期限，综合费率，补偿利率，放款时间
 * filepath：计算器excel存放路径
 * @author YM10104
 *
 */
public class ExcelGetInfo {
	private Workbook wb;
	private Sheet sheet;
	private LoanInitialInfo loanInitialInfo; 
	private ProdCreditProductTerm prodCreditProductTerm;
	private LoanProduct loanProduct;
	private LoanBase loanBase;
	FormulaEvaluator eval = null;
	
	/**
	 * 读取Excel文件
	 */
	public ExcelGetInfo(String filepath,LoanInitialInfo loanInitialInfo,ProdCreditProductTerm prodCreditProductTerm,LoanProduct loanProduct,LoanBase loanBase) {
		this.loanInitialInfo = loanInitialInfo;  
        this.prodCreditProductTerm = prodCreditProductTerm;
        this.loanProduct = loanProduct;
        this.loanBase = loanBase;
		if (filepath == null) {
			return;
		}

		String ext = filepath.substring(filepath.lastIndexOf("."));

		try {
			InputStream inputstream = cloneFileInput(filepath);
			if (".xls".equals(ext)) {
				wb = new HSSFWorkbook(inputstream);
			} else if (".xlsx".equals(ext)) {
				wb = new XSSFWorkbook(inputstream);
			} else {
				wb = null;
			}
			/*
			 * createFormulaEvaluator
			 */
			if (wb != null) {
				eval = wb.getCreationHelper().createFormulaEvaluator();
			}
			// inputstream.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		} 
	}
	public InputStream cloneFileInput(String filepath) throws Exception {
		InputStream inputstream = null;
		try {
			inputstream = new FileInputStream(filepath);

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputstream.read(buffer)) > -1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}

			return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());// 输出流转化为输入流
		} finally {
			if (inputstream != null) {
				inputstream.close();
			}
		}
	}
	
	/**
	 * 传入参数，返回生成的数据
	 */
	public VExcelInfo getInfo(){
		String fundsSources = loanBase.getFundsSources();
		if (wb == null) {
			System.out.println("Workbook对象为空！");
			return null;
		}
		if(FundsSourcesTypeEnum.渤海2.name().equals(fundsSources) || FundsSourcesTypeEnum.外贸3.name().equals(fundsSources)||FundsSourcesTypeEnum.陆金所.name().equals(fundsSources)
				||FundsSourcesTypeEnum.龙信小贷.name().equals(fundsSources)){
		    sheet = wb.getSheetAt(0);
		} else if(FundsSourcesTypeEnum.捞财宝.name().equals(fundsSources)){
			if("Y".equals(loanInitialInfo.getIsRatePreferLoan())){//是优惠费率客户
				sheet = wb.getSheetAt(1);
			}else{
				sheet = wb.getSheetAt(0);
			}			
		} else if(FundsSourcesTypeEnum.证大P2P.name().equals(fundsSources)){
			if("Y".equals(loanInitialInfo.getIsRatePreferLoan())){//是优惠费率客户
				sheet = wb.getSheetAt(1);
			}else{//普通客户
				sheet = wb.getSheetAt(0);
			}
		}else{
		    for (int i = 0; i < wb.getNumberOfSheets(); i++) {//判断读取哪个excel
	            if(wb.getSheetName(i).equals(loanInitialInfo.getLoanType())){
	                sheet = wb.getSheetAt(i);
	            }
	        }
		}
		sheet.setForceFormulaRecalculation(true);
		if(FundsSourcesTypeEnum.包商银行.getValue().equals(fundsSources)){			
			return bsyhInfo();
		}else if(FundsSourcesTypeEnum.捞财宝.getValue().equals(fundsSources)){
			return lcbInfo();
		}else if(FundsSourcesTypeEnum.陆金所.getValue().equals(fundsSources)){
			return ljsInfo();			
		}else if(FundsSourcesTypeEnum.华瑞渤海.getValue().equals(fundsSources)
		        || FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSources)
				|| FundsSourcesTypeEnum.外贸3.getValue().equals(fundsSources)){
			return hrbhBh2Bh3Info();
		}else if(FundsSourcesTypeEnum.证大P2P.getValue().equals(fundsSources)){
			return zdInfo();
		}else if(FundsSourcesTypeEnum.龙信小贷.getValue().equals(fundsSources)){
			return lxInfo();
		}
		return null;
	}
	private VExcelInfo zdInfo() {
		//设置审批金额
		CellReference refe=new CellReference("C1");
		Row row=sheet.getRow(refe.getRow());
		Cell cell=row.getCell(refe.getCol());
		cell.setCellValue(loanInitialInfo.getMoney().doubleValue());
		//设置借款期限
		refe=new CellReference("F1");
		row=sheet.getRow(refe.getRow());
		Cell cell1=row.getCell(refe.getCol());
		cell1.setCellValue(prodCreditProductTerm.getTerm());
		
		//设置产品 新增
		refe=new CellReference("I1"); 
		row=sheet.getRow(refe.getRow());
		Cell cell14=row.getCell(refe.getCol());
		cell14.setCellValue(loanInitialInfo.getLoanType());
		
		//设置放款日期  新增
//		refe=new CellReference("C2");
//		row=sheet.getRow(refe.getRow());
//		Cell cell15=row.getCell(refe.getCol());
//		cell15.setCellValue(Dates.getAfterDays(loanInitialInfo.getSignDate(),2));
		
		
		//设置综合费率
		refe=new CellReference("K1");
		row=sheet.getRow(refe.getRow());
		Cell cell2=row.getCell(refe.getCol());
		if("Y".equals(loanInitialInfo.getIsRatePreferLoan())){
			cell2.setCellValue(prodCreditProductTerm.getReloanRate().doubleValue());
		}else{
			cell2.setCellValue(prodCreditProductTerm.getRate().doubleValue());			
		}
		//设置补偿利率
		refe=new CellReference("K2");
		row=sheet.getRow(refe.getRow());
		Cell cell3=row.getCell(refe.getCol());
		if("Y".equals(loanInitialInfo.getIsRatePreferLoan())){
			cell3.setCellValue(prodCreditProductTerm.getReloanAccrualem().doubleValue());
		}else{
			cell3.setCellValue(prodCreditProductTerm.getAccrualem().doubleValue());			
		}		

		//读取数据
		VExcelInfo excelInfo=new VExcelInfo();	
		refe=new CellReference("C4");//B4
		row=sheet.getRow(refe.getRow());
		Cell cell4=row.getCell(refe.getCol());	
		excelInfo.setPactMoney(getBigDecimal( getCellFormatValue(cell4)));//合同金额
		
		refe=new CellReference("M7");
		row=sheet.getRow(refe.getRow());
		Cell cell5=row.getCell(refe.getCol());
		excelInfo.setRisk(getBigDecimal(getCellFormatValue(cell5)));//风险金
		
		refe=new CellReference("M8");
		row=sheet.getRow(refe.getRow());
		Cell cell6=row.getCell(refe.getCol());
		excelInfo.setReferRate(getBigDecimal(getCellFormatValue(cell6)));//咨询费
		
		refe=new CellReference("M9");
		row=sheet.getRow(refe.getRow());
		Cell cell19=row.getCell(refe.getCol());
		excelInfo.setEvalRate(getBigDecimal(getCellFormatValue(cell19)));//审核费
		
		refe=new CellReference("M10");
		row=sheet.getRow(refe.getRow());
		Cell cell7=row.getCell(refe.getCol());
		excelInfo.setManageRate(getBigDecimal(getCellFormatValue(cell7)));//管理费
		
		refe=new CellReference("M11");
		row=sheet.getRow(refe.getRow());
		Cell cel21=row.getCell(refe.getCol());
		excelInfo.setManageRateForPartyC(getBigDecimal(getCellFormatValue(cel21)));//管理费
		
		refe=new CellReference("F4");//E4
		row=sheet.getRow(refe.getRow());
		Cell cellRateem=row.getCell(refe.getCol());
		excelInfo.setRateem(getBigDecimal(getCellFormatValue(cellRateem)));//月利率
		
		refe=new CellReference("I4");//H4
		row=sheet.getRow(refe.getRow());
		Cell cellRateey=row.getCell(refe.getCol());
		excelInfo.setRateey(getBigDecimal(getCellFormatValue(cellRateey)));//年利率
		
		//读取还款计划
		List<LoanRepaymentDetail> list =new ArrayList<LoanRepaymentDetail>();
		for (int i = 8; i < prodCreditProductTerm.getTerm()+8; i++) {
			LoanRepaymentDetail loanRepaymentDetail = new LoanRepaymentDetail();
			
			refe=new CellReference("E"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell8=row.getCell(refe.getCol());
			loanRepaymentDetail.setCurrentAccrual(getBigDecimal(getCellFormatValue(cell8)));//当期利息
			
			refe=new CellReference("D"+(i+1));
			row=sheet.getRow(refe.getRow());
			Cell cell9=row.getCell(refe.getCol());
			if(i-7==prodCreditProductTerm.getTerm()){//最后一期
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(0));// 本金余额
			}else{
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(getCellFormatValue(cell9)));// 本金余额		
			}
			
			refe=new CellReference("I"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell10=row.getCell(refe.getCol());
			loanRepaymentDetail.setGiveBackRate(getBigDecimal(getCellFormatValue(cell10)));// 退费
			
			refe=new CellReference("H"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell11=row.getCell(refe.getCol());
			loanRepaymentDetail.setPenalty(getBigDecimal(getCellFormatValue(cell11)));// 违约金
			
			refe=new CellReference("G"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell12=row.getCell(refe.getCol());
			loanRepaymentDetail.setRepaymentAll(getBigDecimal(getCellFormatValue(cell12)));// 一次性还款金额
			
			refe=new CellReference("C"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell13=row.getCell(refe.getCol());
			loanRepaymentDetail.setReturneterm(getBigDecimal(getCellFormatValue(cell13)));//每期还款金额
			loanRepaymentDetail.setDeficit(getBigDecimal(getCellFormatValue(cell13)));//剩余欠款
			
//			refe=new CellReference("A"+i);
//			row=sheet.getRow(refe.getRow());
//			Cell cell20=row.getCell(refe.getCol());
//			loanRepaymentDetail.setReturnDate((Date) getCellFormatValue(cell20));//固定还款日期
//			loanRepaymentDetail.setPenaltyDate((Date) getCellFormatValue(cell20));//罚息起算日期

			list.add(loanRepaymentDetail);
		}
		excelInfo.setExcelDetailList(list);
		return excelInfo;
	}
	private VExcelInfo hrbhBh2Bh3Info() {
		//设置审批金额
		CellReference refe=new CellReference("B1");
		Row row=sheet.getRow(refe.getRow());
		Cell cell=row.getCell(refe.getCol());
		cell.setCellValue(loanInitialInfo.getMoney().doubleValue());
		
		//设置借款期限
		refe=new CellReference("E1");
		row=sheet.getRow(refe.getRow());
		Cell cell1=row.getCell(refe.getCol());
		cell1.setCellValue(prodCreditProductTerm.getTerm());
		//设置产品类型
		refe=new CellReference("H1");
		row=sheet.getRow(refe.getRow());
		Cell cell2=row.getCell(refe.getCol());
		cell2.setCellValue(loanInitialInfo.getLoanType());
		//设置综合费率
		refe=new CellReference("J1");
		row=sheet.getRow(refe.getRow());
		Cell cellj1=row.getCell(refe.getCol());
		cellj1.setCellValue(prodCreditProductTerm.getRate().doubleValue());
		//设置补偿费率
		refe=new CellReference("J2");
		row=sheet.getRow(refe.getRow());
		Cell cellj2=row.getCell(refe.getCol());
		cellj2.setCellValue(prodCreditProductTerm.getAccrualem().doubleValue());
		
		//读取数据
		VExcelInfo excelInfo=new VExcelInfo();
		
		refe=new CellReference("B4");
		row=sheet.getRow(refe.getRow());
		Cell cell4=row.getCell(refe.getCol());	
		excelInfo.setPactMoney(getBigDecimal( getCellFormatValue(cell4))); //合同金额
		
		refe=new CellReference("K7");
		row=sheet.getRow(refe.getRow());
		Cell cell5=row.getCell(refe.getCol());
		excelInfo.setRisk(getBigDecimal(getCellFormatValue(cell5))); //风险金
		
		refe=new CellReference("K8");
		row=sheet.getRow(refe.getRow());
		Cell cell6=row.getCell(refe.getCol());
		excelInfo.setReferRate(getBigDecimal(getCellFormatValue(cell6))); //咨询费
		
		refe=new CellReference("K9");
		row=sheet.getRow(refe.getRow());
		Cell cell20=row.getCell(refe.getCol());
		excelInfo.setEvalRate(getBigDecimal(getCellFormatValue(cell20))); //评估费
		
		refe=new CellReference("K10");
		row=sheet.getRow(refe.getRow());
		Cell cell7=row.getCell(refe.getCol());
		excelInfo.setManageRate(getBigDecimal(getCellFormatValue(cell7))); //管理费
		
		refe=new CellReference("E4");
		row=sheet.getRow(refe.getRow());
		Cell cell14=row.getCell(refe.getCol());
		excelInfo.setRateem(getBigDecimal(getCellFormatValue(cell14))); //合同月利率
		
		refe=new CellReference("H4");
		row=sheet.getRow(refe.getRow());
		Cell cell15=row.getCell(refe.getCol());
		excelInfo.setRateey(getBigDecimal(getCellFormatValue(cell15))); //合同年利率
		
		
		
		//读取还款计划
		List<LoanRepaymentDetail> list =new ArrayList<LoanRepaymentDetail>();
		for (int i = 8; i < prodCreditProductTerm.getTerm()+8; i++) {
			LoanRepaymentDetail loanRepaymentDetail = new LoanRepaymentDetail();
			
			refe=new CellReference("D"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell8=row.getCell(refe.getCol());
			loanRepaymentDetail.setCurrentAccrual(getBigDecimal(getCellFormatValue(cell8)));//当期利息
			
			refe=new CellReference("C"+(i+1));
			row=sheet.getRow(refe.getRow());
			Cell cell9=row.getCell(refe.getCol());
			if(i-7==prodCreditProductTerm.getTerm()){//最后一期
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(0));// 本金余额
			}else{
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(getCellFormatValue(cell9)));// 本金余额		
			}
			
			refe=new CellReference("H"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell10=row.getCell(refe.getCol());
			loanRepaymentDetail.setGiveBackRate(getBigDecimal(getCellFormatValue(cell10)));// 退费
			
			refe=new CellReference("G"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell11=row.getCell(refe.getCol());
			loanRepaymentDetail.setPenalty(getBigDecimal(getCellFormatValue(cell11)));// 违约金
			
			refe=new CellReference("F"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell12=row.getCell(refe.getCol());
			loanRepaymentDetail.setRepaymentAll(getBigDecimal(getCellFormatValue(cell12)));// 一次性还款金额
			
			refe=new CellReference("B"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell13=row.getCell(refe.getCol());
			loanRepaymentDetail.setReturneterm(getBigDecimal(getCellFormatValue(cell13)));//每期还款金额			
			loanRepaymentDetail.setDeficit(getBigDecimal(getCellFormatValue(cell13)));//剩余欠款
			
			list.add(loanRepaymentDetail);
		}
		excelInfo.setExcelDetailList(list);
		return excelInfo;
	}
	private VExcelInfo ljsInfo() {
		//设置审批金额
		CellReference refe=new CellReference("B1");
		Row row=sheet.getRow(refe.getRow());
		Cell cell=row.getCell(refe.getCol());
		cell.setCellValue(loanInitialInfo.getMoney().doubleValue());
		
		//设置借款期限
		refe=new CellReference("E1");
		row=sheet.getRow(refe.getRow());
		Cell cell1=row.getCell(refe.getCol());
		cell1.setCellValue(prodCreditProductTerm.getTerm());
		//设置产品类型
		refe=new CellReference("H1");
		row=sheet.getRow(refe.getRow());
		Cell cell2=row.getCell(refe.getCol());
		cell2.setCellValue(loanInitialInfo.getLoanType());
		//设置综合费率
		refe=new CellReference("J1");
		row=sheet.getRow(refe.getRow());
		Cell cellj1=row.getCell(refe.getCol());
		cellj1.setCellValue(prodCreditProductTerm.getRate().doubleValue());
		//设置补偿费率
		refe=new CellReference("J2");
		row=sheet.getRow(refe.getRow());
		Cell cellj2=row.getCell(refe.getCol());
		cellj2.setCellValue(prodCreditProductTerm.getAccrualem().doubleValue());
		
		//读取数据
		VExcelInfo excelInfo=new VExcelInfo();
		
		refe=new CellReference("B4");
		row=sheet.getRow(refe.getRow());
		Cell cell4=row.getCell(refe.getCol());	
		excelInfo.setPactMoney(getBigDecimal( getCellFormatValue(cell4))); //合同金额
		
		refe=new CellReference("L7");
		row=sheet.getRow(refe.getRow());
		Cell cell5=row.getCell(refe.getCol());
		excelInfo.setRisk(getBigDecimal(getCellFormatValue(cell5))); //风险金
		
		refe=new CellReference("L8");
		row=sheet.getRow(refe.getRow());
		Cell cell6=row.getCell(refe.getCol());
		excelInfo.setReferRate(getBigDecimal(getCellFormatValue(cell6))); //咨询费
		
		refe=new CellReference("L9");
		row=sheet.getRow(refe.getRow());
		Cell cell20=row.getCell(refe.getCol());
		excelInfo.setEvalRate(getBigDecimal(getCellFormatValue(cell20))); //评估费
		
		refe=new CellReference("L10");
		row=sheet.getRow(refe.getRow());
		Cell cell7=row.getCell(refe.getCol());
		excelInfo.setManageRate(getBigDecimal(getCellFormatValue(cell7))); //管理费
		
		refe=new CellReference("E4");
		row=sheet.getRow(refe.getRow());
		Cell cell14=row.getCell(refe.getCol());
		excelInfo.setRateem(getBigDecimal(getCellFormatValue(cell14))); //合同月利率
		
		refe=new CellReference("H4");
		row=sheet.getRow(refe.getRow());
		Cell cell15=row.getCell(refe.getCol());
		excelInfo.setRateey(getBigDecimal(getCellFormatValue(cell15))); //合同年利率
		
		
		
		//读取还款计划
		List<LoanRepaymentDetail> list =new ArrayList<LoanRepaymentDetail>();
		for (int i = 8; i < prodCreditProductTerm.getTerm()+8; i++) {
			LoanRepaymentDetail loanRepaymentDetail = new LoanRepaymentDetail();
			
			refe=new CellReference("D"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell8=row.getCell(refe.getCol());
			loanRepaymentDetail.setCurrentAccrual(getBigDecimal(getCellFormatValue(cell8)));//当期利息
			
			refe=new CellReference("C"+(i+1));
			row=sheet.getRow(refe.getRow());
			Cell cell9=row.getCell(refe.getCol());
			if(i-7==prodCreditProductTerm.getTerm()){//最后一期
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(0));// 本金余额
			}else{
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(getCellFormatValue(cell9)));// 本金余额		
			}
			
			refe=new CellReference("H"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell10=row.getCell(refe.getCol());
			loanRepaymentDetail.setGiveBackRate(getBigDecimal(getCellFormatValue(cell10)));// 退费
			
			refe=new CellReference("G"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell11=row.getCell(refe.getCol());
			loanRepaymentDetail.setPenalty(getBigDecimal(getCellFormatValue(cell11)));// 违约金
			
			refe=new CellReference("F"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell12=row.getCell(refe.getCol());
			loanRepaymentDetail.setRepaymentAll(getBigDecimal(getCellFormatValue(cell12)));// 一次性还款金额
			
			refe=new CellReference("B"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell13=row.getCell(refe.getCol());
			loanRepaymentDetail.setReturneterm(getBigDecimal(getCellFormatValue(cell13)));//每期还款金额			
			loanRepaymentDetail.setDeficit(getBigDecimal(getCellFormatValue(cell13)));//剩余欠款
			
			list.add(loanRepaymentDetail);
		}
		excelInfo.setExcelDetailList(list);
		return excelInfo;
	}
	private VExcelInfo lcbInfo() {
		//设置审批金额
		CellReference refe=new CellReference("C1");
		Row row=sheet.getRow(refe.getRow());
		Cell cell=row.getCell(refe.getCol());
		cell.setCellValue(loanInitialInfo.getMoney().doubleValue());
		//设置借款期限
		refe=new CellReference("F1");
		row=sheet.getRow(refe.getRow());
		Cell cell1=row.getCell(refe.getCol());
		cell1.setCellValue(prodCreditProductTerm.getTerm());
		
		//设置产品 新增
		refe=new CellReference("I1"); 
		row=sheet.getRow(refe.getRow());
		Cell cell14=row.getCell(refe.getCol());
		cell14.setCellValue(loanInitialInfo.getLoanType());
		
		//设置放款日期  新增
		refe=new CellReference("C2");
		row=sheet.getRow(refe.getRow());
		Cell cell15=row.getCell(refe.getCol());
		cell15.setCellValue(Dates.getAfterDays(loanInitialInfo.getSignDate(),2));
		
		
		//设置综合费率
		refe=new CellReference("K1");
		row=sheet.getRow(refe.getRow());
		Cell cell2=row.getCell(refe.getCol());
		if("Y".equals(loanInitialInfo.getIsRatePreferLoan())){
			cell2.setCellValue(prodCreditProductTerm.getReloanRate().doubleValue());
		}else{
			cell2.setCellValue(prodCreditProductTerm.getRate().doubleValue());			
		}
		
		//设置补偿利率
		refe=new CellReference("K2");
		row=sheet.getRow(refe.getRow());
		Cell cell3=row.getCell(refe.getCol());
		if("Y".equals(loanInitialInfo.getIsRatePreferLoan())){
			cell3.setCellValue(prodCreditProductTerm.getReloanAccrualem().doubleValue());
		}else{
			cell3.setCellValue(prodCreditProductTerm.getAccrualem().doubleValue());			
		}		

		//读取数据
		VExcelInfo excelInfo=new VExcelInfo();	
		refe=new CellReference("C4");//B4
		row=sheet.getRow(refe.getRow());
		Cell cell4=row.getCell(refe.getCol());	
		excelInfo.setPactMoney(getBigDecimal( getCellFormatValue(cell4)));//合同金额
		
		refe=new CellReference("M7");
		row=sheet.getRow(refe.getRow());
		Cell cell5=row.getCell(refe.getCol());
		excelInfo.setRisk(getBigDecimal(getCellFormatValue(cell5)));//风险金
		
		refe=new CellReference("M8");
		row=sheet.getRow(refe.getRow());
		Cell cell6=row.getCell(refe.getCol());
		excelInfo.setReferRate(getBigDecimal(getCellFormatValue(cell6)));//咨询费
		
		refe=new CellReference("M9");
		row=sheet.getRow(refe.getRow());
		Cell cell19=row.getCell(refe.getCol());
		excelInfo.setEvalRate(getBigDecimal(getCellFormatValue(cell19)));//审核费
		
		refe=new CellReference("M10");
		row=sheet.getRow(refe.getRow());
		Cell cell7=row.getCell(refe.getCol());
		excelInfo.setManageRate(getBigDecimal(getCellFormatValue(cell7)));//管理费

		refe=new CellReference("M11");
		row=sheet.getRow(refe.getRow());
		Cell cel21=row.getCell(refe.getCol());
		excelInfo.setManageRateForPartyC(getBigDecimal(getCellFormatValue(cel21)));//管理费

		refe=new CellReference("F4");//E4
		row=sheet.getRow(refe.getRow());
		Cell cellRateem=row.getCell(refe.getCol());
		excelInfo.setRateem(getBigDecimal(getCellFormatValue(cellRateem)));//月利率
		
		refe=new CellReference("I4");//H4
		row=sheet.getRow(refe.getRow());
		Cell cellRateey=row.getCell(refe.getCol());
		excelInfo.setRateey(getBigDecimal(getCellFormatValue(cellRateey)));//年利率
		
		//读取还款计划
		List<LoanRepaymentDetail> list =new ArrayList<LoanRepaymentDetail>();
		for (int i = 8; i < prodCreditProductTerm.getTerm()+8; i++) {
			LoanRepaymentDetail loanRepaymentDetail = new LoanRepaymentDetail();
			
			refe=new CellReference("E"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell8=row.getCell(refe.getCol());
			loanRepaymentDetail.setCurrentAccrual(getBigDecimal(getCellFormatValue(cell8)));//当期利息
			
			refe=new CellReference("D"+(i+1));
			row=sheet.getRow(refe.getRow());
			Cell cell9=row.getCell(refe.getCol());
			if(i-7==prodCreditProductTerm.getTerm()){//最后一期
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(0));// 本金余额
			}else{
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(getCellFormatValue(cell9)));// 本金余额		
			}
			
			refe=new CellReference("I"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell10=row.getCell(refe.getCol());
			loanRepaymentDetail.setGiveBackRate(getBigDecimal(getCellFormatValue(cell10)));// 退费
			
			refe=new CellReference("H"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell11=row.getCell(refe.getCol());
			loanRepaymentDetail.setPenalty(getBigDecimal(getCellFormatValue(cell11)));// 违约金
			
			refe=new CellReference("G"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell12=row.getCell(refe.getCol());
			loanRepaymentDetail.setRepaymentAll(getBigDecimal(getCellFormatValue(cell12)));// 一次性还款金额
			
			refe=new CellReference("C"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell13=row.getCell(refe.getCol());
			loanRepaymentDetail.setReturneterm(getBigDecimal(getCellFormatValue(cell13)));//每期还款金额			
			loanRepaymentDetail.setDeficit(getBigDecimal(getCellFormatValue(cell13)));//剩余欠款
			
			refe=new CellReference("A"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell20=row.getCell(refe.getCol());
			loanRepaymentDetail.setReturnDate((Date) getCellFormatValue(cell20));//固定还款日期
			loanRepaymentDetail.setPenaltyDate((Date) getCellFormatValue(cell20));//罚息起算日期

			list.add(loanRepaymentDetail);
		}
		excelInfo.setExcelDetailList(list);
		return excelInfo;
	}
	private VExcelInfo bsyhInfo() {
		//设置审批金额
		CellReference refe=new CellReference("C1");
		Row row=sheet.getRow(refe.getRow());
		Cell cell=row.getCell(refe.getCol());
		cell.setCellValue(loanInitialInfo.getMoney().doubleValue());
		
		//设置借款期限
		refe=new CellReference("F1");
		row=sheet.getRow(refe.getRow());
		Cell cell1=row.getCell(refe.getCol());
		cell1.setCellValue(prodCreditProductTerm.getTerm());
		//设置综合费率
		refe=new CellReference("I1");
		row=sheet.getRow(refe.getRow());
		Cell cell2=row.getCell(refe.getCol());
		cell2.setCellValue(prodCreditProductTerm.getRate().doubleValue());
		//设置补偿利率
		refe=new CellReference("K1");
		row=sheet.getRow(refe.getRow());
		Cell cell3=row.getCell(refe.getCol());
		cell3.setCellValue(prodCreditProductTerm.getAccrualem().doubleValue());
		
		//放款时间
		refe=new CellReference("C2");
		row=sheet.getRow(refe.getRow());
		Cell cell18=row.getCell(refe.getCol());
		if(null==loanInitialInfo.getGrantMoneyDate()){
			cell18.setCellValue(Dates.format(new Date(),"yyyy/MM/dd"));
		}else{
			cell18.setCellValue(Dates.format(loanInitialInfo.getGrantMoneyDate(),"yyyy/MM/dd"));			
		}
		//首期还款日
		refe=new CellReference("F2");
		row=sheet.getRow(refe.getRow());
		Cell cell19=row.getCell(refe.getCol());
		cell19.setCellValue(Dates.format(loanProduct.getStartrdate(),"yyyy/MM/dd"));
		
		//读取数据
		VExcelInfo excelInfo=new VExcelInfo();
		
		refe=new CellReference("C4");
		row=sheet.getRow(refe.getRow());
		Cell cell4=row.getCell(refe.getCol());	
		excelInfo.setPactMoney(getBigDecimal( getCellFormatValue(cell4)));
		
		refe=new CellReference("O8");
		row=sheet.getRow(refe.getRow());
		Cell cell5=row.getCell(refe.getCol());
		excelInfo.setRisk(getBigDecimal(getCellFormatValue(cell5)));
		
		refe=new CellReference("O9");
		row=sheet.getRow(refe.getRow());
		Cell cell6=row.getCell(refe.getCol());
		excelInfo.setReferRate(getBigDecimal(getCellFormatValue(cell6)));
		
		refe=new CellReference("O10");
		row=sheet.getRow(refe.getRow());
		Cell cell20=row.getCell(refe.getCol());
		excelInfo.setEvalRate(getBigDecimal(getCellFormatValue(cell20)));
		
		refe=new CellReference("O11");
		row=sheet.getRow(refe.getRow());
		Cell cell7=row.getCell(refe.getCol());
		excelInfo.setManageRate(getBigDecimal(getCellFormatValue(cell7)));
		
		refe=new CellReference("F4");
		row=sheet.getRow(refe.getRow());
		Cell cell14=row.getCell(refe.getCol());
		excelInfo.setRateem(getBigDecimal(getCellFormatValue(cell14)));
		
		refe=new CellReference("I4");
		row=sheet.getRow(refe.getRow());
		Cell cell15=row.getCell(refe.getCol());
		excelInfo.setRateey(getBigDecimal(getCellFormatValue(cell15)));
		
		refe=new CellReference("I5");
		row=sheet.getRow(refe.getRow());
		Cell cell16=row.getCell(refe.getCol());
		excelInfo.setRateed(getBigDecimal(getCellFormatValue(cell16)));//日利率
		
		refe=new CellReference("L10");
		row=sheet.getRow(refe.getRow());
		Cell cell17=row.getCell(refe.getCol());
		excelInfo.setPenaltyRate(getBigDecimal(getCellFormatValue(cell17)));//逾期罚息
		
		//读取还款计划
		List<LoanRepaymentDetail> list =new ArrayList<LoanRepaymentDetail>();
		for (int i = 8; i < prodCreditProductTerm.getTerm()+8; i++) {
			LoanRepaymentDetail loanRepaymentDetail = new LoanRepaymentDetail();
			
			refe=new CellReference("E"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell8=row.getCell(refe.getCol());
			loanRepaymentDetail.setCurrentAccrual(getBigDecimal(getCellFormatValue(cell8)));//当期利息
			
			refe=new CellReference("D"+(i+1));
			row=sheet.getRow(refe.getRow());
			Cell cell9=row.getCell(refe.getCol());
			if(i-7==prodCreditProductTerm.getTerm()){//最后一期
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(0));// 本金余额
			}else{
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(getCellFormatValue(cell9)));// 本金余额		
			}
			
			refe=new CellReference("I"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell10=row.getCell(refe.getCol());
			loanRepaymentDetail.setGiveBackRate(getBigDecimal(getCellFormatValue(cell10)));// 退费
			
			refe=new CellReference("H"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell11=row.getCell(refe.getCol());
			loanRepaymentDetail.setPenalty(getBigDecimal(getCellFormatValue(cell11)));// 违约金
			
			refe=new CellReference("G"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell12=row.getCell(refe.getCol());
			loanRepaymentDetail.setRepaymentAll(getBigDecimal(getCellFormatValue(cell12)));// 一次性还款金额
			
			refe=new CellReference("C"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell13=row.getCell(refe.getCol());
			loanRepaymentDetail.setReturneterm(getBigDecimal(getCellFormatValue(cell13)));//每期还款金额			
			loanRepaymentDetail.setDeficit(getBigDecimal(getCellFormatValue(cell13)));//剩余欠款
			
//				refe=new CellReference("A"+i);
//				row=sheet.getRow(refe.getRow());
//				Cell cell19=row.getCell(refe.getCol());
//				loanRepaymentDetail.setReturnDate((Date) getCellFormatValue(cell19));//固定还款日期
//				loanRepaymentDetail.setPenaltyDate((Date) getCellFormatValue(cell19));//罚息起算日期

			list.add(loanRepaymentDetail);
		}
		excelInfo.setExcelDetailList(list);
		return excelInfo;
	}
	private VExcelInfo lxInfo() {
		//设置审批金额
		CellReference refe=new CellReference("C1");
		Row row=sheet.getRow(refe.getRow());
		Cell cell=row.getCell(refe.getCol());
		cell.setCellValue(loanInitialInfo.getMoney().doubleValue());
		//设置借款期限
		refe=new CellReference("F1");
		row=sheet.getRow(refe.getRow());
		Cell cell1=row.getCell(refe.getCol());
		cell1.setCellValue(prodCreditProductTerm.getTerm());

		//设置产品 新增
		refe=new CellReference("I1");
		row=sheet.getRow(refe.getRow());
		Cell cell14=row.getCell(refe.getCol());
		cell14.setCellValue(loanInitialInfo.getLoanType());

		//设置放款日期  新增
		refe=new CellReference("C2");
		row=sheet.getRow(refe.getRow());
		Cell cell15=row.getCell(refe.getCol());
		cell15.setCellValue(Dates.getAfterDays(loanInitialInfo.getSignDate(),2));


		//设置综合费率
		refe=new CellReference("K1");
		row=sheet.getRow(refe.getRow());
		Cell cell2=row.getCell(refe.getCol());
		cell2.setCellValue(prodCreditProductTerm.getRate().doubleValue());


		//设置补偿利率
		refe=new CellReference("K2");
		row=sheet.getRow(refe.getRow());
		Cell cell3=row.getCell(refe.getCol());
		cell3.setCellValue(prodCreditProductTerm.getAccrualem().doubleValue());

		//读取数据
		VExcelInfo excelInfo=new VExcelInfo();
		refe=new CellReference("C4");//B4
		row=sheet.getRow(refe.getRow());
		Cell cell4=row.getCell(refe.getCol());
		excelInfo.setPactMoney(getBigDecimal( getCellFormatValue(cell4)));//合同金额

		refe=new CellReference("M7");
		row=sheet.getRow(refe.getRow());
		Cell cell5=row.getCell(refe.getCol());
		excelInfo.setRisk(getBigDecimal(getCellFormatValue(cell5)));//风险金

		refe=new CellReference("M8");
		row=sheet.getRow(refe.getRow());
		Cell cell6=row.getCell(refe.getCol());
		excelInfo.setReferRate(getBigDecimal(getCellFormatValue(cell6)));//咨询费

		refe=new CellReference("M9");
		row=sheet.getRow(refe.getRow());
		Cell cell19=row.getCell(refe.getCol());
		excelInfo.setEvalRate(getBigDecimal(getCellFormatValue(cell19)));//审核费

		refe=new CellReference("M10");
		row=sheet.getRow(refe.getRow());
		Cell cell7=row.getCell(refe.getCol());
		excelInfo.setManageRate(getBigDecimal(getCellFormatValue(cell7)));//管理费

		refe=new CellReference("M11");
		row=sheet.getRow(refe.getRow());
		Cell cel21=row.getCell(refe.getCol());
		excelInfo.setManageRateForPartyC(getBigDecimal(getCellFormatValue(cel21)));//管理费

		refe=new CellReference("F4");//E4
		row=sheet.getRow(refe.getRow());
		Cell cellRateem=row.getCell(refe.getCol());
		excelInfo.setRateem(getBigDecimal(getCellFormatValue(cellRateem)));//月利率

		refe=new CellReference("I4");//H4
		row=sheet.getRow(refe.getRow());
		Cell cellRateey=row.getCell(refe.getCol());
		excelInfo.setRateey(getBigDecimal(getCellFormatValue(cellRateey)));//年利率

		//读取还款计划
		List<LoanRepaymentDetail> list =new ArrayList<LoanRepaymentDetail>();
		for (int i = 8; i < prodCreditProductTerm.getTerm()+8; i++) {
			LoanRepaymentDetail loanRepaymentDetail = new LoanRepaymentDetail();

			refe=new CellReference("E"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell8=row.getCell(refe.getCol());
			loanRepaymentDetail.setCurrentAccrual(getBigDecimal(getCellFormatValue(cell8)));//当期利息

			refe=new CellReference("D"+(i+1));
			row=sheet.getRow(refe.getRow());
			Cell cell9=row.getCell(refe.getCol());
			if(i-7==prodCreditProductTerm.getTerm()){//最后一期
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(0));// 本金余额
			}else{
				loanRepaymentDetail.setPrincipalBalance(getBigDecimal(getCellFormatValue(cell9)));// 本金余额
			}

			refe=new CellReference("I"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell10=row.getCell(refe.getCol());
			loanRepaymentDetail.setGiveBackRate(getBigDecimal(getCellFormatValue(cell10)));// 退费

			refe=new CellReference("H"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell11=row.getCell(refe.getCol());
			loanRepaymentDetail.setPenalty(getBigDecimal(getCellFormatValue(cell11)));// 违约金

			refe=new CellReference("G"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell12=row.getCell(refe.getCol());
			loanRepaymentDetail.setRepaymentAll(getBigDecimal(getCellFormatValue(cell12)));// 一次性还款金额

			refe=new CellReference("C"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell13=row.getCell(refe.getCol());
			loanRepaymentDetail.setReturneterm(getBigDecimal(getCellFormatValue(cell13)));//每期还款金额
			loanRepaymentDetail.setDeficit(getBigDecimal(getCellFormatValue(cell13)));//剩余欠款

			refe=new CellReference("A"+i);
			row=sheet.getRow(refe.getRow());
			Cell cell20=row.getCell(refe.getCol());
			loanRepaymentDetail.setReturnDate((Date) getCellFormatValue(cell20));//固定还款日期
			loanRepaymentDetail.setPenaltyDate((Date) getCellFormatValue(cell20));//罚息起算日期

			list.add(loanRepaymentDetail);
		}
		excelInfo.setExcelDetailList(list);
		return excelInfo;
	}
	/**
	 * 获取单元格值
	 * @param cell
	 * @return
	 */
	private Object getCellFormatValue(Cell cell) {
		Object cellvalue = "";
		CellValue cellValue = eval.evaluate(cell);

		if (cellValue != null) {
			// 判断当前Cell的Type
			switch (cellValue.getCellType()) {

			case Cell.CELL_TYPE_NUMERIC: {// 数值型
				if (HSSFDateUtil.isCellDateFormatted(cell)) {//日期
					cellvalue=Dates.format(HSSFDateUtil.getJavaDate(cellValue.getNumberValue()), "yyyy/MM/dd") ;
				}else{
					cellvalue = String.valueOf(cellValue.getNumberValue());					
				}
				break;
			}

			case Cell.CELL_TYPE_FORMULA: {// 公式类型
				cellvalue = String.valueOf(cellValue.getNumberValue());
				break;
			}
			case Cell.CELL_TYPE_STRING: {// 如果当前Cell的Type为字符型
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			}

			default:// 默认的Cell值
				cellvalue = cell.getStringCellValue();
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;
	}
	/**
	 * 转换
	 * @param value
	 * @return
	 */
	public BigDecimal getBigDecimal( Object value ) {  
        BigDecimal ret = null;  
        if( value != null ) {  
            if( value instanceof BigDecimal ) {  
                ret = (BigDecimal) value;  
            } else if( value instanceof String ) {  
                ret = new BigDecimal( (String) value );  
            } else if( value instanceof BigInteger ) {
                ret = new BigDecimal( (BigInteger) value );  
            } else if( value instanceof Number ) {  
                ret = new BigDecimal( ((Number)value).doubleValue()); 
            } else {  
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");  
            }  
        }  
        return ret;  
    }  
}
