package dao;

import java.util.List;
import java.util.Optional;

import dao.impl.BoutiqueDaoImpl;
import metier.model.Boutique;
import metier.model.Proprietaire;

public class BoutiqueDaoImplTest {
	public static void main(String[] args) {
        BoutiqueDaoImpl dao = new BoutiqueDaoImpl();

        try {
            // 1️⃣ Create a new Boutique instance
            Boutique boutique = new Boutique();
            boutique.setNom("TechStore");
            boutique.setAddresse("123 Rue de Casablanca");
            boutique.setLogoPath("/logos/techstore.png");
            boutique.setNumPatente("PT123456");

            // Optional: associate with a Proprietaire
            Proprietaire prop = new Proprietaire();
            prop.setId(1L); // ensure this ID exists in DB
            boutique.setProprietaire(prop);

            System.out.println("\n=== TEST 1: SAVE ===");
            boutique = dao.save(boutique);
            System.out.println("Boutique saved with ID: " + boutique.getId());

            // 2️⃣ Test findById
            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Boutique> found = dao.findById((int) boutique.getId());
            found.ifPresentOrElse(
                b -> System.out.println("Found: " + b.getNom() + ", " + b.getAddresse()),
                () -> System.out.println("Boutique not found!")
            );

            // 3️⃣ Test update
            System.out.println("\n=== TEST 3: UPDATE ===");
            boutique.setAddresse("456 Avenue Rabat");
            dao.update(boutique);
            System.out.println("Boutique updated.");

            // 4️⃣ Test findAll
            System.out.println("\n=== TEST 4: FIND ALL ===");
            List<Boutique> all = dao.findAll();
            all.forEach(b -> System.out.println(b.getId() + " → " + b.getNom() + ", " + b.getAddresse()));

            // 5️⃣ Test findByProprietaireId
            System.out.println("\n=== TEST 5: FIND BY PROPRIETAIRE ID ===");
            List<Boutique> byProp = dao.findByProprietaireId(1L);
            byProp.forEach(b -> System.out.println("Boutique for Proprietaire 1: " + b.getNom()));

            // 6️⃣ Test deleteById
            System.out.println("\n=== TEST 6: DELETE ===");
            boolean deleted = dao.deleteById((int) boutique.getId());
            System.out.println("Deleted: " + deleted);

            // 7️⃣ Verify deletion
            System.out.println("\n=== TEST 7: VERIFY DELETE ===");
            Optional<Boutique> afterDelete = dao.findById((int) boutique.getId());
            System.out.println("Exists after delete? " + afterDelete.isPresent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
