package dao.interfaces;

import java.util.List;
import java.util.Optional;

import metier.model.Appareil;

public interface AppareilDao {
	Appareil save(Appareil appareil) throws Exception;
    Optional<Appareil> findById(Long id) throws Exception;
    List<Appareil> findAll() throws Exception;
    List<Appareil> findByReparationId(Long reparationId) throws Exception;
    Appareil update(Appareil appareil) throws Exception;
    boolean deleteById(Long id) throws Exception;
    Optional<Appareil> findByNumeroSerie(String numeroSerie) throws Exception;
    List<Appareil> findByType(String type) throws Exception;
}

