package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    public static void main(String[] args) throws IOException {
//        Path path = Path.of("D:\\OneDrive\\Java\\XLStoXML\\excel\\1-F.xlsx");
//        FileInputStream inputStream = Files.lines(path);
//        Workbook workbook = new XSSFWorkbook();
        String path = "D:\\OneDrive\\Java\\XLStoXML\\excel\\1-F.xlsx";
//        getCellValueByFetchingLastCachedValue(path, )
//        String text=ExcelUtility.readExcel(path);
        var holes = ExcelUtility.readHolesFromExcel(path);
        System.out.print(holes);
    }
//    public Object getCellValueByFetchingLastCachedValue(String fileLocation, String cellLocation) throws IOException {
//        Object cellValue = new Object();
//
//        FileInputStream inputStream = new FileInputStream(fileLocation);
//        Workbook workbook = new XSSFWorkbook(inputStream);
//
//        Sheet sheet = workbook.getSheetAt(0);
//
//        CellAddress cellAddress = new CellAddress(cellLocation);
//        Row row = sheet.getRow(cellAddress.getRow());
//        Cell cell = row.getCell(cellAddress.getColumn());
//
//        if (cell.getCellType() == CellType.FORMULA) {
//            cellValue = switch (cell.getCachedFormulaResultType()) {
//                case BOOLEAN -> cell.getBooleanCellValue();
//                case NUMERIC -> cell.getNumericCellValue();
//                case STRING -> cell.getStringCellValue();
//                default -> null;
//            };
//        }
//
//        workbook.close();
//        return cellValue;
//    }
}