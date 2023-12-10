package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Detail {
    private String name;
    private String productName;
    private double height;
    private double width;
    private double thickness;
    private int amount;
    private double upBand;
    private double downBand;
    private double leftBand;
    private double rightBand;
    private int multiplicity = 1;
    private String material;
    private String note;
    private List<Hole> frontHoles;
    private List<Hole> backHoles;
    private List<Hole> leftHoles;
    private List<Hole> rightHoles;
    private List<Hole> upHoles;
    private List<Hole> downHoles;
}
