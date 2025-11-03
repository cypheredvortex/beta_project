package dao.interfaces;

import metier.model.Client;
import java.util.List;
import java.util.Optional;

public interface ClientDao {
    Client save(Client client) throws Exception;
    Optional<Client> findById(int id) throws Exception;
    Optional<Client> findByTelephone(String telephone) throws Exception;
    Optional<Client> findByEmail(String email) throws Exception;
    Optional<Client> findByPhotoId(int id) throws Exception;
    List<Client> findAll() throws Exception;
    Client update(Client client) throws Exception;
    boolean deleteById(int id) throws Exception;
}
