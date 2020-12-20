package com.ticandroid.baley_labeye.beans;

import android.widget.ImageView;

public class ProfilBean {
    private String firstname;
    private String lastname;
    private String phone;
    private String town;

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    private ImageView image;


    public ProfilBean(){

    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String fistname) {
        this.firstname = fistname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
