package com.ticandroid.baley_labeye.beans;

import android.widget.ImageView;

/**
 * @author baley
 * bean profil.
 */
public class ProfilBean {
    /**
     * firstname of the user.
     */
    private String firstName;
    /**
     * lastname of the user.
     */
    private String lastName;
    /**
     * phone of the user.
     */
    private String phone;
    /**
     * town of the user.
     */
    private String town;

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    private ImageView image;


    public ProfilBean() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fistName) {
        this.firstName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
