package ma.enset.capstone.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cap_equipements")
public class Equipement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_inventaire", unique = true, nullable = false, length = 50)
    private String codeInventaire;

    @Column(name = "libelle_materiel", nullable = false, length = 100)
    private String libelle;

    @ManyToMany(mappedBy = "equipements")
    private Set<Salle> salles = new HashSet<>();

    public Equipement() {
    }

    public Equipement(String codeInventaire, String libelle) {
        this.codeInventaire = codeInventaire;
        this.libelle = libelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeInventaire() {
        return codeInventaire;
    }

    public void setCodeInventaire(String codeInventaire) {
        this.codeInventaire = codeInventaire;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Salle> getSalles() {
        return salles;
    }

    public void setSalles(Set<Salle> salles) {
        this.salles = salles;
    }

    @Override
    public String toString() {
        return "Equipement{" +
                "id=" + id +
                ", codeInventaire='" + codeInventaire + '\'' +
                ", libelle='" + libelle + '\'' +
                '}';
    }
}
