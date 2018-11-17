package com.zdmoney.credit.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTest {
	
	private static XSSFWorkbook wb;
	private  static XSSFSheet sheet;

	public ExcelTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static InputStream cloneFileInput(String filepath) throws Exception {
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

			}
			inputstream.close();
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String filepath = "E:\\bsyhCalculator.xlsx";
		
		String ext = filepath.substring(filepath.lastIndexOf("."));

		
		InputStream inputstream = cloneFileInput(filepath);
		if (".xls".equals(ext)) {
//			wb = new HSSFWorkbook(inputstream);
		} else if (".xlsx".equals(ext)) {
			wb = new XSSFWorkbook(inputstream);
		} else {
			wb = null;
		}
		/*
		 * createFormulaEvaluator
		 */
		if (wb != null) {
//			eval = wb.getCreationHelper().createFormulaEvaluator();
		}
		sheet = wb.getSheetAt(0);
		
		
		
		//设置审批金额
		CellReference refe1=new CellReference("C1");
		Row row1=sheet.getRow(refe1.getRow());
		Cell cell1=row1.getCell(refe1.getCol());
		cell1.setCellValue("40000.00");
		
		//设置借款期限
		refe1=new CellReference("F1");
		row1=sheet.getRow(refe1.getRow());
		cell1=row1.getCell(refe1.getCol());
		cell1.setCellValue("36");
		
//		sheet.setForceFormulaRecalculation(true);
		
		wb.getCreationHelper()
	      .createFormulaEvaluator()
	      .evaluateAll();
		
		
		
		for (int i=0;i<12;i++) {
			CellReference refe=new CellReference("G"+(i+8));
			Row row=sheet.getRow(refe.getRow());
			Cell cell=row.getCell(refe.getCol());
			System.out.println(getCellValue(cell));
		}
		
		
		

	}
	
	public static String getCellValue(Cell cell) {
		
		String cellValue = "";
		// 判断当前Cell的Type
		switch (cell.getCellType()) {

		case Cell.CELL_TYPE_NUMERIC: {// 数值型
			cellValue = String.valueOf(cell.getNumericCellValue());					
			break;
		}

		case Cell.CELL_TYPE_FORMULA: {// 公式类型
//			cell.setCellFormula(formula);();
			cellValue = String.valueOf(cell.getNumericCellValue());
			break;
		}
		case Cell.CELL_TYPE_STRING: {// 如果当前Cell的Type为字符型
			// 取得当前的Cell字符串
			cellValue = cell.getRichStringCellValue().getString();
			break;
		}

		default:// 默认的Cell值
			cellValue = cell.getStringCellValue();
		}
		return cellValue;
		
	}

}
