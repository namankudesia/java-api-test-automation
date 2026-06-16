package com.naman.framework.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private int id;
    private String name;
    private String email;
    private String role;
    private String createdAt;
}
