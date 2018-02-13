package com.codetreatise.bean;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;


@Entity
@Table(name="Agent")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="agent_id")
    private long agentId;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private String gender;

    private String role;

    private String email;

    private String password;

    @Column(name = "logo")
    private byte[] logo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="coord_id")
    private Coordonnees coordonnees;

    @OneToMany(fetch = FetchType.EAGER,mappedBy="agent")//TODO essayer de passer en lazy
    private Set<Annonce> annonceList;

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Coordonnees getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(Coordonnees coordonnees) {
        this.coordonnees = coordonnees;
    }

    public Set<Annonce> getAnnonceList() {
        return annonceList;
    }

    public void setAnnonceList(Set<Annonce> annonceList) {
        this.annonceList = annonceList;
    }

    public Annonce getAnnonceById(long id){
        Annonce resultat=null;
        for(Annonce annonce : this.getAnnonceList()){
            if(annonce.getAnnonceId() == id){
                resultat = annonce;
                break;
            }
        }
        return resultat;
    }
}
