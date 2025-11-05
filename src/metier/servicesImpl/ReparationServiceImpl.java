package metier.servicesImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import dao.interfaces.ReparationDao;
import dao.impl.ReparationDaoImpl;
import metier.enums.EtatReparation;
import metier.model.Reparation;
import metier.services.IReparationService;

public class ReparationServiceImpl implements IReparationService {

    private final ReparationDao reparationDao;

    public ReparationServiceImpl() {
        this.reparationDao = new ReparationDaoImpl();
    }

    // =========================================================
    // 🔹 Enregistrer une réparation
    // =========================================================
    @Override
    public Reparation enregistrerReparation(Reparation reparation) throws Exception {
        if (reparation == null)
            throw new IllegalArgumentException("La réparation ne peut pas être nulle !");
        if (reparation.getClient() == null)
            throw new IllegalArgumentException("Le client est obligatoire !");
        if (reparation.getDescription() == null || reparation.getDescription().isEmpty())
            throw new IllegalArgumentException("La description est obligatoire !");
        if (reparation.getStatut() == null)
            reparation.setStatut(EtatReparation.EN_COURS);

        // ✅ Date automatique
        if (reparation.getDateCreation() == null)
            reparation.setDateCreation(new Date());

        // ✅ Génération automatique du code unique
        if (reparation.getCodeUnique() == null || reparation.getCodeUnique().isEmpty()) {
            String code = genererCodeUnique();
            reparation.setCodeUnique(code);
        }

        return reparationDao.save(reparation);
    }

    /**
     * 🔧 Génère un code unique de la forme REP-2025-00001
     */
    private String genererCodeUnique() {
        String prefix = "REP";
        String annee = String.valueOf(java.time.Year.now().getValue());
        int random = (int) (Math.random() * 99999);
        return String.format("%s-%s-%05d", prefix, annee, random);
    }

    // =========================================================
    // 🔹 Modifier une réparation
    // =========================================================
    @Override
    public Reparation modifierReparation(Reparation reparation) throws Exception {
        if (reparation == null || reparation.getId() <= 0)
            throw new IllegalArgumentException("Réparation invalide !");
        return reparationDao.update(reparation);
    }

    // =========================================================
    // 🔹 Supprimer une réparation
    // =========================================================
    @Override
    public boolean supprimerReparation(Long id) throws Exception {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID invalide !");
        return reparationDao.deleteById(id);
    }

    // =========================================================
    // 🔹 Trouver une réparation par ID
    // =========================================================
    @Override
    public Optional<Reparation> trouverParId(Long id) throws Exception {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID invalide !");
        return reparationDao.findById(id);
    }

    // =========================================================
    // 🔹 Trouver une réparation par code unique
    // =========================================================
    @Override
    public Optional<Reparation> trouverParCodeUnique(String codeUnique) throws Exception {
        if (codeUnique == null || codeUnique.isEmpty())
            throw new IllegalArgumentException("Le code unique est obligatoire !");
        return reparationDao.findByCodeUnique(codeUnique);
    }

    // =========================================================
    // 🔹 Lister toutes les réparations
    // =========================================================
    @Override
    public List<Reparation> listerReparations() throws Exception {
        return reparationDao.findAll();
    }

    // =========================================================
    // 🔹 Lister par réparateur
    // =========================================================
    @Override
    public List<Reparation> listerParReparateur(Long reparateurId) throws Exception {
        if (reparateurId == null || reparateurId <= 0)
            throw new IllegalArgumentException("ID réparateur invalide !");
        return reparationDao.findByReparateurId(reparateurId);
    }

    // =========================================================
    // 🔹 Lister par téléphone du client
    // =========================================================
    @Override
    public List<Reparation> listerParClientPhone(String phone) throws Exception {
        if (phone == null || phone.isEmpty())
            throw new IllegalArgumentException("Le numéro de téléphone est obligatoire !");
        return reparationDao.findByClientPhone(phone);
    }

    // =========================================================
    // 🔹 Lister par statut
    // =========================================================
    @Override
    public List<Reparation> listerParStatut(EtatReparation statut) throws Exception {
        if (statut == null)
            throw new IllegalArgumentException("Le statut est obligatoire !");
        return reparationDao.findByStatut(statut);
    }

    // =========================================================
    // 🔹 Lister par date de création
    // =========================================================
    @Override
    public List<Reparation> listerParDate(Date date) throws Exception {
        if (date == null)
            throw new IllegalArgumentException("La date est obligatoire !");
        return reparationDao.findByDateCreation(date);
    }

    // =========================================================
    // 🔹 Changer le statut d’une réparation
    // =========================================================
    @Override
    public boolean changerStatut(Long reparationId, EtatReparation nouveauStatut) throws Exception {
        if (reparationId == null || reparationId <= 0)
            throw new IllegalArgumentException("ID réparation invalide !");
        if (nouveauStatut == null)
            throw new IllegalArgumentException("Le nouveau statut ne peut pas être nul !");

        Optional<Reparation> reparationOpt = reparationDao.findById(reparationId);
        if (reparationOpt.isPresent()) {
            Reparation rep = reparationOpt.get();
            rep.setStatut(nouveauStatut);
            reparationDao.update(rep);
            return true;
        }
        return false;
    }
}
