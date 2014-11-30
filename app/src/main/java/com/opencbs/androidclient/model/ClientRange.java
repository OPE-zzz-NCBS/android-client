package com.opencbs.androidclient.model;

public class ClientRange {
    public int from;
    public int to;

    @Override
    public String toString() {
        return String.format("clients=%d..%d", from, to);
    }
}
