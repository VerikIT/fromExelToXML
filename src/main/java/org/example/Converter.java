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

    public static final String CLIPPING = """
                      <operation id="" type="clipping" cutHSize="%.1f" cutHBase="%s" edgeMaterialH="%s" cutVSize="%.1f" cutVBase="%s" edgeMaterialV="%s"/>
            """;
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
                  <detail id="%d" material="1" amount="%d" widthFull="%.1f" heightFull="%.1f" description="%s, %s" multiplicity="%d"  grain="%d">
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
    private static final String GROOVING = """
                      <operation id="" type="grooving" side="1" subtype="0" x="0" y="0" width="4" depth="100" closed="1" full="1"/>
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
    private static final double MIN_DETAIL_WITH_HOLES = 70.0;
    private static final double MIN_DETAIL_SIZE = 54;

    public static void saveXmlByDetailsToFile(Map<String, List<Detail>> materials, String objectPath) throws IOException {
        for (var material : materials.keySet()) {
            var detailList = materials.get(material);
            var stringBuilder = getXmlByDetails(detailList);
            Path savePath = Path.of(objectPath + "\\" + material + ".project");
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
            boolean haveHoles = detail.getUpHoles() != null
                                || detail.getDownHoles() != null
                                || detail.getLeftHoles() != null
                                || detail.getRightHoles() != null
                                || detail.getFrontHoles() != null
                                || detail.getBackHoles() != null;
            String detString = String.format(Locale.US, DETAIL_TEXT,
                    detId,
                    detail.getAmount(),
                    (detail.getHeight() < MIN_DETAIL_SIZE || (haveHoles && detail.getHeight() < MIN_DETAIL_WITH_HOLES)) ? detail.getHeight() + 70 : detail.getHeight(),
                    (detail.getWidth() < MIN_DETAIL_SIZE || (haveHoles && detail.getWidth() < MIN_DETAIL_WITH_HOLES)) ? detail.getWidth() + 70 : detail.getWidth(),
                    detail.getName(),
                    detail.getProductName(),
                    detail.getMultiplicity(),
                    detail.getMultiplicity(),
                    findIdBand(detail, detail.getLeftBand()),
                    findIdBand(detail, detail.getUpBand()),
                    findIdBand(detail, detail.getRightBand()),
                    findIdBand(detail, detail.getDownBand())
            );

            stringBuilder.append(detString);
            stringBuilder.append(BETWEEN_DET_OP);
            addClipping(haveHoles, detail, stringBuilder);
            addOperationsToDetail(1, detail.getFrontHoles(), stringBuilder);
            addOperationsToDetail(6, detail.getBackHoles(), stringBuilder);
            addOperationsToDetail(3, detail.getUpHoles(), stringBuilder);
            addOperationsToDetail(4, detail.getRightHoles(), stringBuilder);
            addOperationsToDetail(5, detail.getDownHoles(), stringBuilder);
            addOperationsToDetail(2, detail.getLeftHoles(), stringBuilder);
            addGrooving(detail, stringBuilder);
            stringBuilder.append(BETWEEN_DETAILS);
            detId++;
        }
    }

    private static void addGrooving(Detail detail, StringBuilder stringBuilder) {

        if (detail.getNote() != null &&
            (detail.getNote().toLowerCase().contains("паз")
             || detail.getNote().toLowerCase().contains("лед")
             || detail.getNote().toLowerCase().contains("led"))) {
            stringBuilder.append(GROOVING);
        }
    }

    private static void addClipping(boolean haveHoles, Detail detail, StringBuilder stringBuilder) {
        if (detail.getHeight() >= MIN_DETAIL_WITH_HOLES && detail.getWidth() >= MIN_DETAIL_WITH_HOLES) {
            return;
        }
        double sizeH = 0.0;
        int baseH = 0;
        int edgeH = 0;
        double sizeV = 0.0;
        int baseV = 0;
        int edgeV = 0;
        if (detail.getHeight() < MIN_DETAIL_SIZE || (haveHoles && detail.getHeight() < MIN_DETAIL_WITH_HOLES)) {
            baseH = 2;
            edgeH = findIdBand(detail, detail.getRightBand());
            switch (edgeH) {
                case 0 -> sizeH = detail.getHeight();
                case 2 -> sizeH = detail.getHeight() - 0.4;
                case 3 -> sizeH = detail.getHeight() - 1.0;
                case 4, 5 -> sizeH = detail.getHeight() - 2.0;
            }
        } else if (detail.getWidth() < MIN_DETAIL_SIZE || (haveHoles && detail.getWidth() < MIN_DETAIL_WITH_HOLES)) {
            baseV = 5;
            edgeV = findIdBand(detail, detail.getUpBand());
            switch (edgeV) {
                case 0 -> sizeV = detail.getWidth();
                case 2 -> sizeV = detail.getWidth() - 0.4;
                case 3 -> sizeV = detail.getWidth() - 1.0;
                case 4, 5 -> sizeV = detail.getWidth() - 2.0;
            }
        }
        String clippingString = String.format(Locale.US, CLIPPING,
                sizeH,
                (baseH == 0) ? "" : String.valueOf(baseH),
                (edgeH == 0) ? "" : String.valueOf(edgeH),
                sizeV,
                (baseV == 0) ? "" : String.valueOf(baseV),
                (edgeV == 0) ? "" : String.valueOf(edgeV)
        );
        stringBuilder.append(clippingString);

    }

    private static int findIdBand(Detail detail, double thicknessBand) {
        final int WITHOUT = 0;
        if (thicknessBand == 0.0) {
            return WITHOUT;
        }
        final int UNTIL_06 = 2;
        final int UNTIL_1 = 3;
        final int UNTIL_2 = 4;
        final int MULTI_2_DET = 5;
        String note = detail.getNote();
        if (note != null && note.toLowerCase().contains("сращ")) {
            return MULTI_2_DET;
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
