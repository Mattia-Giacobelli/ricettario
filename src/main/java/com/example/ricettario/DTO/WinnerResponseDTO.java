package com.example.ricettario.DTO;

public class WinnerResponseDTO {

    private Integer id;
    private String name;
    private String imageUrl;

    public WinnerResponseDTO(Integer id, String name, String imageUrl) {

        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
