package dao.interfaces;

import java.util.List;
import java.util.Optional;

import metier.model.Proprietaire;

public interface ProprietaireDao {
    Proprietaire save(Proprietaire proprietaire) throws Exception;
    Optional<Proprietaire> findById(Long id) throws Exception;
    List<Proprietaire> findAll() throws Exception;
    Proprietaire update(Proprietaire proprietaire) throws Exception;
    boolean deleteById(Long id) throws Exception;
    Optional<Proprietaire> findByEmail(String email) throws Exception;
    Optional<Proprietaire> findByTelephone(String telephone) throws Exception;
    List<Proprietaire> findByBoutiqueId(Long boutiqueId) throws Exception;
}
