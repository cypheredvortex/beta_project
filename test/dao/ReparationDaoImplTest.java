package dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;


import dao.impl.ReparationDaoImpl;
import metier.model.Reparateur;
import metier.model.Reparation;
import metier.enums.EtatReparation;
import metier.model.Client;

public class ReparationDaoImplTest {
	public static void main(String[] args) {
        ReparationDaoImpl dao = new ReparationDaoImpl();

        try {
            System.out.println("=== TEST 1: SAVE ===");

            // Create client and reparateur for testing
            Client client = new Client();
            client.setId(1L); // assuming exists
            client.setTelephone("0601020304");

            Reparateur reparateur = new Reparateur();
            reparateur.setId(1L); // assuming exists

            // Create a new reparation
            Reparation r = new Reparation();
            r.setCodeUnique("REP-" + System.currentTimeMillis());
            r.setDateCreation(new Date());
            r.setStatut(EtatReparation.EN_COURS);
            r.setDescription("Réparation écran smartphone");
            r.setPrixConvenu(50.0);
            r.setPrixTotalPieces(30.0);
            r.setRemarques("Aucune remarque particulière");
            r.setClient(client);
            r.setReparateur(reparateur);

            Reparation saved = dao.save(r);
            System.out.println("Saved reparation: " + saved.getId() + " | " + saved.getCodeUnique());

            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Reparation> found = dao.findById(saved.getId());
            found.ifPresent(rep -> 
                System.out.println("Found reparation: " + rep.getDescription() + " | Statut: " + rep.getStatut())
            );

            System.out.println("\n=== TEST 3: UPDATE ===");
            saved.setStatut(EtatReparation.TERMINE);
            saved.setRemarques("Réparation terminée avec succès");
            dao.update(saved);
            Optional<Reparation> updated = dao.findById(saved.getId());
            updated.ifPresent(rep -> 
                System.out.println("Updated reparation: " + rep.getDescription() + " | Statut: " + rep.getStatut() + " | Remarques: " + rep.getRemarques())
            );

            System.out.println("\n=== TEST 4: FIND BY CODE UNIQUE ===");
            Optional<Reparation> byCode = dao.findByCodeUnique(saved.getCodeUnique());
            byCode.ifPresent(rep -> System.out.println("Found by code: " + rep.getCodeUnique()));

            System.out.println("\n=== TEST 5: FIND BY CLIENT PHONE ===");
            List<Reparation> byPhone = dao.findByClientPhone(client.getTelephone());
            byPhone.forEach(rep -> System.out.println("Reparation by client phone: " + rep.getCodeUnique()));

            System.out.println("\n=== TEST 6: FIND BY STATUT ===");
            List<Reparation> byStatut = dao.findByStatut(EtatReparation.TERMINE);
            byStatut.forEach(rep -> System.out.println("Reparation by statut: " + rep.getCodeUnique() + " | Statut: " + rep.getStatut()));

            System.out.println("\n=== TEST 7: FIND BY REPARATEUR ID ===");
            List<Reparation> byReparateur = dao.findByReparateurId(reparateur.getId());
            byReparateur.forEach(rep -> System.out.println("Reparation by reparateur: " + rep.getCodeUnique()));

            System.out.println("\n=== TEST 8: FIND ALL ===");
            List<Reparation> all = dao.findAll();
            all.forEach(rep -> System.out.println("Reparation: " + rep.getCodeUnique() + " | Statut: " + rep.getStatut()));

            System.out.println("\n=== TEST 9: DELETE ===");
            boolean deleted = dao.deleteById(saved.getId());
            System.out.println("Deleted reparation: " + deleted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
