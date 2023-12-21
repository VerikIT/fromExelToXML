package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("Додайте повний шлях до папки з виробами, \n"+
                           "наприклад: D:\\Проекты солид\\155. Кухня(Виговського)\\в цех\\сол");
        System.out.println("-------------------------");
        String objectPath = SCANNER.nextLine();
//        String objectPath = "D:\\OneDrive\\Java\\XLStoXML\\excel\\prod";
        DetailsManager.createProjectsAndAllDetailTable(objectPath);
    }
}