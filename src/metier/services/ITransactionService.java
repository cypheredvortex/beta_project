package metier.services;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import metier.enums.TypeTransaction;
import metier.model.Transaction;

public interface ITransactionService {
    Transaction enregistrerTransaction(Transaction transaction) throws Exception;
    Transaction modifierTransaction(Transaction transaction) throws Exception;
    boolean supprimerTransaction(Long id) throws Exception;
    Optional<Transaction> trouverParId(Long id) throws Exception;
    List<Transaction> listerTransactions() throws Exception;
    List<Transaction> listerParCaisse(Long caisseId) throws Exception;
    List<Transaction> listerParType(TypeTransaction type) throws Exception;
    List<Transaction> listerParDate(Date date) throws Exception;
    double calculerTotalParType(TypeTransaction type) throws Exception;
}
	