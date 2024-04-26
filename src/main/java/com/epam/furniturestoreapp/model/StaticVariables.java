package com.epam.furniturestoreapp.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StaticVariables {
    public static final String TH_ACTION_FOR_ALL_PRODUCTS = "/products";
    public static final String TH_ACTION_FOR_PRODUCTS_BY_CATEGORY = "/products/category/";

    public static final String FIRST_ADDED = "First added";
    public static final String LAST_ADDED = "Last added";
    public static final String BY_RATING = "By rating";
    public static final String AZ = "A-Z";
    public static final String ZA = "Z-A";
    public static final String FROM_LOWEST_PRICE = "From lowest price";
    public static final String FROM_HIGHEST_PRICE = "From highest price";

    public static final List<String> FILTER_LIST;
    public static final Map<Color, String> COLOR_MAP;
    public static final List<Material> MATERIAL_LIST;

    static {
        FILTER_LIST = new ArrayList<>();
        FILTER_LIST.add(FIRST_ADDED);
        FILTER_LIST.add(LAST_ADDED);
        FILTER_LIST.add(BY_RATING);
        FILTER_LIST.add(AZ);
        FILTER_LIST.add(ZA);
        FILTER_LIST.add(FROM_LOWEST_PRICE);
        FILTER_LIST.add(FROM_HIGHEST_PRICE);
    }

    static {
        COLOR_MAP = new LinkedHashMap<>();
        COLOR_MAP.put(Color.GREY, "background-color:#adadad;");
        COLOR_MAP.put(Color.WHITE, "background-color:#f9f9f9;");
        COLOR_MAP.put(Color.BLUE, "background-color:#0b5fb5;");
        COLOR_MAP.put(Color.GREEN, "background-color:#00a651;");
        COLOR_MAP.put(Color.YELLOW, "background-color:#f7ff01;");
        COLOR_MAP.put(Color.RED, "background-color:#ff0000;");
        COLOR_MAP.put(Color.BLACK, "background-color:#000000;");
        COLOR_MAP.put(Color.BROWN, "background-color:#65270c;");
    }

    static {
        MATERIAL_LIST = new ArrayList<>();
        MATERIAL_LIST.add(Material.WOOD);
        MATERIAL_LIST.add(Material.METAL);
        MATERIAL_LIST.add(Material.GLASS);
        MATERIAL_LIST.add(Material.LEATHER);
        MATERIAL_LIST.add(Material.TEXTILE);
        MATERIAL_LIST.add(Material.PVC);
    }
}
