package ma.enset.capstone.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cap_reservations")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CreneauHoraire creneau;

    @Column(name = "motif_reunion", length = 150)
    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_demande")
    private StatutReservation statut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur demandeur;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    public Reservation() {
        this.statut = StatutReservation.CONFIRMEE;
    }

    public Reservation(CreneauHoraire creneau, String motif, Utilisateur demandeur, Salle salle) {
        this();
        this.creneau = creneau;
        this.motif = motif;
        this.demandeur = demandeur;
        this.salle = salle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CreneauHoraire getCreneau() {
        return creneau;
    }

    public void setCreneau(CreneauHoraire creneau) {
        this.creneau = creneau;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public Utilisateur getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Utilisateur demandeur) {
        this.demandeur = demandeur;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", creneau=" + creneau +
                ", motif='" + motif + '\'' +
                ", statut=" + statut +
                '}';
    }
}
