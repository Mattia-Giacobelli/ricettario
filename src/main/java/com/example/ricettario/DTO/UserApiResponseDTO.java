package com.example.ricettario.DTO;

public class UserApiResponseDTO {

    private String token;
    private String username;
    private String permission;

    public UserApiResponseDTO(String token, String username, String permission) {

        this.token = token;
        this.username = username;
        this.permission = permission;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}
