package com.tavares.recommender.service;

public enum MusicType {
    ALBUMS, SONGS;

    public String getString() {
        return this.name().toLowerCase();
    }

    public static MusicType getMediaType(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return MusicType.SONGS;
        }
    }
}
