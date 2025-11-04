package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.impl.ProprietaireDaoImpl;
import metier.model.Boutique;
import metier.model.Caisse;
import metier.model.Compte;
import metier.model.Proprietaire;

public class ProprietaireDaoImplTest {
	public static void main(String[] args) {
        ProprietaireDaoImpl dao = new ProprietaireDaoImpl();

        try {
            // 1️⃣ Create a new Proprietaire
            Proprietaire p = new Proprietaire();
            p.setNom("Dupont");
            p.setPrenom("Jean");
            p.setEmail("jean.dupont@example.com");
            p.setTelephone("0601020304");
            p.setSalairePourcentage(20.0);
            p.setPourcentageReparation(10.0);

            // Optional: associate boutiques (make sure these IDs exist in DB)
            List<Boutique> boutiques = new ArrayList<>();
            Boutique b1 = new Boutique();
            b1.setId(1L);
            boutiques.add(b1);

            Boutique b2 = new Boutique();
            b2.setId(2L);
            boutiques.add(b2);

            p.setBoutiques(boutiques);

            System.out.println("\n=== TEST 1: SAVE ===");
            p = dao.save(p);
            System.out.println("Proprietaire saved with ID: " + p.getId());

            // 2️⃣ Test findById
            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Proprietaire> found = dao.findById(p.getId());
            found.ifPresentOrElse(
                    prop -> System.out.println("Found Proprietaire: " + prop.getNom() + " " + prop.getPrenom() +
                            ", Email=" + prop.getEmail() + ", Boutiques=" + prop.getBoutiques().size()),
                    () -> System.out.println("Proprietaire not found!")
            );

            // 3️⃣ Test findAll
            System.out.println("\n=== TEST 3: FIND ALL ===");
            List<Proprietaire> all = dao.findAll();
            all.forEach(prop -> System.out.println("ID=" + prop.getId() + ", Nom=" + prop.getNom()));

            // 4️⃣ Test findByEmail
            System.out.println("\n=== TEST 4: FIND BY EMAIL ===");
            Optional<Proprietaire> byEmail = dao.findByEmail("jean.dupont@example.com");
            byEmail.ifPresent(prop -> System.out.println("Found by Email: ID=" + prop.getId()));

            // 5️⃣ Test findByTelephone
            System.out.println("\n=== TEST 5: FIND BY TELEPHONE ===");
            Optional<Proprietaire> byTel = dao.findByTelephone("0601020304");
            byTel.ifPresent(prop -> System.out.println("Found by Telephone: ID=" + prop.getId()));

            // 6️⃣ Test update
            System.out.println("\n=== TEST 6: UPDATE ===");
            p.setNom("Dupont-Modifié");
            p.setPourcentageReparation(15.0);
            dao.update(p);
            System.out.println("Proprietaire updated: new nom=" + p.getNom() + ", pourcentageReparation=" + p.getPourcentageReparation());

            // 7️⃣ Test delete
            System.out.println("\n=== TEST 7: DELETE ===");
            boolean deleted = dao.deleteById(p.getId());
            System.out.println("Deleted: " + deleted);

            // 8️⃣ Verify deletion
            System.out.println("\n=== TEST 8: VERIFY DELETE ===");
            Optional<Proprietaire> afterDelete = dao.findById(p.getId());
            System.out.println("Exists after delete? " + afterDelete.isPresent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
