package com.ticandroid.baley_labeye.beans;

import com.google.firebase.Timestamp;

/**
 * Visit bean used to modelise a visit in our classes
 */
public class VisitBean {

    /** Name of the museum **/
    private String nomMusee;
    /** Date of the visit **/
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
