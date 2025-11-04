package dao.interfaces;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import metier.model.Photo;

public interface PhotoDao {
    Photo save(Photo photo) throws Exception;
    Optional<Photo> findById(Long id) throws Exception;
    List<Photo> findAll() throws Exception;
    Photo update(Photo photo) throws Exception;
    boolean deleteById(Long id) throws Exception;
    List<Photo> findByClientId(Long clientId) throws Exception;
    List<Photo> findByDatePrise(Date datePrise) throws Exception;
    List<Photo> findBySourceType(String sourceType) throws Exception;
}