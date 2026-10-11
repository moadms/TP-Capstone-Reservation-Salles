package ma.enset.capstone.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class CreneauHoraire implements Serializable {

    @Column(name = "debut_horaire", nullable = false)
    private LocalDateTime debut;

    @Column(name = "fin_horaire", nullable = false)
    private LocalDateTime fin;

    public CreneauHoraire() {
    }

    public CreneauHoraire(LocalDateTime debut, LocalDateTime fin) {
        if (fin.isBefore(debut)) {
            throw new IllegalArgumentException("La fin du créneau ne peut pas précéder le début.");
        }
        this.debut = debut;
        this.fin = fin;
    }

    public boolean chevauche(CreneauHoraire autre) {
        return this.debut.isBefore(autre.fin) && this.fin.isAfter(autre.debut);
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreneauHoraire)) return false;
        CreneauHoraire that = (CreneauHoraire) o;
        return Objects.equals(debut, that.debut) && Objects.equals(fin, that.fin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debut, fin);
    }

    @Override
    public String toString() {
        return "[" + debut + " -> " + fin + "]";
    }
}
