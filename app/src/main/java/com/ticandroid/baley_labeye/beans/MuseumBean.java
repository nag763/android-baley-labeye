package com.ticandroid.baley_labeye.beans;

/**
 * The museum class's purpose is to contain any important information related to the museums
 * stored in the firestore database.
 *
 * @author Baley
 * @author Labeye
 */
public class MuseumBean {

    /**
     * Adresse of the museum.
     **/
    private String adr;
    /**
     * Fiscales data.
     **/
    private String coordonneesFinales;
    /**
     * Postal code associated.
     **/
    private int cp;
    /**
     * Department of the museum.
     **/
    private String departement;
    /**
     * Name of the museum.
     **/
    private String nomDuMusee;
    /**
     * Region where it is present.
     **/
    private String region;
    /**
     * Phone number to join the office.
     **/
    private long telephone1;
    /**
     * City where the museum is present.
     **/
    private String ville;

    /**
     * Default constructor.
     */
    public MuseumBean() {
    }

    /**
     * Method to returns the longitude/ latitude instead of
     * latitude,longitude.
     *
     * @return the coordonnnees as understandable by the api
     */
    public String getInvertedCoordonneesFinales() {
        String[] coordonneesSplitted = coordonneesFinales.split(",");
        return String.format("%s,%s", coordonneesSplitted[1], coordonneesSplitted[0]);
    }

    public String getNomDuMusee() {
        return nomDuMusee;
    }

    /**
     * Returns a readable string for the reader.
     *
     * @return the phone number with the local number prefix
     */
    public String getTelephoneWithPrefix() {
        return String.format("0%s", telephone1);
    }

    /**
     * Method to return only the most important part of the address.
     *
     * @return a shortened understandable adress
     */
    public String getPartialAdresse() {
        return String.format("%s\n %s, %s", adr, ville, region);
    }

    /**
     * Method to return the complete adresse of a museum.
     *
     * @return the adr, cp, ville, departement and region as a pretty and readable String value
     */
    public String getCompleteAdresse() {
        return String.format("%s\n%s %s\n%s, %s", adr, cp, ville, departement, region);
    }

    public String getAdr() {
        return adr;
    }

    public String getCoordonneesFinales() {
        return coordonneesFinales;
    }

    public int getCp() {
        return cp;
    }

    public String getDepartement() {
        return departement;
    }

    public String getRegion() {
        return region;
    }

    public long getTelephone1() {
        return telephone1;
    }

    public String getVille() {
        return ville;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public void setCoordonneesFinales(String coordonneesFinales) {
        this.coordonneesFinales = coordonneesFinales;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public void setNomDuMusee(String nomDuMusee) {
        this.nomDuMusee = nomDuMusee;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setTelephone1(long telephone1) {
        this.telephone1 = telephone1;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public String toString() {
        return "MuseumBean{" +
                "adr='" + adr + '\'' +
                ", coordonneesFinales='" + coordonneesFinales + '\'' +
                ", cp=" + cp +
                ", departement='" + departement + '\'' +
                ", nomDuMusee='" + nomDuMusee + '\'' +
                ", region='" + region + '\'' +
                ", telephone1=" + telephone1 +
                ", ville='" + ville + '\'' +
                '}';
    }
}
