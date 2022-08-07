package com.openclassrooms.realestatemanager.data.model.entities;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaceType {
    public static ArrayList<String> types = new ArrayList<>(Arrays.asList(
            "park",
            "school",
            "store",
            "university",
            "subway_station",
            "train_station",
            "bus_station",
            "supermarket",
            "movie_theater"));
}
