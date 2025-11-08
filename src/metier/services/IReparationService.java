package metier.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import metier.enums.EtatReparation;
import metier.model.Reparation;

public interface IReparationService {
    
    // CRUD de base
    Reparation enregistrerReparation(Reparation reparation) throws Exception;
    Reparation modifierReparation(Reparation reparation) throws Exception;
    boolean supprimerReparation(Long id) throws Exception;
    Optional<Reparation> trouverParId(Long id) throws Exception;
    List<Reparation> listerReparations() throws Exception;

    // Recherches spécifiques
    Optional<Reparation> trouverParCodeUnique(String codeUnique) throws Exception;
    List<Reparation> listerParReparateur(Long reparateurId) throws Exception;
    List<Reparation> listerParStatut(EtatReparation statut) throws Exception;
    List<Reparation> listerParDate(Date date) throws Exception;

    // Opération métier
    boolean changerStatut(Long reparationId, EtatReparation nouveauStatut) throws Exception;
}
