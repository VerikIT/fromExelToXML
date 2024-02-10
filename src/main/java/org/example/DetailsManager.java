package org.example;

import org.example.model.Detail;
import org.example.model.Hole;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsManager {
    private static final String FRONT = "ф";
    private static final String BACK = "т";
    private static final String LEFT = "л";
    private static final String RIGHT = "п";
    private static final String UP = "в";
    private static final String DOWN = "н";

    public static void createProjectsAndAllDetailTable(String objectPath) throws IOException {
        var materialsMap = getMaterialsMap(objectPath);
        Converter.saveXmlByDetailsToFile(materialsMap, objectPath);
        var allDetails = materialsMap.values().stream()
                .flatMap(List::stream)
                .toList();
        String excelFilePath = ExcelManager.CreateFileWithAllDetails(objectPath, allDetails);
        System.out.println("file saved successfully: " + excelFilePath);
    }

    public static Map<String, List<Detail>> getMaterialsMap(String objectPath) throws IOException {
        Map<String, List<Detail>> materials = new HashMap<>();
        var prodPathList = Files.list(Path.of(objectPath))
                .filter(path -> path.toFile().isDirectory())
                .toList();
        for (var prodPath : prodPathList) {
            var details = getDetails(prodPath.toString());
            for (var detail : details) {
                String materialName = detail.getMaterial() + " " + detail.getThickness() + "мм";
                if (materials.containsKey(materialName)) {
                    var thisMatList = materials.get(materialName);
                    thisMatList.add(detail);
                } else {
                    materials.put(materialName, new ArrayList<>());
                    var thisMatList = materials.get(materialName);
                    thisMatList.add(detail);
                }
            }
        }
        return materials;
    }

    public static List<Detail> getDetails(String prodPath) throws IOException {

        File prodFile = getProductFile(prodPath);
        List<Detail> details = ExcelManager.readProductDetailsFromFile(prodFile);
        var detailsPathList = Files.list(Path.of(prodPath))
                .filter(path -> path.toFile().isDirectory())
                .toList();
        for (var detail : details) {
            String detNameWithoutDot;
            int dotIndex = detail.getName().lastIndexOf(".");
            if (dotIndex ==detail.getName().length()-1) {
                detNameWithoutDot = detail.getName().substring(0, dotIndex);
            } else {
                detNameWithoutDot = detail.getName();
            }

            var detPath = detailsPathList.stream()
                    .filter(path -> detNameWithoutDot.equalsIgnoreCase(path.toFile().getName()))
                    .findFirst()
                    .orElse(null);

            if (detPath == null) {
                continue;
            }

            var sidePathList = Files.list(detPath).toList();
            if (sidePathList.isEmpty()) {
                continue;
            }
            for (var sidePath : sidePathList) {
                String side = sidePath
                        .toFile()
                        .getName()
                        .replaceFirst(".xlsx", "")
                        .toLowerCase();
                var holes = ExcelManager.readHolesFromExcel(sidePath.toString(), side.equalsIgnoreCase(BACK));
                addHolesToSideDetail(detail, holes, side);
            }
        }
        return details;
    }


    private static File getProductFile(String prodPath) throws IOException {
        return Files.list(Path.of(prodPath))
                .filter(path -> path.toFile().isFile())
                .findFirst()
                .get()
                .toFile();
    }

    private static void addHolesToSideDetail(Detail detail, List<Hole> holes, String side) {
        switch (side) {
            case FRONT -> detail.setFrontHoles(holes);
            case BACK -> detail.setBackHoles(holes);
            case LEFT -> detail.setLeftHoles(holes);
            case RIGHT -> detail.setRightHoles(holes);
            case UP -> detail.setUpHoles(holes);
            case DOWN -> detail.setDownHoles(holes);
        }
    }
}
