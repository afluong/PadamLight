package com.example.padamlight.data.local;

public class Route {

    private String startName;

    private String endName;

    private Double startLat;

    private Double startLng;

    private Double endLat;

    private Double endLng;

    private String overviewPolyline;


    public Route(String startName, String endName, Double startLat, Double startLng, Double endLat, Double endLng,
          String overviewPolyline){
        this.startName = startName;
        this.endName = endName;
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
        this.overviewPolyline = overviewPolyline;
    }

    public String getStartName() {
        return startName;
    }

    public String getEndName() {
        return endName;
    }

    public Double getStartLat() {
        return startLat;
    }

    public Double getStartLng() {
        return startLng;
    }

    public Double getEndLat() {
        return endLat;
    }

    public Double getEndLng() {
        return endLng;
    }

    public String getOverviewPolyline() {
        return overviewPolyline;
    }


}
