package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DBConnection;
import dao.interfaces.AppareilDao;
import metier.model.Appareil;
import metier.model.Reparation;
import metier.enums.TypeProbleme;

public class AppareilDaoImpl implements AppareilDao {

    @Override
    public Appareil save(Appareil appareil) throws Exception {
        String sql = "INSERT INTO appareils (type, marque, modele, numero_serie, probleme, type_probleme, reparation_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, appareil.getType());
            ps.setString(2, appareil.getMarque());
            ps.setString(3, appareil.getModele());
            ps.setString(4, appareil.getNumeroSerie());
            ps.setString(5, appareil.getProbleme());
            ps.setString(6, appareil.getTypeProbleme() != null ? appareil.getTypeProbleme().name() : null);
            ps.setObject(7, appareil.getReparation() != null ? appareil.getReparation().getId() : null);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) appareil.setId(rs.getLong(1));
            }
            return appareil;
        }
    }

    @Override
    public Appareil update(Appareil appareil) throws Exception {
        String sql = "UPDATE appareils SET type=?, marque=?, modele=?, numero_serie=?, probleme=?, type_probleme=?, reparation_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, appareil.getType());
            ps.setString(2, appareil.getMarque());
            ps.setString(3, appareil.getModele());
            ps.setString(4, appareil.getNumeroSerie());
            ps.setString(5, appareil.getProbleme());
            ps.setString(6, appareil.getTypeProbleme() != null ? appareil.getTypeProbleme().name() : null);
            ps.setObject(7, appareil.getReparation() != null ? appareil.getReparation().getId() : null);
            ps.setLong(8, appareil.getId());
            ps.executeUpdate();
            return appareil;
        }
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        String sql = "DELETE FROM appareils WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Appareil> findById(Long id) throws Exception {
        String sql = "SELECT * FROM appareils WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<Appareil> findByNumeroSerie(String numeroSerie) throws Exception {
        String sql = "SELECT * FROM appareils WHERE numero_serie=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeroSerie);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Appareil> findByType(String type) throws Exception {
        String sql = "SELECT * FROM appareils WHERE type=?";
        List<Appareil> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<Appareil> findAll() throws Exception {
        String sql = "SELECT * FROM appareils";
        List<Appareil> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public List<Appareil> findByReparationId(Long reparationId) throws Exception {
        String sql = "SELECT * FROM appareils WHERE reparation_id=?";
        List<Appareil> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reparationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    private Appareil mapRow(ResultSet rs) throws SQLException {
        Appareil a = new Appareil();
        a.setId(rs.getLong("id"));
        a.setType(rs.getString("type"));
        a.setMarque(rs.getString("marque"));
        a.setModele(rs.getString("modele"));
        a.setNumeroSerie(rs.getString("numero_serie"));
        a.setProbleme(rs.getString("probleme"));

        String tp = rs.getString("type_probleme");
        if (tp != null) {
            try { a.setTypeProbleme(TypeProbleme.valueOf(tp)); } catch (Exception e) {}
        }

        long repId = rs.getLong("reparation_id");
        if (!rs.wasNull()) {
            Reparation r = new Reparation();
            r.setId(repId);
            a.setReparation(r);
        }

        return a;
    }
}
