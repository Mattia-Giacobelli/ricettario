package com.example.ricettario.DTO;

public class IngredientResponseDTO {

    private String name;
    private String quantity;
    private String unit;
    private String notes;

    public IngredientResponseDTO(String name, String quantity, String unit, String notes) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}