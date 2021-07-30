package com.example.work_2;

public class ContacterUser {
    private String id;
    private String name;
    private String phone;
    private String email;

    public ContacterUser(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContacterUser(String name, String phone, String email, String id) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "联系人" +
                "姓名：" + name + "\n" +
                "电话：" + phone + "\n" +
                "邮箱：" + email +"\n" ;
    }
}
