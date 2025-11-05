package metier.services;

import java.util.List;
import java.util.Optional;

import metier.enums.RoleCompte;
import metier.model.Compte;

public interface IUtilisateurService {

    // 🔹 Authentification
    Optional<Compte> seConnecter(String login, String motDePasse) throws Exception;

    // 🔹 CRUD de base
    Compte creerCompte(Compte compte) throws Exception;
    Compte modifierCompte(Compte compte) throws Exception;
    boolean supprimerCompte(Long id) throws Exception;
    Optional<Compte> trouverParId(Long id) throws Exception;
    List<Compte> listerComptes() throws Exception;

    // 🔹 Recherches spécifiques
    List<Compte> listerParRole(RoleCompte role) throws Exception;
    List<Compte> listerActifs(boolean actif) throws Exception;
    Optional<Compte> trouverParReparateur(Long reparateurId) throws Exception;
}
