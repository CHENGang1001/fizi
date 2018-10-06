package com.gq.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.gq.common.log.LogManager;
public class ExcelFileUtil {
    /**
     * 日志
     */
    private static Logger logger = LogManager.getDebugLog();

    /**
     * 单元格宽度
     */
    public static final int COLUMN_WIDTH = 6000;

    /**
     * 工具类的私有构造函数
     */
    private ExcelFileUtil() {

    }

    /**
     * 讲xls写入流中
     *
     * @param os            <br />
     * @param fieldNameList <br />
     * @param fieldDataList <br />
     */
    public static void exportExcel(OutputStream os, List<String> fieldNameList, List<ArrayList<String>> fieldDataList) {
    	exportExcel(os, fieldNameList, fieldDataList,null); 
    }

    /**
     * 讲xls写入流中
     *
     * @param os            <br />
     * @param fieldNameList <br />
     * @param fieldDataList <br />
     */
    public static void exportExcel(OutputStream os, List<String> fieldNameList, List<ArrayList<String>> fieldDataList,int[] numArr) {
        try {
            HSSFWorkbook workBook = createWorkbook(fieldNameList, fieldDataList,numArr);
            workBook.write(os);
        } catch (IOException e) {
            logger.error("IOException:", e);
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("IOException:", e);
                }
            }
        }
    }
    /**
     * 讲xls写入流中
     *
     * @param os            <br />
     * @param fieldDataList <br />
     */
    public static void exportExcel(OutputStream os, ArrayList<ArrayList<String>> fieldDataList) {
        exportExcel(os, null, fieldDataList);
    }
    
    /**
     * 设置富文本样式
     * @param row
     * @param columnNum 
     * @param value
     * @param map '5,6'->Font
     */
    public static void setRishFontStyle(HSSFRow row,int columnNum,String value,Map<String,HSSFFont> map){
		HSSFCell cell = row.createCell(columnNum);
		if (!StringUtils.isEmpty(value)) {
			HSSFRichTextString richString = new HSSFRichTextString(value);  
			for(String key : map.keySet()){
				String[] tmpArr = key.split(",");
				richString.applyFont( Integer.parseInt(tmpArr[0]), Integer.parseInt(tmpArr[1]), map.get(key));  
			}
			cell.setCellValue(richString);
	    } else {
	        cell.setCellValue("");
	    }
	}
	
    /**
     * 设置默认的列值
     * @param row
     * @param columnNum
     * @param value
     */
    public static void setDefaultCellValue(HSSFRow row,int columnNum,String value){
		HSSFCell cell = row.createCell(columnNum);
		if (!StringUtils.isEmpty(value)) {
			cell.setCellValue(value);
	    } else {
	        cell.setCellValue("");
	    }
	}

    /**
     * 创建xls
     *
     * @param fieldNameList <br />
     * @param fieldDataList <br />
     * @return <br />
     * @throws IOException <br />
     */
    public static HSSFWorkbook createWorkbook(List<String> fieldNameList, List<ArrayList<String>> fieldDataList,int[] numArr) throws IOException {
    	// 创建一个工作簿
        HSSFWorkbook workBook = null;
    	if(fieldNameList == null || fieldNameList.size() <= 0){
    		InputStream inputStream = ExcelFileUtils.class.getResourceAsStream("/classes/model.xls");
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            workBook = new HSSFWorkbook(fs);
    	} else {
    		workBook = new HSSFWorkbook();
    	}
        // 创建工作表
        HSSFSheet sheet = null;
        if (null != fieldNameList) {
            sheet = workBook.createSheet("report");
            // 设置首行
            createFiledName(workBook, sheet, fieldNameList);
        } else {
            sheet = workBook.getSheet("report");
        }
        // 创建数据栏单元格并填入数据
        createFieldValue(workBook, sheet, fieldDataList,numArr);
        return workBook;
    }

    /**
     * 设置首行标题
     *
     * @param workBook      <br />
     * @param sheet         <br />
     * @param fieldNameList <br />
     */
    private static void createFiledName(HSSFWorkbook workBook, HSSFSheet sheet, List<String> fieldNameList) {
        HSSFCellStyle style = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        short color = HSSFColor.RED.index;
        font.setColor(color);
        style.setFont(font);
        // 创建标题行
        HSSFRow headRow = sheet.createRow(0);
        int columnNum = 0;
        for (String fieldName : fieldNameList) {
            // 创建抬头栏单元格
            HSSFCell cell = headRow.createCell(columnNum++);
            // 设置单元格格式
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            sheet.setColumnWidth(columnNum, COLUMN_WIDTH);
            cell.setCellStyle(style);
            // 将数据填入单元格
            cell.setCellValue(fieldName);
        }
    }

    /**
     * 写入内容
     *
     * @param workBook      <br />
     * @param sheet         <br />
     * @param fieldDataList <br />
     */
    private static void createFieldValue(HSSFWorkbook workBook, HSSFSheet sheet, List<ArrayList<String>> fieldDataList,int[] numArr) {
        int rowNum = 1; 
        HSSFCellStyle cellStyle1 = workBook.createCellStyle();
        cellStyle1.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        HSSFCellStyle cellStyle2 = workBook.createCellStyle();
        cellStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        
        // 创建数据栏单元格并填入数据
        for (ArrayList<String> fieldDatas : fieldDataList) {
            HSSFRow row = sheet.createRow(rowNum++);
            int columnNum = 0;
            for (String fieldData : fieldDatas) {
                HSSFCell cell = row.createCell(columnNum++);
                
                if( numArr!=null ){
                	if(1 == numArr[columnNum-1]){
                    	cell.setCellStyle(cellStyle1);
                    } else if (2 == numArr[columnNum-1]){
                    	cell.setCellStyle(cellStyle2);
                    }
                }
                
                setCellValue(cell,fieldData,numArr==null ? 0 : numArr[columnNum-1]);
            }
        }
    }
    
    private static void setCellValue(HSSFCell cell,String fieldData,int flag){
    	if(flag==1 || flag==2) {
    		cell.setCellValue(Double.parseDouble(StringUtils.isNullOrEmpty(fieldData) ? "0" : fieldData));
        } else {
        	cell.setCellValue(StringUtils.isNullOrEmpty(fieldData) ? "" : fieldData);
        }
    }
}
