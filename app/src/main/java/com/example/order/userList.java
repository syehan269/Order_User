package com.example.order;

public class userList {
    public String userName, email, pass, depart, level;

    public userList(String userName, String email, String pass, String Category, String level) {
        this.userName = userName;
        this.email = email;
        this.pass = pass;
        this.depart = Category;
        this.level = level;
    }

    public userList(){
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
