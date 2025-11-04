package dao;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import dao.impl.DetteDaoImpl;
import metier.model.Caisse;
import metier.model.Dette;

public class DetteDaoImplTest {
	public static void main(String[] args) {
        DetteDaoImpl dao = new DetteDaoImpl();

        try {
            // 1️⃣ Create a new Dette
            Dette dette = new Dette();
            dette.setMontant(500.0);
            dette.setDonnepar("Client A");
            dette.setRecuPar("Reparateur B");
            dette.setDate(Date.valueOf("2025-11-04")); // yyyy-mm-dd
            dette.setReglee(false);

            // Optional: associate with a Caisse
            Caisse caisse = new Caisse();
            caisse.setId(1L); // make sure this ID exists
            dette.setCaisse(caisse);

            System.out.println("\n=== TEST 1: SAVE ===");
            dette = dao.save(dette);
            System.out.println("Dette saved with ID: " + dette.getId());

            // 2️⃣ Test findById
            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Dette> found = dao.findById(dette.getId());
            found.ifPresentOrElse(
                d -> System.out.println("Found Dette: montant=" + d.getMontant() + ", donnePar=" + d.getDonnepar()),
                () -> System.out.println("Dette not found!")
            );

            // 3️⃣ Test findAll
            System.out.println("\n=== TEST 3: FIND ALL ===");
            List<Dette> all = dao.findAll();
            all.forEach(d -> System.out.println("Dette ID=" + d.getId() + ", montant=" + d.getMontant()));

            // 4️⃣ Test findByCaisseId
            System.out.println("\n=== TEST 4: FIND BY CAISSE ID ===");
            List<Dette> byCaisse = dao.findByCaisseId(1L);
            byCaisse.forEach(d -> System.out.println("Dette for caisse 1: ID=" + d.getId() + ", montant=" + d.getMontant()));

            // 5️⃣ Test findByDonnePar
            System.out.println("\n=== TEST 5: FIND BY DONNE PAR ===");
            List<Dette> byDonnePar = dao.findByDonnePar("Client A");
            byDonnePar.forEach(d -> System.out.println("Dette donnePar=Client A: ID=" + d.getId()));

            // 6️⃣ Test findByRecuPar
            System.out.println("\n=== TEST 6: FIND BY RECU PAR ===");
            List<Dette> byRecuPar = dao.findByRecuPar("Reparateur B");
            byRecuPar.forEach(d -> System.out.println("Dette recuPar=Reparateur B: ID=" + d.getId()));

            // 7️⃣ Test findByDate
            System.out.println("\n=== TEST 7: FIND BY DATE ===");
            List<Dette> byDate = dao.findByDate(Date.valueOf("2025-11-04"));
            byDate.forEach(d -> System.out.println("Dette date=2025-11-04: ID=" + d.getId()));

            // 8️⃣ Test findByReglee
            System.out.println("\n=== TEST 8: FIND BY REGLEE ===");
            List<Dette> byReglee = dao.findByReglee(false);
            byReglee.forEach(d -> System.out.println("Dette reglee=false: ID=" + d.getId()));

            // 9️⃣ Test update
            System.out.println("\n=== TEST 9: UPDATE ===");
            dette.setMontant(750.0);
            dette.setReglee(true);
            dao.update(dette);
            System.out.println("Dette updated: new montant=" + dette.getMontant() + ", reglee=" + dette.isReglee());

            // 10️⃣ Test delete
            System.out.println("\n=== TEST 10: DELETE ===");
            boolean deleted = dao.deleteById(dette.getId());
            System.out.println("Deleted: " + deleted);

            // 11️⃣ Verify deletion
            System.out.println("\n=== TEST 11: VERIFY DELETE ===");
            Optional<Dette> afterDelete = dao.findById(dette.getId());
            System.out.println("Exists after delete? " + afterDelete.isPresent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
