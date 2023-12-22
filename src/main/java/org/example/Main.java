package org.example;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Використання JOptionPane для введення тексту
        String objectPath = JOptionPane.showInputDialog(null,
                "Додайте повний шлях до папки з виробами, \n" +
                "наприклад: D:\\Проекты солид\\155. Кухня(Виговського)\\в цех\\сол");
        DetailsManager.createProjectsAndAllDetailTable(objectPath);
    }
}