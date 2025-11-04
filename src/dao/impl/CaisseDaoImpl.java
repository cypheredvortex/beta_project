package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DBConnection;
import dao.interfaces.CaisseDao;
import metier.enums.TypeCaisse;
import metier.model.Caisse;
import metier.model.Reparateur;

public class CaisseDaoImpl implements CaisseDao {

    @Override
    public Caisse save(Caisse caisse) throws Exception {
        String sql = "INSERT INTO caisse (soldeActuel, type, reparateur_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, caisse.getSoldeActuel());
            ps.setString(2, caisse.getType() != null ? caisse.getType().name() : null);
            ps.setObject(3, caisse.getReparateur() != null ? caisse.getReparateur().getId() : null);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) caisse.setId(keys.getLong(1));
            }
        }
        return caisse;
    }

    @Override
    public Caisse update(Caisse caisse) throws Exception {
        String sql = "UPDATE caisse SET soldeActuel=?, type=?, reparateur_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, caisse.getSoldeActuel());
            ps.setString(2, caisse.getType() != null ? caisse.getType().name() : null);
            ps.setObject(3, caisse.getReparateur() != null ? caisse.getReparateur().getId() : null);
            ps.setLong(4, caisse.getId());
            ps.executeUpdate();
        }
        return caisse;
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        String sql = "DELETE FROM caisse WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Caisse> findById(Long id) throws Exception {
        String sql = "SELECT * FROM caisse WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapCaisse(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Caisse> findByReparateurId(Long reparateurId) throws Exception {
        String sql = "SELECT * FROM caisse WHERE reparateur_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reparateurId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapCaisse(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Caisse> findAll() throws Exception {
        String sql = "SELECT * FROM caisse";
        List<Caisse> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapCaisse(rs));
        }
        return list;
    }

    @Override
    public List<Caisse> findByType(TypeCaisse type) throws Exception {
        String sql = "SELECT * FROM caisse WHERE type=?";
        List<Caisse> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type != null ? type.name() : null);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapCaisse(rs));
            }
        }
        return list;
    }

    private Caisse mapCaisse(ResultSet rs) throws SQLException {
        Caisse c = new Caisse();
        c.setId(rs.getLong("id"));
        c.setSoldeActuel(rs.getDouble("soldeActuel"));
        String typeStr = rs.getString("type");
        if (typeStr != null) {
            try {
                c.setType(TypeCaisse.valueOf(typeStr));
            } catch (IllegalArgumentException e) {
                c.setType(null);
            }
        }
        long repId = rs.getLong("reparateur_id");
        if (!rs.wasNull()) {
            Reparateur r = new Reparateur();
            r.setId(repId);
            c.setReparateur(r);
        }
        return c;
    }
}
