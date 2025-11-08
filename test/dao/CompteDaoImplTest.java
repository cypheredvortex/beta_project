package dao;

import java.util.List;
import java.util.Optional;

import dao.impl.CompteDaoImpl;
import metier.enums.RoleCompte;
import metier.model.Compte;
import metier.model.Reparateur;

public class CompteDaoImplTest {
	public static void main(String[] args) {
        CompteDaoImpl dao = new CompteDaoImpl();

        try {
            // 1️⃣ Create a new Compte
            Compte compte = new Compte();
            compte.setLogin("adminTest");
            compte.setMotDePasse("password123");
            compte.setRole(RoleCompte.proprietaire);
            compte.setActif(true);

            // Optional: associate with a Reparateur
            Reparateur rep = new Reparateur();
            rep.setId(1L); // make sure this ID exists in DB
            compte.setReparateur(rep);

            System.out.println("\n=== TEST 1: SAVE ===");
            compte = dao.save(compte);
            System.out.println("Compte saved with ID: " + compte.getId());

            // 2️⃣ Test findById
            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Compte> found = dao.findById(compte.getId());
            found.ifPresentOrElse(
                c -> System.out.println("Found: login=" + c.getLogin() + ", role=" + c.getRole()),
                () -> System.out.println("Compte not found!")
            );

            // 3️⃣ Test findByLogin
            System.out.println("\n=== TEST 3: FIND BY LOGIN ===");
            Optional<Compte> byLogin = dao.findByLogin("adminTest");
            byLogin.ifPresent(c -> System.out.println("Found by login: ID=" + c.getId()));

            // 4️⃣ Test findByReparateurId
            System.out.println("\n=== TEST 4: FIND BY REPARATEUR ID ===");
            Optional<Compte> byRep = dao.findByReparateurId(1L);
            byRep.ifPresent(c -> System.out.println("Compte for reparateur 1: login=" + c.getLogin()));

            // 5️⃣ Test findByRole
            System.out.println("\n=== TEST 5: FIND BY ROLE ===");
            List<Compte> admins = dao.findByRole(RoleCompte.reparateur);
            admins.forEach(c -> System.out.println("Admin compte ID=" + c.getId() + ", login=" + c.getLogin()));

            // 6️⃣ Test findByActif
            System.out.println("\n=== TEST 6: FIND BY ACTIF ===");
            List<Compte> actifs = dao.findByActif(true);
            actifs.forEach(c -> System.out.println("Active compte: login=" + c.getLogin()));

            // 7️⃣ Test findAll
            System.out.println("\n=== TEST 7: FIND ALL ===");
            List<Compte> all = dao.findAll();
            all.forEach(c -> System.out.println("Compte ID=" + c.getId() + ", login=" + c.getLogin()));

            // 8️⃣ Test update
            System.out.println("\n=== TEST 8: UPDATE ===");
            compte.setMotDePasse("newPassword456");
            compte.setActif(false);
            dao.update(compte);
            System.out.println("Compte updated: new password=" + compte.getMotDePasse() + ", actif=" + compte.isActif());

            // 9️⃣ Test delete
            System.out.println("\n=== TEST 9: DELETE ===");
            boolean deleted = dao.deleteById(compte.getId());
            System.out.println("Deleted: " + deleted);

            // 10️⃣ Verify deletion
            System.out.println("\n=== TEST 10: VERIFY DELETE ===");
            Optional<Compte> afterDelete = dao.findById(compte.getId());
            System.out.println("Exists after delete? " + afterDelete.isPresent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
