package ma.enset.capstone.service;

import ma.enset.capstone.exception.ReservationConflictException;
import ma.enset.capstone.model.CreneauHoraire;
import ma.enset.capstone.model.Reservation;
import ma.enset.capstone.model.Salle;
import ma.enset.capstone.model.StatutReservation;
import ma.enset.capstone.model.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class ReservationService {

    private final EntityManagerFactory emf;

    public ReservationService(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Reservation reserverSalle(Long utilisateurId, Long salleId, CreneauHoraire creneau, String motif) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Salle salle = em.find(Salle.class, salleId);
            if (salle == null) {
                throw new IllegalArgumentException("Salle introuvable avec ID : " + salleId);
            }

            Utilisateur demandeur = em.find(Utilisateur.class, utilisateurId);
            if (demandeur == null) {
                throw new IllegalArgumentException("Utilisateur introuvable avec ID : " + utilisateurId);
            }

            // Vérification de non-conflit sur le créneau
            String queryConflit = "SELECT count(r) FROM Reservation r WHERE r.salle.id = :salleId " +
                    "AND r.statut = :statutActif " +
                    "AND r.creneau.debut < :fin AND r.creneau.fin > :debut";

            Long nbConflits = em.createQuery(queryConflit, Long.class)
                    .setParameter("salleId", salleId)
                    .setParameter("statutActif", StatutReservation.CONFIRMEE)
                    .setParameter("debut", creneau.getDebut())
                    .setParameter("fin", creneau.getFin())
                    .getSingleResult();

            if (nbConflits > 0) {
                throw new ReservationConflictException("La salle '" + salle.getNom() + "' est déjà occupée sur le créneau " + creneau);
            }

            Reservation reservation = new Reservation(creneau, motif, demandeur, salle);
            em.persist(reservation);

            em.getTransaction().commit();
            return reservation;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Salle> trouverSallesDisponibles(CreneauHoraire creneau, Integer capaciteMin) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT s FROM Salle s WHERE s.capacite >= :capaciteMin AND s.id NOT IN (" +
                    "   SELECT r.salle.id FROM Reservation r " +
                    "   WHERE r.statut = :statutActif " +
                    "   AND r.creneau.debut < :fin AND r.creneau.fin > :debut" +
                    ")";

            return em.createQuery(jpql, Salle.class)
                    .setParameter("capaciteMin", capaciteMin)
                    .setParameter("statutActif", StatutReservation.CONFIRMEE)
                    .setParameter("debut", creneau.getDebut())
                    .setParameter("fin", creneau.getFin())
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void annulerReservation(Long reservationId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Reservation r = em.find(Reservation.class, reservationId);
            if (r != null) {
                r.setStatut(StatutReservation.ANNULEE);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
