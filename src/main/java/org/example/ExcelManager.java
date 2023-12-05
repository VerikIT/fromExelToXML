package org.example;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.Detail;
import org.example.model.Hole;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelManager {
    private static final int START_SHEET = 0;
    private static final int START_CELL = 1;
    private static final int X_CELL = 1;
    private static final int Y_CELL = 2;
    private static final int DIA_CELL = 3;
    private static final String SPLITERATOR = "<HOLE-DEPTH>";

    public static List<Detail> readProductDetailsFromFile(File prodFile) throws IOException {
        List<Detail> details = new ArrayList<>();
        try (Workbook workBook = new XSSFWorkbook(prodFile)) {
            Sheet sheet = workBook.getSheetAt(START_SHEET);
            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Detail detail = new Detail();
                Row row = sheet.getRow(index);
                for (int cellIndex = START_CELL; cellIndex < row.getLastCellNum(); cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    switch (cellIndex) {
                        case 1 -> detail.setProductName(getStringCellValue(cell));
                        case 2 -> detail.setName(getStringCellValue(cell));
                        case 3 -> detail.setHeight(getDoubleCellValue(cell));
                        case 4 -> detail.setWidth(getDoubleCellValue(cell));
                        case 6 -> detail.setAmount((int) getDoubleCellValue(cell));

                    }
                }
                details.add(detail);
            }
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        return details;
    }

    private static String getStringCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        if (cellType.equals(CellType.STRING)) {
            return cell.getStringCellValue();
        }
        return null;
    }

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
                        case X_CELL -> hole.setDimX(getDoubleCellValue(cell));
                        case Y_CELL -> hole.setDimY(getDoubleCellValue(cell));
                        case DIA_CELL -> addDiaAndDeep(cell, hole);
                    }
                }
                holes.add(hole);
            }
        }
        return holes;
    }

    /*    private static void addDiaAndDeep(Cell cell, Hole hole) {
            String value = cell.getStringCellValue().replace(',', '.');
            String[] values = value.split("x");
            double dia = Double.parseDouble(values[0]
                    .substring(1));
            if (dia == 18.0) {
                hole.setDiameter(20.0);
            } else {
                hole.setDiameter(dia);
            }
            hole.setDeep(Double.parseDouble(values[1]));
        }*/
    private static void addDiaAndDeep(Cell cell, Hole hole) {
        String value = cell.getStringCellValue().replace(',', '.');
        String[] values = value.split(SPLITERATOR);
        if (values.length == 2) {
            double dia = Double.parseDouble(values[0]
                    .substring(10));
            if (dia == 18.0) {
                hole.setDiameter(20.0);
            } else {
                hole.setDiameter(dia);
            }
            hole.setDeep(Double.parseDouble(values[1]));
        } else {

            if (values[0].contains("НАСКВОЗЬ")) {
                String oneValue = values[0].replace(" НАСКВОЗЬ", "");
                double dia = Double.parseDouble(oneValue.substring(10));
                hole.setDiameter(dia);
                hole.setDeep(30.0);
            } else {
                double dia = Double.parseDouble(values[0]
                        .substring(10));
                hole.setDiameter(dia);
                hole.setDeep(34.0);
            }
        }

    }

    private static double getDoubleCellValue(Cell cell) {
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
