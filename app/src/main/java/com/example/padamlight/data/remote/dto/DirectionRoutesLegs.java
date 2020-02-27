package com.example.padamlight.data.remote.dto;

public class DirectionRoutesLegs {
    private DirectionRoutesLegsDuration duration;
    private DirectionRoutesLegsStart_location start_location;
    private DirectionRoutesLegsDistance distance;
    private String start_address;
    private DirectionRoutesLegsEnd_location end_location;
    private String end_address;
    private DirectionRoutesLegsSteps[] steps;

    public DirectionRoutesLegsDuration getDuration() {
        return this.duration;
    }

    public void setDuration(DirectionRoutesLegsDuration duration) {
        this.duration = duration;
    }

    public DirectionRoutesLegsStart_location getStart_location() {
        return this.start_location;
    }

    public void setStart_location(DirectionRoutesLegsStart_location start_location) {
        this.start_location = start_location;
    }

    public DirectionRoutesLegsDistance getDistance() {
        return this.distance;
    }

    public void setDistance(DirectionRoutesLegsDistance distance) {
        this.distance = distance;
    }

    public String getStart_address() {
        return this.start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public DirectionRoutesLegsEnd_location getEnd_location() {
        return this.end_location;
    }

    public void setEnd_location(DirectionRoutesLegsEnd_location end_location) {
        this.end_location = end_location;
    }

    public String getEnd_address() {
        return this.end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public DirectionRoutesLegsSteps[] getSteps() {
        return this.steps;
    }

    public void setSteps(DirectionRoutesLegsSteps[] steps) {
        this.steps = steps;
    }
}
