package com.openclassrooms.realestatemanager.data.model.entities;

import java.util.ArrayList;
import java.util.Arrays;

public class PropertyType {
    public static ArrayList<String> types = new ArrayList<>(Arrays.asList(
            "flat",
            "loft",
            "duplex",
            "penthouse",
            "castle",
            "cabin",
            "chalet",
            "barn",
            "farmhouse"));
}
