package dao.impl;

import dao.DBConnection;
import dao.interfaces.SessionDao;
import metier.model.Session;
import metier.model.Compte;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionDaoImpl implements SessionDao {

    @Override
    public Session save(Session session) throws Exception {
        String sql = "INSERT INTO session (token, dateCreation, dateExpiration, compte_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, session.getToken());
            ps.setDate(2, session.getDateCreation());
            ps.setDate(3, session.getDateExpiration());
            ps.setObject(4, session.getCompte() != null ? session.getCompte().getId() : null);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) session.setId(rs.getLong(1));
            }
        }
        return session;
    }

    @Override
    public Optional<Session> findById(long id) throws Exception {
        String sql = "SELECT * FROM session WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapSession(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Session> findByToken(String token) throws Exception {
        String sql = "SELECT * FROM session WHERE token = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapSession(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Session> findByCompteId(long compteId) throws Exception {
        List<Session> list = new ArrayList<>();
        String sql = "SELECT * FROM session WHERE compte_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, compteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapSession(rs));
            }
        }
        return list;
    }

    @Override
    public List<Session> findAll() throws Exception {
        List<Session> list = new ArrayList<>();
        String sql = "SELECT * FROM session";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapSession(rs));
        }
        return list;
    }

    @Override
    public Session update(Session session) throws Exception {
        String sql = "UPDATE session SET token=?, dateCreation=?, dateExpiration=?, compte_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, session.getToken());
            ps.setDate(2, session.getDateCreation());
            ps.setDate(3, session.getDateExpiration());
            ps.setObject(4, session.getCompte() != null ? session.getCompte().getId() : null);
            ps.setLong(5, session.getId());

            ps.executeUpdate();
        }
        return session;
    }

    @Override
    public boolean deleteById(long id) throws Exception {
        String sql = "DELETE FROM session WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Session mapSession(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setId(rs.getLong("id"));
        session.setToken(rs.getString("token"));
        session.setDateCreation(rs.getDate("dateCreation"));
        session.setDateExpiration(rs.getDate("dateExpiration"));

        long compteId = rs.getLong("compte_id");
        if (!rs.wasNull()) {
            session.setCompte(new Compte());
            session.getCompte().setId(compteId);
        }

        return session;
    }
}
