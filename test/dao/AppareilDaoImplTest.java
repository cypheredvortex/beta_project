package dao;

import java.util.List;
import java.util.Optional;

import dao.impl.AppareilDaoImpl;
import metier.enums.TypeProbleme;
import metier.model.Appareil;
import metier.model.Reparation;

public class AppareilDaoImplTest {
	public static void main(String[] args) {
        AppareilDaoImpl dao = new AppareilDaoImpl();

        try {
            // 1️⃣ Create a new Appareil instance
            Appareil appareil = new Appareil();
            appareil.setType("Smartphone");
            appareil.setMarque("Samsung");
            appareil.setModele("Galaxy S22");
            appareil.setNumeroSerie("SN123456789");
            appareil.setProbleme("Ne s’allume plus");
            appareil.setTypeProbleme(TypeProbleme.HARDWARE);

            // Optional: Link to a reparation
            Reparation rep = new Reparation();
            rep.setId(1L); // only if exists in DB
            appareil.setReparation(rep);

            System.out.println("\n=== TEST 1: SAVE ===");
            appareil = dao.save(appareil);
            System.out.println("Appareil saved with ID: " + appareil.getId());

            // 2️⃣ Test findById
            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Appareil> found = dao.findById(appareil.getId());
            found.ifPresentOrElse(
                a -> System.out.println("Found: " + a.getMarque() + " " + a.getModele()),
                () -> System.out.println("Appareil not found!")
            );

            // 3️⃣ Test update
            System.out.println("\n=== TEST 3: UPDATE ===");
            appareil.setProbleme("L’écran est cassé");
            dao.update(appareil);
            System.out.println("Appareil updated.");

            // 4️⃣ Test findByNumeroSerie
            System.out.println("\n=== TEST 4: FIND BY NUMERO SERIE ===");
            Optional<Appareil> bySerie = dao.findByNumeroSerie("SN123456789");
            bySerie.ifPresent(a -> System.out.println("Found by serial: " + a.getProbleme()));

            // 5️⃣ Test findAll
            System.out.println("\n=== TEST 5: FIND ALL ===");
            List<Appareil> all = dao.findAll();
            all.forEach(a -> System.out.println(a.getId() + " → " + a.getMarque() + " " + a.getModele()));

            // 6️⃣ Test findByType
            System.out.println("\n=== TEST 6: FIND BY TYPE ===");
            List<Appareil> smartphones = dao.findByType("Smartphone");
            smartphones.forEach(a -> System.out.println("Smartphone: " + a.getModele()));

            // 7️⃣ Test findByReparationId
            System.out.println("\n=== TEST 7: FIND BY REPARATION ID ===");
            List<Appareil> reparations = dao.findByReparationId(1L);
            reparations.forEach(a -> System.out.println("Appareil lié à répa 1: " + a.getModele()));

            // 8️⃣ Test delete
            System.out.println("\n=== TEST 8: DELETE ===");
            boolean deleted = dao.deleteById(appareil.getId());
            System.out.println("Deleted: " + deleted);

            // 9️⃣ Verify deletion
            System.out.println("\n=== TEST 9: VERIFY DELETE ===");
            Optional<Appareil> afterDelete = dao.findById(appareil.getId());
            System.out.println("Exists after delete? " + afterDelete.isPresent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
