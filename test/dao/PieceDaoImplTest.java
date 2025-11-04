package dao;

import java.util.List;
import java.util.Optional;

import dao.impl.PieceDaoImpl;
import metier.model.Appareil;
import metier.model.Piece;

public class PieceDaoImplTest {
	public static void main(String[] args) {
        PieceDaoImpl dao = new PieceDaoImpl();

        try {
            // 1️⃣ Create a new Piece
            Piece piece = new Piece();
            piece.setNom("Écran LCD");
            piece.setPrixUnitaire(120.50);
            piece.setEtatRemplacement("Neuf");

            // Optional: associate with Appareil
            Appareil appareil = new Appareil();
            appareil.setId(1L); // make sure this Appareil exists
            piece.setAppareil(appareil);

            System.out.println("\n=== TEST 1: SAVE ===");
            piece = dao.save(piece);
            System.out.println("Piece saved with ID: " + piece.getId());

            // 2️⃣ Test findById
            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Piece> found = dao.findById(piece.getId());
            found.ifPresentOrElse(
                p -> System.out.println("Found Piece: nom=" + p.getNom() + ", prix=" + p.getPrixUnitaire()),
                () -> System.out.println("Piece not found!")
            );

            // 3️⃣ Test findAll
            System.out.println("\n=== TEST 3: FIND ALL ===");
            List<Piece> all = dao.findAll();
            all.forEach(p -> System.out.println("Piece ID=" + p.getId() + ", nom=" + p.getNom()));

            // 4️⃣ Test findByAppareilId
            System.out.println("\n=== TEST 4: FIND BY APPAREIL ID ===");
            List<Piece> byAppareil = dao.findByAppareilId(1L);
            byAppareil.forEach(p -> System.out.println("Piece for Appareil 1: ID=" + p.getId()));

            // 5️⃣ Test findByNom
            System.out.println("\n=== TEST 5: FIND BY NOM ===");
            List<Piece> byNom = dao.findByNom("Écran LCD");
            byNom.forEach(p -> System.out.println("Piece nom='Écran LCD': ID=" + p.getId()));

            // 6️⃣ Test findByEtatRemplacement
            System.out.println("\n=== TEST 6: FIND BY ETAT REMPLACEMENT ===");
            List<Piece> byEtat = dao.findByEtatRemplacement("Neuf");
            byEtat.forEach(p -> System.out.println("Piece etat='Neuf': ID=" + p.getId()));

            // 7️⃣ Test update
            System.out.println("\n=== TEST 7: UPDATE ===");
            piece.setNom("Écran OLED");
            piece.setPrixUnitaire(200.00);
            piece.setEtatRemplacement("Occasion");
            dao.update(piece);
            System.out.println("Piece updated: new nom=" + piece.getNom() + ", prix=" + piece.getPrixUnitaire());

            // 8️⃣ Test delete
            System.out.println("\n=== TEST 8: DELETE ===");
            boolean deleted = dao.deleteById(piece.getId());
            System.out.println("Deleted: " + deleted);

            // 9️⃣ Verify deletion
            System.out.println("\n=== TEST 9: VERIFY DELETE ===");
            Optional<Piece> afterDelete = dao.findById(piece.getId());
            System.out.println("Exists after delete? " + afterDelete.isPresent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
