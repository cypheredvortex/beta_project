package dao.impl;

import dao.DBConnection;
import dao.interfaces.ClientDao;
import metier.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDaoImpl implements ClientDao {

    @Override
    public Client save(Client client) throws Exception {
        String sql = "INSERT INTO client (nom, prenom, email, telephone, photo_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getTelephone());
            ps.setObject(5, client.getPhoto() != null ? client.getPhoto().getId() : null);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) client.setId(rs.getLong(1));
            }
        }
        return client;
    }

    @Override
    public Optional<Client> findById(int id) throws Exception {
        String sql = "SELECT * FROM client WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapClient(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByTelephone(String telephone) throws Exception {
        String sql = "SELECT * FROM client WHERE telephone = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, telephone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapClient(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM client WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapClient(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByPhotoId(int id) throws Exception {
        String sql = "SELECT * FROM client WHERE photo_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapClient(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() throws Exception {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) clients.add(mapClient(rs));
        }
        return clients;
    }

    @Override
    public Client update(Client client) throws Exception {
        String sql = "UPDATE client SET nom=?, prenom=?, email=?, telephone=?, photo_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getTelephone());
            ps.setObject(5, client.getPhoto() != null ? client.getPhoto().getId() : null);
            ps.setLong(6, client.getId());

            ps.executeUpdate();
        }
        return client;
    }

    @Override
    public boolean deleteById(int id) throws Exception {
        String sql = "DELETE FROM client WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Client mapClient(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setId(rs.getLong("id"));
        c.setNom(rs.getString("nom"));
        c.setPrenom(rs.getString("prenom"));
        c.setEmail(rs.getString("email"));
        c.setTelephone(rs.getString("telephone"));

        long photoId = rs.getLong("photo_id");
        if (!rs.wasNull()) {
            try {
                // Fetch the full Photo object from DB
                PhotoDaoImpl photoDao = new PhotoDaoImpl();
                c.setPhoto(photoDao.findById(photoId).orElse(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return c;
    }

}
