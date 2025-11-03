package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DBConnection;
import dao.interfaces.DetteDao;
import metier.model.Dette;

public class DetteDaoImpl implements DetteDao {

    @Override
    public Dette save(Dette dette) throws Exception {
        String sql = "INSERT INTO dette (montant, donne_par, recu_par, date, reglee, caisse_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, dette.getMontant());
            ps.setString(2, dette.getDonnepar());
            ps.setString(3, dette.getRecuPar());
            ps.setDate(4, dette.getDate());
            ps.setBoolean(5, dette.isReglee());
            ps.setObject(6, dette.getCaisse() != null ? dette.getCaisse().getId() : null);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) dette.setId(keys.getLong(1));
            }
        }
        return dette;
    }

    @Override
    public Dette update(Dette dette) throws Exception {
        String sql = "UPDATE dette SET montant=?, donne_par=?, recu_par=?, date=?, reglee=?, caisse_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, dette.getMontant());
            ps.setString(2, dette.getDonnepar());
            ps.setString(3, dette.getRecuPar());
            ps.setDate(4, dette.getDate());
            ps.setBoolean(5, dette.isReglee());
            ps.setObject(6, dette.getCaisse() != null ? dette.getCaisse().getId() : null);
            ps.setLong(7, dette.getId());
            ps.executeUpdate();
        }
        return dette;
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        String sql = "DELETE FROM dette WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Dette> findById(Long id) throws Exception {
        String sql = "SELECT * FROM dette WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapDette(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Dette> findAll() throws Exception {
        List<Dette> list = new ArrayList<>();
        String sql = "SELECT * FROM dette";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapDette(rs));
        }
        return list;
    }

    @Override
    public List<Dette> findByCaisseId(Long caisseId) throws Exception {
        List<Dette> list = new ArrayList<>();
        String sql = "SELECT * FROM dette WHERE caisse_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, caisseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapDette(rs));
            }
        }
        return list;
    }

    @Override
    public List<Dette> findByDonnePar(String donnePar) throws Exception {
        List<Dette> list = new ArrayList<>();
        String sql = "SELECT * FROM dette WHERE donne_par=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, donnePar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapDette(rs));
            }
        }
        return list;
    }

    @Override
    public List<Dette> findByRecuPar(String recuPar) throws Exception {
        List<Dette> list = new ArrayList<>();
        String sql = "SELECT * FROM dette WHERE recu_par=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recuPar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapDette(rs));
            }
        }
        return list;
    }

    @Override
    public List<Dette> findByDate(Date date) throws Exception {
        List<Dette> list = new ArrayList<>();
        String sql = "SELECT * FROM dette WHERE date=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapDette(rs));
            }
        }
        return list;
    }

    @Override
    public List<Dette> findByReglee(boolean reglee) throws Exception {
        List<Dette> list = new ArrayList<>();
        String sql = "SELECT * FROM dette WHERE reglee=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, reglee);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapDette(rs));
            }
        }
        return list;
    }

    private Dette mapDette(ResultSet rs) throws SQLException {
        Dette d = new Dette();
        d.setId(rs.getLong("id"));
        d.setMontant(rs.getDouble("montant"));
        d.setDonnepar(rs.getString("donne_par"));
        d.setRecuPar(rs.getString("recu_par"));
        d.setDate(rs.getDate("date"));
        d.setReglee(rs.getBoolean("reglee"));
        long caisseId = rs.getLong("caisse_id");
        if (!rs.wasNull()) {
            metier.model.Caisse c = new metier.model.Caisse();
            c.setId(caisseId);
            d.setCaisse(c);
        }
        return d;
    }
}
