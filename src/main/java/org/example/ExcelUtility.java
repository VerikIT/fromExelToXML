package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.entity.Hole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtility {
    private static final int START_SHEET = 0;
    private static final int START_CELL = 1;
    private static final int X_CELL = 1;
    private static final int Y_CELL = 2;
    private static final int DIA_CELL = 3;


    public static List<Hole> readHolesFromExcel(String filePath) throws IOException {
        List<Hole> holes = new ArrayList<>();
        try (Workbook workBook = new XSSFWorkbook(filePath)) {
            Sheet sheet = workBook.getSheetAt(START_SHEET);
            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Hole hole = new Hole();
                Row row = sheet.getRow(index);
                for (int cellIndex = START_CELL; cellIndex < row.getLastCellNum(); cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    switch (cellIndex) {
                        case X_CELL -> hole.setDimX(getCellValue(cell));
                        case Y_CELL -> hole.setDimY(getCellValue(cell));
                        case DIA_CELL -> addDiaAndDeep(cell, hole);
                    }
                }
                holes.add(hole);
            }
        } catch (IOException e) {
            throw e;
        }
        return holes;
    }

    private static void addDiaAndDeep(Cell cell, Hole hole) {
        String value = cell.getStringCellValue().replace(',', '.');
        String[] values = value.split("x");
        hole.setDiameter(Double.parseDouble(
                values[0].substring(1)));
        hole.setDeep(Double.parseDouble(values[1]));
    }

    public static double getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        if (cellType.equals(CellType.STRING)) {
            String value = cell.getStringCellValue().replace(',', '.');
            return Double.parseDouble(value);
        } else if (cellType.equals(CellType.NUMERIC)) {
            return cell.getNumericCellValue();
        }
        return 0;
    }
}
