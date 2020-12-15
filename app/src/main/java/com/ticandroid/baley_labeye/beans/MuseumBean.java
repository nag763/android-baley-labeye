package com.ticandroid.baley_labeye.beans;

/**
 * The museum class's purpose is to contain any important information related to the museums
 * stored in the firestore database.
 *
 * @author Baley
 * @author Labeye
 */
public class MuseumBean {

    /** Adresse of the museum. **/
    private String adr;
    /** Fiscales data. **/
    private String coordonneesFinales;
    /** Postal code associated. **/
    private int cp;
    /** Date when it has ben named. **/
    private String dateAppellation;
    private String dateRetraitAppellationParHautConseil;
    /** Department of the museum. **/
    private String departement;
    /** fax of the museum. **/
    private int fax;
    /** Regular dates when it is closed. **/
    private String fermetureAnnuelle;
    /** Name of the museum. **/
    private String nomDuMusee;
    /** Dates when it will be opened. **/
    private String periodeOuverture;
    /** Reference of the museum. **/
    private String refMusee;
    /** Region where it is present. **/
    private String region;
    /** Website associated. **/
    private String siteweb;
    /** Phone number to join the office. **/
    private long telephone1;
    /** City where the museum is present. **/
    private String ville;

    public MuseumBean() {
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getCoordonneesFinales() {
        return coordonneesFinales;
    }

    /**
     *
     * Method to returns the longitude/ latitude instead of
     * latitude,longitude
     *
     * @return the coordonnnees as understandable by the api
     */
    public String getInvertedCoordonneesFinales(){
        String[] coordonneesSplitted = coordonneesFinales
                .split(",");
        return String.format("%s,%s", coordonneesSplitted[1], coordonneesSplitted[0]);
    }

    public void setCoordonneesFinales(String coordonneesFinales) {
        this.coordonneesFinales = coordonneesFinales;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
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

    /**
     * Returns a readable string for the reader
     *
     * @return the phone number with the local number prefix
     */
    public String getTelephoneWithPrefix() {
        return String.format("0%s", telephone1);
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

    /**
     * Method to return only the most important part of the adress
     *
     * @return a shortened understandable adress
     */
    public String getPartialAdresse(){
        return String.format("%s\n %s, %s", adr, ville, region);
    }

    /**
     * Method to return the complete adresse of a museum
     *
     * @return the adr, cp, ville, departement and region as a pretty and readable String value
     */
    public String getCompleteAdresse() {
        return String.format("%s\n%s %s\n%s, %s", adr, cp, ville, departement, region);
    }

    private double getLatitude() {
        return Double.parseDouble(coordonneesFinales.split(",")[0]);
    }

    private double getLongitude() {
        return Double.parseDouble(coordonneesFinales.split(",")[1]);
    }

    @Override
    public String toString() {
        return "Museum{" +
                "adresse='" + adr + '\'' +
                ", coordonneesFiscales='" + coordonneesFinales + '\'' +
                ", codePostal=" + cp +
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
