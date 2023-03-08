package com.example.security.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class JwtResponse {
    String message;
    String username;
    String accessToken;
}
