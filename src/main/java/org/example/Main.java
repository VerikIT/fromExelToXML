package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER=new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        System.out.println("Введіть повний шлях до кореневоє папки об'єкту, \n"+
                           "наприклад: D:\\Проекты солид\\155. Кухня(Виговського)\\в цех\\сол ");
        System.out.println("-------------------------");
        String objectPath = SCANNER.nextLine();
        DetailsManager.   createProjectsAndAllDetailTable(objectPath);
    }
}