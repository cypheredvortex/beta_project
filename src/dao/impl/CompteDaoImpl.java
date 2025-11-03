package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DBConnection;
import dao.interfaces.CompteDao;
import metier.enums.RoleCompte;
import metier.model.Compte;
import metier.model.Reparateur;

public class CompteDaoImpl implements CompteDao {

    @Override
    public Compte save(Compte compte) throws Exception {
        String sql = "INSERT INTO comptes (login, motDePasse, role, actif, reparateur_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, compte.getLogin());
            ps.setString(2, compte.getMotDePasse());
            ps.setString(3, compte.getRole() != null ? compte.getRole().name() : null);
            ps.setBoolean(4, compte.isActif());
            ps.setObject(5, compte.getReparateur() != null ? compte.getReparateur().getId() : null);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) compte.setId(rs.getLong(1));
            }
        }
        return compte;
    }

    @Override
    public Compte update(Compte compte) throws Exception {
        String sql = "UPDATE comptes SET login=?, motDePasse=?, role=?, actif=?, reparateur_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, compte.getLogin());
            ps.setString(2, compte.getMotDePasse());
            ps.setString(3, compte.getRole() != null ? compte.getRole().name() : null);
            ps.setBoolean(4, compte.isActif());
            ps.setObject(5, compte.getReparateur() != null ? compte.getReparateur().getId() : null);
            ps.setLong(6, compte.getId());
            ps.executeUpdate();
        }
        return compte;
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        String sql = "DELETE FROM comptes WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Compte> findById(Long id) throws Exception {
        String sql = "SELECT * FROM comptes WHERE id=?";
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
    public Optional<Compte> findByLogin(String login) throws Exception {
        String sql = "SELECT * FROM comptes WHERE login=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Compte> findAll() throws Exception {
        List<Compte> list = new ArrayList<>();
        String sql = "SELECT * FROM comptes";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public List<Compte> findByRole(RoleCompte role) throws Exception {
        List<Compte> list = new ArrayList<>();
        String sql = "SELECT * FROM comptes WHERE role=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role != null ? role.name() : null);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<Compte> findByActif(boolean actif) throws Exception {
        List<Compte> list = new ArrayList<>();
        String sql = "SELECT * FROM comptes WHERE actif=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, actif);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Compte> findByReparateurId(Long reparateurId) throws Exception {
        String sql = "SELECT * FROM comptes WHERE reparateur_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reparateurId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    private Compte mapRow(ResultSet rs) throws SQLException {
        Compte c = new Compte();
        c.setId(rs.getLong("id"));
        c.setLogin(rs.getString("login"));
        c.setMotDePasse(rs.getString("motDePasse"));

        String roleStr = rs.getString("role");
        if (roleStr != null) {
            try {
                c.setRole(RoleCompte.valueOf(roleStr));
            } catch (IllegalArgumentException e) {
                c.setRole(null);
            }
        }

        c.setActif(rs.getBoolean("actif"));

        long repId = rs.getLong("reparateur_id");
        if (!rs.wasNull()) {
            Reparateur r = new Reparateur();
            r.setId(repId);
            c.setReparateur(r);
        }

        return c;
    }
}
