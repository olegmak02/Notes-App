package com.notes.app.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class AuthenticationResponse {
    @JsonProperty("token")
    private String token;
}
