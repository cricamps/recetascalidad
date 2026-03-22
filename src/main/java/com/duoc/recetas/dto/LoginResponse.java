package com.duoc.recetas.dto;

public class LoginResponse {
    private String token;
    private String username;
    private String roles;
    private long expiresIn;

    public LoginResponse(String token, String username, String roles, long expiresIn) {
        this.token = token;
        this.username = username;
        this.roles = roles;
        this.expiresIn = expiresIn;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
}
