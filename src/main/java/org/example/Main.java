package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER=new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        System.out.println("Введіть повний шлях до кореневоє папки об'єкту, \n"+
                           "наприклад: D:\\Проекты солид\\155. Кухня(Виговського)\\в цех\\сол ");
        System.out.println("-------------------------");
//        String path = "D:\\OneDrive\\Java\\XLStoXML\\excel\\1-F.xlsx";
//        var holes = ExcelUtility.readHolesFromExcel(path);
        //        System.out.print(holes);
//        String path = "D:\\OneDrive\\Java\\XLStoXML\\excel\\prod\\шк";
//        String objectPath = "D:\\OneDrive\\Java\\XLStoXML\\excel\\prod";
        String objectPath = SCANNER.nextLine();
        DetailsManager.   createProjectsAndAllDetailTable(objectPath);
//      var details=  DetailsManager.getDetails(path);
//    var bilder=  Converter.getXmlByDetails(details);
//        System.out.println(bilder);
//        Converter.saveXmlByDetailsToFile(details);


    }
}