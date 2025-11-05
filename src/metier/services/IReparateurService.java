package metier.services;

import java.util.List;
import java.util.Optional;

import metier.model.Reparateur;

public interface IReparateurService {

    // CRUD de base
    Reparateur enregistrerReparateur(Reparateur reparateur) throws Exception;
    Reparateur modifierReparateur(Reparateur reparateur) throws Exception;
    boolean supprimerReparateur(Long id) throws Exception;
    Optional<Reparateur> trouverParId(Long id) throws Exception;
    List<Reparateur> listerReparateurs() throws Exception;

    // Recherches spécifiques
    Optional<Reparateur> trouverParEmail(String email) throws Exception;
    Optional<Reparateur> trouverParTelephone(String telephone) throws Exception;
    List<Reparateur> listerParNom(String nom) throws Exception;
}