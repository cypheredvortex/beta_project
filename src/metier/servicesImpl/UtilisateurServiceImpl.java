package metier.servicesImpl;

import dao.interfaces.CompteDao;
import dao.impl.CompteDaoImpl;
import metier.enums.RoleCompte;
import metier.model.Compte;
import metier.services.IUtilisateurService;

import java.util.List;
import java.util.Optional;

public class UtilisateurServiceImpl implements IUtilisateurService {

    private final CompteDao compteDao;

    public UtilisateurServiceImpl() {
        this.compteDao = new CompteDaoImpl();
    }

    // =========================================================
    // 🔐 Authentification
    // =========================================================
    @Override
    public Optional<Compte> seConnecter(String login, String motDePasse) throws Exception {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Le login ne peut pas être vide !");
        }
        if (motDePasse == null || motDePasse.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide !");
        }

        Optional<Compte> compteOpt = compteDao.findByLogin(login);
        if (compteOpt.isPresent()) {
            Compte c = compteOpt.get();
            if (c.getMotDePasse() != null && c.getMotDePasse().equals(motDePasse) && c.isActif()) {
                return compteOpt;
            }
        }
        return Optional.empty();
    }

    // =========================================================
    // 🔧 CRUD de base
    // =========================================================

    @Override
    public Compte creerCompte(Compte compte) throws Exception {
        if (compte == null) {
            throw new IllegalArgumentException("Le compte ne peut pas être nul !");
        }
        if (compte.getLogin() == null || compte.getLogin().isEmpty()) {
            throw new IllegalArgumentException("Le login est obligatoire !");
        }
        if (compte.getMotDePasse() == null || compte.getMotDePasse().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire !");
        }
        if (compte.getRole() == null) {
            compte.setRole(RoleCompte.REPARATEUR); // valeur par défaut
        }
        compte.setActif(true);

        return compteDao.save(compte);
    }

    @Override
    public Compte modifierCompte(Compte compte) throws Exception {
        if (compte == null || compte.getId() <= 0) {
            throw new IllegalArgumentException("Compte invalide !");
        }
        return compteDao.update(compte);
    }

    @Override
    public boolean supprimerCompte(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalide !");
        }
        return compteDao.deleteById(id);
    }

    @Override
    public Optional<Compte> trouverParId(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalide !");
        }
        return compteDao.findById(id);
    }

    @Override
    public List<Compte> listerComptes() throws Exception {
        return compteDao.findAll();
    }

    @Override
    public List<Compte> listerParRole(RoleCompte role) throws Exception {
        if (role == null) {
            throw new IllegalArgumentException("Le rôle ne peut pas être nul !");
        }
        return compteDao.findByRole(role);
    }

    @Override
    public List<Compte> listerActifs(boolean actif) throws Exception {
        return compteDao.findByActif(actif);
    }

    @Override
    public Optional<Compte> trouverParReparateur(Long reparateurId) throws Exception {
        if (reparateurId == null || reparateurId <= 0) {
            throw new IllegalArgumentException("ID réparateur invalide !");
        }
        return compteDao.findByReparateurId(reparateurId);
    }
}
