package com.openclassrooms.realestatemanager.data;

public class StaticMapService {
    public static String getStaticMap(String apiKey, String propertyLocation) {
        int zoomValue = 16;
        String sizeValue = "800x800";
        String mapTypeValue = "roadmap";

        String baseUrl = "https://maps.googleapis.com/";
        String request = "maps/api/staticmap?";
        String center = "center=" + propertyLocation;
        String zoom = "&zoom=" + zoomValue;
        String size = "&size=" + sizeValue;
        String mapType = "&maptype=" + mapTypeValue;
        String marker = "&markers=color:red|" + propertyLocation;
        String key = "&key=" + apiKey;
        String style = "&style=feature:poi|element:labels|visibility:off&style=feature:transit|element:labels|visibility:off&scale=5";

        return (baseUrl + request + center + zoom + size + mapType + marker + key + style);
    }
}
