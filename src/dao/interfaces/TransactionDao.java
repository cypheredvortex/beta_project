package dao.interfaces;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import metier.enums.TypeTransaction;
import metier.model.Transaction;

public interface TransactionDao {
    Transaction save(Transaction transaction) throws Exception;
    Optional<Transaction> findById(Long id) throws Exception;
    List<Transaction> findByCaisseId(Long caisseId) throws Exception;
    List<Transaction> findByType(TypeTransaction type) throws Exception;
    List<Transaction> findByDate(Date date) throws Exception;
    List<Transaction> findAll() throws Exception;
    Transaction update(Transaction transaction) throws Exception;
    boolean deleteById(Long id) throws Exception;
}