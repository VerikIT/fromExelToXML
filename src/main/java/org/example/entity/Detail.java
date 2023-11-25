package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Detail {
    private String name;
    private double length;
    private double width;
    private int amount;
    private int multiplicity = 1;
    private List<Hole> frontHoles = new ArrayList<>();
    private List<Hole> backHoles = new ArrayList<>();
    private List<Hole> leftHoles = new ArrayList<>();
    private List<Hole> rightHoles = new ArrayList<>();
    private List<Hole> upHoles = new ArrayList<>();
    private List<Hole> downHoles = new ArrayList<>();


}
