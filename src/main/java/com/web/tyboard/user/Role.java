package com.web.tyboard.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    Role(String value) {
        this.value = value;
    }

    private String value;
}