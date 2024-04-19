package com.epam.furniturestoreapp.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StaticVariables {
    public static final String thActionForAllProducts = "/products";
    public static final String thActionForProductsByCategory = "/products/category/";

    public static final String lastAdded = "Last added";
    public static final String byRating = "By rating";
    public static final String AZ = "A-Z";
    public static final String ZA = "Z-A";
    public static final String fromLowestPrice = "From lowest price";
    public static final String fromHighestPrice = "From highest price";

    public static final List<String> filterList;
    public static final Map<Color, String> colorMap;
    public static final List<Material> materialList;

    static {
        filterList = new ArrayList<>();
        filterList.add(lastAdded);
        filterList.add(byRating);
        filterList.add(AZ);
        filterList.add(ZA);
        filterList.add(fromLowestPrice);
        filterList.add(fromHighestPrice);
    }

    static {
        colorMap = new LinkedHashMap<>();
        colorMap.put(Color.Grey, "background-color:#adadad;");
        colorMap.put(Color.White, "background-color:#f9f9f9;");
        colorMap.put(Color.Blue, "background-color:#0b5fb5;");
        colorMap.put(Color.Green, "background-color:#00a651;");
        colorMap.put(Color.Yellow, "background-color:#f7ff01;");
        colorMap.put(Color.Red, "background-color:#ff0000;");
        colorMap.put(Color.Black, "background-color:#000000;");
        colorMap.put(Color.Brown, "background-color:#65270c;");
    }

    static {
        materialList = new ArrayList<>();
        materialList.add(Material.Wood);
        materialList.add(Material.Metal);
        materialList.add(Material.Glass);
        materialList.add(Material.Leather);
        materialList.add(Material.Textile);
        materialList.add(Material.PVC);
    }
}
