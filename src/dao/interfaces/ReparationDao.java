package dao.interfaces;

import metier.enums.EtatReparation;
import metier.model.Reparation;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReparationDao {
    Reparation save(Reparation reparation) throws Exception;
    Optional<Reparation> findById(Long id) throws Exception;
    Optional<Reparation> findByCodeUnique(String codeUnique) throws Exception;
    List<Reparation> findAll() throws Exception;
    List<Reparation> findByReparateurId(Long reparateurId) throws Exception;
    List<Reparation> findByClientPhone(String phone) throws Exception;
    List<Reparation> findByStatut(EtatReparation statut) throws Exception;
    List<Reparation> findByDateCreation(Date date) throws Exception;
    Reparation update(Reparation reparation) throws Exception;
    boolean deleteById(Long id) throws Exception;
}  