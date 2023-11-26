package org.example;

import org.example.model.Detail;
import org.example.model.Hole;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class Converter {
    private static final String START_TEXT = """
             <?xml version="1.0" encoding="UTF-8"?>
             <project>
               <viyar version="27">
                 <constructor id="dsp"/>
                 <materials>
                   <material id="1" type="sheet" width="2800" height="2070" thickness="18" isCliental="0">
                     <parts/>
                   </material>
                   <material id="2" type="band" height="22" thickness="0.4" markingColor="rgb(255,0,0)"/>
                   <material id="3" type="band" height="23" thickness="2" markingColor="rgb(0,0,255)"/>
                   <material id="4" type="band" height="43" thickness="2" markingColor="rgb(0,191,255)"/>
                 </materials>
                 <details>
            """;
    private static final String DETAIL_TEXT = """
                  <detail id="%d" material="1" amount="%d" widthFull="%.1f" heightFull="%.1f" description="%s, %s" multiplicity="%d">
                    <edges joint="0">
                      <left type="" param="0"/>
                      <top type="kromka" param="3"/>
                      <right type="" param="0"/>
                      <bottom type="kromka" param="2"/>
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

    private static StringBuilder getXmlByDetails(List<Detail> detailList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(START_TEXT);
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
                    detail.getWidth(),
                    detail.getHeight(),
                    detail.getName(),
                    detail.getProductName(),
                    detail.getMultiplicity());

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

    public static void saveXmlByDetailsToFile(List<Detail> detailList) throws IOException {
        var stringBuilder = getXmlByDetails(detailList);
        Path savePath = Path.of("details.project");
        Files.writeString(savePath, stringBuilder);
        System.out.println("file saved successfully " + savePath.toFile().getAbsolutePath());
    }

}
