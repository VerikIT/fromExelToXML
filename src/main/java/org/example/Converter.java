package org.example;

import org.example.model.Detail;
import org.example.model.Hole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Converter {
    private static final String START_TEXT = """
             <?xml version="1.0" encoding="UTF-8"?>
             <project>
               <viyar version="27">
                 <constructor id="dsp"/>
                 <materials>
                   <material id="1" type="sheet" width="2800" height="2070" thickness="%f" isCliental="0">
                     <parts/>
                   </material>
                   <material id="2" type="band" height="22" thickness="0.4"/>
                   <material id="3" type="band" height="23" thickness="1"/>
                   <material id="4" type="band" height="23" thickness="2"/>
                   <material id="5" type="band" height="43" thickness="2"/>
                 </materials>
                 <details>
            """;
    private static final String DETAIL_TEXT = """
                  <detail id="%d" material="1" amount="%d" widthFull="%.1f" heightFull="%.1f" description="%s, %s" multiplicity="%d"  grain="1">
                    <edges joint="0">
                      <left type="kromka" param="%d"/>
                      <top type="kromka" param="%d"/>
                      <right type="kromka" param="%d"/>
                      <bottom type="kromka" param="%d"/>
            """;
    private static final String BETWEEN_DET_OP = """
                    </edges>
                    <operations>
            """;
    private static final String OPERATION_TEXT = """
                      <operation id="" type="drilling" side="%d" xo="%.1f" yo="%.1f" d="%.1f" depth="%.1f"/>
            """;
    private static final String BETWEEN_DETAILS = """
                    </operations>
                  </detail>
            """;
    private static final String FINISH_TEXT = """
                </details>
                <products/>
              </viyar>
            </project>
            """;

    public static void saveXmlByDetailsToFile(Map<String, List<Detail>> materials, String objectPath) throws IOException {
        for (var material : materials.keySet()) {
            var detailList = materials.get(material);
            var stringBuilder = getXmlByDetails(detailList);
            Path savePath = Path.of(objectPath+"\\"+material + ".project");
            Files.writeString(savePath, stringBuilder);
            System.out.println("file saved successfully: " + savePath.toFile().getAbsolutePath());
        }
    }

    private static StringBuilder getXmlByDetails(List<Detail> detailList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                String.format(Locale.US, START_TEXT,
                        detailList.get(0).getThickness()));
        addDetailsToBuilder(detailList, stringBuilder);
        stringBuilder.append(FINISH_TEXT);
        return stringBuilder;
    }

    private static void addDetailsToBuilder(List<Detail> detailList, StringBuilder stringBuilder) {
        int detId = 1;
        for (var detail : detailList) {
            String detString = String.format(Locale.US, DETAIL_TEXT,
                    detId,
                    detail.getAmount(),
                    detail.getHeight(),
                    detail.getWidth(),
                    detail.getName(),
                    detail.getProductName(),
                    detail.getMultiplicity(),
                    findIdBand(detail, detail.getLeftBand()),
                    findIdBand(detail, detail.getUpBand()),
                    findIdBand(detail, detail.getRightBand()),
                    findIdBand(detail, detail.getDownBand())
            );

            stringBuilder.append(detString);
            stringBuilder.append(BETWEEN_DET_OP);

            addOperationsToDetail(1, detail.getFrontHoles(), stringBuilder);
            addOperationsToDetail(6, detail.getBackHoles(), stringBuilder);
            addOperationsToDetail(3, detail.getUpHoles(), stringBuilder);
            addOperationsToDetail(4, detail.getRightHoles(), stringBuilder);
            addOperationsToDetail(5, detail.getDownHoles(), stringBuilder);
            addOperationsToDetail(2, detail.getLeftHoles(), stringBuilder);
            stringBuilder.append(BETWEEN_DETAILS);
            detId++;
        }
    }

    private static int findIdBand(Detail detail, double thicknessBand) {
        final int WITHOUT = 0;
        if (thicknessBand == 0.0) {
            return WITHOUT;
        }
        final int UNTIL_06 = 2;
        final int UNTIL_1 = 3;
        final int UNTIL_2 = 4;
        final int MULTY_2_DET = 5;
        String note = detail.getNote();
        if (note!=null&&note.toLowerCase().contains("сращ")) {
            return MULTY_2_DET;
        }
        if (thicknessBand <= 0.6) {
            return UNTIL_06;
        } else if (thicknessBand <= 1.0) {
            return UNTIL_1;
        } else if (thicknessBand <= 2.0) {
            return UNTIL_2;
        }
        return WITHOUT;
    }

    private static void addOperationsToDetail(int side, List<Hole> holes, StringBuilder stringBuilder) {
        if (holes == null) {
            return;
        }
        for (var hole : holes) {
            String holeString = String.format(Locale.US, OPERATION_TEXT,
                    side,
                    hole.getDimX(),
                    hole.getDimY(),
                    hole.getDiameter(),
                    hole.getDeep());
            stringBuilder.append(holeString);
        }
    }


}
