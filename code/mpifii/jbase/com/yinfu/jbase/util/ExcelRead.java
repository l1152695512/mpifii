
package com.yinfu.jbase.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelRead {
	private HSSFWorkbook workbook;
	
	/*
	 * 读取文件路径字符串
	 */
	public void importExcel(String strfile) {
		try {
			// 获取工作薄workbook
			workbook = new HSSFWorkbook(new FileInputStream(strfile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 读取文件
	 */
	public void importExcel(File file) {
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List readRow(int sheetNumber, int rowIndex) {
		List result = new ArrayList();
		// 获得指定的sheet
		HSSFSheet sheet = workbook.getSheetAt(sheetNumber);
		// 获得sheet总行数
		int rowCount = sheet.getLastRowNum();
		if (rowCount < 1) {
			return result;
		}
		// 遍历行row
		// for (int rowIndex = rows+2; rowIndex <= rowCount; rowIndex++) {
		// 获得行对象
		HSSFRow row = sheet.getRow(rowIndex);
		if (null != row) {
			// Vector<Object> vector=new Vector<Object>();
			// 获得本行中单元格的个数
			int cellCount = row.getLastCellNum();
			// 遍历列cell
			for (short cellIndex = 0; cellIndex < cellCount; cellIndex++) {
				HSSFCell cell = row.getCell(cellIndex);
				// 获得指定单元格中的数据
				Object cellStr = this.getCellString(cell);
				// vector.add(cellStr);
				result.add(cellStr);
			}
		}
		// }
		
		return result;
	}
	
	/**
	 * 获取一个cell的数据类型
	 * 
	 * @param cell
	 * @return
	 */
	public Object getCellString(HSSFCell cell) {
		// TODO Auto-generated method stub
		Object result = null;
		if (cell != null) {
			// 单元格类型：Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
			int cellType = cell.getCellType();
			switch (cellType) {
				case HSSFCell.CELL_TYPE_STRING:
					result = cell.getRichStringCellValue().getString();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						result = cell.getDateCellValue();
					} else
						result = cell.getNumericCellValue();
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					result = cell.getNumericCellValue();
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					result = cell.getBooleanCellValue();
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					result = null;
					break;
				case HSSFCell.CELL_TYPE_ERROR:
					result = null;
					break;
				default:
					break;
			}
		}
		return result;
	}
}
