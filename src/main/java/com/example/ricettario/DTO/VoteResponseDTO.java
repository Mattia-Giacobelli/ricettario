package com.example.ricettario.DTO;

public class VoteResponseDTO {

    private String recipeName;

    private String username;

    public VoteResponseDTO(String recipeName, String username) {

        this.recipeName = recipeName;
        this.username = username;

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeName() {
        return recipeName;
    }

}
