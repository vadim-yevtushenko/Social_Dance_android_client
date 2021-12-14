package com.example.socialdance.model.enums;

public enum Dances {
    SALSA("Salsa"),
    BACHATA("Bachata"),
    KIZOMBA("Kizomba"),
    ZOUK("Zouk"),
    MAMBO("Mambo"),
    MERENGE("Merenge"),
    REGGAETON("Reggaeton"),
    TANGO("Argentine Tango");

    private String name;

    Dances(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
