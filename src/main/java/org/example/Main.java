package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    private static final Scanner SCANNER=new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        System.out.println("введіть папку з деталями:");
        System.out.println("-------------------------");
//        String path = "D:\\OneDrive\\Java\\XLStoXML\\excel\\1-F.xlsx";
//        var holes = ExcelUtility.readHolesFromExcel(path);
        //        System.out.print(holes);
        String path = "D:\\OneDrive\\Java\\XLStoXML\\excel\\H-1";
//        String path = SCANNER.nextLine();
      var details=  FileManager.getDetails(path);
        System.out.println("ok");
    }
}