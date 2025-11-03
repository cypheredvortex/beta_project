package dao.interfaces;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import metier.model.Dette;

public interface DetteDao {
    Dette save(Dette dette) throws Exception;
    Optional<Dette> findById(Long id) throws Exception;
    List<Dette> findByCaisseId(Long caisseId) throws Exception;
    List<Dette> findAll() throws Exception;
    Dette update(Dette dette) throws Exception;
    boolean deleteById(Long id) throws Exception;
    List<Dette> findByDonnePar(String donnepar) throws Exception;
    List<Dette> findByRecuPar(String recuPar) throws Exception;
    List<Dette> findByDate(Date date) throws Exception;
    List<Dette> findByReglee(boolean reglee) throws Exception;
}