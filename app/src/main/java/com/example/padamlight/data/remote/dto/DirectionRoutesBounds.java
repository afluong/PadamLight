package com.example.padamlight.data.remote.dto;

public class DirectionRoutesBounds {
    private DirectionRoutesBoundsSouthwest southwest;
    private DirectionRoutesBoundsNortheast northeast;

    public DirectionRoutesBoundsSouthwest getSouthwest() {
        return this.southwest;
    }

    public void setSouthwest(DirectionRoutesBoundsSouthwest southwest) {
        this.southwest = southwest;
    }

    public DirectionRoutesBoundsNortheast getNortheast() {
        return this.northeast;
    }

    public void setNortheast(DirectionRoutesBoundsNortheast northeast) {
        this.northeast = northeast;
    }
}
