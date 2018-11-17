package com.zdmoney.credit.common.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel导出组件
 * @author 00236640
 * @version $Id: ExcelImportUtil.java, v 0.1 2015年8月17日 上午11:44:13 00236640 Exp $
 */
public class ExcelExportUtil {
    
    private static final String SHEET_NAME ="sheet1";

    private static final String TYPE_XLS = "xls";

    private static final String TYPE_XLSX = "xlsx";
    
    private static final String TYPE_CSV = "csv";
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 数据写入excel文件
     * @param fileName Excel文件名
     * @param labels excel文件的表头显示名
     * @param fields excel文件的数据字段名
     * @param dataList 待写入数据
     * @param sheetName 工作表的名称
     * @return
     * @throws IOException
     */
    public static Workbook createExcelByMap(String fileName, String[] labels,String[] fields, List<Map<String, Object>> dataList,
            String sheetName) throws IOException {
        return createExcelByMap(fileName,Arrays.asList(labels),Arrays.asList(fields),dataList,sheetName);
    }
    
    public static Workbook createExcelByMapString(String fileName, String[] labels,String[] fields, List<Map<String, String>> dataList,
            String sheetName) throws IOException {
        return createExcelByMapString(fileName,Arrays.asList(labels),Arrays.asList(fields),dataList,sheetName);
    }
    /**
     * 数据写入excel文件
     * @param fileName Excel文件名
     * @param labels excel文件的表头显示名
     * @param fields excel文件的数据字段名
     * @param dataList 待写入数据
     * @param sheetName 工作表的名称
     * @return
     * @throws IOException
     */
    public static Workbook createExcelByVo(String fileName, String[] labels,String[] fields, List dataList,
            String sheetName) throws Exception {
        return createExcelByVo(fileName,Arrays.asList(labels),Arrays.asList(fields),dataList,sheetName);
    }
    
