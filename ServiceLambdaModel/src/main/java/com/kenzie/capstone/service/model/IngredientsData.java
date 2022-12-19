package com.kenzie.capstone.service.model;

import java.util.Objects;

public class IngredientsData {
    private String id;
    private String data;

    public IngredientsData(String id, String data) {
        this.id = id;
        this.data = data;
    }

    public IngredientsData() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IngredientsData that = (IngredientsData) o;
        return Objects.equals(id, that.id) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data);
    }
}
