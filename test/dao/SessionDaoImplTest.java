package dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dao.impl.SessionDaoImpl;
import metier.model.Compte;
import metier.model.Session;

public class SessionDaoImplTest {
	public static void main(String[] args) {
        SessionDaoImpl dao = new SessionDaoImpl();

        try {
            System.out.println("=== TEST 1: SAVE ===");

            // Create a dummy compte for testing (must exist in DB)
            Compte compte = new Compte();
            compte.setId(1L);

            // Create a new session
            Session s = new Session();
            s.setToken("token-" + System.currentTimeMillis());
            s.setDateCreation(Date.valueOf(LocalDate.now()));
            s.setDateExpiration(Date.valueOf(LocalDate.now().plusDays(7)));
            s.setCompte(compte);

            Session saved = dao.save(s);
            System.out.println("Saved session: " + saved.getId() + " | " + saved.getToken());

            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Session> foundById = dao.findById(saved.getId());
            foundById.ifPresent(sess -> 
                System.out.println("Found by ID: " + sess.getId() + " | " + sess.getToken())
            );

            System.out.println("\n=== TEST 3: FIND BY TOKEN ===");
            Optional<Session> foundByToken = dao.findByToken(saved.getToken());
            foundByToken.ifPresent(sess -> 
                System.out.println("Found by token: " + sess.getId() + " | " + sess.getToken())
            );

            System.out.println("\n=== TEST 4: FIND BY COMPTE ID ===");
            List<Session> byCompte = dao.findByCompteId(compte.getId());
            byCompte.forEach(sess -> 
                System.out.println("Session by compte: " + sess.getId() + " | " + sess.getToken())
            );

            System.out.println("\n=== TEST 5: FIND ALL ===");
            List<Session> all = dao.findAll();
            all.forEach(sess -> 
                System.out.println("Session: " + sess.getId() + " | " + sess.getToken())
            );

            System.out.println("\n=== TEST 6: UPDATE ===");
            saved.setToken(saved.getToken() + "-updated");
            saved.setDateExpiration(Date.valueOf(LocalDate.now().plusDays(14)));
            dao.update(saved);
            Optional<Session> updated = dao.findById(saved.getId());
            updated.ifPresent(sess -> 
                System.out.println("Updated session: " + sess.getId() + " | " + sess.getToken() + " | Exp: " + sess.getDateExpiration())
            );

            System.out.println("\n=== TEST 7: DELETE ===");
            boolean deleted = dao.deleteById(saved.getId());
            System.out.println("Deleted session: " + deleted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
