package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DBConnection;
import dao.interfaces.ProprietaireDao;
import metier.model.Proprietaire;
import metier.model.Boutique;

public class ProprietaireDaoImpl implements ProprietaireDao {

    @Override
    public Proprietaire save(Proprietaire p) throws Exception {
        String sql = "INSERT INTO proprietaires (nom, prenom, telephone, email, boutique_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setString(3, p.getTelephone());
            ps.setString(4, p.getEmail());
            ps.setObject(5, p.getBoutique() != null ? p.getBoutique().getId() : null);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getLong(1));
            }
        }
        return p;
    }

    @Override
    public Proprietaire update(Proprietaire p) throws Exception {
        String sql = "UPDATE proprietaires SET nom=?, prenom=?, telephone=?, email=?, boutique_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setString(3, p.getTelephone());
            ps.setString(4, p.getEmail());
            ps.setObject(5, p.getBoutique() != null ? p.getBoutique().getId() : null);
            ps.setLong(6, p.getId());
            ps.executeUpdate();
        }
        return p;
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        String sql = "DELETE FROM proprietaires WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Proprietaire> findById(Long id) throws Exception {
        String sql = "SELECT * FROM proprietaires WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Proprietaire> findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM proprietaires WHERE email=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Proprietaire> findByTelephone(String telephone) throws Exception {
        String sql = "SELECT * FROM proprietaires WHERE telephone=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, telephone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Proprietaire> findByBoutiqueId(Long boutiqueId) throws Exception {
        List<Proprietaire> list = new ArrayList<>();
        String sql = "SELECT * FROM proprietaires WHERE boutique_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, boutiqueId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<Proprietaire> findAll() throws Exception {
        List<Proprietaire> list = new ArrayList<>();
        String sql = "SELECT * FROM proprietaires";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Proprietaire mapRow(ResultSet rs) throws SQLException {
        Proprietaire p = new Proprietaire();
        p.setId(rs.getLong("id"));
        p.setNom(rs.getString("nom"));
        p.setPrenom(rs.getString("prenom"));
        p.setTelephone(rs.getString("telephone"));
        p.setEmail(rs.getString("email"));

        long boutiqueId = rs.getLong("boutique_id");
        if (!rs.wasNull()) {
            Boutique b = new Boutique();
            b.setId(boutiqueId);
            p.setBoutique(b);
        }

        return p;
    }
}
