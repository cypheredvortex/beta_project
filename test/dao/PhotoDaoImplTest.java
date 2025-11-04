package dao;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import dao.impl.PhotoDaoImpl;
import metier.enums.SourcePhoto;
import metier.model.Client;
import metier.model.Photo;
import metier.model.Reparation;

public class PhotoDaoImplTest {
	public static void main(String[] args) {
        PhotoDaoImpl dao = new PhotoDaoImpl();

        try {
            // 1️⃣ Create a new Photo
            Photo photo = new Photo();
            photo.setChemin("C:/images/test_photo.jpg");
            photo.setDatePrise(Date.valueOf("2025-11-04"));
            photo.setSource(SourcePhoto.CLIENT);

            Client client = new Client();
            client.setId(1L); // make sure this exists
            photo.setClient(client);

            System.out.println("\n=== TEST 1: SAVE ===");
            photo = dao.save(photo);
            System.out.println("Photo saved with ID: " + photo.getId());

            // 2️⃣ Test findById
            System.out.println("\n=== TEST 2: FIND BY ID ===");
            Optional<Photo> found = dao.findById(photo.getId());
            found.ifPresentOrElse(
                p -> System.out.println("Found Photo: chemin=" + p.getChemin() + ", source=" + p.getSource()),
                () -> System.out.println("Photo not found!")
            );

            // 3️⃣ Test findAll
            System.out.println("\n=== TEST 3: FIND ALL ===");
            List<Photo> all = dao.findAll();
            all.forEach(p -> System.out.println("Photo ID=" + p.getId() + ", chemin=" + p.getChemin()));

            // 5️⃣ Test findByClientId
            System.out.println("\n=== TEST 5: FIND BY CLIENT ID ===");
            List<Photo> byClient = dao.findByClientId(1L);
            byClient.forEach(p -> System.out.println("Photo for client 1: ID=" + p.getId()));

            // 6️⃣ Test findByDatePrise
            System.out.println("\n=== TEST 6: FIND BY DATE PRISE ===");
            List<Photo> byDate = dao.findByDatePrise(Date.valueOf("2025-11-04"));
            byDate.forEach(p -> System.out.println("Photo date=2025-11-04: ID=" + p.getId()));

            // 7️⃣ Test findBySourceType
            System.out.println("\n=== TEST 7: FIND BY SOURCE TYPE ===");
            List<Photo> bySource = dao.findBySourceType("CLIENT");
            bySource.forEach(p -> System.out.println("Photo source=CLIENT: ID=" + p.getId()));

            // 8️⃣ Test update
            System.out.println("\n=== TEST 8: UPDATE ===");
            photo.setChemin("C:/images/updated_photo.jpg");
            photo.setSource(SourcePhoto.CAMERA);
            dao.update(photo);
            System.out.println("Photo updated: new chemin=" + photo.getChemin() + ", source=" + photo.getSource());

            // 9️⃣ Test delete
            System.out.println("\n=== TEST 9: DELETE ===");
            boolean deleted = dao.deleteById(photo.getId());
            System.out.println("Deleted: " + deleted);

            // 10️⃣ Verify deletion
            System.out.println("\n=== TEST 10: VERIFY DELETE ===");
            Optional<Photo> afterDelete = dao.findById(photo.getId());
            System.out.println("Exists after delete? " + afterDelete.isPresent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