    /**
     * 数据写入excel文件
     * @param fileName Excel文件名
     * @param labels excel文件的表头显示名
     * @param fields excel文件的数据字段名
     * @param dataList 待写入数据
     * @param sheetName 工作表的名称
     * @return
     * @throws IOException
     */
    public static Workbook createExcelByMap(String fileName, List<String> labels,List<String> fields, List<Map<String, Object>> dataList,
            String sheetName) throws IOException {
        Workbook workbook = createExcelCommon(fileName, labels, fields,dataList,sheetName);
        if(null==workbook){
            return workbook;
        }
        // 工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 工作表的每一行
        Row row = null;
        // 每一行中的每一个单元格
        Cell cell = null;
        // 写数据
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> dataMap = dataList.get(i);
            // 从第二行开始创建数据行
            row = sheet.createRow(i + 1);
            // 每行单元格的索引位置
            int index = 0;
            for(String key:fields){
                Object value = dataMap.get(key);
                cell = row.createCell(index);
                eidtCell(value, cell);
                index++;
            }
        }
        return workbook;
    }
    
    public static Workbook createExcelByMapString(String fileName, List<String> labels,List<String> fields, List<Map<String, String>> dataList,
            String sheetName) throws IOException {
        Workbook workbook = createExcelCommon(fileName, labels, fields,dataList,sheetName);
        if(null==workbook){
            return workbook;
        }
        // 工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 工作表的每一行
        Row row = null;
        // 每一行中的每一个单元格
        Cell cell = null;
        // 写数据
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> dataMap = dataList.get(i);
            // 从第二行开始创建数据行
            row = sheet.createRow(i + 1);
            // 每行单元格的索引位置
            int index = 0;
            for(String key:fields){
                String value = dataMap.get(key);
                cell = row.createCell(index);
                eidtCell(value, cell);
                index++;
            }
        }
        return workbook;
    }
    /**
     * 数据写入excel文件
     * @param fileName Excel文件名
     * @param labels excel文件的表头显示名
     * @param fields excel文件的数据字段名
     * @param dataList 待写入数据
     * @param sheetName 工作表的名称
     * @return
     * @throws IOException
     */
    public static Workbook createExcelByVo(String fileName, List<String> labels,List<String> fields, List dataList,
            String sheetName) throws Exception {
        Workbook workbook = createExcelCommon(fileName, labels, fields,dataList,sheetName);
        if(null==workbook){
            return workbook;
        }
        Sheet sheet = workbook.getSheetAt(0);
        // 工作表的每一行
        Row row = null;
        // 每一行中的每一个单元格
        Cell cell = null;
        
        Field[] fieldList = null;
        // 写数据
        if(fileName.indexOf("龙信债权_供理财")>0 || fileName.indexOf("渤海债权_供理财")>0)
        {
        	// sheet.setDefaultColumnWidth(16);
        	 for (int i = 0; i < dataList.size(); i++) {
                 // 从第二行开始创建数据行
                 row = sheet.createRow(i + 4);
                 // 数据对象
                 Object bean = dataList.get(i);
                 if(i == 0){
                     // 获取数据对象的所有属性
                     fieldList = FieldUtils.getClassField(bean.getClass());
                 }
                 // 每行单元格的索引位置
                 int index = 0;
                 for(String key : fields){
                     Object value = null;
                     for(Field field : fieldList){
                    	 if(key.equals("idNum"))
                    	 {
                    		 value =i+1;
                             break; 
                    	 }
                         if(key.equals(field.getName())){
                             field.setAccessible(true);
                             value =field.get(bean);
                             break;
                         }
                     }
                     cell = row.createCell(index);
                     eidtCell(value,cell);
                     index++;
                 }
             }
        }else{
        for (int i = 0; i < dataList.size(); i++) {
            // 从第二行开始创建数据行
            row = sheet.createRow(i + 1);
            // 数据对象
            Object bean = dataList.get(i);
            if(i == 0){
                // 获取数据对象的所有属性
                fieldList = FieldUtils.getClassField(bean.getClass());
            }
            // 每行单元格的索引位置
            int index = 0;
            for(String key : fields){
                Object value = null;
                for(Field field : fieldList){
                    if(key.equals(field.getName())){
                        field.setAccessible(true);
                        value =field.get(bean);
                        break;
                    }
                }
                cell = row.createCell(index);
                eidtCell(value,cell);
                index++;
            }
        }
        }
        return workbook;
    }

    /**
     * 设置工作表的名称和数据的字段名
     * @param fileName
     * @param labels
     * @param fields
     * @param dataList
     * @param sheetName
     * @return
     * @throws IOException
     */
    private static Workbook createExcelCommon(String fileName, List<String> labels,List<String> fields,List dataList,
            String sheetName) throws IOException {
        if(!check(labels, fields,dataList)){
            return null;
        }
        // 获取文件后缀名
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);

        // 创建工作薄
        Workbook workbook = createWorkbook(extensionName);

        // 创建工作表
        Sheet sheet = workbook.createSheet();
        // 如果不指定工作薄名称、则默认设置为'sheet1'
        if (null == sheetName || "".equals(sheetName.trim())) {
            sheetName = SHEET_NAME;
        }
        // 设置工作表名
        workbook.setSheetName(0, sheetName);
        if(fileName.indexOf("龙信债权_供理财")>0 || fileName.indexOf("渤海债权_供理财")>0)
        {
        	Row row = sheet.createRow(0);
            CellStyle style = workbook.createCellStyle();
            Font font =  workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            Cell cell = null;
            cell = row.createCell(0);
            cell.setCellValue("证大投资咨询有限公司放款通知书");
            cell.setCellStyle(style);
           
            
            row=null;
            cell = null;
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue("申明：以下批次贷款我公司已按照内部审批程序完成审核工作，同意推荐发放，并同意为该笔贷款提供代偿。");
            cell.setCellStyle(style);
            
            
            row=null;
            cell = null;
            row = sheet.createRow(2);
            cell = row.createCell(11);
            cell.setCellValue("上海证大投资咨询有限公司");
            cell.setCellStyle(style);
            
            row = sheet.createRow(3);
            cell = null;
            for (int i=0; i<labels.size();i++) {
            	
                cell = row.createCell(i);
                cell.setCellValue(labels.get(i));
                cell.setCellStyle(style);
            }
        }else{
        // 工作表中创建第一行
        Row row = sheet.createRow(0);
        // 设置标题
        CellStyle style = workbook.createCellStyle();
        Font font =  workbook.createFont();
        // 设置单元格粗体
        font.setBold(true);
        style.setFont(font);
        
        Cell cell = null;
        for (int i=0; i<labels.size();i++) {
            cell = row.createCell(i);
            cell.setCellValue(labels.get(i));
            cell.setCellStyle(style);
        }
        }
        return workbook;
    }
    
    /**
     * 创建excel对象
     * @param fileName
     * @param sheetName
     * @return
     */
    public static Workbook createWorkbook(String fileName, String sheetName) {
        // 获取文件后缀名
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 创建工作薄
        Workbook workbook = createWorkbook(extensionName);
        // 创建工作表
        workbook.createSheet();
        // 如果不指定工作薄名称、则默认设置为'sheet1'
        if (null == sheetName || "".equals(sheetName.trim())) {
            sheetName = SHEET_NAME;
        }
        // 设置工作表名
        workbook.setSheetName(0, sheetName);
        return workbook;
    }
    
    /**
     * 写excel文件
     * @param workbook
     * @param labels
     * @param fields
     * @param dataList
     * @param isWriteHeader
     * @param startRow
     * @return
     * @throws Exception
     */
    public static Workbook createExcelByVo(Workbook workbook, String[] labels,
            String[] fields, List dataList, boolean isWriteHeader, int startRow)
            throws Exception {
        return createExcelByVo(workbook, Arrays.asList(labels),
                Arrays.asList(fields), dataList, isWriteHeader, startRow);
    }

    /**
     * 写excel文件
     * @param workbook
     * @param labels
     * @param fields
     * @param dataList
     * @param isWriteHeader
     * @param startRow
     * @return
     * @throws Exception
     */
    public static Workbook createExcelByVo(Workbook workbook,
            List<String> labels, List<String> fields, List dataList,
            boolean isWriteHeader, int startRow) throws Exception {
        writeHeader(workbook, labels, isWriteHeader, startRow);
        writeData(workbook, fields, dataList, isWriteHeader, startRow);
        return workbook;
    }
    
    /**
     * 写标题头部
     * @param workbook
     * @param labels
     * @param isWriteHeader
     * @param startRow
     */
    private static void writeHeader(Workbook workbook, List<String> labels, boolean isWriteHeader, int startRow) {
        // 是否写标题头部
        if(isWriteHeader){
            // 获取工作簿的sheet对象
            Sheet sheet = workbook.getSheetAt(0);
            // 工作表中创建一行
            Row row = sheet.createRow(startRow - 1);
            // 设置标题
            CellStyle style = workbook.createCellStyle();
            Font font =  workbook.createFont();
            // 设置单元格粗体
            font.setBold(true);
            style.setFont(font);
            Cell cell = null;
            for (int i=0; i<labels.size();i++) {
                cell = row.createCell(i);
                cell.setCellValue(labels.get(i));
                cell.setCellStyle(style);
            }
        }
    }

    /**
     * 写具体数据
     * @param workbook
     * @param fields
     * @param dataList
     * @param isWriteHeader
     * @param startRow
     * @throws Exception
     */
    private static void writeData(Workbook workbook, List<String> fields, List dataList,
            boolean isWriteHeader, int startRow)throws Exception {
        // 获取工作簿的sheet对象
        Sheet sheet = workbook.getSheetAt(0);
        // 工作表的每一行
        Row row = null;
        // 每一行中的每一个单元格
        Cell cell = null;
        Field[] fieldList = null;
        
        // 如果没有写标题头部，则从头部开始写
        if(!isWriteHeader){
            startRow --;
        }
        // 写数据
        for (int i = 0; i < dataList.size(); i++) {
            // 从第二行开始创建数据行
            row = sheet.createRow(i + startRow);
            // 数据对象
            Object bean = dataList.get(i);
            if (i == 0) {
                // 获取数据对象的所有属性
                fieldList = FieldUtils.getClassField(bean.getClass());
            }
            // 每行单元格的索引位置
            int index = 0;
            for (String key : fields) {
                Object value = null;
                for (Field field : fieldList) {
                    if (key.equals(field.getName())) {
                        field.setAccessible(true);
                        value = field.get(bean);
                        break;
                    }
                }
                cell = row.createCell(index);
                eidtCell(value, cell);
                index++;
            }
        }
    }

    
    /**
     * 设置每个单元格
     * @param value
     * @param cell
     */
    private static void eidtCell(Object value,Cell cell){
        if (null == value) {
            value = "";
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Date) {
            Date date = (Date) value;
            cell.setCellValue(new SimpleDateFormat(DATE_FORMAT).format(date));
        } else if (value instanceof Boolean) {
            cell.setCellValue(Boolean.valueOf(value.toString()));
        } else if (value instanceof Character || value instanceof Short || value instanceof Integer
                || value instanceof Long || value instanceof Double || value instanceof BigDecimal) {
            cell.setCellValue(Double.valueOf(value.toString()));
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

    /**
     * 创建空的Workbook对象
     * @param extensionName
     * @return
     */
    public static Workbook createWorkbook(String extensionName) {
        // 创建工作薄
        Workbook workbook = null;
        if (TYPE_XLS.equals(extensionName.toLowerCase())) {
            // 创建xls工作薄
            workbook = new HSSFWorkbook();
        } else if (TYPE_XLSX.equals(extensionName.toLowerCase())) {
            // 创建xlsx工作薄
            workbook = new XSSFWorkbook();
        }else if (TYPE_CSV.equals(extensionName.toLowerCase())) {
            workbook = new HSSFWorkbook();
        }
        return workbook;
    }
    
    /**
     * 标题、字段、及数据非空校验
     * @param labels 显示标题
     * @param fields 显示字段
     * @param dataList 传入数据
     * @return
     */
    private static boolean check(List<String> labels, List<String> fields,List dataList) {
        // 如果没有传入显示标题、则操作结束
        if (null == labels || labels.size() == 0) {
            return false;
        }
        
        // 如果没有传入显示字段、则操作结束
        if (null == fields || fields.size() == 0) {
            return false;
        }
        
        // 如果没有传入数据、则操作结束
        if (null == dataList || dataList.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 创建表格页
     * @param workbook
     * @param sheetName
     * @param labels
     * @param fields
     * @param dataList
     * @return
     * @throws Exception
     */
	public static Workbook createWorkSheetByMap(Workbook workbook,int sheetIndex,String sheetName, String[] labels, String[] fields, List<Map<String, Object>> dataList) throws Exception {
	      // 创建工作表
        Sheet sheet = workbook.createSheet();
        // 如果不指定工作薄名称、则默认设置为'sheet1'
        if (null == sheetName || "".equals(sheetName.trim())) {
            sheetName = SHEET_NAME;
        }
        // 设置工作表名
        if(sheetIndex < 0 ){
        	sheetIndex = 0;
        }
        workbook.setSheetName(sheetIndex, sheetName);
        // 工作表中创建第一行
        Row row = sheet.createRow(0);
        // 设置标题
        CellStyle style = workbook.createCellStyle();
        Font font =  workbook.createFont();
        // 设置单元格粗体
        font.setBold(true);
        style.setFont(font);
        // 每一行中的每一个单元格
        Cell cell = null;
       
        for (int i=0; i<labels.length;i++) {
            cell = row.createCell(i);
            cell.setCellValue(labels[i]);
            cell.setCellStyle(style);
        }
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> dataMap = dataList.get(i);
            // 从第二行开始创建数据行
            row = sheet.createRow(i + 1);
            // 每行单元格的索引位置
            int index = 0;
            for(String key:fields){
                Object value = dataMap.get(key);
                cell = row.createCell(index);
                eidtCell(value, cell);
                index++;
            }
        }
        return workbook;		
	}
}