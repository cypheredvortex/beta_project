package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DBConnection;
import dao.interfaces.TransactionDao;
import metier.enums.TypeTransaction;
import metier.model.Caisse;
import metier.model.Transaction;

public class TransactionDaoImpl implements TransactionDao {

    @Override
    public Transaction save(Transaction transaction) throws Exception {
        String sql = "INSERT INTO transaction (date, montant, type, description, contrepartie, caisse_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, transaction.getDate() != null ? new Timestamp(transaction.getDate().getTime()) : null);
            ps.setDouble(2, transaction.getMontant());
            ps.setString(3, transaction.getType() != null ? transaction.getType().name() : null);
            ps.setString(4, transaction.getDescription());
            ps.setString(5, transaction.getContrepartie());
            ps.setObject(6, transaction.getCaisse() != null ? transaction.getCaisse().getId() : null);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) transaction.setId(keys.getLong(1));
            }
        }
        return transaction;
    }

    @Override
    public Transaction update(Transaction transaction) throws Exception {
        String sql = "UPDATE transaction SET date=?, montant=?, type=?, description=?, contrepartie=?, caisse_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, transaction.getDate() != null ? new Timestamp(transaction.getDate().getTime()) : null);
            ps.setDouble(2, transaction.getMontant());
            ps.setString(3, transaction.getType() != null ? transaction.getType().name() : null);
            ps.setString(4, transaction.getDescription());
            ps.setString(5, transaction.getContrepartie());
            ps.setObject(6, transaction.getCaisse() != null ? transaction.getCaisse().getId() : null);
            ps.setLong(7, transaction.getId());
            ps.executeUpdate();
        }
        return transaction;
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        String sql = "DELETE FROM transaction WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Transaction> findById(Long id) throws Exception {
        String sql = "SELECT * FROM transaction WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapTransaction(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() throws Exception {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapTransaction(rs));
        }
        return list;
    }

    @Override
    public List<Transaction> findByCaisseId(Long caisseId) throws Exception {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE caisse_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, caisseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapTransaction(rs));
            }
        }
        return list;
    }

    @Override
    public List<Transaction> findByType(TypeTransaction type) throws Exception {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE type=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapTransaction(rs));
            }
        }
        return list;
    }

    @Override
    public List<Transaction> findByDate(java.sql.Date date) throws Exception {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE date=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapTransaction(rs));
            }
        }
        return list;
    }

    private Transaction mapTransaction(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setId(rs.getLong("id"));
        t.setDate(rs.getTimestamp("date"));
        t.setMontant(rs.getDouble("montant"));
        String type = rs.getString("type");
        if (type != null) t.setType(TypeTransaction.valueOf(type));
        t.setDescription(rs.getString("description"));
        t.setContrepartie(rs.getString("contrepartie"));
        long caisseId = rs.getLong("caisse_id");
        if (!rs.wasNull()) t.setCaisse(new Caisse(caisseId, caisseId, null, null, null, null));
        return t;
    }
}
