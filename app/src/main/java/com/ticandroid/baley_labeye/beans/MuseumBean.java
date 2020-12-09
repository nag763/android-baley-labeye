package com.ticandroid.baley_labeye.beans;

/**
 * The museum class's purpose is to contain any important information related to the museums
 * stored in the firestore database
 *
 * @author Baley
 * @author Labeye
 */
public class MuseumBean {

    /** Adresse of the museum **/
    private String adr;
    /** Fiscales data **/
    private String coordonneesFiscales;
    /** Postal code associated **/
    private int codePostal;
    /** Date when it has ben named **/
    private String dateAppellation;
    private String dateRetraitAppellationParHautConseil;
    /** Department of the museum **/
    private String departement;
    /** fax of the museum **/
    private int fax;
    /** Regular dates when it is closed **/
    private String fermetureAnnuelle;
    /** Name of the museum **/
    private String nomDuMusee;
    /** Dates when it will be opened **/
    private String periodeOuverture;
    /** Reference of the museum **/
    private String refMusee;
    /** Region where it is present **/
    private String region;
    /** Website associated **/
    private String siteweb;
    /** Phone number to join the office **/
    private long telephone1;
    /** City where the museum is present **/
    private String ville;

    public MuseumBean() {
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getCoordonneesFiscales() {
        return coordonneesFiscales;
    }

    public void setCoordonneesFiscales(String coordonneesFiscales) {
        this.coordonneesFiscales = coordonneesFiscales;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getDateAppellation() {
        return dateAppellation;
    }

    public void setDateAppellation(String dateAppellation) {
        this.dateAppellation = dateAppellation;
    }

    public String getDateRetraitAppellationParHautConseil() {
        return dateRetraitAppellationParHautConseil;
    }

    public void setDateRetraitAppellationParHautConseil(String dateRetraitAppellationParHautConseil) {
        this.dateRetraitAppellationParHautConseil = dateRetraitAppellationParHautConseil;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }



    public String getFermetureAnnuelle() {
        return fermetureAnnuelle;
    }

    public void setFermetureAnnuelle(String fermetureAnnuelle) {
        this.fermetureAnnuelle = fermetureAnnuelle;
    }

    public String getNomDuMusee() {
        return nomDuMusee;
    }

    public void setNomDuMusee(String nomDuMusee) {
        this.nomDuMusee = nomDuMusee;
    }

    public String getPeriodeOuverture() {
        return periodeOuverture;
    }

    public void setPeriodeOuverture(String periodeOuverture) {
        this.periodeOuverture = periodeOuverture;
    }

    public String getRefMusee() {
        return refMusee;
    }

    public void setRefMusee(String refMusee) {
        this.refMusee = refMusee;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSiteweb() {
        return siteweb;
    }

    public void setSiteweb(String siteweb) {
        this.siteweb = siteweb;
    }

    public long getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(long telephone1) {
        this.telephone1 = telephone1;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public String toString() {
        return "Museum{" +
                "adresse='" + adr + '\'' +
                ", coordonneesFiscales='" + coordonneesFiscales + '\'' +
                ", codePostal=" + codePostal +
                ", dateAppellation='" + dateAppellation + '\'' +
                ", dateRetraitAppellationParHautConseil='" + dateRetraitAppellationParHautConseil + '\'' +
                ", departement='" + departement + '\'' +
                ", fax=" + fax +
                ", fermetureAnnuelle='" + fermetureAnnuelle + '\'' +
                ", nomDuMusee='" + nomDuMusee + '\'' +
                ", periodeOuverture='" + periodeOuverture + '\'' +
                ", refMusee='" + refMusee + '\'' +
                ", region='" + region + '\'' +
                ", siteWeb='" + siteweb + '\'' +
                ", telephone1='" + telephone1 + '\'' +
                ", ville='" + ville + '\'' +
                '}';
    }

}
