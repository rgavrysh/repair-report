package org.repair.model;

//todo: Use this class as Embeddable for Projects dimensions
public class ObjectDimensions {
    private Double floor;
    private Double ceiling;
    private Double walls;
    private Double slopes;

    public ObjectDimensions() {
        throw new UnsupportedOperationException("Should not be called until @Embeddable support.");
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
