package com.example.padamlight.data.remote.dto;

public class Direction {
    private DirectionRoutes[] routes;
    private String status;

    public DirectionRoutes[] getRoutes() {
        return this.routes;
    }

    public void setRoutes(DirectionRoutes[] routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
