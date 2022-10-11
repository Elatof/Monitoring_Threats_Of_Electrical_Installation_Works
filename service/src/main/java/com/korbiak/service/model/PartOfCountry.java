package com.korbiak.service.model;

public enum PartOfCountry {
    WESTERN("western"),
    CENTRAL("central"),
    EASTERN("eastern");

    private final String value;

    PartOfCountry(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
