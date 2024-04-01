package com.epam.furniturestoreapp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterUtil {
    private double price;
    private Color color;
    private Material material;
    private String filter;
}