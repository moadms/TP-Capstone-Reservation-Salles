package ma.enset.capstone.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cap_utilisateurs")
public class Utilisateur implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricule_pro", unique = true, nullable = false, length = 50)
    private String matricule;

    @Column(name = "nom_prenom", nullable = false, length = 120)
    private String nomComplet;

    @Column(name = "adresse_mail", unique = true, nullable = false, length = 150)
    private String email;

    @OneToMany(mappedBy = "demandeur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    public Utilisateur() {
    }

    public Utilisateur(String matricule, String nomComplet, String email) {
        this.matricule = matricule;
        this.nomComplet = nomComplet;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", matricule='" + matricule + '\'' +
                ", nomComplet='" + nomComplet + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
