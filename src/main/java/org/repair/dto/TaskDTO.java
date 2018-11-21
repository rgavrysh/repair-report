package org.repair.dto;

public class TaskDTO {
    private String shortDescription;
    private Double tariff;
    private Double qty;

    public TaskDTO() {
    }

    public TaskDTO(String shortDescription, Double tariff, Double qty) {
        this.shortDescription = shortDescription;
        this.tariff = tariff;
        this.qty = qty;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Double getTariff() {
        return tariff;
    }

    public void setTariff(Double tariff) {
        this.tariff = tariff;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}
