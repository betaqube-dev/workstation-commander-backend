package dev.betaqube.wc.auth;

public record AuthResponse(String token, String name, String email, String role) {
}
