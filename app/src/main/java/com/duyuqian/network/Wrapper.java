package com.duyuqian.network;

import java.util.List;

public class Wrapper {
    private List<Person> data;

    public Wrapper(List<Person> data) {
        this.data = data;
    }

    public List<Person> getData() {
        return data;
    }
}
