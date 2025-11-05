package metier.services;

import java.util.List;
import java.util.Optional;

import metier.enums.TypeCaisse;
import metier.model.Caisse;

public interface ICaisseService {

    // 🔹 CRUD de base
    Caisse enregistrerOperation(Caisse caisse) throws Exception;
    Caisse modifierOperation(Caisse caisse) throws Exception;
    boolean supprimerOperation(Long id) throws Exception;
    Optional<Caisse> trouverParId(Long id) throws Exception;
    List<Caisse> listerOperations() throws Exception;

    // 🔹 Recherches spécifiques
    Optional<Caisse> trouverParReparateur(Long reparateurId) throws Exception;
    List<Caisse> listerParType(TypeCaisse type) throws Exception;

    // 🔹 Calculs / traitements métier
    double calculerRecetteTotale() throws Exception;
}
