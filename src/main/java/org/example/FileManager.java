package org.example;

import org.example.entity.Detail;
import org.example.entity.Hole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String FRONT = "ф";
    private static final String BACK = "т";
    private static final String LEFT = "л";
    private static final String RIGHT = "п";
    private static final String UP = "в";
    private static final String DOWN = "н";

    public static List<Detail> getDetails(String path) throws IOException {
        List<Detail> details = new ArrayList<>();
        var pathList = Files.list(Path.of(path)).toList();
        for (var detPath : pathList) {
            String detName = detPath.getFileName().toString();
            Detail detail = new Detail();
            detail.setName(detName);

            var sidePathList = Files.list(detPath).toList();
            for (var sidePath : sidePathList) {
                var holes = ExcelUtility.readHolesFromExcel(sidePath.toString());
                addHolesToSideDetail(detail, holes, sidePath);
            }
            details.add(detail);
        }
        return details;
    }

    private static void addHolesToSideDetail(Detail detail, List<Hole> holes, Path sidePath) {
//        String side = sidePath.getFileName().toString().toLowerCase();
        String side = sidePath.toFile()
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
