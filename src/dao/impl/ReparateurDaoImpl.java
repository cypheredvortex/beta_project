package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DBConnection;
import dao.interfaces.ReparateurDao;
import metier.model.Boutique;
import metier.model.Compte;
import metier.model.Reparateur;

public class ReparateurDaoImpl implements ReparateurDao {

    @Override
    public Reparateur save(Reparateur r) throws Exception {
        String sql = "INSERT INTO reparateur (nom, prenom, telephone, email, salairePourcentage, compte_id, boutique_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getNom());
            ps.setString(2, r.getPrenom());
            ps.setString(3, r.getTelephone());
            ps.setString(4, r.getEmail());
            ps.setDouble(5, r.getSalairePourcentage());
            ps.setObject(6, r.getCompte() != null ? r.getCompte().getId() : null);
            ps.setObject(7, r.getBoutique() != null ? r.getBoutique().getId() : null);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) r.setId(rs.getLong(1));
            }
        }
        return r;
    }

    @Override
    public Reparateur update(Reparateur r) throws Exception {
        String sql = "UPDATE reparateur SET nom=?, prenom=?, telephone=?, email=?, salairePourcentage=?, compte_id=?, boutique_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getNom());
            ps.setString(2, r.getPrenom());
            ps.setString(3, r.getTelephone());
            ps.setString(4, r.getEmail());
            ps.setDouble(5, r.getSalairePourcentage());
            ps.setObject(6, r.getCompte() != null ? r.getCompte().getId() : null);
            ps.setObject(7, r.getBoutique() != null ? r.getBoutique().getId() : null);
            ps.setLong(8, r.getId());
            ps.executeUpdate();
        }
        return r;
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        String sql = "DELETE FROM reparateur WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Reparateur> findById(Long id) throws Exception {
        String sql = "SELECT * FROM reparateur WHERE id=?";
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
    public Optional<Reparateur> findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM reparateur WHERE email=?";
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
    public Optional<Reparateur> findByTelephone(String telephone) throws Exception {
        String sql = "SELECT * FROM reparateur WHERE telephone=?";
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
    public List<Reparateur> findByNom(String nom) throws Exception {
        List<Reparateur> list = new ArrayList<>();
        String sql = "SELECT * FROM reparateur WHERE nom=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<Reparateur> findAll() throws Exception {
        List<Reparateur> list = new ArrayList<>();
        String sql = "SELECT * FROM reparateur";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Reparateur mapRow(ResultSet rs) throws SQLException {
        Reparateur r = new Reparateur();
        r.setId(rs.getLong("id"));
        r.setNom(rs.getString("nom"));
        r.setPrenom(rs.getString("prenom"));
        r.setTelephone(rs.getString("telephone"));
        r.setEmail(rs.getString("email"));
        r.setSalairePourcentage(rs.getDouble("salairePourcentage"));

        long compteId = rs.getLong("compte_id");
        if (!rs.wasNull()) {
            Compte c = new Compte();
            c.setId(compteId);
            r.setCompte(c);
        }

        long boutiqueId = rs.getLong("boutique_id");
        if (!rs.wasNull()) {
            Boutique b = new Boutique();
            b.setId(boutiqueId);
            r.setBoutique(b);
        }

        return r;
    }
}
