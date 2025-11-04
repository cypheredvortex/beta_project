package dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dao.impl.TransactionDaoImpl;
import metier.enums.TypeTransaction;
import metier.model.Caisse;
import metier.model.Transaction;

public class TransactionDaoImplTest {
	public static void main(String[] args) {
        TransactionDaoImpl dao = new TransactionDaoImpl();

        try {
            System.out.println("=== TEST 1: SAVE ===");

            // Create a dummy caisse (must exist in DB)
            Caisse caisse = new Caisse();
            caisse.setId(1L);

            // Create a new transaction
            Transaction t = new Transaction();
            t.setDate(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            t.setMontant(150.0);
            t.setType(TypeTransaction.AVANCE);
            t.setDescription("Paiement client");
            t.setContrepartie("Client XYZ");
            t.setCaisse(caisse);

            Transaction saved = dao.save(t);
            System.out.println("Saved transaction: " + saved.getId() + " | " + saved.getDescription());

            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Transaction> foundById = dao.findById(saved.getId());
            foundById.ifPresent(tx -> 
                System.out.println("Found by ID: " + tx.getId() + " | " + tx.getDescription())
            );

            System.out.println("\n=== TEST 3: FIND ALL ===");
            List<Transaction> all = dao.findAll();
            all.forEach(tx -> 
                System.out.println("Transaction: " + tx.getId() + " | " + tx.getDescription() + " | Type: " + tx.getType())
            );

            System.out.println("\n=== TEST 4: FIND BY CAISSE ID ===");
            List<Transaction> byCaisse = dao.findByCaisseId(caisse.getId());
            byCaisse.forEach(tx -> 
                System.out.println("Transaction by caisse: " + tx.getId() + " | " + tx.getDescription())
            );

            System.out.println("\n=== TEST 5: FIND BY TYPE ===");
            List<Transaction> byType = dao.findByType(TypeTransaction.AVANCE);
            byType.forEach(tx -> 
                System.out.println("Transaction by type: " + tx.getId() + " | " + tx.getDescription())
            );

            System.out.println("\n=== TEST 6: FIND BY DATE ===");
            Date today = Date.valueOf(LocalDate.now());
            List<Transaction> byDate = dao.findByDate(today);
            byDate.forEach(tx -> 
                System.out.println("Transaction by date: " + tx.getId() + " | " + tx.getDescription())
            );

            System.out.println("\n=== TEST 7: UPDATE ===");
            saved.setMontant(200.0);
            saved.setDescription("Paiement client - mis à jour");
            dao.update(saved);
            Optional<Transaction> updated = dao.findById(saved.getId());
            updated.ifPresent(tx -> 
                System.out.println("Updated transaction: " + tx.getId() + " | " + tx.getDescription() + " | Montant: " + tx.getMontant())
            );

            System.out.println("\n=== TEST 8: DELETE ===");
            boolean deleted = dao.deleteById(saved.getId());
            System.out.println("Deleted transaction: " + deleted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
