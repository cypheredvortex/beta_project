package dao.interfaces;

import metier.model.Boutique;
import java.util.List;
import java.util.Optional;

public interface BoutiqueDao {
    Boutique save(Boutique boutique) throws Exception;
    Optional<Boutique> findById(int id) throws Exception;
    List<Boutique> findAll() throws Exception;
    List<Boutique> findByProprietaireId(long proprietaireId) throws Exception;
    Boutique update(Boutique boutique) throws Exception;
    boolean deleteById(int id) throws Exception;
}
