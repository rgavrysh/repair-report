package org.repair.model;

import java.io.Serializable;

public class ObjectDimensions implements Serializable {
    private static final long serialVersionUID = 2L;

    private Double walls;
    private Double floor;
    private Double ceiling;
    private Double slopes;

    public ObjectDimensions() {
    }

    public ObjectDimensions(Double walls, Double floor, Double ceiling, Double slopes) {
        this.walls = walls;
        this.floor = floor;
        this.ceiling = ceiling;
        this.slopes = slopes;
    }

    public Double getFloor() {
        return floor;
    }

    public void setFloor(Double floor) {
        this.floor = floor;
    }

    public Double getCeiling() {
        return ceiling;
    }

    public void setCeiling(Double ceiling) {
        this.ceiling = ceiling;
    }

    public Double getWalls() {
        return walls;
    }

    public void setWalls(Double walls) {
        this.walls = walls;
    }

    public Double getSlopes() {
        return slopes;
    }

    public void setSlopes(Double slopes) {
        this.slopes = slopes;
    }
}
