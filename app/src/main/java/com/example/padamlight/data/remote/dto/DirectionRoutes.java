package com.example.padamlight.data.remote.dto;

public class DirectionRoutes {
    private String summary;
    private String copyrights;
    private DirectionRoutesLegs[] legs;
    private DirectionRoutesBounds bounds;
    private DirectionRoutesOverview_polyline overview_polyline;

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCopyrights() {
        return this.copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public DirectionRoutesLegs[] getLegs() {
        return this.legs;
    }

    public void setLegs(DirectionRoutesLegs[] legs) {
        this.legs = legs;
    }

    public DirectionRoutesBounds getBounds() {
        return this.bounds;
    }

    public void setBounds(DirectionRoutesBounds bounds) {
        this.bounds = bounds;
    }

    public DirectionRoutesOverview_polyline getOverview_polyline() {
        return this.overview_polyline;
    }

    public void setOverview_polyline(DirectionRoutesOverview_polyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }
}
