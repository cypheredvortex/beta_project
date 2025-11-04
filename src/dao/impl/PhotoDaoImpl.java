package dao.impl;

import dao.DBConnection;
import dao.interfaces.PhotoDao;
import metier.enums.SourcePhoto;
import metier.model.Photo;
import metier.model.Reparation;
import metier.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhotoDaoImpl implements PhotoDao {

    @Override
    public Photo save(Photo photo) throws Exception {
        String sql = "INSERT INTO photo (chemin, datePrise, source, client_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, photo.getChemin());
            ps.setDate(2, photo.getDatePrise());
            ps.setString(3, photo.getSource() != null ? photo.getSource().name() : null);
            ps.setObject(4, photo.getClient() != null ? photo.getClient().getId() : null);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) photo.setId(rs.getLong(1));
            }
        }
        return photo;
    }

    @Override
    public Optional<Photo> findById(Long id) throws Exception {
        String sql = "SELECT * FROM photo WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapPhoto(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Photo> findByClientId(Long clientId) throws Exception {
        List<Photo> list = new ArrayList<>();
        String sql = "SELECT * FROM photo WHERE client_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapPhoto(rs));
            }
        }
        return list;
    }

    @Override
    public List<Photo> findByDatePrise(Date datePrise) throws Exception {
        List<Photo> list = new ArrayList<>();
        String sql = "SELECT * FROM photo WHERE datePrise = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, datePrise);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapPhoto(rs));
            }
        }
        return list;
    }

    @Override
    public List<Photo> findBySourceType(String sourceType) throws Exception {
        List<Photo> list = new ArrayList<>();
        String sql = "SELECT * FROM photo WHERE source = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sourceType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapPhoto(rs));
            }
        }
        return list;
    }

    @Override
    public List<Photo> findAll() throws Exception {
        List<Photo> list = new ArrayList<>();
        String sql = "SELECT * FROM photo";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapPhoto(rs));
        }
        return list;
    }

    @Override
    public Photo update(Photo photo) throws Exception {
        String sql = "UPDATE photo SET chemin=?, datePrise=?, source=?, client_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, photo.getChemin());
            ps.setDate(2, photo.getDatePrise());
            ps.setString(3, photo.getSource() != null ? photo.getSource().name() : null);
            ps.setObject(4, photo.getClient() != null ? photo.getClient().getId() : null);
            ps.setLong(5, photo.getId());

            ps.executeUpdate();
        }
        return photo;
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        String sql = "DELETE FROM photo WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Photo mapPhoto(ResultSet rs) throws SQLException {
        Photo photo = new Photo();
        photo.setId(rs.getLong("id"));
        photo.setChemin(rs.getString("chemin"));
        photo.setDatePrise(rs.getDate("datePrise"));

        String source = rs.getString("source");
        if (source != null) photo.setSource(SourcePhoto.valueOf(source.toUpperCase()));

        long clientId = rs.getLong("client_id");
        if (!rs.wasNull()) {
            photo.setClient(new Client());
            photo.getClient().setId(clientId);
        }

        return photo;
    }
}
