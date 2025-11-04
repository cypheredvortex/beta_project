package dao;

import java.util.List;
import java.util.Optional;

import dao.impl.ClientDaoImpl;
import metier.model.Client;
import metier.model.Photo;

public class ClientDaoImplTest {
	public static void main(String[] args) {
        ClientDaoImpl dao = new ClientDaoImpl();

        try {
            // 1️⃣ Create a new Client
            Client client = new Client();
            client.setNom("Doe");
            client.setPrenom("John");
            client.setEmail("john.doe@example.com");
            client.setTelephone("0600000000");

            // Optional: associate a photo
            Photo photo = new Photo();
            photo.setId(1); // Ensure this exists in DB
            client.setPhoto(photo);

            System.out.println("\n=== TEST 1: SAVE ===");
            client = dao.save(client);
            System.out.println("Client saved with ID: " + client.getId());

            // 2️⃣ Test findById
            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Client> found = dao.findById((int) client.getId());
            found.ifPresentOrElse(
                c -> System.out.println("Found: " + c.getNom() + " " + c.getPrenom()),
                () -> System.out.println("Client not found!")
            );

            // 3️⃣ Test findByEmail
            System.out.println("\n=== TEST 3: FIND BY EMAIL ===");
            Optional<Client> byEmail = dao.findByEmail("john.doe@example.com");
            byEmail.ifPresent(c -> System.out.println("Found by email: " + c.getNom()));

            // 4️⃣ Test findByTelephone
            System.out.println("\n=== TEST 4: FIND BY TELEPHONE ===");
            Optional<Client> byTel = dao.findByTelephone("0600000000");
            byTel.ifPresent(c -> System.out.println("Found by telephone: " + c.getNom()));

            // 5️⃣ Test findByPhotoId
            System.out.println("\n=== TEST 5: FIND BY PHOTO ID ===");
            Optional<Client> byPhoto = dao.findByPhotoId(1);
            byPhoto.ifPresent(c -> System.out.println("Found by photo: " + c.getNom()));

            // 6️⃣ Test findAll
            System.out.println("\n=== TEST 6: FIND ALL ===");
            List<Client> all = dao.findAll();
            all.forEach(c -> System.out.println("Client ID=" + c.getId() + ", Nom=" + c.getNom()));

            // 7️⃣ Test update
            System.out.println("\n=== TEST 7: UPDATE ===");
            client.setTelephone("0611111111");
            dao.update(client);
            System.out.println("Client updated: new telephone=" + client.getTelephone());

            // 8️⃣ Test delete
            System.out.println("\n=== TEST 8: DELETE ===");
            boolean deleted = dao.deleteById((int) client.getId());
            System.out.println("Deleted: " + deleted);

            // 9️⃣ Verify deletion
            System.out.println("\n=== TEST 9: VERIFY DELETE ===");
            Optional<Client> afterDelete = dao.findById((int) client.getId());
            System.out.println("Exists after delete? " + afterDelete.isPresent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
