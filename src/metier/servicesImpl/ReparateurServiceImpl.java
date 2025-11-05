package metier.servicesImpl;

import java.util.List;
import java.util.Optional;

import dao.impl.ReparateurDaoImpl;
import dao.interfaces.ReparateurDao;
import metier.model.Reparateur;
import metier.services.IReparateurService;

public class ReparateurServiceImpl implements IReparateurService {

    private final ReparateurDao reparateurDao;

    public ReparateurServiceImpl() {
        this.reparateurDao = new ReparateurDaoImpl();
    }

    // =========================================================
    // 🔹 Enregistrer un réparateur
    // =========================================================
    @Override
    public Reparateur enregistrerReparateur(Reparateur reparateur) throws Exception {
        if (reparateur == null)
            throw new IllegalArgumentException("Le réparateur ne peut pas être nul !");
        if (reparateur.getNom() == null || reparateur.getNom().isEmpty())
            throw new IllegalArgumentException("Le nom du réparateur est obligatoire !");
        if (reparateur.getTelephone() == null || reparateur.getTelephone().isEmpty())
            throw new IllegalArgumentException("Le numéro de téléphone est obligatoire !");
        return reparateurDao.save(reparateur);
    }

    // =========================================================
    // 🔹 Modifier un réparateur
    // =========================================================
    @Override
    public Reparateur modifierReparateur(Reparateur reparateur) throws Exception {
        if (reparateur == null || reparateur.getId() <= 0)
            throw new IllegalArgumentException("Réparateur invalide !");
        return reparateurDao.update(reparateur);
    }

    // =========================================================
    // 🔹 Supprimer un réparateur
    // =========================================================
    @Override
    public boolean supprimerReparateur(Long id) throws Exception {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID invalide !");
        return reparateurDao.deleteById(id);
    }

    // =========================================================
    // 🔹 Trouver par ID
    // =========================================================
    @Override
    public Optional<Reparateur> trouverParId(Long id) throws Exception {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID invalide !");
        return reparateurDao.findById(id);
    }

    // =========================================================
    // 🔹 Lister tous les réparateurs
    // =========================================================
    @Override
    public List<Reparateur> listerReparateurs() throws Exception {
        return reparateurDao.findAll();
    }

    // =========================================================
    // 🔹 Trouver par email
    // =========================================================
    @Override
    public Optional<Reparateur> trouverParEmail(String email) throws Exception {
        if (email == null || email.isEmpty())
            throw new IllegalArgumentException("L'email est obligatoire !");
        return reparateurDao.findByEmail(email);
    }

    // =========================================================
    // 🔹 Trouver par téléphone
    // =========================================================
    @Override
    public Optional<Reparateur> trouverParTelephone(String telephone) throws Exception {
        if (telephone == null || telephone.isEmpty())
            throw new IllegalArgumentException("Le téléphone est obligatoire !");
        return reparateurDao.findByTelephone(telephone);
    }

    // =========================================================
    // 🔹 Lister par nom
    // =========================================================
    @Override
    public List<Reparateur> listerParNom(String nom) throws Exception {
        if (nom == null || nom.isEmpty())
            throw new IllegalArgumentException("Le nom est obligatoire !");
        return reparateurDao.findByNom(nom);
    }
}