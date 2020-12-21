package com.ticandroid.baley_labeye.beans;

import com.google.firebase.Timestamp;

public class VisitBean {

    private String nomMusee;
    private Timestamp date;

    public VisitBean() {
    }

    public String getNomMusee() {
        return nomMusee;
    }

    public void setNomMusee(String nomMusee) {
        this.nomMusee = nomMusee;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
