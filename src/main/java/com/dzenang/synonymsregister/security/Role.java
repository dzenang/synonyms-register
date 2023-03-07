package com.dzenang.synonymsregister.security;

public enum Role {
    WRITER("writer"),
    READER("reader");

    Role(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
