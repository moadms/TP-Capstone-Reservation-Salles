package ma.enset.capstone;

import ma.enset.capstone.exception.ReservationConflictException;
import ma.enset.capstone.model.*;
import ma.enset.capstone.service.ReservationService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println(">>> DEMARRAGE DU PROJET CAPSTONE - APPLICATION RESERVATION DE SALLES <<<");
        System.out.println("==========================================================================");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("capstone_reservation_salles");
        ReservationService reservationService = new ReservationService(emf);

        // [1] Initialisation des données de base
        System.out.println("\n--- [1] INITIALISATION DES UTILISATEURS, EQUIPEMENTS ET SALLES ---");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Utilisateur u1 = new Utilisateur("MAT-2026-01", "Moad Moussaoui", "moussaoui9881@gmail.com");
        Utilisateur u2 = new Utilisateur("MAT-2026-02", "Yassine Benali", "y.benali@entreprise.ma");
        em.persist(u1);
        em.persist(u2);

        Equipement eqVisio = new Equipement("INV-VIS-01", "Borne Vidéoconférence 4K");
        Equipement eqTableau = new Equipement("INV-TAB-01", "Tableau Blanc Numérique");
        em.persist(eqVisio);
        em.persist(eqTableau);

        Salle sAtlas = new Salle("S-101", "Amphithéâtre Atlas", 100);
        sAtlas.ajouterEquipement(eqVisio);

        Salle sToubkal = new Salle("S-102", "Salle Réunion Toubkal", 20);
        sToubkal.ajouterEquipement(eqVisio);
        sToubkal.ajouterEquipement(eqTableau);

        em.persist(sAtlas);
        em.persist(sToubkal);

        em.getTransaction().commit();
        Long u1Id = u1.getId();
        Long u2Id = u2.getId();
        Long sToubkalId = sToubkal.getId();
        Long sAtlasId = sAtlas.getId();
        em.close();

        System.out.println("✓ Entités initialisées avec succès.");

        // [2] Création d'une première réservation valide
        System.out.println("\n--- [2] RESERVATION NOMINALE REUSSIE ---");
        LocalDateTime debut1 = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);
        LocalDateTime fin1 = LocalDateTime.now().plusDays(1).withHour(11).withMinute(0);
        CreneauHoraire creneau1 = new CreneauHoraire(debut1, fin1);

        Reservation resa1 = reservationService.reserverSalle(u1Id, sToubkalId, creneau1, "Réunion Architecture Logicielle");
        System.out.println("✓ Réservation créée : " + resa1.getId() + " pour la salle Toubkal sur " + creneau1);

        // [3] Tentative de réservation concurrente/en conflit sur le même créneau
        System.out.println("\n--- [3] TENTATIVE DE RESERVATION EN CONFLIT TEMPOREL ---");
        LocalDateTime debutConflit = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime finConflit = LocalDateTime.now().plusDays(1).withHour(12).withMinute(0);
        CreneauHoraire creneauConflit = new CreneauHoraire(debutConflit, finConflit);

        try {
            reservationService.reserverSalle(u2Id, sToubkalId, creneauConflit, "Session Recrutement");
            System.err.println("Échec: Le conflit n'a pas été détecté !");
        } catch (ReservationConflictException e) {
            System.out.println("✓ Détection du conflit réussie !");
            System.out.println("  Message métier : " + e.getMessage());
        }

        // [4] Recherche des salles disponibles sur ce même créneau
        System.out.println("\n--- [4] RECHERCHE DE SALLES ALTERNATIVES DISPONIBLES ---");
        List<Salle> sallesDispos = reservationService.trouverSallesDisponibles(creneauConflit, 15);
        System.out.println("Salles disponibles pour au moins 15 personnes sur " + creneauConflit + " :");
        for (Salle s : sallesDispos) {
            System.out.println("  -> " + s.getCodeSalle() + " (" + s.getNom() + ") - Capacité : " + s.getCapacite());
        }

        // [5] Annulation et vérification du statut
        System.out.println("\n--- [5] ANNULATION DE RESERVATION ---");
        reservationService.annulerReservation(resa1.getId());
        System.out.println("✓ Réservation #" + resa1.getId() + " annulée avec succès.");

        emf.close();
        System.out.println("\n==========================================================================");
        System.out.println(">>> PROJET CAPSTONE TERMINE AVEC SUCCES <<<");
        System.out.println("==========================================================================");
    }
}
