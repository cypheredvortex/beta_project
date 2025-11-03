package dao.interfaces;

import java.util.List;
import java.util.Optional;

import metier.enums.TypeCaisse;
import metier.model.Caisse;

public interface CaisseDao {
    Caisse save(Caisse caisse) throws Exception;
    Optional<Caisse> findById(Long id) throws Exception;
    Optional<Caisse> findByReparateurId(Long reparateurId) throws Exception;
    List<Caisse> findAll() throws Exception;
    Caisse update(Caisse caisse) throws Exception;
    boolean deleteById(Long id) throws Exception;
    List<Caisse> findByType(TypeCaisse type) throws Exception;
}
