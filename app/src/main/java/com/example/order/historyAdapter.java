package com.example.order;

public class historyAdapter {

    public String name, category, description, status, approval, request;

    public historyAdapter(String name, String category, String description, String status, String approval, String request) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.status = status;
        this.approval = approval;
        this.request = request;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
