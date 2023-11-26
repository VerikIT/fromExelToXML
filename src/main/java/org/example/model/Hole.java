package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hole {
    private double diameter;
    private double dimX;
    private double dimY;
    private double deep;
}
