package com.ticandroid.baley_labeye.beans;

import com.google.firebase.Timestamp;

import static com.ticandroid.baley_labeye.utils.Setters.valueOrDefaultDouble;

/**
 * Visit bean used to modelise a visit in our classes.
 *
 * @author Labeye
 */
public class VisitBean {

    /**
     * Name of the museum.
     **/
    private String nomDuMusee;
    /**
     * Date of the visit.
     **/
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

    public void setDistance(Object distance) {
        this.distance = valueOrDefaultDouble(distance);
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Object evaluation) {
        this.evaluation = valueOrDefaultDouble(evaluation);
    }

}
