package com.ez4us.shieldapp;

public class UserHelperClass {
    String  name;
    String profilePhoto;
    public UserHelperClass() {
    }
    public UserHelperClass(String name, String profilePhoto, String profession, String workplace, String age, String phone, String email) {
        this.name = name;
        this.profilePhoto = profilePhoto;
        this.profession = profession;
        this.workplace = workplace;
        this.age = age;
        this.phone = phone;
        this.email = email;
    }

    String profession;
    String workplace;
    String age;
    String phone;
    String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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
}