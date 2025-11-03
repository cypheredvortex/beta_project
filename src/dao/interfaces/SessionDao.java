package dao.interfaces;

import metier.model.Session;
import java.util.List;
import java.util.Optional;

public interface SessionDao {
    Session save(Session session) throws Exception;
    Optional<Session> findById(long id) throws Exception;
    Optional<Session> findByToken(String token) throws Exception;
    List<Session> findByCompteId(long compteId) throws Exception;
    List<Session> findAll() throws Exception;
    Session update(Session session) throws Exception;
    boolean deleteById(long id) throws Exception;
}
