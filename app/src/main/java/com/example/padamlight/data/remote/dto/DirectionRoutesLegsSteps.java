package com.example.padamlight.data.remote.dto;

public class DirectionRoutesLegsSteps {
    private DirectionRoutesLegsStepsDuration duration;
    private DirectionRoutesLegsStepsStart_location start_location;
    private DirectionRoutesLegsStepsDistance distance;
    private String travel_mode;
    private String html_instructions;
    private DirectionRoutesLegsStepsEnd_location end_location;
    private DirectionRoutesLegsStepsPolyline polyline;

    public DirectionRoutesLegsStepsDuration getDuration() {
        return this.duration;
    }

    public void setDuration(DirectionRoutesLegsStepsDuration duration) {
        this.duration = duration;
    }

    public DirectionRoutesLegsStepsStart_location getStart_location() {
        return this.start_location;
    }

    public void setStart_location(DirectionRoutesLegsStepsStart_location start_location) {
        this.start_location = start_location;
    }

    public DirectionRoutesLegsStepsDistance getDistance() {
        return this.distance;
    }

    public void setDistance(DirectionRoutesLegsStepsDistance distance) {
        this.distance = distance;
    }

    public String getTravel_mode() {
        return this.travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public String getHtml_instructions() {
        return this.html_instructions;
    }

    public void setHtml_instructions(String html_instructions) {
        this.html_instructions = html_instructions;
    }

    public DirectionRoutesLegsStepsEnd_location getEnd_location() {
        return this.end_location;
    }

    public void setEnd_location(DirectionRoutesLegsStepsEnd_location end_location) {
        this.end_location = end_location;
    }

    public DirectionRoutesLegsStepsPolyline getPolyline() {
        return this.polyline;
    }

    public void setPolyline(DirectionRoutesLegsStepsPolyline polyline) {
        this.polyline = polyline;
    }
}
