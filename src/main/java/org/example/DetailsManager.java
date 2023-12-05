package org.example;

import org.example.model.Detail;
import org.example.model.Hole;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DetailsManager {
    private static final String FRONT = "ф";
    private static final String BACK = "т";
    private static final String LEFT = "л";
    private static final String RIGHT = "п";
    private static final String UP = "в";
    private static final String DOWN = "н";

    public static List<Detail> getDetails(String prodPath) throws IOException {

        File prodFile = getProductPath(prodPath);
        List<Detail> details = ExcelManager.readProductDetailsFromFile(prodFile);
        var detailsPathList = Files.list(Path.of(prodPath))
                .filter(path -> path.toFile().isDirectory())
                .toList();

        for (var detail : details) {
            var detPath = detailsPathList.stream()
                    .filter(path -> detail.getName().contains(path.toFile().getName()))
                    .findFirst()
                    .get();

            var sidePathList = Files.list(detPath).toList();
            if (sidePathList.isEmpty()) {
                continue;
            }
            for (var sidePath : sidePathList) {
                var holes = ExcelManager.readHolesFromExcel(sidePath.toString());
                addHolesToSideDetail(detail, holes, sidePath);
            }
        }
        return details;
    }


    private static File getProductPath(String prodPath) throws IOException {
        return Files.list(Path.of(prodPath))
                .filter(path -> path.toFile().isFile())
                .findFirst().get()
                .toFile();
    }

    private static void addHolesToSideDetail(Detail detail, List<Hole> holes, Path sidePath) {
        String side = sidePath
                .toFile()
                .getName()
                .replaceFirst(".xlsx", "")
                .toLowerCase();
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
