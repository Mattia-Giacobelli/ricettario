package com.example.ricettario.DTO;

public class SuggestionResponseDTO {

    private Integer id;

    private String name;

    public SuggestionResponseDTO(Integer id, String name) {

        this.id = id;
        this.name = name;

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
