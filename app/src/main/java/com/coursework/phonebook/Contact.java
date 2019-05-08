package com.coursework.phonebook;

public class Contact {
    private String name;
    private String surname;
    private String phone;
    private String photoPath=".";
    private String company;


    public Contact(){

    }
    public Contact(String name, String surname, String phone, String photoPath) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.photoPath = photoPath;
    }

    public Contact(String name, String surname, String phone, String photoPath, String company) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.photoPath = photoPath;
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
    @Override
    public String toString(){
        return name+" "+surname;
    }
}
