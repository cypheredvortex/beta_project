package metier.servicesImpl;

import java.util.List;
import java.util.Optional;

import dao.interfaces.BoutiqueDao;
import dao.impl.BoutiqueDaoImpl;
import metier.model.Boutique;
import metier.services.IBoutiqueService;

public class BoutiqueServiceImpl implements IBoutiqueService {

    private final BoutiqueDao boutiqueDao;

    public BoutiqueServiceImpl() {
        this.boutiqueDao = new BoutiqueDaoImpl();
    }

    // =========================================================
    // 🔹 Créer une nouvelle boutique
    // =========================================================
    @Override
    public Boutique creerBoutique(Boutique boutique) throws Exception {
        if (boutique == null) {
            throw new IllegalArgumentException("La boutique ne peut pas être nulle !");
        }
        if (boutique.getNom() == null || boutique.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la boutique est obligatoire !");
        }
        if (boutique.getAddresse() == null || boutique.getAddresse().isEmpty()) {
            throw new IllegalArgumentException("L'adresse de la boutique est obligatoire !");
        }
        return boutiqueDao.save(boutique);
    }

    // =========================================================
    // 🔹 Modifier une boutique existante
    // =========================================================
    @Override
    public Boutique modifierBoutique(Boutique boutique) throws Exception {
        if (boutique == null || boutique.getId() <= 0) {
            throw new IllegalArgumentException("Boutique invalide !");
        }
        return boutiqueDao.update(boutique);
    }

    // =========================================================
    // 🔹 Supprimer une boutique par ID
    // =========================================================
    @Override
    public boolean supprimerBoutique(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalide !");
        }
        return boutiqueDao.deleteById(id);
    }

    // =========================================================
    // 🔹 Trouver une boutique par ID
    // =========================================================
    @Override
    public Optional<Boutique> trouverParId(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalide !");
        }
        return boutiqueDao.findById(id);
    }

    // =========================================================
    // 🔹 Lister toutes les boutiques
    // =========================================================
    @Override
    public List<Boutique> listerBoutiques() throws Exception {
        return boutiqueDao.findAll();
    }

    // =========================================================
    // 🔹 Lister les boutiques d’un propriétaire
    // =========================================================
    @Override
    public List<Boutique> listerParProprietaire(long proprietaireId) throws Exception {
        if (proprietaireId <= 0) {
            throw new IllegalArgumentException("ID du propriétaire invalide !");
        }
        return boutiqueDao.findByProprietaireId(proprietaireId);
    }
}
