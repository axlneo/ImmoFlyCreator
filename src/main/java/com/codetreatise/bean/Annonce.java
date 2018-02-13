package com.codetreatise.bean;

import javax.persistence.*;

@Entity
@Table(name="Annonce")
public class Annonce {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "annonce_id", updatable = false, nullable = false)
    private long annonceId;

    @Column(name = "titre")
    private String titre;

    @Column(name = "description")
    private String description;

    @Column(name = "photo1")
    private byte[] photo1;
    @Column(name = "photo2")
    private byte[] photo2;

    @Column(name = "photo3")
    private byte[] photo3;

    @Column(name = "photo4")
    private byte[] photo4;

    @Column(name = "cover")
    private byte[] cover;

    @ManyToOne
    @JoinColumn(name="agent_id", nullable=false)
    private Agent agent;

    public long getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(long annonceId) {
        this.annonceId = annonceId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhoto1() {
        return photo1;
    }

    public void setPhoto1(byte[] photo1) {
        this.photo1 = photo1;
    }

    public byte[] getPhoto2() {
        return photo2;
    }

    public void setPhoto2(byte[] photo2) {
        this.photo2 = photo2;
    }

    public byte[] getPhoto3() {
        return photo3;
    }

    public void setPhoto3(byte[] photo3) {
        this.photo3 = photo3;
    }

    public byte[] getPhoto4() {
        return photo4;
    }

    public void setPhoto4(byte[] photo4) {
        this.photo4 = photo4;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
