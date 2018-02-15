package com.pandasoftware.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="Coordonnees")
public class Coordonnees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coord_id")
    private long coordId;

    @Column(name = "nom")
    private String nom;

    @Column(name = "num_rue")
    private String numRue;

    @Column(name = "rue")
    private String rue;

    @Column(name = "ville")
    private String ville;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "url")
    private String url;


    public long getCoordId() {
        return coordId;
    }

    public void setCoordId(long coordId) {
        this.coordId = coordId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumRue() {
        return numRue;
    }

    public void setNumRue(String numRue) {
        this.numRue = numRue;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLine1forPdf(){
        return this.getNom();
    }
    public String getLine2forPdf(){
        StringBuffer result = new StringBuffer();
        result = result.append(this.getNumRue()).append(", ").
                append(this.getRue()).append(" - ").
                append(this.getCodePostal()).append(" ").
                append(this.getVille());
        return result.toString();
    }
    public String getLine3forPdf(){
        StringBuffer result = new StringBuffer();
        result = result.append(this.getPhone()).append(" - ").
                append(this.getEmail());
        return result.toString();
    }
    public String getLine4forPdf(){
        return this.getUrl();
    }


}
