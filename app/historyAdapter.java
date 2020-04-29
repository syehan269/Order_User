package com.example.order;

public class historyAdapter {

    public String name, category, description;

    public historyAdapter(String name, String category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public historyAdapter() {
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getcategory() {
        return category;
    }

    public void setcategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
