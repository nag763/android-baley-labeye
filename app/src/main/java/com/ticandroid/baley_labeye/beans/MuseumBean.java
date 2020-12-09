package com.ticandroid.baley_labeye.beans;

public class MuseumBean {

    private String adresse;
    private String coordonneesFiscales;
    private int codePostal;
    private String dateAppellation;
    private String dateRetraitAppellationParHautConseil;
    private String departement;
    private int fax;
    private String fermetureAnnuelle;
    private String nomDuMusee;
    private String periodeOuverture;
    private String refMusee;
    private String region;
    private String siteWeb;
    private String telephone;
    private String ville;

    public MuseumBean() {
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
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

    public int getFax() {
        return fax;
    }

    public void setFax(int fax) {
        this.fax = fax;
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

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
                "adresse='" + adresse + '\'' +
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
                ", siteWeb='" + siteWeb + '\'' +
                ", telephone1='" + telephone + '\'' +
                ", ville='" + ville + '\'' +
                '}';
    }

}
