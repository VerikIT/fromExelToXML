package org.example;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        // Використання JOptionPane для введення тексту
        String objectPath = JOptionPane.showInputDialog(null,
                "Додайте повний шлях до папки з виробами, \n" +
                "наприклад: D:\\Проекты солид\\155. Кухня(Виговського)\\в цех\\сол");
//        String objectPath = null;
//        File zipFile = findZipFile();
//        if (zipFile != null) {
//            objectPath=unzippingFile(zipFile);
//        }

        if (objectPath != null) {
            DetailsManager.createProjectsAndAllDetailTable(objectPath);
        }

    }

    private static String unzippingFile(File zipFile) throws IOException {
        String fileZip  = zipFile.getAbsolutePath();
        String unzipDir = fileZip.substring(0, fileZip.lastIndexOf(".zip"));
        File destDir = new File(unzipDir);

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip), Charset.forName("windows-1251"));
        ZipEntry zipEntry = zis.getNextEntry();
        
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        return unzipDir;
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private static File findZipFile() {
        // Отримати поточну робочу папку
        String currentDirectory = System.getProperty("user.dir");
        // Створити об'єкт класу File для поточної папки
        File directory = new File(currentDirectory);
        // Отримати список всіх файлів у поточній папці
        File[] files = directory.listFiles();
        // знайти Zip
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains(".zip")) {
                    return file;
                }

            }
        }
        return null;
    }
}