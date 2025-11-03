package dao.interfaces;

import metier.enums.RoleCompte;
import metier.model.Compte;
import java.util.List;
import java.util.Optional;

public interface CompteDao {
    Compte save(Compte compte) throws Exception;
    Optional<Compte> findById(Long id) throws Exception;
    Optional<Compte> findByLogin(String login) throws Exception;
    List<Compte> findAll() throws Exception;
    Compte update(Compte compte) throws Exception;
    boolean deleteById(Long id) throws Exception;
    List<Compte> findByRole(RoleCompte role) throws Exception;
    List<Compte> findByActif(boolean actif) throws Exception;
    Optional<Compte> findByReparateurId(Long reparateurId) throws Exception;
}