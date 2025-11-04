package dao;

import java.util.List;
import java.util.Optional;

import dao.impl.ReparateurDaoImpl;
import metier.model.Boutique;
import metier.model.Compte;
import metier.model.Reparateur;

public class ReparateurDaoImplTest {
	public static void main(String[] args) {
        ReparateurDaoImpl dao = new ReparateurDaoImpl();

        try {
            System.out.println("=== TEST 1: SAVE ===");

            // Create a new compte for testing
            Compte compte = new Compte();
            compte.setId(1L); // Assuming a compte with ID 1 exists

            // Create a new boutique for testing
            Boutique boutique = new Boutique();
            boutique.setId(1L); // Assuming a boutique with ID 1 exists

            // Create a new reparateur
            Reparateur r = new Reparateur();
            r.setNom("Durand");
            r.setPrenom("Pierre");
            r.setTelephone("0601020304");
            r.setEmail("pierre.durand+" + System.currentTimeMillis() + "@example.com"); // unique email
            r.setSalairePourcentage(10.5);
            r.setCompte(compte);
            r.setBoutique(boutique);

            Reparateur saved = dao.save(r);
            System.out.println("Saved reparateur: " + saved.getId() + " | " + saved.getEmail());

            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Reparateur> found = dao.findById(saved.getId());
            found.ifPresent(reparateur -> 
                System.out.println("Found reparateur: " + reparateur.getNom() + " " + reparateur.getPrenom())
            );

            System.out.println("\n=== TEST 3: UPDATE ===");
            saved.setNom("Durand-Updated");
            saved.setSalairePourcentage(12.0);
            dao.update(saved);
            Optional<Reparateur> updated = dao.findById(saved.getId());
            updated.ifPresent(rp -> 
                System.out.println("Updated reparateur: " + rp.getNom() + " | Commission: " + rp.getSalairePourcentage())
            );

            System.out.println("\n=== TEST 4: FIND BY EMAIL ===");
            Optional<Reparateur> byEmail = dao.findByEmail(saved.getEmail());
            byEmail.ifPresent(rp -> System.out.println("Found by email: " + rp.getNom()));

            System.out.println("\n=== TEST 5: FIND BY TELEPHONE ===");
            Optional<Reparateur> byTel = dao.findByTelephone(saved.getTelephone());
            byTel.ifPresent(rp -> System.out.println("Found by telephone: " + rp.getNom()));

            System.out.println("\n=== TEST 6: FIND BY NOM ===");
            List<Reparateur> byNom = dao.findByNom("Durand-Updated");
            byNom.forEach(rp -> System.out.println("Found by nom: " + rp.getNom() + " " + rp.getPrenom()));

            System.out.println("\n=== TEST 7: FIND ALL ===");
            List<Reparateur> all = dao.findAll();
            all.forEach(rp -> System.out.println("Reparateur: " + rp.getNom() + " " + rp.getPrenom()));

            System.out.println("\n=== TEST 8: DELETE ===");
            boolean deleted = dao.deleteById(saved.getId());
            System.out.println("Deleted: " + deleted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
