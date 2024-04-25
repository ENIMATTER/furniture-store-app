package com.epam.furniturestoreapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltersDto {
    private String filter;
    private Double from;
    private Double to;
    private Color color;
    private Material[] materials;
}
