package com.url.shortener.model;

public enum Tier {
    TIER1(1000),
    TIER2(100);

    private final int limit;

    Tier(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return this.limit;
    }
}
