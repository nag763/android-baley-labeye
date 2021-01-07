package com.ticandroid.baley_labeye.beans;

import com.google.firebase.Timestamp;

/**
 * Visit bean used to modelise a visit in our classes.
 *
 * @author Baley
 * @author Labeye
 */
public class VisitBean {

    /** Name of the museum. **/
    private String nomDuMusee;
    /** Date of the visit. **/
    private Timestamp date;

    private double distance;
    private double evaluation;



    public VisitBean() {
    }

    public String getNomDuMusee() {
        return nomDuMusee;
    }

    public void setNomDuMusee(String nomMusee) {
        this.nomDuMusee = nomMusee;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

}
