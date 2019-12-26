package com.midnightgeek.cryptography.enums;

/**
 * @author Alireza Karimi
 */

public enum CryptographyAlgorithm {
    AES("AES");
    private final String text;

    private CryptographyAlgorithm(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
