package metier.servicesImpl;

import java.util.List;
import java.util.Optional;

import dao.interfaces.CaisseDao;
import dao.impl.CaisseDaoImpl;
import metier.enums.TypeCaisse;
import metier.model.Caisse;
import metier.services.ICaisseService;

public class CaisseServiceImpl implements ICaisseService {

    private final CaisseDao caisseDao;

    public CaisseServiceImpl() {
        this.caisseDao = new CaisseDaoImpl();
    }

    // --------------------------------------------------------------------
    // 🔹 Enregistrer une nouvelle opération de caisse
    // --------------------------------------------------------------------
    @Override
    public Caisse enregistrerOperation(Caisse caisse) throws Exception {
        if (caisse == null) {
            throw new IllegalArgumentException("L'opération ne peut pas être nulle !");
        }
        if (caisse.getSoldeActuel() == 0) {
            throw new IllegalArgumentException("Le solde ne peut pas être nul !");
        }
        if (caisse.getType() == null) {
            throw new IllegalArgumentException("Le type de l'opération doit être précisé !");
        }

        return caisseDao.save(caisse);
    }

    // --------------------------------------------------------------------
    // 🔹 Modifier une opération existante
    // --------------------------------------------------------------------
    @Override
    public Caisse modifierOperation(Caisse caisse) throws Exception {
        if (caisse == null || caisse.getId() <= 0) {
            throw new IllegalArgumentException("Opération invalide !");
        }
        return caisseDao.update(caisse);
    }

    // --------------------------------------------------------------------
    // 🔹 Supprimer une opération
    // --------------------------------------------------------------------
    @Override
    public boolean supprimerOperation(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalide !");
        }
        return caisseDao.deleteById(id);
    }

    // --------------------------------------------------------------------
    // 🔹 Lister toutes les opérations
    // --------------------------------------------------------------------
    @Override
    public List<Caisse> listerOperations() throws Exception {
        return caisseDao.findAll();
    }

    // --------------------------------------------------------------------
    // 🔹 Trouver une opération par ID
    // --------------------------------------------------------------------
    @Override
    public Optional<Caisse> trouverParId(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalide !");
        }
        return caisseDao.findById(id);
    }

    // --------------------------------------------------------------------
    // 🔹 Trouver la caisse d’un réparateur
    // --------------------------------------------------------------------
    @Override
    public Optional<Caisse> trouverParReparateur(Long reparateurId) throws Exception {
        if (reparateurId == null || reparateurId <= 0) {
            throw new IllegalArgumentException("ID réparateur invalide !");
        }
        return caisseDao.findByReparateurId(reparateurId);
    }

    // --------------------------------------------------------------------
    // 🔹 Lister les opérations selon le type (ENTREE ou SORTIE)
    // --------------------------------------------------------------------
    @Override
    public List<Caisse> listerParType(TypeCaisse type) throws Exception {
        if (type == null) {
            throw new IllegalArgumentException("Le type ne peut pas être nul !");
        }
        return caisseDao.findByType(type);
    }

    // --------------------------------------------------------------------
    // 🔹 Calculer la recette totale (somme des soldes)
    // --------------------------------------------------------------------
    @Override
    public double calculerRecetteTotale() throws Exception {
        List<Caisse> caisses = caisseDao.findAll();
        if (caisses == null || caisses.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (Caisse c : caisses) {
            total += c.getSoldeActuel();
        }
        return total;
    }
}
