package dao;

import java.util.List;
import java.util.Optional;

import dao.impl.CaisseDaoImpl;
import metier.enums.TypeCaisse;
import metier.model.Caisse;
import metier.model.Reparateur;

public class CaisseDaoImplTest {
	public static void main(String[] args) {
        CaisseDaoImpl dao = new CaisseDaoImpl();

        try {
            // 1️⃣ Create a new Caisse instance
            Caisse caisse = new Caisse();
            caisse.setSoldeActuel(1000.0);
            caisse.setType(TypeCaisse.PERSONNELLE);

            // Optional: associate with a Reparateur
            Reparateur rep = new Reparateur();
            rep.setId(1L); // ensure this ID exists in DB
            caisse.setReparateur(rep);

            System.out.println("\n=== TEST 1: SAVE ===");
            caisse = dao.save(caisse);
            System.out.println("Caisse saved with ID: " + caisse.getId());

            // 2️⃣ Test findById
            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Caisse> found = dao.findById(caisse.getId());
            found.ifPresentOrElse(
                c -> System.out.println("Found: ID=" + c.getId() + ", Solde=" + c.getSoldeActuel()),
                () -> System.out.println("Caisse not found!")
            );

            // 3️⃣ Test findByReparateurId
            System.out.println("\n=== TEST 3: FIND BY REPARATEUR ID ===");
            Optional<Caisse> byRep = dao.findByReparateurId(1L);
            byRep.ifPresent(c -> System.out.println("Caisse for reparateur 1: " + c.getSoldeActuel()));

            // 4️⃣ Test findByType
            System.out.println("\n=== TEST 4: FIND BY TYPE ===");
            List<Caisse> byType = dao.findByType(TypeCaisse.GLOBALE);
            byType.forEach(c -> System.out.println("Caisse type CASH: ID=" + c.getId() + ", Solde=" + c.getSoldeActuel()));

            // 5️⃣ Test findAll
            System.out.println("\n=== TEST 5: FIND ALL ===");
            List<Caisse> all = dao.findAll();
            all.forEach(c -> System.out.println("Caisse ID=" + c.getId() + ", Solde=" + c.getSoldeActuel()));

            // 6️⃣ Test update
            System.out.println("\n=== TEST 6: UPDATE ===");
            caisse.setSoldeActuel(1500.0);
            dao.update(caisse);
            System.out.println("Caisse updated: new solde=" + caisse.getSoldeActuel());

            // 7️⃣ Test delete
            System.out.println("\n=== TEST 7: DELETE ===");
            boolean deleted = dao.deleteById(caisse.getId());
            System.out.println("Deleted: " + deleted);

            // 8️⃣ Verify deletion
            System.out.println("\n=== TEST 8: VERIFY DELETE ===");
            Optional<Caisse> afterDelete = dao.findById(caisse.getId());
            System.out.println("Exists after delete? " + afterDelete.isPresent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
