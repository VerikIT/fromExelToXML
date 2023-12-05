package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER=new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        System.out.println("введіть папку з деталями:");
        System.out.println("-------------------------");
//        String path = "D:\\OneDrive\\Java\\XLStoXML\\excel\\1-F.xlsx";
//        var holes = ExcelUtility.readHolesFromExcel(path);
        //        System.out.print(holes);
        String path = "D:\\OneDrive\\Java\\XLStoXML\\excel\\prod\\шк";
//        String path = SCANNER.nextLine();
      var details=  DetailsManager.getDetails(path);
//    var bilder=  Converter.getXmlByDetails(details);
//        System.out.println(bilder);
        Converter.saveXmlByDetailsToFile(details);

    }
}