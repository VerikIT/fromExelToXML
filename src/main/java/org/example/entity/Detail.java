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
    private List<Hole> holes = new ArrayList<>();
    public void addHole(Hole hole){
        holes.add(hole);
    }
}
