package dao.interfaces;

import metier.model.Reparateur;
import java.util.List;
import java.util.Optional;

public interface ReparateurDao {
    Reparateur save(Reparateur reparateur) throws Exception;
    Optional<Reparateur> findById(Long id) throws Exception;
    List<Reparateur> findAll() throws Exception;
    Reparateur update(Reparateur reparateur) throws Exception;
    boolean deleteById(Long id) throws Exception;
    Optional<Reparateur> findByEmail(String email) throws Exception;
    Optional<Reparateur> findByTelephone(String telephone) throws Exception;
    List<Reparateur> findByNom(String nom) throws Exception;
}
