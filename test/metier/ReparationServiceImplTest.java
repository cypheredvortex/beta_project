package metier;


import metier.model.Client;
import metier.model.Reparation;
import metier.enums.EtatReparation;
import metier.servicesImpl.ReparationServiceImpl;

public class ReparationServiceImplTest {

	public static void main(String[] args) {
		try {
			// Instancier le service
			ReparationServiceImpl service = new ReparationServiceImpl();

			// Simuler un client existant
			Client client = new Client();
			client.setId(1L); // 🔸 Doit exister dans ta base de données
			client.setNom("Dupont");
			client.setTelephone("0600000000");

			// Créer une nouvelle réparation
			Reparation rep = new Reparation();
			rep.setDescription("Remplacement de batterie iPhone 13");
			rep.setPrixConvenu(80.0);
			rep.setClient(client);

			// Appel du service
			Reparation saved = service.enregistrerReparation(rep);

			// Affichage du résultat
			System.out.println("✅ Réparation créée avec succès !");
			System.out.println("ID : " + saved.getId());
			System.out.println("Code unique : " + saved.getCodeUnique());
			System.out.println("Statut : " + saved.getStatut());
			System.out.println("Date création : " + saved.getDateCreation());

		} catch (Exception e) {
			System.err.println("❌ Erreur : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
