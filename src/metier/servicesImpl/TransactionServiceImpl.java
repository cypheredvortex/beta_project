package metier.servicesImpl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import dao.interfaces.TransactionDao;
import dao.impl.TransactionDaoImpl;
import metier.enums.TypeTransaction;
import metier.model.Transaction;
import metier.services.ITransactionService;

public class TransactionServiceImpl implements ITransactionService {

    private final TransactionDao transactionDao;

    public TransactionServiceImpl() {
        this.transactionDao = new TransactionDaoImpl();
    }

    // =========================================================
    // 🔹 Enregistrer une nouvelle transaction
    // =========================================================
    @Override
    public Transaction enregistrerTransaction(Transaction transaction) throws Exception {
        if (transaction == null)
            throw new IllegalArgumentException("La transaction ne peut pas être nulle !");
        if (transaction.getMontant() == 0)
            throw new IllegalArgumentException("Le montant doit être supérieur à 0 !");
        if (transaction.getType() == null)
            throw new IllegalArgumentException("Le type de transaction est obligatoire !");
        return transactionDao.save(transaction);
    }

    // =========================================================
    // 🔹 Modifier une transaction existante
    // =========================================================
    @Override
    public Transaction modifierTransaction(Transaction transaction) throws Exception {
        if (transaction == null || transaction.getId() <= 0)
            throw new IllegalArgumentException("Transaction invalide !");
        return transactionDao.update(transaction);
    }

    // =========================================================
    // 🔹 Supprimer une transaction
    // =========================================================
    @Override
    public boolean supprimerTransaction(Long id) throws Exception {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID invalide !");
        return transactionDao.deleteById(id);
    }

    // =========================================================
    // 🔹 Rechercher une transaction par ID
    // =========================================================
    @Override
    public Optional<Transaction> trouverParId(Long id) throws Exception {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID invalide !");
        return transactionDao.findById(id);
    }

    // =========================================================
    // 🔹 Lister toutes les transactions
    // =========================================================
    @Override
    public List<Transaction> listerTransactions() throws Exception {
        return transactionDao.findAll();
    }

    // =========================================================
    // 🔹 Lister les transactions par caisse
    // =========================================================
    @Override
    public List<Transaction> listerParCaisse(Long caisseId) throws Exception {
        if (caisseId == null || caisseId <= 0)
            throw new IllegalArgumentException("ID de caisse invalide !");
        return transactionDao.findByCaisseId(caisseId);
    }

    // =========================================================
    // 🔹 Lister les transactions par type (ENTREE / SORTIE)
    // =========================================================
    @Override
    public List<Transaction> listerParType(TypeTransaction type) throws Exception {
        if (type == null)
            throw new IllegalArgumentException("Le type ne peut pas être nul !");
        return transactionDao.findByType(type);
    }

    // =========================================================
    // 🔹 Lister les transactions par date
    // =========================================================
    @Override
    public List<Transaction> listerParDate(Date date) throws Exception {
        if (date == null)
            throw new IllegalArgumentException("La date ne peut pas être nulle !");
        return transactionDao.findByDate(date);
    }

    // =========================================================
    // 🔹 Calculer le total selon le type de transaction
    // =========================================================
    @Override
    public double calculerTotalParType(TypeTransaction type) throws Exception {
        if (type == null)
            throw new IllegalArgumentException("Le type ne peut pas être nul !");
        List<Transaction> transactions = transactionDao.findByType(type);

        double total = 0;
        for (Transaction t : transactions) {
            total += t.getMontant();
        }
        return total;
    }
}
